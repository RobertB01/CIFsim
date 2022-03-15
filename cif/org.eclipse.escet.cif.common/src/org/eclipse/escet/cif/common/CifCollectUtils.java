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
        getComplexComponents(comp)
                .forEach(cc ->
                {
                    cc.getDeclarations().stream().filter(decl -> decl instanceof Event)
                            .forEach(decl -> events.add((Event)decl));
                });
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
        getComplexComponents(comp).forEach(cc -> {
            cc.getDeclarations().stream().filter(decl -> decl instanceof Event
                    && ((Event)decl).getControllable() != null && ((Event)decl).getControllable())
                    .forEach(decl -> ctrlEvents.add((Event)decl));
        });
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
        getComplexComponents(comp).filter(cc -> cc instanceof Automaton).forEach(cc -> automata.add((Automaton)cc));
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
        getComplexComponents(comp).forEach(cc -> {
            cc.getDeclarations().stream().filter(decl -> decl instanceof DiscVariable || decl instanceof InputVariable)
                    .forEach(decl -> variables.add(decl));
        });
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
        getComplexComponents(comp).forEach(cc -> declarations.addAll(cc.getDeclarations()));
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
        getComplexComponents(comp).forEach(cc -> declarations.addAll(cc.getIoDecls()));
    }

    /**
     * Collect the {@link EnumDecl}s declared in the given component (recursively).
     *
     * @param comp The component.
     * @param enumDecls The enumeration declarations collected so far. Is modified in-place.
     */
    public static void collectEnumDecls(ComplexComponent comp, Collection<EnumDecl> enumDecls) {
        getComplexComponents(comp, true).forEach(cc -> {
            cc.getDeclarations().stream().filter(decl -> decl instanceof EnumDecl)
                    .forEach(decl -> enumDecls.add((EnumDecl)decl));
        });
    }

    /** Class to construct a finite stream of {@link ComplexComponent} in the given root component. */
    private static class ComplexComponentSpliterator implements Spliterator<ComplexComponent> {
        /** Whether to search component definitions as well. */
        private final boolean traverseCompDefs;

        /** Queue with discovered complex components that have not been processed yet. */
        private Deque<ComplexComponent> notDone = new ArrayDeque<>();

        /**
         * Constructor of the {@link ComplexComponentSpliterator} class.
         *
         * @param traverseCompDefs Whether to search component definitions as well.
         * @param comp Root component to traverse for all its {@link ComplexComponent}s.
         */
        public ComplexComponentSpliterator(ComplexComponent comp, boolean traverseCompDefs) {
            this.traverseCompDefs = traverseCompDefs;
            notDone.add(comp);
        }

        @Override
        public int characteristics() {
            return DISTINCT | IMMUTABLE | NONNULL | ORDERED;
        }

        @Override
        public long estimateSize() {
            long estimate = 0;
            for (ComplexComponent comp: notDone) {
                if (comp instanceof Group) {
                    Group grp = (Group)comp;
                    estimate += grp.getComponents().size();
                } else {
                    estimate++;
                }
            }
            return estimate; // Under-estimated size.
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

    /**
     * Function providing a stream of complex components to process from the root.
     *
     * @param comp Root component to traverse.
     * @return Stream of contained complex component found in the root component.
     */
    public static Stream<ComplexComponent> getComplexComponents(ComplexComponent comp) {
        return StreamSupport.stream(new ComplexComponentSpliterator(comp, false), false);
    }

    /**
     * Function providing a stream of complex components to process from the root.
     *
     * @param comp Root component to traverse.
     * @param traverseCompDefs Whether to search component definitions as well.
     * @return Stream of contained complex component found in the root component.
     */
    public static Stream<ComplexComponent> getComplexComponents(ComplexComponent comp, boolean traverseCompDefs) {
        return StreamSupport.stream(new ComplexComponentSpliterator(comp, traverseCompDefs), false);
    }
}
