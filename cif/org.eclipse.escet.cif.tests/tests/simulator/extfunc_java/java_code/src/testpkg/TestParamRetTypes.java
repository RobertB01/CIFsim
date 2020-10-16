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

package testpkg;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("javadoc")
public class TestParamRetTypes {
    public static boolean p_b(boolean x) {
        return x;
    }

    public static Boolean p_B(Boolean x) {
        return x;
    }

    public static int p_i(int x) {
        return x;
    }

    public static Integer p_I(Integer x) {
        return x;
    }

    public static double p_d(double x) {
        return x;
    }

    public static Double p_D(Double x) {
        return x;
    }

    public static String p_G(String x) {
        return x;
    }

    public static List<Boolean> p_LB(List<Boolean> x) {
        return x;
    }

    public static List<Integer> p_LI(List<Integer> x) {
        return x;
    }

    public static List<Double> p_LD(List<Double> x) {
        return x;
    }

    public static List<String> p_LG(List<String> x) {
        return x;
    }

    public static List<Boolean> p_LN(List<Boolean> x) {
        return x;
    }

    public static List<List<Integer>> p_LLI(List<List<Integer>> x) {
        return x;
    }

    public static Set<Integer> p_SI(Set<Integer> x) {
        return x;
    }

    public static Set<Set<Integer>> p_SSI(Set<Set<Integer>> x) {
        return x;
    }

    public static Map<Integer, Boolean> p_MIB(Map<Integer, Boolean> x) {
        return x;
    }

    public static Map<List<Integer>, Set<Boolean>> p_MLISB(Map<List<Integer>, Set<Boolean>> x) {
        return x;
    }

    public static List<Object> p_TIBX(List<Object> x) {
        return x;
    }

    public static List<Object> p_TLITIIXX(List<Object> x) {
        return x;
    }
}
