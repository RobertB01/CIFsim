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

package org.eclipse.escet.cif.simulator.options;

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Distributions initial seed option. */
public class DistributionSeedOption extends IntegerOption {
    /** Constructor for the {@link DistributionSeedOption} class. */
    public DistributionSeedOption() {
        super("Initial seed for stochastic distributions",
                "The initial seed to use for the first random generator for stochastic distributions, or \"random\" "
                        + "for a random initial seed. Value must be in the range [1 .. 2^30]. [DEFAULT=random]",
                null, "distributions-seed", "DSEED", null, 1, 1 << 30, 1, true,
                "The initial seed to use for the first random generator for stochastic distributions. Must be "
                        + "greater or equal to zero.",
                "Initial seed:", true, 123, "random", "Random initial seed", "Specific initial seed");
    }

    /**
     * Returns the initial seed to use for the first random generator for stochastic distributions, or {@code null} to
     * use a random initial seed.
     *
     * @return The initial seed, or {@code null}.
     */
    public static Integer getInitialSeed() {
        return Options.get(DistributionSeedOption.class);
    }
}
