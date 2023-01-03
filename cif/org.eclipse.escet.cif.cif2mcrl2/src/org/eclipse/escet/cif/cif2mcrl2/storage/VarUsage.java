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

/** Usage of a variable for read and for write. */
public class VarUsage {
    /** Variable being examined. */
    public final VariableData var;

    /** Read access of the variable. */
    public VarAccess readAccess = new VarAccess();

    /** Write access of the variable. */
    public VarAccess writeAccess = new VarAccess();

    /**
     * Constructor of the {@link VarUsage} class.
     *
     * @param var Variable being examined.
     */
    public VarUsage(VariableData var) {
        this.var = var;
    }

    /**
     * Make a fresh copy of the object.
     *
     * @return The fresh copy of the object.
     */
    public VarUsage copy() {
        VarUsage vu = new VarUsage(var);
        vu.readAccess = readAccess.copy();
        vu.writeAccess = writeAccess.copy();
        return vu;
    }

    /**
     * Merge the other variable usage into self. Goal is to find as much variable usage as possible.
     *
     * @param other Other variable access to merge into self.
     */
    public void merge(VarUsage other) {
        readAccess.merge(other.readAccess);
        writeAccess.merge(other.writeAccess);
    }
}
