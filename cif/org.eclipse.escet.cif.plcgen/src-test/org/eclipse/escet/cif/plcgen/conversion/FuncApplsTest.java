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

package org.eclipse.escet.cif.plcgen.conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.targets.PlcOpenXmlTarget;
import org.junit.jupiter.api.Test;

/** Pretty-print function applications test. */
@SuppressWarnings("javadoc")
public class FuncApplsTest {
    /** Value {@code 1} expression. */
    private final PlcExpression num1 = new PlcIntLiteral(1, PlcElementaryType.DINT_TYPE);

    /** Value {@code 2} expression. */
    private final PlcExpression num2 = new PlcIntLiteral(2, PlcElementaryType.DINT_TYPE);

    /** Value {@code 3} expression. */
    private final PlcExpression num3 = new PlcIntLiteral(3, PlcElementaryType.DINT_TYPE);

    /** Value {@code false} expression. */
    private final PlcExpression bool0 = new PlcBoolLiteral(false);

    /** Value {@code true} expression. */
    private final PlcExpression bool1 = new PlcBoolLiteral(true);

    /** Value {@code 1.0} expression. */
    private final PlcExpression real1 = new PlcRealLiteral("1.0", PlcElementaryType.LREAL_TYPE);

    /** Value {@code 2.0} expression. */
    private final PlcExpression real2 = new PlcRealLiteral("2.0", PlcElementaryType.LREAL_TYPE);

    /** Function application generator. */
    private final PlcFunctionAppls funcAppls = new PlcFunctionAppls(new PlcOpenXmlTarget());

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
    public void negateFuncApplTest() {
        assertEquals("-3", toStr(funcAppls.negateFuncAppl(num3)));
    }

    @Test
    public void complementFuncApplTest() {
        assertEquals("NOT FALSE", toStr(funcAppls.complementFuncAppl(bool0)));
    }

    @Test
    public void powerFuncApplTest() {
        assertEquals("2.0 ** 1", toStr(funcAppls.powerFuncAppl(real2, num1)));
        assertEquals("2.0 ** 1.0", toStr(funcAppls.powerFuncAppl(real2, real1)));
    }

    @Test
    public void multiplyFuncApplTest() {
        assertEquals("1 * 2 * 3", toStr(funcAppls.multiplyFuncAppl(num1, num2, num3)));
        assertEquals("1.0 * 2.0", toStr(funcAppls.multiplyFuncAppl(real1, real2)));
    }

    @Test
    public void divideFuncApplTest() {
        assertEquals("1 / 2", toStr(funcAppls.divideFuncAppl(num1, num2)));
        assertEquals("1.0 / 2.0", toStr(funcAppls.divideFuncAppl(real1, real2)));
    }

    @Test
    public void moduloFuncApplTest() {
        assertEquals("1 MOD 2", toStr(funcAppls.moduloFuncAppl(num1, num2)));
    }

    @Test
    public void addFuncApplTest() {
        assertEquals("1 + 2 + 3", toStr(funcAppls.addFuncAppl(num1, num2, num3)));
        assertEquals("1.0 + 2.0", toStr(funcAppls.addFuncAppl(real1, real2)));
    }

    @Test
    public void subtractFuncApplTest() {
        assertEquals("1 - 2", toStr(funcAppls.subtractFuncAppl(num1, num2)));
        assertEquals("1.0 - 2.0", toStr(funcAppls.subtractFuncAppl(real1, real2)));
    }

    @Test
    public void lessEqualFuncApplTest() {
        assertEquals("1 <= 2", toStr(funcAppls.lessEqualFuncAppl(num1, num2)));
        assertEquals("1.0 <= 2.0", toStr(funcAppls.lessEqualFuncAppl(real1, real2)));
    }

    @Test
    public void lessThanFuncApplTest() {
        assertEquals("1 < 2", toStr(funcAppls.lessThanFuncAppl(num1, num2)));
        assertEquals("1.0 < 2.0", toStr(funcAppls.lessThanFuncAppl(real1, real2)));
    }

    @Test
    public void greaterEqualFuncApplTest() {
        assertEquals("1 >= 2", toStr(funcAppls.greaterEqualFuncAppl(num1, num2)));
        assertEquals("1.0 >= 2.0", toStr(funcAppls.greaterEqualFuncAppl(real1, real2)));
    }

    @Test
    public void greaterThanFuncApplTest() {
        assertEquals("1 > 2", toStr(funcAppls.greaterThanFuncAppl(num1, num2)));
        assertEquals("1.0 > 2.0", toStr(funcAppls.greaterThanFuncAppl(real1, real2)));
    }

    @Test
    public void equalFuncApplTest() {
        assertEquals("1 = 2", toStr(funcAppls.equalFuncAppl(num1, num2)));
        assertEquals("1.0 = 2.0", toStr(funcAppls.equalFuncAppl(real1, real2)));
    }

    @Test
    public void unEqualFuncApplTest() {
        assertEquals("1 <> 2", toStr(funcAppls.unEqualFuncAppl(num1, num2)));
        assertEquals("1.0 <> 2.0", toStr(funcAppls.unEqualFuncAppl(real1, real2)));
    }

    @Test
    public void andFuncApplTest() {
        assertEquals("FALSE AND TRUE AND FALSE", toStr(funcAppls.andFuncAppl(bool0, bool1, bool0)));
    }

    @Test
    public void xorFuncApplTest() {
        assertEquals("FALSE XOR TRUE XOR FALSE", toStr(funcAppls.xorFuncAppl(bool0, bool1, bool0)));
    }

    @Test
    public void orFuncApplTest() {
        assertEquals("FALSE OR TRUE OR FALSE", toStr(funcAppls.orFuncAppl(bool0, bool1, bool0)));
    }

    @Test
    public void castFuncApplTest() {
        assertEquals("DINT_TO_LREAL(1)",
                toStr(funcAppls.castFunctionAppl(num1, PlcElementaryType.LREAL_TYPE)));
    }

    @Test
    public void selFuncApplTest() {
        assertEquals("SEL_DINT(TRUE, 1, 2)", toStr(funcAppls.selFuncAppl(bool1, num1, num2)));
    }

    @Test
    public void parenthesesTest() {
        PlcFuncAppl realNegate = funcAppls.negateFuncAppl(real2);
        PlcFuncAppl negate = funcAppls.negateFuncAppl(num3);
        PlcFuncAppl power = funcAppls.powerFuncAppl(real1, num1);
        PlcFuncAppl realMul = funcAppls.multiplyFuncAppl(real1, real2);
        PlcFuncAppl mul = funcAppls.multiplyFuncAppl(num1, num2);
        PlcFuncAppl add = funcAppls.addFuncAppl(num1, num2);
        PlcFuncAppl order = funcAppls.greaterThanFuncAppl(num1, num2);
        PlcFuncAppl equal = funcAppls.equalFuncAppl(num1, num2);
        PlcFuncAppl and = funcAppls.andFuncAppl(bool0, bool1);
        PlcFuncAppl xor = funcAppls.xorFuncAppl(bool0, bool1);
        PlcFuncAppl or = funcAppls.orFuncAppl(bool0, bool1);

        assertEquals("--3", toStr(funcAppls.negateFuncAppl(negate))); // Same strength.
        assertEquals("-(1.0 ** 1)", toStr(funcAppls.negateFuncAppl(power))); // Root first.

        assertEquals("-2.0 ** -2.0", toStr(funcAppls.powerFuncAppl(realNegate, realNegate))); // Children first.
        assertEquals("(1.0 ** 1) ** (1.0 ** 1)", toStr(funcAppls.powerFuncAppl(power, power))); // Same strength.
        assertEquals("(1.0 * 2.0) ** (1.0 * 2.0)", toStr(funcAppls.powerFuncAppl(realMul, realMul))); // Root first.

        assertEquals("1.0 ** 1 * 1.0 ** 1", toStr(funcAppls.multiplyFuncAppl(power, power))); // Children first.
        assertEquals("1 * 2 * (1 * 2)", toStr(funcAppls.multiplyFuncAppl(mul, mul))); // Same strength.
        assertEquals("(1 + 2) * (1 + 2)", toStr(funcAppls.multiplyFuncAppl(add, add))); // Root first.

        assertEquals("1 * 2 + 1 * 2", toStr(funcAppls.addFuncAppl(mul, mul))); // Children first.
        assertEquals("1 + 2 + (1 + 2)", toStr(funcAppls.addFuncAppl(add, add))); // Same strength.

        assertEquals("(1 > 2) > (1 > 2)", toStr(funcAppls.greaterThanFuncAppl(order, order))); // Same strength.
        assertEquals("(1 = 2) > (1 = 2)", toStr(funcAppls.greaterThanFuncAppl(equal, equal))); // Root first.

        assertEquals("1 > 2 = 1 > 2", toStr(funcAppls.equalFuncAppl(order, order))); // Children first.
        assertEquals("(1 = 2) = (1 = 2)", toStr(funcAppls.equalFuncAppl(equal, equal))); // Same strength.
        assertEquals("(FALSE AND TRUE) = (FALSE AND TRUE)", toStr(funcAppls.equalFuncAppl(and, and))); // Root first.

        assertEquals("1 = 2 AND 1 = 2", toStr(funcAppls.andFuncAppl(equal, equal))); // Children first.
        assertEquals("FALSE AND TRUE AND (FALSE AND TRUE)", toStr(funcAppls.andFuncAppl(and, and))); // Same strength.
        assertEquals("(FALSE XOR TRUE) AND (FALSE XOR TRUE)", toStr(funcAppls.andFuncAppl(xor, xor))); // Root first.

        assertEquals("FALSE AND TRUE XOR FALSE AND TRUE", toStr(funcAppls.xorFuncAppl(and, and))); // Children first.
        assertEquals("FALSE XOR TRUE XOR (FALSE XOR TRUE)", toStr(funcAppls.xorFuncAppl(xor, xor))); // Same strength.
        assertEquals("(FALSE OR TRUE) XOR (FALSE OR TRUE)", toStr(funcAppls.xorFuncAppl(or, or))); // Root first.

        assertEquals("FALSE XOR TRUE OR FALSE XOR TRUE", toStr(funcAppls.orFuncAppl(xor, xor))); // Children first.
        assertEquals("FALSE OR TRUE OR (FALSE OR TRUE)", toStr(funcAppls.orFuncAppl(or, or))); // Same strength.
    }
}
