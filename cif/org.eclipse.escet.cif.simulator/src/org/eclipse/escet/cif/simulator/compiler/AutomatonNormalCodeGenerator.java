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
import static org.eclipse.escet.cif.common.CifTextUtils.exprsToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.simulator.compiler.AssignmentCodeGenerator.gencodeAssignment;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.RCVD_VALUE_VAR_NAME;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodePreds;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.truncate;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.common.box.CodeBox;

/**
 * Automaton code generator. Supports all features of automata. Does not support automata with large numbers of
 * locations and/or edges.
 *
 * @see AutomatonCodeGenerator
 */
public class AutomatonNormalCodeGenerator {
    /** Constructor for the {@link AutomatonNormalCodeGenerator} class. */
    private AutomatonNormalCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the given automaton.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeAutomaton(Automaton aut, CifCompilerContext ctxt) {
        // Add new code file.
        String className = ctxt.getAutClassName(aut);
        JavaCodeFile file = ctxt.addCodeFile(className);

        // Add header.
        String absName = getAbsName(aut);
        CodeBox h = file.header;
        h.add("/** Automaton \"%s\". */", absName);
        h.add("public final class %s extends RuntimeAutomaton<State> {", className);

        // Add body.
        CodeBox c = file.body;

        // Add 'getName' method.
        c.add();
        c.add("@Override");
        c.add("public String getName() {");
        c.indent();
        c.add("return \"%s\";", absName);
        c.dedent();
        c.add("}");

        // Add 'getLocCount' method.
        List<Location> locs = aut.getLocations();
        c.add();
        c.add("@Override");
        c.add("public int getLocCount() {");
        c.indent();
        c.add("return %d;", locs.size());
        c.dedent();
        c.add("}");

        // Add 'getCurLocName' method.
        c.add();
        c.add("@Override");
        c.add("public String getCurLocName(State state) {");
        c.indent();
        c.add("return getLocName(state.%s.%s);", ctxt.getAutSubStateFieldName(aut),
                ctxt.getLocationPointerFieldName(aut));
        c.dedent();
        c.add("}");

        // Add 'getLocName' method.
        c.add();
        c.add("@Override");
        c.add("public String getLocName(int locIdx) {");
        c.indent();

        c.add("switch (locIdx) {");
        c.indent();

        for (int i = 0; i < locs.size(); i++) {
            Location loc = locs.get(i);
            String name = CifLocationUtils.getName(loc);
            c.add("case %d: return \"%s\";", i, name);
        }
        c.add("default: throw new RuntimeException(\"Invalid loc idx: \" + locIdx);");

        c.dedent();
        c.add("}");

        c.dedent();
        c.add("}");

        // Add 'fill*' methods.
        gencodeFillSyncData(aut, ctxt, c);
        gencodeFillTauData(aut, ctxt, c);
        gencodeFillSendData(aut, ctxt, c);
        gencodeFillRecvData(aut, ctxt, c);

        // Add inner classes for the edges, with fields for singletons.
        // Process all locations.
        for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
            Location loc = locs.get(locIdx);
            String locTxt = CifTextUtils.getLocationText2(loc);

            // Process all edges.
            List<Edge> edges = loc.getEdges();
            for (int edgeIdx = 0; edgeIdx < edges.size(); edgeIdx++) {
                Edge edge = edges.get(edgeIdx);

                boolean syncSeen = false;
                boolean recvSeen = false;

                // Handle implicit 'tau'.
                List<EdgeEvent> edgeEvents = edge.getEvents();
                if (edgeEvents.isEmpty()) {
                    gencodeEdge(aut, loc, locIdx, locTxt, edge, edgeIdx, null, 0, ctxt, c);
                    continue;
                }

                // Process all representative edge events.
                for (int eeIdx = 0; eeIdx < edgeEvents.size(); eeIdx++) {
                    EdgeEvent edgeEvent = edgeEvents.get(eeIdx);

                    // Handle representatives.
                    if (edgeEvent instanceof EdgeSend) {
                        // Each 'send' use is its own representative, as they
                        // can each send different values.
                    } else if (edgeEvent instanceof EdgeReceive) {
                        if (recvSeen) {
                            continue;
                        }
                        recvSeen = true;
                    } else {
                        if (syncSeen) {
                            continue;
                        }
                        syncSeen = true;
                    }

                    // Generate code for the representative.
                    gencodeEdge(aut, loc, locIdx, locTxt, edge, edgeIdx, edgeEvent, eeIdx, ctxt, c);
                }
            }
        }
    }

    /**
     * Generate Java code for the 'fillSyncData_*' methods of the given automaton, for the events in the alphabet of the
     * automaton.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to write the code.
     */
    private static void gencodeFillSyncData(Automaton aut, CifCompilerContext ctxt, CodeBox c) {
        Set<Event> syncAlphabet = ctxt.getAlphabet(aut);
        List<Event> events = ctxt.getEvents();
        for (int eventIdx = 0; eventIdx < events.size(); eventIdx++) {
            // If not in alphabet, skip it.
            Event event = events.get(eventIdx);
            if (!syncAlphabet.contains(event)) {
                continue;
            }

            // Get index of the automaton as part of the synchronizing automata
            // for the event.
            List<Automaton> auts = ctxt.getSyncAuts(event);
            int autIdx = auts.indexOf(aut);

            // Add method header.
            c.add();
            c.add("public static boolean fillSyncData_%s(State state) {", ctxt.getEventFieldName(event));
            c.indent();

            // Add init.
            c.add("List<RuntimeEdge<State>> rslt = SPEC.syncData.get(%d).get(%d);", eventIdx, autIdx);
            c.add("rslt.clear();");

            // Add switch over location pointer, with cases for the relevant
            // locations (the ones with outgoing edges for the event).
            c.add();
            c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
            c.indent();

            // Process all locations.
            List<Location> locs = aut.getLocations();
            for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
                Location loc = locs.get(locIdx);

                boolean caseAdded = false;

                // Process all edges.
                List<Edge> edges = loc.getEdges();
                for (int edgeIdx = 0; edgeIdx < edges.size(); edgeIdx++) {
                    Edge edge = edges.get(edgeIdx);

                    int reprIdx = -1;

                    // Process all edge events.
                    List<EdgeEvent> edgeEvents = edge.getEvents();
                    for (int eeIdx = 0; eeIdx < edgeEvents.size(); eeIdx++) {
                        EdgeEvent edgeEvent = edgeEvents.get(eeIdx);

                        // Skip send/receive.
                        if (edgeEvent instanceof EdgeSend) {
                            continue;
                        }
                        if (edgeEvent instanceof EdgeReceive) {
                            continue;
                        }

                        // Update representative.
                        if (reprIdx == -1) {
                            reprIdx = eeIdx;
                        }

                        // Skip 'tau'.
                        Expression eventRef = edgeEvent.getEvent();
                        if (eventRef instanceof TauExpression) {
                            continue;
                        }

                        // Skip other events.
                        Event e = ((EventExpression)eventRef).getEvent();
                        if (e != event) {
                            continue;
                        }

                        // Add 'case', if not yet present.
                        if (!caseAdded) {
                            caseAdded = true;
                            c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
                            c.indent();
                        }

                        // Add edge.
                        String postfix = fmt("%d_%d_%d", locIdx, edgeIdx, reprIdx);
                        c.add("if (sync%s.evalGuards(state)) rslt.add(sync%s);", postfix, postfix);
                    }
                }

                // Close 'case', if added.
                if (caseAdded) {
                    c.add("break;");
                    c.dedent();
                }
            }

            // Close the 'switch'.
            c.dedent();
            c.add("}");

            // Handle monitors.
            Set<Event> monitors = ctxt.getMonitors(aut);
            if (monitors.contains(event)) {
                c.add();
                c.add("// Monitor.");
                c.add("if (rslt.isEmpty()) rslt.add(MONITOR_EDGE);");
            }

            // Close the method.
            c.add();
            c.add("return !rslt.isEmpty();");
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Generate Java code for the 'fillTauData' method of the given automaton.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to write the code.
     */
    private static void gencodeFillTauData(Automaton aut, CifCompilerContext ctxt, CodeBox c) {
        // Add method header.
        c.add();
        c.add("public static void fillTauData(State state) {");
        c.indent();

        // Add init.
        c.add("List<RuntimeEdge<State>> edgeData;");

        // Add switch over location pointer, with cases for the relevant
        // locations (the ones with outgoing edges for the event).
        c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
        c.indent();

        // Process all locations.
        List<Location> locs = aut.getLocations();
        for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
            Location loc = locs.get(locIdx);

            boolean caseAdded = false;

            // Process all edges.
            List<Edge> edges = loc.getEdges();
            for (int edgeIdx = 0; edgeIdx < edges.size(); edgeIdx++) {
                Edge edge = edges.get(edgeIdx);

                int reprIdx = -1;

                // Handle implicit 'tau'.
                List<EdgeEvent> edgeEvents = edge.getEvents();
                if (edgeEvents.isEmpty()) {
                    edgeEvents = list((EdgeEvent)null);
                }

                // Process all edge events.
                for (int eeIdx = 0; eeIdx < edgeEvents.size(); eeIdx++) {
                    EdgeEvent edgeEvent = edgeEvents.get(eeIdx);

                    // Skip send/receive.
                    if (edgeEvent instanceof EdgeSend) {
                        continue;
                    }
                    if (edgeEvent instanceof EdgeReceive) {
                        continue;
                    }

                    // Update representative.
                    if (reprIdx == -1) {
                        reprIdx = eeIdx;
                    }

                    // Skip synchronizing events.
                    if (edgeEvent != null) {
                        Expression eventRef = edgeEvent.getEvent();
                        if (eventRef instanceof EventExpression) {
                            continue;
                        }
                    }

                    // Found 'tau' edge.
                    ctxt.hasTauEdge = true;

                    // Add 'case', if not yet present.
                    if (!caseAdded) {
                        caseAdded = true;
                        c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
                        c.indent();
                    }

                    // Add edge.
                    String postfix = fmt("%d_%d_%d", locIdx, edgeIdx, reprIdx);
                    c.add("if (sync%s.evalGuards(state)) {", postfix);
                    c.indent();
                    c.add("edgeData = list((RuntimeEdge<State>)sync%s);", postfix);
                    c.add("SPEC.tauData.add(edgeData);");
                    c.dedent();
                    c.add("}");
                }
            }

            // Close 'case', if added.
            if (caseAdded) {
                c.add("break;");
                c.dedent();
            }
        }

        // Close the 'switch'.
        c.dedent();
        c.add("}");

        // Close the method.
        c.dedent();
        c.add("}");
    }

    /**
     * Generate Java code for the 'fillSendData_*' methods of the given automaton, for the events over which the
     * automaton sends.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to write the code.
     */
    private static void gencodeFillSendData(Automaton aut, CifCompilerContext ctxt, CodeBox c) {
        Set<Event> sendAlphabet = ctxt.getSendAlphabet(aut);
        List<Event> events = ctxt.getEvents();
        for (int eventIdx = 0; eventIdx < events.size(); eventIdx++) {
            // If not in send alphabet, skip it.
            Event event = events.get(eventIdx);
            if (!sendAlphabet.contains(event)) {
                continue;
            }

            // Add method header.
            c.add();
            c.add("public static void fillSendData_%s(State state) {", ctxt.getEventFieldName(event));
            c.indent();

            // Add init.
            c.add("List<RuntimeEdge<State>> rslt = SPEC.sendData.get(%d);", eventIdx);

            // Add switch over location pointer, with cases for the relevant
            // locations (the ones with outgoing sends for the event).
            c.add();
            c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
            c.indent();

            // Process all locations.
            List<Location> locs = aut.getLocations();
            for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
                Location loc = locs.get(locIdx);

                boolean caseAdded = false;

                // Process all edges.
                List<Edge> edges = loc.getEdges();
                for (int edgeIdx = 0; edgeIdx < edges.size(); edgeIdx++) {
                    Edge edge = edges.get(edgeIdx);

                    // Process all edge events.
                    List<EdgeEvent> edgeEvents = edge.getEvents();
                    for (int eeIdx = 0; eeIdx < edgeEvents.size(); eeIdx++) {
                        EdgeEvent edgeEvent = edgeEvents.get(eeIdx);

                        // Skip sync/receive.
                        if (!(edgeEvent instanceof EdgeSend)) {
                            continue;
                        }

                        // Skip other events.
                        Expression eventRef = edgeEvent.getEvent();
                        Event e = ((EventExpression)eventRef).getEvent();
                        if (e != event) {
                            continue;
                        }

                        // Add 'case', if not yet present.
                        if (!caseAdded) {
                            caseAdded = true;
                            c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
                            c.indent();
                        }

                        // Add edge.
                        String postfix = fmt("%d_%d_%d", locIdx, edgeIdx, eeIdx);
                        c.add("if (send%s.evalGuards(state)) rslt.add(send%s);", postfix, postfix);
                    }
                }

                // Close 'case', if added.
                if (caseAdded) {
                    c.add("break;");
                    c.dedent();
                }
            }

            // Close the 'switch'.
            c.dedent();
            c.add("}");

            // Close the method.
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Generate Java code for the 'fillRecvData_*' methods of the given automaton, for the events over which the
     * automaton receives.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to write the code.
     */
    private static void gencodeFillRecvData(Automaton aut, CifCompilerContext ctxt, CodeBox c) {
        Set<Event> rcvAlphabet = ctxt.getReceiveAlphabet(aut);
        List<Event> events = ctxt.getEvents();
        for (int eventIdx = 0; eventIdx < events.size(); eventIdx++) {
            // If not in receive alphabet, skip it.
            Event event = events.get(eventIdx);
            if (!rcvAlphabet.contains(event)) {
                continue;
            }

            // Add method header.
            c.add();
            c.add("public static void fillRecvData_%s(State state) {", ctxt.getEventFieldName(event));
            c.indent();

            // Add init.
            c.add("List<RuntimeEdge<State>> rslt = SPEC.recvData.get(%d);", eventIdx);

            // Add switch over location pointer, with cases for the relevant
            // locations (the ones with outgoing sends for the event).
            c.add();
            c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
            c.indent();

            // Process all locations.
            List<Location> locs = aut.getLocations();
            for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
                Location loc = locs.get(locIdx);

                boolean caseAdded = false;

                // Process all edges.
                List<Edge> edges = loc.getEdges();
                for (int edgeIdx = 0; edgeIdx < edges.size(); edgeIdx++) {
                    Edge edge = edges.get(edgeIdx);

                    int reprIdx = -1;

                    // Process all edge events.
                    List<EdgeEvent> edgeEvents = edge.getEvents();
                    for (int eeIdx = 0; eeIdx < edgeEvents.size(); eeIdx++) {
                        EdgeEvent edgeEvent = edgeEvents.get(eeIdx);

                        // Skip sync/send.
                        if (!(edgeEvent instanceof EdgeReceive)) {
                            continue;
                        }

                        // Update representative.
                        if (reprIdx == -1) {
                            reprIdx = eeIdx;
                        }

                        // Skip other events.
                        Expression eventRef = edgeEvent.getEvent();
                        Event e = ((EventExpression)eventRef).getEvent();
                        if (e != event) {
                            continue;
                        }

                        // Add 'case', if not yet present.
                        if (!caseAdded) {
                            caseAdded = true;
                            c.add("case %s:", ctxt.getLocationValueText(loc, locIdx));
                            c.indent();
                        }

                        // Add edge.
                        String postfix = fmt("%d_%d_%d", locIdx, edgeIdx, reprIdx);
                        c.add("if (receive%s.evalGuards(state)) rslt.add(receive%s);", postfix, postfix);
                    }
                }

                // Close 'case', if added.
                if (caseAdded) {
                    c.add("break;");
                    c.dedent();
                }
            }

            // Close the 'switch'.
            c.dedent();
            c.add("}");

            // Close the method.
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Generate Java code for an edge class for a given representative edge event of an edge, and for a field with a
     * singleton instance of that generated class.
     *
     * @param aut The automaton.
     * @param loc The source location of the edge.
     * @param locIdx The 0-based index of the location in the locations of the automaton.
     * @param locTxt A text to use to refer to the location in error messages.
     * @param edge The edge.
     * @param edgeIdx The 0-based index of the edge in the outgoing edges of the location.
     * @param edgeEvent The representative edge event, or {@code null} for implicit 'tau' (no edge events on the edge).
     *     The representative edge event is the first edge event on the edge with a particular use. For 'send' use, each
     *     edge event is its own representative, as they may each send different values.
     * @param eeReprIdx The 0-based index of the representative edge event in the edge events of the edge.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to write the code.
     */
    private static void gencodeEdge(Automaton aut, Location loc, int locIdx, String locTxt, Edge edge, int edgeIdx,
            EdgeEvent edgeEvent, int eeReprIdx, CifCompilerContext ctxt, CodeBox c)
    {
        // Check use.
        boolean isSend = edgeEvent instanceof EdgeSend;
        boolean isRecv = edgeEvent instanceof EdgeReceive;
        String useName = isSend ? "Send" : (isRecv ? "Receive" : "Sync");
        String postfix = fmt("%d_%d_%d", locIdx, edgeIdx, eeReprIdx);

        // Generate field with singleton instance.
        c.add();
        c.add("private static final %s%s %s%s = new %s%s();", useName, postfix, useName.toLowerCase(Locale.US), postfix,
                useName, postfix);

        // Add header.
        c.add();
        c.add("private static final class %s%s extends Runtime%sEdge<State> {", useName, postfix, useName);
        c.indent();

        // Add 'eval' method.
        c.add("@Override");
        c.add("public boolean evalGuards(State state) {");
        c.indent();
        List<ExprCodeGeneratorResult> guardResults = list();
        if (edge.getGuards().isEmpty()) {
            c.add("return true;");
        } else {
            c.add("boolean b;");
            for (Expression guard: edge.getGuards()) {
                // Start of 'try'.
                c.add("try {");
                c.indent();

                // Actual evaluation of guard.
                ExprCodeGeneratorResult result = gencodeExpr(guard, ctxt, "state");
                c.add("b = %s;", result);
                guardResults.add(result);

                // End of 'try'.
                c.dedent();
                c.add("} catch (CifSimulatorException e) {");
                c.indent();
                c.add("throw new CifSimulatorException(\"Evaluation of guard \\\"%s\\\" of an edge of %s failed.\", "
                        + "e, state);", escapeJava(truncate(exprToStr(guard), 1000)), escapeJava(locTxt));

                c.dedent();
                c.add("}");

                // If guard is false, then entire guard is false.
                c.add("if (!b) return false;");
            }
            c.add("return true;");
        }
        c.dedent();
        c.add("}");

        // Add potential extra guard expression evaluation methods.
        for (ExprCodeGeneratorResult guardResult: guardResults) {
            guardResult.addExtraMethods(c);
        }

        List<ExprCodeGeneratorResult> sendResults = list();
        // Add 'evalSendValue' method.
        if (isSend) {
            c.add();
            c.add("@Override");
            c.add("public Object evalSendValue(State state) {");
            c.indent();

            Expression sendValue = ((EdgeSend)edgeEvent).getValue();
            if (sendValue == null) {
                c.add("return null; // void");
            } else {
                c.add("try {");
                c.indent();
                ExprCodeGeneratorResult result = gencodeExpr(sendValue, ctxt, "state");
                c.add("return %s;", result);
                sendResults.add(result);
                c.dedent();
                c.add("} catch (CifSimulatorException e) {");
                c.indent();
                c.add("throw new CifSimulatorException(\"Evaluation of value \\\"%s\\\" to send of an edge of %s "
                        + "failed.\", e, state);", escapeJava(truncate(exprToStr(sendValue), 1000)),
                        escapeJava(locTxt));
                c.dedent();
                c.add("}");
            }

            c.dedent();
            c.add("}");
        }

        // Add potential extra send expression evaluation methods.
        for (ExprCodeGeneratorResult sendResult: sendResults) {
            sendResult.addExtraMethods(c);
        }

        // Add 'update' method.
        c.add();
        c.add("@Override");
        if (isRecv) {
            c.add("public void update(State source, State target, Object %s_) {", RCVD_VALUE_VAR_NAME);
        } else {
            c.add("public void update(State source, State target) {");
        }
        c.indent();

        boolean selfLoop = CifEdgeUtils.isSelfLoop(edge);
        boolean noUpdates = selfLoop && edge.getUpdates().isEmpty();
        List<ExprCodeGeneratorResult> updateResults = list();

        if (noUpdates) {
            c.add("// No updates.");
        } else {
            // Generate variable for received value.
            if (isRecv) {
                Event event = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                if (!(event.getType() instanceof VoidType)) {
                    c.add("%s %s = (%s)%s_;", gencodeType(event.getType(), ctxt), RCVD_VALUE_VAR_NAME,
                            gencodeType(event.getType(), ctxt), RCVD_VALUE_VAR_NAME);
                }
            }

            // Generate variable 'b', for evaluation of predicates.
            // Reused for guards of if/elif/etc.
            c.add("boolean b; // temp var for pred eval rslts");

            // Copy automaton sub-state.
            c.add("target.%s = source.%s.copy();", ctxt.getAutSubStateFieldName(aut),
                    ctxt.getAutSubStateFieldName(aut));

            // Update location pointer variable, if needed.
            if (!selfLoop) {
                c.add("target.%s.%s = %s;", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut),
                        ctxt.getLocationValueText(edge.getTarget()));
            }

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Apply actual updates.
            updateResults.addAll(gencodeUpdates(c, aut, ctxt, edge.getUpdates()));

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Execution of the updates of an edge of %s failed.\", e, source);",
                    escapeJava(locTxt));
            c.dedent();
            c.add("}");
        }

        c.dedent();
        c.add("}");

        // Add potential extra update expression evaluation methods.
        for (ExprCodeGeneratorResult updateResult: updateResults) {
            updateResult.addExtraMethods(c);
        }

        // Wrap up the class.
        c.dedent();
        c.add("}");
    }

    /**
     * Generate Java code for the given updates.
     *
     * @param c The code box to which to write the code.
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     * @param updates The updates.
     * @return The {@code ExprCodeGeneratorResult}s for the generated Java code.
     */
    private static List<ExprCodeGeneratorResult> gencodeUpdates(CodeBox c, Automaton aut, CifCompilerContext ctxt,
            List<Update> updates)
    {
        List<ExprCodeGeneratorResult> exprResults = list();
        for (Update update: updates) {
            if (update instanceof Assignment) {
                Assignment asgn = (Assignment)update;
                exprResults.addAll(gencodeAssignment(asgn.getAddressable(), asgn.getValue(), aut, c, ctxt, "source"));
            } else {
                exprResults.addAll(gencodeIfUpdate(c, aut, ctxt, (IfUpdate)update));
            }
        }
        return exprResults;
    }

    /**
     * Generate Java code for the given 'if' update.
     *
     * @param c The code box to which to write the code.
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     * @param update The 'if' update.
     * @return The {@code ExprCodeGeneratorResult}s for the generated Java code.
     */
    private static List<ExprCodeGeneratorResult> gencodeIfUpdate(CodeBox c, Automaton aut, CifCompilerContext ctxt,
            IfUpdate update)
    {
        List<ExprCodeGeneratorResult> exprResults = list();
        // Start of 'try'.
        c.add("try {");
        c.indent();

        // If guards.
        ExprCodeGeneratorResult updateResult = gencodePreds(update.getGuards(), ctxt, "source");
        c.add("b = %s;", updateResult);
        exprResults.add(updateResult);

        // End of 'try'.
        c.dedent();
        c.add("} catch (CifSimulatorException e) {");
        c.indent();
        c.add("throw new CifSimulatorException(\"Evaluation of \\\"if\\\" update guard(s) \\\"%s\\\" failed.\", e, "
                + "source);", escapeJava(truncate(exprsToStr(update.getGuards()), 1000)));
        c.dedent();
        c.add("}");

        // If updates.
        c.add("if (b) {");
        c.indent();
        exprResults.addAll(gencodeUpdates(c, aut, ctxt, update.getThens()));
        c.dedent();

        // Elifs.
        for (ElifUpdate elifUpd: update.getElifs()) {
            c.add("} else {");
            c.indent();

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Elif guards.
            ExprCodeGeneratorResult elifUpdResult = gencodePreds(elifUpd.getGuards(), ctxt, "source");
            c.add("b = %s;", elifUpdResult);
            exprResults.add(updateResult);

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of \\\"elif\\\" update guard(s) \\\"%s\\\" failed.\", "
                    + "e, source);", escapeJava(truncate(exprsToStr(elifUpd.getGuards()), 1000)));
            c.dedent();
            c.add("}");

            // Elif updates.
            c.add("if (b) {");
            c.indent();
            exprResults.addAll(gencodeUpdates(c, aut, ctxt, elifUpd.getThens()));
            c.dedent();
        }

        // Else.
        if (!update.getElses().isEmpty()) {
            c.add("} else {");
            c.indent();
            exprResults.addAll(gencodeUpdates(c, aut, ctxt, update.getElses()));
            c.dedent();
        }

        // Close elifs.
        for (int i = 0; i < update.getElifs().size(); i++) {
            c.add("}");
            c.dedent();
        }

        // Close if.
        c.add("}");

        return exprResults;
    }
}
