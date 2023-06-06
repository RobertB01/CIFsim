//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.sequencing;

import static org.eclipse.escet.common.java.Strings.trimLeft;
import static org.eclipse.escet.common.java.Strings.trimRight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.escet.common.dsm.sequencing.graph.Graph;
import org.eclipse.escet.common.dsm.sequencing.graph.GraphCreator;

/** Class for sequencing a graph or DSM. */
public class Sequencer {
    /** Constructor of the {@link Sequencer} class. */
    private Sequencer() {
        // Static class.
    }

    /** Pattern for matching a {@code (vertex-name, vertex-name)} pair. */
    private static final Pattern PAIR_PATTERN = Pattern.compile("[(]([^,)]+),([^)]+)[)]");

    /**
     * Load a file containing {@code (<name1>, <name2>)} directed edges of a graph.
     *
     * @param fpath The file path to load.
     * @return The loaded graph.
     */
    public static Graph loadVertexPairs(Path fpath) {
        Graph g = new Graph();
        GraphCreator creator = g.getGraphCreator();
        creator.setupCreation();

        try {
            Files.lines(fpath).forEachOrdered(line -> { addVertexPairs(creator, line); });
        } catch (IOException ex) {
            throw new RuntimeException("Could not read file \"" + fpath + "\".", ex);
        }

        creator.finishCreation();
        return g;
    }

    /**
     * Create a graph from a line with a sequence of directed edges, each of the form {@code (<name1>, <name2>)}.
     *
     * @param pairs Line of text with the edge pairs.
     * @return The loaded graph.
     */
    public static Graph loadVertexPairs(String pairs) {
        Graph g = new Graph();
        GraphCreator creator = g.getGraphCreator();
        creator.setupCreation();
        addVertexPairs(creator, pairs);
        creator.finishCreation();
        return g;
    }

    /**
     * Parse a line of text with a sequence of directed edges, each of the form {@code (<name1>, <name2>)}, and add the
     * edges to the graph.
     *
     * @param creator Storage for the found edges.
     * @param line Line of text to parse.
     */
    private static void addVertexPairs(GraphCreator creator, String line) {
        Matcher m = PAIR_PATTERN.matcher(line);
        while (m.find()) {
            String sourceName = trimLeft(trimRight(m.group(1)));
            String targetName = trimLeft(trimRight(m.group(2)));
            creator.addEdge(sourceName, targetName);
        }
    }
}
