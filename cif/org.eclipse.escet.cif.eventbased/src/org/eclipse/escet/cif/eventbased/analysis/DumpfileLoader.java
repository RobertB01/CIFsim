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

package org.eclipse.escet.cif.eventbased.analysis;

import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.BLOCKING_LOC_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.EOD_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.EVENTS_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.NEW_EDGE;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.NEW_MARKED_LOC_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.NEW_UNMARKED_LOC_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.NOT_COREACH_LOC_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.NOT_REACH_LOC_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.REMOVED_DEST_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.REMOVED_EDGE_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.SOURCE_INFO_ID;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.openInput;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.readBlockByte;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.readDataEvents;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.readDataRemovedEdge;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.readDataRemovedLocation;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.readDataSourceAutomataInformation;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.readNewEdge;
import static org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpIO.readNewLocation;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.DataInputStream;

import org.eclipse.escet.cif.eventbased.apps.SynthesisAnalysisEditor;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.swt.widgets.Display;

/** Class for loading the synthesis dump file. */
public class DumpfileLoader implements Runnable {
    /** Absolute filename of the dump file to load. */
    private final String filename;

    /** Object to call back after finishing loading. */
    private final SynthesisAnalysisEditor editor;

    /** Data of the dump file. */
    private final DumpfileData data = new DumpfileData();

    /**
     * Constructor of the {@link DumpfileLoader} class.
     *
     * @param filename Name of the dump file to load.
     * @param editor Synthesis analysis editor.
     */
    public DumpfileLoader(String filename, SynthesisAnalysisEditor editor) {
        this.filename = filename;
        this.editor = editor;
    }

    @Override
    public void run() {
        DataInputStream inHandle = null;
        boolean ok = false;
        InputOutputException exception = null;
        try {
            inHandle = openInput(filename);
            readDumpFile(inHandle);
            ok = true;
        } catch (InputOutputException ex) {
            exception = ex;
        } finally {
            SynthesisDumpIO.close(inHandle);
        }

        final DumpfileData result = ok ? data : null;
        final InputOutputException ex = exception;
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                editor.loadingFinished(result, ex);
            }
        });
    }

    /**
     * Read the information from the synthesis dump file.
     *
     * @param inHandle Input file handle to read from.
     */
    private void readDumpFile(DataInputStream inHandle) {
        boolean finished = false;
        while (!finished) {
            byte blockByte = readBlockByte(inHandle);
            switch (blockByte) {
                case SOURCE_INFO_ID:
                    data.sourceInfo = readDataSourceAutomataInformation(inHandle);
                    break;

                case EVENTS_ID:
                    data.events = readDataEvents(inHandle);
                    break;

                case NEW_UNMARKED_LOC_ID:
                case NEW_MARKED_LOC_ID: {
                    int numAuts = data.getNumberAutomata();
                    StateInfo state = readNewLocation(blockByte, numAuts, inHandle);
                    state.outEdges = list();
                    Assert.check(state.targetState == data.states.size());
                    data.states.add(state);
                    data.sortedStates.put(state.srcLocs, state.targetState);
                    break;
                }

                case NEW_EDGE: {
                    AddedEdgeInfo edgeInfo = readNewEdge(inHandle);
                    StateInfo src = data.states.get(edgeInfo.from);
                    src.outEdges.add(new EdgeInfo(edgeInfo.event, edgeInfo.to));
                    break;
                }

                case BLOCKING_LOC_ID:
                case NOT_COREACH_LOC_ID:
                case NOT_REACH_LOC_ID: {
                    RemovedLocationInfo locInfo = readDataRemovedLocation(blockByte, inHandle);
                    data.calculationEvents.add(locInfo);
                    break;
                }

                case REMOVED_DEST_ID:
                case REMOVED_EDGE_ID: {
                    RemovedEdgeInfo edgeInfo = readDataRemovedEdge(blockByte, inHandle);
                    data.calculationEvents.add(edgeInfo);
                    break;
                }

                case EOD_ID:
                    finished = true;
                    break;

                default:
                    Assert.fail(fmt("Unknown block byte 0x%02x found.", blockByte));
                    break; // Not reached.
            }
        }
    }
}
