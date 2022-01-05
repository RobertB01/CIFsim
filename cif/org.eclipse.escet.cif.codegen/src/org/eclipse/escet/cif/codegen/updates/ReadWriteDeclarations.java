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

package org.eclipse.escet.cif.codegen.updates;

import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

/** Declarations that are written and/or read. */
public class ReadWriteDeclarations {
    /** Declarations that are read. */
    public final Set<VariableWrapper> read;

    /** Declarations that are written. */
    public final Set<VariableWrapper> written;

    /** Constructor of the {@link ReadWriteDeclarations} class. */
    public ReadWriteDeclarations() {
        this(set(), set());
    }

    /**
     * Copy constructor of the {@link ReadWriteDeclarations} class.
     *
     * @param rwDecls Read/write declarations to copy.
     */
    public ReadWriteDeclarations(ReadWriteDeclarations rwDecls) {
        this(copy(rwDecls.read), copy(rwDecls.written));
    }

    /**
     * Constructor of the {@link ReadWriteDeclarations} class.
     *
     * @param read Declarations that are read.
     * @param written Declarations that are written.
     */
    public ReadWriteDeclarations(Set<VariableWrapper> read, Set<VariableWrapper> written) {
        this.read = read;
        this.written = written;
    }

    /**
     * Merge the given read/written accesses into the object.
     *
     * @param rwDecls Read/write accesses to merge.
     */
    public void addAll(ReadWriteDeclarations rwDecls) {
        read.addAll(rwDecls.read);
        written.addAll(rwDecls.written);
    }
}
