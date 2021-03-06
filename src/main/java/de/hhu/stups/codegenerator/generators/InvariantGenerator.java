package de.hhu.stups.codegenerator.generators;

import de.hhu.stups.codegenerator.handlers.NameHandler;
import de.prob.parser.ast.nodes.predicate.PredicateNode;
import de.prob.parser.ast.types.BType;
import org.stringtemplate.v4.STGroup;

import java.util.Map;

public class InvariantGenerator extends BXMLBodyGenerator {
    public InvariantGenerator(Map<Integer, BType> nodeType, NameHandler nameHandler, STGroup currentGroup) {
        super(nodeType, nameHandler, currentGroup);
    }

    public String generateInvariants(PredicateNode node){
        return processPredicateNode(node);
    }

}
