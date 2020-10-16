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

package org.eclipse.escet.cif.simulator.input;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;

import java.util.List;
import java.util.Random;

import org.eclipse.escet.cif.simulator.options.AutoAlgoOption;
import org.eclipse.escet.cif.simulator.options.AutoTimeDuration;
import org.eclipse.escet.cif.simulator.options.AutoTimeDurationOption;
import org.eclipse.escet.cif.simulator.output.NormalOutputOption;
import org.eclipse.escet.cif.simulator.output.NormalOutputType;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.java.Assert;

/**
 * Input component that automatically takes decisions without any user interaction. The way the automatic decisions are
 * taken can be controlled via {@link AutoAlgoOption}.
 *
 * @param <S> The type of state objects to use.
 */
public final class AutomaticInputComponent<S extends RuntimeState> extends InputComponent<S> {
    /** The automatic input mode choice algorithm. See {@link AutoAlgoOption}. */
    private final int transChoice;

    /** The automatic input mode time duration. See {@link AutoTimeDurationOption}. */
    private final AutoTimeDuration timeChoice;

    /**
     * The random generator to use to randomly choose transitions. If the choice algorithm is not random, then the
     * random generator is {@code null}.
     *
     * @see AutoAlgoOption
     */
    private final Random transRandom;

    /**
     * The random generator to use to randomly choose time transition durations. If the choice algorithm is not random,
     * then the random generator is {@code null}.
     *
     * @see AutoTimeDurationOption
     */
    private final Random timeRandom;

    /**
     * The next time at which to interrupt a time transition, for random automatic time duration selection. May be
     * {@code -1} if not applicable, or if not yet initialized.
     */
    private double nextInterruptTime = -1.0;

    /**
     * Constructor for the {@link AutomaticInputComponent} class.
     *
     * @param spec The specification. The specification has not yet been {@link RuntimeSpec#init initialized}.
     */
    public AutomaticInputComponent(RuntimeSpec<S> spec) {
        super(spec);

        // Initialize random generator for transition choices.
        Integer transSeed = null;
        transChoice = AutoAlgoOption.getAutoAlgo();
        if (transChoice == -2 || transChoice == -3) {
            // First/last.
            transRandom = null;
        } else if (transChoice >= 0) {
            // Specified seed.
            transRandom = new Random(transChoice);
        } else {
            // Calculate a random seed in the [0..2^30] interval.
            Assert.check(transChoice == -1);
            transSeed = new Random().nextInt((1 << 30) + 1);
            transRandom = new Random(transSeed);
            if (NormalOutputOption.doPrint(NormalOutputType.SEEDS)) {
                out("Using seed %d for random automatic mode choice algorithm.", transSeed);
            }
        }

        // Initialize random generator for time transition duration choices.
        Integer timeSeed = null;
        timeChoice = AutoTimeDurationOption.getAutoTimeDuration();
        if (!timeChoice.random) {
            // Maximum.
            timeRandom = null;
        } else if (timeChoice.seed != null) {
            // Specified seed.
            timeRandom = new Random(timeChoice.seed);
        } else {
            // Calculate a random seed in the [0..2^30] interval.
            timeSeed = new Random().nextInt((1 << 30) + 1);
            timeRandom = new Random(timeSeed);
            if (NormalOutputOption.doPrint(NormalOutputType.SEEDS)) {
                out("Using seed %d for random automatic mode time duration.", timeSeed);
            }
        }

        // Store the seeds. This is useful for crash reports.
        AppEnv.setProperty("org.eclipse.escet.cif.simulator.input.auto.transSeed",
                (transSeed == null) ? "n/a" : transSeed.toString());
        AppEnv.setProperty("org.eclipse.escet.cif.simulator.input.auto.timeSeed",
                (timeSeed == null) ? "n/a" : timeSeed.toString());
    }

    @Override
    public Transition<S> chooseTransition(S state, List<Transition<S>> transitions, SimulationResult result) {
        // If no transitions possible, stop simulating.
        if (transitions.isEmpty()) {
            throw new SimulatorExitException(result);
        }

        // Case distinction based on automatic mode choice algorithm.
        switch (transChoice) {
            case -2:
                // First one (deterministic).
                return first(transitions);

            case -3:
                // Last one (deterministic).
                return last(transitions);

            default: {
                // Randomly select a transition.
                int idx = transRandom.nextInt(transitions.size());
                return transitions.get(idx);
            }
        }
    }

    @Override
    public Double getNextMaxEndTime(S state) {
        // Maximum delay.
        if (!timeChoice.random) {
            return null;
        }

        // Random delay. If we have reached the next interruption time, choose
        // a new one, in the future.
        double curTime = state.getTime();
        while (nextInterruptTime <= curTime) {
            double sample = timeRandom.nextDouble() * timeChoice.upper;
            nextInterruptTime += sample;
            if (nextInterruptTime > curTime) {
                break;
            }
        }

        // Restrict the next time transition duration, to make sure that the
        // time transitions don't go past the interruption time. That is, this
        // ensures that the interruption time will at some point become the end
        // time of a time transition.
        return nextInterruptTime;
    }

    @Override
    public ChosenTargetTime chooseTargetTime(S state, double maxTargetTime) {
        // Maximum delay.
        if (!timeChoice.random) {
            return new ChosenTargetTime(state.getTime(), maxTargetTime, true);
        }

        // Random delay. If enough time is left, take the entire time
        // transition.
        double sourceTime = state.getTime();
        if (nextInterruptTime >= maxTargetTime) {
            return new ChosenTargetTime(state.getTime(), maxTargetTime, true);
        }

        // Take a partial time transition. Note that we requested a time
        // transition not past 'nextInterruptTime', but due to rounding etc,
        // we may end up with a slightly longer one. Taking the remaining time
        // will then result in taking the entire time transition after all.
        return new ChosenTargetTime(sourceTime, nextInterruptTime, true);
    }
}
