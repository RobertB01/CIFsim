//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.equivalence;

import org.eclipse.escet.cif.eventbased.automata.Event;

/** Reason for why a block was split from it origin block. */
public class SplitReason {
    /**
     * The block that the event points to, or {@code null} if no block is pointed towards, either because the reason is
     * because of markings, or the event is not enabled.
     */
    public Block block;

    /** The event that resulted in the split, or {@code null} if it is because of markings. */
    public Event event;

    /**
     * Creates a splitting reason.
     *
     * @param block The block that the event points to, or {@code null} if no block is pointed towards.
     * @param event The event that resulted in the split, or {@code null} if it was because of markings.
     */
    public SplitReason(Block block, Event event) {
        this.block = block;
        this.event = event;
    }
}
