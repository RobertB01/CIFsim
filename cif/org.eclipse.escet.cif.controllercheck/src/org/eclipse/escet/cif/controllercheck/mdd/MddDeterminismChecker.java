//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import java.util.function.BooleanSupplier;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;

/**
 * Determinism checker for the controller properties checker, for checks that use an MDD representation of the CIF
 * specification.
 */
public class MddDeterminismChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link MddDeterminismChecker} class.
     *
     * @param shouldTerminate Callback that indicates whether execution should be terminated on user request.
     */
    public MddDeterminismChecker(BooleanSupplier shouldTerminate) {
        super(new MddDeterminismCheck(shouldTerminate));
    }
}
