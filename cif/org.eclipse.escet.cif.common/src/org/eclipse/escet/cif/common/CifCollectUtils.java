//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/**
 * CIF collect utility methods. This is not about collections of values, but about collecting certain kinds of objects
 * from a CIF specification.
 */
public class CifCollectUtils {
    /** Constructor for the {@link CifCollectUtils} class. */
    private CifCollectUtils() {
        // Static class.
    }

    /**
     * Collect the events declared in the given component (recursively). Note that since the 'tau' event is not declared
     * explicitly, it will not be collected.
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp The component.
     * @param events The events collected so far. Is modified in-place.
     */
    public static void collectEvents(ComplexComponent comp, List<Event> events) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Event) {
                events.add((Event)decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectEvents((ComplexComponent)child, events);
            }
        }
    }

    /**
     * Collect the automata declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp The component.
     * @param automata The automata collected so far. Is modified in-place.
     */
    public static void collectAutomata(ComplexComponent comp, List<Automaton> automata) {
        if (comp instanceof Automaton) {
            // Add automaton.
            automata.add((Automaton)comp);
        } else {
            // Collect recursively.
            for (Component child: ((Group)comp).getComponents()) {
                collectAutomata((ComplexComponent)child, automata);
            }
        }
    }
}
