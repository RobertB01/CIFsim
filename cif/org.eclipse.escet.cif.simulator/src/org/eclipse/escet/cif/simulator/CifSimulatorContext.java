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

package org.eclipse.escet.cif.simulator;

import java.util.EnumSet;

import org.eclipse.escet.cif.simulator.options.ExtFuncAsyncOption;
import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.options.TestModeOption;
import org.eclipse.escet.cif.simulator.output.DebugOutputOption;
import org.eclipse.escet.cif.simulator.output.DebugOutputType;
import org.eclipse.escet.cif.simulator.output.NormalOutputOption;
import org.eclipse.escet.cif.simulator.output.NormalOutputType;
import org.eclipse.escet.cif.simulator.output.SimulatorOutputProvider;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.common.app.framework.AppEnvData;

/** CIF simulator runtime context. */
public final class CifSimulatorContext {
    /** The simulator application environment data. */
    public final AppEnvData appEnvData;

    /** The simulator output provider. */
    public final SimulatorOutputProvider provider;

    /** The types of normal output to print to the console. */
    public final EnumSet<NormalOutputType> normal;

    /** The types of debug output to print to the console. */
    public final EnumSet<DebugOutputType> debug;

    /**
     * Is real-time simulation enabled?
     *
     * @see FrameRateOption#isRealTimeEnabled
     */
    public final boolean realTime;

    /** Is {@link TestModeOption test mode} enabled? */
    public final boolean testMode;

    /** Should external user-defined functions be executed asynchronously? */
    public final boolean extFuncAsync;

    /**
     * Constructor for the {@link CifSimulatorContext} class.
     *
     * @param appEnvData The simulator application environment data.
     */
    public CifSimulatorContext(AppEnvData appEnvData) {
        this.appEnvData = appEnvData;
        provider = (SimulatorOutputProvider)appEnvData.getProvider();
        normal = NormalOutputOption.getOutputTypes();
        debug = DebugOutputOption.getDebugTypes();
        realTime = FrameRateOption.isRealTimeEnabled();
        testMode = TestModeOption.isEnabled();
        extFuncAsync = ExtFuncAsyncOption.execAsync();
    }

    /**
     * Checks for application termination request, and terminates the simulation if termination is requested.
     *
     * @throws SimulatorExitException If termination is requested.
     */
    public void checkTermination() {
        if (appEnvData.isTerminationRequested()) {
            throw new SimulatorExitException(SimulationResult.USER_TERMINATED);
        }
    }
}
