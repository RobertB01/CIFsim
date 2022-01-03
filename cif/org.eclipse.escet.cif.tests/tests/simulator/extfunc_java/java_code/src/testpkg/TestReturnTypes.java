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

package testpkg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("javadoc")
public class TestReturnTypes {
    private TestReturnTypes() {
        // Static class.
    }

    public static double retNaN() {
        return Double.NaN;
    }

    public static double retInfPos() {
        return Double.POSITIVE_INFINITY;
    }

    public static double retInfNeg() {
        return Double.NEGATIVE_INFINITY;
    }

    public static boolean retb() {
        return true;
    }

    public static Boolean retB() {
        return false;
    }

    public static int reti() {
        return 1;
    }

    public static Integer retI() {
        return 2;
    }

    public static double retd() {
        return 3.0;
    }

    public static Double retD() {
        return 4.0;
    }

    public static String retG() {
        return "abc";
    }

    public static List<Boolean> retLB() {
        return Arrays.asList(new Boolean[]{true});
    }

    public static List<Integer> retLI() {
        return Arrays.asList(new Integer[]{5});
    }

    public static List<Double> retLD() {
        return Arrays.asList(new Double[]{6.0});
    }

    public static List<String> retLG() {
        return Arrays.asList(new String[]{"def"});
    }

    public static List<Boolean> retLN() {
        return Arrays.asList(new Boolean[]{null});
    }

    public static List<List<Integer>> retLLI() {
        List<Integer> li = Arrays.asList(new Integer[]{1});
        List<List<Integer>> lli = new ArrayList<List<Integer>>();
        lli.add(li);
        return lli;
    }

    public static Set<Integer> retSI() {
        return new LinkedHashSet<Integer>(Arrays.asList(new Integer[]{7}));
    }

    public static Set<Set<Integer>> retSSI() {
        Set<Integer> si = new LinkedHashSet<Integer>();
        si.add(8);
        Set<Set<Integer>> ssi = new LinkedHashSet<Set<Integer>>();
        ssi.add(si);
        return ssi;
    }

    public static Map<Integer, Boolean> retMIB() {
        Map<Integer, Boolean> mib = new LinkedHashMap<Integer, Boolean>();
        mib.put(9, true);
        return mib;
    }

    public static Map<List<Integer>, Set<Boolean>> retMLISB() {
        List<Integer> li = new ArrayList<Integer>();
        li.add(10);

        Set<Boolean> si = new LinkedHashSet<Boolean>();
        si.add(true);

        Map<List<Integer>, Set<Boolean>> mlisb;
        mlisb = new LinkedHashMap<List<Integer>, Set<Boolean>>();
        mlisb.put(li, si);
        return mlisb;
    }

    public static List<Object> retTIBX() {
        List<Object> tibx = new ArrayList<Object>();
        tibx.add(11);
        tibx.add(true);
        return tibx;
    }

    public static List<Object> retTLITIIXX() {
        List<Integer> li = new ArrayList<Integer>();
        li.add(12);

        List<Object> tiix = new ArrayList<Object>();
        tiix.add(13);
        tiix.add(14);

        List<Object> tlitiixx = new ArrayList<Object>();
        tlitiixx.add(li);
        tlitiixx.add(tiix);
        return tlitiixx;
    }
}
