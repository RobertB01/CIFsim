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
public class TestClass1 {
    private TestClass1() {
        // Static class.
    }

    public static void voidMethod() {
        // No body.
    }

    public static int ret5() {
        return 5;
    }

    // list set dict(tuple(bool b; int i; real r; string s) : bool);
    // [{{(true, 1, 2.0, "abc") : true}}]
    public static List<Set<Map<List<Object>, Boolean>>> sideEffect(
                  List<Set<Map<List<Object>, Boolean>>> l)
    {
        Set<Map<List<Object>, Boolean>> s = l.get(0);
        Map<List<Object>, Boolean> m = s.iterator().next();
        List<Object> t = m.entrySet().iterator().next().getKey();

        m.put(t, !m.get(t));

        t.set(0, !(Boolean)t.get(0));
        t.set(1, ((Integer)t.get(1)) + 1);
        t.set(2, ((Double)t.get(2)) * 2);
        t.set(3, ((String)t.get(3)) + "x");

        return l;
    }

    public static int inf() {
        int x = 0;
        while (x < 99999) {
            x = (x + 1) % 1000;
        }
        return 0; // Never reached.
    }

    public static int exc() {
        throw new IllegalStateException("Some error...");
    }

    public static int multiParam(int x, double y, List<Integer> z) {
        return x + (int)Math.floor(y) + z.get(0);
    }

    private static int retPriv125() {
        return 125;
    }
}
