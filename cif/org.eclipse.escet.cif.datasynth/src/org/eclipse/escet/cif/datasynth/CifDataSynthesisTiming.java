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

package org.eclipse.escet.cif.datasynth;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.box.GridBox.GridBoxLayout;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Stopwatch;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.output.DebugNormalOutput;

/** Timing measurement data for CIF data-based synthesis. */
public class CifDataSynthesisTiming {
    /** Total tool execution, timing measurement. */
    public Stopwatch total = new Stopwatch();

    /** Input model read, timing measurement. */
    public Stopwatch inputRead = new Stopwatch();

    /** Input model preprocessing (including precondition checks), timing measurement. */
    public Stopwatch inputPreProcess = new Stopwatch();

    /** CIF to internal format input conversion, timing measurement. */
    public Stopwatch inputConvert = new Stopwatch();

    /** Pre synthesis, timing measurement. */
    public Stopwatch preSynth = new Stopwatch();

    /** Synthesis main loop, timing measurement. */
    public Stopwatch main = new Stopwatch();

    /** Synthesis main loop, backward marked, timing measurement. */
    public Stopwatch mainBwMarked = new Stopwatch();

    /** Synthesis main loop, backward bad-state, timing measurement. */
    public Stopwatch mainBwBadState = new Stopwatch();

    /** Synthesis main loop, forward initial, timing measurement. */
    public Stopwatch mainFwInit = new Stopwatch();

    /** Post synthesis, timing measurement. */
    public Stopwatch postSynth = new Stopwatch();

    /** Internal format to CIF output conversion, timing measurement. */
    public Stopwatch outputConvert = new Stopwatch();

    /** Output model write, timing measurement. */
    public Stopwatch outputWrite = new Stopwatch();

    /**
     * Prints the timing statistics to the console.
     *
     * @param indentAmount The number of spaces of indentation to use.
     * @param debugOutput Callback for debug output.
     * @param normalOutput Callback for normal output.
     */
    public void print(int indentAmount, DebugNormalOutput debugOutput, DebugNormalOutput normalOutput) {
        // Get prefixes and stopwatches.
        String indent = Strings.spaces(indentAmount);
        List<String> prefixes = list();
        List<Stopwatch> stopwatches = list();
        if (total.hasMeasured()) {
            prefixes.add("Total");
            stopwatches.add(total);
        }
        if (inputRead.hasMeasured()) {
            prefixes.add(indent + "Read input model");
            stopwatches.add(inputRead);
        }
        if (inputPreProcess.hasMeasured()) {
            prefixes.add(indent + "Preprocess and check input model");
            stopwatches.add(inputPreProcess);
        }
        if (inputConvert.hasMeasured()) {
            prefixes.add(indent + "Convert input model");
            stopwatches.add(inputConvert);
        }
        if (preSynth.hasMeasured()) {
            prefixes.add(indent + "Pre synthesis");
            stopwatches.add(preSynth);
        }
        if (main.hasMeasured()) {
            prefixes.add(indent + "Main synthesis loop");
            stopwatches.add(main);
        }
        if (mainBwMarked.hasMeasured()) {
            prefixes.add(indent + indent + "Backward marking");
            stopwatches.add(mainBwMarked);
        }
        if (mainBwBadState.hasMeasured()) {
            prefixes.add(indent + indent + "Backward bad-state");
            stopwatches.add(mainBwBadState);
        }
        if (mainFwInit.hasMeasured()) {
            prefixes.add(indent + indent + "Forward initial");
            stopwatches.add(mainFwInit);
        }
        if (postSynth.hasMeasured()) {
            prefixes.add(indent + "Post synthesis");
            stopwatches.add(postSynth);
        }
        if (outputConvert.hasMeasured()) {
            prefixes.add(indent + "Convert output model");
            stopwatches.add(outputConvert);
        }
        if (outputWrite.hasMeasured()) {
            prefixes.add(indent + "Write output model");
            stopwatches.add(outputWrite);
        }
        Assert.check(prefixes.size() == stopwatches.size());

        // Get measurements.
        String[] measurements = new String[stopwatches.size()];
        for (int i = 0; i < stopwatches.size(); i++) {
            Assert.check(!stopwatches.get(i).isRunning());
            measurements[i] = fmt("%,.0f ms", stopwatches.get(i).getDurationMillis());
        }

        // Fill grid, except timing data.
        GridBox grid = new GridBox(measurements.length + 2, 2, 0, 2);
        grid.set(0, 0, "Timing measurement");
        grid.set(0, 1, "Duration");
        for (int i = 0; i < prefixes.size(); i++) {
            grid.set(i + 2, 0, prefixes.get(i));
            grid.set(i + 2, 1, measurements[i]);
        }

        // Fill separation rows.
        GridBoxLayout layout = grid.computeLayout();
        for (int i = 0; i < layout.numCols; i++) {
            String bar = Strings.duplicate("-", layout.widths[i]);
            grid.set(1, i, bar);
        }

        // Right align timing column.
        int timingColWidth = layout.widths[1];
        for (int i = 0; i < measurements.length; i++) {
            String txt = measurements[i];
            String empty = Strings.spaces(timingColWidth - txt.length());
            grid.set(i + 2, 1, empty + txt);
        }

        // Separate from debug output. There is no other 'normal' output.
        debugOutput.line();

        // Print.
        for (String line: grid.getLines()) {
            normalOutput.line(line);
        }
    }
}
