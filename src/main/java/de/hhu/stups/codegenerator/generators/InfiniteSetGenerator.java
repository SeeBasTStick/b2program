package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.hhu.stups.codegenerator.handlers.TemplateHandler;
import de.prob.parser.ast.nodes.expression.ExprNode;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.expression.StructNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fabian on 21.05.19.
 */
public class InfiniteSetGenerator {

    private static final List<PredicateOperatorWithExprArgsNode.PredOperatorExprArgs> INFINITE_PREDICATE_OPERATORS =
            Arrays.asList(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF, PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.NOT_BELONGING, PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.EQUAL, PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.NOT_EQUAL,
                    PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.INCLUSION, PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.STRICT_INCLUSION, PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.NON_INCLUSION, PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.STRICT_NON_INCLUSION);

    private static final List<ExpressionOperatorNode.ExpressionOperator> INFINITE_EXPRESSIONS =
            Arrays.asList(ExpressionOperatorNode.ExpressionOperator.INTEGER, ExpressionOperatorNode.ExpressionOperator.NATURAL, ExpressionOperatorNode.ExpressionOperator.NATURAL1, ExpressionOperatorNode.ExpressionOperator.STRING,
                    ExpressionOperatorNode.ExpressionOperator.SEQ, ExpressionOperatorNode.ExpressionOperator.SEQ1, ExpressionOperatorNode.ExpressionOperator.ISEQ, ExpressionOperatorNode.ExpressionOperator.ISEQ1, ExpressionOperatorNode.ExpressionOperator.PERM);

    private static final List<ExpressionOperatorNode.ExpressionOperator> POWER_SET_EXPRESSIONS =
            Arrays.asList(ExpressionOperatorNode.ExpressionOperator.POW, ExpressionOperatorNode.ExpressionOperator.POW1, ExpressionOperatorNode.ExpressionOperator.FIN, ExpressionOperatorNode.ExpressionOperator.FIN1);

    private final STGroup currentGroup;

    private final MachineGenerator machineGenerator;

    private final NameHandler nameHandler;

    public InfiniteSetGenerator(STGroup group, MachineGenerator machineGenerator, NameHandler nameHandler) {
        this.currentGroup = group;
        this.machineGenerator = machineGenerator;
        this.nameHandler = nameHandler;
    }

    /*
    * This function checks whether the given predicate contains an infinite epxression on the right-hand side
    */
    public boolean checkInfinite(PredicateOperatorWithExprArgsNode node) {
        List<ExprNode> expressions = node.getExpressionNodes();
        PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator = node.getOperator();
        if(expressions.size() == 2 && INFINITE_PREDICATE_OPERATORS.contains(operator)) {
            ExprNode rhs = node.getExpressionNodes().get(1);
            if(rhs instanceof ExpressionOperatorNode) {
                ExpressionOperatorNode.ExpressionOperator rhsOperator = ((ExpressionOperatorNode) rhs).getOperator();
                if(INFINITE_EXPRESSIONS.contains(rhsOperator)) {
                    return true;
                } else if(POWER_SET_EXPRESSIONS.contains(rhsOperator)) {
                    ExprNode innerRhs = ((ExpressionOperatorNode) rhs).getExpressionNodes().get(0);
                    if(innerRhs instanceof ExpressionOperatorNode && INFINITE_EXPRESSIONS.contains(((ExpressionOperatorNode) innerRhs).getOperator()) && operator == PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.ELEMENT_OF) {
                        return true;
                    } else if(innerRhs instanceof StructNode) {
                        return true;
                    }
                }
            } else if(rhs instanceof StructNode) {
                return true;
            }
        }
        return false;
    }

    /*
    * This function checks whether the given expression is infinite
    */
    public boolean isInfiniteExpression(ExprNode expression) {
        if(expression instanceof ExpressionOperatorNode) {
            ExpressionOperatorNode.ExpressionOperator operator = ((ExpressionOperatorNode) expression).getOperator();
            if(INFINITE_EXPRESSIONS.contains(operator)) {
                return true;
            }
        } else if(expression instanceof StructNode) {
            return true;
        }
        return false;
    }

    /*
    * This function generates code an operation name for a predicate with INTEGER on the right-hand side
    */
    private String generateInfiniteInteger(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch(operator) {
            case ELEMENT_OF:
                operatorName = "isInteger";
                break;
            case NOT_BELONGING:
                operatorName = "isNotInteger";
                break;
            case INCLUSION:
                operatorName = "subsetOfInteger";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfInteger";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfInteger";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfInteger";
                break;
            case EQUAL:
                operatorName = "equalInteger";
                break;
            case NOT_EQUAL:
                operatorName = "unequalInteger";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    /*
    * This function generates code an operation name for a predicate with NATURAL on the right-hand side
    */
    private String generateInfiniteNatural(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch (operator) {
            case ELEMENT_OF:
                operatorName = "isNatural";
                break;
            case NOT_BELONGING:
                operatorName = "isNotNatural";
                break;
            case INCLUSION:
                operatorName = "subsetOfNatural";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfNatural";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfNatural";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfNatural";
                break;
            case EQUAL:
                operatorName = "equalNatural";
                break;
            case NOT_EQUAL:
                operatorName = "unequalNatural";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    /*
    * This function generates code an operation name for a predicate with NATURAL1 on the right-hand side
    */
    private String generateInfiniteNatural1(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch (operator) {
            case ELEMENT_OF:
                operatorName = "isNatural1";
                break;
            case NOT_BELONGING:
                operatorName = "isNotNatural1";
                break;
            case INCLUSION:
                operatorName = "subsetOfNatural1";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfNatural1";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfNatural1";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfNatural1";
                break;
            case EQUAL:
                operatorName = "equalNatural1";
                break;
            case NOT_EQUAL:
                operatorName = "unequalNatural1";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    /*
    * This function generates code an operation name for a predicate with STRING on the right-hand side
    */
    private String generateInfiniteString(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch (operator) {
            case ELEMENT_OF:
                operatorName = "isString";
                break;
            case NOT_BELONGING:
                operatorName = "isNotString";
                break;
            case INCLUSION:
                operatorName = "subsetOfString";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfString";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfString";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfString";
                break;
            case EQUAL:
                operatorName = "equalString";
                break;
            case NOT_EQUAL:
                operatorName = "unequalString";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    /*
    * This function generates code an operation name for a predicate with a struct on the right-hand side
    */
    private String generateInfiniteStruct(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch (operator) {
            case ELEMENT_OF:
                operatorName = "isRecord";
                break;
            case NOT_BELONGING:
                operatorName = "isNotRecord";
                break;
            case INCLUSION:
                operatorName = "subsetOfStruct";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfStruct";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfStruct";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfStruct";
                break;
            case EQUAL:
                operatorName = "equalStruct";
                break;
            case NOT_EQUAL:
                operatorName = "unequalStruct";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    private String generateInfiniteSeq(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch(operator) {
            case ELEMENT_OF:
                operatorName = "isSeq";
                break;
            case NOT_BELONGING:
                operatorName = "isNotSeq";
                break;
            case INCLUSION:
                operatorName = "subsetOfSeq";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfSeq";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfSeq";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfSeq";
                break;
            case EQUAL:
                operatorName = "equalSeq";
                break;
            case NOT_EQUAL:
                operatorName = "unequalSeq";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    private String generateInfiniteSeq1(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch(operator) {
            case ELEMENT_OF:
                operatorName = "isSeq1";
                break;
            case NOT_BELONGING:
                operatorName = "isNotSeq1";
                break;
            case INCLUSION:
                operatorName = "subsetOfSeq1";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfSeq1";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfSeq1";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfSeq1";
                break;
            case EQUAL:
                operatorName = "equalSeq1";
                break;
            case NOT_EQUAL:
                operatorName = "unequalSeq1";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    private String generateInfiniteISeq(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch(operator) {
            case ELEMENT_OF:
                operatorName = "isISeq";
                break;
            case NOT_BELONGING:
                operatorName = "isNotISeq";
                break;
            case INCLUSION:
                operatorName = "subsetOfISeq";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfISeq";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfISeq";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfISeq";
                break;
            case EQUAL:
                operatorName = "equalISeq";
                break;
            case NOT_EQUAL:
                operatorName = "unequalISeq";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    private String generateInfiniteISeq1(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch(operator) {
            case ELEMENT_OF:
                operatorName = "isISeq1";
                break;
            case NOT_BELONGING:
                operatorName = "isNotISeq1";
                break;
            case INCLUSION:
                operatorName = "subsetOfISeq1";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfISeq1";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfISeq1";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfISeq1";
                break;
            case EQUAL:
                operatorName = "equalISeq1";
                break;
            case NOT_EQUAL:
                operatorName = "unequalISeq1";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    private String generateInfinitePerm(PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName;
        switch(operator) {
            case ELEMENT_OF:
                operatorName = "isPerm";
                break;
            case NOT_BELONGING:
                operatorName = "isNotPerm";
                break;
            case INCLUSION:
                operatorName = "subsetOfPerm";
                break;
            case NON_INCLUSION:
                operatorName = "notSubsetOfPerm";
                break;
            case STRICT_INCLUSION:
                operatorName = "strictSubsetOfPerm";
                break;
            case STRICT_NON_INCLUSION:
                operatorName = "notStrictSubsetOfPerm";
                break;
            case EQUAL:
                operatorName = "equalPerm";
                break;
            case NOT_EQUAL:
                operatorName = "unequalPerm";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        return operatorName;
    }

    /*
    * This function generates code for a struct on the right-hand side of a predicate
    */
    private String generateInfiniteStruct(ST template, PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator) {
        String operatorName = generateInfiniteStruct(operator);
        TemplateHandler.add(template, "operator", nameHandler.handle(operatorName));
        return template.render();
    }

    /*
    * This function generates code for an infinite expression on the right-hand side of a predicate
    */
    public String generateInfinite(PredicateOperatorWithExprArgsNode node) {
        PredicateOperatorWithExprArgsNode.PredOperatorExprArgs operator = node.getOperator();
        ST template = currentGroup.getInstanceOf("infinite_predicate");
        ExprNode lhs = node.getExpressionNodes().get(0);
        ExprNode rhs = node.getExpressionNodes().get(1);
        TemplateHandler.add(template, "arg", machineGenerator.visitExprNode(lhs, null));
        String operatorName;
        if(rhs instanceof StructNode) {
            return generateInfiniteStruct(template, operator);
        }
        ExpressionOperatorNode.ExpressionOperator rhsOperator = ((ExpressionOperatorNode) rhs).getOperator();
        if (rhsOperator == ExpressionOperatorNode.ExpressionOperator.POW) {
            operator = PredicateOperatorWithExprArgsNode.PredOperatorExprArgs.INCLUSION;
            ExprNode innerRhs = ((ExpressionOperatorNode) rhs).getExpressionNodes().get(0);
            if(innerRhs instanceof ExpressionOperatorNode) {
                rhsOperator = ((ExpressionOperatorNode) innerRhs).getOperator();
            } else if(innerRhs instanceof StructNode) {
                return generateInfiniteStruct(template, operator);
            }
        }
        switch(rhsOperator) {
            case INTEGER:
                operatorName = generateInfiniteInteger(operator);
                break;
            case NATURAL:
                operatorName = generateInfiniteNatural(operator);
                break;
            case NATURAL1:
                operatorName = generateInfiniteNatural1(operator);
                break;
            case STRING:
                operatorName = generateInfiniteString(operator);
                break;
            case SEQ:
                operatorName = generateInfiniteSeq(operator);
                break;
            case SEQ1:
                operatorName = generateInfiniteSeq1(operator);
                break;
            case ISEQ:
                operatorName = generateInfiniteISeq(operator);
                break;
            case ISEQ1:
                operatorName = generateInfiniteISeq1(operator);
                break;
            case PERM:
                operatorName = generateInfinitePerm(operator);
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        TemplateHandler.add(template, "operator", nameHandler.handle(operatorName));
        return template.render();
    }

    /*
    * This function generates code for checking the domain of a relation. The domain is infinite. So the set of relations is not evaluated
    */
    public String generateInfiniteDomainChecking(PredicateOperatorWithExprArgsNode node, ExpressionOperatorNode.ExpressionOperator operator, ExprNode domain) {
        ST template = currentGroup.getInstanceOf("infinite_predicate");
        ExprNode lhs = node.getExpressionNodes().get(0);
        TemplateHandler.add(template, "arg", machineGenerator.visitExprNode(lhs, null));
        if(domain instanceof StructNode) {
            return generateInfiniteDomainCheckingStruct(template);
        }
        String operatorName;
        ExpressionOperatorNode.ExpressionOperator domainOperator = ((ExpressionOperatorNode) domain).getOperator();
        switch(domainOperator) {
            case INTEGER:
                operatorName = "checkDomainInteger";
                break;
            case NATURAL:
                operatorName = "checkDomainNatural";
                break;
            case NATURAL1:
                operatorName = "checkDomainNatural1";
                break;
            case STRING:
                operatorName = "checkDomainString";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        TemplateHandler.add(template, "operator", nameHandler.handle(operatorName));
        return template.render();
    }

    /*
    * This function generates code for checking the domain of a relation. In this case, the domain of the relation is a struct. The set of relations is not evaluated.
    */
    private String generateInfiniteDomainCheckingStruct(ST template) {
        String operatorName = "checkDomainStruct";
        TemplateHandler.add(template, "operator", nameHandler.handle(operatorName));
        return template.render();
    }

    /*
    * This function generates code for checking the range of a relation. The range is infinite. So the set of relations is not evaluated.
    */
    public String generateInfiniteRangeChecking(PredicateOperatorWithExprArgsNode node, ExpressionOperatorNode.ExpressionOperator operator, ExprNode range) {
        ST template = currentGroup.getInstanceOf("infinite_predicate");
        ExprNode lhs = node.getExpressionNodes().get(0);
        TemplateHandler.add(template, "arg", machineGenerator.visitExprNode(lhs, null));
        if(range instanceof StructNode) {
            return generateInfiniteRangeCheckingStruct(template);
        }
        String operatorName;
        ExpressionOperatorNode.ExpressionOperator rangeOperator = ((ExpressionOperatorNode) range).getOperator();
        switch(rangeOperator) {
            case INTEGER:
                operatorName = "checkRangeInteger";
                break;
            case NATURAL:
                operatorName = "checkRangeNatural";
                break;
            case NATURAL1:
                operatorName = "checkRangeNatural1";
                break;
            case STRING:
                operatorName = "checkRangeString";
                break;
            default:
                throw new RuntimeException("Given node is not implemented: " + operator);
        }
        TemplateHandler.add(template, "operator", nameHandler.handle(operatorName));
        return template.render();
    }

    /*
    * This function generates code for checking the range of a relation. In this case, the range of the relation is a struct. The set of relations is not evaluated.
    */
    private String generateInfiniteRangeCheckingStruct(ST template) {
        String operatorName = "checkRangeStruct";
        TemplateHandler.add(template, "operator", nameHandler.handle(operatorName));
        return template.render();
    }

    /*
    * This function generates code for checking whether a relation is total. The domain is infinite. So the set of relations is not evaluated.
    */
    private String generateInfiniteTotalRelation(ExpressionOperatorNode.ExpressionOperator domainOperator) {
        String operatorName;
        switch(domainOperator) {
            case INTEGER:
                operatorName = "isTotalInteger";
                break;
            case NATURAL:
                operatorName = "isTotalNatural";
                break;
            case NATURAL1:
                operatorName = "isTotalNatural1";
                break;
            case STRING:
                operatorName = "isTotalString";
                break;
            default:
                throw new RuntimeException("Argument of relation expressions are not supported");
        }
        return operatorName;
    }

    /*
    * This function generates code for checking whether a relation is partial. The domain is infinite. So the set of relations is not evaluated.
    */
    private String generateInfinitePartialRelation(ExpressionOperatorNode.ExpressionOperator domainOperator) {
        String operatorName;
        switch(domainOperator) {
            case INTEGER:
                operatorName = "isPartialInteger";
                break;
            case NATURAL:
                operatorName = "isPartialNatural";
                break;
            case NATURAL1:
                operatorName = "isPartialNatural1";
                break;
            case STRING:
                operatorName = "isPartialString";
                break;
            default:
                throw new RuntimeException("Argument of relation expressions are not supported");
        }
        return operatorName;
    }

    /*
    * This function generates code for a total/partial set of relations. As the domain is infinite, the set of relations is not evaluated.
    */
    public String generateInfiniteTotalPartial(PredicateOperatorWithExprArgsNode node, ExpressionOperatorNode.ExpressionOperator operator, ExprNode domain) {
        ST template = currentGroup.getInstanceOf("infinite_predicate");
        ExprNode lhs = node.getExpressionNodes().get(0);
        TemplateHandler.add(template, "arg", machineGenerator.visitExprNode(lhs, null));
        if(domain instanceof StructNode) {
            return generateInfiniteTotalPartialStruct(template, operator);
        }
        ExpressionOperatorNode.ExpressionOperator domainOperator = ((ExpressionOperatorNode) domain).getOperator();
        if(RelationSetGenerator.TOTAL_EXPRESSIONS.contains(operator)) {
            TemplateHandler.add(template, "operator", generateInfiniteTotalRelation(domainOperator));
        } else if(RelationSetGenerator.PARTIAL_EXPRESSIONS.contains(operator)) {
            TemplateHandler.add(template, "operator", generateInfinitePartialRelation(domainOperator));
        } else {
            //Must be empty because predicate can be optional
            return "";
        }
        return template.render();
    }

    /*
    * This function generates code for a total/partial set of relations. As the domain is infinite, the set of relations is not evaluated.
    */
    private String generateInfiniteTotalPartialStruct(ST template, ExpressionOperatorNode.ExpressionOperator operator) {
        if(RelationSetGenerator.TOTAL_EXPRESSIONS.contains(operator)) {
            TemplateHandler.add(template, "operator", "isTotalStruct");
            return template.render();
        } else if(RelationSetGenerator.PARTIAL_EXPRESSIONS.contains(operator)) {
            TemplateHandler.add(template, "operator", "isPartialStruct");
            return template.render();
        } else {
            //Must be empty because predicate can be optional
            return "";
        }
    }

    /*
    * This function generates code for checking whether a relation is surjective. The range is infinite. So the set of relations is not evaluated.
    */
    private String generateInfiniteSurjection(ExpressionOperatorNode.ExpressionOperator rangeOperator) {
        String operatorName;
        switch(rangeOperator) {
            case INTEGER:
                operatorName = "isSurjectionInteger";
                break;
            case NATURAL:
                operatorName = "isSurjectionNatural";
                break;
            case NATURAL1:
                operatorName = "isSurjectionNatural1";
                break;
            case STRING:
                operatorName = "isSurjectionString";
                break;
            default:
                throw new RuntimeException("Argument of relation expressions are not supported");
        }
        return operatorName;
    }

    /*
    * This function generates code for checking whether a relation is bijective. The range and domain are infinite. So the set of relations is not evaluated.
    */
    private String generateInfiniteBijection(ExpressionOperatorNode.ExpressionOperator rangeOperator) {
        String operatorName;
        switch(rangeOperator) {
            case INTEGER:
                operatorName = "isBijectionInteger";
                break;
            case NATURAL:
                operatorName = "isBijectionNatural";
                break;
            case NATURAL1:
                operatorName = "isBijectionNatural1";
                break;
            case STRING:
                operatorName = "isBijectionString";
                break;
            default:
                throw new RuntimeException("Argument of relation expressions are not supported");
        }
        return operatorName;
    }

    /*
    * This function generates code for a surjective/injective/bijective set of relations. As the domain or range is infinite, the set of relations is not evaluated.
    */
    public String generateInfiniteSurjectionInjectionBijection(PredicateOperatorWithExprArgsNode node, ExpressionOperatorNode.ExpressionOperator operator, ExprNode range) {
        ST template = currentGroup.getInstanceOf("infinite_predicate");
        ExprNode lhs = node.getExpressionNodes().get(0);
        TemplateHandler.add(template, "arg", machineGenerator.visitExprNode(lhs, null));
        if(range instanceof StructNode) {
            return generateInfiniteSurjectionInjectionBijectionStruct(template, operator);
        }
        ExpressionOperatorNode.ExpressionOperator rangeOperator = ((ExpressionOperatorNode) range).getOperator();
        if(RelationSetGenerator.SURJECTIVE_EXPRESSIONS.contains(operator)) {
            TemplateHandler.add(template, "operator", generateInfiniteSurjection(rangeOperator));
        } else if(RelationSetGenerator.BIJECTIVE_EXPRESSIONS.contains(operator)) {
            TemplateHandler.add(template, "operator", generateInfiniteBijection(rangeOperator));
        } else {
            //Must be empty because predicate can be optional
            return "";
        }
        return template.render();
    }

    /*
    * This function generates code for a surjective/injective/bijective set of relations containing structs. As the domain or range is infinite, the set of relations is not evaluated.
    */
    private String generateInfiniteSurjectionInjectionBijectionStruct(ST template, ExpressionOperatorNode.ExpressionOperator operator) {
        if(RelationSetGenerator.TOTAL_EXPRESSIONS.contains(operator)) {
            TemplateHandler.add(template, "operator", "isSurjectionStruct");
            return template.render();
        } else if(RelationSetGenerator.PARTIAL_EXPRESSIONS.contains(operator)) {
            TemplateHandler.add(template, "operator", "isBijectionStruct");
            return template.render();
        } else {
            //Must be empty because predicate can be optional
            return "";
        }
    }

    //TODO: Maybe support set of sequences

}
