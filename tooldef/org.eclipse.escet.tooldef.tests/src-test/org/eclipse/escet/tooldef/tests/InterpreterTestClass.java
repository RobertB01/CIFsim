//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.tooldef.runtime.ToolDefList;
import org.eclipse.escet.tooldef.runtime.ToolDefMap;
import org.eclipse.escet.tooldef.runtime.ToolDefSet;
import org.eclipse.escet.tooldef.runtime.ToolDefTupleNary;
import org.eclipse.escet.tooldef.runtime.ToolDefTuplePair;

/** Test class used for interpreter tests. */
@SuppressWarnings("javadoc")
public class InterpreterTestClass {
    private InterpreterTestClass() {
        // Static class.
    }

    // 2-tuple parameter and return types.
    public static <T1, T2> ToolDefTuplePair<T2, T1> swap2(ToolDefTuplePair<T1, T2> t) {
        return new ToolDefTuplePair<>(t.right, t.left);
    }

    // 3-tuple parameter and return types.
    public static <T1, T2, T3> ToolDefTupleNary<T3, ToolDefTuplePair<T2, T1>>
            swap3(ToolDefTupleNary<T1, ToolDefTuplePair<T2, T3>> t)
    {
        return new ToolDefTupleNary<>(t.remainder.right, new ToolDefTuplePair<>(t.remainder.left, t.prefix));
    }

    // 4-tuple parameter and return types.
    public static <T1, T2, T3, T4> ToolDefTupleNary<T4, ToolDefTupleNary<T3, ToolDefTuplePair<T2, T1>>>
            swap4(ToolDefTupleNary<T1, ToolDefTupleNary<T2, ToolDefTuplePair<T3, T4>>> t)
    {
        return new ToolDefTupleNary<>(t.remainder.remainder.right, new ToolDefTupleNary<>(t.remainder.remainder.left,
                new ToolDefTuplePair<>(t.remainder.prefix, t.prefix)));
    }

    // Variadic parameter with primitive component type.
    public static int variadic1(int... a) {
        int rslt = 0;
        for (int b: a) {
            rslt += b;
        }
        return rslt;
    }

    // Variadic parameter with object component type.
    public static String variadic2(String... a) {
        String rslt = "";
        for (String b: a) {
            rslt += b;
        }
        return rslt;
    }

    // Variadic parameter with generic component type with class variant of
    // primitive type as element type.
    @SafeVarargs
    public static int variadic3(List<Integer>... a) {
        int rslt = 0;
        for (List<Integer> b: a) {
            for (int c: b) {
                rslt += c;
            }
        }
        return rslt;
    }

    // Variadic parameter with generic component type with object type as
    // element type.
    @SafeVarargs
    public static String variadic4(List<String>... a) {
        String rslt = "";
        for (List<String> b: a) {
            for (String c: b) {
                rslt += c;
            }
        }
        return rslt;
    }

    // Variadic parameter with multi-level generic component type.
    @SafeVarargs
    public static int variadic5(List<List<Integer>>... a) {
        int rslt = 0;
        for (List<List<Integer>> b: a) {
            for (List<Integer> c: b) {
                for (int d: c) {
                    rslt += d;
                }
            }
        }
        return rslt;
    }

    // List not a ToolDefList.
    public static List<Integer> testWrongList() {
        return new ArrayList<>();
    }

    // Set not a ToolDefSet.
    public static Set<Integer> testWrongSet() {
        return new HashSet<>();
    }

    // Map not a ToolDefMap.
    public static Map<Integer, Integer> testWrongMap() {
        return new HashMap<>();
    }

    // NaN double result.
    public static double testNaN() {
        return Double.NaN;
    }

    // +inf double result.
    public static double testPosInf() {
        return Double.POSITIVE_INFINITY;
    }

    // -inf double result.
    public static double testNegInf() {
        return Double.NEGATIVE_INFINITY;
    }

    // -0.0 normalization.
    public static Map<List<Set<Double>>, List<ToolDefTuplePair<Double, Double>>> testNegativeZero() {
        List<Set<Double>> key1 = new ToolDefList<>();
        List<ToolDefTuplePair<Double, Double>> value1 = new ToolDefList<>();

        Set<Double> key2 = new ToolDefSet<>();
        key2.add(-0.0);
        key1.add(key2);

        ToolDefTuplePair<Double, Double> value2 = new ToolDefTuplePair<>(-0.0, -0.0);
        value1.add(value2);

        Map<List<Set<Double>>, List<ToolDefTuplePair<Double, Double>>> map = new ToolDefMap<>();
        map.put(key1, value1);

        return map;
    }

    // Private method.
    @SuppressWarnings("unused")
    private static int priv() {
        return 5;
    }
}
