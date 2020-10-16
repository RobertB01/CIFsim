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

package org.eclipse.escet.cif.simulator.tests;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.random.MersenneTwister;
import org.eclipse.escet.cif.simulator.runtime.distributions.CifMersenneTwister;
import org.eclipse.escet.cif.simulator.runtime.distributions.CifRandomGenerator;
import org.junit.Test;

/** Unit tests for the {@link CifMersenneTwister} class. */
public class CifMersenneTwisterTest {
    /** Compare {@link MersenneTwister} and {@link CifMersenneTwister} output. */
    @Test
    public void testCompare() {
        MersenneTwister mt = new MersenneTwister(123L);
        CifMersenneTwister cmt = new CifMersenneTwister(123L);
        assertEquals(mt.nextDouble(), cmt.draw(), 0.0d);
        assertEquals(mt.nextDouble(), cmt.draw(), 0.0d);
        assertEquals(mt.nextDouble(), cmt.draw(), 0.0d);

        mt.setSeed(234L);
        cmt.setSeed(234L);
        assertEquals(mt.nextDouble(), cmt.draw(), 0.0d);
        assertEquals(mt.nextDouble(), cmt.draw(), 0.0d);
        assertEquals(mt.nextDouble(), cmt.draw(), 0.0d);
    }

    /** Test {@link CifMersenneTwister#copy}. */
    @Test
    public void testCopy() {
        MersenneTwister mt = new MersenneTwister(123L);
        CifRandomGenerator r1 = new CifMersenneTwister(123L);
        CifRandomGenerator r2 = r1.copy();

        double v1 = mt.nextDouble();
        double v2 = r1.draw();
        double v3 = r2.draw();
        assertEquals(v1, v2, 0.0d);
        assertEquals(v2, v3, 0.0d);

        double w1 = mt.nextDouble();
        double x1 = mt.nextDouble();
        double w2 = r1.draw();
        double x2 = r1.draw();
        double w3 = r2.draw();
        double x3 = r2.draw();
        assertEquals(w1, w2, 0.0d);
        assertEquals(w2, w3, 0.0d);
        assertEquals(x1, x2, 0.0d);
        assertEquals(x2, x3, 0.0d);
    }
}
