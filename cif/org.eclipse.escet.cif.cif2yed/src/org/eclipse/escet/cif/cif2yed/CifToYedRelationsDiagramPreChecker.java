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

package org.eclipse.escet.cif.cif2yed;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.CompDefNoSpecificParamsCheck;
import org.eclipse.escet.cif.checkers.checks.CompDefNoSpecificParamsCheck.NoSpecificCompDefParam;
import org.eclipse.escet.common.java.Termination;

/** CIF to yEd transformation precondition checker for generating relations diagrams. */
public class CifToYedRelationsDiagramPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link CifToYedRelationsDiagramPreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public CifToYedRelationsDiagramPreChecker(Termination termination) {
        super(termination, new CompDefNoSpecificParamsCheck(NoSpecificCompDefParam.COMPONENT));
    }
}
