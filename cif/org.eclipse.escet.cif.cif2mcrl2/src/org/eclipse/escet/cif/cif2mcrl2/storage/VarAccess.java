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

package org.eclipse.escet.cif.cif2mcrl2.storage;

/** How is a variable accessed with respect to read or write. */
public class VarAccess {
    /** Is the variable accessed (for read or write) at all? */
    public boolean everUsed = false;

    /** If it is {@link #everUsed}, are there edges that do not access it? */
    public boolean alwaysUsed = true;

    @Override
    public String toString() {
        if (!everUsed) {
            return "NEVER";
        }
        return alwaysUsed ? "ALWAYS" : "SOMETIMES";
    }

    /**
     * Make a fresh copy of the object.
     *
     * @return The fresh copy of the object.
     */
    public VarAccess copy() {
        VarAccess va = new VarAccess();
        va.everUsed = everUsed;
        va.alwaysUsed = alwaysUsed;
        return va;
    }

    /**
     * Merge the other variable access into self. Goal is to find as much variable access as possible.
     *
     * @param other Other variable access to merge into self.
     */
    public void merge(VarAccess other) {
        if (!other.everUsed) {
            return; // Cannot be better than self.
        }

        if (!everUsed) {
            // Other is used, self is not -> other is better.
            everUsed = other.everUsed;
            alwaysUsed = other.alwaysUsed;
            return;
        }

        // Both other and self are used.

        if (!other.alwaysUsed) {
            return; // Cannot be better than self.
        }

        if (!alwaysUsed) {
            // Other is always used, self is not -> other is better.
            alwaysUsed = other.alwaysUsed;
            return;
        }

        // Both always used -> Already equal.
        return;
    }
}
