//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.runtime;

/** Internal exception used to indicate the end of the simulation. */
public class SimulatorExitException extends RuntimeException {
    /** The simulation result. */
    public final SimulationResult result;

    /**
     * Constructor for the {@link SimulatorExitException}.
     *
     * @param result The simulation result.
     */
    public SimulatorExitException(SimulationResult result) {
        this.result = result;
    }

    @Override
    public String getMessage() {
        return "Simulator exit: " + result;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
