//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcReturnStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement.PlcSelectChoice;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.junit.jupiter.api.Test;

/** Tests of the conversion of PLC statements to text. */
public class StatementTextTest {
    /** Name of the surrounding POU in the tests. */
    private static final String POU_NAME = "testPou";

    /** Converter of expressions and statements to text. */
    private final ModelTextGenerator textGen = new ModelTextGenerator();

    /**
     * Convert an expression to text.
     *
     * @param stat Statement to convert.
     * @return The generated text of the statement.
     */
    private String toStr(PlcStatement stat) {
        return toStr(List.of(stat), true);
    }

    /**
     * Convert an expression to text.
     *
     * @param stats Statements to convert.
     * @param fixCodeBlock Whether to insert an empty statement in a code block if necessary.
     * @return The generated text of the statements.
     */
    private String toStr(List<PlcStatement> stats, boolean fixCodeBlock) {
        return textGen.toString(stats, POU_NAME, fixCodeBlock);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void emptyBlockFixTest() {
        assertEquals("(* Nothing to do. *) ;", toStr(List.of(), true));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void emptyBlockNonfixCrashTest() {
        assertThrows(AssertionError.class, () -> toStr(List.of(), false));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void commentStatementTest() {
        assertEquals("(* A comment. *)\n(* Nothing to do. *) ;", toStr(new PlcCommentLine("A comment.")));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void valueReturnStatementTest() {
        PlcExpression trueValue = new PlcBoolLiteral(true);
        assertEquals(POU_NAME + " := TRUE;\nRETURN;", toStr(new PlcReturnStatement(trueValue)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void voidReturnStatementTest() {
        assertEquals("RETURN;", toStr(new PlcReturnStatement(null)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void assignmentStatementTest() {
        PlcVarExpression lhs = new PlcVarExpression(new PlcDataVariable("my_var", PlcElementaryType.BOOL_TYPE));
        PlcExpression trueValue = new PlcBoolLiteral(true);
        assertEquals("my_var := TRUE;", toStr(new PlcAssignmentStatement(lhs, trueValue)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void emptySelectionStatementTest() {
        // Completely empty selection statement.
        assertEquals("(* Nothing to do. *) ;", toStr(new PlcSelectionStatement(List.of(), List.of())));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void elseSelectionStatementTest() {
        // Selection statement with only an ELSE block.
        PlcBasicVariable v = new PlcDataVariable("v", PlcElementaryType.DINT_TYPE);
        PlcStatement selStat = new PlcSelectionStatement(List.of(),
                List.of(new PlcAssignmentStatement(new PlcVarExpression(v),
                        new PlcIntLiteral(0, PlcElementaryType.DINT_TYPE)),
                        new PlcAssignmentStatement(new PlcVarExpression(v),
                                new PlcIntLiteral(1, PlcElementaryType.DINT_TYPE))));
        assertEquals("""
                v := 0;
                v := 1;""", toStr(selStat));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void ifSelectionStatementTest() {
        PlcBasicVariable v = new PlcDataVariable("v", PlcElementaryType.DINT_TYPE);
        PlcSelectChoice ifChoice = new PlcSelectChoice(new PlcBoolLiteral(false),
                List.of(new PlcAssignmentStatement(new PlcVarExpression(v),
                        new PlcIntLiteral(0, PlcElementaryType.DINT_TYPE)),
                        new PlcAssignmentStatement(new PlcVarExpression(v),
                                new PlcIntLiteral(1, PlcElementaryType.DINT_TYPE))));
        PlcStatement selStat = new PlcSelectionStatement(List.of(ifChoice), List.of());
        assertEquals("""
                IF FALSE THEN
                    v := 0;
                    v := 1;
                END_IF;""", toStr(selStat));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void ifElifElseSelectionStatementTest() {
        PlcBasicVariable v = new PlcDataVariable("v", PlcElementaryType.DINT_TYPE);
        PlcSelectChoice ifChoice = new PlcSelectChoice(new PlcBoolLiteral(false),
                List.of(new PlcAssignmentStatement(new PlcVarExpression(v),
                        new PlcIntLiteral(0, PlcElementaryType.DINT_TYPE))));
        PlcSelectChoice elifChoice = new PlcSelectChoice(new PlcBoolLiteral(true),
                List.of(new PlcAssignmentStatement(new PlcVarExpression(v),
                        new PlcIntLiteral(1, PlcElementaryType.DINT_TYPE))));
        PlcStatement selStat = new PlcSelectionStatement(List.of(ifChoice, elifChoice),
                List.of(new PlcAssignmentStatement(new PlcVarExpression(v),
                        new PlcIntLiteral(2, PlcElementaryType.DINT_TYPE))));
        assertEquals("""
                IF FALSE THEN
                    v := 0;
                ELSIF TRUE THEN
                    v := 1;
                ELSE
                    v := 2;
                END_IF;""", toStr(selStat));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void ifEmptyElifElseSelectionStatementTest() {
        PlcBasicVariable v = new PlcDataVariable("v", PlcElementaryType.DINT_TYPE);
        PlcSelectChoice ifChoice = new PlcSelectChoice(new PlcBoolLiteral(false),
                List.of(new PlcAssignmentStatement(new PlcVarExpression(v),
                        new PlcIntLiteral(0, PlcElementaryType.DINT_TYPE))));
        PlcSelectChoice elifChoice = new PlcSelectChoice(new PlcBoolLiteral(true), List.of());
        PlcStatement selStat = new PlcSelectionStatement(List.of(ifChoice, elifChoice),
                List.of(new PlcAssignmentStatement(new PlcVarExpression(v),
                        new PlcIntLiteral(1, PlcElementaryType.DINT_TYPE))));
        assertEquals("""
                IF FALSE THEN
                    v := 0;
                ELSIF TRUE THEN
                    (* Nothing to do. *) ;
                ELSE
                    v := 1;
                END_IF;""", toStr(selStat));
    }
}
