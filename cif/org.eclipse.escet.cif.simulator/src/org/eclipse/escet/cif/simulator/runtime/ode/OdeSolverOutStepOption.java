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

import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.DoubleOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** ODE solver fixed output step size option. */
public class OdeSolverOutStepOption extends DoubleOption {
    /** Constructor for the {@link OdeSolverOutStepOption} class. */
    public OdeSolverOutStepOption() {
        super("ODE solver fixed output step size",
                "Specifies that ODE solver output should only contain output for multiples of the given step size, "
                        + "besides the start and end points of the time transitions. The step size must be an integer "
                        + "or real number larger than zero. Specify \"auto\" (default) to disable the fixed step "
                        + "size output and let the integrator determine the output points (dynamic output step size) "
                        + "if real-time simulation is disabled, and to use the model time delta used for real-time "
                        + "simulation as fixed output step size if real-time simulation is enabled.",
                null, "solver-outstep", "OUTSTP", null, 0.0, null, true,
                "If a fixed output step size is used, trajectory data points are only outputted for multiples of "
                        + "the given step size, besides the start and end points of the time transitions. If set to "
                        + "automatic, the integrator determines the output points (dynamic output step size) if "
                        + "real-time simulation is disabled, and the model time delta used for real-time simulation "
                        + "is used as fixed output step size if real-time simulation is enabled.",
                "Step size:", true, 0.1, "auto", "Automatic output step size", "Custom fixed output step size");
    }

    /**
     * Returns the ODE solver fixed output step size, or {@code null} if it is disabled.
     *
     * @return The ODE solver fixed output step size, or {@code null}.
     */
    public static Double getSolverOutStep() {
        Double stepSize = Options.get(OdeSolverOutStepOption.class);
        Double delta = FrameRateOption.getModelTimeDelta();

        // stepSize : delta : method : result
        // ----------------------------------
        // null : null : null : fixed output step size disabled
        // null : MTD : MTD : inherit real-time model time delta
        // SS : null : SS : user-specified fixed output step size
        // SS : MTD : - : not allowed

        // If no real-time simulation, use option value directly.
        if (delta == null) {
            return stepSize;
        }

        // Disallow custom value when needs to be inherited from real-time.
        if (stepSize != null) {
            String msg = "Setting a custom ODE solver fixed output step size while real-time simulation is enabled, "
                    + "is not supported.";
            throw new InvalidOptionException(msg);
        }

        // Use model time delta used for real-time simulation.
        return delta;
    }

    @Override
    public void verifyValue(Double value) {
        if (value == null) {
            return;
        }
        checkValue(value > 0.0, value + " <= 0");
    }
}
