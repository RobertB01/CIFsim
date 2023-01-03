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

package org.eclipse.escet.cif.simulator.input.trace;

import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;

/** Event trace command. */
public class EventTraceCommand extends TraceCommand {
    /** The 0-based index of the event in {@link RuntimeSpec#events}. */
    public final int idx;

    /**
     * Constructor for the {@link EventTraceCommand} class.
     *
     * @param idx The 0-based index of the event in {@link RuntimeSpec#events}.
     */
    public EventTraceCommand(int idx) {
        this.idx = idx;
    }
}
