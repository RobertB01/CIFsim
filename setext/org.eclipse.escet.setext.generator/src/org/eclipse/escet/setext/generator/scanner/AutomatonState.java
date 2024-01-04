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

package org.eclipse.escet.setext.generator.scanner;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;
import org.eclipse.escet.setext.parser.ast.scanner.Terminal;

/** A state of a scanner {@link Automaton}. */
public class AutomatonState {
    /** The positions (unique character nodes) that this state represents. */
    public final Set<RegExChar> positions;

    /** The unique id of this state in the automaton. Value is {@code -1} if not yet part of an automaton. */
    public int id = -1;

    /** The terminal that may be accepted in this state, or {@code null} if no terminal may be accepted. */
    public Terminal accept = null;

    /**
     * The outgoing edges of this state. Mapping from the code point that must be recognized before the edge may be
     * taken, to the target state of the edge. See {@link RegExChar#character} for the semantics of the code point
     * values. Mapping is filled in-place.
     */
    public final Map<Integer, AutomatonState> edges = map();

    /**
     * Constructor for the {@link AutomatonState} class.
     *
     * @param positions The positions (unique character nodes) that this state represents.
     */
    public AutomatonState(Set<RegExChar> positions) {
        this.positions = positions;
        Assert.check(!positions.isEmpty());
    }

    /**
     * Adds an outgoing edge to this state.
     *
     * @param codePoint The code point that must be recognized before this edge may be taken. See
     *     {@link RegExChar#character} for the semantics of the code point value.
     * @param state The target state of the edge.
     * @throws IllegalStateException If an edge with that code point already exists.
     */
    public void addEdge(int codePoint, AutomatonState state) {
        if (edges.containsKey(codePoint)) {
            throw new IllegalStateException("Duplicate edge.");
        }
        edges.put(codePoint, state);
    }

    @Override
    public int hashCode() {
        // We only consider the positions for value equality.
        return AutomatonState.class.hashCode() ^ positions.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // We only consider the positions for value equality.
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AutomatonState)) {
            return false;
        }
        AutomatonState other = (AutomatonState)obj;
        return positions.equals(other.positions);
    }

    /**
     * Returns the outgoing edges of this state, sorted first in ascending order by target state id, and then in
     * ascending order by code point.
     *
     * @return The sorted, outgoing edges of this state.
     */
    public List<Pair<Integer, AutomatonState>> getSortedEdges() {
        // Note that all edges have unique code points.
        List<Pair<Integer, AutomatonState>> sortedEdges = list();
        for (Entry<Integer, AutomatonState> entry: edges.entrySet()) {
            sortedEdges.add(pair(entry.getKey(), entry.getValue()));
        }

        Comparator<Pair<Integer, AutomatonState>> cmp;
        cmp = new Comparator<>() {
            @Override
            public int compare(Pair<Integer, AutomatonState> t1, Pair<Integer, AutomatonState> t2) {
                // Ascending order by target state id.
                int targetId1 = t1.right.id;
                int targetId2 = t2.right.id;
                if (targetId1 < targetId2) {
                    return -1;
                }
                if (targetId1 > targetId2) {
                    return 1;
                }

                // Ascending order by code point.
                return t1.left.compareTo(t2.left);
            }
        };
        Collections.sort(sortedEdges, cmp);

        return sortedEdges;
    }

    /**
     * Prints the state.
     *
     * @param s The stream to which to write the output.
     * @param initial Is this the initial state?
     */
    public void print(AppStream s, boolean initial) {
        // Get position texts.
        List<String> charTxts = list();
        for (RegExChar c: positions) {
            charTxts.add(codePointToStr(c.character, true));
        }
        Collections.sort(charTxts, Strings.SORTER);

        // Print state header.
        s.println(fmt("%sstate %d (%s):", initial ? "initial " : "", id, charTxts));

        // Print accepting terminal, if any.
        if (accept != null) {
            String termTxt = fmt("%s\"%s\" (priority %d)", (accept.name == null) ? "" : accept.name + " = ",
                    accept.regEx.toString(), accept.priority);
            s.println("  accepts terminal: " + termTxt);
        }

        // Separator.
        if (accept != null && !edges.isEmpty()) {
            s.println();
        }

        // Print edges.
        for (Pair<Integer, AutomatonState> edge: getSortedEdges()) {
            int codePoint = edge.left;
            AutomatonState target = edge.right;
            String codePointTxt = codePointToStr(codePoint, false);
            String edgeTxt = codePointTxt + " -> " + target.id;
            s.println("  " + edgeTxt);
        }
    }

    /**
     * Convert code point to human readable text.
     *
     * @param codePoint The code point to convert.
     * @param compact Whether to return a compact notation.
     * @return The human readable text.
     * @see RegExChar#character Information on the interpretation of code point values.
     */
    private String codePointToStr(int codePoint, boolean compact) {
        String codePointTxt;
        if (codePoint > 127) {
            // Unsupported non-ASCII character.
            String msg = "We should not have non-ASCII chars here.";
            throw new RuntimeException(msg);
        } else if (codePoint == -1) {
            // End-of-file marker.
            codePointTxt = "\u00B6";
        } else if (codePoint < -1) {
            // Custom marker.
            codePointTxt = fmt("\u00AB%d\u00BB", codePoint);
        } else if (codePoint == '\n') {
            codePointTxt = "\\n";
        } else if (codePoint == '\r') {
            codePointTxt = "\\r";
        } else if (codePoint == '\t') {
            codePointTxt = "\\t";
        } else if (codePoint <= 31 || codePoint == 127) {
            // Remaining control characters.
            codePointTxt = "";
        } else {
            // Remaining ASCII characters.
            codePointTxt = "\"" + Strings.codePointToStr(codePoint) + "\"";
        }

        if (!codePointTxt.isEmpty() && codePoint >= -1) {
            codePointTxt += compact ? "=" : " = ";
        }

        if (codePoint >= 0) {
            String hexString = Integer.toHexString(codePoint);
            hexString = StringUtils.leftPad(hexString, 2, '0');
            if (!compact) {
                codePointTxt += "(";
            }
            codePointTxt += "U+" + hexString;
            if (!compact) {
                codePointTxt += ")";
            }
        } else if (codePoint == -1) {
            codePointTxt += compact ? "-1" : "(-1)";
        }

        return codePointTxt;
    }
}
