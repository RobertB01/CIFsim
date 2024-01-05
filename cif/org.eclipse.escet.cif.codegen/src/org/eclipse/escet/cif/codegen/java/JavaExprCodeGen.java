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

package org.eclipse.escet.cif.codegen.java;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeInitialUppercase;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprCodeGen;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.common.java.Assert;

/** Expression code generator for the Java target language. */
public class JavaExprCodeGen extends ExprCodeGen {
    /**
     * Convert bool, int, real, or string type to textual representation in the cast conversion functions, in lower
     * case.
     *
     * @param type Type to convert.
     * @return Text denoting the type in the cast functions.
     */
    private String typeToCastString(CifType type) {
        if (type instanceof BoolType) {
            return "bool";
        } else if (type instanceof IntType) {
            return "int";
        } else if (type instanceof RealType) {
            return "real";
        } else if (type instanceof StringType) {
            return "str";
        }

        String msg = "Unexpected type: " + type;
        throw new RuntimeException(msg);
    }

    @Override
    protected ExprCode convertCastExpression(CifType exprType, CifType childType, Expression child, Destination dest,
            CodeContext ctxt)
    {
        ExprCode childCode = ctxt.exprToTarget(child, null);

        ExprCode result = new ExprCode();
        result.add(childCode);
        String childValue = childCode.getData();

        String childPart = typeToCastString(childType);
        String resultPart = makeInitialUppercase(typeToCastString(exprType));
        String resultText = fmt("%sTo%s(%s)", childPart, resultPart, childValue);

        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(resultText));
        return result;
    }

    @Override
    protected ExprCode convertIfExpression(IfExpression expr, Destination dest, CodeContext ctxt) {
        // The chain of ?: expressions will break when code needs to be
        // executed beforehand. Since the Java code generator is not doing
        // that, lack of code is simply checked, and further ignored.
        ExprCode result = new ExprCode();

        // Start with 'else'.
        ExprCode elseCode = exprToTarget(expr.getElse(), null, ctxt);
        Assert.check(!elseCode.hasCode());
        result.add(elseCode);
        String resultText = elseCode.getData();

        // Wrap 'elifs' around else.
        for (int i = expr.getElifs().size() - 1; i >= 0; i--) {
            ElifExpression elif = expr.getElifs().get(i);
            ExprCode guardCode = predsToTarget(elif.getGuards(), ctxt);
            ExprCode thenCode = exprToTarget(elif.getThen(), null, ctxt);
            Assert.check(!guardCode.hasCode());
            Assert.check(!thenCode.hasCode());
            resultText = fmt("(%s) ? %s : (%s)", guardCode.getData(), thenCode.getData(), resultText);
        }

        // Wrap 'if' around 'elifs/else'.
        ExprCode guardCode = predsToTarget(expr.getGuards(), ctxt);
        ExprCode thenCode = exprToTarget(expr.getThen(), null, ctxt);
        Assert.check(!guardCode.hasCode());
        Assert.check(!thenCode.hasCode());
        resultText = fmt("(%s) ? %s : (%s)", guardCode.getData(), thenCode.getData(), resultText);

        // Return final result.
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(resultText));
        return result;
    }

    @Override
    protected ExprCode convertInternalFunctionCall(InternalFunction func, List<ExprCode> argsCode, Destination dest,
            CodeContext ctxt)
    {
        ExprCode result = new ExprCode();
        for (ExprCode argCode: argsCode) {
            result.add(argCode);
        }

        StringBuilder callText = new StringBuilder();
        callText.append(ctxt.getFunctionName(func));
        callText.append('(');
        boolean first = true;
        for (ExprCode argCode: argsCode) {
            if (!first) {
                callText.append(", ");
            }
            first = false;
            callText.append(argCode.getData());
        }
        callText.append(')');

        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(callText.toString()));
        return result;
    }

    @Override
    protected ExprCode convertConstantExpression(ConstantExpression expr, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        VariableWrapper var = new VariableWrapper(expr.getConstant(), false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(varInfo.targetRef));
        return result;
    }

    @Override
    public ExprCode convertDiscVariableExpression(DiscVariable discVar, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        VariableWrapper var = new VariableWrapper(discVar, false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(varInfo.targetRef));
        return result;
    }

    @Override
    protected ExprCode convertAlgVariableExpression(AlgVariable algVar, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        VariableWrapper var = new VariableWrapper(algVar, false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        String resultText;
        if (varInfo.isTempVar) {
            resultText = varInfo.targetRef;
        } else {
            resultText = fmt("%s()", varInfo.targetRef);
        }
        result.setDataValue(new JavaDataValue(resultText));
        result.setDestination(dest);
        return result;
    }

    @Override
    protected ExprCode convertContVariableExpression(ContVariable contVar, boolean isDerivative, Destination dest,
            CodeContext ctxt)
    {
        VariableWrapper var = new VariableWrapper(contVar, isDerivative);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        String varName = varInfo.targetRef;

        ExprCode result = new ExprCode();
        result.setDestination(dest);
        if (!varInfo.isTempVar && isDerivative) {
            String resultText = fmt("%sderiv()", varName);
            result.setDataValue(new JavaDataValue(resultText));
        } else {
            result.setDataValue(new JavaDataValue(varName));
        }
        return result;
    }

    @Override
    protected ExprCode convertInputVariableExpression(InputVariableExpression expr, Destination dest,
            CodeContext ctxt)
    {
        ExprCode result = new ExprCode();
        VariableWrapper var = new VariableWrapper(expr.getVariable(), false);
        VariableInformation varInfo = ctxt.getReadVarInfo(var);
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(varInfo.targetRef));
        return result;
    }

    @Override
    protected ExprCode predTextsToTarget(List<ExprCode> predCodes, CodeContext ctxt) {
        if (predCodes.isEmpty()) {
            ExprCode result = new ExprCode();
            result.setDataValue(new JavaDataValue("true"));
            return result;
        }
        if (predCodes.size() == 1) {
            return first(predCodes);
        }

        // Add parentheses around each expression.
        ExprCode result = new ExprCode();
        String[] preds = new String[predCodes.size()];
        for (int i = 0; i < predCodes.size(); i++) {
            // Due to short-circuit evaluation, only code for the first
            // condition may be evaluated. Since the Java code generator
            // shouldn't generate pre-execute code for conditions, an
            // assertion check suffices.
            if (i == 0) {
                result.add(predCodes.get(i));
            } else {
                Assert.check(!predCodes.get(i).hasCode());
            }
            preds[i] = fmt("(%s)", predCodes.get(i).getData());
        }
        result.setDataValue(new JavaDataValue(String.join(" && ", preds)));
        return result;
    }
}
