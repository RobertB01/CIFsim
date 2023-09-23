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

package org.eclipse.escet.cif.simulator.input;

import java.util.List;

import org.eclipse.escet.cif.simulator.CifSimulatorHistory;
import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.options.SimulationSpeedOption;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;

/**
 * Base class for all input components. Input components can be used to guide the simulator, by for instance choosing
 * which transition to take, from a set of possible transitions. Furthermore, they can act as the environment, by
 * allowing or disallowing certain events, and interrupting time transitions before they are completed (when an
 * environment event becomes enabled). This way, they turn open specifications into closed ones.
 *
 * <p>
 * The input format of input components is explicitly left to implementations, to allow interactive input from a console
 * or GUI, as well as automatic input from for instance a file or an option controlled selection mode (first transition,
 * random transition, etc).
 * </p>
 *
 * @param <S> The type of state objects to use.
 */
public abstract class InputComponent<S extends RuntimeState> {
    /** The specification. */
    protected final RuntimeSpec<S> spec;

    /** State history for undo/reset. Is {@code null} if not yet available, or if history is disabled. */
    public CifSimulatorHistory history;

    /**
     * Constructor for the {@link InputComponent} class.
     *
     * @param spec The specification. The specification has not yet been {@link RuntimeSpec#init initialized}.
     */
    public InputComponent(RuntimeSpec<S> spec) {
        this.spec = spec;
    }

    /**
     * Initializes the input component. In the constructor, the {@link #spec} has not yet been initialized. When this
     * method is called, the specification has been initialized.
     *
     * <p>
     * By default no initialization is performed. Derived classes may override this method to perform their own
     * initialization.
     * </p>
     */
    public void init() {
        // By default, no initialization is performed.
    }

    /**
     * Choose a transition to take, from the possible transitions. In case no transitions are possible, it is allowed to
     * throw a {@link SimulatorExitException} with the given {@code result} as simulation result.
     *
     * <p>
     * Interactive input components may also create and return a {@link HistoryTransition}, regardless of whether any
     * transitions are possible. They may also throw a {@link SimulatorExitException} with
     * {@link SimulationResult#USER_TERMINATED} simulation result, if at least one transition is possible, and the user
     * interactively requests to stop the simulation.
     * </p>
     *
     * @param state The source state of the transitions.
     * @param transitions The transitions that are possible from the current state. May be empty if no transitions are
     *     possible.
     * @param result Indicates the simulation result, in case no transitions are possible. Is {@code null} if at least
     *     one transition is possible. Is {@link SimulationResult#DEADLOCK} or {@link SimulationResult#ENDTIME_REACHED}
     *     otherwise.
     * @return The transition that is chosen to be taken, or {@code null} if no transition is chosen.
     * @throws SimulatorExitException May be thrown if no transitions are possible, to stop simulation, rather than
     *     performing a history transition.
     * @throws SimulatorExitException May be thrown by interactive input components in case at least one transition is
     *     possible, and the user interactively requests to stop the simulation.
     * @see RuntimeState#chooseTransition
     */
    public abstract Transition<S> chooseTransition(S state, List<Transition<S>> transitions, SimulationResult result);

    /**
     * Choose the target time of the time transition to take, given the allowed interval
     * {@code (state.getTime() .. maxTargetTime]}.
     *
     * @param state The source state of the time transition.
     * @param maxTargetTime The maximum allowed target time.
     * @return The chosen target time of the time transition to take.
     */
    public abstract ChosenTargetTime chooseTargetTime(S state, double maxTargetTime);

    /**
     * Returns a value indicating whether the current time transition is to be interrupted.
     *
     * <p>
     * If the simulator chooses a time transition, and if real-time simulation is enabled (and intermediate frames
     * states are sent to the output components), the input component is regularly polled to see whether it wants to
     * interrupt the current time transition. The poll frequency is determined by the {@link FrameRateOption frame rate}
     * and {@link SimulationSpeedOption simulation speed}.
     * </p>
     *
     * <p>
     * By default, input components never interrupt time transitions. Derived classes may override this method if they
     * do want to be able to interrupt time transitions.
     * </p>
     *
     * @return {@code true} if the time transition is to be interrupted, {@code false} otherwise.
     */
    public boolean interruptTimeTrans() {
        return false;
    }

    /**
     * Updates, per event, the information regarding whether or not the environment disables the event. If the
     * environment disables an event, calculation of the possible event transitions are skipped for that event.
     *
     * <p>
     * By default, the environment doesn't disable any events. Derived classes may override this method to alter this
     * behavior.
     * </p>
     *
     * @param disabled Per event, {@code true} if the environment disables the event, {@code false} otherwise. When
     *     passed to the method for the first time, value {@code false} is present for each event. It should be modified
     *     in-place if events are disabled by the environment. For each subsequent invocation, the array is reused.
     * @param state The source state.
     */
    public void updateDisabledEvents(S state, boolean[] disabled) {
        // By default, all events are enabled.
    }

    /**
     * Returns a value indicating the maximum allowed end time, for the next time transition to calculate.
     *
     * @param state The source state.
     * @return The maximum allowed end time, for the next time transition to calculate, or {@code null} to not impose
     *     any additional restrictions on the maximum allowed end time.
     */
    public Double getNextMaxEndTime(S state) {
        // By default, the maximum allowed end time is not further restricted.
        return null;
    }
}
