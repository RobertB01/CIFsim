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

package org.eclipse.escet.cif.cif2mcrl2.storage;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Class for storing how non-local variables are used in an event. */
public class EventVarUsage {
    /** Event associated with the variable use. */
    public final Event event;

    /** Mapping of variables to their usage. */
    public Map<VariableData, VarUsage> varUses = map();

    /**
     * Constructor of the {@link EventVarUsage} class.
     *
     * @param event Event associated with this variable use.
     */
    public EventVarUsage(Event event) {
        this.event = event;
    }

    /**
     * Set variable access properties for reading, for variables on an edge with the {@link #event}.
     *
     * @param vars Variables being read in an edge.
     * @param firstEdge Whether this is the first time the event on an edge is encountered. Used to store whether a
     *     variable is always used with an event.
     * @param localVars Variables that are local to behavior processes.
     */
    public void addReadEdgeAccess(Set<VariableData> vars, boolean firstEdge, Set<VariableData> localVars) {
        // 1. Update the variables already associated with the event.
        for (Entry<VariableData, VarUsage> entry: varUses.entrySet()) {
            if (vars.contains(entry.getKey())) {
                entry.getValue().readAccess.everUsed = true;
            } else {
                entry.getValue().readAccess.alwaysUsed = false;
            }
        }

        // 2. Add new, non-local, variables.
        for (VariableData vd: vars) {
            if (localVars.contains(vd)) {
                continue;
            }
            if (varUses.containsKey(vd)) {
                continue;
            }

            VarUsage vu = new VarUsage(vd);
            vu.readAccess.everUsed = true;
            if (!firstEdge) {
                vu.readAccess.alwaysUsed = false;
            }
            varUses.put(vd, vu);
        }
    }

    /**
     * Set variable access properties for writing, for variables on an edge with the {@link #event}.
     *
     * @param vars Variables being written in an edge.
     * @param firstEdge Whether this is the first time the event on an edge is encountered. Used to store whether a
     *     variable is always used with an event.
     * @param localVars Variables that are local to behavior processes.
     */
    public void addWriteEdgeAccess(Set<VariableData> vars, boolean firstEdge, Set<VariableData> localVars) {
        // 1. Update the variables already associated with the event.
        for (Entry<VariableData, VarUsage> entry: varUses.entrySet()) {
            if (vars.contains(entry.getKey())) {
                entry.getValue().writeAccess.everUsed = true;
            } else {
                entry.getValue().writeAccess.alwaysUsed = false;
            }
        }

        // 2. Add new, non-local, variables.
        for (VariableData vd: vars) {
            if (localVars.contains(vd)) {
                continue;
            }
            if (varUses.containsKey(vd)) {
                continue;
            }

            VarUsage vu = new VarUsage(vd);
            vu.writeAccess.everUsed = true;
            if (!firstEdge) {
                vu.writeAccess.alwaysUsed = false;
            }
            varUses.put(vd, vu);
        }
    }
}
