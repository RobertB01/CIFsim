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

package org.eclipse.escet.cif.simulator;

import java.util.Deque;
import java.util.LinkedList;

import org.eclipse.escet.cif.simulator.options.HistorySizeOption;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.java.Assert;

/** CIF simulator state history. */
public class CifSimulatorHistory {
    /** The initial state. */
    private final RuntimeState initialState;

    /**
     * Stack of history states. Does not include the initial state. The oldest state is at the beginning, the newest
     * state is at the end. The newest/last state is the current state, unless the stack is empty, which means that the
     * {@link #initialState} is the current state. Never remembers more than {@link #maxSize} states. If already
     * contains {@link #maxSize} states, and another state is added, oldest state is removed, causing a {@link #hasGap
     * gap} between the then oldest state and the {@link #initialState}.
     */
    private Deque<RuntimeState> stateStack;

    /**
     * The maximum number of history states to remember, excluding the initial state, i.e. the maximum size of
     * {@link #stateStack}. May be {@code null} for infinite maximum (no maximum). May be {@code null} to disable undo
     * and only support reset.
     */
    private final Integer maxSize;

    /** Whether there is a gap between the oldest state on the stack, and the initial state. */
    private boolean hasGap = false;

    /**
     * Constructor for the {@link CifSimulatorHistory} class.
     *
     * @param initialState The initial state.
     */
    public CifSimulatorHistory(RuntimeState initialState) {
        this.initialState = initialState;
        maxSize = HistorySizeOption.getMaximum();
        stateStack = new LinkedList<>();
    }

    /**
     * Returns the initial state.
     *
     * @return The initial state.
     */
    public RuntimeState getInitialState() {
        return initialState;
    }

    /**
     * Adds a newly reached state to the state history.
     *
     * @param state The newly reached state.
     */
    public void addState(RuntimeState state) {
        // Skip if undo is disabled.
        if (!isUndoEnabled()) {
            return;
        }

        // If maximum number of undo states stored, remove oldest. Causes a
        // new gap or increases the already present gap.
        if (maxSize != null && stateStack.size() == maxSize) {
            stateStack.removeFirst();
            hasGap = true;
        }

        // Add new state.
        stateStack.addLast(state);
    }

    /**
     * Can a reset be performed from the current state?
     *
     * @param state The current state.
     * @return {@code true} if the reset can be performed, {@code false} otherwise.
     */
    public boolean canReset(RuntimeState state) {
        return state != initialState;
    }

    /**
     * Is undo functionality enabled? It is only disabled if the {@link #maxSize} is zero. If it is enabled, it may be
     * the case that it is currently not possible, see {@link #canUndo}.
     *
     * @return {@code true} if undo functionality is enabled, {@code false} otherwise.
     */
    public boolean isUndoEnabled() {
        return maxSize == null || maxSize != 0;
    }

    /**
     * Can the requested number of state undo operations be performed from the current state?
     *
     * @param state The current state.
     * @param count The number of state undo operations to perform. Must be a positive number.
     * @return {@code true} if the undo can be performed, {@code false} otherwise.
     */
    public boolean canUndo(RuntimeState state, int count) {
        // Precondition check.
        Assert.check(count > 0);

        // Can't undo for more than the allowed maximum.
        return count <= getMaxUndoCount(state);
    }

    /**
     * Returns the maximum number of transitions that can be undone using an undo transition.
     *
     * @param state The current state.
     * @return The maximum number of transitions that can be undone.
     */
    public int getMaxUndoCount(RuntimeState state) {
        // Can't undo if undo is disabled.
        if (!isUndoEnabled()) {
            return 0;
        }

        // In principle, can undo for as many states as are on the stack, as
        // each represents the target state of a transition. However, for the
        // last undo, we go back to the initial state, which requires
        // additional conditions, see below.
        int count = stateStack.size() - 1;

        // To go back to the initial state, we must not have a gap, and we must
        // not currently be in the initial state.
        if (!hasGap && state != initialState) {
            count++;
        }

        // Return the maximum undo count.
        return count;
    }

    /**
     * Reset state history to the initial state.
     *
     * @return The initial state.
     */
    public RuntimeState reset() {
        // Update administration.
        stateStack.clear();
        hasGap = false;

        // Return initial state.
        return initialState;
    }

    /**
     * Update state history for a specified number of undo operations.
     *
     * <p>
     * It is assumed that the caller has verified that the undo is possible, using {@link #canUndo}.
     * </p>
     *
     * @param count The number of state undo operations to perform. Must be a positive number.
     * @return The state after the undo operations.
     */
    public RuntimeState undo(int count) {
        // Precondition check.
        Assert.check(count > 0);
        Assert.check(stateStack.size() >= count);

        // Update administration.
        for (int i = 0; i < count; i++) {
            stateStack.removeLast();
        }

        // Return state reached after the undo operations.
        if (!stateStack.isEmpty()) {
            // Undo to non-initial state. It is the target state of the last
            // transition performed, that was not undone.
            return stateStack.peekLast();
        } else {
            // Undo to initial state.
            Assert.check(!hasGap);
            return initialState;
        }
    }
}
