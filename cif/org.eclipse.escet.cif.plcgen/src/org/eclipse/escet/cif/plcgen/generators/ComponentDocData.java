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
import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Data of a complex component for generating PLC program documentation. */
public class ComponentDocData {
    /** The complex component that it describes. */
    public final ComplexComponent component;

    /** Variables of the component. */
    public final List<Declaration> variables = list();

    /** The uncontrollable events of the automaton. */
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
}
