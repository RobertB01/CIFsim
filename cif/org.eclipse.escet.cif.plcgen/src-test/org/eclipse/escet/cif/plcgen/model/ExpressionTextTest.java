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

import java.util.List;

import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcArrayLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcStructLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcArrayProjection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcStructProjection;
import org.junit.jupiter.api.Test;

/** Pretty-print expressions test. */
public class ExpressionTextTest {
    /** Converter of expressions and statements to text. */
    private final ModelTextGenerator textGen = new ModelTextGenerator();

    /**
     * Convert an expression to text.
     *
     * @param expr Expression to convert.
     * @return The generated text of the expression.
     */
    private String toStr(PlcExpression expr) {
        return textGen.toString(expr);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void leafsPrintingTest() {
        // Booleans.
        assertEquals("TRUE", toStr(new PlcBoolLiteral(true)));
        assertEquals("FALSE", toStr(new PlcBoolLiteral(false)));

        // Integers.
        assertEquals("1", toStr(new PlcIntLiteral(1)));
        assertEquals("-1", toStr(new PlcIntLiteral(-1)));
        assertEquals("0", toStr(new PlcIntLiteral(0)));

        // Reals.
        assertEquals("1.390579367798679", toStr(new PlcRealLiteral("1.390579367798679")));
        assertEquals("1.39e-15", toStr(new PlcRealLiteral("1.39e-15")));
        assertEquals("1.0e-15", toStr(new PlcRealLiteral("1e-15"))); // Inserts a ".0".
        assertEquals("39.0", toStr(new PlcRealLiteral("39"))); // Appends a ".0".

        // Arrays.
        assertEquals("[0, 1, 2]",
                toStr(new PlcArrayLiteral(List.of(new PlcIntLiteral(0), new PlcIntLiteral(1), new PlcIntLiteral(2)))));

        // Structures.
        assertEquals("(a := 3, b := TRUE)", toStr(new PlcStructLiteral(List
                .of(new PlcNamedValue("a", new PlcIntLiteral(3)), new PlcNamedValue("b", new PlcBoolLiteral(true))))));

        // Variables.
        PlcVariable aVar = new PlcVariable("a", null);
        PlcArrayProjection arrayProj = new PlcArrayProjection(List.of(new PlcIntLiteral(7)));
        PlcStructProjection structProj = new PlcStructProjection("abc");
        PlcArrayProjection multiArrayProj = new PlcArrayProjection(List.of(new PlcIntLiteral(3), new PlcIntLiteral(5)));

        assertEquals("a", toStr(new PlcVarExpression(aVar)));
        assertEquals("a[7].abc[3, 5]",
                toStr(new PlcVarExpression(aVar, List.of(arrayProj, structProj, multiArrayProj))));

        // Function application is tested in conversions.FuncApplsTest.
    }
}
