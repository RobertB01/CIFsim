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

package org.eclipse.escet.cif.datasynth.varorder.orders;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;

/** Sorted variable order. Variables are sorted based on their name. No interleaving is used. */
public class SortedVarOrder extends NonInterleavedVarOrder {
    @Override
    public List<Pair<SynthesisVariable, Integer>> order(VarOrderHelper helper, boolean dbgEnabled, int dbgLevel) {
        // Get variables in model order.
        List<SynthesisVariable> modelOrder = helper.getVariables();

        // Sort variables based on their name.
        List<SynthesisVariable> sortedOrder = modelOrder.stream()
                .sorted((v, w) -> Strings.SORTER.compare(v.rawName, w.rawName)).collect(Collectors.toList());

        // Return a non-interleaved variable order.
        return getNonInterleavedOrder(sortedOrder);
    }
}
