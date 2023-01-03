//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Pair.pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.common.java.Pair;

/** Base class for non-interleaved variable orders. */
public abstract class NonInterleavedVarOrder implements VarOrder {
    /**
     * Produces a non-interleaved variable order from ordered variables.
     *
     * @param variables The ordered variables.
     * @return The variable order, without any interleaving.
     */
    protected List<Pair<SynthesisVariable, Integer>> getNonInterleavedOrder(List<SynthesisVariable> variables) {
        return IntStream.range(0, variables.size()).mapToObj(i -> pair(variables.get(i), i))
                .collect(Collectors.toList());
    }
}
