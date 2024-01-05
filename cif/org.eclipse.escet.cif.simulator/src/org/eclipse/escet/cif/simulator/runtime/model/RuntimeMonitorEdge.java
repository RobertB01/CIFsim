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

package org.eclipse.escet.cif.simulator.runtime.model;

/**
 * Runtime monitor self-loop edge, with '{@code true}' guard, and without any updates. Every specification should have a
 * singleton instance of this class, for monitor self loops. The guard must never be evaluated.
 *
 * @param <S> The type of state objects to use.
 */
public final class RuntimeMonitorEdge<S extends RuntimeState> extends RuntimeSyncEdge<S> {
    @Override
    public boolean evalGuards(S state) {
        String msg = "Must not eval monitor guards.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public final void update(S source, S target) {
        // No updates.
    }
}
