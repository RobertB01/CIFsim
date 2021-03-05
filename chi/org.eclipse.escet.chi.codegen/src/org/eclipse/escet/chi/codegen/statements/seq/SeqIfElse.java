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

import static org.eclipse.escet.chi.codegen.Constants.INDENT_SIZE;

import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.metamodel.chi.IfCase;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.MultiLineTextBox;
import org.eclipse.escet.common.box.VBox;

/** If/else statement in sequential language. */
public class SeqIfElse extends Seq {
    /** If condition. */
    public final ExpressionBase condition;

    /** If-child statements. */
    public final SeqList ifChilds;

    /** Else-child statements (may be {@code null}). */
    public final SeqList elseChilds;

    /**
     * If/else sequential statement.
     *
     * @param condition Condition expression for deciding which branch to take.
     * @param ifChilds Statements to execute if the condition holds.
     * @param elseChilds Statements to execute if the condition does not hold. May be {@code null}.
     * @param ifCase Chi statement associated with the if/else, for position information.
     */
    public SeqIfElse(ExpressionBase condition, SeqList ifChilds, SeqList elseChilds, IfCase ifCase) {
        super(ifCase);
        this.condition = condition;
        this.ifChilds = ifChilds;
        this.elseChilds = elseChilds;
    }

    @Override
    public Box boxify() {
        VBox stat = new VBox(0);
        if (!condition.getCode().isEmpty()) {
            condition.setCurrentPositionStatement(stat);
            stat.add(new MultiLineTextBox(condition.getCode()));
        }
        stat.add("if (" + condition.getValue() + ") {");

        VBox childs = new VBox(INDENT_SIZE);
        childs.add(ifChilds.boxify());
        stat.add(childs);

        if (elseChilds != null && !elseChilds.seqs.isEmpty()) {
            stat.add("} else {");
            childs = new VBox(INDENT_SIZE);
            childs.add(elseChilds.boxify());
            stat.add(childs);
        }
        stat.add("}");
        return stat;
    }
}
