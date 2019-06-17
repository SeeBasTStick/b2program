package de.hhu.stups.codegenerator.generators.iteration;

import de.hhu.stups.codegenerator.generators.MachineGenerator;
import de.hhu.stups.codegenerator.generators.TypeGenerator;
import de.hhu.stups.codegenerator.handlers.IterationConstructHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.LetExpressionNode;
import de.prob.parser.ast.nodes.predicate.LetPredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Collection;
import java.util.List;

/**
 * Created by fabian on 30.04.19.
 */
public class LetExpressionPredicateGenerator {

    private final STGroup group;

    private final MachineGenerator machineGenerator;

    private final TypeGenerator typeGenerator;

    private final IterationConstructGenerator iterationConstructGenerator;

    private final IterationConstructHandler iterationConstructHandler;

    private final IterationPredicateGenerator iterationPredicateGenerator;


    public LetExpressionPredicateGenerator(final STGroup group, final MachineGenerator machineGenerator, final TypeGenerator typeGenerator, final IterationConstructGenerator iterationConstructGenerator,
                                           final IterationConstructHandler iterationConstructHandler, final IterationPredicateGenerator iterationPredicateGenerator) {
        this.group = group;
        this.machineGenerator = machineGenerator;
        this.typeGenerator = typeGenerator;
        this.iterationConstructGenerator = iterationConstructGenerator;
        this.iterationConstructHandler = iterationConstructHandler;
        this.iterationPredicateGenerator = iterationPredicateGenerator;
    }

    /*
    * This function generates code for a let expression from the belonging AST node
    */
    public String generateLetExpression(LetExpressionNode node) {
        machineGenerator.inIterationConstruct();
        BType type = node.getType();
        PredicateNode predicate = node.getPredicate();
        ExprNode expression = node.getExpression();
        List<DeclarationNode> declarations = node.getLocalIdentifiers();

        ST template = group.getInstanceOf("let_expression_predicate");

        iterationConstructGenerator.prepareGeneration(predicate, declarations);
        List<ST> enumerationTemplates = iterationPredicateGenerator.getEnumerationTemplates(iterationConstructGenerator, declarations, predicate);
        Collection<String> otherConstructs = generateOtherIterationConstructs(predicate);

        int iterationConstructCounter = iterationConstructHandler.getIterationConstructCounter();
        String identifier = "_ic_let_"+ iterationConstructCounter;
        generateBody(template, enumerationTemplates, otherConstructs, identifier, type, expression);

        String result = template.render();
        iterationConstructGenerator.addGeneration(node.toString(), identifier, declarations, result);

        machineGenerator.leaveIterationConstruct();
        return result;
    }

    /*
    * This function generates code for a let predicate from the belonging AST node
    */
    public String generateLetPredicate(LetPredicateNode node) {
        machineGenerator.inIterationConstruct();
        BType type = node.getType();
        PredicateNode letPredicate = node.getWherePredicate();
        PredicateNode thenPredicate = node.getPredicate();
        List<DeclarationNode> declarations = node.getLocalIdentifiers();

        ST template = group.getInstanceOf("let_expression_predicate");

        iterationConstructGenerator.prepareGeneration(letPredicate, declarations);
        List<ST> enumerationTemplates = iterationPredicateGenerator.getEnumerationTemplates(iterationConstructGenerator, declarations, letPredicate);
        Collection<String> otherConstructs = generateOtherIterationConstructs(letPredicate);

        int iterationConstructCounter = iterationConstructHandler.getIterationConstructCounter();
        String identifier = "_ic_let_"+ iterationConstructCounter;

        generateBody(template, enumerationTemplates, otherConstructs, identifier, type, thenPredicate);

        String result = template.render();

        iterationConstructGenerator.addGeneration(node.toString(), identifier, declarations, result);

        machineGenerator.leaveIterationConstruct();
        return result;
    }

    /*
    * This function generates code for other iteration constructs used within a let expression or a let predicate
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
    * This function generates code for the inner body of a let expression
    */
    private String generateLetBody(Collection<String> otherConstructs, String identifier, BType type, ExprNode exprNode) {
        ST template = group.getInstanceOf("let_expression_predicate_body");
        TemplateHandler.add(template, "otherIterationConstructs", otherConstructs);
        TemplateHandler.add(template, "identifier", identifier);
        TemplateHandler.add(template, "type", typeGenerator.generate(type));
        TemplateHandler.add(template, "val", machineGenerator.visitExprNode(exprNode, null));
        return template.render();
    }

    /*
    * This function generates code for the inner body of a let predicate
    */
    private String generateLetBody(Collection<String> otherConstructs, String identifier, BType type, PredicateNode thenPredicateNode) {
        ST template = group.getInstanceOf("let_expression_predicate_body");
        TemplateHandler.add(template, "otherIterationConstructs", otherConstructs);
        TemplateHandler.add(template, "identifier", identifier);
        TemplateHandler.add(template, "type", typeGenerator.generate(type));
        TemplateHandler.add(template, "val", machineGenerator.visitPredicateNode(thenPredicateNode, null));
        return template.render();
    }

    /*
    * This function generates code for the body of a let predicate
    */
    private void generateBody(ST template, List<ST> enumerationTemplates, Collection<String> otherConstructs, String identifier, BType type, PredicateNode thenPredicate) {
        iterationConstructHandler.setIterationConstructGenerator(iterationConstructGenerator);
        String innerBody = generateLetBody(otherConstructs, identifier, type, thenPredicate);
        String body = iterationPredicateGenerator.evaluateEnumerationTemplates(enumerationTemplates, innerBody).render();
        TemplateHandler.add(template, "body", body);
    }

    /*
    * This function generates code for the body of a let expression
    */
    private void generateBody(ST template, List<ST> enumerationTemplates, Collection<String> otherConstructs, String identifier, BType type, ExprNode expression) {
        iterationConstructHandler.setIterationConstructGenerator(iterationConstructGenerator);
        String innerBody = generateLetBody(otherConstructs, identifier, type, expression);
        String body = iterationPredicateGenerator.evaluateEnumerationTemplates(enumerationTemplates, innerBody).render();
        TemplateHandler.add(template, "body", body);
    }

}
