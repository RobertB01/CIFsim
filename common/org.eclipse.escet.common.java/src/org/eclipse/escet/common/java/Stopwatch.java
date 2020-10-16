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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Strings.fmt;

/** Stopwatch to measure passage of time. */
public class Stopwatch {
    /**
     * The measured duration, in nanoseconds. Does not include the duration of the current run, if currently
     * {@link #running}.
     */
    private long duration = 0;

    /** The start time of the current run, in nanoseconds. Is {@code -1} if not currently {@link #running}. */
    private long startNano = -1;

    /** Whether the stopwatch is currently running. */
    private boolean running = false;

    /** Has anything has been measured? */
    private boolean measured = false;

    /**
     * Is the stopwatch currently running?
     *
     * @return {@code true} if the stopwatch is currently running, {@code false} otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Has anything been measured? Nothing has been measured if the stopwatch hasn't been started, as well as when it is
     * {@link #reset} while it is not {@link #running}.
     *
     * @return {@code true} anything has been measured, {@code false} otherwise.
     */
    public boolean hasMeasured() {
        return measured;
    }

    /**
     * Start/resume the stopwatch.
     *
     * @throws IllegalStateException If the stopwatch is already running.
     */
    public void start() {
        if (running) {
            throw new IllegalStateException("Stopwatch is already running.");
        }
        startNano = System.nanoTime();
        running = true;
        measured = true;
    }

    /**
     * Stop/pause the stopwatch.
     *
     * @throws IllegalStateException If the stopwatch is not running.
     */
    public void stop() {
        if (!running) {
            throw new IllegalStateException("Stopwatch is not running.");
        }
        duration += System.nanoTime() - startNano;
        startNano = -1;
        running = false;
    }

    /** Resets the time measured so far. */
    public void reset() {
        duration = 0;
        if (running) {
            startNano = System.nanoTime();
        } else {
            measured = false;
        }
    }

    /**
     * Returns the time measured so far, in nanoseconds.
     *
     * @return The time measured so far, in nanoseconds.
     */
    public long getDurationNanos() {
        long total = duration;
        if (running) {
            total += System.nanoTime() - startNano;
        }
        return total;
    }

    /**
     * Returns the time measured so far, in microseconds.
     *
     * @return The time measured so far, in microseconds.
     */
    public double getDurationMicros() {
        return getDurationNanos() / 1e3;
    }

    /**
     * Returns the time measured so far, in milliseconds.
     *
     * @return The time measured so far, in milliseconds.
     */
    public double getDurationMillis() {
        return getDurationNanos() / 1e6;
    }

    /**
     * Returns the time measured so far, in seconds.
     *
     * @return The time measured so far, in seconds.
     */
    public double getDurationSecs() {
        return getDurationNanos() / 1e9;
    }

    @Override
    public String toString() {
        return fmt("%d ns = %,.0f us = %,.0f ms = %,.0f s", getDurationNanos(), getDurationMicros(),
                getDurationMillis(), getDurationSecs());
    }
}
