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

package org.eclipse.escet.cif.datasynth.varorder.orders;

import static org.eclipse.escet.common.java.Lists.copy;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.common.java.Pair;

/** Random variable order. Variables are randomly ordered. No interleaving is used. */
public class RandomVarOrder extends NonInterleavedVarOrder {
    /** The random order seed number in case a fixed seed is to be used, or {@code null} otherwise. */
    private final Long seed;

    /**
     * Constructor for the {@link RandomVarOrder} class.
     *
     * @param seed The random order seed number in case a fixed seed is to be used, or {@code null} otherwise.
     */
    public RandomVarOrder(Long seed) {
        this.seed = seed;
    }

    @Override
    public List<Pair<SynthesisVariable, Integer>> order(VarOrderHelper helper, boolean dbgEnabled, int dbgLevel) {
        // Get variables in model order.
        List<SynthesisVariable> modelOrder = helper.getVariables();

        // Shuffle to random order.
        List<SynthesisVariable> randomOrder = copy(modelOrder);
        if (seed == null) {
            Collections.shuffle(randomOrder);
        } else {
            Collections.shuffle(randomOrder, new Random(seed));
        }

        // Return non-interleaved variable order.
        return getNonInterleavedOrder(randomOrder);
    }
}
