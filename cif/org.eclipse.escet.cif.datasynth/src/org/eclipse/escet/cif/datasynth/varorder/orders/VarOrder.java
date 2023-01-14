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

/** Variable order. */
public interface VarOrder {
    /**
     * Produce a variable order.
     *
     * @param helper Helper for variable ordering, with variables in model order.
     * @param dbgEnabled Whether debug output is enabled.
     * @param dbgLevel The debug indentation level.
     * @return The variable order. Each variable is accompanied with its 0-based group index number. Variables are in
     *     the same group if their BDD domains are interleaved. Variables must be given group index numbers in
     *     increasing manner, with the first variable having group index number zero.
     */
    public List<Pair<SynthesisVariable, Integer>> order(VarOrderHelper helper, boolean dbgEnabled, int dbgLevel);
}
