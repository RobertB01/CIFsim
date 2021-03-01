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

package org.eclipse.escet.cif.simulator.output;

import java.util.List;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.input.InputComponent;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;

/**
 * CIF simulator output component interface. Adds CIF simulator specific event to the {@link IOutputComponent}
 * interface.
 */
public interface SimulatorOutputComponent extends IOutputComponent {
    /**
     * Handler for the event that indicates that the initial state is available.
     *
     * @param state The initial state.
     * @see SimulatorOutputProvider#initialState
     */
    public void initialState(RuntimeState state);

    /**
     * Handler for the event that indicates that a number of possible transitions were calculated and are now available.
     *
     * @param transitions The transitions that are possible from the current state. May be empty if no transitions are
     *     possible.
     * @param result Indicates the simulation result, in case no transitions are possible. Is {@code null} if at least
     *     one transition is possible. Is {@link SimulationResult#DEADLOCK} or {@link SimulationResult#ENDTIME_REACHED}
     *     otherwise.
     * @see SimulatorOutputProvider#possibleTransitions
     */
    public void possibleTransitions(List<Transition<?>> transitions, SimulationResult result);

    /**
     * Handler for the event that indicates that a transition was chosen.
     *
     * @param sourceState The source state of the transition.
     * @param transition The transition that was taken.
     * @param chosenTargetTime The chosen target time of the time transition to take, or {@code null} for non-time
     *     transitions.
     * @see SimulatorOutputProvider#transitionChosen
     */
    public void transitionChosen(RuntimeState sourceState, Transition<?> transition, ChosenTargetTime chosenTargetTime);

    /**
     * Handler for the event that indicates that a new intermediate state from a trajectory is available.
     *
     * @param state The intermediate state, between the start state of the time transition, and the end state of the
     *     time transition.
     * @see SimulatorOutputProvider#intermediateTrajectoryState
     */
    public void intermediateTrajectoryState(RuntimeState state);

    /**
     * Handler for the event that indicates that a transition was taken, and the state of the simulator has been
     * updated.
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
     * @see SimulatorOutputProvider#transitionTaken
     */
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, RuntimeState targetState,
            Boolean interrupted);

    /**
     * Handler for the event that indicates that simulation has ended. Note that cleanup should not be performed by
     * handlers for this event. Instead {@link #cleanup} should be used for this.
     *
     * @param rslt The simulation result.
     * @param state The state at the end of the simulation, or {@code null} if simulation ended before the initial state
     *     was available.
     * @see SimulatorOutputProvider#simulationEnded
     */
    public void simulationEnded(SimulationResult rslt, RuntimeState state);

    /**
     * Returns a value indicating whether the output component has a visual interface (GUI). If it does, and the output
     * component is used by the simulator, then termination of the simulation may require confirmation of the user to
     * terminate the simulator, and thus close the visual representations of the output components.
     *
     * @return {@code true} to indicating that the output component has a visual interface (GUI), {@code false}
     *     otherwise.
     * @see SimulatorOutputProvider#hasVisualInterface
     */
    public boolean hasVisualInterface();

    /**
     * Returns a value indicating whether the output component supports real-time simulation.
     *
     * @return {@code true} to indicating that the output component supports real-time simulation, {@code false}
     *     otherwise.
     * @see SimulatorOutputProvider#supportsRealTimeSimulation
     */
    public boolean supportsRealTimeSimulation();
}
