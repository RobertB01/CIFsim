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

package org.eclipse.escet.cif.cif2plc;

import static org.eclipse.escet.cif.cif2plc.NaryExpressionConverter.convert;
import static org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator.CONJUNCTION;
import static org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator.DISJUNCTION;
import static org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator.INVERSE;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.escet.cif.cif2plc.NaryExpressionConverter.NaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link NaryExpressionConverter} class. */
@SuppressWarnings("javadoc")
public class NaryExpressionConverterTest {
    @Test
    public void testNotAndOr() {
        Expression e = newBoolExpression();
        Expression r = convert(e);
        assertSame(e, r);
    }

    @Test
    public void testAnd() {
        // (b1 and b2) and (b3 and b4) -> nary(b1, b2, b3, b4)
        Expression b1 = newBoolExpression();
        Expression b2 = newBoolExpression();
        Expression b3 = newBoolExpression();
        Expression b4 = newBoolExpression();
        Expression c1 = newBinaryExpression(b1, CONJUNCTION, null, b2, null);
        Expression c2 = newBinaryExpression(b3, CONJUNCTION, null, b4, null);
        Expression c = newBinaryExpression(c1, CONJUNCTION, null, c2, null);

        Expression r = convert(c);

        assertTrue(r instanceof NaryExpression);
        NaryExpression nr = (NaryExpression)r;
        assertEquals(CONJUNCTION, nr.operator);
        assertEquals(4, nr.children.size());
        assertSame(b1, nr.children.get(0));
        assertSame(b2, nr.children.get(1));
        assertSame(b3, nr.children.get(2));
        assertSame(b4, nr.children.get(3));
    }

    @Test
    public void testComplex() {
        // and(and(a, or(b, c)) or(d, or(and(e, f), not(g or h))))
        // ->
        // nary_and(a, nary_or(b, c), nary_or(d, nary_and(e, f), not(g or h)))
        Expression a = newBoolExpression();
        Expression b = newBoolExpression();
        Expression c = newBoolExpression();
        Expression d = newBoolExpression();
        Expression e = newBoolExpression();
        Expression f = newBoolExpression();
        Expression g = newBoolExpression();
        Expression h = newBoolExpression();
        Expression bc = newBinaryExpression(b, DISJUNCTION, null, c, null);
        Expression ac = newBinaryExpression(a, CONJUNCTION, null, bc, null);
        Expression ef = newBinaryExpression(e, CONJUNCTION, null, f, null);
        Expression gh = newBinaryExpression(g, DISJUNCTION, null, h, null);
        Expression ngh = newUnaryExpression(gh, INVERSE, null, null);
        Expression eh = newBinaryExpression(ef, DISJUNCTION, null, ngh, null);
        Expression dh = newBinaryExpression(d, DISJUNCTION, null, eh, null);
        Expression ah = newBinaryExpression(ac, CONJUNCTION, null, dh, null);

        Expression re = convert(ah);

        assertTrue(re instanceof NaryExpression);
        NaryExpression nre = (NaryExpression)re;
        assertEquals(CONJUNCTION, nre.operator);
        assertEquals(3, nre.children.size());
        assertSame(a, nre.children.get(0));
        assertTrue(nre.children.get(1) instanceof NaryExpression);
        assertTrue(nre.children.get(2) instanceof NaryExpression);

        NaryExpression nre2 = (NaryExpression)nre.children.get(1);
        assertEquals(DISJUNCTION, nre2.operator);
        assertEquals(2, nre2.children.size());
        assertSame(b, nre2.children.get(0));
        assertSame(c, nre2.children.get(1));

        NaryExpression nre3 = (NaryExpression)nre.children.get(2);
        assertEquals(DISJUNCTION, nre3.operator);
        assertEquals(3, nre3.children.size());
        assertSame(d, nre3.children.get(0));
        assertTrue(nre3.children.get(1) instanceof NaryExpression);
        assertSame(ngh, nre3.children.get(2));

        NaryExpression nre4 = (NaryExpression)nre3.children.get(1);
        assertEquals(CONJUNCTION, nre4.operator);
        assertEquals(2, nre4.children.size());
        assertSame(e, nre4.children.get(0));
        assertSame(f, nre4.children.get(1));
    }
}
