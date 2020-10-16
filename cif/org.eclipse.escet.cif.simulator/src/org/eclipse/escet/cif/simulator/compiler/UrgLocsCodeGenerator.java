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

package org.eclipse.escet.cif.simulator.compiler;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.common.box.CodeBox;

/** Urgent locations code generator. */
public class UrgLocsCodeGenerator {
    /** Constructor for the {@link UrgLocsCodeGenerator} class. */
    private UrgLocsCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the urgent locations of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeUrgLocs(Specification spec, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("UrgLocs");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Urgent locations. */");
        h.add("public final class UrgLocs {");

        // Add body.
        CodeBox c = file.body;

        // Add 'evalUrgLocs' method.
        c.add("public static boolean evalUrgLocs(State state) {");
        c.indent();
        gencodeUrgLocsComponent(spec, ctxt, c);
        c.add();
        c.add("return false;");
        c.dedent();
        c.add("}");
    }

    /**
     * Generates checking code for the urgent locations of the component (recursively).
     *
     * @param comp The component.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     */
    private static void gencodeUrgLocsComponent(ComplexComponent comp, CifCompilerContext ctxt, CodeBox c) {
        // Generate locally (for automata).
        if (comp instanceof Automaton) {
            // Switch on location pointer value.
            Automaton aut = (Automaton)comp;
            c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
            c.indent();

            // Case per location, if needed.
            List<Location> locs = aut.getLocations();
            for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
                Location loc = locs.get(locIdx);
                if (loc.isUrgent()) {
                    c.add("case %s: return true;", ctxt.getLocationValueText(loc, locIdx));
                }
            }

            // Close the 'switch'.
            c.dedent();
            c.add("}");
        }

        // Generate recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                gencodeUrgLocsComponent((ComplexComponent)child, ctxt, c);
            }
        }
    }
}
