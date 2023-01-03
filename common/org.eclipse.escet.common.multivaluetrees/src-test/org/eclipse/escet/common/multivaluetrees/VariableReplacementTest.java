//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.multivaluetrees;

import static org.junit.Assert.assertSame;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class VariableReplacementTest {
    public static SimpleVariable vc;

    public static SimpleVariable vn;

    public static SimpleVariable wc;

    public static SimpleVariable wn;

    public static SimpleVarInfoBuilder svb;

    static {
        vc = new SimpleVariable("vc", 5, 4); // [5..8] v_current
        vn = new SimpleVariable("vn", 5, 4); // [5..8] v_new
        wc = new SimpleVariable("wc", 5, 4); // [5..8] w_current
        wn = new SimpleVariable("wn", 5, 4); // [5..8] w_new

        svb = new SimpleVarInfoBuilder(1);
        svb.addVariable(vc);
        svb.addVariable(vn);
        svb.addVariable(wc);
        svb.addVariable(wn);
    }

    @Test
    public void replaceValueFailTest() {
        Tree t = new Tree();
        VarInfo vic = svb.getVarInfo(vc, 0);
        VarInfo vin = svb.getVarInfo(vn, 0);

        Node vicEq6 = t.buildEqualityValue(vic, 6);
        Node vinEq8 = t.buildEqualityValue(vin, 8);
        Node rel = t.conjunct(vicEq6, vinEq8); // v_current == 6 && v_new == 8

        Node result = t.assign(rel, vin, 7 - 5); // v_new = 7 --> relation is false
        assertSame(result, Tree.ZERO);
        result = t.replace(result, vic, vin);
        assertSame(result, Tree.ZERO);
    }

    @Test
    public void replaceValueInputVarFailTest() {
        Tree t = new Tree();
        VarInfo vic = svb.getVarInfo(wc, 0);
        VarInfo vin = svb.getVarInfo(wn, 0);

        Node vicEq6 = t.buildEqualityValue(vic, 6);
        Node vinEq8 = t.buildEqualityValue(vin, 8);
        Node rel = t.conjunct(vicEq6, vinEq8); // v_current == 6 && v_new == 8

        Node result = t.assign(rel, vin, 7 - 5); // v_new = 7 --> relation is false
        assertSame(result, Tree.ZERO);
        result = t.replace(result, vic, vin);
        assertSame(result, Tree.ZERO);
    }

    @Test
    public void replaceValueOkTest() {
        Tree t = new Tree();
        VarInfo vic = svb.getVarInfo(vc, 0);
        VarInfo vin = svb.getVarInfo(vn, 0);

        Node vicEq6 = t.buildEqualityValue(vic, 6);
        Node vicEq8 = t.buildEqualityValue(vic, 8);
        Node vinEq8 = t.buildEqualityValue(vin, 8);
        Node rel = t.conjunct(vicEq6, vinEq8); // v_current == 6 && v_new == 8

        Node result = t.replace(rel, vic, vin);
        assertSame(result, vicEq8);
    }

    @Test
    public void replaceValueInputVarOkTest() {
        Tree t = new Tree();
        VarInfo vic = svb.getVarInfo(wc, 0);
        VarInfo vin = svb.getVarInfo(wn, 0);

        Node vicEq6 = t.buildEqualityValue(vic, 6);
        Node vicEq8 = t.buildEqualityValue(vic, 8);
        Node vinEq8 = t.buildEqualityValue(vin, 8);
        Node rel = t.conjunct(vicEq6, vinEq8); // v_current == 6 && v_new == 8

        Node result = t.replace(rel, vic, vin);
        assertSame(result, vicEq8);
    }
}
