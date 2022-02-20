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

package org.eclipse.escet.cif.simulator.runtime.model;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.escet.cif.simulator.compiler.AutomatonCodeGenerator;
import org.eclipse.escet.cif.simulator.compiler.CifCompilerContext;

/**
 * Runtime automaton representation, for {@link AutomatonCodeGenerator#isSimpleAut simple} automata.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class RuntimeSimpleAutomaton<S extends RuntimeState> extends RuntimeAutomaton<S> {
    /** The names of the locations of the automaton. {@code "*"} is used for nameless locations. */
    public final String[] locNames;

    /**
     * Edge data of the automaton. Per location, contains information about the outgoing edges. For each outgoing edge,
     * contains the event index and the target location index.
     */
    public final int[][][] edgeData;

    /** Monitor event data. For each event, indicates whether the automaton monitors that event. */
    public final boolean[] monitorData;

    /** The singleton instance of {@link RuntimeMonitorEdge} to use. */
    public final RuntimeSyncEdge<S> monitorEdge;

    /**
     * Constructor for the {@link RuntimeSimpleAutomaton} class.
     *
     * @param monitorData The monitor event data. For each event, indicates whether the automaton monitors that event.
     * @param monitorEdge The singleton instance of {@link RuntimeMonitorEdge} to use.
     * @param className The absolute class name of the derived class. This name is used to load the edge data resource.
     * @param classLoader The class loader that was used to load the derived class.
     */
    public RuntimeSimpleAutomaton(boolean[] monitorData, RuntimeSyncEdge<S> monitorEdge, String className,
            ClassLoader classLoader)
    {
        this.locNames = loadLocNames(className, classLoader);
        this.edgeData = loadEdgeData(className, classLoader);
        this.monitorData = monitorData;
        this.monitorEdge = monitorEdge;
    }

    @Override
    public int getLocCount() {
        return locNames.length;
    }

    @Override
    public String getLocName(int idx) {
        return locNames[idx];
    }

    /**
     * Loads the names of the locations of an automaton from a location names resource file.
     *
     * @param className The absolute class name of the derived class.
     * @param classLoader The class loader that was used to load the derived class.
     * @return The location names.
     */
    private static String[] loadLocNames(String className, ClassLoader classLoader) {
        // Get resource stream.
        String resName = CifCompilerContext.getLocNamesResourcePath(className);
        InputStream stream = classLoader.getResourceAsStream(resName);
        if (stream == null) {
            String msg = "Failed to load loc names resource: " + resName;
            throw new RuntimeException(msg);
        }

        // Read data.
        try {
            // Buffer the input stream, and allow data reading.
            stream = new BufferedInputStream(stream);
            DataInputStream data = new DataInputStream(stream);

            // Deserialize data.
            String[] rslt;
            int len = data.readInt();
            rslt = new String[len];
            for (int i = 0; i < len; i++) {
                int len2 = data.readInt();
                StringBuilder b = new StringBuilder(len2);
                for (int j = 0; j < len2; j++) {
                    char c = data.readChar();
                    b.append(c);
                }
                rslt[i] = b.toString();
            }

            // Return the loaded data.
            return rslt;
        } catch (IOException e) {
            String msg = "Failed to read loc names resource: " + resName;
            throw new RuntimeException(msg, e);
        } finally {
            // Always close the resource stream.
            try {
                stream.close();
            } catch (IOException e) {
                String msg = "Failed to close loc names resource: " + resName;
                throw new RuntimeException(msg, e);
            }
        }
    }

    /**
     * Loads the edge data of an automaton from an edge data resource file.
     *
     * @param className The absolute class name of the derived class.
     * @param classLoader The class loader that was used to load the derived class.
     * @return The edge data.
     */
    private static int[][][] loadEdgeData(String className, ClassLoader classLoader) {
        // Get resource stream.
        String resName = CifCompilerContext.getEdgeDataResourcePath(className);
        InputStream stream = classLoader.getResourceAsStream(resName);
        if (stream == null) {
            String msg = "Failed to load edge data resource: " + resName;
            throw new RuntimeException(msg);
        }

        // Read data.
        try {
            // Buffer the input stream, and allow data reading.
            stream = new BufferedInputStream(stream);
            DataInputStream data = new DataInputStream(stream);

            // Deserialize data.
            int[][][] rslt;
            int len1 = data.readInt();
            rslt = new int[len1][][];
            for (int i = 0; i < len1; i++) {
                int len2 = data.readInt();
                int[][] rslt2 = new int[len2][];
                rslt[i] = rslt2;
                for (int j = 0; j < len2; j++) {
                    int[] rslt3 = new int[2];
                    rslt2[j] = rslt3;
                    rslt3[0] = data.readInt();
                    rslt3[1] = data.readInt();
                }
            }

            // Return the loaded data.
            return rslt;
        } catch (IOException e) {
            String msg = "Failed to read edge data resource: " + resName;
            throw new RuntimeException(msg, e);
        } finally {
            // Always close the resource stream.
            try {
                stream.close();
            } catch (IOException e) {
                String msg = "Failed to close edge data resource: " + resName;
                throw new RuntimeException(msg, e);
            }
        }
    }

    /**
     * Fills the edge data list for a given event and the current location of a given automaton.
     *
     * @param <S> The type of state objects to use.
     * @param aut The automaton.
     * @param eventIdx The 0-based index of the event.
     * @param edges The list of edges for the given event. Is filled in-place.
     * @param locIdx The 0-based index of the source location of the automaton.
     */
    protected static <S extends RuntimeState> void fillSyncData(RuntimeSimpleAutomaton<S> aut, int eventIdx,
            List<RuntimeEdge<S>> edges, int locIdx)
    {
        // Process outgoing edges of the location.
        int[][] locEdges = aut.edgeData[locIdx];
        for (int[] locEdge: locEdges) {
            if (locEdge[0] != eventIdx) {
                continue;
            }
            int targetIdx = locEdge[1];
            if (locIdx == targetIdx) {
                edges.add(aut.monitorEdge);
            } else {
                edges.add(new RuntimeSimpleEdge<>(aut, locEdge[1]));
            }
        }

        // Handle monitor events.
        if (edges.isEmpty() && aut.monitorData[eventIdx]) {
            edges.add(aut.monitorEdge);
        }
    }

    /**
     * Sets the value of the location pointer variable of this automaton to the given value, after taking an edge.
     *
     * @param target The target state (of the transition), for which to update the location pointer value.
     * @param value The new value of the location pointer variable.
     */
    protected abstract void updateLocPointerValue(S target, int value);
}
