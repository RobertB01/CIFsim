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
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcDataVariable;
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
import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructField;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
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
        assertEquals("1", toStr(new PlcIntLiteral(1, PlcElementaryType.DINT_TYPE)));
        assertEquals("-1", toStr(new PlcIntLiteral(-1, PlcElementaryType.DINT_TYPE)));
        assertEquals("0", toStr(new PlcIntLiteral(0, PlcElementaryType.DINT_TYPE)));

        // Reals.
        assertEquals("1.390579367798679", toStr(new PlcRealLiteral("1.390579367798679", PlcElementaryType.LREAL_TYPE)));
        assertEquals("1.39e-15", toStr(new PlcRealLiteral("1.39e-15", PlcElementaryType.LREAL_TYPE)));
        assertEquals("1.0e-15", toStr(new PlcRealLiteral("1e-15", PlcElementaryType.LREAL_TYPE))); // Inserts a ".0".
        assertEquals("39.0", toStr(new PlcRealLiteral("39", PlcElementaryType.LREAL_TYPE))); // Appends a ".0".

        // Arrays.
        assertEquals("[0, 1, 2]",
                toStr(new PlcArrayLiteral(List.of(new PlcIntLiteral(0, PlcElementaryType.DINT_TYPE),
                        new PlcIntLiteral(1, PlcElementaryType.DINT_TYPE),
                        new PlcIntLiteral(2, PlcElementaryType.DINT_TYPE)))));

        // Structures.
        PlcStructType sType = new PlcStructType("sType", List.of(new PlcStructField("a", PlcElementaryType.DINT_TYPE),
                new PlcStructField("b", PlcElementaryType.BOOL_TYPE)));
        assertEquals("(a := 3, b := TRUE)",
                toStr(new PlcStructLiteral(
                        List.of(new PlcNamedValue("a", new PlcIntLiteral(3, PlcElementaryType.DINT_TYPE)),
                                new PlcNamedValue("b", new PlcBoolLiteral(true))),
                        sType)));

        // Variables.
        PlcType arr5 = new PlcArrayType(0, 6, PlcElementaryType.BOOL_TYPE);
        PlcType arr35 = new PlcArrayType(0, 4, arr5);
        PlcType struct35 = new PlcStructType("SomStruct", List.of(new PlcStructField("abc", arr35)));
        PlcType arr7Struct35 = new PlcArrayType(0, 8, struct35);
        PlcBasicVariable aVar = new PlcDataVariable("a", arr7Struct35);
        PlcArrayProjection arrayProj7 = new PlcArrayProjection(new PlcIntLiteral(7, PlcElementaryType.DINT_TYPE));
        PlcStructProjection structProj = new PlcStructProjection("abc");
        PlcArrayProjection arrayProj3 = new PlcArrayProjection(new PlcIntLiteral(3, PlcElementaryType.DINT_TYPE));
        PlcArrayProjection arrayProj5 = new PlcArrayProjection(new PlcIntLiteral(5, PlcElementaryType.DINT_TYPE));

        assertEquals("a", toStr(new PlcVarExpression(aVar)));
        assertEquals("a[7].abc[3][5]",
                toStr(new PlcVarExpression(aVar, List.of(arrayProj7, structProj, arrayProj3, arrayProj5))));

        // Function application is tested in conversions.FuncApplsTest.
    }
}
