//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Sets.setc;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.escet.cif.bdd.settings.CifBddStatistics;

/**
 * Synthesis statistics.
 *
 * <p>
 * These statistics to choose from must at least include those from {@link CifBddStatistics} for {@link #toCifBdd} to
 * function correctly.
 * </p>
 */
public enum SynthesisStatistics {
    /** BDD garbage collection. */
    BDD_GC_COLLECT,

    /** BDD node table resize. */
    BDD_GC_RESIZE,

    /** BDD cache. */
    BDD_PERF_CACHE,

    /** Continuous BDD performance. */
    BDD_PERF_CONT,

    /** Maximum used BDD nodes. */
    BDD_PERF_MAX_NODES,

    /** Controlled system states. */
    CTRL_SYS_STATES,

    /** Timing. */
    TIMING,

    /** Maximum used memory. */
    MAX_MEMORY;

    /**
     * Converts a set of synthesis statistics to a set of CIF/BDD-related statistics. Leaves out the statistics that are
     * not CIF/BDD-related.
     *
     * @param synthesisStatistics The synthesis statistics.
     * @return The CIF/BDD-related statistics.
     */
    public static EnumSet<CifBddStatistics> toCifBdd(EnumSet<SynthesisStatistics> synthesisStatistics) {
        // Get the statistics to include.
        Set<CifBddStatistics> cifBddStatistics = setc(synthesisStatistics.size());
        for (SynthesisStatistics synthesisStatistic: synthesisStatistics) {
            try {
                CifBddStatistics cifBddStatistic = CifBddStatistics.valueOf(synthesisStatistic.name());
                cifBddStatistics.add(cifBddStatistic);
            } catch (IllegalArgumentException e) {
                // Filter out synthesis statistics that are not CIF/BDD statistics.
            }
        }

        // Return an enum set with the statistics.
        EnumSet<CifBddStatistics> result = EnumSet.noneOf(CifBddStatistics.class);
        result.addAll(cifBddStatistics);
        return result;
    }
}
