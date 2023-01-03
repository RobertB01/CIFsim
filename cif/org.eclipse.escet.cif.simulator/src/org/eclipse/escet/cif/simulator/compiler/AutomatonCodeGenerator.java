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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Pair.pair;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.common.java.Pair;

/** Automaton code generator. */
public class AutomatonCodeGenerator {
    /** Constructor for the {@link AutomatonCodeGenerator} class. */
    private AutomatonCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the automata of the specification.
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeAutomata(CifCompilerContext ctxt) {
        for (Automaton aut: ctxt.getAutomata()) {
            gencodeAutomaton(aut, ctxt);
        }
    }

    /**
     * Generate Java code for the given automaton.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeAutomaton(Automaton aut, CifCompilerContext ctxt) {
        // Generate class body. Analyze automaton first, to choose the best
        // code generation.
        if (isSimpleAut(aut)) {
            AutomatonSimpleCodeGenerator.gencodeAutomaton(aut, ctxt);
        } else {
            AutomatonNormalCodeGenerator.gencodeAutomaton(aut, ctxt);
        }
    }

    /**
     * Is the given automaton simple? An automaton is simple if it does not have any guards or updates, does have any
     * 'tau' edges, and does not perform any channel communications.
     *
     * @param aut The automaton.
     * @return {@code true} if the automaton is simple, {@code false} otherwise.
     */
    private static boolean isSimpleAut(Automaton aut) {
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                if (!edge.getGuards().isEmpty()) {
                    return false;
                }
                if (!edge.getUpdates().isEmpty()) {
                    return false;
                }
                if (edge.getEvents().isEmpty()) {
                    return false;
                }
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    if (edgeEvent instanceof EdgeSend) {
                        return false;
                    }
                    if (edgeEvent instanceof EdgeReceive) {
                        return false;
                    }

                    Expression eventRef = edgeEvent.getEvent();
                    if (eventRef instanceof TauExpression) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Collect the automata of the given component (recursively).
     *
     * @param comp The component.
     * @param automata The automata collected so far, as pairs of absolute names of the automata and the automata
     *     themselves. Is modified in-place.
     */
    public static void collectAutomata(ComplexComponent comp, List<Pair<String, Automaton>> automata) {
        if (comp instanceof Automaton) {
            // Add automaton.
            automata.add(pair(getAbsName(comp, false), (Automaton)comp));
        } else {
            // Collect recursively.
            for (Component child: ((Group)comp).getComponents()) {
                collectAutomata((ComplexComponent)child, automata);
            }
        }
    }
}
