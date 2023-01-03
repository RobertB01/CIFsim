//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output;

import java.util.List;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.options.TestModeOption;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.output.OutputComponentBase;

/**
 * Default implementation of {@link SimulatorOutputComponent} that does nothing. Useful only for sub-classing, to avoid
 * having to implement events that are not relevant to the output component.
 *
 * <p>
 * This implementation does not provide a visual interface. Derived classes that do provide a visual interface should
 * override the {@link #hasVisualInterface} method.
 * </p>
 *
 * <p>
 * This implementation does not support real-time simulation. Derived classes that do support real-time simulation
 * should override the {@link #supportsRealTimeSimulation} method.
 * </p>
 */
public abstract class NullSimulatorOutputComponent extends OutputComponentBase implements SimulatorOutputComponent {
    /** Is {@link TestModeOption test mode} enabled? */
    protected final boolean testMode;

    /** Constructor for the {@link NullSimulatorOutputComponent} class. */
    public NullSimulatorOutputComponent() {
        testMode = TestModeOption.isEnabled();
    }

    @Override
    public void initialState(RuntimeState state) {
        // By default, does nothing.
    }

    @Override
    public void possibleTransitions(List<Transition<?>> transitions, SimulationResult result) {
        // By default, does nothing.
    }

    @Override
    public void transitionChosen(RuntimeState sourceState, Transition<?> transition,
            ChosenTargetTime chosenTargetTime)
    {
        // By default, does nothing.
    }

    @Override
    public void intermediateTrajectoryState(RuntimeState state) {
        // By default, does nothing.
    }

    @Override
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, RuntimeState targetState,
            Boolean interrupted)
    {
        // By default, does nothing.
    }

    @Override
    public void simulationEnded(SimulationResult rslt, RuntimeState state) {
        // By default, does nothing.
    }

    @Override
    public boolean hasVisualInterface() {
        return false;
    }

    @Override
    public boolean supportsRealTimeSimulation() {
        return false;
    }
}
