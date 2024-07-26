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
import org.eclipse.escet.common.java.Termination;

/** CIF to mCRL2 transformation precondition checker. */
public class CifToMcrl2PreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link CifToMcrl2PreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public CifToMcrl2PreChecker(Termination termination) {
        super(termination

        );
    }
}
