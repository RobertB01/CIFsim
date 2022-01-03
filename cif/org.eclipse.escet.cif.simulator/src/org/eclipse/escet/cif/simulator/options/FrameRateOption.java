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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.DoubleOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Frame rate option. */
public class FrameRateOption extends DoubleOption {
    /** Constructor for the {@link FrameRateOption} class. */
    public FrameRateOption() {
        super("Frame rate",
                "The frame rate, as the number of times per second that output is sent to the output components, "
                        + "such as visualizers. Specify any positive real value RATE as the frame rate. Specify "
                        + "\"auto\" (default) for a frame rate of 20 if a simulation speed is given, and to disable "
                        + "if no simulation speed is given.",
                null, "frame-rate", "RATE", null, 0.0, null, true,
                "The frame rate, as the number of times per second that output is sent to the output components, "
                        + "such as visualizers. The frame rate must be a positive real value.",
                "Frame rate:", true, 20.0, "auto",
                "Automatic (frame rate 20 if simulation speed is given, disabled otherwise)",
                "Enabled, with a specific frame rate");
    }

    /**
     * Returns the frame rate, as the number of times per second that output is sent to the output components, such as
     * visualizers, or {@code null} if disabled.
     *
     * @return The frame rate, or {@code null}.
     */
    public static Double getFrameRate() {
        Double rslt = Options.get(FrameRateOption.class);
        if (rslt == null && Options.get(SimulationSpeedOption.class) != null) {
            return 20.0;
        }
        return rslt;
    }

    /**
     * Returns the model time delta used for real-time simulation, or {@code null} if real-time simulation is disabled.
     *
     * @return The model time delta, or {@code null}.
     */
    public static Double getModelTimeDelta() {
        Double frameRate = getFrameRate();
        if (frameRate == null) {
            return null;
        }

        double simSpeed = SimulationSpeedOption.getSimSpeed();
        return 1 / (frameRate / simSpeed);
    }

    /**
     * Is real-time simulation enabled?
     *
     * @return {@code true} if real-time simulation is enabled, {@code false} otherwise.
     */
    public static boolean isRealTimeEnabled() {
        return getFrameRate() != null;
    }

    @Override
    public void verifyValue(Double value) {
        if (value == null) {
            return;
        }
        checkValue(value > 0, value + " <= 0");
    }
}
