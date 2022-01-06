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

package org.eclipse.escet.cif.simulator.output;

/** Type of normal console output. */
public enum NormalOutputType {
    /** Initial state. */
    STATE_INIT("initial state"),

    /** Target states (includes the final/deadlock state). */
    STATE_TARGET("target states (includes the final/deadlock state)"),

    /** Final state (includes the deadlock state). */
    STATE_FINAL("final state (includes the deadlock state)"),

    /** Deadlock state. */
    STATE_DEADLOCK("deadlock state"),

    /** Intermediate states/frames. */
    STATE_INTERMEDIATE("intermediate states/frames"),

    /** Algebraic variables as part of the state. */
    STATE_ALG_VARS("algebraic variables as part of the state"),

    /** Derivatives as part of the state. */
    STATE_DERIVS("derivatives as part of the state"),

    /** Possible transitions (for interactive console choice only). */
    TRANS_MINIMAL("possible transitions (for interactive console choice only)"),

    /** Possible transitions (for interactive console choice, or if more than one transition). */
    TRANS_DEFAULT("possible transitions (for interactive console choice, or if more than one transition)"),

    /** Possible transitions (always). */
    TRANS_ALWAYS("possible transitions (always)"),

    /** Chosen transitions. */
    CHOSEN_TRANS("chosen transitions"),

    /** Interrupted transitions. */
    INTERRUPTED_TRANS("interrupted transitions"),

    /** Simulation result. */
    SIM_RSLT("simulation result"),

    /** Random seeds used for the random generators. */
    SEEDS("random seeds used for the random generators"),

    /** Print declarations output. */
    PRINT("print declarations output");

    /**
     * The description of the type of normal output, for the help texts. They are used as follows:
     * <ul>
     * <li>For the option dialog check box texts: {@code "The " + description}</li>
     * <li>For the command line argument help text: {@code "... to print the " + description}</li>
     * </ul>
     */
    public final String description;

    /**
     * Constructor for the {@link NormalOutputType} enumeration.
     *
     * @param description The description of the type of normal output, for the help texts.
     */
    private NormalOutputType(String description) {
        this.description = description;
    }
}
