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

import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.invToStr;
import static org.eclipse.escet.cif.common.CifValueUtils.isTimeConstant;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.common.CifInvariantUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.box.CodeBox;

/** State invariant predicate code generator. */
public class StateInvPredCodeGenerator {
    /** Constructor for the {@link StateInvPredCodeGenerator} class. */
    private StateInvPredCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the state invariant predicates of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeStateInvPreds(Specification spec, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("StateInvPreds");

        // Add header.
        CodeBox h = file.header;
        h.add("/** State invariants. */");
        h.add("public final class StateInvPreds {");

        // Add body.
        CodeBox c = file.body;

        // Add 'evalInvPreds' method.
        c.add("public static boolean evalStateInvPreds(State state, boolean initial) {");
        c.indent();

        gencodeEvalComponent(spec, ctxt, c);
        c.add("// Invariants for current locations of automata.");
        for (Automaton aut: ctxt.getAutomata()) {
            c.add("if (!evalStateInvPreds%s(state, initial)) return false;", ctxt.getAutClassName(aut));
        }

        c.add();
        c.add("// All invariant predicates satisfied.");
        c.add("return true;");
        c.dedent();
        c.add("}");

        // Add methods for the evaluation of invariant predicates in locations.
        for (Automaton aut: ctxt.getAutomata()) {
            gencodeEvalAutLocs(aut, ctxt, c);
        }
    }

    /**
     * Generates state invariant evaluation code for the state invariants of the component (recursively). This does not
     * include the invariants of the locations.
     *
     * @param comp The component.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     */
    private static void gencodeEvalComponent(ComplexComponent comp, CifCompilerContext ctxt, CodeBox c) {
        // Generate locally.
        List<Invariant> stateInvs = list();
        for (Invariant inv: comp.getInvariants()) {
            if (inv.getInvKind() != InvKind.STATE) {
                continue;
            }
            stateInvs.add(inv);
        }

        String absName = getAbsName(comp);
        if (!stateInvs.isEmpty()) {
            c.add("// Invariants for \"%s\".", absName);
        }

        String compTxt = CifTextUtils.getComponentText2(comp);

        for (Invariant inv: stateInvs) {
            Expression pred = inv.getPredicate();

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Actual invariant predicate evaluation.
            String predTxt = exprToStr(pred);
            c.add("if (!(%s)) {", gencodeExpr(pred, ctxt, "state"));
            c.indent();
            c.add("if (initial) warn(\"Invariant \\\"%s\\\" of %s is not satisfied.\");", escapeJava(predTxt),
                    escapeJava(compTxt));
            c.add("return false;");
            c.dedent();
            c.add("}");

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of invariant \\\"%s\\\" of %s failed.\", e, state);",
                    escapeJava(predTxt), escapeJava(compTxt));
            c.dedent();
            c.add("}");

            // Check the invariant.
            checkInvTimeConstant(inv);

            // Warn about requirement invariants.
            SupKind kind = CifInvariantUtils.getSupKind(inv);
            if (kind == SupKind.REQUIREMENT) {
                warn("Invariant \"%s\" of %s is a requirement, but will be simulated as a plant.", predTxt, compTxt);
            }
        }

        // Generate recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                gencodeEvalComponent((ComplexComponent)child, ctxt, c);
            }
        }
    }

    /**
     * Generates invariant evaluation code for the invariants of the locations of the given automaton.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     */
    private static void gencodeEvalAutLocs(Automaton aut, CifCompilerContext ctxt, CodeBox c) {
        c.add();
        c.add("private static boolean evalStateInvPreds%s(State state, boolean initial) {", ctxt.getAutClassName(aut));
        c.indent();

        c.add("// Invariants for current location.");
        c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
        c.indent();
        List<Location> locs = aut.getLocations();
        for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
            // Get location.
            Location loc = locs.get(locIdx);

            // If no invariants, then no 'case'.
            List<Invariant> locInvs = list();
            for (Invariant inv: loc.getInvariants()) {
                if (inv.getInvKind() != InvKind.STATE) {
                    continue;
                }
                locInvs.add(inv);
            }
            if (locInvs.isEmpty()) {
                continue;
            }

            // Add 'case'.
            c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
            c.indent();

            String locTxt = CifTextUtils.getLocationText2(loc);

            for (Invariant inv: locInvs) {
                Expression pred = inv.getPredicate();

                // Start of 'try'.
                c.add("try {");
                c.indent();

                // Actual invariant predicate evaluation.
                String predTxt = exprToStr(pred);
                c.add("if (!(%s)) {", gencodeExpr(pred, ctxt, "state"));
                c.indent();
                c.add("if (initial) warn(\"Invariant \\\"%s\\\" of %s is not satisfied.\");", escapeJava(predTxt),
                        escapeJava(locTxt));
                c.add("return false;");
                c.dedent();
                c.add("}");

                // End of 'try'.
                c.dedent();
                c.add("} catch (CifSimulatorException e) {");
                c.indent();
                c.add("throw new CifSimulatorException(\"Evaluation of invariant \\\"%s\\\" of %s failed.\", e, "
                        + "state);", escapeJava(predTxt), escapeJava(locTxt));
                c.dedent();
                c.add("}");

                // Check the invariant.
                checkInvTimeConstant(inv);

                // Warn about requirement invariants.
                SupKind kind = CifInvariantUtils.getSupKind(inv);
                if (kind == SupKind.REQUIREMENT) {
                    warn("Invariant \"%s\" of %s is a requirement, but will be simulated as a plant.", predTxt, locTxt);
                }
            }
            c.add("break;");
            c.dedent();
        }
        c.dedent();
        c.add("}");

        c.add();
        c.add("// All invariants satisfied.");
        c.add("return true;");

        c.dedent();
        c.add("}");
    }

    /**
     * Checks the given invariant, and makes sure it is time constant.
     *
     * @param inv The invariant.
     * @throws UnsupportedException If the invariant is time dependent.
     */
    private static void checkInvTimeConstant(Invariant inv) {
        if (isTimeConstant(inv.getPredicate())) {
            return;
        }

        // Time dependent invariants are currently not supported. If they were
        // supported, consider this specification:
        //
        // plant a:
        // location l1:
        // initial;
        // edge tau goto l2;
        // location l2:
        // invariant time >= 10.0;
        // end
        //
        // then event 'tau' in location 'l1' is not allowed for the first
        // 10 time units. That is, we would have to take into account the
        // invariants of the target locations of the edges of the current
        // locations when calculating the maximum delay.

        // Furthermore, consider:
        //
        // invariant time <= 10.0;
        //
        // we can delay for 10 time units. However, the ODE solver will delay
        // until 10.000000000001 or so. After that, we would have to check the
        // invariants, to make sure they are still valid, and thus still allow
        // subsequent transitions. In fact, if the invariant wouldn't hold
        // after the time transition, the new state is invalid.

        String msg = fmt("Time dependent invariants are currently not supported by the CIF simulator: \"%s\".",
                escapeJava(invToStr(inv, false)));
        throw new UnsupportedException(msg);
    }
}
