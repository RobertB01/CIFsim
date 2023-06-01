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

package org.eclipse.escet.cif.checkers.checks;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifAddressableUtils.DuplVarAsgnException;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;

/** CIF check to disallow specific statements in internal user-defined functions. */
public class FuncNoSpecificIntUserDefFuncStatsCheck extends CifCheck {
    /** The disallowed statements. */
    private final EnumSet<NoSpecificStatement> disAlloweds;

    /**
     * Constructor of the {@link FuncNoSpecificIntUserDefFuncStatsCheck} class.
     *
     * @param disAlloweds The disallowed statements.
     */
    public FuncNoSpecificIntUserDefFuncStatsCheck(EnumSet<NoSpecificStatement> disAlloweds) {
        this.disAlloweds = disAlloweds;
    }

    /**
     * Constructor of the {@link FuncNoSpecificIntUserDefFuncStatsCheck} class.
     *
     * @param disAlloweds The disallowed statements.
     */
    public FuncNoSpecificIntUserDefFuncStatsCheck(NoSpecificStatement... disAlloweds) {
        this(Arrays.stream(disAlloweds)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(NoSpecificStatement.class))));
    }

    @Override
    protected void preprocessAssignmentFuncStatement(AssignmentFuncStatement asgStat, CifCheckViolations violations) {
        if (disAlloweds.contains(NoSpecificStatement.ASSIGN_MULTI_PARTS_SAME_VAR)) {
            Expression addr = asgStat.getAddressable();
            try {
                CifAddressableUtils.getRefs(addr);
            } catch (DuplVarAsgnException ex) {
                violations.add(asgStat, "Internal user-defined function has a multi-assignment "
                        + "that assigns multiple (non-overlapping) parts of a single variable");
            }
        }
    }

    @Override
    protected void preprocessBreakFuncStatement(BreakFuncStatement breakStat, CifCheckViolations violations) {
        if (disAlloweds.contains(NoSpecificStatement.BREAK)) {
            violations.add(breakStat, "Internal user-defined function has a 'break' statement");
        }
    }

    @Override
    protected void preprocessContinueFuncStatement(ContinueFuncStatement continueStat, CifCheckViolations violations) {
        if (disAlloweds.contains(NoSpecificStatement.CONTINUE)) {
            violations.add(continueStat, "Internal user-defined function has a 'continue' statement");
        }
    }

    @Override
    protected void preprocessWhileFuncStatement(WhileFuncStatement whileStat, CifCheckViolations violations) {
        if (disAlloweds.contains(NoSpecificStatement.WHILE)) {
            violations.add(whileStat, "Internal user-defined function has a 'while' statement");
        }
    }

    /** Disallowed user-defined function statement. */
    public static enum NoSpecificStatement {
        /** Do not allow an assignment to multiple parts of the same variable in internal user-defined functions. */
        ASSIGN_MULTI_PARTS_SAME_VAR,

        /** Do not allow the 'break' statement in internal user-defined functions. */
        BREAK,

        /** Do not allow the 'continue' statement in internal user-defined functions. */
        CONTINUE,

        /** Do not allow the 'while' statement in internal user-defined functions. */
        WHILE,
    }
}
