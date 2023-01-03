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

package org.eclipse.escet.cif.datasynth.varorder.orderers;

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;

/** Variable ordering algorithm. */
public interface VarOrderer {
    /**
     * Apply the variable ordering algorithm, to order the variables.
     *
     * <p>
     * In general, there are no guarantees that the new order is always a 'better' order, though some algorithms may
     * offer such guarantees. Some heuristic algorithms may in certain cases even produce 'worse' orders.
     * </p>
     *
     * @param helper Helper for variable ordering.
     * @param inputOrder The input variable order (to attempt) to improve. Must not be changed in-place.
     * @param dbgEnabled Whether debug output is enabled.
     * @param dbgLevel The debug indentation level.
     * @return The new variable order, as produced by the algorithm.
     */
    public List<SynthesisVariable> order(VarOrderHelper helper, List<SynthesisVariable> inputOrder,
            boolean dbgEnabled, int dbgLevel);
}
