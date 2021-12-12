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

package org.eclipse.escet.cif.common;

import java.util.Collection;
import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;

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
    public static void collectEvents(ComplexComponent comp, Collection<Event> events) {
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
     * Collect the controllable events declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp The component.
     * @param ctrlEvents The controllable events collected so far. Is modified in-place.
     */
    public static void collectControllableEvents(ComplexComponent comp, Collection<Event> ctrlEvents) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Event && ((Event)decl).getControllable() != null && ((Event)decl).getControllable()) {
                ctrlEvents.add((Event)decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectControllableEvents((ComplexComponent)child, ctrlEvents);
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
    public static void collectAutomata(ComplexComponent comp, Collection<Automaton> automata) {
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

    /**
     * Collect the discrete and input variables declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp The component.
     * @param variables The discrete and input variables collected so far. Is modified in-place.
     */
    public static void collectDiscAndInputVariables(ComplexComponent comp, Collection<Declaration> variables) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof DiscVariable || decl instanceof InputVariable) {
                variables.add(decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectDiscAndInputVariables((ComplexComponent)child, variables);
            }
        }
    }

    /**
     * Collect the {@link Declaration}s declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp The component.
     * @param declarations The declarations collected so far. Is modified in-place.
     */
    public static void collectDeclarations(ComplexComponent comp, Collection<Declaration> declarations) {
        // Collect locally.
        declarations.addAll(comp.getDeclarations());

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectDeclarations((ComplexComponent)child, declarations);
            }
        }
    }

    /**
     * Collect the I/O declarations declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp The component.
     * @param declarations The I/O declarations collected so far. Is modified in-place.
     */
    public static void collectIoDeclarations(ComplexComponent comp, Collection<IoDecl> declarations) {
        // Collect locally.
        declarations.addAll(comp.getIoDecls());

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectIoDeclarations((ComplexComponent)child, declarations);
            }
        }
    }

    /**
     * Collect the {@link EnumDecl}s declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp The component.
     * @param enumDecls The enumeration declarations collected so far. Is modified in-place.
     */
    public static void collectEnumDecls(ComplexComponent comp, List<EnumDecl> enumDecls) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof EnumDecl) {
                enumDecls.add((EnumDecl)decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectEnumDecls((ComplexComponent)child, enumDecls);
            }
        }
    }
}
