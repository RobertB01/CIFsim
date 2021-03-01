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

package org.eclipse.escet.chi.codegen.statements.seq;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;

/** List of (child) statements in the sequential language. */
public class SeqList {
    /** List of child statements. */
    public List<Seq> seqs;

    /**
     * Constructor of the {@link SeqList} class.
     *
     * @param seqs Sequential statements to wrap in the list.
     */
    public SeqList(List<Seq> seqs) {
        this.seqs = (seqs == null) ? list() : seqs;
    }

    /**
     * Generate a {@link Box} representation of the statement list.
     *
     * @return The {@link Box} representation of the statement list.
     */
    public Box boxify() {
        VBox vb = new VBox(0);
        for (Seq seq: seqs) {
            vb.add(seq.boxify());
        }
        return vb;
    }
}
