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

import static org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption.autoChooseEvents;
import static org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption.autoChooseSingle;
import static org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption.autoChooseTime;
import static org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption.autoChooseTimeDur;
import static org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption.getFilters;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.simulator.options.HistoryOption;
import org.eclipse.escet.cif.simulator.options.HistorySizeOption;
import org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeEvent;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.java.Assert;

/**
 * Input component that interactively asks the user for input, via a method chosen by derived classes. Using the
 * {@link InteractiveAutoChooseOption} option, the interactive input component can be made semi-automatic.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class InteractiveInputComponent<S extends RuntimeState> extends InputComponent<S> {
    /**
     * Whether a transition should be automatically chosen, if it is the only transition possible. If {@code true},
     * semi-automatic mode is enabled.
     */
    protected boolean autoSingle;

    /** Whether time transitions should be automatically chosen. If {@code true}, semi-automatic mode is enabled. */
    protected boolean autoTime;

    /**
     * Whether the delay duration for time transitions should be automatically chosen. If {@code true}, semi-automatic
     * mode is enabled.
     */
    protected boolean autoTimeDur;

    /**
     * Indicates per events, whether it should automatically be chosen. If for any event, the value is {@code true},
     * semi-automatic mode is enabled.
     */
    protected boolean[] autoEvents;

    /**
     * The automatic input component to use to automatically choose transitions if semi-automatic mode is enabled. The
     * value is {@code null} unless semi-automatic mode is enabled. It is also {@code null} if semi-automatic mode is
     * only enabled for {@link #autoSingle}.
     *
     * @see #autoSingle
     * @see #autoTime
     * @see #autoTimeDur
     * @see #autoEvents
     */
    protected AutomaticInputComponent<S> autoInput;

    /** Is reset enabled, option-wise? */
    protected final boolean isResetEnabled;

    /** Is undo enabled, option-wise? */
    protected final boolean isUndoEnabled;

    /**
     * Constructor for the {@link InteractiveInputComponent}.
     *
     * @param spec The specification. The specification has not yet been {@link RuntimeSpec#init initialized}.
     */
    public InteractiveInputComponent(RuntimeSpec<S> spec) {
        super(spec);
        isResetEnabled = HistoryOption.isEnabled();
        isUndoEnabled = HistorySizeOption.isUndoEnabled();
    }

    @Override
    public void init() {
        String[] filters = getFilters();
        autoSingle = autoChooseSingle(filters);
        autoTime = autoChooseTime(filters);
        autoTimeDur = autoChooseTimeDur(filters);
        autoEvents = autoChooseEvents(spec, filters);
        autoInput = needAutomaticInputComponent() ? new AutomaticInputComponent<>(spec) : null;
    }

    /**
     * Whether an {@link AutomaticInputComponent} is needed.
     *
     * @return {@code true} if it is needed, {@code false} otherwise.
     */
    protected abstract boolean needAutomaticInputComponent();

    @Override
    public final Transition<S> chooseTransition(S state, List<Transition<S>> transitions, SimulationResult result) {
        // Handle no transitions possible.
        if (transitions.isEmpty()) {
            if (!isResetEnabled && !isUndoEnabled) {
                throw new SimulatorExitException(result);
            }
            return chooseTransitionInteractive(state, result);
        }

        // Handle the automatic choices.
        if (autoInput != null) {
            // Get the automatic choices.
            List<Transition<S>> autoTrans = listc(transitions.size());
            for (Transition<S> transition: transitions) {
                boolean isAuto;
                if (transition instanceof TimeTransition) {
                    isAuto = autoTime;
                } else {
                    RuntimeEvent<?> event;
                    event = ((EventTransition<?>)transition).event;
                    isAuto = autoEvents[event.idx];
                }

                if (isAuto) {
                    autoTrans.add(transition);
                }
            }

            // If there is only one transition, the choice is obvious.
            // Otherwise, let the automatic input component choose.
            if (autoTrans.size() == 1) {
                return first(autoTrans);
            } else if (autoTrans.size() > 1) {
                Assert.check(result == null);
                return autoInput.chooseTransition(state, autoTrans, result);
            }
        }

        // If there is only one possible transition, then return that one,
        // unless we always want to be asked.
        if (autoSingle && transitions.size() == 1) {
            return first(transitions);
        }

        // No automatic transitions. Interactively ask the user to choose.
        return chooseTransitionInteractive(state, transitions);
    }

    /**
     * Interactively let the user choose a transition to take, from the possible transitions. Semi-automatic mode has
     * already been handled. It is also allowed to return a {@link HistoryTransition}, depending on the values of
     * {@link #isResetEnabled} and {@link #isUndoEnabled}. Furthermore, it is allowed to throw a
     * {@link SimulatorExitException} with the {@link SimulationResult#USER_TERMINATED} simulation result, in case the
     * user requests to stop simulation.
     *
     * @param state The source state of the transitions.
     * @param transitions The transitions that are possible from the current state. Is never empty.
     * @return The chosen transition, that is to be taken.
     */
    protected abstract Transition<S> chooseTransitionInteractive(S state, List<Transition<S>> transitions);

    /**
     * Interactively let the user choose whether to perform a history transition, or stop simulation. It is allowed to
     * return a {@link HistoryTransition}, depending on the values of {@link #isResetEnabled} and {@link #isUndoEnabled}
     * (at least one of them is enabled). It is always allowed to throw a {@link SimulatorExitException} with the given
     * 'result'. It is not allowed to throw a {@link SimulatorExitException} with a different result. In particular, it
     * is not allowed to use {@link SimulationResult#USER_TERMINATED} as simulation result.
     *
     * @param state The source state of the transitions.
     * @param result The allowed simulation result for a {@link SimulatorExitException}. Is either
     *     {@link SimulationResult#DEADLOCK} or {@link SimulationResult#ENDTIME_REACHED}.
     * @return The transition that is chosen to be taken.
     */
    protected abstract HistoryTransition<S> chooseTransitionInteractive(S state, SimulationResult result);

    @Override
    public Double getNextMaxEndTime(S state) {
        // If we use semi-automatic mode for time transition durations, then
        // let the automatic input component decide.
        if (autoTimeDur) {
            return autoInput.getNextMaxEndTime(state);
        }

        // No additional restrictions.
        return null;
    }

    @Override
    public final ChosenTargetTime chooseTargetTime(S state, double maxTargetTime) {
        // If we use semi-automatic mode for the delay duration of time
        // transitions, then let the automatic input component choose.
        if (autoTimeDur) {
            return autoInput.chooseTargetTime(state, maxTargetTime);
        }

        // Interactively ask the user to choose.
        return chooseTargetTimeInteractive(state, maxTargetTime);
    }

    /**
     * Interactively let the user choose the target time of the time transition to take, given the allowed interval
     * {@code (state.getTime() .. maxTargetTime]}. Semi-automatic mode has already been handled.
     *
     * @param state The source state of the time transition.
     * @param maxTargetTime The maximum allowed target time.
     * @return The chosen target time of the time transition to take.
     */
    protected abstract ChosenTargetTime chooseTargetTimeInteractive(S state, double maxTargetTime);
}
