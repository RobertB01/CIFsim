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

package org.eclipse.escet.cif.explorer.runtime;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Interface for constructing explorer states. */
public abstract class AbstractStateFactory {
    /** Whether non-initial states should be unfolded. */
    public final boolean unfoldExplorerState;

    /**
     * Constructor of the {@link AbstractStateFactory} class.
     *
     * @param unfoldExplorerState Whether non-initial states should be unfolded.
     */
    public AbstractStateFactory(boolean unfoldExplorerState) {
        this.unfoldExplorerState = unfoldExplorerState;
    }

    /**
     * Construct an initial state for the explorer.
     *
     * @param expl Explorer managing the exploration process.
     * @return New initial state.
     */
    public abstract InitialState makeInitial(Explorer expl);

    /**
     * Construct a new state to be used as target of a transition.
     *
     * @param event Event taking place at the transition, {@code null} means 'tau' event.
     * @param prevState Source state.
     * @return The created next state.
     */
    public abstract ExplorerState makeExplorerState(Event event, BaseState prevState);

    /**
     * If {@link #unfoldExplorerState}, this method performs the actual unfolding. Default implementation should not be
     * called.
     *
     * @param transData Originating state and transition information.
     * @param newState New target explorer state.
     * @return Unfolded target states to add.
     */
    public List<ExplorerState> doUnfoldExplorerState(TransitionData transData, ExplorerState newState) {
        return null; // Will crash if unfoldExplorerState is set.
    }
}
