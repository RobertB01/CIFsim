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

package org.eclipse.escet.cif.codegen.javascript;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.ExprCodeGen;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;

/** Expression code generator for the JavaScript target language. */
public class JavaScriptExprCodeGen extends ExprCodeGen {
    /**
     * Convert bool, int, real, or string type to textual representation in the cast conversion functions, in lower
     * case.
     *
     * @param type Type to convert.
     * @return Text denoting the type in the cast functions.
     */
    @SuppressWarnings("unused")
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
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode convertIfExpression(IfExpression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode convertInternalFunctionCall(InternalFunction func, List<ExprCode> argsCode, Destination dest,
            CodeContext ctxt)
    {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode convertConstantExpression(ConstantExpression expr, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public ExprCode convertDiscVariableExpression(DiscVariable discVar, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode convertAlgVariableExpression(AlgVariable algVar, Destination dest, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode convertContVariableExpression(ContVariable contVar, boolean isDerivative, Destination dest,
            CodeContext ctxt)
    {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode convertInputVariableExpression(InputVariableExpression expr, Destination dest,
            CodeContext ctxt)
    {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected ExprCode predTextsToTarget(List<ExprCode> predCodes, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }
}
