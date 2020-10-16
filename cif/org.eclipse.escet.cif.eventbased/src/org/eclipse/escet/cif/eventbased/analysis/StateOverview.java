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

package org.eclipse.escet.cif.eventbased.analysis;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.spaces;

import java.util.List;

import org.eclipse.escet.cif.eventbased.analysis.reporttext.BackgroundColoredText;
import org.eclipse.escet.cif.eventbased.analysis.reporttext.ColoredText;
import org.eclipse.escet.cif.eventbased.analysis.reporttext.ReportText;
import org.eclipse.escet.cif.eventbased.analysis.reporttext.SimpleText;
import org.eclipse.escet.cif.eventbased.apps.SynthesisAnalysisEditor;
import org.eclipse.escet.common.java.Assert;

/** Store an overview of the status of a state. */
public class StateOverview {
    /** The synthesis analysis application, holding state information, and resources like colors. */
    private SynthesisAnalysisEditor app;

    /** The state that was queried. */
    public final StateInfo locInfo;

    /**
     * Existing outgoing edges from this state for all events if available, else {@code null}.
     *
     * <p>
     * Array elements denote the target state, {@code -1} means 'edge not available'.
     * </p>
     */
    public int[] outEdges = null;

    /** Removed edges with controllable events. */
    public List<RemovedEdgeInfo> removedControllables = list();

    /** Removed an edge with an uncontrollable event, or {@code null} if such a calculation event was not found. */
    public List<RemovedEdgeInfo> removedUncontrollables = list();

    /**
     * The removed edge in {@link #removedUncontrollables} that caused removal of the state if it exists, else
     * {@code null}.
     */
    public RemovedEdgeInfo killerRemovedEdge = null;

    /**
     * Calculation event that removed the state (for being non-coreachable), or {@code null} if such a calculation event
     * did not happen.
     */
    public RemovedLocationInfo removedState = null;

    /**
     * Constructor of the {@link StateOverview} class.
     *
     * @param app The synthesis analysis application, holding state information, and resources like colors.
     * @param locInfo State being queried.
     */
    public StateOverview(SynthesisAnalysisEditor app, StateInfo locInfo) {
        this.app = app;
        this.locInfo = locInfo;

        Assert.notNull(this.locInfo);
    }

    /**
     * Does the state exist in the synthesis result?
     *
     * @return Whether the state is available in the synthesis result.
     */
    public boolean stateExists() {
        return killerRemovedEdge == null && removedState == null;
    }

    /**
     * From the information, try to find an interesting successor state to investigate.
     *
     * @return The number of the interesting successor state, or {@code -1} if there is no such state.
     */
    public int getSuccessorState() {
        // Removed edge that killed the state is an interesting successor state.
        if (killerRemovedEdge != null && !killerRemovedEdge.toIsAutomaton) {
            return killerRemovedEdge.to;
        }

        // State was removed due to non-coreachability. Try finding a successor state in the removed edges.
        if (removedState != null) {
            // Since a removal of an edge with an uncontrolled event was not deadly,
            // it can only contain removed edges by plants.

            // Check removed edges with controllable events for a successor state.
            int nextState = -1; // No successor state found.
            for (RemovedEdgeInfo removedEdge: removedControllables) {
                if (removedEdge.toIsAutomaton) {
                    continue;
                }

                if (nextState == -1) {
                    nextState = removedEdge.to;
                } else if (nextState != removedEdge.to) {
                    return -1; // More than one successor state -> User must decide.
                }
            }
            return nextState;
        }

        return -1;
    }

    /**
     * Construct a human-readable report about the state.
     *
     * <p>
     * TODO: Cleanup insertion of empty lines in the output.
     * </p>
     *
     * @param displayFullState Whether to display the full state (the location of each automaton).
     * @param displayControllables Whether to always display disabled controllable events.
     * @return Human-readable report about the state.
     */
    public List<ReportText> makeDescription(boolean displayFullState, boolean displayControllables) {
        List<ReportText> texts = list();

        // Print the state itself.
        texts.add(new SimpleText("Result of "));
        if (!stateExists()) {
            texts.add(new ColoredText("removed", app.redColor));
            texts.add(new SimpleText(" "));
        }
        texts.addAll(getStateText(locInfo.targetState, displayFullState));
        texts.add(new SimpleText(":\r\n  "));

        if (killerRemovedEdge != null) {
            texts.addAll(makeRemovedEdgeText(killerRemovedEdge, displayFullState));
            texts.addAll(
                    addFullRemovedEdgesDescription(removedUncontrollables, killerRemovedEdge, displayFullState, 4));
            if (displayControllables) {
                texts.addAll(addFullRemovedEdgesDescription(removedControllables, null, displayFullState, 4));
            } else {
                texts.addAll(addSummaryRemovedEdgesDescription(removedControllables, "controllable", 4));
            }
        } else if (removedState != null) {
            texts.addAll(makeRemovedLocationText(removedState));
            texts.addAll(
                    addFullRemovedEdgesDescription(removedUncontrollables, killerRemovedEdge, displayFullState, 4));
            texts.addAll(addFullRemovedEdgesDescription(removedControllables, null, displayFullState, 4));
        } else {
            texts.add(new BackgroundColoredText("This state exists in the synthesis result.", app.greenColor));
            texts.add(new SimpleText("\r\n"));
            if (outEdges != null) {
                texts.add(new SimpleText("\r\n"));
                boolean foundEdge = false;
                for (int idx = 0; idx < app.data.events.size(); idx++) {
                    if (outEdges[idx] >= 0 && outEdges[idx] < app.data.states.size()) {
                        texts.add(new SimpleText(
                                fmt("    Edge with event \"%s\" leads to ", app.data.events.get(idx).name)));
                        texts.addAll(getStateText(outEdges[idx], false));
                        texts.add(new SimpleText("\r\n"));
                        foundEdge = true;
                    }
                }
                if (!foundEdge) {
                    texts.add(new SimpleText(fmt("    No outgoing edges found!\r\n")));
                }
            }
            texts.add(new SimpleText("\r\n"));
            texts.addAll(addFullRemovedEdgesDescription(removedControllables, null, displayFullState, 4));
        }

        return texts;
    }

    /**
     * Add a full description of the given removed edges to the output.
     *
     * @param edges Edges to describe.
     * @param skipEdge Edge that should be skipped in the output (use {@code null} if no such edge exists).
     * @param displayFullState Whether to display the full state (the location of each automaton).
     * @param indent Amount of indenting at the start of each line.
     * @return Text to add to the report.
     */
    private List<ReportText> addFullRemovedEdgesDescription(List<RemovedEdgeInfo> edges, RemovedEdgeInfo skipEdge,
            boolean displayFullState, int indent)
    {
        List<ReportText> texts = list();

        for (RemovedEdgeInfo removedEdge: edges) {
            if (removedEdge == skipEdge) {
                continue;
            }

            texts.add(new SimpleText(spaces(indent)));
            texts.addAll(makeRemovedEdgeText(removedEdge, displayFullState));
        }
        return texts;
    }

    /**
     * Add a summary of how many edges were removed.
     *
     * @param edges Edges that were removed.
     * @param evtType Type of the events ("uncontrollable" or "controllable").
     * @param indent Amount of indenting at the start of each line.
     * @return Text to add to the report.
     */
    private static List<ReportText> addSummaryRemovedEdgesDescription(List<RemovedEdgeInfo> edges, String evtType,
            int indent)
    {
        if (edges.isEmpty()) {
            return listc(0);
        }

        String msg;
        if (edges.size() == 1) {
            msg = fmt("(1 removed edge with a %s event is omitted.)\r\n", evtType);
        } else {
            msg = fmt("(%d removed edges with %s events are omitted.)\r\n", edges.size(), evtType);
        }
        return list(new SimpleText(spaces(indent)), new SimpleText(msg));
    }

    /**
     * Make a readable report about the removed edge.
     *
     * @param displayFullState Whether to display the full state (the location of each automaton).
     * @param removedEdge Edge that was removed.
     * @return Text of the report.
     */
    public List<ReportText> makeRemovedEdgeText(RemovedEdgeInfo removedEdge, boolean displayFullState) {
        List<ReportText> result = list();

        EventInfo info = app.data.events.get(removedEdge.event);
        String eventName = info.name;

        String contrText = info.contr ? "controllable" : "uncontrollable";
        result.add(new SimpleText(fmt("Edge with %s event \"%s\" was removed", contrText, eventName)));

        if (removedEdge.toIsAutomaton) {
            String autName = app.data.sourceInfo.sourceInfo.get(removedEdge.to).autName;
            if (removedEdge.to >= app.data.getNumberPlants()) {
                result.add(new SimpleText(fmt(" due to requirement automaton \"%s\".\r\n", autName)));
            } else {
                result.add(new SimpleText(fmt(" due to plant automaton \"%s\".\r\n", autName)));
            }
        } else {
            result.add(new SimpleText(" due to removal of "));
            result.addAll(getStateText(removedEdge.to, displayFullState));
            result.add(new SimpleText(".\r\n"));
        }
        return result;
    }

    /**
     * Make a readable report about the removed location.
     *
     * @param removedState State that was removed.
     * @return Text of the report.
     */
    public List<ReportText> makeRemovedLocationText(RemovedLocationInfo removedState) {
        List<ReportText> result = list(new SimpleText("State was "), new ColoredText("removed", app.redColor));
        if (removedState.isNotCoreachable) {
            result.add(new SimpleText(" as it was found to be non-coreachable.\r\n"));
        } else {
            result.add(new SimpleText(" as it is not marked and locally found to be a deadlock state.\r\n"));
        }
        return result;
    }

    /**
     * Construct a human-readable representation of a state.
     *
     * @param stateNumber Number of the state to convert.
     * @param full Give the full original name as well.
     * @return Pieces of the report to display.
     */
    public List<ReportText> getStateText(int stateNumber, boolean full) {
        List<ReportText> result = list();

        StateInfo state = app.data.states.get(stateNumber);
        boolean initial = state.isInitial();

        // Collect the string pieces.
        int numAuts = app.data.getNumberAutomata();

        result.add(new SimpleText("state "));
        result.add(new BackgroundColoredText(fmt("#%d", stateNumber), app.linkColor));
        if (initial) {
            if (state.marked) {
                result.add(new SimpleText("[initial, marked]"));
            } else {
                result.add(new SimpleText("[initial]"));
            }
        } else if (state.marked) {
            result.add(new SimpleText("[marked]"));
        }

        if (full) {
            result.add(new SimpleText("("));
            for (int aut = 0; aut < numAuts; aut++) {
                int locNumber = state.srcLocs[aut];
                if (aut > 0) {
                    result.add(new SimpleText(", "));
                }
                AutomatonNamesInfo nameInfo = app.data.sourceInfo.sourceInfo.get(aut);
                result.add(new SimpleText(fmt("%s.%s", nameInfo.autName, nameInfo.locNames.get(locNumber))));
            }
            result.add(new SimpleText(")"));
        }
        return result;
    }
}
