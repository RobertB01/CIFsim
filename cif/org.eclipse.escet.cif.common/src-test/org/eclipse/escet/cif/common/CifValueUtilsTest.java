//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifValueUtils.makeInt;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompInstWrapType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCompParamWrapType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDistType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumDecl;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteral;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFuncType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSetType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTypeDecl;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTypeRef;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.str;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.junit.jupiter.api.Test;

/** Unit tests for the {@link CifValueUtils} class. */
public class CifValueUtilsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testMakeInt() {
        assertEquals("-2147483647 - 1", exprToStr(makeInt(Integer.MIN_VALUE)));
        assertEquals("-2147483647", exprToStr(makeInt(Integer.MIN_VALUE + 1)));
        assertEquals("-2147483646", exprToStr(makeInt(Integer.MIN_VALUE + 2)));
        assertEquals("-123", exprToStr(makeInt(-123)));
        assertEquals("-1", exprToStr(makeInt(-1)));
        assertEquals("0", exprToStr(makeInt(0)));
        assertEquals("1", exprToStr(makeInt(1)));
        assertEquals("123", exprToStr(makeInt(123)));
        assertEquals("2147483646", exprToStr(makeInt(Integer.MAX_VALUE - 1)));
        assertEquals("2147483647", exprToStr(makeInt(Integer.MAX_VALUE)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCreateConjunction3() {
        Expression i1 = CifValueUtils.makeInt(1);
        Expression i2 = CifValueUtils.makeInt(2);
        Expression i3 = CifValueUtils.makeInt(3);

        Expression total = CifValueUtils.createConjunction(list(i1, i2, i3));

        assertEquals("1 and (2 and 3)", CifTextUtils.exprToStr(total));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCreateConjunctionLeft() {
        Expression i1 = CifValueUtils.makeInt(1);
        Expression i2 = CifValueUtils.makeInt(2);
        Expression i3 = CifValueUtils.makeInt(3);

        Expression left = CifValueUtils.createConjunction(list(i1, i2));
        Expression total = CifValueUtils.createConjunction(list(left, i3));

        assertEquals("1 and (2 and 3)", CifTextUtils.exprToStr(total));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCreateConjunctionRight() {
        Expression i1 = CifValueUtils.makeInt(1);
        Expression i2 = CifValueUtils.makeInt(2);
        Expression i3 = CifValueUtils.makeInt(3);

        Expression right = CifValueUtils.createConjunction(list(i2, i3));
        Expression total = CifValueUtils.createConjunction(list(i1, right));

        assertEquals("1 and (2 and 3)", CifTextUtils.exprToStr(total));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCreateConjunctionTree8() {
        Expression[] ii = new Expression[8];
        for (int i = 0; i < ii.length; i++) {
            ii[i] = CifValueUtils.makeInt(i + 1);
        }

        Expression x12 = CifValueUtils.createConjunction(list(ii[0], ii[1]));
        Expression x34 = CifValueUtils.createConjunction(list(ii[2], ii[3]));
        Expression x56 = CifValueUtils.createConjunction(list(ii[4], ii[5]));
        Expression x78 = CifValueUtils.createConjunction(list(ii[6], ii[7]));

        Expression x1234 = CifValueUtils.createConjunction(list(x12, x34));
        Expression x5678 = CifValueUtils.createConjunction(list(x56, x78));

        Expression total = CifValueUtils.createConjunction(list(x1234, x5678));

        assertEquals("1 and 2 and (3 and 4) and (5 and 6 and (7 and 8))", CifTextUtils.exprToStr(total));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCreateDisjunction3() {
        Expression i1 = CifValueUtils.makeInt(1);
        Expression i2 = CifValueUtils.makeInt(2);
        Expression i3 = CifValueUtils.makeInt(3);

        Expression total = CifValueUtils.createDisjunction(list(i1, i2, i3));

        assertEquals("1 or (2 or 3)", CifTextUtils.exprToStr(total));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCreateDisjunctionLeft() {
        Expression i1 = CifValueUtils.makeInt(1);
        Expression i2 = CifValueUtils.makeInt(2);
        Expression i3 = CifValueUtils.makeInt(3);

        Expression left = CifValueUtils.createDisjunction(list(i1, i2));
        Expression total = CifValueUtils.createDisjunction(list(left, i3));

        assertEquals("1 or (2 or 3)", CifTextUtils.exprToStr(total));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCreateDisjunctionRight() {
        Expression i1 = CifValueUtils.makeInt(1);
        Expression i2 = CifValueUtils.makeInt(2);
        Expression i3 = CifValueUtils.makeInt(3);

        Expression right = CifValueUtils.createDisjunction(list(i2, i3));
        Expression total = CifValueUtils.createDisjunction(list(i1, right));

        assertEquals("1 or (2 or 3)", CifTextUtils.exprToStr(total));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testCreateDisjunctionTree8() {
        Expression[] ii = new Expression[8];
        for (int i = 0; i < ii.length; i++) {
            ii[i] = CifValueUtils.makeInt(i + 1);
        }

        Expression x12 = CifValueUtils.createDisjunction(list(ii[0], ii[1]));
        Expression x34 = CifValueUtils.createDisjunction(list(ii[2], ii[3]));
        Expression x56 = CifValueUtils.createDisjunction(list(ii[4], ii[5]));
        Expression x78 = CifValueUtils.createDisjunction(list(ii[6], ii[7]));

        Expression x1234 = CifValueUtils.createDisjunction(list(x12, x34));
        Expression x5678 = CifValueUtils.createDisjunction(list(x56, x78));

        Expression total = CifValueUtils.createDisjunction(list(x1234, x5678));

        assertEquals("1 or 2 or (3 or 4) or (5 or 6 or (7 or 8))", CifTextUtils.exprToStr(total));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValueCountBasic() {
        // Declare an enum with 3 values.
        Specification spec = newSpecification();
        EnumDecl enumDecl = newEnumDecl(null, null, "E", null);
        enumDecl.getLiterals().add(newEnumLiteral(null, "a", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "b", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "c", null));
        spec.getDeclarations().add(enumDecl);

        // Declare type declaration.
        TypeDecl typeDecl = newTypeDecl(null, "t", null, newBoolType());
        spec.getDeclarations().add(typeDecl);

        // Check possible value counts for various types.
        assertEquals(2, CifValueUtils.getPossibleValueCount(newBoolType()));
        assertEquals(5, CifValueUtils.getPossibleValueCount(newIntType(1, null, 5)));
        assertEquals(4, CifValueUtils.getPossibleValueCount(newIntType(-1, null, 2)));
        assertEquals(Double.POSITIVE_INFINITY, CifValueUtils.getPossibleValueCount(newRealType()));
        assertEquals(3, CifValueUtils.getPossibleValueCount(newEnumType(enumDecl, null)));

        assertEquals(6, CifValueUtils.getPossibleValueCount(newTupleType(
                list(newField("x", null, newBoolType()), newField("y", null, newIntType(0, null, 2))), null)));
        assertEquals(4, CifValueUtils.getPossibleValueCount(newListType(newBoolType(), 2, null, 2)));
        assertEquals(6, CifValueUtils.getPossibleValueCount(newListType(newBoolType(), 1, null, 2)));
        assertEquals(7, CifValueUtils.getPossibleValueCount(newListType(newBoolType(), 0, null, 2)));
        assertEquals(4, CifValueUtils.getPossibleValueCount(newSetType(newBoolType(), null)));
        assertEquals(Double.POSITIVE_INFINITY, CifValueUtils.getPossibleValueCount(newStringType()));
        assertEquals(Double.POSITIVE_INFINITY,
                CifValueUtils.getPossibleValueCount(newFuncType(list(), null, newBoolType())));
        assertEquals(Double.POSITIVE_INFINITY, CifValueUtils.getPossibleValueCount(newDistType(null, newBoolType())));
        assertEquals(9, CifValueUtils.getPossibleValueCount(newDictType(newBoolType(), null, newBoolType())));
        assertEquals(2, CifValueUtils.getPossibleValueCount(newTypeRef(null, typeDecl)));
        assertEquals(2,
                CifValueUtils.getPossibleValueCount(newCompInstWrapType(null, null, newTypeRef(null, typeDecl))));
        assertEquals(2,
                CifValueUtils.getPossibleValueCount(newCompParamWrapType(null, null, newTypeRef(null, typeDecl))));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValueCountMatchesPossibleValues() {
        // Declare an enum with 3 values.
        Specification spec = newSpecification();
        EnumDecl enumDecl = newEnumDecl(null, null, "E", null);
        enumDecl.getLiterals().add(newEnumLiteral(null, "a", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "b", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "c", null));
        spec.getDeclarations().add(enumDecl);

        // Declare enums with variable number of literals.
        List<EnumDecl> enumDecls = list();
        for (int i = 0; i < 2; i++) {
            String enumName = "E" + str(i + 1);
            EnumDecl enumDecl2 = newEnumDecl(null, null, enumName, null);
            for (int j = 0; j <= i; j++) {
                char c = (char)(97 + j);
                String litName = String.valueOf(c);
                enumDecl2.getLiterals().add(newEnumLiteral(null, litName, null));
            }
            spec.getDeclarations().add(enumDecl2);
            enumDecls.add(enumDecl2);
        }

        // Get types to test.
        List<CifType> types = list(
                // Boolean types.
                newBoolType(), //

                // Integer types.
                newIntType(0, null, 0), //
                newIntType(1, null, 1), //
                newIntType(-1, null, -1), //
                newIntType(0, null, 1), //
                newIntType(1, null, 5), //
                newIntType(-1, null, 1), //

                // Enumeration types.
                newEnumType(enumDecl, null), //

                // Array types.
                newListType(newBoolType(), 3, null, 3), //
                newListType(newIntType(-1, null, 1), 2, null, 2), //
                newListType(newEnumType(enumDecl, null), 2, null, 2), //

                // Tuple types.
                newTupleType(list(newField("x", null, newBoolType()), newField("y", null, newIntType(0, null, 2))),
                        null), //
                newTupleType(list(newField("x", null, newBoolType()), newField("y", null, newEnumType(enumDecl, null))),
                        null), //

                // Set types.
                newSetType(newBoolType(), null), //
                newSetType(newIntType(0, null, 2), null), //
                newSetType(newEnumType(enumDecl, null), null), //

                // Dict types.
                newDictType(newBoolType(), null, newIntType(0, null, 2)), //
                newDictType(newBoolType(), null, newEnumType(enumDecl, null)) //
        );

        // Add more array types.
        for (EnumDecl enumDecl2: enumDecls) {
            for (int lower = 0; lower < 4; lower++) {
                for (int upper = lower; upper < 4; upper++) {
                    types.add(newListType(newEnumType(enumDecl2, null), lower, null, upper));
                }
            }
        }

        // For each type, check possible value count against number of actual possible values.
        for (CifType type: types) {
            double possibleValueCount = CifValueUtils.getPossibleValueCount(type);
            List<Expression> possibleValues = CifValueUtils.getPossibleValues(type);
            assertEquals(possibleValueCount, possibleValues.size(), CifTextUtils.typeToStr(type));
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValueCountLarge() {
        CifType[] types = { //
                newIntType(0, null, Integer.MAX_VALUE), //
                newIntType(1, null, Integer.MAX_VALUE), //
                newIntType(),

                newListType(newBoolType(), 32, null, 32), //
                newListType(newListType(newBoolType(), 0, null, 0), 0, null, Integer.MAX_VALUE), //
                newListType(newBoolType(), null, null, null), //
                newListType(newStringType(), null, null, null), //
                newListType(newListType(newBoolType(), null, null, null), null, null, null), //

                newSetType(newBoolType(), null), //
                newSetType(newIntType(0, null, 31), null), //
                newSetType(newIntType(), null), //
                newSetType(newStringType(), null), //

                newDictType(newBoolType(), null, newBoolType()), //
                newDictType(newIntType(1, null, Integer.MAX_VALUE), null, newBoolType()), //
                newDictType(newStringType(), null, newBoolType()), //
                newDictType(newBoolType(), null, newStringType()), //
        };

        Double[] expecteds = { //
                2_147_483_648d, // Integer.MAX_VALUE + 1
                2_147_483_647d, // Integer.MAX_VALUE
                4_294_967_296d, // full 32 bits

                4_294_967_296d, // full 32 bits
                2_147_483_648d, // Integer.MAX_VALUE + 1
                Double.POSITIVE_INFINITY, // infinity
                Double.POSITIVE_INFINITY, // infinity
                Double.POSITIVE_INFINITY, // infinity

                4d, // 4
                4_294_967_296d, // full 32 bits
                Double.POSITIVE_INFINITY, // infinity
                Double.POSITIVE_INFINITY, // infinity

                9d, // 9
                Double.POSITIVE_INFINITY, // infinity
                Double.POSITIVE_INFINITY, // infinity
                Double.POSITIVE_INFINITY, // infinity
        };

        assertEquals(types.length, expecteds.length);
        for (int i = 0; i < types.length; i++) {
            CifType type = types[i];
            String msg = CifTextUtils.typeToStr(type);
            double expected = expecteds[i];
            double actual = CifValueUtils.getPossibleValueCount(type);
            assertEquals(expected, actual, 0.0, msg);
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValuesBool() {
        CifType[] types = {newBoolType()};
        String[] expecteds = {"false, true"};
        testGetPossibleValues(types, expecteds);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValuesInt() {
        CifType[] types = { //
                newIntType(0, null, 0), //
                newIntType(1, null, 1), //
                newIntType(-1, null, -1), //
                newIntType(0, null, 1), //
                newIntType(1, null, 5), //
                newIntType(-1, null, 1) //
        };
        String[] expecteds = {"0", "1", "-1", "0, 1", "1, 2, 3, 4, 5", "-1, 0, 1"};
        testGetPossibleValues(types, expecteds);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValuesEnum() {
        Specification spec = newSpecification();
        EnumDecl enumDecl = newEnumDecl(null, null, "E", null);
        enumDecl.getLiterals().add(newEnumLiteral(null, "a", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "b", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "c", null));
        spec.getDeclarations().add(enumDecl);

        CifType[] types = {newEnumType(enumDecl, null)};
        String[] expecteds = {"a, b, c"};
        testGetPossibleValues(types, expecteds);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValuesArray() {
        Specification spec = newSpecification();
        EnumDecl enumDecl = newEnumDecl(null, null, "E", null);
        enumDecl.getLiterals().add(newEnumLiteral(null, "a", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "b", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "c", null));
        spec.getDeclarations().add(enumDecl);

        CifType[] types = { //
                newListType(newBoolType(), 3, null, 3), //
                newListType(newIntType(-1, null, 1), 2, null, 2), //
                newListType(newEnumType(enumDecl, null), 2, null, 2) //
        };
        String[] expecteds = { //
                "[false, false, false], " + //
                        "[false, false, true], " + //
                        "[false, true, false], " + //
                        "[false, true, true], " + //
                        "[true, false, false], " + //
                        "[true, false, true], " + //
                        "[true, true, false], " + //
                        "[true, true, true]", //

                "[-1, -1], [-1, 0], [-1, 1], [0, -1], [0, 0], [0, 1], [1, -1], [1, 0], [1, 1]", //

                "[a, a], [a, b], [a, c], [b, a], [b, b], [b, c], [c, a], [c, b], [c, c]", //
        };
        testGetPossibleValues(types, expecteds);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValuesList() {
        Specification spec = newSpecification();
        List<EnumDecl> enumDecls = list();
        for (int i = 0; i < 2; i++) {
            String enumName = "E" + str(i + 1);
            EnumDecl enumDecl = newEnumDecl(null, null, enumName, null);
            for (int j = 0; j <= i; j++) {
                char c = (char)(97 + j);
                String litName = String.valueOf(c);
                enumDecl.getLiterals().add(newEnumLiteral(null, litName, null));
            }
            spec.getDeclarations().add(enumDecl);
            enumDecls.add(enumDecl);
        }

        List<CifType> types = list();
        for (EnumDecl enumDecl: enumDecls) {
            for (int lower = 0; lower < 4; lower++) {
                for (int upper = lower; upper < 4; upper++) {
                    types.add(newListType(newEnumType(enumDecl, null), lower, null, upper));
                }
            }
        }

        String[] expecteds = {
                // E1 = a

                /* [0..0] */ "[]", //
                /* [0..1] */ "[], [a]", //
                /* [0..2] */ "[], [a], [a, a]", //
                /* [0..3] */ "[], [a], [a, a], [a, a, a]", //
                /* [1..1] */ "[a]", //
                /* [1..2] */ "[a], [a, a]", //
                /* [1..3] */ "[a], [a, a], [a, a, a]", //
                /* [2..2] */ "[a, a]", //
                /* [2..3] */ "[a, a], [a, a, a]", //
                /* [3..3] */ "[a, a, a]", //

                // E2 = a, b

                /* [0..0] */ "[]", //
                /* [0..1] */ "[], [a], [b]", //
                /* [0..2] */ "[], [a], [b], [a, a], [a, b], [b, a], [b, b]", //
                /* [0..3] */ "[], [a], [b], [a, a], [a, b], [b, a], [b, b], [a, a, a], [a, a, b], [a, b, a], [a, b, b], [b, a, a], [b, a, b], [b, b, a], [b, b, b]", //
                /* [1..1] */ "[a], [b]", //
                /* [1..2] */ "[a], [b], [a, a], [a, b], [b, a], [b, b]", //
                /* [1..3] */ "[a], [b], [a, a], [a, b], [b, a], [b, b], [a, a, a], [a, a, b], [a, b, a], [a, b, b], [b, a, a], [b, a, b], [b, b, a], [b, b, b]", //
                /* [2..2] */ "[a, a], [a, b], [b, a], [b, b]", //
                /* [2..3] */ "[a, a], [a, b], [b, a], [b, b], [a, a, a], [a, a, b], [a, b, a], [a, b, b], [b, a, a], [b, a, b], [b, b, a], [b, b, b]", //
                /* [3..3] */ "[a, a, a], [a, a, b], [a, b, a], [a, b, b], [b, a, a], [b, a, b], [b, b, a], [b, b, b]", //
        };
        testGetPossibleValues(types.toArray(new CifType[0]), expecteds);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValuesTuple() {
        Specification spec = newSpecification();
        EnumDecl enumDecl = newEnumDecl(null, null, "E", null);
        enumDecl.getLiterals().add(newEnumLiteral(null, "a", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "b", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "c", null));
        spec.getDeclarations().add(enumDecl);

        CifType[] types = {
                newTupleType(list(newField("x", null, newBoolType()), newField("y", null, newIntType(0, null, 2))),
                        null), //
                newTupleType(list(newField("x", null, newBoolType()), newField("y", null, newEnumType(enumDecl, null))),
                        null), //
        };
        String[] expecteds = { //
                "(false, 0), (false, 1), (false, 2), (true, 0), (true, 1), (true, 2)", //
                "(false, a), (false, b), (false, c), (true, a), (true, b), (true, c)", //
        };
        testGetPossibleValues(types, expecteds);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValuesSet() {
        Specification spec = newSpecification();
        EnumDecl enumDecl = newEnumDecl(null, null, "E", null);
        enumDecl.getLiterals().add(newEnumLiteral(null, "a", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "b", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "c", null));
        spec.getDeclarations().add(enumDecl);

        CifType[] types = { //
                newSetType(newBoolType(), null), //
                newSetType(newIntType(0, null, 2), null), //
                newSetType(newEnumType(enumDecl, null), null), //
        };
        String[] expecteds = { //
                "{}, {false}, {true}, {false, true}", //
                "{}, {0}, {1}, {0, 1}, {2}, {0, 2}, {1, 2}, {0, 1, 2}", //
                "{}, {a}, {b}, {a, b}, {c}, {a, c}, {b, c}, {a, b, c}", //
        };
        testGetPossibleValues(types, expecteds);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testGetPossibleValuesDict() {
        Specification spec = newSpecification();
        EnumDecl enumDecl = newEnumDecl(null, null, "E", null);
        enumDecl.getLiterals().add(newEnumLiteral(null, "a", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "b", null));
        enumDecl.getLiterals().add(newEnumLiteral(null, "c", null));
        spec.getDeclarations().add(enumDecl);

        CifType[] types = { //
                newDictType(newBoolType(), null, newIntType(0, null, 2)), //
                newDictType(newBoolType(), null, newEnumType(enumDecl, null)), //
        };
        String[] expecteds = { //
                "{}, " + //
                        "{false: 0}, {false: 1}, {false: 2}, " + //
                        "{true: 0}, {true: 1}, {true: 2}, " + //
                        "{false: 0, true: 0}, " + //
                        "{false: 0, true: 1}, " + //
                        "{false: 0, true: 2}, " + //
                        "{false: 1, true: 0}, " + //
                        "{false: 1, true: 1}, " + //
                        "{false: 1, true: 2}, " + //
                        "{false: 2, true: 0}, " + //
                        "{false: 2, true: 1}, " + //
                        "{false: 2, true: 2}", //

                "{}, " + //
                        "{false: a}, {false: b}, {false: c}, " + //
                        "{true: a}, {true: b}, {true: c}, " + //
                        "{false: a, true: a}, " + //
                        "{false: a, true: b}, " + //
                        "{false: a, true: c}, " + //
                        "{false: b, true: a}, " + //
                        "{false: b, true: b}, " + //
                        "{false: b, true: c}, " + //
                        "{false: c, true: a}, " + //
                        "{false: c, true: b}, " + //
                        "{false: c, true: c}", //
        };
        testGetPossibleValues(types, expecteds);
    }

    /**
     * Single {@link CifValueUtils#getPossibleValues} and {@link CifValueUtils#getPossibleValueCount} test.
     *
     * @param types The types for which to test.
     * @param expecteds The possible values, converted to text, and separated by commas.
     */
    private void testGetPossibleValues(CifType[] types, String[] expecteds) {
        assertEquals(types.length, expecteds.length);
        for (int i = 0; i < types.length; i++) {
            // Get input and expected values.
            CifType type = types[i];
            String expected = expecteds[i];
            String msg = CifTextUtils.typeToStr(type);

            // Calculate actual values.
            List<Expression> values = CifValueUtils.getPossibleValues(type);
            String actual = CifTextUtils.exprsToStr(values);

            // Check expected and actual counts.
            double expCount = CifValueUtils.getPossibleValueCount(type);
            double actCount = values.size();
            assertEquals(expCount, actCount, 0.0, msg);

            // Check expected and actual values.
            assertEquals(expected, actual, msg);
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testExpSum() {
        for (int n = 0; n < 10; n++) {
            for (int m = 0; m < 10; m++) {
                // Expected: manual expensive calculation.
                double expected = 0;
                for (int i = 0; i < m; i++) {
                    expected += Math.pow(n, i);
                }

                // Actual: optimized calculation (the actual implementation).
                double actual = CifValueUtils.expSum(n, m);

                // Test.
                assertEquals(expected, actual, 0.0);
            }
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTryGetIntLiteralValueValidInts() {
        int[] values = {Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -5, -1, 0, 1, 5, Integer.MAX_VALUE - 1,
                Integer.MAX_VALUE};
        for (int i: values) {
            assertEquals(i, CifValueUtils.tryGetIntLiteralValue(CifValueUtils.makeInt(i)));
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTryGetIntLiteralValueOverflow() {
        // Binary subtraction overflow.
        Expression left = CifValueUtils.makeInt(-2_147_000_000);
        Expression right = CifValueUtils.makeInt(2_147_000_000);
        Expression binExpr = newBinaryExpression(left, BinaryOperator.SUBTRACTION, null, right, newIntType());
        assertEquals(null, CifValueUtils.tryGetIntLiteralValue(binExpr));

        // Unary negation overflow.
        Expression child = CifValueUtils.makeInt(Integer.MIN_VALUE);
        Expression unExpr = newUnaryExpression(child, UnaryOperator.NEGATE, null, newIntType());
        assertEquals(null, CifValueUtils.tryGetIntLiteralValue(unExpr));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTryGetIntLiteralValueUnrecognizedForm() {
        Expression left = CifValueUtils.makeInt(1);
        Expression right = CifValueUtils.makeInt(2);
        Expression binExpr = newBinaryExpression(left, BinaryOperator.ADDITION, null, right, newIntType());
        assertEquals(null, CifValueUtils.tryGetIntLiteralValue(binExpr));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTryGetIntLiteralValueNonInt() {
        Expression left = CifValueUtils.makeReal(1.0);
        Expression right = CifValueUtils.makeReal(2.0);
        Expression binExpr = newBinaryExpression(left, BinaryOperator.SUBTRACTION, null, right, newRealType());
        assertEquals(null, CifValueUtils.tryGetIntLiteralValue(binExpr));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTryGetRealLiteralValueValidReals() {
        String[] values = {"-5.0", "0.0", "1e5", "-3.0", "-1e+5", "-1.34e-3"};
        for (String value: values) {
            Expression expr = value.startsWith("-")
                    ? newUnaryExpression(newRealExpression(null, newRealType(), value.substring(1)),
                            UnaryOperator.NEGATE, null, newRealType())
                    : newRealExpression(null, newRealType(), value);
            assertEquals(value, CifValueUtils.tryGetRealLiteralValue(expr));
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTryGetRealLiteralValueUnrecognizedForm() {
        Expression left = CifValueUtils.makeReal(1.0);
        Expression right = CifValueUtils.makeReal(2.0);
        Expression binExpr = newBinaryExpression(left, BinaryOperator.ADDITION, null, right, newRealType());
        assertEquals(null, CifValueUtils.tryGetRealLiteralValue(binExpr));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTryGetRealLiteralValueNonReal() {
        Expression left = CifValueUtils.makeInt(1);
        Expression right = CifValueUtils.makeInt(2);
        Expression binExpr = newBinaryExpression(left, BinaryOperator.SUBTRACTION, null, right, newIntType());
        assertEquals(null, CifValueUtils.tryGetRealLiteralValue(binExpr));
    }
}
