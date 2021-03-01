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
import org.eclipse.escet.chi.metamodel.chi.WhileStatement;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;

/** While loop in sequential language. */
public class SeqWhile extends Seq {
    /** While condition. */
    public final ExpressionBase condition;

    /** Child statements. */
    public final SeqList childs;

    /**
     * Constructor of the {@link SeqWhile} class.
     *
     * @param condition Boolean guard of the while.
     * @param childs Statements in the body of the while.
     * @param wstat Original while statement.
     */
    public SeqWhile(ExpressionBase condition, SeqList childs, WhileStatement wstat) {
        super(wstat);
        this.condition = condition;
        this.childs = childs;
    }

    @Override
    public Box boxify() {
        VBox vb = new VBox(0);
        vb.add("while (ALWAYS) {");

        VBox cb = new VBox(INDENT_SIZE);
        condition.setCurrentPositionStatement(cb);
        if (!condition.getCode().isEmpty()) {
            cb.add(condition.getCode());
        }
        cb.add("if (!(" + condition.getValue() + ")) break;");
        cb.add("if (NEVER) break;");
        cb.add("chiCoordinator.testTerminating();");
        cb.add(childs.boxify());

        vb.add(cb);
        vb.add("}");
        return vb;
    }
}
