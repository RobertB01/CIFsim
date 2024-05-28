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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Data of a complex component for generating PLC program documentation. */
public class ComponentDocData {
    /** The complex component that it describes. */
    public final ComplexComponent component;

    /** Variables of the component. */
    public final List<Declaration> variables = list();

    /** The uncontrollable events of the automaton, if the component is an automaton. */
    public final List<Event> uncontrollableEvents = list();

    /** The controllable events of the automaton. */
    public final List<Event> controllableEvents = list();

    /** If not {@code null}, the name of the PLC variable used to select edges of the automaton. */
    public String edgeVariableName = null;

    /**
     * Constructor of the {@link ComponentDocData} class.
     *
     * @param component Complex component that it describes.
     */
    public ComponentDocData(ComplexComponent component) {
        this.component = component;
    }

    /**
     * Add the given events to the component data.
     *
     * @param events Events to add.
     */
    public void addEvents(Collection<Event> events) {
        for (Event evt: events) {
            if (evt.getControllable()) {
                controllableEvents.add(evt);
            } else {
                uncontrollableEvents.add(evt);
            }
        }
    }

    /**
     * Get the full name of the component.
     *
     * @return The full name of the component.
     */
    public String getComponentName() {
        return CifTextUtils.getAbsName(component, false);
    }

    /**
     * Determine whether the component has no variables to report.
     *
     * @return Whether the component has no variables to report.
     */
    public boolean isEmptyVariables() {
        return variables.isEmpty() && edgeVariableName == null;
    }

    /**
     * Determine whether the component has nothing useful to report.
     *
     * @return Whether the component has nothing useful to report.
     */
    public boolean isEmpty() {
        boolean empty = isEmptyVariables();
        if (component instanceof Automaton) {
            empty &= uncontrollableEvents.isEmpty();
            empty &= controllableEvents.isEmpty();
        }
        return empty;
    }

    /** Sort the contents of the component. */
    public void sortData() {
        // Variables are stored by their containing component, thus their absolute name prefixes are all the same.
        Collections.sort(variables, Comparator.comparing(v -> v.getName()));
        Collections.sort(uncontrollableEvents, Comparator.comparing(e -> CifTextUtils.getAbsName(e, false)));
        Collections.sort(controllableEvents, Comparator.comparing(e -> CifTextUtils.getAbsName(e, false)));
    }
}
