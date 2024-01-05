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

package org.eclipse.escet.chi.runtime.data;

/** Kind of definition being executed. */
public enum DefinitionKind {
    /** Experiment. */
    XPER("xper"),

    /** Model. */
    MODEL("model"),

    /** Process. */
    PROCESS("process"),

    /** Function. */
    FUNCTION("function");

    /** Textual description of the kind. */
    public final String name;

    /**
     * Constructor of the {@link DefinitionKind} class.
     *
     * @param name Textual description of the kind.
     */
    private DefinitionKind(String name) {
        this.name = name;
    }
}
