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

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.common.java.Pair;

/** Model variable order. Variables are ordered as in the model, without interleaving. */
public class ModelVarOrder extends NonInterleavedVarOrder {
    @Override
    public List<Pair<SynthesisVariable, Integer>> order(VarOrderHelper helper, boolean dbgEnabled, int dbgLevel) {
        // Debug output.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying model variable order.");
        }

        // Get variables in model order.
        List<SynthesisVariable> modelOrder = helper.getVariables();

        // Return a non-interleaved variable order.
        return getNonInterleavedOrder(modelOrder);
    }
}
