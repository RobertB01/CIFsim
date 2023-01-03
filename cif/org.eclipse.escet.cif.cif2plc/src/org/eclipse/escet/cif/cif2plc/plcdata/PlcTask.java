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

package org.eclipse.escet.cif.cif2plc.plcdata;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

/** PLC task. */
public class PlcTask extends PlcObject {
    /** The name of the task. */
    public final String name;

    /**
     * The positive cycle time in milliseconds, if periodic task scheduling is used, or zero if periodic task scheduling
     * is disabled.
     */
    public final int cycleTime;

    /** The priority of the task, in range [0 .. 65535]. */
    public final int priority;

    /** The POU instances of the task. */
    public List<PlcPouInstance> pouInstances = list();

    /**
     * Constructor for the {@link PlcTask} class.
     *
     * @param name The name of the task.
     * @param cycleTime The positive cycle time in milliseconds, if periodic task scheduling is used, or zero if
     *     periodic task scheduling is disabled.
     * @param priority The priority of the task, in range [0 .. 65535].
     */
    public PlcTask(String name, int cycleTime, int priority) {
        this.name = name;
        this.cycleTime = cycleTime;
        this.priority = priority;
    }
}
