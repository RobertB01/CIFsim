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

package org.eclipse.escet.cif.simulator.runtime.model;

import org.eclipse.escet.cif.common.CifTextUtils;

/**
 * Runtime event representation.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class RuntimeEvent<S extends RuntimeState> {
    /**
     * The absolute name of the event. For event 'tau', it is {@code "tau"}. For all other events, it is the absolute
     * name of the event in the CIF model, with keyword escaping, obtained via {@link CifTextUtils#getAbsName}.
     */
    public final String name;

    /** The unique 0-based index of the event. */
    public final int idx;

    /** Is this event the 'tau' event? */
    public final boolean isTauEvent;

    /** Is the event controllable? {@code null} for neither controllable nor uncontrollable. */
    public final Boolean controllable;

    /**
     * Constructor for the {@link RuntimeEvent} class.
     *
     * @param name The absolute name of the event. For event 'tau', it is {@code "tau"}. For all other events, it is the
     *     absolute name of the event in the CIF model, with keyword escaping, obtained via
     *     {@link CifTextUtils#getAbsName}.
     * @param idx The unique 0-based index of the event.
     * @param controllable Is the event controllable? {@code null} for neither controllable nor uncontrollable.
     */
    public RuntimeEvent(String name, int idx, Boolean controllable) {
        this.name = name;
        this.idx = idx;
        this.isTauEvent = name.equals("tau");
        this.controllable = controllable;
    }

    /**
     * Fills the {@link RuntimeSpec#syncData}, {@link RuntimeSpec#sendData}, {@link RuntimeSpec#recvData}, and/or
     * {@link RuntimeSpec#tauData} in the {@link RuntimeSpec specification} for this event.
     *
     * @param state The state to use to evaluate the guards.
     * @return {@code true} if the event is enabled (guard-wise), {@code false} otherwise.
     */
    public abstract boolean fillData(S state);

    /**
     * Is this event allowed by the state/event exclusion invariants, for the given state?
     *
     * @param state The state to use to evaluate the state/event exclusion invariants.
     * @return {@code true} if the event is allowed, {@code false} if it is disabled.
     */
    public abstract boolean allowedByInvs(S state);
}
