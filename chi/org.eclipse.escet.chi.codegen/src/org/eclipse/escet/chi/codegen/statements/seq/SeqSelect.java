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

package org.eclipse.escet.chi.codegen.statements.seq;

import static org.eclipse.escet.chi.codegen.Constants.INDENT_SIZE;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Select statement in sequential language.
 *
 * <p>
 * Also used for send/receive/delay/run/finish statements.
 * </p>
 */
public class SeqSelect extends Seq {
    /** Class for storing the information of a single alternative. */
    public static class SelectAlternative {
        /** Unique number of this alternative. */
        public final int number;

        /** Code to construct select alternatives at run time. */
        public final List<Seq> createCode;

        /** Code blob associated with the selection. May be {@code null} */
        public final SeqList code;

        /** Variables to assign a value prior to execution. May be {@code null}. */
        public final List<VariableDeclaration> varParms;

        /**
         * Alternative in a selection/send/receive/delay/run statement.
         *
         * @param number Unique number of the alternative (statically decided).
         * @param createCode Code needed to compute the alternatives for this case.
         * @param code Blob of code associated with the alternative. May be {@code null}.
         * @param varParms List of variable to change prior to executing anything of this alternative. May be
         *     {@code null}.
         */
        public SelectAlternative(int number, List<Seq> createCode, SeqList code, List<VariableDeclaration> varParms) {
            this.number = number;
            this.createCode = createCode;
            this.code = code;
            this.varParms = varParms;
        }
    }

    /** Selection alternatives (static blobs of code). */
    public final List<SelectAlternative> alternatives;

    /**
     * Select/send/receive/delay/run statement.
     *
     * @param alternatives Selection alternatives (just one perhaps).
     * @param stat Original source statement.
     */
    public SeqSelect(List<SelectAlternative> alternatives, PositionObject stat) {
        super(stat);
        this.alternatives = alternatives;
    }

    @Override
    public Box boxify() {
        VBox vb = new VBox(0);
        for (SelectAlternative sa: alternatives) {
            for (Seq seq: sa.createCode) {
                vb.add(seq.boxify());
            }
        }
        vb.add("choice = sim->selectAlternative(selectList);");
        vb.add("switch (choice) {");
        for (SelectAlternative sa: alternatives) {
            VBox caseText = new VBox(INDENT_SIZE);
            caseText.add(fmt("case %d:", sa.number));
            VBox child = new VBox(INDENT_SIZE);
            if (sa.code != null) {
                child.add(sa.code.boxify());
            }
            child.add("break;");
            caseText.add(child);
            vb.add(caseText);
        }
        vb.add("}");
        return vb;
    }
}
