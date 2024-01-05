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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.java.Lists.cast;

import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.Import;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolDefTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement;

/** Statement reachability checker. */
public class ReachabilityChecker {
    /** Constructor for the {@link ReachabilityChecker} class. */
    private ReachabilityChecker() {
        // Static class.
    }

    /**
     * Check for and warn about unreachable statements.
     *
     * <p>
     * We don't take values of 'if' and 'while' conditions etc into account. Therefore, we may get false positives. To
     * prevent them, we only report definitely unreachable statements.
     * </p>
     *
     * @param stats The statements to check.
     * @param reachable Whether the first statement may be reachable.
     * @param ctxt The type checker context.
     * @return Whether statements after the given statements may be reachable.
     */
    public static boolean warnNonReachable(List<Declaration> stats, boolean reachable, CheckerContext ctxt) {
        // Check statements, in order.
        for (Declaration stat: stats) {
            reachable = warnNonReachable(stat, reachable, ctxt);
        }
        return reachable;
    }

    /**
     * Check for and warn about unreachable statements.
     *
     * <p>
     * We don't take values of 'if' and 'while' conditions etc into account. Therefore, we may get false positives. To
     * prevent them, we only report definitely unreachable statements.
     * </p>
     *
     * @param stat The statement to check.
     * @param reachable Whether the statement may be reachable.
     * @param ctxt The type checker context.
     * @return Whether statements after the given statement may be reachable.
     */
    public static boolean warnNonReachable(Declaration stat, boolean reachable, CheckerContext ctxt) {
        // Report unreachable statement.
        if (!reachable) {
            ctxt.addProblem(Message.STAT_UNREACHABLE, stat.getPosition());
            // Non-fatal problem.
        }

        // Simple statements, move to next.
        if (stat instanceof AssignmentStatement) {
            return reachable;
        } else if (stat instanceof Import) {
            return reachable;
        } else if (stat instanceof Script) {
            return reachable;
        } else if (stat instanceof ToolInvokeStatement) {
            return reachable;
        } else if (stat instanceof TypeDecl) {
            return reachable;
        } else if (stat instanceof Variable) {
            return reachable;
        }

        // Simple statements, next not reachable.
        if (stat instanceof BreakStatement) {
            return false;
        } else if (stat instanceof ContinueStatement) {
            return false;
        } else if (stat instanceof ExitStatement) {
            return false;
        } else if (stat instanceof ReturnStatement) {
            return false;
        }

        // Scoped statements.
        if (stat instanceof ForStatement) {
            // Check the body.
            List<Declaration> body = cast(((ForStatement)stat).getStatements());
            warnNonReachable(body, reachable, ctxt);
            return reachable;
        } else if (stat instanceof IfStatement) {
            // Check the 'then' statements of the 'if'.
            List<Declaration> thens = cast(((IfStatement)stat).getThens());
            boolean reachThens = warnNonReachable(thens, reachable, ctxt);

            // Check the 'then' statements of the 'elifs'.
            boolean reachElifs = false;
            for (ElifStatement elif: ((IfStatement)stat).getElifs()) {
                List<Declaration> elifThens = cast(elif.getThens());
                boolean reachElif = warnNonReachable(elifThens, reachable, ctxt);
                reachElifs |= reachElif;
            }

            // Check the 'else' statements of the 'if'.
            List<Declaration> elses = cast(((IfStatement)stat).getElses());
            boolean reachElses = warnNonReachable(elses, reachable, ctxt);

            // Result for entire 'if'.
            return reachThens || reachElifs || reachElses;
        } else if (stat instanceof Tool) {
            // Must be a ToolDef tool.
            Assert.check(stat instanceof ToolDefTool);

            // Check the body.
            List<Declaration> body = cast(((ToolDefTool)stat).getStatements());
            warnNonReachable(body, true, ctxt);
            return reachable;
        } else if (stat instanceof WhileStatement) {
            // Check the body.
            List<Declaration> body = cast(((WhileStatement)stat).getStatements());
            warnNonReachable(body, reachable, ctxt);
            return reachable;
        }

        // Others.
        throw new RuntimeException("Unknown statement: " + stat);
    }
}
