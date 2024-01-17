//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.settings;

/** Events for which to allow non-determinism. */
public enum AllowNonDeterminism {
    /** Disallow non-determinism for all events. */
    NONE(false, false),

    /** Allow non-determinism only for controllable events. */
    CONTROLLABLE(true, false),

    /** Allow non-determinism only for uncontrollable events. */
    UNCONTROLLABLE(false, true),

    /** Allow non-determinism for all events. */
    ALL(true, true);

    /** Whether to allow non-determinism for controllable events. */
    private final boolean allowForControllableEvents;

    /** Whether to allow non-determinism for uncontrollable events. */
    private final boolean allowForUncontrollableEvents;

    /**
     * Constructor for the {@link AllowNonDeterminism} enumeration.
     *
     * @param allowForControllableEvents Whether to allow non-determinism for controllable events.
     * @param allowForUncontrollableEvents Whether to allow non-determinism for uncontrollable events.
     */
    private AllowNonDeterminism(boolean allowForControllableEvents, boolean allowForUncontrollableEvents) {
        this.allowForControllableEvents = allowForControllableEvents;
        this.allowForUncontrollableEvents = allowForUncontrollableEvents;
    }

    /**
     * Returns whether non-determinism is allowed for the event.
     *
     * @param isControllableEvent Whether the event is controllable ({@code true}) or uncontrollable ({@code false}).
     * @return {@code true} if non-determinism is allowed for the event, {@code false} otherwise.
     */
    public boolean allowFor(boolean isControllableEvent) {
        return isControllableEvent ? allowForControllableEvents : allowForUncontrollableEvents;
    }
}
