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
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.invToStr;
import static org.eclipse.escet.cif.common.CifValueUtils.isTimeConstant;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

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
import org.eclipse.escet.common.java.Pair;

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

        // Collect the non-location state invariants linked to their parent components.
        List<Pair<Invariant, ComplexComponent>> invsComps = list();
        collectInvsComps(spec, invsComps);
        int subMethodCount = (int)Math.ceil(invsComps.size() / 100d);

        // Add 'evalInvPreds' method.
        c.add("public static boolean evalStateInvPreds(State state, boolean initial) {");
        c.indent();

        c.add("// Invariants not in locations of automata.");
        for (int i = 0; i < subMethodCount; i++) {
            c.add("if (!evalStateInvPreds%d(state, initial)) return false;", i);
        }

        c.add();
        c.add("// Invariants for current locations of automata.");
        for (Automaton aut: ctxt.getAutomata()) {
            c.add("if (!evalStateInvPreds%s(state, initial)) return false;", ctxt.getAutClassName(aut));
        }

        c.add();
        c.add("// All invariant predicates satisfied.");
        c.add("return true;");
        c.dedent();
        c.add("}");

        // Add methods for the evaluation of invariant predicates outside locations.
        gencodeEvalInvariants(invsComps, ctxt, c);

        // Add methods for the evaluation of invariant predicates in locations.
        for (Automaton aut: ctxt.getAutomata()) {
            gencodeEvalAutLocs(aut, ctxt, c);
        }
    }

    /**
     * Generates state invariant evaluation code for the supplied state invariants.
     *
     * @param invsComps The invariants linked to their parent component.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     */
    private static void gencodeEvalInvariants(List<Pair<Invariant, ComplexComponent>> invsComps,
            CifCompilerContext ctxt, CodeBox c)
    {
        if (invsComps.isEmpty()) {
            return;
        }

        c.add();
        c.add("private static boolean evalStateInvPreds0(State state, boolean initial) {");
        c.indent();

        ComplexComponent comp = invsComps.get(0).right;
        String absName = getAbsName(comp);
        c.add("// Invariants for \"%s\".", absName);

        List<ExprCodeGeneratorResult> exprResults = list();
        for (int i = 0; i < invsComps.size(); i++) {
            // New sub method.
            if ((i > 0) && (i % 100 == 0)) {
                c.add("// All invariants satisfied.");
                c.add("return true;");
                c.dedent();
                c.add("}");

                c.add();
                c.add("private static boolean evalStateInvPreds%d(State state, boolean initial) {", i / 100);
                c.indent();

                // Always print component name at the start of a new method.
                comp = invsComps.get(i).right;
                absName = getAbsName(comp);
                c.add("// Invariants for \"%s\".", absName);
            }

            if (comp != invsComps.get(i).right) {
                // Print component name if we have a 'new' component.
                comp = invsComps.get(i).right;
                absName = getAbsName(comp);
                c.add("// Invariants for \"%s\".", absName);
            }

            Invariant inv = invsComps.get(i).left;
            exprResults.add(gencodeEvalInvariant(inv, CifTextUtils.getComponentText2(comp), ctxt, c));
        }

        c.add("// All invariants satisfied.");
        c.add("return true;");
        c.dedent();
        c.add("}");

        // Add potential extra expression evaluation methods.
        for (ExprCodeGeneratorResult exprResult: exprResults) {
            exprResult.addExtraMethods(c);
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
        List<ExprCodeGeneratorResult> exprResults = list();
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

            for (Invariant inv: locInvs) {
                exprResults.add(gencodeEvalInvariant(inv, CifTextUtils.getLocationText2(loc), ctxt, c));
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

        // Add potential extra expression evaluation methods.
        for (ExprCodeGeneratorResult exprResult: exprResults) {
            exprResult.addExtraMethods(c);
        }
    }

    /**
     * Generates state invariant evaluation code.
     *
     * @param inv The invariant.
     * @param parentText An end-user readable textual (reference) representation of the parent (location or component),
     *     used in error messages.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     * @return The {@code ExprCodeGeneratorResult} for the generated Java code.
     */
    private static ExprCodeGeneratorResult gencodeEvalInvariant(Invariant inv, String parentText,
            CifCompilerContext ctxt, CodeBox c)
    {
        Expression pred = inv.getPredicate();

        // Start of 'try'.
        c.add("try {");
        c.indent();

        // Actual invariant predicate evaluation.
        String predTxt = exprToStr(pred);
        ExprCodeGeneratorResult result = gencodeExpr(pred, ctxt, "state");
        c.add("if (!(%s)) {", result);
        c.indent();
        c.add("if (initial) warn(\"Invariant \\\"%s\\\" of %s is not satisfied.\");", escapeJava(predTxt),
                escapeJava(parentText));
        c.add("return false;");
        c.dedent();
        c.add("}");

        // End of 'try'.
        c.dedent();
        c.add("} catch (CifSimulatorException e) {");
        c.indent();
        c.add("throw new CifSimulatorException(\"Evaluation of invariant \\\"%s\\\" of %s failed.\", e, state);",
                escapeJava(predTxt), escapeJava(parentText));
        c.dedent();
        c.add("}");

        // Check the invariant.
        checkInvTimeConstant(inv);

        // Warn about requirement invariants.
        if (inv.getSupKind() == SupKind.REQUIREMENT) {
            warn("Invariant \"%s\" of %s is a requirement, but will be simulated as a plant.", predTxt, parentText);
        }

        return result;
    }

    /**
     * Checks the given invariant, and makes sure it is time constant.
     *
     * @param inv The invariant.
     * @throws UnsupportedException If the invariant is time dependent.
     */
    private static void checkInvTimeConstant(Invariant inv) {
        // In the context of the simulator, input variables are regarded as being 'time constant'.
        if (isTimeConstant(inv.getPredicate(), true)) {
            return;
        }

        // Time dependent state invariants are currently not supported. If they were
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

        String msg = fmt("Time dependent state invariants are currently not supported by the CIF simulator: \"%s\".",
                escapeJava(invToStr(inv, false)));
        throw new UnsupportedException(msg);
    }

    /**
     * Collect the non-location state invariants linked to their parent component that are declared in the given
     * component (recursively).
     *
     * @param comp The component to search.
     * @param invsComps The state invariants and their parent component collected so far. Is modified in-place.
     */
    public static void collectInvsComps(ComplexComponent comp, List<Pair<Invariant, ComplexComponent>> invsComps) {
        // Collect locally.
        for (Invariant inv: comp.getInvariants()) {
            if (inv.getInvKind() != InvKind.STATE) {
                continue;
            }
            invsComps.add(pair(inv, comp));
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectInvsComps((ComplexComponent)child, invsComps);
            }
        }
    }
}
