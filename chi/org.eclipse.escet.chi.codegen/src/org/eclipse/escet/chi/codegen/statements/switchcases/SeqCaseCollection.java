//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen.statements.switchcases;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

/** Number of {@link SeqCase} blocks, and an entry point. */
public class SeqCaseCollection {
    /** Cases of the switch. Order is critical as cases may fall through to the next case. */
    public final List<SeqCase> cases;

    /** Entry point of the cases. */
    public final int entry;

    /**
     * Construct a {@link SeqCaseCollection} class with one switch case.
     *
     * @param swCase Switch case to use.
     * @param entry Entry point number of the collection.
     */
    public SeqCaseCollection(SeqCase swCase, int entry) {
        this(list(swCase), entry);
    }

    /**
     * Construct a {@link SeqCaseCollection} class with multiple switch case.
     *
     * @param cases Switch cases to use.
     * @param entry Entry point number of the collection.
     */
    public SeqCaseCollection(List<SeqCase> cases, int entry) {
        this.cases = cases;
        this.entry = entry;
    }
}
