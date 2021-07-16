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

package org.eclipse.escet.cif.simulator.compiler;

import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.invToStr;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.EVENT_CLS_PREFIX;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Pair.pair;

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
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;

/** Event code generator. */
public class EventCodeGenerator {
    /** Constructor for the {@link EventCodeGenerator} class. */
    private EventCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the events of a specification.
     *
     * @param spec The CIF specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeEvents(Specification spec, CifCompilerContext ctxt) {
        // Get all events from the specification, including 'tau'.
        List<Event> events = ctxt.getEvents();

        // Generate a unique name for 'tau' first, to avoid renaming of 'tau'.
        Event tau = ctxt.tauEvent;
        String tauName = ctxt.getEventClassName(tau);
        Assert.check(tauName.equals(EVENT_CLS_PREFIX + "_tau"));

        // Generate code for each event.
        for (int eventIdx = 0; eventIdx < events.size(); eventIdx++) {
            // Get event.
            Event event = events.get(eventIdx);
            boolean isTau = event == tau;
            boolean isComm = event.getType() != null;
            String absName = isTau ? "tau" : getAbsName(event);
            String ctrlTxt = (event.getControllable() == null) ? "null" : event.getControllable() ? "true" : "false";

            // Add new code file.
            String className = ctxt.getEventClassName(event);
            JavaCodeFile file = ctxt.addCodeFile(className);

            // Add header.
            CodeBox h = file.header;
            h.add("/** Event \"%s\". */", absName);
            h.add("public final class %s extends RuntimeEvent<State> {", className);

            // Add body.
            CodeBox c = file.body;

            // Add constructor.
            c.add("public %s() {", className);
            c.indent();
            c.add("super(\"%s\", %d, %s);", absName, eventIdx, ctrlTxt);
            c.dedent();
            c.add("}");

            // Add 'fillData' method.
            c.add();
            c.add("@Override");
            c.add("public boolean fillData(State state) {");
            c.indent();

            if (isComm) {
                c.add("// Send.");
                c.add("SPEC.sendData.get(%d).clear();", eventIdx);
                genFillCallsSend(event, c, ctxt);
                c.add("if (SPEC.sendData.get(%d).isEmpty()) return false;", eventIdx);
                c.add();

                c.add("// Receive.");
                c.add("SPEC.recvData.get(%d).clear();", eventIdx);
                genFillCallsRecv(event, c, ctxt);
                c.add("if (SPEC.recvData.get(%d).isEmpty()) return false;", eventIdx);
                c.add();
            }

            if (isTau) {
                c.add("SPEC.tauData.clear();");
                genFillCallsTau(c, ctxt);
                c.add("return !SPEC.tauData.isEmpty();");
            } else {
                c.add("// Sync.");
                c.add("boolean proceed;");
                genFillCallsSync(event, c, ctxt);
                c.add();
                c.add("// All done, possible so far.");
                if (!isComm && ctxt.getSyncAuts(event).isEmpty()) {
                    c.add("return false; // No aut has event in alphabet.");
                } else {
                    c.add("return true;");
                }
            }

            c.dedent();
            c.add("}");

            // Warn about channels that are used, but have no senders or no
            // receivers.
            if (isComm) {
                // Get automata.
                List<Automaton> sendAuts = ctxt.getSendAuts(event);
                List<Automaton> recvAuts = ctxt.getRecvAuts(event);
                List<Automaton> syncAuts = ctxt.getSyncAuts(event);

                // Check if event is used.
                int cnt = 0;
                cnt += sendAuts.size();
                cnt += recvAuts.size();
                cnt += syncAuts.size();

                // Warn if applicable.
                if (cnt > 0 && sendAuts.isEmpty()) {
                    warn("No senders found for channel \"%s\".", absName);
                }
                if (cnt > 0 && recvAuts.isEmpty()) {
                    warn("No receivers found for channel \"%s\".", absName);
                }
            }

            // Add 'allowedByInvs' method.
            c.add();
            c.add("@Override");
            c.add("public boolean allowedByInvs(State state) {");
            c.indent();

            genAllowedByInvsComp(spec, event, c, ctxt);
            for (Automaton aut: ctxt.getAutomata()) {
                genAllowedByInvsAutLocs(aut, event, c, ctxt);
            }
            c.add("// Event allowed by invariants.");
            c.add("return true;");

            c.dedent();
            c.add("}");
        }
    }

    /**
     * Generates 'fillTauData' calls for the 'tau' event, for all automata.
     *
     * @param c The code box in which to generate the code.
     * @param ctxt The compiler context to use.
     */
    private static void genFillCallsTau(CodeBox c, CifCompilerContext ctxt) {
        // We could generate calls only for the automata that actually can do
        // 'tau' events, but that means we have to walk over all edges, which
        // is expensive. So, we generate calls for all automata, regardless of
        // whether they can do a 'tau'.
        List<Automaton> auts = ctxt.getAutomata();
        for (Automaton aut: auts) {
            c.add("%s.fillTauData(state);", ctxt.getAutClassName(aut));
        }
    }

    /**
     * Generates 'fillSendData_*' calls for the given event, for all automata that have the event in their send
     * alphabet.
     *
     * @param event The event. Must be a channel.
     * @param c The code box in which to generate the code.
     * @param ctxt The compiler context to use.
     */
    private static void genFillCallsSend(Event event, CodeBox c, CifCompilerContext ctxt) {
        List<Automaton> auts = ctxt.getSendAuts(event);
        for (Automaton aut: auts) {
            c.add("%s.fillSendData_%s(state);", ctxt.getAutClassName(aut), ctxt.getEventFieldName(event));
        }
    }

    /**
     * Generates 'fillRecvData_*' calls for the given event, for all automata that have the event in their receive
     * alphabet.
     *
     * @param event The event. Must be a channel.
     * @param c The code box in which to generate the code.
     * @param ctxt The compiler context to use.
     */
    private static void genFillCallsRecv(Event event, CodeBox c, CifCompilerContext ctxt) {
        List<Automaton> auts = ctxt.getRecvAuts(event);
        for (Automaton aut: auts) {
            c.add("%s.fillRecvData_%s(state);", ctxt.getAutClassName(aut), ctxt.getEventFieldName(event));
        }
    }

    /**
     * Generates 'fillSyncData_*' calls for the given event, for all automata that have the event in their alphabet.
     *
     * @param event The event. Must not be the 'tau' event.
     * @param c The code box in which to generate the code.
     * @param ctxt The compiler context to use.
     */
    private static void genFillCallsSync(Event event, CodeBox c, CifCompilerContext ctxt) {
        List<Automaton> auts = ctxt.getSyncAuts(event);
        for (Automaton aut: auts) {
            c.add();
            c.add("// Check automaton \"%s\".", getAbsName(aut));
            c.add("proceed = %s.fillSyncData_%s(state);", ctxt.getAutClassName(aut), ctxt.getEventFieldName(event));
            c.add("if (!proceed) return false;");
        }
    }

    /**
     * Generates state/event exclusion invariant code for the invariants of the component (recursively). This does not
     * include the invariants of the locations.
     *
     * @param comp The component.
     * @param event The event.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void genAllowedByInvsComp(ComplexComponent comp, Event event, CodeBox c, CifCompilerContext ctxt) {
        // Generate for component.
        List<Invariant> stateEvtExclInvs = list();
        for (Invariant inv: comp.getInvariants()) {
            if (inv.getInvKind() == InvKind.STATE) {
                continue;
            }
            Event invEvent = ((EventExpression)inv.getEvent()).getEvent();
            if (invEvent != event) {
                continue;
            }
            stateEvtExclInvs.add(inv);
        }

        String absName = getAbsName(comp);
        if (!stateEvtExclInvs.isEmpty()) {
            c.add("// Invariants for \"%s\".", absName);
        }

        String compTxt = CifTextUtils.getComponentText2(comp);

        for (Invariant inv: stateEvtExclInvs) {
            Expression pred = inv.getPredicate();

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Actual invariant predicate evaluation.
            String predCode = gencodeExpr(pred, ctxt, "state");
            InvKind invKind = inv.getInvKind();
            switch (invKind) {
                case EVENT_DISABLES:
                    c.add("if (%s) return false;", predCode);
                    break;

                case EVENT_NEEDS:
                    c.add("if (!(%s)) return false;", predCode);
                    break;

                default:
                    String msg = "Unknown state/evt excl inv: " + invKind;
                    throw new RuntimeException(msg);
            }

            // End of 'try'.
            String invTxt = invToStr(inv, false);
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of invariant \\\"%s\\\" of %s failed.\", e, state);",
                    escapeJava(invTxt), escapeJava(compTxt));
            c.dedent();
            c.add("}");
            c.add();

            // Warn about requirement invariants.
            if (inv.getSupKind() == SupKind.REQUIREMENT) {
                warn("Invariant \"%s\" of %s is a requirement, but will be simulated as a plant.", invTxt, compTxt);
            }
        }

        // Generate recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                genAllowedByInvsComp((ComplexComponent)child, event, c, ctxt);
            }
        }
    }

    /**
     * Generates state/event exclusion invariant code for the invariants of the locations of the given automaton.
     *
     * @param aut The automaton.
     * @param event The event.
     * @param c The code box to which to add the code.
     * @param ctxt The compiler context to use.
     */
    private static void genAllowedByInvsAutLocs(Automaton aut, Event event, CodeBox c, CifCompilerContext ctxt) {
        c.add("// Invariants for current location.");
        c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
        c.indent();
        List<Location> locs = aut.getLocations();
        for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
            // Get location.
            Location loc = locs.get(locIdx);

            // If no invariants, then no 'case'.
            List<Invariant> locEvtExclInvs = list();
            for (Invariant inv: loc.getInvariants()) {
                if (inv.getInvKind() == InvKind.STATE) {
                    continue;
                }
                Event invEvent = ((EventExpression)inv.getEvent()).getEvent();
                if (invEvent != event) {
                    continue;
                }
                locEvtExclInvs.add(inv);
            }
            if (locEvtExclInvs.isEmpty()) {
                continue;
            }

            // Add 'case'.
            c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
            c.indent();

            String locTxt = CifTextUtils.getLocationText2(loc);

            for (Invariant inv: locEvtExclInvs) {
                Expression pred = inv.getPredicate();

                // Start of 'try'.
                c.add("try {");
                c.indent();

                // Actual invariant predicate evaluation.
                String predCode = gencodeExpr(pred, ctxt, "state");
                InvKind invKind = inv.getInvKind();
                switch (invKind) {
                    case EVENT_DISABLES:
                        c.add("if (%s) return false;", predCode);
                        break;

                    case EVENT_NEEDS:
                        c.add("if (!(%s)) return false;", predCode);
                        break;

                    default:
                        String msg = "Unknown state/evt excl inv: " + invKind;
                        throw new RuntimeException(msg);
                }

                // End of 'try'.
                String invTxt = invToStr(inv, false);
                c.dedent();
                c.add("} catch (CifSimulatorException e) {");
                c.indent();
                c.add("throw new CifSimulatorException(\"Evaluation of invariant \\\"%s\\\" of %s failed.\", e, "
                        + "state);", escapeJava(invTxt), escapeJava(locTxt));
                c.dedent();
                c.add("}");

                // Warn about requirement invariants.
                if (inv.getSupKind() == SupKind.REQUIREMENT) {
                    warn("Invariant \"%s\" of %s is a requirement, but will be simulated as a plant.", invTxt, locTxt);
                }
            }
            c.add("break;");
            c.dedent();
        }
        c.dedent();
        c.add("}");
        c.add();
    }

    /**
     * Collect the events declared in the given component (recursively).
     *
     * @param comp The component.
     * @param events The events collected so far, as pairs of absolute names of the events and the events themselves. Is
     *     modified in-place.
     */
    public static void collectEvents(ComplexComponent comp, List<Pair<String, Event>> events) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Event) {
                events.add(pair(getAbsName(decl, false), (Event)decl));
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectEvents((ComplexComponent)child, events);
            }
        }
    }
}
