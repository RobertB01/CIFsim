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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.escet.common.dsm.sequencing.graph.Graph;
import org.eclipse.escet.common.dsm.sequencing.graph.GraphCreator;

/** Class for sequencing a graph or DSM. */
public class Sequencer {
    /** Pattern for matching a {@code (vertex-name, vertex-name)} pair. */
    private static final Pattern PAIR_PATTERN = Pattern.compile("[(]([^,)]+),([^)]+)[)]");

    /** Graph being sequenced. */
    public final Graph g = new Graph();

    /**
     * Load a file containing {@code (<name1>, <name2>)} directed edges of a graph.
     *
     * @param fname Name of the file to load.
     */
    public void loadVertexPairs(Path fname) {
        GraphCreator creator = g.getGraphCreator();
        creator.setupCreation();

        try (BufferedReader reader = Files.newBufferedReader(fname)) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                addVertexPairs(creator, line);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not read file \"" + fname + "\".", ex);
        }
        creator.finishCreastion();
    }

    /**
     * Create a graph from a line {@code (<name1>, <name2>)} directed edges.
     *
     * @param pairs Line of text with the edge pairs.
     */
    public void loadVertexPairs(String pairs) {
        GraphCreator creator = g.getGraphCreator();
        creator.setupCreation();
        addVertexPairs(creator, pairs);
        creator.finishCreastion();
    }

    /**
     * Parse a line of text containing {@code (<name1>, <name2>)} directed edges and add them to the graph.
     *
     * @param creator Storage for the found edges.
     * @param line Line of text to parse.
     */
    private void addVertexPairs(GraphCreator creator, String line) {
        Matcher m = PAIR_PATTERN.matcher(line);
        while (m.find()) {
            String sourceName = trimLeft(trimRight(m.group(1)));
            String targetName = trimLeft(trimRight(m.group(2)));
            creator.addEdge(sourceName, targetName);
        }
    }
}
