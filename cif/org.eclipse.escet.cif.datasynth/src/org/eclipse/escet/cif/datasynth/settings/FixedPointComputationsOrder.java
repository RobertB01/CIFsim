//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.settings;

import static org.eclipse.escet.cif.datasynth.settings.FixedPointComputation.CTRL;
import static org.eclipse.escet.cif.datasynth.settings.FixedPointComputation.NONBLOCK;
import static org.eclipse.escet.cif.datasynth.settings.FixedPointComputation.REACH;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;

import org.eclipse.escet.common.java.Assert;

/** Fixed-point computations order. */
public enum FixedPointComputationsOrder {
    /** Non-blocking -> controllable -> reachable. */
    NONBLOCK_CTRL_REACH(NONBLOCK, CTRL, REACH),

    /** Non-blocking -> reachable -> controllable. */
    NONBLOCK_REACH_CTRL(NONBLOCK, REACH, CTRL),

    /** Controllable -> non-blocking -> reachable. */
    CTRL_NONBLOCK_REACH(CTRL, NONBLOCK, REACH),

    /** Controllable -> reachable -> non-blocking. */
    CTRL_REACH_NONBLOCK(CTRL, REACH, NONBLOCK),

    /** Reachable -> non-blocking -> controllable. */
    REACH_NONBLOCK_CTRL(REACH, NONBLOCK, CTRL),

    /** Reachable -> controllable -> non-blocking. */
    REACH_CTRL_NONBLOCK(REACH, CTRL, NONBLOCK);

    /** The computations to perform, in the order to perform them. */
    public final List<FixedPointComputation> computations;

    /**
     * Constructor of the {@link FixedPointComputationsOrder} enum.
     *
     * @param computations The computations to perform, in the order to perform them.
     */
    private FixedPointComputationsOrder(FixedPointComputation... computations) {
        Assert.areEqual(FixedPointComputation.values().length, set(computations).size());
        this.computations = List.of(computations);
    }
}
