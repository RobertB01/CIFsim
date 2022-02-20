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

import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator;
import org.apache.commons.math3.ode.nonstiff.AdamsMoultonIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator;
import org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator;

/** ODE solver integrator algorithm. */
public enum IntegratorAlgo {
    /** {@link HighamHall54Integrator Higham and Hall 5(4)} integrator. */
    HIGHAM_HALL("Higham and Hall 5(4) integrator"),

    /** {@link DormandPrince54Integrator Dormand-Prince 5(4)} integrator. */
    DORMAND_PRINCE_54("Dormand-Prince 5(4) integrator"),

    /** {@link DormandPrince853Integrator Dormand-Prince 8(5,3)} integrator. */
    DORMAND_PRINCE_853("Dormand-Prince 8(5,3) integrator"),

    /** {@link GraggBulirschStoerIntegrator Gragg-Bulirsch-Stoer} integrator. */
    GRAGG_BULIRSCH_STOER("Gragg-Bulirsch-Stoer integrator"),

    /** {@link AdamsBashforthIntegrator Adams-Bashforth} integrator. */
    ADAMS_BASHFORTH("Adams-Bashforth integrator (experimental)"),

    /** {@link AdamsMoultonIntegrator Adams-Moulton} integrator. */
    ADAMS_MOULTON("Adams-Moulton integrator (experimental)");

    /** The end-user friendly name of the algorithm. */
    public final String name;

    /**
     * Constructor for the {@link IntegratorAlgo} enumeration.
     *
     * @param name The end-user friendly name of the algorithm.
     */
    private IntegratorAlgo(String name) {
        this.name = name;
    }

    /**
     * Creates an instance of the first order integrator, with given settings.
     *
     * @param numSteps The Number of steps of the method, excluding the one being computed.
     * @param minStep Minimal step size. The last step can be smaller than this.
     * @param maxStep Maximal step size.
     * @param atol Absolute tolerance, the maximum allowed absolute error.
     * @param rtol Relative tolerance, the maximum allowed relative error.
     * @return The newly constructed integrator.
     */
    public FirstOrderIntegrator create(int numSteps, double minStep, double maxStep, double atol, double rtol) {
        switch (this) {
            case HIGHAM_HALL:
                return new HighamHall54Integrator(minStep, maxStep, atol, rtol);
            case DORMAND_PRINCE_54:
                return new DormandPrince54Integrator(minStep, maxStep, atol, rtol);
            case DORMAND_PRINCE_853:
                return new DormandPrince853Integrator(minStep, maxStep, atol, rtol);
            case GRAGG_BULIRSCH_STOER:
                return new GraggBulirschStoerIntegrator(minStep, maxStep, atol, rtol);
            case ADAMS_BASHFORTH:
                return new AdamsBashforthIntegrator(numSteps, minStep, maxStep, atol, rtol);
            case ADAMS_MOULTON:
                return new AdamsMoultonIntegrator(numSteps, minStep, maxStep, atol, rtol);
        }
        return null; // Never happens.
    }
}
