//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.chi.runtime.data.DefinitionKind;
import org.eclipse.escet.common.java.Assert;

/**
 * Class for storing the results of a simulation.
 *
 * <p>
 * Objects also have information about random numbers which is only valid after all simulations have been executed.
 * </p>
 */
public class SimulationResult {
    /** Kind of definition that was executed. */
    public final DefinitionKind kind;

    /** Whether the simulator generated random numbers at all (only useful after the simulator ended). */
    public final boolean usedRandom;

    /** Seed of the simulator (only useful after the simulator ended). */
    public final long seed;

    /** Reason for ending the simulation. */
    public final ExitReason exitReason;

    /** Simulation time at the end of the simulation. */
    public final double endTime;

    /** Exit value ({@code null} if no 'exit' done, or with 'void' exit). */
    public final Object exitValue;

    /**
     * Constructor of the {@link SimulationResult} class.
     *
     * @param kind Kind of definition that started this (sub)simulation.
     * @param usedRandom Whether the simulator generated random numbers at all (only useful after the simulator ended).
     * @param seed Seed of the simulator (only useful after the simulator ended).
     * @param exitReason Reason why the simulation ended.
     * @param endTime Simulation time at the end of the simulation.
     * @param exitValue Exit value ({@code null} if no 'exit' done, or with 'void' exit).
     */
    public SimulationResult(DefinitionKind kind, boolean usedRandom, long seed, ExitReason exitReason, double endTime,
            Object exitValue)
    {
        Assert.check(exitReason != null);

        this.kind = kind;
        this.usedRandom = usedRandom;
        this.seed = seed;
        this.exitReason = exitReason;
        this.endTime = endTime;
        this.exitValue = exitValue;
    }

    /**
     * Output a human readable description of the simulation result (only useful after all simulations have been done).
     *
     * @param seedProvided Whether a seed was provided by the user.
     * @return Text describing the simulation result.
     */
    public String getInfo(boolean seedProvided) {
        String msg = "";
        if (kind.equals(DefinitionKind.MODEL)) {
            switch (exitReason) {
                case ABORTED:
                    msg = fmt("Simulation aborted at time %.2f", endTime);
                    break;

                case ERROR:
                    msg = fmt("Simulation runtime error at time %.2f", endTime);
                    break;

                case DEADLOCKED:
                    msg = fmt("Deadlock detected at time %.2f", endTime);
                    break;

                case FINISHED:
                    msg = fmt("All processes finished at time %.2f", endTime);
                    break;

                case EXITED:
                    msg = fmt("Simulation performed exit at time %.2f", endTime);
                    break;

                default:
                    Assert.fail("Unexpected fail reason.");
            }
            if (usedRandom) {
                // At least one non-constant distribution created.
                msg += fmt(" (initial seed was %d)", seed);
            } else if (seedProvided) {
                // Initial seed specified, but no distributions were created.
                msg += fmt(" (seed %d was not used)", seed);
            }
            msg += ".";
        } else {
            Assert.check(kind.equals(DefinitionKind.XPER));
            if (usedRandom) {
                // At least one non-constant distribution created.
                msg = fmt("initial seed was %d.", seed);
            } else if (seedProvided) {
                // Initial seed specified, but no distributions were created.
                msg = fmt("seed %d was not used.", seed);
            }
            if (exitReason.equals(ExitReason.ABORTED)) {
                msg = fmt("Simulation was aborted (%s)", msg);
            } else {
                msg = StringUtils.capitalize(msg);
            }
        }
        if (exitValue != null) {
            if (!msg.isEmpty()) {
                msg += "\n";
            }
            msg += "Simulation ended with exit value " + exitValue.toString() + ".";
        }
        return msg;
    }

    /** Reasons for ending the simulation. */
    public static enum ExitReason {
        /** Simulation was stopped by request of the user. */
        ABORTED,

        /** Simulation got in a deadlock. */
        DEADLOCKED,

        /** Simulation ended due to a runtime error. */
        ERROR,

        /** Simulation exited due to 'exit'. */
        EXITED,

        /** All processes finished. */
        FINISHED,
    }
}
