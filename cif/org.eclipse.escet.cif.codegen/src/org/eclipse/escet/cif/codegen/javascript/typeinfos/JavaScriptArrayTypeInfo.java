//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifTypeUtils.checkTypeCompat;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.java.JavaDataValue;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.ArrayTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.RangeCheckErrorLevelText;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** JavaScript type information about an array type. */
public class JavaScriptArrayTypeInfo extends ArrayTypeInfo {
    /**
     * Constructor of the {@link JavaScriptArrayTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param childTIs Element type information, array of length 1.
     * @param length Number of elements in the array.
     */
    public JavaScriptArrayTypeInfo(CifType cifType, TypeInfo[] childTIs, int length) {
        super(cifType, childTIs, -1); // List length is not relevant.
    }

    @Override
    public String getElementTargetType() {
        return "var";
    }

    @Override
    public String getTargetType() {
        return "var";
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        // Nothing to generate.
    }

    @Override
    public void storeValue(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("var %s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        // Use 'equalObjs' instead of '==' to avoid object equality for two array objects.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return fmt("%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return fmt("!%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public ExprCode convertLiteral(ListExpression expr, Destination dest, CodeContext ctxt) {
        // Generate list construction code.
        String emptyList = "[]";

        // Special case for empty lists.
        if (expr.getElements().size() == 0) {
            ExprCode result = new ExprCode();
            result.setDestination(dest);
            result.setDataValue(new JavaDataValue(emptyList));
            return result;
        }

        // For non-empty lists, add the elements.
        ExprCode result = new ExprCode();

        StringBuilder listMaker = new StringBuilder();
        listMaker.append('[');
        boolean first = true;
        for (Expression elm: expr.getElements()) {
            if (!first) {
                listMaker.append(", ");
            }
            first = false;
            ExprCode elmCode = ctxt.exprToTarget(elm, null);
            result.add(elmCode);
            listMaker.append(elmCode.getData());
        }
        listMaker.append(']');

        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(listMaker.toString()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof JavaScriptArrayTypeInfo)) {
            return false;
        }
        JavaScriptArrayTypeInfo otherArray = (JavaScriptArrayTypeInfo)other;
        return childInfos[0].equals(otherArray.childInfos[0]);
    }

    @Override
    public int hashCode() {
        int h = JavaScriptArrayTypeInfo.class.hashCode();
        h = h + childInfos[0].hashCode();
        return h;
    }

    @Override
    public ExprCode convertSizeStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        ExprCode childCode = ctxt.exprToTarget(expr, null);

        ExprCode result = new ExprCode();
        result.add(childCode);

        String value = fmt("%sUtils.sizeList(" + childCode.getData() + ")", ctxt.getPrefix());
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(value));
        return result;
    }

    @Override
    public ExprCode convertEmptyStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        ExprCode childCode = ctxt.exprToTarget(expr, null);

        ExprCode result = new ExprCode();
        result.add(childCode);

        String value = fmt("%sUtils.empty(" + childCode.getData() + ")", ctxt.getPrefix());
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(value));
        return result;
    }

    @Override
    public ExprCode getProjectedValue(ExprCode childCode, ExprCode indexCode, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.add(childCode);
        result.add(indexCode);
        String resultText = fmt("%sUtils.projectList(%s, %s)", ctxt.getPrefix(), childCode.getData(),
                indexCode.getData());
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(resultText));
        return result;
    }

    @Override
    public CodeBox modifyContainer(VariableInformation containerVar, ExprCode partCode, ExprCode indexCode,
            CodeContext ctxt)
    {
        ExprCode result = new ExprCode();
        result.add(indexCode);
        result.add(partCode);
        String resultText = fmt("%sUtils.modify(%s, %s, %s)", ctxt.getPrefix(), containerVar.targetRef,
                indexCode.getData(), partCode.getData());
        result.setDestination(ctxt.makeDestination(containerVar));
        result.setDataValue(new JavaScriptDataValue(resultText));
        return result.getCode();
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
        code.add("for (var %s = 0; %s < %s.length; %s++) {", indexName, indexName, rhsValue.getData(), indexName);
        code.indent();
        code.add("var %s = %s[%s];", elemName, rhsValue.getData(), indexName);

        int last = errorTexts.size();
        errorTexts.add(new RangeCheckErrorLevelText(true, indexName));
        childInfos[0].checkRange(lhsList.getElementType(), rhsList.getElementType(), new JavaScriptDataValue(elemName),
                varType, varName, errorTexts, level + 1, code, ctxt);
        errorTexts.remove(last);
        Assert.check(last == errorTexts.size());

        code.dedent();
        code.add("}");
    }
}
