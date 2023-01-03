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

package org.eclipse.escet.setext.generator.parser;

/** {@link LALR1AutomatonState} and {@link GrammarItem} combination for lookahead propagation. */
public class PropagationItem {
    /** The state to which to propagate. */
    public final LALR1AutomatonState state;

    /** The grammar item to which to propagate. */
    public final GrammarItem item;

    /**
     * Constructor for the {@link PropagationItem} class.
     *
     * @param state The state to which to propagate.
     * @param item The grammar item to which to propagate.
     */
    public PropagationItem(LALR1AutomatonState state, GrammarItem item) {
        this.state = state;
        this.item = item;
    }
}
