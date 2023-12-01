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

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.CurlyBraceIfElseGenerator;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.FunctionCodeGen;
import org.eclipse.escet.cif.codegen.IfElseGenerator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.common.box.CodeBox;

/** Function code generator for the JavaScript target language. */
public class JavaScriptFunctionCodeGen extends FunctionCodeGen {
    /**
     * Constructor of the {@link JavaScriptFunctionCodeGen} class.
     *
     * @param function Function to translate.
     */
    public JavaScriptFunctionCodeGen(InternalFunction function) {
        super(function);
    }

    /**
     * Generate the JavaScript code for a function.
     *
     * @param code Destination of the generated code.
     * @param ctxt Code context of the function.
     */
    public void generate(CodeBox code, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    /**
     * Add code for the body of the given function.
     *
     * @param func The internal user-defined function.
     * @param code The code. Is extended in-place.
     * @param ctxt Code generation context.
     */
    @SuppressWarnings("unused")
    private void addFunctionBody(InternalFunction func, CodeBox code, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected IfElseGenerator getIfElseFuncGenerator() {
        return new CurlyBraceIfElseGenerator();
    }

    @Override
    protected void generateBreakFuncStatement(CodeBox code) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected void generateContinueFuncStatement(CodeBox code) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected void generateReturnFuncStatement(Expression retValue, CodeBox code, boolean safeScope, CodeContext ctxt) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected boolean generateWhileFuncStatement(ExprCode guardCode, CodeBox code, boolean safeScope) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    protected void generateEndWhileFuncStatement(CodeBox code) {
        // TODO: Unimplemented method stub, to be implemented when generating JavaScript vars and functions.
        throw new UnsupportedOperationException("To be implemented");
    }
}
