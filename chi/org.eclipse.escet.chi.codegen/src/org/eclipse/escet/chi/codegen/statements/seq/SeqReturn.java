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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.metamodel.chi.ReturnStatement;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.MultiLineTextBox;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Return statement in sequential language. */
public class SeqReturn extends Seq {
    /** Reason of the 'return'. */
    private static enum ReturnReason {
        /** Function returns a value. */
        FUNC_RETURN,

        /** Process/model/xper goto. */
        PROC_GOTO,

        /** Process/model/xper finishes. */
        PROC_FINISH,

        /** Process/model blocks. */
        PROC_BLOCKS,
    }

    /** Type of return. */
    public final ReturnReason reason;

    /** In case of {@link ReturnReason#FUNC_RETURN}, the returned value. */
    public final ExpressionBase value;

    /** Jump destination of a {@link ReturnReason#PROC_GOTO} return. */
    public final int caseNumber;

    /**
     * Constructor of the {@link SeqReturn} class.
     *
     * @param position Position information of the statement.
     * @param reason Type of return.
     * @param value Value to return if 'reason' is {@link ReturnReason#FUNC_RETURN}.
     * @param caseNumber Jump destination of a {@link ReturnReason#PROC_GOTO} return.
     */
    protected SeqReturn(PositionObject position, ReturnReason reason, ExpressionBase value, int caseNumber) {
        super(position);
        this.reason = reason;
        this.value = value;
        this.caseNumber = caseNumber;
    }

    /**
     * Finishing a model, xper, or a process.
     *
     * @param chiObject Declaration (experiment, model, or process) that finishes.
     * @return The constructed sequential return statement.
     */
    public static SeqReturn finishProcess(PositionObject chiObject) {
        return new SeqReturn(chiObject, ReturnReason.PROC_FINISH, null, -1);
    }

    /**
     * Goto a different case.
     *
     * @param caseNumber Case number to jump to.
     * @param position Position information.
     * @return The constructed sequential return statement.
     */
    public static SeqReturn jumpToCase(int caseNumber, PositionObject position) {
        return new SeqReturn(position, ReturnReason.PROC_GOTO, null, caseNumber);
    }

    /**
     * Process block on a select.
     *
     * @param position Position of the select statement.
     * @return The constructed sequential return statement.
     */
    public static SeqReturn selectBlock(PositionObject position) {
        return new SeqReturn(position, ReturnReason.PROC_BLOCKS, null, 0);
    }

    /**
     * Function returns a value.
     *
     * @param val Returned value.
     * @param stat Original source statement.
     * @return The constructed sequential return statement.
     */
    public static SeqReturn funcReturn(ExpressionBase val, ReturnStatement stat) {
        return new SeqReturn(stat, ReturnReason.FUNC_RETURN, val, -1);
    }

    @Override
    public Box boxify() {
        switch (reason) {
            case PROC_GOTO:
                return generateGoto(caseNumber);

            case PROC_FINISH:
                return new TextBox("return RunResult.FINISHED;");

            case FUNC_RETURN: {
                VBox vb = new VBox(0);
                setCurrentPositionStatement(vb);

                if (!value.getCode().isEmpty()) {
                    vb.add(new MultiLineTextBox(value.getCode()));
                }
                vb.add("if (ALWAYS) {");
                vb.add("    if (positionStack != null) positionStack.remove(positionStack.size()-1);");
                vb.add("    return " + value.getValue() + ";");
                vb.add("}");
                return vb;
            }

            case PROC_BLOCKS:
                return new TextBox("return RunResult.BLOCKED;");

            default:
                Assert.fail("Unknown type of return.");
                return null;
        }
    }

    /**
     * Generate a 'goto' statement for Java.
     *
     * @param dest Destination to jump to.
     * @return Boxified representation of the jump.
     */
    public static Box generateGoto(int dest) {
        // TODO: Merge SeqBreak, SeqContinue, and SeqReturn into one class.
        String line = fmt("chiProgramCounter = %d; if (ALWAYS) break;", dest);
        return new TextBox(line);
    }
}
