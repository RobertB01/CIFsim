//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.getEdgeDataResourcePath;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.getLocNamesResourcePath;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.stringToJava;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.common.box.CodeBox;

/**
 * Automaton code generator. Supports automata with a large number of locations and/or edges. Supports only simple
 * automata.
 *
 * @see AutomatonCodeGenerator
 * @see AutomatonCodeGenerator#isSimpleAut
 */
public class AutomatonSimpleCodeGenerator {
    /** Constructor for the {@link AutomatonSimpleCodeGenerator} class. */
    private AutomatonSimpleCodeGenerator() {
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
        h.add("public final class %s extends RuntimeSimpleAutomaton<State> {", className);

        // Add body.
        CodeBox c = file.body;

        // Add constructor.
        String absClassName = CifCompilerContext.getClassName(className);
        c.add("public %s() {", className);
        c.indent();
        c.add("super(getMonitorData(), Spec.MONITOR_EDGE, %s, SPEC.resourceClassLoader);", stringToJava(absClassName),
                className);
        c.dedent();
        c.add("}");

        // Add 'getName' method.
        c.add();
        c.add("@Override");
        c.add("public String getName() {");
        c.indent();
        c.add("return \"%s\";", absName);
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

        // Add 'getMonitorData()' method.
        Set<Event> monitors = ctxt.getMonitors(aut);
        List<Event> events = ctxt.getEvents();

        c.add();
        c.add("public static boolean[] getMonitorData() {");
        c.indent();
        List<String> monitorValues = listc(events.size());
        for (Event event: events) {
            boolean monitor = monitors.contains(event);
            monitorValues.add(String.valueOf(monitor));
        }
        c.add("return new boolean[] {%s};", String.join(", ", monitorValues));
        c.dedent();
        c.add("}");

        // Add 'fillSyncData_*' method for all events in the alphabet.
        Set<Event> alphabet = ctxt.getAlphabet(aut);
        for (int eventIdx = 0; eventIdx < events.size(); eventIdx++) {
            // If not in alphabet, skip it.
            Event event = events.get(eventIdx);
            if (!alphabet.contains(event)) {
                continue;
            }

            // Get index of the automaton as part of the synchronizing automata
            // for the event.
            List<Automaton> auts = ctxt.getSyncAuts(event);
            int autIdx = auts.indexOf(aut);

            // Add method.
            c.add();
            c.add("public static boolean fillSyncData_%s(State state) {", ctxt.getEventFieldName(event));
            c.indent();
            c.add("List<RuntimeEdge<State>> rslt = SPEC.syncData.get(%d).get(%d);", eventIdx, autIdx);
            c.add("rslt.clear();");
            c.add("RuntimeSimpleAutomaton<State> aut = (RuntimeSimpleAutomaton<State>)SPEC.automata.get(%d);",
                    ctxt.getAutomata().indexOf(aut));
            c.add("fillSyncData(aut, %d, rslt, state.%s.%s);", eventIdx, ctxt.getAutSubStateFieldName(aut),
                    ctxt.getLocationPointerFieldName(aut));
            c.add("return !rslt.isEmpty();");
            c.dedent();
            c.add("}");
        }

        // Add 'fillTauData' method.
        c.add();
        c.add("public static void fillTauData(State state) {");
        c.indent();
        c.add("// Simple automaton: no tau.");
        c.dedent();
        c.add("}");

        // Add 'updateLocPointerValue' method.
        c.add();
        c.add("@Override");
        c.add("protected void updateLocPointerValue(State target, int value) {");
        c.indent();
        c.add("target.%s = target.%s.copy();", ctxt.getAutSubStateFieldName(aut), ctxt.getAutSubStateFieldName(aut));
        c.add("target.%s.%s = value;", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
        c.dedent();
        c.add("}");

        // Generate location names resource.
        gencodeLocNamesResource(aut, ctxt);

        // Generate edge data resource.
        gencodeEdgeDataResource(aut, ctxt);
    }

    /**
     * Generates code for the location names resource for the given automaton.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeLocNamesResource(Automaton aut, CifCompilerContext ctxt) {
        String className = ctxt.getAutClassName(aut);
        String absClassName = CifCompilerContext.getClassName(className);
        String resPath = getLocNamesResourcePath(absClassName);
        OutputStream stream = ctxt.addResourceFile(resPath);
        DataOutputStream data = new DataOutputStream(stream);

        List<Location> locs = aut.getLocations();
        try {
            data.writeInt(locs.size());
            for (int i = 0; i < locs.size(); i++) {
                Location loc = locs.get(i);
                String name = CifLocationUtils.getName(loc);
                data.writeInt(name.length());
                data.writeChars(name);
            }
            data.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates code for the edge data resource for the given automaton.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeEdgeDataResource(Automaton aut, CifCompilerContext ctxt) {
        // Generate edge data.
        List<Location> locs = aut.getLocations();
        int[][][] edgesData = new int[locs.size()][][];
        for (int i = 0; i < locs.size(); i++) {
            Location loc = locs.get(i);
            List<Edge> edges = loc.getEdges();

            int edgeCount = 0;
            for (Edge edge: edges) {
                edgeCount += edge.getEvents().size();
            }

            int[][] locData = new int[edgeCount][2];
            edgesData[i] = locData;

            int edgeIdx = 0;
            for (Edge edge: edges) {
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    Event evt = ((EventExpression)edgeEvent.getEvent()).getEvent();
                    Location target = edge.getTarget();
                    int targetIdx = (target == null) ? i : locs.indexOf(target);

                    locData[edgeIdx][0] = ctxt.getEvents().indexOf(evt);
                    locData[edgeIdx][1] = targetIdx;

                    edgeIdx++;
                }
            }
        }

        // Write edge data.
        String className = ctxt.getAutClassName(aut);
        String absClassName = CifCompilerContext.getClassName(className);
        String resPath = getEdgeDataResourcePath(absClassName);
        OutputStream stream = ctxt.addResourceFile(resPath);
        DataOutputStream data = new DataOutputStream(stream);

        try {
            data.writeInt(edgesData.length);
            for (int i = 0; i < edgesData.length; i++) {
                int[][] locData = edgesData[i];
                data.writeInt(locData.length);
                for (int j = 0; j < locData.length; j++) {
                    int[] edgeData = locData[j];
                    data.writeInt(edgeData[0]);
                    data.writeInt(edgeData[1]);
                }
            }
            data.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
