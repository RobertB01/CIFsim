//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.cif.codegen.c89.typeinfos;

import static org.eclipse.escet.cif.codegen.c89.C89CodeGen.ENUM_NAMES_LIST;
import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeLiteral;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.typeinfos.EnumTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.common.box.CodeBox;

/**
 * Type information about CIF enumeration types, for the C89 target language.
 *
 * <p>
 * Linearized models have exactly one enumeration type.
 * </p>
 */
public class C89EnumTypeInfo extends EnumTypeInfo implements C89TypeInfo {
    /**
     * If set, generate functions only available in the current source file, else generate globally accessible
     * functions.
     */
    public final boolean genLocalFunctions;

    /** Prefix of the enumeration type. */
    private final String prefix;

    /**
     * Constructor for {@link C89EnumTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param prefix Common prefix of all generated code.
     * @param enumType Enumeration type being represented by the object.
     */
    public C89EnumTypeInfo(boolean genLocalFunctions, String prefix, EnumType enumType) {
        super(enumType);
        this.prefix = prefix;
        this.genLocalFunctions = genLocalFunctions;
    }

    @Override
    public boolean supportRawMemCmp() {
        return true;
    }

    @Override
    public boolean useValues() {
        return true;
    }

    @Override
    public String getTypePrintName(boolean rawString) {
        return "EnumTypePrint";
    }

    /**
     * Construct the name of an enumeration literal in C89.
     *
     * @param ctxt Code generation context.
     * @param eLit Literal to convert.
     * @return Name of the enumeration literal in C89.
     */
    protected String getLiteralName(CodeContext ctxt, EnumLiteral eLit) {
        String litName = fmt("_%s_%s", ctxt.getPrefix(), eLit.getName());
        return litName;
    }

    @Override
    public ExprCode convertEnumLiteral(EnumLiteralExpression expr, Destination dest, CodeContext ctxt) {
        EnumLiteral eLit = expr.getLiteral();
        String litName = getLiteralName(ctxt, eLit);

        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(makeLiteral(litName));
        return result;
    }

    @Override
    public String getTargetType() {
        return prefix + "Enum";
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        CodeBox typeCode = ctxt.makeCodeBox();
        CodeBox funcCode = ctxt.makeCodeBox();

        generateC89TypeCode(typeCode, ctxt);
        generateC89FuncCode(typeCode, funcCode, ctxt);

        typeCode.add();
        ctxt.appendReplacement("generated-types", typeCode.toString());
        ctxt.appendReplacement("type-support-code", funcCode.toString());
    }

    /**
     * Generate C89 enum type code.
     *
     * @param typeCode Destination for the generated type code.
     * @param ctxt Code generation context.
     */
    protected void generateC89TypeCode(CodeBox typeCode, CodeContext ctxt) {
        String declarationPrefix = genLocalFunctions ? "static " : "extern ";

        String enumInternalTypename = "Enum" + ctxt.getPrefix() + "_";
        String enumTypename = ctxt.getPrefix() + "Enum";
        typeCode.add("enum %s {", enumInternalTypename);
        typeCode.indent();
        for (EnumLiteral eLit: enumType.getEnum().getLiterals()) {
            typeCode.add(getLiteralName(ctxt, eLit) + ",");
        }
        typeCode.dedent();
        typeCode.add("};");
        typeCode.add("typedef enum %s %s;", enumInternalTypename, enumTypename);
        typeCode.add();

        // Declarations of name table and functions.
        typeCode.add("%sconst char *%s[];", declarationPrefix, ENUM_NAMES_LIST);
    }

    /**
     * Generate C89 enum support functions and definitions.
     *
     * @param typeCode Destination for the generated type code.
     * @param funcCode Destination for the generated functions.
     * @param ctxt Code generation context.
     */
    protected void generateC89FuncCode(CodeBox typeCode, CodeBox funcCode, CodeContext ctxt) {
        String definitionPrefix, declarationPrefix;
        if (genLocalFunctions) {
            definitionPrefix = "static ";
            declarationPrefix = "static ";
        } else {
            definitionPrefix = "";
            declarationPrefix = "extern ";
        }

        String enumTypename = ctxt.getPrefix() + "Enum";

        String header = fmt("int EnumTypePrint(%s value, char *dest, int start, int end)", enumTypename);
        typeCode.add(declarationPrefix + header + ";");

        // Definition of the enum type print function.

        funcCode.add("%s%s {", definitionPrefix, header);
        funcCode.indent();
        funcCode.add("int last = end - 1;");
        funcCode.add("const char *lit_name = %s[value];", ENUM_NAMES_LIST);
        funcCode.add("while (start < last && *lit_name) {");
        funcCode.indent();
        funcCode.add("dest[start++] = *lit_name;");
        funcCode.add("lit_name++;");
        funcCode.dedent();
        funcCode.add("}");
        funcCode.add("dest[start] = '\\0';");
        funcCode.add("return start;");
        funcCode.dedent();
        funcCode.add("}");
        funcCode.add();
    }

    @Override
    public void storeValue(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s %s = %s;", getTargetType(), dest.getData(), sourceValue.getData());
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp) {
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return "(${left-value}) == (${right-value})";
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return "(${left-value} != ${right-value})";
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof C89EnumTypeInfo;
    }

    @Override
    public int hashCode() {
        return C89EnumTypeInfo.class.hashCode();
    }
}
