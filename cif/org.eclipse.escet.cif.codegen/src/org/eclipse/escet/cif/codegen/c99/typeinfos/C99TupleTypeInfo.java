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

import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.c99.typeinfos.C99TypeInfoHelper.typeGetTypePrintName;
import static org.eclipse.escet.cif.codegen.c99.typeinfos.C99TypeInfoHelper.typeSupportsRawMemCmp;
import static org.eclipse.escet.cif.codegen.c99.typeinfos.C99TypeInfoHelper.typeUsesValues;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionValues;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.codegen.typeinfos.TupleTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Type information about a CIF tuple type for the C99 target language. */
public class C99TupleTypeInfo extends TupleTypeInfo implements C99TypeInfo {
    /**
     * If set, generate functions only available in the current source file, else generate globally accessible
     * functions.
     */
    public final boolean genLocalFunctions;

    /**
     * Constructor for {@link C99TupleTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     * @param fieldTypes Types of the fields.
     */
    public C99TupleTypeInfo(boolean genLocalFunctions, CifType cifType, TypeInfo[] fieldTypes) {
        super(cifType, fieldTypes);
        this.genLocalFunctions = genLocalFunctions;
    }

    @Override
    public boolean supportRawMemCmp() {
        return false; // C99 may introduce padding between fields with undefined values.
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
    public ExprCode convertLiteral(TupleExpression expr, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();

        // Find or make room to store the elements.
        String storeVar;
        if (dest == null) {
            VariableInformation tempVarInfo = ctxt.makeTempVariable(expr.getType(), "tuple_tmp");
            storeVar = tempVarInfo.targetName;

            result.add(fmt("%s %s;", getTargetType(), storeVar));
            result.setDataValue(makeValue(storeVar));
        } else {
            storeVar = dest.getData();
        }

        // Copy the elements.
        Assert.check(expr.getFields().size() == childInfos.length);
        for (int i = 0; i < childInfos.length; i++) {
            String elementVar = fmt("(%s)._field%d", storeVar, i);
            Destination elmDest = new Destination(null, childInfos[0], makeValue(elementVar));
            ExprCode elmCode = ctxt.exprToTarget(expr.getFields().get(i), elmDest);
            result.add(elmCode);
        }

        return result;
    }

    @Override
    public CodeBox modifyContainer(VariableInformation containerVar, ExprCode partCode, int index, CodeContext ctxt) {
        CodeBox code = ctxt.makeCodeBox();
        code.add(partCode.getCode());

        String fieldText = fmt("%s._field%d", containerVar.targetName, index);
        Destination dest = new Destination(null, childInfos[index], makeValue(fieldText));
        childInfos[index].storeValue(code, partCode.getRawDataValue(), dest);
        return code;
    }

    @Override
    public ExprCode getProjectedValue(ExprCode childCode, int index, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.add(childCode);
        result.setDestination(dest);
        DataValue dataValue = childCode.getRawDataValue();
        if (dataValue.isReferenceValue()) {
            result.setDataValue(makeValue(fmt("(%s)->_field%d", dataValue.getReference(), index)));
        } else {
            result.setDataValue(makeValue(fmt("(%s)._field%d", dataValue.getData(), index)));
        }
        return result;
    }

    @Override
    public String appendProjection(String value, boolean safe, int index) {
        return safe ? fmt("%s._field%d", value, index) : fmt("(%s)._field%d", value, index);
    }

    @Override
    public int getSize() {
        return childInfos.length;
    }

    @Override
    public String getTargetType() {
        return fmt("%sType", getTypeName());
    }

    @Override
    public void generateCode(CodeContext ctxt) {
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
        CodeBox declCode = ctxt.makeCodeBox();

        String cifTypeText = CifTextUtils.typeToStr(cifType);
        declCode.add("/* CIF type: %s */", cifTypeText);
        declCode.add("struct %s_struct {", typeName);
        declCode.indent();
        for (int i = 0; i < childInfos.length; i++) {
            declCode.add("%s _field%s;", childInfos[i].getTargetType(), i);
        }
        declCode.dedent();
        declCode.add("};");
        declCode.add("typedef struct %s_struct %sType;", typeName, typeName);
        declCode.add();

        // Support code.
        CodeBox defCode = ctxt.makeCodeBox();

        // Equality.
        String header = fmt("BoolType %sTypeEquals(%s *left, %s *right)", typeName, getTargetType(), getTargetType());
        defCode.add("/**");
        defCode.add(" * Compare two tuples for equality.");
        defCode.add(" * @param left First tuple to compare.");
        defCode.add(" * @param right Second tuple to compare.");
        defCode.add(" * @return Whether both tuples are the same.");
        defCode.add(" */");
        defCode.add("%s%s {", definitionPrefix, header);
        declCode.add("%s%s;", declarationPrefix, header);

        defCode.indent();
        defCode.add("if (left == right) return TRUE;");
        for (int i = 0; i < childInfos.length; i++) {
            TypeInfo childTi = childInfos[i];
            String leftField = fmt("left->_field%d", i);
            String rightField = fmt("right->_field%d", i);

            if (typeSupportsRawMemCmp(childTi)) {
                defCode.add("if (memcmp(&%s, &%s, sizeof(%s)) != 0) return FALSE;", leftField, rightField,
                        childTi.getTargetType());
            } else {
                String elmEqualsTemplate = childTi.getBinaryExpressionTemplate(BinaryOperator.EQUAL);

                DataValue leftValue = makeValue(leftField);
                DataValue rightValue = makeValue(rightField);
                String check = convertBinaryExpressionValues(leftValue, rightValue, elmEqualsTemplate);
                defCode.add("if (!(%s)) return FALSE;", check);
            }
        }
        defCode.add("return TRUE;");
        defCode.dedent();
        defCode.add("}");
        defCode.add();

        // TYPE TypePrint(&value, char *dst, int start, int end)
        header = fmt("int %sTypePrint(%s *tuple, char *dest, int start, int end)", typeName, getTargetType());
        defCode.add("/**");
        defCode.add(" * Append textual representation of the tuple value into the provided");
        defCode.add(" * destination, space permitting.");
        defCode.add(" * @param tuple Tuple to print.");
        defCode.add(" * @param dest Destination to write text to.");
        defCode.add(" * @param start First available offset in \\a dest for new text.");
        defCode.add(" * @param end Fist offset behind \\a dest.");
        defCode.add(" * @return First free offset in \\a dest, mat be \\a end.");
        defCode.add(" */");
        defCode.add("%s%s {", definitionPrefix, header);
        declCode.add("%s%s;", declarationPrefix, header);

        defCode.indent();
        defCode.add("int last = end - 1;");
        defCode.add("if (start < last) { dest[start++] = '('; }");
        for (int i = 0; i < childInfos.length; i++) {
            TypeInfo childTi = childInfos[i];

            if (i > 0) {
                defCode.add("if (start < last) { dest[start++] = ','; }");
                defCode.add("if (start < last) { dest[start++] = ' '; }");
            }
            String childArgument = fmt("tuple->_field%d", i);
            if (!typeUsesValues(childTi)) {
                childArgument = "&" + childArgument;
            }
            defCode.add("start = %s(%s, dest, start, end);", typeGetTypePrintName(childTi, false), childArgument);
        }
        defCode.add("if (start < last) { dest[start++] = ')'; }");
        defCode.add("dest[start] = '\\0';");
        defCode.add("return start;");
        defCode.dedent();
        defCode.add("}");
        defCode.add();

        // Output generated code to the replacements.
        declCode.add();
        ctxt.appendReplacement("generated-types", declCode.toString());
        ctxt.appendReplacement("type-support-code", defCode.toString());
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
        if (!(other instanceof C99TupleTypeInfo)) {
            return false;
        }
        C99TupleTypeInfo otherTuple = (C99TupleTypeInfo)other;
        if (childInfos.length != otherTuple.childInfos.length) {
            return false;
        }
        for (int i = 0; i < childInfos.length; i++) {
            if (!childInfos[i].equals(otherTuple.childInfos[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = C99TupleTypeInfo.class.hashCode();
        for (int i = 0; i < childInfos.length; i++) {
            hash += childInfos[i].hashCode() * (i + 16);
        }
        return hash;
    }

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        // Push the range check down to the fields, which generates nothing if
        // field type has no partial overlap problem.
        TupleType lhsTuple = (TupleType)lhsType;
        TupleType rhsTuple = (TupleType)rhsType;
        int last = errorTexts.size();
        errorTexts.add(null);
        for (int i = 0; i < childInfos.length; i++) {
            errorTexts.set(last, new RangeCheckErrorLevelText(false, lhsTuple.getFields().get(i).getName()));
            childInfos[i].checkRange(lhsTuple.getFields().get(i).getType(), rhsTuple.getFields().get(i).getType(),
                    makeValue(appendProjection(rhsValue.getData(), false, i)), varType, varName, errorTexts, level,
                    code, ctxt);
        }
        errorTexts.remove(last);
        Assert.check(last == errorTexts.size());
    }
}
