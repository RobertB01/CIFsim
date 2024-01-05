//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

/** A choice made via the {@link InteractiveGuiInputEditor}. */
public class InteractiveGuiInputChoice {
    /**
     * The 0-based index of the chosen transition, if a specific transition was chosen, {@code -1} otherwise. Is always
     * {@code -1} for non-event transitions.
     */
    public int transIdx = -1;

    /**
     * The 0-based index of the chosen event, if an event was chosen without specifically choosing a transition,
     * {@code -1} otherwise. Is always {@code -1} for non-event choices.
     */
    public int eventIdx = -1;

    /** Whether a time transition was chosen. */
    public boolean timeChosen = false;

    /**
     * The chosen target time of the time transition, or {@code null} if no {@link #timeChosen time} transition was
     * chosen, or semi-automatic mode is enabled for time transition delay durations.
     */
    public ChosenTargetTime targetTime = null;

    /** Whether a reset transition was chosen. */
    public boolean resetChosen = false;

    /** How many transitions to undo ({@code > 0}), or {@code 0} if no undo transition was chosen. */
    public int undoCount = 0;

    /**
     * Has a choice been made about what transition to choose, for when at least one transition is possible?
     *
     * @return {@code true} if a choice has been made, {@code false} otherwise.
     */
    public boolean hasTransitionChoice() {
        return transIdx >= 0 || // Event transition chosen.
                eventIdx >= 0 || // Event chosen.
                timeChosen || // Time transition chosen.
                resetChosen || // Reset transition chosen.
                undoCount > 0; // Undo transition chosen.
    }

    /**
     * Has a choice been made about what delay duration to choose?
     *
     * @return {@code true} if a choice has been made, {@code false} otherwise.
     */
    public boolean hasDelayChoice() {
        return timeChosen;
    }

    /**
     * Has a choice been made about what to do, for when no transitions are possible.
     *
     * @return {@code true} if a choice has been made, {@code false} otherwise.
     */
    public boolean hasNoTransitionChoice() {
        return resetChosen || // Reset transition chosen.
                undoCount > 0; // Undo transition chosen.
    }
}
