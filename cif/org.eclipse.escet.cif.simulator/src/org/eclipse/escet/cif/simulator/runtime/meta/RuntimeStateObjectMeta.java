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

package org.eclipse.escet.cif.simulator.runtime.meta;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Comparator;

import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.java.Strings;

/** Static meta data on a single object from the runtime state. */
public class RuntimeStateObjectMeta {
    /** Singleton instance of {@link RuntimeStateObjectMetaComparator}. */
    public static final RuntimeStateObjectMetaComparator SORTER = new RuntimeStateObjectMetaComparator();

    /**
     * The 0-based index of the object:
     * <ul>
     * <li>For automata, the index in {@link RuntimeSpec#automata}.</li>
     * <li>For variable 'time', always {@code 0}.</li>
     * <li>For discrete variables, the index in {@link RuntimeState#getStateVarNames}.</li>
     * <li>For input variables, the index in {@link RuntimeState#getStateVarNames}.</li>
     * <li>For continuous variables, the index in {@link RuntimeState#getStateVarNames}.</li>
     * <li>For derivatives of continuous variables, the index in {@link RuntimeState#getStateVarNames} of the continuous
     * variable.</li>
     * <li>For algebraic variables, the index in {@link RuntimeState#getAlgVarNames}.</li>
     * </ul>
     */
    public final int idx;

    /** The type of the state object, as in what kind of object it is, not its data type. */
    public final StateObjectType type;

    /** The absolute name of the state object, with keyword escaping. */
    public final String name;

    /** The absolute name of the state object, without keyword escaping. */
    public final String plainName;

    /**
     * Constructor for the {@link RuntimeStateObjectMeta} class.
     *
     * @param idx The 0-based index of the object. See {@link #idx}.
     * @param type The type of the state object, as in what kind of object it is, not its data type.
     * @param name The absolute name of the state object, with keyword escaping.
     */
    public RuntimeStateObjectMeta(int idx, StateObjectType type, String name) {
        this.idx = idx;
        this.type = type;
        this.name = name;
        this.plainName = name.replace("$", "");
    }

    @Override
    public String toString() {
        return fmt("%s(%s,%d)", name, type, idx);
    }

    /**
     * Comparator for {@link RuntimeStateObjectMeta}. Sorts in ascending order primarily based on {@link #plainName},
     * and secondarily based on {@link #name}.
     *
     * @see Strings#SORTER
     */
    public static class RuntimeStateObjectMetaComparator implements Comparator<RuntimeStateObjectMeta> {
        @Override
        public int compare(RuntimeStateObjectMeta meta1, RuntimeStateObjectMeta meta2) {
            int rslt = Strings.SORTER.compare(meta1.plainName, meta2.plainName);
            if (rslt != 0) {
                return rslt;
            }
            return Strings.SORTER.compare(meta1.name, meta2.name);
        }
    }
}
