//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output;

/** Type of debug console output. */
public enum DebugOutputType {
    /** CIF/SVG declarations. */
    SVG("CIF/SVG declarations"),

    /** Parser. */
    PARSER("parser"),

    /** Generated code. */
    GEN_CODE("generated code (by writing it to disk)"),

    /** ODE solver. */
    ODE("ODE solver");

    // TODO: Implement additional debugging output.
    /// ***/ INIT("initialization"),

    /// ***/ TRANS("transitions (minimal)"),
    /// ***/ TRANS_FULL("transitions (full)"),

    /**
     * The description of the type of debug output, for the help texts. They are used as follows:
     * <ul>
     * <li>For the option dialog check box texts: {@code "Debug the " + description}</li>
     * <li>For the command line argument help text: {@code "... to debug the " + description}</li>
     * </ul>
     */
    public final String description;

    /**
     * Constructor for the {@link DebugOutputType} enumeration.
     *
     * @param description The description of the type of debug output, for the help texts.
     */
    private DebugOutputType(String description) {
        this.description = description;
    }
}
