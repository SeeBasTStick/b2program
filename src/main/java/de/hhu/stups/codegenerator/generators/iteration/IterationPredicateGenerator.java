package de.hhu.stups.codegenerator.generators.iteration;

import de.hhu.stups.codegenerator.generators.MachineGenerator;
import de.hhu.stups.codegenerator.generators.TypeGenerator;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabian on 04.03.19.
 */
public class IterationPredicateGenerator {

    private final STGroup group;

    private final MachineGenerator machineGenerator;

    private final TypeGenerator typeGenerator;

    public IterationPredicateGenerator(final STGroup group, final MachineGenerator machineGenerator, final TypeGenerator typeGenerator) {
        this.group = group;
        this.machineGenerator = machineGenerator;
        this.typeGenerator = typeGenerator;
    }

    public ST generateEnumeration(ST template, DeclarationNode declarationNode) {
        TemplateHandler.add(template, "type", typeGenerator.generate(declarationNode.getType()));
        TemplateHandler.add(template, "identifier", "_ic_" + declarationNode.getName());
        return template;
    }

    public void checkPredicate(PredicateNode predicate, List<DeclarationNode> declarations) {
        if(!(predicate instanceof PredicateOperatorNode)) {
            //TODO
            if(declarations.size() != 1) {
                throw new RuntimeException("Predicate for iteration must be a conjunction");
            }
        } else {
            PredicateOperatorNode predicateOperatorNode = ((PredicateOperatorNode) predicate);
            if(predicateOperatorNode.getOperator() != PredicateOperatorNode.PredicateOperator.AND) {
                throw new RuntimeException("Predicate for iteration must be a conjunction");
            } else {
                for(int i = 0; i < declarations.size(); i++) {
                    PredicateNode innerPredicate = predicateOperatorNode.getPredicateArguments().get(i);
                    if(!(innerPredicate instanceof PredicateOperatorWithExprArgsNode)) {
                        throw new RuntimeException("First predicates must declare the set to iterate over");
                    }
                }
            }
        }
    }

    private void checkEnumerationPredicate(ExprNode lhs, DeclarationNode declarationNode) {
        if(!(lhs instanceof IdentifierExprNode) || !(((IdentifierExprNode) lhs).getName().equals(declarationNode.getName()))) {
            throw new RuntimeException("The expression on the left hand side of the first predicates must match the first identifier names");
        }
    }

    private ST getElementOfTemplate(DeclarationNode declarationNode, ExprNode lhs, ExprNode rhs) {
        checkEnumerationPredicate(lhs, declarationNode);
        ST template = group.getInstanceOf("iteration_construct_enumeration");
        ST enumerationTemplate = generateEnumeration(template, declarationNode);
        TemplateHandler.add(enumerationTemplate, "set", machineGenerator.visitExprNode(rhs, null));
        return enumerationTemplate;
    }

    private ST getEqualTemplate(DeclarationNode declarationNode, ExprNode lhs, ExprNode rhs) {
        checkEnumerationPredicate(lhs, declarationNode);
        ST template = group.getInstanceOf("iteration_construct_assignment");
        ST enumerationTemplate = generateEnumeration(template, declarationNode);
        TemplateHandler.add(enumerationTemplate, "expression", machineGenerator.visitExprNode(rhs, null));
        return enumerationTemplate;
    }

    private ST getSubsetTemplate(DeclarationNode declarationNode, ExprNode lhs, ExprNode rhs) {
        checkEnumerationPredicate(lhs, declarationNode);
        ST template = group.getInstanceOf("iteration_construct_subset");
        ST enumerationTemplate = generateEnumeration(template, declarationNode);
        TemplateHandler.add(enumerationTemplate, "set", machineGenerator.visitExprNode(rhs, null));
        return enumerationTemplate;
    }

    private ST getSubsetNeqTemplate(DeclarationNode declarationNode, ExprNode lhs, ExprNode rhs) {
        checkEnumerationPredicate(lhs, declarationNode);
        ST template = group.getInstanceOf("iteration_construct_subsetneq");
        ST enumerationTemplate = generateEnumeration(template, declarationNode);
        TemplateHandler.add(enumerationTemplate, "set", machineGenerator.visitExprNode(rhs, null));
        return enumerationTemplate;
    }

    public List<ST> getEnumerationTemplates(List<DeclarationNode> declarations, PredicateNode predicate) {
        ST enumerationTemplate = null;
        List<ST> enumerationTemplates = new ArrayList<>();
        for(int i = 0; i < declarations.size(); i++) {
            DeclarationNode declarationNode = declarations.get(i);
            PredicateOperatorWithExprArgsNode innerPredicate;
            if(predicate instanceof PredicateOperatorWithExprArgsNode) {
                innerPredicate = (PredicateOperatorWithExprArgsNode) predicate;
            } else {
                innerPredicate = (PredicateOperatorWithExprArgsNode) ((PredicateOperatorNode) predicate).getPredicateArguments().get(i);
            }
            if(innerPredicate.getOperator() == PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF) {
                enumerationTemplate = getElementOfTemplate(declarationNode, innerPredicate.getExpressionNodes().get(0), innerPredicate.getExpressionNodes().get(1));
            } else if(innerPredicate.getOperator() == PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.EQUAL) {
                enumerationTemplate = getEqualTemplate(declarationNode, innerPredicate.getExpressionNodes().get(0), innerPredicate.getExpressionNodes().get(1));
            } else if(innerPredicate.getOperator() == PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.INCLUSION) {
                enumerationTemplate = getSubsetTemplate(declarationNode, innerPredicate.getExpressionNodes().get(0), innerPredicate.getExpressionNodes().get(1));
            } else if(innerPredicate.getOperator() == PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.STRICT_INCLUSION) {
                enumerationTemplate = getSubsetNeqTemplate(declarationNode, innerPredicate.getExpressionNodes().get(0), innerPredicate.getExpressionNodes().get(1));
            } else {
                throw new RuntimeException("Other operations within predicate node not supported yet");
            }
            enumerationTemplates.add(enumerationTemplate);
        }
        return enumerationTemplates;
    }

    public ST evaluateEnumerationTemplates(List<ST> enumerationTemplates, String innerBody) {
        int enumerationSize = enumerationTemplates.size();
        ST lastEnumeration = enumerationTemplates.get(enumerationSize - 1);
        TemplateHandler.add(lastEnumeration, "body", innerBody);

        int i = enumerationSize - 2;
        while(i >= 0) {
            ST currentEnumeration = enumerationTemplates.get(i);
            TemplateHandler.add(currentEnumeration, "body", lastEnumeration.render());
            lastEnumeration = currentEnumeration;
            i--;
        }
        return lastEnumeration;
    }

}