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

package org.eclipse.escet.cif.simulator.runtime.model;

import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.simulator.options.CifSpecInitOption;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;
import org.eclipse.escet.cif.simulator.runtime.meta.StateObjectType;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;

/** Runtime state initializer base class. */
public abstract class RuntimeStateInit {
    /**
     * Initial values of the state variables (see {@link RuntimeState#getStateVarNames}), as provided by the
     * {@link CifSpecInitOption}. Is {@code null} until initialized by the {@link #processInitOption} method. May be
     * {@code null} for individual discrete variables or input variables, if not supplied using the option. Is
     * {@code null} for individual continuous variables.
     */
    protected Object[] optionVarValues;

    /**
     * 0-based indices of the initial locations of the automata (see {@link RuntimeSpec#automata}), as provided by the
     * {@link CifSpecInitOption}. Is {@code null} until initialized by the {@link #processInitOption} method. May be
     * {@code null} for individual automata, if not supplied using the option.
     */
    protected Integer[] optionLocIndices;

    /**
     * Process the {@link CifSpecInitOption}, filling {@link #optionVarValues} and {@link #optionLocIndices}.
     *
     * @param spec The runtime specification. Used to object the state objects metadata.
     * @param state The runtime state, to be initialized.
     */
    protected void processInitOption(RuntimeSpec<?> spec, RuntimeState state) {
        // Initialization.
        optionVarValues = new Object[state.getStateVarCount()];
        optionLocIndices = new Integer[state.getAutCount()];

        // Create a mapping from unescaped absolute names of objects to their meta information, for all discrete
        // variables, input variables and automata.
        int metaCount = optionVarValues.length + optionLocIndices.length;
        Map<String, RuntimeStateObjectMeta> metaMap = mapc(metaCount);
        for (RuntimeStateObjectMeta meta: spec.stateObjectsMeta) {
            switch (meta.type) {
                case TIME:
                case CONTINUOUS:
                case DERIVATIVE:
                case ALGEBRAIC:
                    break;

                case DISCRETE:
                case INPUT:
                case AUTOMATON:
                    metaMap.put(meta.plainName, meta);
                    break;
            }
        }

        // Process the initialization option values.
        List<String> inits = CifSpecInitOption.getInits();
        for (String init: inits) {
            // Separate name and value.
            int colonIdx = init.indexOf(':');
            if (colonIdx == -1) {
                String msg = fmt("Missing \":\" in initialization \"%s\".", init);
                throw new InvalidOptionException(msg);
            }
            String name = init.substring(0, colonIdx);
            String valueTxt = init.substring(colonIdx + 1);

            // Get variable/automaton.
            RuntimeStateObjectMeta obj = metaMap.get(name);
            if (obj == null) {
                String msg = fmt("Could not find a discrete variable, input variable or automaton with name \"%s\" for "
                        + "initialization \"%s\".", name, init);
                throw new InvalidOptionException(msg);
            }

            // Process value.
            if (obj.type == StateObjectType.AUTOMATON) {
                // Initial location for automaton. Match against names of
                // existing locations. All locations of an automaton have
                // unique names, so there is at most one match.
                RuntimeAutomaton<?> aut = spec.automata.get(obj.idx);
                int foundIdx = -1;
                for (int i = 0; i < aut.getLocCount(); i++) {
                    String locName = aut.getLocName(i);
                    if (locName.equals("*")) {
                        locName = null;
                    }
                    if (valueTxt.equals(locName)) {
                        foundIdx = i;
                        break;
                    }
                }
                if (foundIdx == -1) {
                    String msg = fmt("Could not find a location named \"%s\" in automaton \"%s\" for "
                            + "initialization \"%s\".", valueTxt, name, init);
                    throw new InvalidOptionException(msg);
                }

                // Store.
                if (optionLocIndices[obj.idx] != null) {
                    String msg = fmt("Duplicate initialization provided for automaton \"%s\".", name);
                    throw new InvalidOptionException(msg);
                }
                optionLocIndices[obj.idx] = foundIdx;
            } else {
                // Initial value for a discrete variable or input variable.
                Assert.check(obj.type == StateObjectType.DISCRETE || obj.type == StateObjectType.INPUT);
                String objectType = obj.type == StateObjectType.DISCRETE ? "discrete" : "input";

                // Use runtime literal reader to convert literal text to a runtime value.
                Object value;
                try {
                    value = processVarValue(obj, valueTxt);
                } catch (InputOutputException ex) {
                    String msg = fmt(
                            "Initial value \"%s\" for %s variable \"%s\" is invalid, for initialization \"%s\".",
                            valueTxt, objectType, name, init);
                    throw new InvalidOptionException(msg, ex);
                }

                // Store.
                if (optionVarValues[obj.idx] != null) {
                    String msg = fmt("Duplicate initialization provided for %s variable \"%s\".", objectType, name);
                    throw new InvalidOptionException(msg);
                }
                optionVarValues[obj.idx] = value;
            }
        }
    }

    /**
     * Processes the given literal value text for the given discrete or input variable.
     *
     * @param obj The runtime state object metadata for the discrete or input variable for which to process the value.
     * @param valueTxt The literal value text to convert to a runtime value.
     * @return The runtime value obtained form the literal value text.
     */
    protected abstract Object processVarValue(RuntimeStateObjectMeta obj, String valueTxt);
}
