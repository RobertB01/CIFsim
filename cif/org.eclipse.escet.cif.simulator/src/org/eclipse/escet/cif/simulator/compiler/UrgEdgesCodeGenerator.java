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

import static org.apache.commons.text.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.exprsToStr;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodePreds;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.truncate;

import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.common.box.CodeBox;

/** Urgent edges code generator. */
public class UrgEdgesCodeGenerator {
    /** Constructor for the {@link UrgEdgesCodeGenerator} class. */
    private UrgEdgesCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the urgent edges of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeUrgEdges(Specification spec, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("UrgEdges");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Urgent edges. */");
        h.add("public final class UrgEdges {");

        // Add body.
        CodeBox c = file.body;

        // Add 'evalUrgEdges' method.
        c.add("public static boolean evalUrgEdges(State state) {");
        c.indent();

        // Generate variable 'b'. Reused for all guards.
        c.add("boolean b; // temp var for pred eval rslts");

        // Generate code for the actual urgent edges.
        List<ExprCodeGeneratorResult> exprResults = gencodeUrgEdgesComponent(spec, ctxt, c);

        // Finish the method.
        c.add();
        c.add("return false;");
        c.dedent();
        c.add("}");

        // Add potential extra expression evaluation methods.
        for (ExprCodeGeneratorResult exprResult: exprResults) {
            exprResult.addExtraMethods(c);
        }
    }

    /**
     * Generates evaluation code for the guards of the urgent edges of the component (recursively).
     *
     * @param comp The component.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     * @return The {@code ExprCodeGeneratorResult}s for the generated Java code.
     */
    private static List<ExprCodeGeneratorResult> gencodeUrgEdgesComponent(ComplexComponent comp,
            CifCompilerContext ctxt, CodeBox c)
    {
        // Generate locally (for automata).
        List<ExprCodeGeneratorResult> exprResults = list();
        if (comp instanceof Automaton) {
            // Switch on location pointer value.
            Automaton aut = (Automaton)comp;
            c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
            c.indent();

            // Case per location, if needed.
            List<Location> locs = aut.getLocations();
            for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
                Location loc = locs.get(locIdx);
                boolean caseAdded = false;

                String locTxt = CifTextUtils.getLocationText2(loc);

                // Add for all urgent outgoing edges.
                for (Edge edge: loc.getEdges()) {
                    if (!edge.isUrgent()) {
                        continue;
                    }

                    // Add 'case' for the location, if not yet present.
                    if (!caseAdded) {
                        caseAdded = true;
                        c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
                        c.indent();
                    }

                    // Start of 'try'.
                    c.add("try {");
                    c.indent();

                    // Actual evaluation of guard.
                    ExprCodeGeneratorResult result = gencodePreds(edge.getGuards(), ctxt, "state");
                    c.add("b = %s;", result);
                    exprResults.add(result);

                    // End of 'try'.
                    c.dedent();
                    c.add("} catch (CifSimulatorException e) {");
                    c.indent();
                    c.add("throw new CifSimulatorException(\"Evaluation of guard(s) \\\"%s\\\" of an urgent edge of "
                            + "%s failed.\", e, state);", escapeJava(truncate(exprsToStr(edge.getGuards()), 1000)),
                            escapeJava(locTxt));
                    c.dedent();
                    c.add("}");

                    // If one urgent edge is enabled, delaying is forbidden.
                    c.add("if (b) return true;");
                }

                if (caseAdded) {
                    c.add("break;");
                    c.dedent();
                }
            }

            c.dedent();
            c.add("}");
        }

        // Generate recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                exprResults.addAll(gencodeUrgEdgesComponent((ComplexComponent)child, ctxt, c));
            }
        }

        return exprResults;
    }
}
