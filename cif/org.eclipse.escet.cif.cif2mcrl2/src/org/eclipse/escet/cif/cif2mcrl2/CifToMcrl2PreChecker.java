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

package org.eclipse.escet.cif.cif2mcrl2;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.EqnNotAllowedCheck;
import org.eclipse.escet.cif.checkers.checks.EventNoChannelsCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck;
import org.eclipse.escet.cif.checkers.checks.TypeNoSpecificTypesCheck.NoSpecificType;
import org.eclipse.escet.cif.checkers.checks.VarDiscOnlyStaticEvalInitCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoContinuousCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoDiscWithMultiInitValuesCheck;
import org.eclipse.escet.cif.checkers.checks.VarNoInputCheck;
import org.eclipse.escet.common.java.Termination;

/** CIF to mCRL2 transformation precondition checker. */
public class CifToMcrl2PreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link CifToMcrl2PreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public CifToMcrl2PreChecker(Termination termination) {
        super(termination,

                // Only boolean, integer and enumeration types are supported.
                new TypeNoSpecificTypesCheck(
                        NoSpecificType.COMP_TYPES,
                        NoSpecificType.DICT_TYPES,
                        NoSpecificType.DIST_TYPES,
                        NoSpecificType.FUNC_TYPES,
                        NoSpecificType.LIST_TYPES,
                        NoSpecificType.REAL_TYPES,
                        NoSpecificType.SET_TYPES,
                        NoSpecificType.STRING_TYPES,
                        NoSpecificType.TUPLE_TYPES),

                // Discrete variables must not have multiple potential initial values.
                new VarNoDiscWithMultiInitValuesCheck(),

                // Initial values of discrete variables must be statically evaluable.
                new VarDiscOnlyStaticEvalInitCheck(),

                // Continuous variables are not supported.
                new VarNoContinuousCheck(),

                // Input variables are not supported.
                new VarNoInputCheck(),

                // Equations are not supported.
                new EqnNotAllowedCheck(),

                // Channels are not supported.
                new EventNoChannelsCheck()

        );
    }
}
