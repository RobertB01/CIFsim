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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.java.Assert;

/** Value of the {@link AutoTimeDurationOption}. */
public class AutoTimeDuration {
    /** Whether to choose randomly ({@code true}) or always choose the maximum allowed delay ({@code false}). */
    public final boolean random;

    /**
     * The upper bound of the uniform integer distribution, {@code upper > 0}. Is {@code null} if and only if
     * {@link #random} is {@code false}.
     */
    public final Double upper;

    /**
     * The initial seed for the uniform integer distribution, {@code seed in [0..2^30]}. Must be {@code null} if
     * {@link #random} is {@code false}, and may be {@code null} otherwise, to use a random initial seed.
     */
    public final Integer seed;

    /**
     * Constructor for the {@link AutoTimeDuration} class.
     *
     * @param random Whether to choose randomly ({@code true}) or always choose the maximum allowed delay
     *     ({@code false}).
     * @param upper The upper bound of the uniform integer distribution, {@code upper > 0}. Is {@code null} if and only
     *     if {@code random} is {@code false}.
     * @param seed The initial seed for the uniform integer distribution, {@code seed in [0..2^30]}. Must be
     *     {@code null} if {@code random} is {@code false}, and may be {@code null} otherwise, to use a random initial
     *     seed.
     */
    public AutoTimeDuration(boolean random, Double upper, Integer seed) {
        this.random = random;
        this.upper = upper;
        this.seed = seed;
        Assert.ifAndOnlyIf(!random, upper == null);
        Assert.implies(!random, seed == null);
    }
}
