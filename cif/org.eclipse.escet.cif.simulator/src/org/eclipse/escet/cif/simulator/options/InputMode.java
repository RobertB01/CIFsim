//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.cif.simulator.input.AutomaticInputComponent;
import org.eclipse.escet.cif.simulator.input.InteractiveConsoleInputComponent;
import org.eclipse.escet.cif.simulator.input.InteractiveGuiInputComponent;
import org.eclipse.escet.cif.simulator.input.SvgInputComponent;
import org.eclipse.escet.cif.simulator.input.trace.TraceInputComponent;

/** Input modes. */
public enum InputMode {
    /**
     * Interactive console input mode (deprecated). Use {@link #CONSOLE} instead. Kept for backward compatibility, and
     * re-mapped to {@link #CONSOLE} by the {@link InputModeOption}.
     */
    INTERACTIVE,

    /**
     * Interactive console input mode. The user is asked to choose, via the console.
     *
     * @see InteractiveConsoleInputComponent
     */
    CONSOLE,

    /**
     * Interactive GUI input mode. The user is asked to choose, via a GUI.
     *
     * @see InteractiveGuiInputComponent
     */
    GUI,

    /**
     * Automatic input mode. The simulator chooses by itself.
     *
     * @see AutomaticInputComponent
     * @see AutoAlgoOption
     */
    AUTO,

    /**
     * Trace input mode. A trace file specifies which transitions to choose.
     *
     * @see TraceInputComponent
     * @see TraceInputFileOption
     */
    TRACE,

    /**
     * SVG input mode.
     *
     * @see SvgInputComponent
     */
    SVG;
}
