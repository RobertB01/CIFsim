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

package org.eclipse.escet.cif.eventbased.analysis;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.java.Assert;

/** Perform IO to and from a synthesis dump file. */
public class SynthesisDumpIO {
    /** Header data of an event-based synthesis dump file (version 1). */
    public static final byte[] HEADER_BLOCK = {'E', 'S', 'D', 3, 0};

    /** ID of a data block with source automata information. */
    public static final byte SOURCE_INFO_ID = 0x01;

    /** ID of a data block describing the available events. */
    public static final byte EVENTS_ID = 0x02;

    /** ID of a data block describing a new unmarked location. */
    public static final byte NEW_UNMARKED_LOC_ID = 0x10;

    /** ID of a data block describing a new marked location. */
    public static final byte NEW_MARKED_LOC_ID = 0x11;

    /** ID of a data block describing a new edge. */
    public static final byte NEW_EDGE = 0x12;

    /** ID of a data block describing a blocking location. */
    public static final byte BLOCKING_LOC_ID = 0x20;

    /** ID of a data block describing a non-coreachable location. */
    public static final byte NOT_COREACH_LOC_ID = 0x21;

    /** ID of a data block describing removal of an edge due to removing the destination. */
    public static final byte REMOVED_DEST_ID = 0x22;

    /** ID of a data block describing removal of an edge due to a not participating automaton. */
    public static final byte REMOVED_EDGE_ID = 0x23;

    /** ID of data block describing a non-reachable location. */
    public static final byte NOT_REACH_LOC_ID = 0x24;

    /** ID of an end-of-data block. */
    public static final byte EOD_ID = 0x70;

    /** Value denoting the event is controllable. */
    public static final byte EVT_CONTR = 1;

    /** Value denoting the event is uncontrollable. */
    public static final byte EVT_UNCONTR = 0;

    /** Constructor of the {@link SynthesisDumpIO} class. */
    private SynthesisDumpIO() {
        // Static class.
    }

    /**
     * Construct a buffered data output stream to the given file.
     *
     * @param filename Filename of the file to create and open for writing.
     * @return File handle opened for writing.
     */
    public static DataOutputStream openOutput(String filename) {
        OutputStream out;
        try {
            out = new FileOutputStream(filename);
        } catch (FileNotFoundException ex) {
            String msg = fmt("Cannot open the synthesis dump file \"%s\" for writing.", filename);
            throw new InputOutputException(msg, ex);
        }
        out = new BufferedOutputStream(out);
        DataOutputStream outHandle = new DataOutputStream(out);

        writeHeader(outHandle);
        return outHandle;
    }

    /**
     * Open a buffered data input stream for the given file.
     *
     * @param filename Filename of the file to open for reading.
     * @return File handle to the opened input stream.
     */
    public static DataInputStream openInput(String filename) {
        InputStream inp;
        try {
            inp = new FileInputStream(filename);
        } catch (FileNotFoundException ex) {
            String msg = fmt("Cannot open the synthesis dump file \"%s\" for reading.", filename);
            throw new InputOutputException(msg, ex);
        }
        inp = new BufferedInputStream(inp);
        DataInputStream inHandle = new DataInputStream(inp);
        readHeader(inHandle);
        return inHandle;
    }

    /**
     * Close the opened output stream.
     *
     * @param outHandle File handle to close.
     */
    public static void close(DataOutputStream outHandle) {
        if (outHandle == null) {
            return;
        }

        try {
            outHandle.close();
        } catch (IOException ex) {
            String msg = "Failed to close the (written) synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Close the opened input stream.
     *
     * @param inHandle File handle to close.
     */
    public static void close(DataInputStream inHandle) {
        if (inHandle == null) {
            return;
        }

        try {
            inHandle.close();
        } catch (IOException ex) {
            String msg = "Failed to close the (read) synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Write a header to the file for recognizing a dump file.
     *
     * @param outHandle Output stream to write to.
     */
    private static void writeHeader(DataOutputStream outHandle) {
        try {
            outHandle.write(HEADER_BLOCK);
        } catch (IOException ex) {
            String msg = "Failed to write the header to the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Read the header from the input stream, and verify the signature.
     *
     * @param inHandle Input stream to read from.
     */
    private static void readHeader(DataInputStream inHandle) {
        byte[] inBlock = new byte[HEADER_BLOCK.length];

        try {
            for (int index = 0; index < inBlock.length; index++) {
                inBlock[index] = inHandle.readByte();
            }
        } catch (IOException ex) {
            String msg = "Failed to read the header of the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }

        if (!Arrays.equals(HEADER_BLOCK, inBlock)) {
            String msg = "File header does not match with the synthesis dump file header.";
            throw new InputOutputException(msg);
        }
    }

    /**
     * Read the block byte from the input stream to get the next block.
     *
     * @param inHandle Input stream to read from.
     * @return Header block byte of the next block.
     */
    public static byte readBlockByte(DataInputStream inHandle) {
        byte b;

        try {
            b = inHandle.readByte();
        } catch (IOException ex) {
            String msg = "Failed to read the block byte from the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
        return b;
    }

    /**
     * Write the source automata information onto the dump file as a {@link #SOURCE_INFO_ID} byte and the data.
     *
     * @param sourceInfo Source information to write (ordered locations for each of the automata).
     * @param numPlants Number of plant automata.
     * @param outHandle Output stream to write to.
     */
    public static void writeSourceAutomataInformation(List<AutomatonNamesInfo> sourceInfo, int numPlants,
            DataOutputStream outHandle)
    {
        Assert.check(numPlants >= 0 && numPlants <= sourceInfo.size());

        try {
            outHandle.write(SOURCE_INFO_ID);
            outHandle.writeInt(sourceInfo.size());
            outHandle.writeInt(numPlants);
            for (AutomatonNamesInfo autNames: sourceInfo) {
                outHandle.writeUTF(autNames.autName);
                outHandle.writeInt(autNames.locNames.size());
                for (String locName: autNames.locNames) {
                    outHandle.writeUTF(locName);
                }
            }
        } catch (IOException ex) {
            String msg = "Failed to write the source information to the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Read the data part of the {@link #SOURCE_INFO_ID} block, as written by {@link #writeSourceAutomataInformation}.
     *
     * @param inHandle Input stream to read from.
     * @return Data of the {@link #SOURCE_INFO_ID} block.
     */
    public static SourceReadInfo readDataSourceAutomataInformation(DataInputStream inHandle) {
        SourceReadInfo sri = new SourceReadInfo();

        try {
            // SOURCE_INFO_ID is already read before the call.
            int numAuts = inHandle.readInt();
            sri.numPlants = inHandle.readInt();
            for (int autNumber = 0; autNumber < numAuts; autNumber++) {
                String autName = inHandle.readUTF();
                int locCount = inHandle.readInt();
                List<String> locs = listc(locCount);
                for (int locNumber = 0; locNumber < locCount; locNumber++) {
                    locs.add(inHandle.readUTF());
                }
                sri.sourceInfo.add(new AutomatonNamesInfo(autName, locs));
            }
        } catch (IOException ex) {
            String msg = "Failed to read the source information from the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
        return sri;
    }

    /**
     * Write event information to the synthesis dump file, as a {@link #EVENTS_ID} byte and the data.
     *
     * @param events Available events ordered by their number.
     * @param outHandle Output stream to write to.
     */
    public static void writeEvents(List<EventInfo> events, DataOutputStream outHandle) {
        try {
            outHandle.write(EVENTS_ID);
            outHandle.writeInt(events.size());
            for (EventInfo evt: events) {
                outHandle.writeByte(evt.contr ? EVT_CONTR : EVT_UNCONTR);
                outHandle.writeUTF(evt.name);
            }
        } catch (IOException ex) {
            String msg = "Failed to write event information to the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Read the data part of the {@link #EVENTS_ID} block, as written by {@link #writeEvents}.
     *
     * @param inHandle Input stream to read from.
     * @return Data of the {@link #EVENTS_ID} block.
     */
    public static List<EventInfo> readDataEvents(DataInputStream inHandle) {
        List<EventInfo> events;

        try {
            // EVENTS_ID is already read before the call.
            int numEvents = inHandle.readInt();
            events = listc(numEvents);
            for (int index = 0; index < numEvents; index++) {
                int contr = inHandle.readByte();
                String name = inHandle.readUTF();
                if (contr == EVT_CONTR) {
                    events.add(new EventInfo(name, true));
                } else if (contr == EVT_UNCONTR) {
                    events.add(new EventInfo(name, false));
                } else {
                    String msg = "Failed to read the event controllability from the synthesis dump file.";
                    throw new InputOutputException(msg);
                }
            }
        } catch (IOException ex) {
            String msg = "Failed to read the event information from the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
        return events;
    }

    /**
     * Write new state information to the synthesis dump file, as a {@link #NEW_MARKED_LOC_ID} or
     * {@link #NEW_UNMARKED_LOC_ID} byte, and the data.
     *
     * @param srcLocs Location numbers of the source automata.
     * @param targetLoc Number of the target location.
     * @param marked Whether the target location is marked.
     * @param outHandle Output stream to write to.
     */
    public static void writeNewLocation(int[] srcLocs, int targetLoc, boolean marked, DataOutputStream outHandle) {
        try {
            outHandle.write(marked ? NEW_MARKED_LOC_ID : NEW_UNMARKED_LOC_ID);
            outHandle.writeInt(targetLoc);
            for (int src: srcLocs) {
                outHandle.writeInt(src);
            }
        } catch (IOException ex) {
            String msg = "Failed to write a new state to the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Read the data part of the {@link #NEW_MARKED_LOC_ID} or {@link #NEW_UNMARKED_LOC_ID} block, as written by
     * {@link #writeNewLocation}.
     *
     * @param blockByte Header byte of the block.
     * @param numAuts Number of automata in the source.
     * @param inHandle Input stream to read from.
     * @return Data of the {@link #NEW_MARKED_LOC_ID} or {@link #NEW_UNMARKED_LOC_ID} block.
     */
    public static StateInfo readNewLocation(byte blockByte, int numAuts, DataInputStream inHandle) {
        Assert.check(blockByte == NEW_MARKED_LOC_ID || blockByte == NEW_UNMARKED_LOC_ID);
        int[] srcLocs = new int[numAuts];
        int targetLoc;

        try {
            // NEW_[UN]MARKED_LOC_ID is already read before the call.
            targetLoc = inHandle.readInt();
            for (int idx = 0; idx < numAuts; idx++) {
                srcLocs[idx] = inHandle.readInt();
            }
        } catch (IOException ex) {
            String msg = "Failed to read a new state from the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
        return new StateInfo(srcLocs, targetLoc, blockByte == NEW_MARKED_LOC_ID);
    }

    /**
     * Write adding of a new edge to the synthesis dump file, as a {@link #NEW_EDGE} byte, and the data.
     *
     * @param edgeInfo Information of the new edge.
     * @param outHandle Output stream to write to.
     */
    public static void writeNewEdge(AddedEdgeInfo edgeInfo, DataOutputStream outHandle) {
        try {
            outHandle.write(NEW_EDGE);
            outHandle.writeInt(edgeInfo.from);
            outHandle.writeInt(edgeInfo.to);
            outHandle.writeInt(edgeInfo.event);
        } catch (IOException ex) {
            String msg = "Failed to write a new edge to the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Read the data part of a {@link #NEW_EDGE} block, as written by {@link #writeNewEdge}.
     *
     * @param inHandle Input stream to read from.
     * @return Data of the {@link #NEW_EDGE} block.
     */
    public static AddedEdgeInfo readNewEdge(DataInputStream inHandle) {
        int src, dst, evt;

        try {
            // NEW_EDGE is already read before the call.
            src = inHandle.readInt();
            dst = inHandle.readInt();
            evt = inHandle.readInt();
        } catch (IOException ex) {
            String msg = "Failed to read a new edge from the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
        return new AddedEdgeInfo(src, dst, evt);
    }

    /**
     * Write removed location information to the synthesis dump file, as a {@link #BLOCKING_LOC_ID},
     * {@link #NOT_COREACH_LOC_ID} or {@link #NOT_REACH_LOC_ID} byte and the data.
     *
     * @param removedLoc Removed location to write.
     * @param outHandle Output stream to write to.
     */
    public static void writeRemovedLocation(RemovedLocationInfo removedLoc, DataOutputStream outHandle) {
        try {
            switch (removedLoc.reason) {
                case IS_BLOCKING:
                    outHandle.write(BLOCKING_LOC_ID);
                    break;

                case IS_NOT_COREACHABLE:
                    outHandle.write(NOT_COREACH_LOC_ID);
                    break;

                case IS_NOT_REACHABLE:
                    outHandle.write(NOT_REACH_LOC_ID);
                    break;

                default:
                    Assert.fail("Unknown removed location reason.");
            }
            outHandle.writeInt(removedLoc.loc);
        } catch (IOException ex) {
            String msg = "Failed to write removed location information to the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Read the data part of the {@link #BLOCKING_LOC_ID}, {@link #NOT_COREACH_LOC_ID} or {@link #NOT_REACH_LOC_ID}
     * block, as written by {@link #writeRemovedLocation}.
     *
     * @param blockByte Header byte of the block.
     * @param inHandle Input stream to read from.
     * @return Data of the {@link #BLOCKING_LOC_ID}, {@link #NOT_COREACH_LOC_ID} or {@link #NOT_REACH_LOC_ID} block.
     */
    public static RemovedLocationInfo readDataRemovedLocation(byte blockByte, DataInputStream inHandle) {
        RemovedLocationReason reason = null;
        switch (blockByte) {
            case BLOCKING_LOC_ID:
                reason = RemovedLocationReason.IS_BLOCKING;
                break;

            case NOT_COREACH_LOC_ID:
                reason = RemovedLocationReason.IS_NOT_COREACHABLE;
                break;

            case NOT_REACH_LOC_ID:
                reason = RemovedLocationReason.IS_NOT_REACHABLE;
                break;

            default:
                Assert.fail("Unknown removed location reason.");
        }
        int loc;

        try {
            // [BLOCKING_LOC|NOT_COREACH|NOT_REACH]_ID is already read before the call.
            loc = inHandle.readInt();
        } catch (IOException ex) {
            String msg = "Failed to read removed location information from the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
        return new RemovedLocationInfo(loc, reason);
    }

    /**
     * Write removed edge information to the synthesis dump file, as a {@link #REMOVED_DEST_ID} or
     * {@link #REMOVED_EDGE_ID} byte and the data.
     *
     * @param removedEdge Removed edge to write.
     * @param outHandle Output stream to write to.
     */
    public static void writeRemovedEdge(RemovedEdgeInfo removedEdge, DataOutputStream outHandle) {
        try {
            outHandle.write(removedEdge.toIsAutomaton ? REMOVED_EDGE_ID : REMOVED_DEST_ID);
            outHandle.writeInt(removedEdge.from);
            outHandle.writeInt(removedEdge.to);
            outHandle.writeInt(removedEdge.event);
        } catch (IOException ex) {
            String msg = "Failed to write removed edge information to the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }

    /**
     * Read the data part of the {@link #REMOVED_DEST_ID} or {@link SynthesisDumpIO#REMOVED_EDGE_ID} block, as written
     * by {@link #writeRemovedEdge}.
     *
     * @param blockByte Header byte of the block.
     * @param inHandle Input stream to read from.
     * @return Data of the {@link #REMOVED_DEST_ID} or {@link #REMOVED_EDGE_ID} block.
     */
    public static RemovedEdgeInfo readDataRemovedEdge(byte blockByte, DataInputStream inHandle) {
        int from, to, evt;

        Assert.check(blockByte == REMOVED_DEST_ID || blockByte == REMOVED_EDGE_ID);
        try {
            // REMOVED_[EDGE|DEST]_ID is already read before the call.
            from = inHandle.readInt();
            to = inHandle.readInt();
            evt = inHandle.readInt();
        } catch (IOException ex) {
            String msg = "Failed to read removed edge information from the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
        return new RemovedEdgeInfo(from, to, evt, blockByte == REMOVED_EDGE_ID);
    }

    /**
     * Write an end-of-data (EOD) marker to the synthesis dump file, denoting the end of the data file has been reached.
     *
     * @param outHandle Output stream to write to.
     */
    public static void writeEndOfData(DataOutputStream outHandle) {
        try {
            outHandle.write(EOD_ID);
        } catch (IOException ex) {
            String msg = "Failed to write an end-of-data to the synthesis dump file.";
            throw new InputOutputException(msg, ex);
        }
    }
}
