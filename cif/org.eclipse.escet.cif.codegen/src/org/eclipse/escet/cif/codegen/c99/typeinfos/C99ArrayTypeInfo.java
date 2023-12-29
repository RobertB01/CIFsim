//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.c99.typeinfos;

import static org.eclipse.escet.cif.codegen.c99.C99DataValue.constructReference;
import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeComputed;
import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeLiteral;
import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeReference;
import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.c99.typeinfos.C99TypeInfoHelper.typeGetTypePrintName;
import static org.eclipse.escet.cif.codegen.c99.typeinfos.C99TypeInfoHelper.typeSupportsRawMemCmp;
import static org.eclipse.escet.cif.codegen.c99.typeinfos.C99TypeInfoHelper.typeUsesValues;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionValues;
import static org.eclipse.escet.cif.common.CifTypeUtils.checkTypeCompat;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/**
 * Type information about the CIF array type (list type with fixed and equal lower and upper boundary) for the C99
 * target language.
 */
public class C99ArrayTypeInfo extends ArrayTypeInfo implements C99TypeInfo {
    /**
     * If set, generate functions only available in the current source file, else generate globally accessible
     * functions.
     */
    public final boolean genLocalFunctions;

    /**
     * Constructor for {@link C99ArrayTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     * @param childTIs Child type information (always 1 element).
     * @param length Length of the array.
     */
    public C99ArrayTypeInfo(boolean genLocalFunctions, CifType cifType, TypeInfo[] childTIs, int length) {
        super(cifType, childTIs, length);
        this.genLocalFunctions = genLocalFunctions;
    }

    @Override
    public boolean supportRawMemCmp() {
        return typeSupportsRawMemCmp(childInfos[0]); // List type can if its element type can.
    }

    @Override
    public boolean useValues() {
        return false;
    }

    @Override
    public String getTypePrintName(boolean rawString) {
        return fmt("%sTypePrint", getTypeName());
    }

    @Override
    public String getElementTargetType() {
        return childInfos[0].getTargetType();
    }

    @Override
    public ExprCode getProjectedValue(ExprCode containerCode, ExprCode indexCode, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.add(containerCode);
        result.add(indexCode);
        String containerRef = constructReference(containerCode.getRawDataValue(), this, ctxt, result);
        String resultText = fmt("%sTypeProject(%s, %s)", getTypeName(), containerRef, indexCode.getData());

        result.setDestination(dest);
        if (typeUsesValues(childInfos[0])) {
            result.setDataValue(makeComputed(resultText));
        } else {
            result.setDataValue(makeReference(resultText));
        }
        return result;
    }

    @Override
    public CodeBox modifyContainer(VariableInformation containerVar, ExprCode partCode, ExprCode indexCode,
            CodeContext ctxt)
    {
        CodeBox modifyCode = ctxt.makeCodeBox();
        modifyCode.add(indexCode.getCode());
        modifyCode.add(partCode.getCode());

        String partValue;
        if (typeUsesValues(childInfos[0])) {
            partValue = partCode.getData();
        } else {
            partValue = constructReference(partCode.getRawDataValue(), childInfos[0], ctxt, modifyCode);
        }
        modifyCode.add("%sTypeModify(&%s, %s, %s);", getTypeName(), containerVar.targetRef, indexCode.getData(),
                partValue);
        return modifyCode;
    }

    @Override
    public ExprCode convertLiteral(ListExpression expr, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();

        // Find or make room to store the elements.
        String storeVar;
        if (dest == null) {
            VariableInformation tempVarInfo = ctxt.makeTempVariable(expr.getType(), "array_tmp");
            storeVar = tempVarInfo.targetRef;

            result.add(fmt("%s %s;", getTargetType(), storeVar));
            result.setDataValue(makeValue(storeVar));
        } else {
            storeVar = dest.getData();
        }

        // Copy the elements.
        Assert.check(expr.getElements().size() == length);
        for (int i = 0; i < length; i++) {
            String elementVar = fmt("(%s).data[%d]", storeVar, i);
            Destination elmDest = new Destination(null, childInfos[0], makeValue(elementVar));
            ExprCode elmCode = ctxt.exprToTarget(expr.getElements().get(i), elmDest);
            result.add(elmCode);
        }

        return result;
    }

    @Override
    public ExprCode convertSizeStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // Argument of the call is ignored!!

        String value = Integer.toString(length);

        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(makeLiteral(value));
        return result;
    }

    @Override
    public ExprCode convertEmptyStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        // Argument of the call is ignored!!

        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(makeLiteral(length == 0 ? "TRUE" : "FALSE"));
        return result;
    }

    @Override
    public String getTargetType() {
        return fmt("%sType", getTypeName());
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        CodeBox declCode = ctxt.makeCodeBox();
        CodeBox defCode = ctxt.makeCodeBox();

        generateC99Code(declCode, defCode, ctxt);
        declCode.add();

        ctxt.appendReplacement("generated-types", declCode.toString());
        ctxt.appendReplacement("type-support-code", defCode.toString());
    }

    /**
     * Generate C99 types and functions for manipulating arrays.
     *
     * @param declCode Declarations stream for the generated types and functions. Appended in-place.
     * @param defCode Implementation of the generated functions. Appended in-place.
     * @param ctxt The code generation context.
     */
    protected void generateC99Code(CodeBox declCode, CodeBox defCode, CodeContext ctxt) {
        String definitionPrefix, declarationPrefix;
        if (genLocalFunctions) {
            definitionPrefix = "static ";
            declarationPrefix = "static ";
        } else {
            definitionPrefix = "";
            declarationPrefix = "extern ";
        }

        String typeName = getTypeName();

        // The struct declaration.
        String cifTypeText = CifTextUtils.typeToStr(cifType);
        declCode.add("/* CIF type: %s */", cifTypeText);
        declCode.add("struct %s_struct {", typeName);
        declCode.indent();
        declCode.add("%s data[%d];", getElementTargetType(), length);
        declCode.dedent();
        declCode.add("};");
        declCode.add("typedef struct %s_struct %sType;", typeName, typeName);
        declCode.add();

        // Support code.

        // Equality.
        String header = fmt("BoolType %sTypeEquals(%s *left, %s *right)", typeName, getTargetType(), getTargetType());
        defCode.add("/**");
        defCode.add(" * Compare two arrays for equality.");
        defCode.add(" * @param left First array to compare.");
        defCode.add(" * @param right Second array to compare.");
        defCode.add(" * @return Whether both arrays are the same.");
        defCode.add(" */");
        defCode.add("%s%s {", definitionPrefix, header);
        declCode.add("%s%s;", declarationPrefix, header);

        defCode.indent();
        defCode.add("if (left == right) return TRUE;");

        if (typeSupportsRawMemCmp(childInfos[0])) {
            defCode.add("return memcmp(left, right, sizeof(%s)) == 0;", getTargetType());
        } else {
            String elmEqualsTemplate = childInfos[0].getBinaryExpressionTemplate(BinaryOperator.EQUAL, ctxt);

            defCode.add("int i;");
            defCode.add("for (i = 0; i < %d; i++) {", length);
            defCode.indent();

            DataValue leftValue = makeValue("left->data[i]");
            DataValue rightValue = makeValue("right->data[i]");
            String check = convertBinaryExpressionValues(leftValue, rightValue, elmEqualsTemplate);
            defCode.add("if (!(%s)) return FALSE;", check);
            defCode.dedent();
            defCode.add("}");
            defCode.add("return TRUE;");
        }
        defCode.dedent();
        defCode.add("}");
        defCode.add();

        boolean elmUsesValues = typeUsesValues(childInfos[0]);
        String elmTypeDecl = elmUsesValues ? (childInfos[0].getTargetType() + " ")
                : (childInfos[0].getTargetType() + " *");

        // Projection "Element TYPE_project(list, index)".
        header = fmt("%s%sTypeProject(%s *array, IntType index)", elmTypeDecl, typeName, getTargetType());
        defCode.add("/**");
        defCode.add(" * Extract an element from the array.");
        defCode.add(" * @param array Array with value to retrieve.");
        defCode.add(" * @param index Element index in the array (not normalized).");
        defCode.add(" * @return Element value at the indicated index from the array.");
        defCode.add(" */");
        defCode.add("%s%s {", definitionPrefix, header);
        declCode.add("%s%s;", declarationPrefix, header);

        defCode.indent();
        defCode.add("if (index < 0) index += %d; /* Normalize index. */", length);
        defCode.add("assert(index >= 0 && index < %d);", length);
        defCode.add();
        if (elmUsesValues) {
            defCode.add("return array->data[index];");
        } else {
            defCode.add("return &array->data[index];");
        }
        defCode.dedent();
        defCode.add("}");
        defCode.add();

        // In-place modification void TYPE_modify(list, int, element)".
        header = fmt("void %sTypeModify(%s *array, IntType index, %svalue)", typeName, getTargetType(), elmTypeDecl);
        defCode.add("/**");
        defCode.add(" * In-place change of the array.");
        defCode.add(" * @param array Array to modify.");
        defCode.add(" * @param index Element index in the array (not normalized).");
        defCode.add(" * @param value New value to copy into the array.");
        defCode.add(" */");
        defCode.add("%s%s {", definitionPrefix, header);
        declCode.add("%s%s;", declarationPrefix, header);

        defCode.indent();
        defCode.add("if (index < 0) index += %d; /* Normalize index. */", length);
        defCode.add("assert(index >= 0 && index < %d);", length);
        defCode.add();
        if (elmUsesValues) {
            defCode.add("array->data[index] = value;");
        } else {
            defCode.add("memcpy(&array->data[index], value, sizeof(%s));", getElementTargetType());
        }
        defCode.dedent();
        defCode.add("}");
        defCode.add();

        // TYPE TypePrint(&value, char *dst, int start, int end)
        header = fmt("int %sTypePrint(%s *array, char *dest, int start, int end)", typeName, getTargetType());
        defCode.add("/**");
        defCode.add(" * Append textual representation of the array value into the provided");
        defCode.add(" * destination, space permitting.");
        defCode.add(" * @param array Array to print.");
        defCode.add(" * @param dest Destination to write text to.");
        defCode.add(" * @param start First available offset in \\a dest for new text.");
        defCode.add(" * @param end Fist offset behind \\a dest.");
        defCode.add(" * @return First free offset in \\a dest, mat be \\a end.");
        defCode.add(" */");
        defCode.add("%s%s {", definitionPrefix, header);
        declCode.add("%s%s;", declarationPrefix, header);

        defCode.indent();
        defCode.add("int last = end - 1;");
        defCode.add("if (start < last) { dest[start++] = '['; }");
        defCode.add("int index;");
        defCode.add("for (index = 0; index < %d; index++) {", length);
        defCode.indent();
        defCode.add("if (index > 0) {");
        defCode.indent();
        defCode.add("if (start < last) { dest[start++] = ','; }");
        defCode.add("if (start < last) { dest[start++] = ' '; }");
        defCode.dedent();
        defCode.add("}");
        String childArgument = typeUsesValues(childInfos[0]) ? "array->data[index]" : "&array->data[index]";
        defCode.add("start = %s(%s, dest, start, end);", typeGetTypePrintName(childInfos[0], false), childArgument);
        defCode.dedent();
        defCode.add("}");
        defCode.add("if (start < last) { dest[start++] = ']'; }");
        defCode.add("dest[start] = '\\0';");
        defCode.add("return start;");
        defCode.dedent();
        defCode.add("}");
        defCode.add();
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
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return fmt("%sTypeEquals(${left-ref}, ${right-ref})", getTypeName());
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return fmt("!%sTypeEquals(${left-ref}, ${right-ref})", getTypeName());
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof C99ArrayTypeInfo)) {
            return false;
        }
        C99ArrayTypeInfo otherArray = (C99ArrayTypeInfo)other;
        if (!childInfos[0].equals(otherArray.childInfos[0])) {
            return false;
        }
        return length == otherArray.length;
    }

    @Override
    public int hashCode() {
        int h = C99ArrayTypeInfo.class.hashCode();
        h = h + childInfos[0].hashCode();
        return h + length;
    }

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        ListType lhsList = (ListType)lhsType;
        ListType rhsList = (ListType)rhsType;
        if (checkTypeCompat(lhsList.getElementType(), rhsList.getElementType(), RangeCompat.CONTAINED)) {
            return;
        }

        String indexName = fmt("rng_index%d", level);
        String elemName = fmt("rng_elem%d", level);
        code.add("for(int %s = 0; %s < %d; %s++) {", indexName, indexName, length, indexName);
        code.indent();

        boolean elmUsesValues = typeUsesValues(childInfos[0]);

        // ElementType element = list->data[index];
        String elementText;
        if (rhsValue.isReferenceValue()) {
            elementText = fmt("(%s)->data[%s]", rhsValue.getReference(), indexName);
        } else {
            elementText = fmt("(%s).data[%s]", rhsValue.getData(), indexName);
        }

        DataValue elmRhsValue;
        if (elmUsesValues) {
            code.add("%s %s = %s;", childInfos[0].getTargetType(), elemName, elementText);
            elmRhsValue = makeValue(elemName);
        } else {
            code.add("%s *%s = &(%s);", childInfos[0].getTargetType(), elemName, elementText);
            elmRhsValue = makeReference(elemName);
        }

        // Check element value with element type information object.
        int last = errorTexts.size();
        errorTexts.add(new RangeCheckErrorLevelText(true, indexName));
        childInfos[0].checkRange(lhsList.getElementType(), rhsList.getElementType(), elmRhsValue, varType, varName,
                errorTexts, level + 1, code, ctxt);
        errorTexts.remove(last);
        Assert.check(last == errorTexts.size());

        code.dedent();
        code.add("}");
    }
}
