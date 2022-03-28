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

package org.eclipse.escet.cif.common;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
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
     * Function providing a stream of complex components to process, including the provided root component.
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp Root component to traverse.
     * @return Stream of contained complex components found, including the root component.
     */
    public static Stream<ComplexComponent> getComplexComponentsStream(ComplexComponent comp) {
        return StreamSupport.stream(new ComplexComponentSpliterator(comp, false), false);
    }

    /**
     * Function providing a stream of complex components to process, including the provided root component.
     *
     * @param comp Root component to traverse.
     * @param traverseCompDefs Whether to search component definitions as well.
     * @return Stream of contained complex components found, including the root component.
     */
    public static Stream<ComplexComponent> getComplexComponentsStream(ComplexComponent comp, boolean traverseCompDefs) {
        return StreamSupport.stream(new ComplexComponentSpliterator(comp, traverseCompDefs), false);
    }

    /**
     * Function that gives a stream of event declarations from the complex components tree.
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param comp Root complex component to search for events.
     * @return Stream with the event declarations in the tree.
     */
    public static Stream<Event> getEventDeclarationsStream(ComplexComponent comp) {
        return getComplexComponentsStream(comp).flatMap(cc -> cc.getDeclarations().stream())
                .filter(decl -> decl instanceof Event).map(decl -> (Event)decl);
    }

    /**
     * Collect the events declared in the given component (recursively). Note that since the 'tau' event is not declared
     * explicitly, it will not be collected.
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param <T> The type of the collection for storing the found events.
     * @param comp The component.
     * @param events The events collected so far. Is modified in-place.
     * @return The updated events collection.
     */
    public static <T extends Collection<Event>> T collectEvents(ComplexComponent comp, T events) {
        getEventDeclarationsStream(comp).collect(Collectors.toCollection(() -> events));
        return events;
    }

    /**
     * Collect the controllable events declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param <T> The type of the collection for storing the found controllable events.
     * @param comp The component.
     * @param ctrlEvents The controllable events collected so far. Is modified in-place.
     * @return The updated controllable events collection.
     */
    public static <T extends Collection<Event>> T collectControllableEvents(ComplexComponent comp, T ctrlEvents) {
        getEventDeclarationsStream(comp).filter(ed -> ed.getControllable() != null && ed.getControllable())
                .collect(Collectors.toCollection(() -> ctrlEvents));
        return ctrlEvents;
    }

    /**
     * Collect the automata declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param <T> The type of the collection for storing the found automata.
     * @param comp The component.
     * @param automata The automata collected so far. Is modified in-place.
     * @return The updated automata collection.
     */
    public static <T extends Collection<Automaton>> T collectAutomata(ComplexComponent comp, T automata) {
        getComplexComponentsStream(comp).filter(cc -> cc instanceof Automaton).map(cc -> (Automaton)cc)
                .collect(Collectors.toCollection(() -> automata));
        return automata;
    }

    /**
     * Collect the {@link Declaration}s declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param <T> The type of the collection for storing found declarations.
     * @param comp The component.
     * @param declarations The declarations collected so far. Is modified in-place.
     * @return The updated declarations collection.
     */
    public static <T extends Collection<Declaration>> T collectDeclarations(ComplexComponent comp, T declarations) {
        getComplexComponentsStream(comp).forEach(cc -> declarations.addAll(cc.getDeclarations()));
        return declarations;
    }

    /**
     * Collect the discrete and input variables declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param <T> The type of the collection for storing found discrete and input variables.
     * @param comp The component.
     * @param variables The discrete and input variables collected so far. Is modified in-place.
     * @return The updated variables collection.
     */
    public static <T extends Collection<Declaration>> T collectDiscAndInputVariables(ComplexComponent comp,
            T variables)
    {
        getComplexComponentsStream(comp).flatMap(cc -> cc.getDeclarations().stream())
                .filter(decl -> decl instanceof DiscVariable || decl instanceof InputVariable)
                .collect(Collectors.toCollection(() -> variables));
        return variables;
    }

    /**
     * Collect the I/O declarations declared in the given component (recursively).
     *
     * <p>
     * Does not support component definition/instantiation.
     * </p>
     *
     * @param <T> The type of the collection for storing found I/O declarations.
     * @param comp The component.
     * @param declarations The I/O declarations collected so far. Is modified in-place.
     * @return The updated I/O declarations collection.
     */
    public static <T extends Collection<IoDecl>> T collectIoDeclarations(ComplexComponent comp, T declarations) {
        getComplexComponentsStream(comp).forEach(cc -> declarations.addAll(cc.getIoDecls()));
        return declarations;
    }

    /**
     * Collect the {@link EnumDecl}s declared in the given component (recursively).
     *
     * @param <T> The type of the collection for storing found enum declarations.
     * @param comp The component.
     * @param enumDecls The enumeration declarations collected so far. Is modified in-place.
     * @return The updated enum declarations collection.
     */
    public static <T extends Collection<EnumDecl>> T collectEnumDecls(ComplexComponent comp, T enumDecls) {
        getComplexComponentsStream(comp, true).flatMap(cc -> cc.getDeclarations().stream())
                .filter(decl -> decl instanceof EnumDecl).map(decl -> (EnumDecl)decl)
                .collect(Collectors.toCollection(() -> enumDecls));
        return enumDecls;
    }

    /**
     * Constructs a finite stream of {@link ComplexComponent} starting with the given root component.
     *
     * <p>
     * Modifying the structure of the {@link Component} tree reachable from the root component is not safe after
     * constructing a {@link ComplexComponentSpliterator} instance for it. It may store found components internally
     * before providing them to the stream.
     * </p>
     */
    private static class ComplexComponentSpliterator implements Spliterator<ComplexComponent> {
        /** Whether to search component definitions as well. */
        private final boolean traverseCompDefs;

        /** Queue with discovered complex components that have not been processed yet. */
        private Deque<ComplexComponent> notDone = new ArrayDeque<>(32);

        /**
         * Constructor of the {@link ComplexComponentSpliterator} class.
         *
         * <p>
         * The provided root component and its nested {@link ComplexComponent}s are included in the generated stream.
         * </p>
         * <p>
         * After construction of an instance, the component tree reachable from the root component should not be
         * modified.
         * </p>
         *
         * @param traverseCompDefs Whether to search component definitions as well.
         * @param comp Root component to traverse.
         */
        public ComplexComponentSpliterator(ComplexComponent comp, boolean traverseCompDefs) {
            this.traverseCompDefs = traverseCompDefs;
            notDone.add(comp);
        }

        @Override
        public int characteristics() {
            return DISTINCT | NONNULL | ORDERED;
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override
        public boolean tryAdvance(Consumer<? super ComplexComponent> action) {
            if (notDone.isEmpty()) {
                return false;
            }

            ComplexComponent comp = notDone.poll();
            expandGroup(comp);
            action.accept(comp);
            return true;
        }

        /**
         * Examine the given complex component for child complex components.
         *
         * @param comp Component to examine.
         */
        private void expandGroup(ComplexComponent comp) {
            if (comp instanceof Group) {
                Group grp = (Group)comp;
                for (Component child: grp.getComponents()) {
                    if (child instanceof ComplexComponent) {
                        notDone.add((ComplexComponent)child);
                    }
                }
                if (traverseCompDefs) {
                    for (ComponentDef compDef: grp.getDefinitions()) {
                        notDone.add(compDef.getBody());
                    }
                }
            }
        }

        @Override
        public Spliterator<ComplexComponent> trySplit() {
            return null; // Don't allow splitting the stream.
        }
    }
}
