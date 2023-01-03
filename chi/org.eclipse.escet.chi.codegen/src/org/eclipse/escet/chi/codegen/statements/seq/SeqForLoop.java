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

package org.eclipse.escet.chi.codegen.statements.seq;

import static org.eclipse.escet.chi.codegen.Constants.INDENT_SIZE;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.MultiLineTextBox;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * For loop in sequential language. It uses Java/C semantics for the end condition and increment step, that is both are
 * evaluated each iteration.
 */
public class SeqForLoop extends Seq {
    /** Initialization expression, may be {@code null}. */
    public final ExpressionBase init;

    /** End condition, may be {@code null}. */
    public final ExpressionBase endCondition;

    /** Increment expression, may be {@code null}. */
    public final ExpressionBase increment;

    /** Child statements. */
    public final SeqList childStats;

    /**
     * For statement (may get translated to a while statement depending on complexity of the {@link #endCondition} and
     * {@link #increment} expressions).
     *
     * @param init Optional initialization expression, may be {@code null}.
     * @param endCondition Optional end condition expression, may be {@code null}.
     * @param increment Optional increment expression, may be {@code null}.
     * @param childStats Statements of the body.
     * @param stat Source statement.
     */
    public SeqForLoop(ExpressionBase init, ExpressionBase endCondition, ExpressionBase increment, SeqList childStats,
            PositionObject stat)
    {
        super(stat);
        this.init = init;
        this.endCondition = endCondition;
        this.increment = increment;
        this.childStats = childStats;
    }

    @Override
    public Box boxify() {
        // If the code is too complicated for a 'for', use a 'while'.
        boolean useWhile = (endCondition != null && !endCondition.getCode().isEmpty());

        // Construct the child statement box.
        Box childs = childStats.boxify();
        VBox vb = new VBox(INDENT_SIZE);
        if (useWhile && endCondition != null) {
            if (!endCondition.getCode().isEmpty()) {
                endCondition.setCurrentPositionStatement(vb);
                vb.add(new MultiLineTextBox(endCondition.getCode()));
            }
            String endCheck = fmt("if (!(%s)) break;", endCondition.getValue());
            vb.add(endCheck);
        }
        vb.add(childs);
        if (increment != null && !increment.getCode().isEmpty()) {
            increment.setCurrentPositionStatement(vb);
            vb.add(new MultiLineTextBox(increment.getCode()));
            if (useWhile) {
                vb.add(new TextBox(increment.getValue()));
            }
        }
        childs = vb;

        // Construct the box of the loop.
        vb = new VBox(0);
        if (init != null && !init.getCode().isEmpty()) {
            init.setCurrentPositionStatement(vb);
            vb.add(new MultiLineTextBox(init.getCode()));
        }
        if (useWhile) {
            vb.add("while (true) {");
            vb.add("    if (NEVER) break;");
            vb.add("    chiCoordinator.testTerminating();");
            vb.add(childs);
            vb.add("}");
        } else {
            String iniText, endText, incText, forLine;
            iniText = (init != null) ? init.getValue() : "";
            endText = (endCondition != null) ? endCondition.getValue() : "";
            incText = (increment != null) ? increment.getValue() : "";
            forLine = fmt("for (%s;%s;%s) {", iniText, endText, incText);
            setCurrentPositionStatement(vb);
            vb.add(forLine);
            vb.add("    if (NEVER) break;");
            vb.add("    chiCoordinator.testTerminating();");
            vb.add(childs);
            setCurrentPositionStatement(vb);
            vb.add("}");
        }
        return vb;
    }
}
