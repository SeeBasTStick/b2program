package de.hhu.stups.codegenerator;


import de.prob.parser.ast.types.BType;
import de.prob.parser.ast.types.BoolType;
import de.prob.parser.ast.types.CoupleType;
import de.prob.parser.ast.types.EnumeratedSetElementType;
import de.prob.parser.ast.types.IntegerType;
import de.prob.parser.ast.types.SetType;
import de.prob.parser.ast.types.UntypedType;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

public class TypeGenerator {

    private final STGroup group;

    private final NameHandler nameHandler;

    private final boolean useBigInteger;

    public TypeGenerator(final STGroup group, final NameHandler nameHandler, final boolean useBigInteger) {
        this.group = group;
        this.nameHandler = nameHandler;
        this.useBigInteger = useBigInteger;
    }

    /*
    * This function generates code for a type with the given type and the information whether the type is generated for casting an object
    */
    public String generate(BType type) {
        ST template = group.getInstanceOf("type");
        if(type instanceof IntegerType) {
            if(useBigInteger) {
                TemplateHandler.add(template, "type", "BBigInteger");
            } else {
                TemplateHandler.add(template, "type", "BInteger");
            }
            return template.render();
        } else if(type instanceof BoolType) {
            TemplateHandler.add(template, "type", "BBoolean");
            return template.render();
        } else if(type instanceof SetType) {
            BType subType = ((SetType) type).getSubType();
            if(subType instanceof CoupleType) {
                template = group.getInstanceOf("relation_type");
                TemplateHandler.add(template, "leftType", generate(((CoupleType) subType).getLeft()));
                TemplateHandler.add(template, "rightType", generate(((CoupleType) subType).getRight()));
            } else {
                template = group.getInstanceOf("set_type");
                TemplateHandler.add(template, "type", generate(subType));
            }
            return template.render();
        } else if(type instanceof EnumeratedSetElementType) {
            TemplateHandler.add(template, "type", nameHandler.handleIdentifier(type.toString(), NameHandler.IdentifierHandlingEnum.INCLUDED_MACHINES));
            return template.render();
        } else if(type instanceof CoupleType) {
            template = group.getInstanceOf("couple_type");
            TemplateHandler.add(template, "leftType", generate(((CoupleType) type).getLeft()));
            TemplateHandler.add(template, "rightType", generate(((CoupleType) type).getRight()));
            return template.render();
        } else if(type instanceof UntypedType) {
            return generateUntyped();
        }
        return "";
    }

    /*
    * This function generates code for untyped nodes.
    */
    private String generateUntyped() {
        return group.getInstanceOf("void").render();
    }

}
