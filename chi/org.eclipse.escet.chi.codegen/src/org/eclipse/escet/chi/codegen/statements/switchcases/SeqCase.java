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

package org.eclipse.escet.chi.codegen.statements.switchcases;

import java.util.List;

import org.eclipse.escet.chi.codegen.statements.seq.Seq;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;

/** A blob of sequential language code that can be executed without need for interruptions. */
public class SeqCase {
    /** Unique number of this case for the switch. */
    public final int caseNumber;

    /** Sequential language statements of this case. */
    public List<Seq> statements;

    /**
     * Switch case constructor.
     *
     * <p>
     * Note that the caller should ensure the code can be executed without interruptions.
     * </p>
     *
     * @param caseNumber Number of this case in the switch.
     * @param statements Statements of this case.
     */
    public SeqCase(int caseNumber, List<Seq> statements) {
        this.caseNumber = caseNumber;
        this.statements = statements;
    }

    /**
     * Convert the body to box format.
     *
     * @return The switch cases in box format.
     */
    public Box boxify() {
        VBox vb = new VBox(0);
        for (Seq s: statements) {
            vb.add(s.boxify());
        }
        return vb;
    }
}
