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

package org.eclipse.escet.cif.eventbased.apps;

import static org.eclipse.escet.common.java.Exceptions.exToStr;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.cif.eventbased.analysis.AnalysisListHandler;
import org.eclipse.escet.cif.eventbased.analysis.AnalysisReportHandler;
import org.eclipse.escet.cif.eventbased.analysis.AnalysisSearchHandler;
import org.eclipse.escet.cif.eventbased.analysis.DumpfileData;
import org.eclipse.escet.cif.eventbased.analysis.DumpfileLoader;
import org.eclipse.escet.cif.eventbased.analysis.EdgeInfo;
import org.eclipse.escet.cif.eventbased.analysis.EventInfo;
import org.eclipse.escet.cif.eventbased.analysis.RemovedEdgeInfo;
import org.eclipse.escet.cif.eventbased.analysis.RemovedLocationInfo;
import org.eclipse.escet.cif.eventbased.analysis.StateInfo;
import org.eclipse.escet.cif.eventbased.analysis.StateOverview;
import org.eclipse.escet.cif.eventbased.analysis.reporttext.ColoredText;
import org.eclipse.escet.cif.eventbased.analysis.reporttext.ReportText;
import org.eclipse.escet.cif.eventbased.analysis.reporttext.SimpleText;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.eclipse.themes.EclipseThemePreferenceChangeListener;
import org.eclipse.escet.common.app.framework.eclipse.themes.EclipseThemeUtils;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/** 'Editor' for .synth_dump files. */
public class SynthesisAnalysisEditor extends ControlEditor {
    /** Root composite. */
    private Composite root;

    /** Scrolled composite containing the automata and their locations. */
    private ScrolledComposite scrolledLists = null;

    /** Composite area for the automata + locations. */
    private Composite automataArea = null;

    /** Data of the dump file. */
    public DumpfileData data = null;

    /** List handlers of the location names of each automaton. */
    private List<AnalysisListHandler> listSelections = list();

    /** Handler for the buttons, toggles, and text fields. */
    private AnalysisSearchHandler buttons = null;

    /** Report window widget and its handler (initialized while building the gui). */
    private AnalysisReportHandler reportArea = null;

    /** The history of analyzed states. */
    private List<Integer> searchHistory = list();

    /** Color to denote 'available'. */
    public Color availableColor = null;

    /** Color to denote 'removed'. */
    public Color removedColor = null;

    /** Color of a link. */
    public Color linkColor = null;

    /**
     * Construct a string with a count, and fix the spelling depending on having a singular count.
     *
     * @param count Count to print.
     * @param text Basic text to output, contains a "%d" and a "%s" (in that order).
     * @param singular Text to insert at "%s" if the count is 1.
     * @param multilar Text to insert at "%s" if the text is not 1.
     * @return Output text with the count and the right spelling.
     */
    public static String makeCountText(int count, String text, String singular, String multilar) {
        if (count == 1) {
            return fmt(text, count, singular);
        }
        return fmt(text, count, multilar);
    }

    /**
     * Layout the widgets of the window for the application.
     *
     * @param parent Parent window.
     * @return Root of the application tree.
     */
    private Composite buildWidgets(Composite parent) {
        // Root composite.
        root = new Composite(parent, SWT.NONE);
        GridLayout automataGrid = new GridLayout(2, false);
        root.setLayout(automataGrid);

        // Automata column.
        scrolledLists = new ScrolledComposite(root, SWT.V_SCROLL);
        automataArea = new Composite(scrolledLists, SWT.NONE);
        scrolledLists.setContent(automataArea);
        scrolledLists.setVisible(false);
        GridLayout listLayout = new GridLayout(1, false);
        automataArea.setLayout(listLayout);

        automataArea.setSize(automataArea.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, false, true);
        gridData.verticalSpan = 2;
        scrolledLists.setLayoutData(gridData);
        scrolledLists.pack();

        // Top-right search handler, and bottom-right report area.
        buttons = new AnalysisSearchHandler(root);
        reportArea = new AnalysisReportHandler(root);

        root.pack();
        return root;
    }

    /** Build a vertically scrollable column of automata in the window. */
    private void buildAutomataColumn() {
        int numAuts = data.getNumberAutomata();
        int numPlants = data.getNumberPlants();
        for (int autIndex = 0; autIndex < numAuts; autIndex++) {
            int preIndent = ((autIndex != 0) ? 10 : 0) + ((autIndex == numPlants) ? 40 : 0);
            AnalysisListHandler locList = new AnalysisListHandler(automataArea, preIndent,
                    data.sourceInfo.sourceInfo.get(autIndex));
            listSelections.add(locList);
        }

        // Recompute layout.
        automataArea.pack(); // Recompute natural size.
        scrolledLists.setVisible(true);
        root.layout(true, true);
    }

    /** User clicked 'reset to initial state'. */
    public void clickedResetState() {
        if (data == null) {
            return;
        }

        for (AnalysisListHandler alh: listSelections) {
            alh.setValue(0); // Select initial state in each automaton.
        }
        clickedSearch(false);
    }

    /**
     * User clicked 'search'/'back', dig in the data looking for clues about the currently indicated state.
     *
     * @param goBack Go to the previous state.
     */
    public void clickedSearch(boolean goBack) {
        if (data == null) {
            return;
        }

        int state;
        if (goBack && searchHistory.size() > 1) {
            searchHistory.remove(searchHistory.size() - 1);

            state = last(searchHistory);
        } else {
            // Check that all automata have a selected location.
            for (AnalysisListHandler handler: listSelections) {
                Assert.check(handler.value >= 0);
            }

            StateInfo found = findSelectedState();
            if (found == null) {
                reportArea.setResult(
                        new SimpleText("No information about the queried state found, it was never created."));
                return;
            }

            state = found.targetState;
            if (searchHistory.isEmpty() || last(searchHistory) != state) {
                searchHistory.add(state);
            }
        }
        buttons.setEnableBack(searchHistory.size() > 1);

        int count = buttons.getNumberIntermediateStates();
        boolean displayFullState = buttons.getFullStateDisplayed();
        boolean displayControllables = buttons.getDisabledControllablesDisplayed();
        boolean displayPlant = buttons.getPlantRemovedDisplayed();

        // Display the full state in the header of the result.
        StateOverview overview = getOverview(state, displayPlant, true);
        List<ReportText> header = list();
        header.add(new SimpleText("Analysis of "));
        header.addAll(overview.getStateText(overview.locInfo.targetState, true));
        header.add(new SimpleText(":\r\n\r\n"));
        reportArea.setResult(header);

        // Report about the queried state.
        reportArea.addResult(overview.makeDescription(displayFullState, displayControllables));

        // Follow cause chain, reporting the first few entries, and finally the last one.
        int skipCount = 0;
        while (true) {
            state = overview.getSuccessorState();
            if (state < 0) {
                break;
            }

            overview = getOverview(state, displayPlant, false);
            if (count > 0) {
                reportArea.addResult(new SimpleText("\r\n"));
                reportArea.addResult(overview.makeDescription(displayFullState, displayControllables));
                count--;
            } else {
                skipCount++;
            }
        }
        if (skipCount > 0) {
            // Some states were not printed, last one is printed below, but warn the user if more are missing.
            if (skipCount > 1) {
                reportArea
                        .addResult(new SimpleText(fmt("\r\n(Skipped reporting of %d states.)\r\n\r\n", skipCount - 1)));
            }
            reportArea.addResult(overview.makeDescription(displayFullState, displayControllables));
        }

        reportArea.addResult(new SimpleText("\r\nDone.\r\n"));
    }

    /**
     * Construct an overview of the calculation events that happened for a given state.
     *
     * @param state State to summarize to an overview.
     * @param displayPlant Whether to display events disabled by the plant.
     * @param expandEdges Expand available edges from this state.
     * @return Calculation events that happened, and status of the state, if the state exists, else {@code null}.
     */
    private StateOverview getOverview(int state, boolean displayPlant, boolean expandEdges) {
        if (state < 0 || state >= data.states.size() || data.states.get(state) == null) {
            return null;
        }

        StateOverview overview = new StateOverview(this, data.states.get(state));

        // Process calculation events for this state.
        for (Object calcEvent: data.calculationEvents) {
            if (calcEvent instanceof RemovedEdgeInfo) {
                RemovedEdgeInfo removedEdge = (RemovedEdgeInfo)calcEvent;
                if (removedEdge.from != state) {
                    continue;
                }

                EventInfo evtInfo = data.events.get(removedEdge.event);
                boolean notPlantInduced = !removedEdge.toIsAutomaton || removedEdge.to >= data.getNumberPlants();
                if (evtInfo.contr) {
                    if (displayPlant || notPlantInduced) {
                        overview.removedControllables.add(removedEdge);
                    }
                } else {
                    if (displayPlant || notPlantInduced) {
                        overview.removedUncontrollables.add(removedEdge);
                    }
                    if (notPlantInduced && overview.killerRemovedEdge == null) {
                        overview.killerRemovedEdge = removedEdge;
                    }
                }
                continue;
            } else if (calcEvent instanceof RemovedLocationInfo) {
                RemovedLocationInfo removedState = (RemovedLocationInfo)calcEvent;
                if (removedState.loc != state) {
                    continue;
                }

                overview.removedState = removedState;
                break; // Location was deleted, remaining calculation events won't give any information.
            } else {
                Assert.fail("Unexpected kind of calculation event found.");
                break;
            }
        }

        // Expand edges of this state, if requested.
        if (expandEdges && overview.locInfo.outEdges != null && overview.removedState == null) {
            int[] edges = new int[data.events.size()];
            Arrays.fill(edges, -1);

            // Add created edges.
            for (EdgeInfo edgeInfo: overview.locInfo.outEdges) {
                edges[edgeInfo.event] = edgeInfo.destLoc;
            }

            // Remove deleted edges.
            for (RemovedEdgeInfo removed: overview.removedControllables) {
                edges[removed.event] = -1;
            }
            for (RemovedEdgeInfo removed: overview.removedUncontrollables) {
                edges[removed.event] = -1;
            }

            overview.outEdges = edges;
        }
        return overview;
    }

    /**
     * Find the state expressed by the lists with locations at the GUI.
     *
     * @return The state belonging to the selected combination of locations, or {@code null} if the combination was
     *     never created by the synthesis.
     */
    private StateInfo findSelectedState() {
        // Construct collection of locations.
        int[] locs = new int[listSelections.size()];
        for (int autIndex = 0; autIndex < listSelections.size(); autIndex++) {
            locs[autIndex] = listSelections.get(autIndex).value;
        }

        // Lookup collection of locations in the sorted map. If it exists return the state, else return null.
        Integer stateNum = data.sortedStates.get(locs);
        if (stateNum == null) {
            return null;
        }
        return data.states.get(stateNum);
    }

    /**
     * Set the automata locations selection to the provided state.
     *
     * @param stateNumber State to select in the automata locations.
     */
    public void setSelectedState(int stateNumber) {
        if (stateNumber < 0 || stateNumber >= data.states.size()) {
            return;
        }
        StateInfo stateInfo = data.states.get(stateNumber);
        for (int autIndex = 0; autIndex < listSelections.size(); autIndex++) {
            listSelections.get(autIndex).setValue(stateInfo.srcLocs[autIndex]);
        }
        clickedSearch(false); // Update state analysis text.
    }

    @Override
    protected Control createContents(Composite parent) {
        // Get filename, setup the displayed content names.
        String absFilename = input.getAbsoluteFilePath();
        String filename = Paths.getFileName(absFilename);

        int dirPrefix = absFilename.length() - filename.length();
        while (dirPrefix > 0
                && (absFilename.charAt(dirPrefix - 1) == '/' || absFilename.charAt(dirPrefix - 1) == '\\'))
        {
            dirPrefix--;
        }
        if (dirPrefix == 0) {
            dirPrefix = absFilename.length(); // Make sure there is always something to see.
        }

        setContentDescription(absFilename.substring(0, dirPrefix));
        setPartName(filename);

        // Set up colors and theming.
        setColors();

        EclipseThemePreferenceChangeListener themeListener = new EclipseThemePreferenceChangeListener(e -> {
            if (root.isDisposed()) {
                return;
            }
            setColors();

            // Reset state to update GUI colors.
            clickedResetState();
        });
        parent.addDisposeListener(e -> themeListener.unregister());

        // Create GUI.
        Composite root = buildWidgets(parent);

        // Start loading the dump file in another thread, eventually calling {@link #loadingFinished}.
        DumpfileLoader loader = new DumpfileLoader(absFilename, this);
        Thread loaderThread = new Thread(loader, loader.getClass().getName());
        loaderThread.start();

        return root;
    }

    /**
     * Callback to report that loading the dump file has finished.
     *
     * @param data Data of the dump file, or {@code null} if loading failed.
     * @param ex Failure exception if failed (else {@code null}).
     */
    public void loadingFinished(DumpfileData data, Exception ex) {
        if (!isAvailable()) {
            return; // Gui has been closed in the mean time.
        }

        if (data == null) {
            List<ReportText> report = list();
            report.add(new ColoredText(fmt("Loading of file \"%s\" failed!\r\n", input.getAbsoluteFilePath()),
                    removedColor));
            if (ex != null) {
                report.add(new SimpleText("\r\nReason:\r\n" + exToStr(ex) + "\r\n"));
            }
            reportArea.setResult(report);
            return;
        }

        this.data = data;
        buttons.setApplication(this);
        reportArea.setApplication(this);

        buildAutomataColumn();
        buttons.setEnableBack(searchHistory.size() > 1);
    }

    /** Set the GUI colors. */
    private void setColors() {
        if (EclipseThemeUtils.isDarkThemeInUse()) {
            availableColor = new Color(0x06, 0x55, 0x3C); // #06553C, green.
            removedColor = new Color(0xFF, 0x63, 0x47); // #FF6347, tomato red.
            linkColor = new Color(0x0B, 0x24, 0xE5); // #0B24E5, blue.
        } else {
            availableColor = new Color(0x9A, 0xCD, 0x32); // #9ACD32, yellowgreen.
            removedColor = new Color(0xFF, 0x00, 0x00); // #FF0000, red.
            linkColor = new Color(0x87, 0xCE, 0xFA); // #87CEFA, lightskyblue.
        }
    }
}
