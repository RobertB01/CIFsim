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

package org.eclipse.escet.cif.datasynth.varorder.helper;

import java.util.BitSet;
import java.util.List;

/** Variable orderer metric. Lower metric values (heuristically) indicate better variable orders. */
public interface VarOrdererMetric {
    /**
     * Compute the metric value. Lower metric values (heuristically) indicate better variable orders.
     *
     * @param newIndices For each variable, its new 0-based index.
     * @param hyperEdges The hyper-edges to use to compute the metric value.
     * @return The metric value.
     */
    public double computeForNewIndices(int[] newIndices, List<BitSet> hyperEdges);
}
