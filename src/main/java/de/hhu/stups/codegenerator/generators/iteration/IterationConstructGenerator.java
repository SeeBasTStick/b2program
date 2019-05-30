package de.hhu.stups.codegenerator.generators.iteration;

import de.hhu.stups.codegenerator.generators.ImportGenerator;
import de.hhu.stups.codegenerator.generators.MachineGenerator;
import de.hhu.stups.codegenerator.generators.TypeGenerator;
import de.hhu.stups.codegenerator.handlers.IterationConstructHandler;
import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.prob.parser.ast.nodes.DeclarationNode;
import de.prob.parser.ast.nodes.expression.ExpressionOperatorNode;
import de.prob.parser.ast.nodes.expression.IdentifierExprNode;
import de.prob.parser.ast.nodes.expression.IfExpressionNode;
import de.prob.parser.ast.nodes.expression.LambdaNode;
import de.prob.parser.ast.nodes.expression.LetExpressionNode;
import de.prob.parser.ast.nodes.expression.NumberNode;
import de.prob.parser.ast.nodes.expression.QuantifiedExpressionNode;
import de.prob.parser.ast.nodes.expression.RecordFieldAccessNode;
import de.prob.parser.ast.nodes.expression.RecordNode;
import de.prob.parser.ast.nodes.expression.SetComprehensionNode;
import de.prob.parser.ast.nodes.expression.StringNode;
import de.prob.parser.ast.nodes.expression.StructNode;
import de.prob.parser.ast.nodes.ltl.LTLBPredicateNode;
import de.prob.parser.ast.nodes.ltl.LTLInfixOperatorNode;
import de.prob.parser.ast.nodes.ltl.LTLKeywordNode;
import de.prob.parser.ast.nodes.ltl.LTLPrefixOperatorNode;
import de.prob.parser.ast.nodes.predicate.CastPredicateExpressionNode;
import de.prob.parser.ast.nodes.predicate.IdentifierPredicateNode;
import de.prob.parser.ast.nodes.predicate.IfPredicateNode;
import de.prob.parser.ast.nodes.predicate.LetPredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorNode;
import de.prob.parser.ast.nodes.predicate.PredicateOperatorWithExprArgsNode;
import de.prob.parser.ast.nodes.predicate.QuantifiedPredicateNode;
import de.prob.parser.ast.nodes.substitution.AnySubstitutionNode;
import de.prob.parser.ast.nodes.substitution.AssignSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.BecomesElementOfSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.BecomesSuchThatSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.ChoiceSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.ConditionSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.IfOrSelectSubstitutionsNode;
import de.prob.parser.ast.nodes.substitution.LetSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.ListSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.OperationCallSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.SkipSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.VarSubstitutionNode;
import de.prob.parser.ast.nodes.substitution.WhileSubstitutionNode;
import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.visitors.AbstractVisitor;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fabian on 05.02.19.
 */
public class IterationConstructGenerator implements AbstractVisitor<Void, Void> {

    private final IterationConstructHandler iterationConstructHandler;

    private final IterationPredicateGenerator iterationPredicateGenerator;

    private final SetComprehensionGenerator setComprehensionGenerator;

    private final LambdaGenerator lambdaGenerator;

    private final QuantifiedPredicateGenerator quantifiedPredicateGenerator;

    private final QuantifiedExpressionGenerator quantifiedExpressionGenerator;

    private final AnySubstitutionGenerator anySubstitutionGenerator;

    private final LetExpressionPredicateGenerator letExpressionPredicateGenerator;

    private final BecomesSuchThatGenerator becomesSuchThatGenerator;

    private final ImportGenerator importGenerator;

    private final HashMap<String, String> iterationsMapCode;

    private final HashMap<String, String> iterationsMapIdentifier;

    private final List<String> boundedVariables;

    private final List<String> allBoundedVariables;

    public IterationConstructGenerator(final IterationConstructHandler iterationConstructHandler, final MachineGenerator machineGenerator, final NameHandler nameHandler, final STGroup group,
                                       final TypeGenerator typeGenerator, final ImportGenerator importGenerator) {
        this.iterationConstructHandler = iterationConstructHandler;
        this.iterationPredicateGenerator = new IterationPredicateGenerator(group, machineGenerator, typeGenerator, iterationConstructHandler);
        this.setComprehensionGenerator = new SetComprehensionGenerator(group, machineGenerator, this, iterationConstructHandler, iterationPredicateGenerator, typeGenerator);
        this.lambdaGenerator = new LambdaGenerator(group, machineGenerator, this, iterationConstructHandler, iterationPredicateGenerator, typeGenerator);
        this.quantifiedPredicateGenerator = new QuantifiedPredicateGenerator(group, machineGenerator, this, iterationConstructHandler, iterationPredicateGenerator);
        this.quantifiedExpressionGenerator = new QuantifiedExpressionGenerator(group, machineGenerator, nameHandler, typeGenerator, this, iterationConstructHandler, iterationPredicateGenerator);
        this.anySubstitutionGenerator = new AnySubstitutionGenerator(group, machineGenerator, this, iterationConstructHandler, iterationPredicateGenerator);
        this.letExpressionPredicateGenerator = new LetExpressionPredicateGenerator(group, machineGenerator, typeGenerator, this, iterationConstructHandler, iterationPredicateGenerator);
        this.becomesSuchThatGenerator = new BecomesSuchThatGenerator(group, machineGenerator, typeGenerator, this, iterationConstructHandler, iterationPredicateGenerator);
        this.importGenerator = importGenerator;
        this.iterationsMapCode = new HashMap<>();
        this.iterationsMapIdentifier = new HashMap<>();
        this.boundedVariables = new ArrayList<>();
        this.allBoundedVariables = new ArrayList<>();
    }


    @Override
    public Void visitExprOperatorNode(ExpressionOperatorNode node, Void expected) {
        node.getExpressionNodes().forEach(expr -> visitExprNode(expr, null));
        return null;
    }

    @Override
    public Void visitIdentifierExprNode(IdentifierExprNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitCastPredicateExpressionNode(CastPredicateExpressionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitNumberNode(NumberNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitStringNode(StringNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitQuantifiedExpressionNode(QuantifiedExpressionNode node, Void expected) {
        iterationsMapCode.put(node.toString(), quantifiedExpressionGenerator.generateQuantifiedExpression(node));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitSetComprehensionNode(SetComprehensionNode node, Void expected) {
        iterationsMapCode.put(node.toString(), setComprehensionGenerator.generateSetComprehension(node));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitLambdaNode(LambdaNode node, Void expected) {
        iterationsMapCode.put(node.toString(), lambdaGenerator.generateLambda(node));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitLTLPrefixOperatorNode(LTLPrefixOperatorNode ltlPrefixOperatorNode, Void aVoid) {
        return null;
    }

    @Override
    public Void visitLTLKeywordNode(LTLKeywordNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLTLInfixOperatorNode(LTLInfixOperatorNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitLTLBPredicateNode(LTLBPredicateNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitIdentifierPredicateNode(IdentifierPredicateNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitPredicateOperatorNode(PredicateOperatorNode node, Void expected) {
        node.getPredicateArguments().forEach(pred -> visitPredicateNode(pred, expected));
        return null;
    }

    @Override
    public Void visitPredicateOperatorWithExprArgs(PredicateOperatorWithExprArgsNode node, Void expected) {
        node.getExpressionNodes().forEach(expr -> visitExprNode(expr, expected));
        return null;
    }

    @Override
    public Void visitQuantifiedPredicateNode(QuantifiedPredicateNode node, Void aVoid) {
        iterationsMapCode.put(node.toString(), quantifiedPredicateGenerator.generateQuantifiedPredicate(node));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitVarSubstitutionNode(VarSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitWhileSubstitutionNode(WhileSubstitutionNode whileSubstitutionNode, Void aVoid) {
        return null;
    }

    @Override
    public Void visitListSubstitutionNode(ListSubstitutionNode node, Void aVoid) {
        node.getSubstitutions().forEach(subs -> visitSubstitutionNode(subs, null));
        return null;
    }

    @Override
    public Void visitIfOrSelectSubstitutionsNode(IfOrSelectSubstitutionsNode ifOrSelectSubstitutionsNode, Void aVoid) {
        return null;
    }

    @Override
    public Void visitIfExpressionNode(IfExpressionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitIfPredicateNode(IfPredicateNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitAssignSubstitutionNode(AssignSubstitutionNode node, Void aVoid) {
        node.getLeftSide().forEach(lhs -> visitExprNode(lhs, null));
        node.getRightSide().forEach(rhs -> visitExprNode(rhs, null));
        return null;
    }

    @Override
    public Void visitSkipSubstitutionNode(SkipSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitConditionSubstitutionNode(ConditionSubstitutionNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitAnySubstitution(AnySubstitutionNode node, Void expected) {
        iterationsMapCode.put(node.toString(), anySubstitutionGenerator.generateAnySubstitution(node));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitLetSubstitution(LetSubstitutionNode node, Void expected) {
        iterationsMapCode.put(node.toString(), anySubstitutionGenerator.generateAnySubstitution(new AnySubstitutionNode(node.getSourceCodePosition(), node.getLocalIdentifiers(), node.getPredicate(), node.getBody())));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitLetExpressionNode(LetExpressionNode node, Void expected) {
        iterationsMapCode.put(node.toString(), letExpressionPredicateGenerator.generateLetExpression(node));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitLetPredicateNode(LetPredicateNode node, Void expected) {
        iterationsMapCode.put(node.toString(), letExpressionPredicateGenerator.generateLetPredicate(node));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitBecomesElementOfSubstitutionNode(BecomesElementOfSubstitutionNode node, Void expected) {
        node.getIdentifiers().forEach(id -> visitExprNode(id, null));
        visitExprNode(node.getExpression(), null);
        return null;
    }

    @Override
    public Void visitBecomesSuchThatSubstitutionNode(BecomesSuchThatSubstitutionNode node, Void expected) {
        iterationsMapCode.put(node.toString(), becomesSuchThatGenerator.generateBecomesSuchThat(node));
        iterationConstructHandler.incrementIterationConstructCounter();
        return null;
    }

    @Override
    public Void visitSubstitutionIdentifierCallNode(OperationCallSubstitutionNode operationCallSubstitutionNode, Void expected) {
        return null;
    }

    @Override
    public Void visitChoiceSubstitutionNode(ChoiceSubstitutionNode choiceSubstitutionNode, Void expected) {
        return null;
    }

    @Override
    public Void visitRecordNode(RecordNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitStructNode(StructNode node, Void expected) {
        return null;
    }

    @Override
    public Void visitRecordFieldAccessNode(RecordFieldAccessNode node, Void expected) {
        return null;
    }

    private void addBoundedVariables(List<DeclarationNode> declarations) {
        boundedVariables.clear();
        boundedVariables.addAll(declarations.stream().map(DeclarationNode::toString).collect(Collectors.toList()));
        allBoundedVariables.addAll(declarations.stream().map(DeclarationNode::toString).collect(Collectors.toList()));
    }

    private void clearBoundedVariables(List<DeclarationNode> declarations) {
        boundedVariables.clear();
        allBoundedVariables.removeAll(declarations.stream().map(DeclarationNode::toString).collect(Collectors.toList()));
    }

    public List<String> getBoundedVariables() {
        return boundedVariables;
    }

    public List<String> getAllBoundedVariables() {
        return allBoundedVariables;
    }

    private void addIteration(String node, String identifier, String code) {
        iterationsMapIdentifier.put(node, identifier);
        iterationsMapCode.put(node, code);
    }

    private void addIteration(String node, String code) {
        iterationsMapCode.put(node, code);
    }

    public HashMap<String, String> getIterationsMapCode() {
        return iterationsMapCode;
    }

    public HashMap<String, String> getIterationsMapIdentifier() {
        return iterationsMapIdentifier;
    }

    public void prepareGeneration(PredicateNode predicate, List<DeclarationNode> declarations, BType type) {
        this.addBoundedVariables(declarations);
        iterationPredicateGenerator.checkPredicate(predicate, declarations);
        importGenerator.addImportInIteration(type);
        iterationConstructHandler.setIterationConstructGenerator(this);
    }

    public void prepareGeneration(PredicateNode predicate, List<DeclarationNode> declarations) {
        this.addBoundedVariables(declarations);
        iterationPredicateGenerator.checkPredicate(predicate, declarations);
        iterationConstructHandler.setIterationConstructGenerator(this);
    }

    public void addGeneration(String node, String identifier, List<DeclarationNode> declarations, String result) {
        this.addIteration(node, identifier, result);
        this.clearBoundedVariables(declarations);
    }

    public void addGeneration(String node, List<DeclarationNode> declarations, String result) {
        this.addIteration(node, result);
        this.clearBoundedVariables(declarations);
    }
}
