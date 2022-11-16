//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;

/** CIF check that does not allow specific user-defined functions. */
public class FuncNoSpecificUserDefCheck extends CifCheck {
    /** Set of all kinds of user-defined functions. */
    private static final EnumSet<NoSpecificUserDefFunc> ALL_USER_DEF_FUNCS = EnumSet.of(NoSpecificUserDefFunc.INTERNAL,
            NoSpecificUserDefFunc.EXTERNAL);

    /** The collection of disallowed specific user-defined functions. */
    private final EnumSet<NoSpecificUserDefFunc> disalloweds;

    /**
     * Constructor of the {@link FuncNoSpecificUserDefCheck} class.
     *
     * @param disalloweds The collection of disallowed specific user-defined functions.
     */
    public FuncNoSpecificUserDefCheck(EnumSet<NoSpecificUserDefFunc> disalloweds) {
        this.disalloweds = disalloweds;
    }

    /**
     * Constructor of the {@link FuncNoSpecificUserDefCheck} class.
     *
     * @param disalloweds The collection of disallowed specific user-defined functions.
     */
    public FuncNoSpecificUserDefCheck(NoSpecificUserDefFunc... disalloweds) {
        this(Arrays.stream(disalloweds)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(NoSpecificUserDefFunc.class))));
    }

    @Override
    protected void preprocessInternalFunction(InternalFunction func, CifCheckViolations violations) {
        reportFunction(func, NoSpecificUserDefFunc.INTERNAL, violations);
    }

    @Override
    protected void preprocessExternalFunction(ExternalFunction func, CifCheckViolations violations) {
        reportFunction(func, NoSpecificUserDefFunc.EXTERNAL, violations);
    }

    /**
     * Check a user-defined function.
     *
     * @param func Function to check.
     * @param funcKind Kind of the function.
     * @param violations Destination of the reported violations, is modified in-place.
     */
    private void checkFunction(Function func, NoSpecificUserDefFunc funcKind, CifCheckViolations violations) {
        // Check the kind of the function.
        if (disalloweds.contains(funcKind)) {
            if (disalloweds.containsAll(ALL_USER_DEF_FUNCS)) {
                violations.add(func, new LiteralMessage("is a user-defined function"));
            } else {
                String funcText = (funcKind == NoSpecificUserDefFunc.INTERNAL) ? "internal" : "external";
                violations.add(func, new LiteralMessage("is an %s user-defined function", funcText));
            }
        }

        // Check the number of parameters of the function.
        if (disalloweds.contains(NoSpecificUserDefFunc.NO_PARAMETER) && func.getParameters().isEmpty()) {
            violations.add(func, new LiteralMessage("has no parameters"));
        }
    }

    /** Disallowed specific user-defined functions. */
    public static enum NoSpecificUserDefFunc {
        /** Disallow internal user-defined functions. */
        INTERNAL,

        /** Disallow external user-defined functions. */
        EXTERNAL,

        /** Disallow user-defined functions without a formal parameter. */
        NO_PARAMETER,
    }
}
