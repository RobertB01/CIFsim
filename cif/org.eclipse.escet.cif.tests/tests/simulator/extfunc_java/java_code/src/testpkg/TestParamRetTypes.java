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

package testpkg;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("javadoc")
public class TestParamRetTypes {
    private TestParamRetTypes() {
        // Static class.
    }

    public static boolean pb(boolean x) {
        return x;
    }

    public static Boolean pB(Boolean x) {
        return x;
    }

    public static int pi(int x) {
        return x;
    }

    public static Integer pI(Integer x) {
        return x;
    }

    public static double pd(double x) {
        return x;
    }

    public static Double pD(Double x) {
        return x;
    }

    public static String pG(String x) {
        return x;
    }

    public static List<Boolean> pLB(List<Boolean> x) {
        return x;
    }

    public static List<Integer> pLI(List<Integer> x) {
        return x;
    }

    public static List<Double> pLD(List<Double> x) {
        return x;
    }

    public static List<String> pLG(List<String> x) {
        return x;
    }

    public static List<Boolean> pLN(List<Boolean> x) {
        return x;
    }

    public static List<List<Integer>> pLLI(List<List<Integer>> x) {
        return x;
    }

    public static Set<Integer> pSI(Set<Integer> x) {
        return x;
    }

    public static Set<Set<Integer>> pSSI(Set<Set<Integer>> x) {
        return x;
    }

    public static Map<Integer, Boolean> pMIB(Map<Integer, Boolean> x) {
        return x;
    }

    public static Map<List<Integer>, Set<Boolean>> pMLISB(Map<List<Integer>, Set<Boolean>> x) {
        return x;
    }

    public static List<Object> pTIBX(List<Object> x) {
        return x;
    }

    public static List<Object> pTLITIIXX(List<Object> x) {
        return x;
    }
}
