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

/** The kind of relations to include in relations diagrams. */
public enum RelationKind {
    /** Event relations. */
    EVENT,

    /** Data relations. */
    DATA;

    /**
     * Returns a textual description of the kind of relations.
     *
     * @return A textual description, of the form {@code "... relations"}, starting with a capital letter.
     */
    public String getDescription() {
        switch (this) {
            case EVENT:
                return "Event relations";
            case DATA:
                return "Data relations";
        }
        throw new RuntimeException("Unknown relations kind: " + this);
    }
}
