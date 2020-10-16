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

import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.metamodel.chi.ExitStatement;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.MultiLineTextBox;
import org.eclipse.escet.common.box.VBox;

/** Exit statement in sequential language. */
public class SeqExit extends Seq {
    /** In case of a non-void exit, the returned value. */
    public final ExpressionBase value;

    /**
     * Constructor of the {@link SeqExit} class.
     *
     * @param value Exit value (if exit type is non-void, else {@code null}).
     * @param stat Chi exit statement.
     */
    public SeqExit(ExpressionBase value, ExitStatement stat) {
        super(stat);
        this.value = value;
    }

    @Override
    public Box boxify() {
        VBox vb = new VBox();
        String exitValue;

        setCurrentPositionStatement(vb);
        // Compute exit value.
        if (value == null) {
            exitValue = "null";
        } else {
            if (!value.getCode().isEmpty()) {
                vb.add(new MultiLineTextBox(value.getCode()));
            }
            exitValue = value.getValue();
        }
        vb.add("chiCoordinator.setTerminateAll(" + exitValue + ");");
        vb.add("if (ALWAYS) return RunResult.TERMINATED;");
        return vb;
    }
}
