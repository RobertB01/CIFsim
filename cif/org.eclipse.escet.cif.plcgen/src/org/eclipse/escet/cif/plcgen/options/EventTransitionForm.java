//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.options;

/**
 * The available forms of the code for an event transition.
 *
 * <p>
 * The main program repeats trying to perform events until they are all blocked.
 * This enumeration defines where the code of each transition is kept.
 * </p>
 */
public enum EventTransitionForm {
    /** PLC code of an event transition is inlined in the main program. */
    CODE_IN_MAIN("code-in-main",
            "the main program contains all event transitions code",
            "All event code in main program"),

    /** PLC code of an event transition is stored in a POU. */
    CODE_IN_FUNCTION("code-in-function",
            "each event transition code has its own function and the main program calls the functions",
            "Each event in a separate function");

    /** Descriptive name of the option value. */
    public final String name;

    /** Short description of the value. */
    public final String shortDescription;

    /** Description of the transition form. */
    public final String description;

    /**
     * Constructor of the {@link EventTransitionForm}.
     *
     * @param name Descriptive name of the option value.
     * @param shortDescription Short description of the value
     * @param description Longer description of the value.
     */
    private EventTransitionForm(String name, String shortDescription, String description) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
    }
}
