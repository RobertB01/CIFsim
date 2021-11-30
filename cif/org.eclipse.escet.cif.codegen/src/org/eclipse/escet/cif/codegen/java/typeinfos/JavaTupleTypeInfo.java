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

package org.eclipse.escet.cif.codegen.java.typeinfos;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.java.JavaDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.codegen.typeinfos.TupleTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;

/** Java type information about the tuple type. */
public class JavaTupleTypeInfo extends TupleTypeInfo {
    /**
     * Constructor of the {@link JavaTupleTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param fieldTIs Fields type information.
     */
    public JavaTupleTypeInfo(CifType cifType, TypeInfo[] fieldTIs) {
        super(cifType, fieldTIs);
    }

    @Override
    public String getTargetType() {
        return "CifTuple_" + getTypeName();
    }

    @Override
    public int getSize() {
        return childInfos.length;
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        String className = getTargetType();
        String cifTypeText = CifTextUtils.typeToStr(cifType);

        // Generate code for Java class for the tuple type.
        CodeBox code = ctxt.makeCodeBox(1);
        code.add();
        code.add("/** Tuple class for CIF tuple type representative \"%s\". */", cifTypeText);
        code.add("public static class %s extends CifTupleBase<%s> {", className, className);
        code.indent();

        // Determine field names.
        String[] names = new String[childInfos.length];
        String[] paramTxts = new String[childInfos.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = fmt("_field%d", i);
            paramTxts[i] = fmt("%s %s", childInfos[i].getTargetType(), names[i]);
        }

        // Add fields.
        for (int i = 0; i < names.length; i++) {
            code.add("/** The %s field. */", Numbers.toOrdinal(i + 1));
            code.add("public %s %s;", childInfos[i].getTargetType(), names[i]);
            code.add();
        }

        // Add constructor.
        code.add("/**");
        code.add(" * Constructor for the {@link %s} class.", className);
        code.add(" *");
        for (int i = 0; i < names.length; i++) {
            code.add(" * @param %s The %s field.", names[i], Numbers.toOrdinal(i + 1));
        }
        code.add(" */");
        code.add("public %s(%s) {", className, StringUtils.join(paramTxts, ", "));
        code.indent();
        for (String name: names) {
            code.add("this.%s = %s;", name, name);
        }
        code.dedent();
        code.add("}");

        // Add 'copy' method, for use in tuple addressables of assignments.
        code.add();
        code.add("@Override");
        code.add("public %s copy() {", className);
        code.indent();
        code.add("return new %s(%s);", className, StringUtils.join(names, ", "));
        code.dedent();
        code.add("}");

        // Add 'hashCode' method.
        code.add();
        code.add("@Override");
        code.add("public int hashCode() {");
        code.indent();
        code.add("return hashObjs(%s);", StringUtils.join(names, ", "));
        code.dedent();
        code.add("}");

        // Add 'equals' method.
        code.add();
        code.add("@Override");
        code.add("public boolean equals(Object obj) {");
        code.indent();
        code.add("if (this == obj) return true;");
        code.add("%s other = (%s)obj;", className, className);
        for (int i = 0; i < names.length; i++) {
            code.add("%s equalObjs(this.%s, other.%s)%s", (i == 0) ? "return" : "      ", names[i], names[i],
                    (i == names.length - 1) ? ";" : " &&");
        }
        code.dedent();
        code.add("}");

        // Add 'toString' method.
        code.add();
        code.add("@Override");
        code.add("public String toString() {");
        code.indent();
        code.add("StringBuilder rslt = new StringBuilder();");
        code.add("rslt.append(\"(\");");
        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                code.add("rslt.append(\", \");");
            }
            code.add("rslt.append(valueToStr(%s));", names[i]);
        }
        code.add("rslt.append(\")\");");
        code.add("return rslt.toString();");
        code.dedent();
        code.add("}");

        // Close tuple type class.
        code.dedent();
        code.add("}");

        // Add new code.
        String newTupleCode = code.toString();
        ctxt.appendReplacement("java-tuples-code", "\n" + newTupleCode);
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
        // Use 'equalObjs' instead of '==' to avoid object equality for two tuple objects.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return "equalObjs(${left-value}, ${right-value})";
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return "!equalObjs(${left-value}, ${right-value})";
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public ExprCode getProjectedValue(ExprCode childCode, int index, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.add(childCode);
        String resultText = appendProjection(childCode.getData(), false, index);
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(resultText));
        return result;
    }

    @Override
    public String appendProjection(String value, boolean safe, int index) {
        return safe ? fmt("%s._field%d", value, index) : fmt("(%s)._field%d", value, index);
    }

    @Override
    public CodeBox modifyContainer(VariableInformation containerInfo, ExprCode partCode, int index, CodeContext ctxt) {
        // Modify the field of the destination.
        CodeBox code = ctxt.makeCodeBox();
        code.add(partCode.getCode());
        code.add("%s = %s;", appendProjection(containerInfo.targetName, true, index), partCode.getData());
        return code;
    }

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        // 'is type contained' check also recurses down to the elements, might
        // as well do that immediately.
        TupleType lhsTuple = (TupleType)lhsType;
        TupleType rhsTuple = (TupleType)rhsType;
        int last = errorTexts.size();
        errorTexts.add(null);
        for (int i = 0; i < childInfos.length; i++) {
            errorTexts.set(last, new RangeCheckErrorLevelText(false, lhsTuple.getFields().get(i).getName()));
            childInfos[i].checkRange(lhsTuple.getFields().get(i).getType(), rhsTuple.getFields().get(i).getType(),
                    new JavaDataValue(appendProjection(rhsValue.getData(), false, i)), varType, varName, errorTexts,
                    level, code, ctxt);
        }
        errorTexts.remove(last);
        Assert.check(last == errorTexts.size());
    }

    @Override
    public ExprCode convertLiteral(TupleExpression expr, Destination dest, CodeContext ctxt) {
        String className = getTargetType();

        ExprCode result = new ExprCode();
        StringBuilder constructorCall = new StringBuilder();
        constructorCall.append("new ");
        constructorCall.append(className);
        constructorCall.append('(');
        boolean first = true;
        for (Expression arg: expr.getFields()) {
            if (!first) {
                constructorCall.append(", ");
            }
            first = false;
            ExprCode fieldValue = ctxt.exprToTarget(arg, null);
            result.add(fieldValue);
            constructorCall.append(fieldValue.getData());
        }
        constructorCall.append(')');

        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(constructorCall.toString()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof JavaTupleTypeInfo)) {
            return false;
        }
        JavaTupleTypeInfo otherTuple = (JavaTupleTypeInfo)other;
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
        int h = JavaTupleTypeInfo.class.hashCode() + childInfos.length;
        for (int i = 0; i < childInfos.length; i++) {
            h = h + childInfos[i].hashCode();
        }
        return h;
    }
}
