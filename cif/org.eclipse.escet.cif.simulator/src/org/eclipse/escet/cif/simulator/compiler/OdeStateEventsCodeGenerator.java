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
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifValueUtils.isTimeConstant;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.CONT_SUB_STATE_FIELD_NAME;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Pair;

/**
 * ODE state events code generator.
 *
 * <p>
 * We support timed guards of edges, as well as timed state/event exclusion invariants. We don't support timed state
 * invariants.
 * </p>
 */
public class OdeStateEventsCodeGenerator {
    /** Constructor for the {@link OdeStateEventsCodeGenerator} class. */
    private OdeStateEventsCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the ODE state events of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeOdeStateEvents(Specification spec, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("OdeStateEvents");

        // Add header.
        CodeBox h = file.header;
        h.add("/** ODE state events. */");
        h.add("public final class OdeStateEvents {");

        // Add body.
        CodeBox c = file.body;

        // Collect time dependent predicates.
        Map<Expression, Pair<Integer, Integer>> preds = map();
        collectTimedPreds(spec, preds, 0);

        // Add fields for the time dependent predicates.
        for (int i = 0; i < preds.size(); i++) {
            c.add("public static OdeStateEvent<State> STATE_EVENT%d = new StateEvent%d();", i, i);
        }

        // Add 'getOdeStateEvents' method.
        c.add();
        c.add("public static List<OdeStateEvent<State>> getOdeStateEvents(State state) {");
        c.indent();

        c.add("List<OdeStateEvent<State>> rslt = list();");

        gencodeOdeStateEventsComponent(spec, preds, ctxt, c);

        c.add();
        c.add("return rslt;");
        c.dedent();
        c.add("}");

        // Add inner classes for the time dependent predicates.
        for (Entry<Expression, Pair<Integer, Integer>> entry: preds.entrySet()) {
            Expression pred = entry.getKey();
            int number = entry.getValue().left;
            int predOrigin = entry.getValue().right;

            // Add inner class header.
            c.add();
            c.add("public static final class StateEvent%d extends OdeStateEvent<State> {", number);
            c.indent();

            // Add constructor.
            c.add("public StateEvent%d() {", number);
            c.indent();
            c.add("super(Solver.getSolver());");
            c.dedent();
            c.add("}");

            // Add 'getPredText' method.
            c.add();
            c.add("@Override");
            c.add("public String getPredText() {");
            c.indent();
            c.add("return \"%s\";", escapeJava(exprToStr(pred)));
            c.dedent();
            c.add("}");

            // Add 'eval' method.
            c.add();
            c.add("@Override");
            c.add("public boolean eval(State state) {");
            c.indent();
            c.add("try {");
            c.indent();
            c.add("return %s;", gencodeExpr(pred, ctxt, "state"));
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            String origTxt;
            switch (predOrigin) {
                case 0:
                    origTxt = "guard";
                    break;
                case 1:
                    origTxt = "state/event exclusion invariant";
                    break;
                default:
                    throw new RuntimeException("Unknown pred origin: " + predOrigin);
            }
            c.add("throw new CifSimulatorException(fmt(\"Evaluation of timed %s \\\"%%s\\\" at time %%s failed.\", "
                    + "getPredText(), realToStr(state.%s.time)), e, state);", origTxt, CONT_SUB_STATE_FIELD_NAME);
            c.dedent();
            c.add("}");
            c.dedent();
            c.add("}");

            // Close inner class.
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Generates code for the retrieval of the ODE state events of the component (recursively).
     *
     * @param comp The component.
     * @param preds Mapping from the time dependent predicates collected so far, to their unique numbers and an
     *     indication of their origin (0 or timed guards, 1 for state/event exclusion invariants).
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     */
    private static void gencodeOdeStateEventsComponent(ComplexComponent comp,
            Map<Expression, Pair<Integer, Integer>> preds, CifCompilerContext ctxt, CodeBox c)
    {
        // Generate locally (state/event exclusion invariants of component).
        for (Invariant inv: comp.getInvariants()) {
            if (inv.getInvKind() == InvKind.STATE) {
                continue;
            }
            Expression pred = inv.getPredicate();
            Pair<Integer, Integer> info = preds.get(pred);
            if (info == null) {
                continue;
            }
            int number = info.left;
            c.add("rslt.add(STATE_EVENT%d);", number);
        }

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
                boolean caseAdded = false;

                // Add for all guards of all the outgoing edges.
                for (Edge edge: loc.getEdges()) {
                    for (Expression pred: edge.getGuards()) {
                        Pair<Integer, Integer> info = preds.get(pred);
                        if (info == null) {
                            continue;
                        }
                        int number = info.left;

                        if (!caseAdded) {
                            caseAdded = true;
                            c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
                            c.indent();
                        }

                        c.add("rslt.add(STATE_EVENT%d);", number);
                    }
                }

                // Add for state/event exclusion invariants of location.
                for (Invariant inv: loc.getInvariants()) {
                    if (inv.getInvKind() == InvKind.STATE) {
                        continue;
                    }
                    Expression pred = inv.getPredicate();
                    Pair<Integer, Integer> info = preds.get(pred);
                    if (info == null) {
                        continue;
                    }
                    int number = info.left;

                    if (!caseAdded) {
                        caseAdded = true;
                        c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
                        c.indent();
                    }

                    c.add("rslt.add(STATE_EVENT%d);", number);
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
                gencodeOdeStateEventsComponent((ComplexComponent)child, preds, ctxt, c);
            }
        }
    }

    /**
     * Collects the time dependent predicates from the component (recursively), and maps them to unique numbers.
     *
     * <p>
     * In the context of the simulator, input variables are regarded as being 'time constant'.
     * </p>
     *
     * @param comp The component.
     * @param preds Mapping from the time dependent predicates collected so far, to their unique numbers and an
     *     indication of their origin (0 or timed guards, 1 for state/event exclusion invariants). Is modified in-place.
     * @param number The next unique number to use.
     * @return The next unique number to use.
     */
    private static int collectTimedPreds(ComplexComponent comp, Map<Expression, Pair<Integer, Integer>> preds,
            int number)
    {
        // Collect locally (state/event exclusion invariants of component).
        for (Invariant inv: comp.getInvariants()) {
            if (inv.getInvKind() == InvKind.STATE) {
                continue;
            }
            Expression pred = inv.getPredicate();
            if (!isTimeConstant(pred, true)) {
                preds.put(pred, pair(number, 1));
                number++;
            }
        }

        // Collect locally (for locations of automata).
        if (comp instanceof Automaton) {
            for (Location loc: ((Automaton)comp).getLocations()) {
                // Guards of edges of automata.
                for (Edge edge: loc.getEdges()) {
                    for (Expression guard: edge.getGuards()) {
                        if (!isTimeConstant(guard, true)) {
                            preds.put(guard, pair(number, 0));
                            number++;
                        }
                    }
                }

                // State/event exclusion invariants of location of automata.
                for (Invariant inv: loc.getInvariants()) {
                    if (inv.getInvKind() == InvKind.STATE) {
                        continue;
                    }
                    Expression pred = inv.getPredicate();
                    if (!isTimeConstant(pred, true)) {
                        preds.put(pred, pair(number, 1));
                        number++;
                    }
                }
            }
        }

        // Collect recursively (in groups).
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                number = collectTimedPreds((ComplexComponent)child, preds, number);
            }
        }

        // Return the next unique number to use.
        return number;
    }
}
