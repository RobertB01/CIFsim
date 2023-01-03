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

/** Thread utility methods. */
public class ThreadUtils {
    /** Constructor for the {@link ThreadUtils} class. */
    private ThreadUtils() {
        // Static class.
    }

    /**
     * Causes the currently executing thread to sleep (temporarily cease execution) for the specified number of
     * milliseconds, subject to the precision and accuracy of system timers and schedulers. The thread does not lose
     * ownership of any monitors.
     *
     * <p>
     * This method is exactly like {@link Thread#sleep}, but if a checked {@link InterruptedException} is thrown, it is
     * wrapped in an unchecked {@link RuntimeException}.
     * </p>
     *
     * @param millis The length of time to sleep, in milliseconds.
     * @throws IllegalArgumentException If the value of {@code millis} is negative.
     * @throws RuntimeException If any thread has interrupted the current thread. The interrupted status of the current
     *     thread is cleared when this exception is thrown.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Thread sleep interrupted.", ex);
        }
    }
}
