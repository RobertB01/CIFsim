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

package org.eclipse.escet.cif.simulator.runtime.ode;

import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;

/** ODE solver root finding algorithm. */
public enum RootFinderAlgo {
    /** {@link RegulaFalsiSolver Regula Falsi} root finding algorithm. */
    REGULA_FALSI("Regula Falsi (False position) method"),

    /** {@link IllinoisSolver Illinois} root finding algorithm. */
    ILLINOIS("Illinois method"),

    /** {@link PegasusSolver Pegasus} root finding algorithm. */
    PEGASUS("Pegasus method");

    /** The end-user friendly name of the algorithm. */
    public final String name;

    /**
     * Constructor for the {@link RootFinderAlgo} enumeration.
     *
     * @param name The end-user friendly name of the algorithm.
     */
    private RootFinderAlgo(String name) {
        this.name = name;
    }

    /**
     * Creates an instance of the root finding algorithm, with given tolerances.
     *
     * @param atol The absolute tolerance to use.
     * @param rtol The relative tolerance to use.
     * @return The newly created instance of the root finding algorithm.
     */
    public BaseSecantSolver create(double atol, double rtol) {
        // Note that constructors with one more argument (the function value
        // accuracy) exist. However, since we map values true and false to
        // +1.0 and -1.0, respectively, specifying the accuracy is of use.
        switch (this) {
            case REGULA_FALSI:
                return new RegulaFalsiSolver(rtol, atol);
            case ILLINOIS:
                return new IllinoisSolver(rtol, atol);
            case PEGASUS:
                return new PegasusSolver(rtol, atol);
        }
        return null; // Never reached.
    }
}
