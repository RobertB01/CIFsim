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

package org.eclipse.escet.cif.cif2yed.options;

/** The kind of diagram to generate. */
public enum DiagramKind {
    /** Diagram of the model. */
    MODEL,

    /** Diagram of relations. */
    RELATIONS;

    /**
     * Returns a textual description of the kind of diagram.
     *
     * @return A textual description, of the form {@code "... diagram"}, starting with a capital letter.
     */
    public String getDescription() {
        switch (this) {
            case MODEL:
                return "Model diagram";
            case RELATIONS:
                return "Relations diagram";
        }
        throw new RuntimeException("Unknown diagram kind: " + this);
    }
}
