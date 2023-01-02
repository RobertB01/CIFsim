//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.common.java.Pair;

/** Custom variable order. Variables are ordered and interleaved as configured. */
public class CustomVarOrder extends NonInterleavedVarOrder {
    /** The variable order. */
    private final List<Pair<SynthesisVariable, Integer>> order;

    /**
     * Constructor for the {@link CustomVarOrder} class.
     *
     * @param order The variable order.
     */
    public CustomVarOrder(List<Pair<SynthesisVariable, Integer>> order) {
        this.order = Collections.unmodifiableList(order);
    }

    @Override
    public List<Pair<SynthesisVariable, Integer>> order(VarOrderHelper helper, boolean dbgEnabled, int dbgLevel) {
        return order;
    }
}