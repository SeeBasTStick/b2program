package de.hhu.stups.codegenerator.generators.iteration;

import de.hhu.stups.codegenerator.generators.MachineGenerator;
import de.hhu.stups.codegenerator.generators.TypeGenerator;
import de.hhu.stups.codegenerator.handlers.IterationConstructHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.expression.SetComprehensionNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Collection;
import java.util.List;

/**
 * Created by fabian on 04.03.19.
 */
public class SetComprehensionGenerator {

    private final STGroup group;

    private final MachineGenerator machineGenerator;

    private final IterationConstructGenerator iterationConstructGenerator;

    private final IterationConstructHandler iterationConstructHandler;

    private final IterationPredicateGenerator iterationPredicateGenerator;

    private final TypeGenerator typeGenerator;

    public SetComprehensionGenerator(final STGroup group, final MachineGenerator machineGenerator, final IterationConstructGenerator iterationConstructGenerator,
                                     final IterationConstructHandler iterationConstructHandler, final IterationPredicateGenerator iterationPredicateGenerator,
                                     final TypeGenerator typeGenerator) {
        this.group = group;
        this.machineGenerator = machineGenerator;
        this.iterationConstructGenerator = iterationConstructGenerator;
        this.iterationConstructHandler = iterationConstructHandler;
        this.iterationPredicateGenerator = iterationPredicateGenerator;
        this.typeGenerator = typeGenerator;
    }

    /*
    * This function generates code for a set comprehension from the belonging AST node
    */
    public String generateSetComprehension(SetComprehensionNode node) {
        machineGenerator.inIterationConstruct();
        PredicateNode predicate = node.getPredicateNode();
        List<DeclarationNode> declarations = node.getDeclarationList();
        BType type = node.getType();

        ST template = group.getInstanceOf("set_comprehension");

        iterationConstructGenerator.prepareGeneration(predicate, declarations, type);

        List<ST> enumerationTemplates = iterationPredicateGenerator.getEnumerationTemplates(iterationConstructGenerator, declarations, predicate);
        Collection<String> otherConstructs = generateOtherIterationConstructs(predicate);

        int iterationConstructCounter = iterationConstructHandler.getIterationConstructCounter();
        String identifier = "_ic_set_" + iterationConstructCounter;
        boolean isRelation = node.getDeclarationList().size() > 1;
        generateBody(template, enumerationTemplates, otherConstructs, identifier, isRelation, predicate, declarations, type);

        String result = template.render();
        iterationConstructGenerator.addGeneration(node.toString(), identifier, declarations, result);

        machineGenerator.leaveIterationConstruct();
        return result;
    }

    /*
    * This function generates code for the predicate of a set comprehension
    */
    private String generateSetComprehensionPredicate(Collection<String> otherConstructs, PredicateNode predicateNode, String type, String setName, String elementName, List<DeclarationNode> declarations) {
        PredicateNode subpredicate = iterationPredicateGenerator.subpredicate(predicateNode, declarations.size());
        ST template = group.getInstanceOf("set_comprehension_predicate");
        TemplateHandler.add(template, "otherIterationConstructs", otherConstructs);
        TemplateHandler.add(template, "emptyPredicate", ((PredicateOperatorNode) subpredicate).getPredicateArguments().size() == 0);
        TemplateHandler.add(template, "predicate", machineGenerator.visitPredicateNode(subpredicate, null));
        TemplateHandler.add(template, "type", type);
        TemplateHandler.add(template, "set", setName);
        TemplateHandler.add(template, "isRelation", iterationConstructGenerator.getBoundedVariables().size() > 1);
        generateSubType(template, declarations);
        TemplateHandler.add(template, "element", elementName);
        return template.render();
    }

    /*
    * This function generates code for other iteration constructs within a set comprehension
    */
    private Collection<String> generateOtherIterationConstructs(PredicateNode predicate) {
        IterationConstructGenerator otherConstructsGenerator = iterationConstructHandler.getNewIterationConstructGenerator();
        otherConstructsGenerator.getAllBoundedVariables().addAll(iterationConstructGenerator.getAllBoundedVariables());
        for (String key : iterationConstructGenerator.getIterationsMapIdentifier().keySet()) {
            otherConstructsGenerator.getIterationsMapIdentifier().put(key, iterationConstructGenerator.getIterationsMapIdentifier().get(key));
        }
        iterationConstructHandler.inspectPredicate(otherConstructsGenerator, predicate);
        for (String key : otherConstructsGenerator.getIterationsMapIdentifier().keySet()) {
            iterationConstructGenerator.getIterationsMapIdentifier().put(key, otherConstructsGenerator.getIterationsMapIdentifier().get(key));
        }
        return otherConstructsGenerator.getIterationsMapCode().values();
    }

    /*
    * This function generates code for the body of a set comprehension
    */
    private void generateBody(ST template, List<ST> enumerationTemplates, Collection<String> otherConstructs, String identifier, boolean isRelation, PredicateNode predicate, List<DeclarationNode> declarations, BType type) {
        iterationConstructHandler.setIterationConstructGenerator(iterationConstructGenerator);

        String elementName = getElementFromBoundedVariables(declarations);

        String generatedType = typeGenerator.generate(type);

        String innerBody = generateSetComprehensionPredicate(otherConstructs, predicate, generatedType, identifier, elementName, declarations);
        String comprehension = iterationPredicateGenerator.evaluateEnumerationTemplates(enumerationTemplates, innerBody).render();
        generateSubType(template, declarations);
        TemplateHandler.add(template, "type", generatedType);
        TemplateHandler.add(template, "identifier", identifier);
        TemplateHandler.add(template, "isRelation", isRelation);
        TemplateHandler.add(template, "comprehension", comprehension);
    }

    /*
    * This function generates code for the type of the set comprehension from the given semantic information
    */
    private void generateSubType(ST template, List<DeclarationNode> declarations) {
        if(declarations.size() == 1) {
            DeclarationNode declarationNode = declarations.get(0);
            TemplateHandler.add(template, "subType", typeGenerator.generate(declarationNode.getType()));
        } else {
            DeclarationNode left = declarations.get(0);
            DeclarationNode right = declarations.get(1);
            TemplateHandler.add(template, "leftType", typeGenerator.generate(left.getType()));
            TemplateHandler.add(template, "rightType", typeGenerator.generate(right.getType()));
        }
    }

    /*
    * This function extracts the resulted element of a set comprehension from the given semantic information
    */
    private String getElementFromBoundedVariables(List<DeclarationNode> declarations) {
        if(declarations.size() == 1) {
            return "_ic_" + declarations.get(declarations.size() - 1).getName();
        } else {
            ST firstTuple = group.getInstanceOf("tuple_create");
            DeclarationNode left = declarations.get(0);
            DeclarationNode right = declarations.get(1);
            TemplateHandler.add(firstTuple, "leftType", typeGenerator.generate(left.getType()));
            TemplateHandler.add(firstTuple, "rightType", typeGenerator.generate(right.getType()));
            TemplateHandler.add(firstTuple, "arg1", "_ic_" + left.getName());
            TemplateHandler.add(firstTuple, "arg2", "_ic_" + right.getName());
            return declarations.subList(2, declarations.size()).stream()
                    .map(DeclarationNode::getName)
                    .reduce(firstTuple.render(), (a,e) -> {
                        //TODO: implement type placeholder for C++
                        ST tuple = group.getInstanceOf("tuple_create");
                        TemplateHandler.add(tuple, "arg1", a);
                        TemplateHandler.add(tuple, "arg2", "_ic_" + e);
                        return tuple.render();
                    });
        }
    }

}
