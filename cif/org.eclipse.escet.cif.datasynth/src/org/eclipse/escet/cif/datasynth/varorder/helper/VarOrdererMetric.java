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

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;

/** Variable orderer metric. Lower metric values (heuristically) indicate better variable orders. */
public interface VarOrdererMetric {
    /**
     * Compute the metric.
     *
     * @param helper Helper for variable ordering algorithms.
     * @param order The variable order.
     * @return The metric.
     */
    public double compute(VarOrdererHelper helper, List<SynthesisVariable> order);
}
