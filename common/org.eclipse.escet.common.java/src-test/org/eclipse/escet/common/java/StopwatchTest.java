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

package org.eclipse.escet.common.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Unit tests for the {@link Stopwatch} class. */
@SuppressWarnings("javadoc")
public class StopwatchTest {
    @Test
    public void testCreate() {
        Stopwatch stopwatch = new Stopwatch();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(false, stopwatch.hasMeasured());
        assertEquals(0, stopwatch.getDurationNanos());
        assertEquals(0.0, stopwatch.getDurationMicros(), 0.0);
        assertEquals(0.0, stopwatch.getDurationMillis(), 0.0);
        assertEquals(0.0, stopwatch.getDurationSecs(), 0.0);
    }

    @Test
    public void testRun1() {
        Stopwatch stopwatch = new Stopwatch();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(false, stopwatch.hasMeasured());
        assertEquals(0, stopwatch.getDurationNanos());
        assertEquals(0.0, stopwatch.getDurationMicros(), 0.0);
        assertEquals(0.0, stopwatch.getDurationMillis(), 0.0);
        assertEquals(0.0, stopwatch.getDurationSecs(), 0.0);

        stopwatch.start();
        assertEquals(true, stopwatch.isRunning());
        assertEquals(true, stopwatch.hasMeasured());

        ThreadUtils.sleep(550);
        stopwatch.stop();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(true, stopwatch.hasMeasured());
        assertTrue(stopwatch.getDurationNanos() > 500_000_000);
        assertTrue(stopwatch.getDurationMicros() > 500_000);
        assertTrue(stopwatch.getDurationMillis() > 500);
        assertTrue(stopwatch.getDurationSecs() > 0.5);
    }

    @Test
    public void testRun2() {
        Stopwatch stopwatch = new Stopwatch();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(false, stopwatch.hasMeasured());
        assertEquals(0, stopwatch.getDurationNanos());
        assertEquals(0.0, stopwatch.getDurationMicros(), 0.0);
        assertEquals(0.0, stopwatch.getDurationMillis(), 0.0);
        assertEquals(0.0, stopwatch.getDurationSecs(), 0.0);

        stopwatch.start();
        assertEquals(true, stopwatch.isRunning());
        assertEquals(true, stopwatch.hasMeasured());

        ThreadUtils.sleep(550);
        stopwatch.stop();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(true, stopwatch.hasMeasured());
        assertTrue(stopwatch.getDurationNanos() > 500_000_000);
        assertTrue(stopwatch.getDurationMicros() > 500_000);
        assertTrue(stopwatch.getDurationMillis() > 500);
        assertTrue(stopwatch.getDurationSecs() > 0.5);

        stopwatch.start();
        assertEquals(true, stopwatch.isRunning());
        assertEquals(true, stopwatch.hasMeasured());

        ThreadUtils.sleep(550);
        stopwatch.stop();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(true, stopwatch.hasMeasured());
        assertTrue(stopwatch.getDurationNanos() > 1_000_000_000);
        assertTrue(stopwatch.getDurationMicros() > 1_000_000);
        assertTrue(stopwatch.getDurationMillis() > 1_000);
        assertTrue(stopwatch.getDurationSecs() > 1);
    }

    @Test
    public void testStartAlreadyRunning() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        assertThrows(IllegalStateException.class, () -> stopwatch.start());
    }

    @Test
    public void testStopNotRunning() {
        Stopwatch stopwatch = new Stopwatch();
        assertThrows(IllegalStateException.class, () -> stopwatch.stop());
    }

    @Test
    public void testReset() {
        Stopwatch stopwatch = new Stopwatch();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(false, stopwatch.hasMeasured());
        assertEquals(0, stopwatch.getDurationNanos());
        assertEquals(0.0, stopwatch.getDurationMicros(), 0.0);
        assertEquals(0.0, stopwatch.getDurationMillis(), 0.0);
        assertEquals(0.0, stopwatch.getDurationSecs(), 0.0);

        stopwatch.reset();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(false, stopwatch.hasMeasured());
        assertEquals(0, stopwatch.getDurationNanos());
        assertEquals(0.0, stopwatch.getDurationMicros(), 0.0);
        assertEquals(0.0, stopwatch.getDurationMillis(), 0.0);
        assertEquals(0.0, stopwatch.getDurationSecs(), 0.0);

        stopwatch.start();
        ThreadUtils.sleep(550);
        stopwatch.reset();
        assertEquals(true, stopwatch.isRunning());
        assertEquals(true, stopwatch.hasMeasured());
        assertTrue(stopwatch.getDurationNanos() < 100_000_000);
        assertTrue(stopwatch.getDurationMicros() < 100_000);
        assertTrue(stopwatch.getDurationMillis() < 100);
        assertTrue(stopwatch.getDurationSecs() < 0.1);

        ThreadUtils.sleep(10);
        stopwatch.stop();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(true, stopwatch.hasMeasured());
        assertTrue(stopwatch.getDurationNanos() > 0);
        assertTrue(stopwatch.getDurationMicros() > 0);
        assertTrue(stopwatch.getDurationMillis() > 0);
        assertTrue(stopwatch.getDurationSecs() > 0);

        stopwatch.reset();
        assertEquals(false, stopwatch.isRunning());
        assertEquals(false, stopwatch.hasMeasured());
        assertEquals(0, stopwatch.getDurationNanos());
        assertEquals(0.0, stopwatch.getDurationMicros(), 0.0);
        assertEquals(0.0, stopwatch.getDurationMillis(), 0.0);
        assertEquals(0.0, stopwatch.getDurationSecs(), 0.0);
    }

    @Test
    public void testDuration() {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        ThreadUtils.sleep(100);
        stopwatch.stop();
        long t = stopwatch.getDurationNanos();

        double t2 = t / 1e3;
        assertEquals(t2, stopwatch.getDurationMicros(), 1e-12);

        double t3 = t2 / 1e3;
        assertEquals(t3, stopwatch.getDurationMillis(), 1e-9);

        double t4 = t3 / 1e3;
        assertEquals(t4, stopwatch.getDurationSecs(), 1e-6);
    }
}
