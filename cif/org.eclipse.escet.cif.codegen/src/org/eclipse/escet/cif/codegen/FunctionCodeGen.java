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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.cif.codegen.updates.tree.UpdateData.generateAssignment;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleType;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;

import java.util.List;

import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** Code generator for a function. */
public abstract class FunctionCodeGen {
    /** Function being translated. */
    protected InternalFunction function;

    /**
     * Constructor of the {@link FunctionCodeGen} class.
     *
     * @param function Function being translated.
     */
    public FunctionCodeGen(InternalFunction function) {
        this.function = function;
    }

    /**
     * Provide the return type of the function.
     *
     * @return The return type of the function.
     */
    public CifType getReturnType() {
        return makeTupleType(deepclone(function.getReturnTypes()), null);
    }

    /**
     * Add code for the statements in the body of an internal user-defined function.
     *
     * @param statements The statements.
     * @param code The code. Is extended in-place.
     * @param ctxt Code generation context.
     */
    protected void addFuncStatements(List<FunctionStatement> statements, CodeBox code, CodeContext ctxt) {
        addFuncStatements(statements, code, false, ctxt);
    }

    /**
     * Add code for the statements in the body of an internal user-defined function.
     *
     * @param statements The statements.
     * @param code The code. Is extended in-place.
     * @param safeScope Whether the current scope is safe for creating temporary variables.
     * @param ctxt Code generation context.
     */
    private void addFuncStatements(List<FunctionStatement> statements, CodeBox code, boolean safeScope,
            CodeContext ctxt)
    {
        safeScope = safeScope && statements.size() == 1; // Only safe if body was safe and has one statement.

        for (int i = 0; i < statements.size(); i++) {
            FunctionStatement statement = statements.get(i);
            if (i > 0) {
                code.add();
            }
            addFuncStatement(statement, code, safeScope, ctxt);
        }
    }

    /**
     * Add code for the statement in the body of an internal user-defined function.
     *
     * @param statement The statement.
     * @param code The code. Is extended in-place.
     * @param safeScope Whether the current scope is safe for creating temporary variables.
     * @param ctxt Code generation context.
     */
    private void addFuncStatement(FunctionStatement statement, CodeBox code, boolean safeScope, CodeContext ctxt) {
        if (statement instanceof AssignmentFuncStatement) {
            AssignmentFuncStatement asgn = (AssignmentFuncStatement)statement;
            generateAssignment(asgn, code, safeScope, ctxt);
        } else if (statement instanceof BreakFuncStatement) {
            generateBreakFuncStatement(code);
        } else if (statement instanceof ContinueFuncStatement) {
            generateContinueFuncStatement(code);
        } else if (statement instanceof IfFuncStatement) {
            IfFuncStatement istat = (IfFuncStatement)statement;
            IfElseGenerator ifElseFrame = getIfElseFuncGenerator();

            // If statements.
            ExprCode guardCode = ctxt.predsToTarget(istat.getGuards());
            ifElseFrame.generateIf(guardCode, code);
            addFuncStatements(istat.getThens(), code, ifElseFrame.branchIsSafeScope(), ctxt);

            // Elifs.
            for (ElifFuncStatement elif: istat.getElifs()) {
                ExprCode elifGuardCode = ctxt.predsToTarget(elif.getGuards());
                ifElseFrame.generateElseIf(elifGuardCode, code);
                addFuncStatements(elif.getThens(), code, ifElseFrame.branchIsSafeScope(), ctxt);
            }

            // Else.
            if (!istat.getElses().isEmpty()) {
                ifElseFrame.generateElse(code);
                addFuncStatements(istat.getElses(), code, ifElseFrame.branchIsSafeScope(), ctxt);
            }

            // Close if/elif/else.
            ifElseFrame.generateEndIf(code);
        } else if (statement instanceof ReturnFuncStatement) {
            ReturnFuncStatement rstat = (ReturnFuncStatement)statement;
            Expression retValue = CifValueUtils.makeTuple(deepclone(rstat.getValues()), null);
            generateReturnFuncStatement(retValue, code, safeScope, ctxt);
        } else if (statement instanceof WhileFuncStatement) {
            WhileFuncStatement wstat = (WhileFuncStatement)statement;

            ExprCode guardCode = ctxt.predsToTarget(wstat.getGuards());
            safeScope = generateWhileFuncStatement(guardCode, code, safeScope);
            addFuncStatements(wstat.getStatements(), code, safeScope, ctxt);
            generateEndWhileFuncStatement(code);
        } else {
            throw new RuntimeException("Unknown func stat: " + statement);
        }
    }

    /**
     * Get a fresh generator for function 'if' statements in the target language.
     *
     * @return The created target language 'if' statement generator.
     */
    protected abstract IfElseGenerator getIfElseFuncGenerator();

    /**
     * Generate a 'break' statement in a function.
     *
     * @param code Destination for the generated code.
     */
    protected abstract void generateBreakFuncStatement(CodeBox code);

    /**
     * Generate a 'continue' statement in a function.
     *
     * @param code Destination for the generated code.
     */
    protected abstract void generateContinueFuncStatement(CodeBox code);

    /**
     * Generate a 'return' statement in a function.
     *
     * @param retValue Value to return.
     * @param code Destination for the generated code.
     * @param safeScope Whether the current scope is safe for creating temporary variables.
     * @param ctxt Code context of the function statements.
     */
    protected abstract void generateReturnFuncStatement(Expression retValue, CodeBox code, boolean safeScope,
            CodeContext ctxt);

    /**
     * Generate the start of a 'while' statement in a function.
     *
     * @param guardCode Guard that must hold to take the 'while'.
     * @param code Destination for the generated code.
     * @param safeScope Whether the current scope is safe for creating temporary variables.
     * @return Whether the body of the while is a safe scope.
     */
    protected abstract boolean generateWhileFuncStatement(ExprCode guardCode, CodeBox code, boolean safeScope);

    /**
     * Generate the end of a 'while' statement in a function.
     *
     * @param code Destination for the generated code.
     */
    protected abstract void generateEndWhileFuncStatement(CodeBox code);
}
