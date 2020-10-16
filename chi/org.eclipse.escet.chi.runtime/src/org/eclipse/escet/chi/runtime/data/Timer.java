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

package org.eclipse.escet.chi.runtime.data;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.SelectChoice;
import org.eclipse.escet.common.java.removablelist.RemovableList;

/** Timer in a Chi process. */
public class Timer implements Comparable<Timer> {
    /** Simulation coordinator. */
    private ChiCoordinator chiCoordinator;

    /** Time when the timer expires in the simulation. */
    public final double endTime;

    /**
     * The authorative timer of this moment in time (exists in the SimulationData.timers set). {@code null} if the timer
     * already expired.
     */
    public final Timer authorativeTimer;

    /** Select choices blocked on a delaying guard. */
    public RemovableList<SelectChoice> choices = new RemovableList<>();

    /**
     * Constructor of the {@link Timer} class.
     *
     * @param chiCoordinator Simulation coordinator managing the timers.
     */
    public Timer(ChiCoordinator chiCoordinator) {
        this(chiCoordinator, 0);
    }

    /**
     * Constructor of the {@link Timer} class.
     *
     * @param chiCoordinator Simulation coordinator managing the timers.
     * @param endTime Simulation time of the timer expiration.
     */
    public Timer(ChiCoordinator chiCoordinator, double endTime) {
        this.chiCoordinator = chiCoordinator;
        this.endTime = endTime;
        authorativeTimer = chiCoordinator.addTimer(this);
    }

    /**
     * Get remaining delay time.
     *
     * @return The remaining delay time.
     */
    public double getRemaining() {
        double remaining = endTime - chiCoordinator.getCurrentTime();
        if (remaining < 0) {
            return 0;
        }
        return remaining;
    }

    /**
     * Has the timer reached timeout?
     *
     * @return Timeout of the timer has been reached,
     */
    public boolean isReady() {
        return chiCoordinator.getCurrentTime() >= endTime;
    }

    /**
     * Make a copy of this timer.
     *
     * @param chiCoordinator Simulation coordinator managing the timers.
     * @return A copy of this timer.
     */
    public Timer copyTimer(ChiCoordinator chiCoordinator) {
        Timer t = new Timer(chiCoordinator, endTime);
        return t;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Timer) {
            Timer tother = (Timer)other;
            return endTime == tother.endTime;
        }
        return false;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(endTime);
        return (int)(bits ^ (bits >>> 32));
    }

    @Override
    public int compareTo(Timer other) {
        if (other == null) {
            throw new NullPointerException();
        }
        if (endTime < other.endTime) {
            return -1;
        }
        if (endTime > other.endTime) {
            return 1;
        }
        return 0;
    }
}
