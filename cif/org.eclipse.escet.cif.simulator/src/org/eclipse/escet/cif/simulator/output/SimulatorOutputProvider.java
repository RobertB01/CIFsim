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

package org.eclipse.escet.cif.simulator.output;

import java.util.List;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.input.InputComponent;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** CIF simulator output provider. */
public final class SimulatorOutputProvider extends OutputProvider<SimulatorOutputComponent> {
    /**
     * Signals an event that indicates that the initial state is available.
     *
     * @param state The initial state.
     * @see SimulatorOutputComponent#initialState
     */
    public void initialState(RuntimeState state) {
        for (SimulatorOutputComponent component: components) {
            component.initialState(state);
        }
    }

    /**
     * Signals an event that indicates that a number of possible transitions were calculated and are now available.
     *
     * @param transitions The transitions that are possible from the current state. May be empty if no transitions are
     *     possible.
     * @param result Indicates the simulation result, in case no transitions are possible. Is {@code null} if at least
     *     one transition is possible. Is {@link SimulationResult#DEADLOCK} or {@link SimulationResult#ENDTIME_REACHED}
     *     otherwise.
     * @see SimulatorOutputComponent#possibleTransitions
     */
    public void possibleTransitions(List<Transition<?>> transitions, SimulationResult result) {
        for (SimulatorOutputComponent component: components) {
            component.possibleTransitions(transitions, result);
        }
    }

    /**
     * Signals an event that indicates that a transition was chosen.
     *
     * @param sourceState The source state of the transition.
     * @param transition The transition that was taken.
     * @param chosenTargetTime The chosen target time of the time transition to take, or {@code null} for non-time
     *     transitions.
     * @see SimulatorOutputComponent#transitionChosen
     */
    public void transitionChosen(RuntimeState sourceState, Transition<?> transition,
            ChosenTargetTime chosenTargetTime)
    {
        for (SimulatorOutputComponent component: components) {
            component.transitionChosen(sourceState, transition, chosenTargetTime);
        }
    }

    /**
     * Signals an event that indicates that a new intermediate state from a trajectory is available.
     *
     * @param state The intermediate state, between the start state of the time transition, and the end state of the
     *     time transition.
     * @see SimulatorOutputComponent#intermediateTrajectoryState
     */
    public void intermediateTrajectoryState(RuntimeState state) {
        for (SimulatorOutputComponent component: components) {
            component.intermediateTrajectoryState(state);
        }
    }

    /**
     * Signals an event that indicates that a transition was taken, and the state of the simulator has been updated.
     *
     * <p>
     * For a {@link HistoryTransition}, the target state may go back in time.
     * </p>
     *
     * @param sourceState The source state of the transition.
     * @param transition The transition that was taken.
     * @param targetState The target state of the transition.
     * @param interrupted Whether the time transition was {@link InputComponent#interruptTimeTrans interrupted} by the
     *     {@link InputComponent input component}, before the chosen delay ended. Is {@code null} for non-time
     *     transitions.
     * @see SimulatorOutputComponent#transitionTaken
     */
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, RuntimeState targetState,
            Boolean interrupted)
    {
        for (SimulatorOutputComponent component: components) {
            component.transitionTaken(sourceState, transition, targetState, interrupted);
        }
    }

    /**
     * Signals an event that indicates that simulation has ended. Note that cleanup should not be performed by handlers
     * for this event. Instead {@link #cleanup} should be used for this.
     *
     * @param rslt The simulation result.
     * @param state The state at the end of the simulation, or {@code null} if simulation ended before the initial state
     *     was available.
     * @see SimulatorOutputComponent#simulationEnded
     */
    public void simulationEnded(SimulationResult rslt, RuntimeState state) {
        for (SimulatorOutputComponent component: components) {
            component.simulationEnded(rslt, state);
        }
    }

    /**
     * Returns a value indicating whether any of the currently registered output components has a visual interface
     * (GUI). If so, termination of the simulation may require confirmation of the user to terminate the simulator, and
     * thus close the visual representations of the output components.
     *
     * @return {@code true} to indicating that at least one of the output components has a visual interface (GUI),
     *     {@code false} otherwise.
     * @see SimulatorOutputComponent#hasVisualInterface
     */
    public boolean hasVisualInterface() {
        for (SimulatorOutputComponent component: components) {
            if (component.hasVisualInterface()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a value indicating whether any of the currently registered output components supports real-time
     * simulation.
     *
     * @return {@code true} to indicating that at least one of the output components supports real-time simulation,
     *     {@code false} otherwise.
     * @see SimulatorOutputComponent#supportsRealTimeSimulation
     */
    public boolean supportsRealTimeSimulation() {
        for (SimulatorOutputComponent component: components) {
            if (component.supportsRealTimeSimulation()) {
                return true;
            }
        }
        return false;
    }
}
