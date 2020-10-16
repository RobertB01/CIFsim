//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.getAbsName;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.typeToStr;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.isSubType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newListType;

import java.util.List;

import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/** ToolDef tool type checker. */
public class ToolsChecker {
    /** Constructor for the {@link ToolsChecker} class. */
    private ToolsChecker() {
        // Static class.
    }

    /**
     * Type check a tool.
     *
     * @param tool The tool.
     * @param ctxt The type checker context.
     */
    public static void tcheck(ToolDefTool tool, CheckerContext ctxt) {
        // Type check the type parameters. They are available for the entire
        // header.
        CheckerContext newCtxt = new CheckerContext(ctxt, tool);
        for (TypeParam param: tool.getTypeParams()) {
            newCtxt.addDecl(param);
        }

        // Type check the return types.
        for (ToolDefType type: tool.getReturnTypes()) {
            TypesChecker.tcheck(type, newCtxt);
        }

        // Type check the tool parameters.
        List<ToolParameter> params = tool.getParameters();
        for (ToolParameter param: params) {
            // Type check the type of the tool parameter.
            TypesChecker.tcheck(param.getType(), newCtxt);
            ToolDefType paramType = param.getType();

            // Type check the default value of the tool parameter, if any.
            if (param.getValue() != null) {
                // Default value expression is not allowed to refer to objects
                // that can have a value that changes. We haven't added the
                // parameters of this tool yet. Variables from the context may
                // not be used either, which is already enforced by the scoping
                // rules.

                // Type check the default value expression.
                TypeHints hints = new TypeHints();
                hints.add(paramType);
                ExprsChecker.tcheck(param.getValue(), newCtxt, hints);
                ToolDefType valueType = param.getValue().getType();

                // Check type of the default value against the parameter type.
                // Don't check for variadic parameters, see below.
                if (!param.isVariadic() && !isSubType(valueType, paramType)) {
                    ctxt.addProblem(Message.TOOL_PARAM_VALUE_TYPE, param.getValue().getPosition(), getAbsName(param),
                            typeToStr(valueType), typeToStr(paramType));
                    // Non-fatal problem.
                }
            }
        }

        // At most one variadic parameter.
        int variadicCount = 0;
        for (ToolParameter param: params) {
            if (param.isVariadic()) {
                variadicCount++;
            }
        }

        if (variadicCount > 1) {
            ctxt.addProblem(Message.TOOL_MULTIPLE_VARIADIC, tool.getPosition(), getAbsName(tool));
            // Non-fatal problem.
        }

        // Variadic parameter may not be optional.
        for (ToolParameter param: params) {
            if (param.isVariadic() && param.getValue() != null) {
                ctxt.addProblem(Message.TOOL_PARAM_VARIADIC_OPTIONAL, param.getPosition(), getAbsName(param));
                // Non-fatal problem.
            }
        }

        // Parameter order: mandatory before variadic/optional.
        boolean mandatory = true;
        for (ToolParameter param: params) {
            boolean paramMandatory = !param.isVariadic() && param.getValue() == null;
            if (mandatory == paramMandatory) {
                // Expected and encountered match. OK.
            } else if (mandatory && !paramMandatory) {
                // Expected and encountered are different. Continuing to check
                // non-mandatory.
                mandatory = paramMandatory;
            } else {
                // Wrong order: mandatory after variadic or optional.
                ctxt.addProblem(Message.TOOL_PARAM_ORDER, param.getPosition(), getAbsName(param));
                // Non-fatal problem.
            }
        }

        // Change the type of variadic parameters to be list types.
        for (ToolParameter param: params) {
            if (!param.isVariadic()) {
                continue;
            }

            // Wrap a non-nullable list type around the element type. Changes
            // the containment.
            ListType type = newListType(param.getType(), false, null);
            param.setType(type);
        }

        // Add parameters to the context, to allow using them in the statements.
        for (ToolParameter param: params) {
            newCtxt.addDecl(param);
        }

        // Type check the statements.
        StatementsChecker.tcheck(tool.getStatements(), newCtxt);

        // Check for missing return statement.
        if (!tool.getReturnTypes().isEmpty()) {
            if (!mayEncounterReturn(tool.getStatements())) {
                ctxt.addProblem(Message.TOOL_RETURN_MISSING, tool.getPosition(), getAbsName(tool));
                // Non-fatal problem.
            }
        }

        // Report unused declarations.
        if (!ctxt.tchecker.hasError()) {
            newCtxt.checkUnused();
        }

        // Add the tool to the context.
        // TODO: We explicitly do it here, and not before checking the
        // statements, to disallow recursively tool invocations. The
        // problem is that for now, having the same type parameter on
        // the calling side as the called side, is too complicated. We
        // may want to support recursive invocations in the future.
        ctxt.addDecl(tool);
    }

    /**
     * Check for missing return statement.
     *
     * <p>
     * We don't take values of 'if' and 'while' conditions etc into account. Therefore, we may get false positives. To
     * prevent them, we only report paths definitely not ending with a return statement.
     * </p>
     *
     * <p>
     * Since an 'exit' statement ends the entire execution, we check for both 'return' and 'exit'.
     * </p>
     *
     * @param stats The statements to check.
     * @return Whether any trace through the statements can encounter a 'return' or 'exit' statement ({@code true}) or
     *     not ({@code false}).
     */
    private static boolean mayEncounterReturn(List<Statement> stats) {
        // Check statements, in order.
        for (Statement stat: stats) {
            if (stat instanceof AssignmentStatement) {
                // No 'return' or 'exit', continue with next statement.
            } else if (stat instanceof BreakStatement) {
                // No 'return' or 'exit', and statement block ends.
                return false;
            } else if (stat instanceof ContinueStatement) {
                // No 'return' or 'exit', and statement block ends.
                return false;
            } else if (stat instanceof ExitStatement) {
                // 'exit' statement directly.
                return true;
            } else if (stat instanceof ForStatement) {
                // Check the body.
                List<Statement> body = ((ForStatement)stat).getStatements();
                if (mayEncounterReturn(body)) {
                    return true;
                }
                // Move on to following statements.
            } else if (stat instanceof IfStatement) {
                // Check the 'then' statements of the 'if'.
                List<Statement> thens = ((IfStatement)stat).getThens();
                if (mayEncounterReturn(thens)) {
                    return true;
                }

                // Check the 'then' statements of the 'elifs'.
                for (ElifStatement elif: ((IfStatement)stat).getElifs()) {
                    if (mayEncounterReturn(elif.getThens())) {
                        return true;
                    }
                }

                // Check the 'else' statements of the 'if'.
                List<Statement> elses = ((IfStatement)stat).getThens();
                if (mayEncounterReturn(elses)) {
                    return true;
                }
            } else if (stat instanceof ReturnStatement) {
                // 'return' statement directly.
                return true;
            } else if (stat instanceof ToolInvokeStatement) {
                // No 'return' or 'exit', continue with next statement.
            } else if (stat instanceof Variable) {
                // No 'return' or 'exit', continue with next statement.
            } else if (stat instanceof WhileStatement) {
                // Check the body.
                List<Statement> body = ((WhileStatement)stat).getStatements();
                if (mayEncounterReturn(body)) {
                    return true;
                }
                // Move on to following statements.
            } else {
                throw new RuntimeException("Unknown statement: " + stat);
            }
        }

        // No 'return' or 'exit' encountered.
        return false;
    }
}
