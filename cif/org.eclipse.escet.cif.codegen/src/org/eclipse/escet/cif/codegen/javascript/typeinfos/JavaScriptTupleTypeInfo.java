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

package org.eclipse.escet.cif.codegen.javascript.typeinfos;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptDataValue;
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

/** JavaScript type information about the tuple type. */
public class JavaScriptTupleTypeInfo extends TupleTypeInfo {
    /** Name of the main object in the generated code. Is used as prefix to ensure fully-qualified variable names. */
    private final String prefix;

    /**
     * Constructor of the {@link JavaScriptTupleTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param fieldTIs Fields type information.
     * @param prefix Name of the main object in the generated code. Is used as prefix to ensure fully-qualified variable
     *     names.
     */
    public JavaScriptTupleTypeInfo(CifType cifType, TypeInfo[] fieldTIs, String prefix) {
        super(cifType, fieldTIs);
        this.prefix = prefix;
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

        // Generate code for JavaScript class for the tuple type.
        CodeBox code = ctxt.makeCodeBox(3);
        code.add();
        code.add("/** Tuple class for CIF tuple type representative \"%s\". */", cifTypeText);
        code.add("class %s {", className);
        code.indent();

        // Determine field names.
        String[] names = new String[childInfos.length];
        String[] paramTxts = new String[childInfos.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = fmt("_field%d", i);
            paramTxts[i] = fmt("%s", names[i]);
        }

        // Add fields.
        for (int i = 0; i < names.length; i++) {
            code.add("/** The %s field. */", Numbers.toOrdinal(i + 1));
            code.add("%s;", names[i]);
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
        code.add("constructor(%s) {", String.join(", ", paramTxts));
        code.indent();
        for (String name: names) {
            code.add("%s.%s = %s;", ctxt.getPrefix(), name, name);
        }
        code.dedent();
        code.add("}");

        // Add 'copy' method, for use in tuple addressables of assignments.
        code.add();
        code.add("copy() {");
        code.indent();
        code.add("return new %s(%s);", className, String.join(", ", names));
        code.dedent();
        code.add("}");

        // Add 'equals' method.
        code.add();
        code.add("equals(obj) {");
        code.indent();
        code.add("if (this === obj) return true;");
        code.add("var other = obj;");
        for (int i = 0; i < names.length; i++) {
            code.add("%s %sUtils.equalObjs(%s.%s, other.%s)%s", (i == 0) ? "return" : "      ", ctxt.getPrefix(),
                    ctxt.getPrefix(), names[i], names[i], (i == names.length - 1) ? ";" : " &&");
        }
        code.dedent();
        code.add("}");

        // Add 'toString' method.
        code.add();
        code.add("toString() {");
        code.indent();
        code.add("var rslt = \"\";");
        code.add("rslt += \"(\";");
        for (int i = 0; i < names.length; i++) {
            if (i > 0) {
                code.add("rslt += \", \";");
            }
            code.add("rslt += %sUtils.valueToStr(%s);", ctxt.getPrefix(), names[i]);
        }
        code.add("rslt += \")\";");
        code.add("return rslt;");
        code.dedent();
        code.add("}");

        // Close tuple type class.
        code.dedent();
        code.add("}");

        // Add new code.
        String newTupleCode = code.toString();
        ctxt.appendReplacement("javascript-tuples-code", "\n" + newTupleCode);
    }

    @Override
    public void storeValue(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s.%s = %s;", this.prefix, dest.getData(), sourceValue.getData());
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("var %s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        // Use 'equalObjs' instead of '==' to avoid object equality for two tuple objects.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return fmt("%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return fmt("!%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public ExprCode getProjectedValue(ExprCode childCode, int index, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.add(childCode);
        String resultText = appendProjection(childCode.getData(), false, index);
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(resultText));
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
                    new JavaScriptDataValue(appendProjection(rhsValue.getData(), false, i)), varType, varName,
                    errorTexts, level, code, ctxt);
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
        result.setDataValue(new JavaScriptDataValue(constructorCall.toString()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof JavaScriptTupleTypeInfo)) {
            return false;
        }
        JavaScriptTupleTypeInfo otherTuple = (JavaScriptTupleTypeInfo)other;
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
        int h = JavaScriptTupleTypeInfo.class.hashCode() + childInfos.length;
        for (int i = 0; i < childInfos.length; i++) {
            h = h + childInfos[i].hashCode();
        }
        return h;
    }
}
