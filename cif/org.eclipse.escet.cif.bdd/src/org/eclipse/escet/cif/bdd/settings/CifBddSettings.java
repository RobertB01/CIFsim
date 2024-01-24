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

package org.eclipse.escet.cif.bdd.settings;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.function.Supplier;

import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.output.BlackHoleOutputProvider;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** CIF/BDD-related settings. */
public class CifBddSettings {
    /**
     * Function that indicates whether termination has been requested. Once it returns {@code true}, it must return
     * {@code true} also on subsequent calls.
     */
    public Supplier<Boolean> shouldTerminate = () -> false;

    /** Callback for debug output. */
    public DebugNormalOutput debugOutput = new BlackHoleOutputProvider().getDebugOutput();

    /** Callback for normal output. */
    public DebugNormalOutput normalOutput = new BlackHoleOutputProvider().getNormalOutput();

    /** Callback for warning output. */
    public WarnOutput warnOutput = new BlackHoleOutputProvider().getWarnOutput();

    /** Whether to warn for plants that reference requirement state ({@code true}) or don't warn ({@code false}). */
    public boolean doPlantsRefReqsWarn = CifBddSettingsDefaults.DO_PLANTS_REF_REQS_WARN_DEFAULT;

    /** Events for which to allow non-determinism. */
    public AllowNonDeterminism allowNonDeterminism = CifBddSettingsDefaults.ALLOW_NON_DETERMINISM_DEFAULT;

    /**
     * The initial size of the node table of the BDD library. The value is in the positive range {@code [1 .. 2^31-1]}.
     */
    public int bddInitNodeTableSize = CifBddSettingsDefaults.BDD_INIT_NODE_TABLE_SIZE_DEFAULT;

    /**
     * The ratio of the size of the operation cache of the BDD library to the size of the node table of the BDD library.
     * The value is in the range {@code [0.01 .. 1000]}. This setting only has an effect if {@link #bddOpCacheSize} is
     * {@code null}.
     */
    public double bddOpCacheRatio = CifBddSettingsDefaults.BDD_OP_CACHE_RATIO_DEFAULT;

    /**
     * The fixed size of the operation cache of the BDD library. {@code null} means a fixed cache size is disabled. If
     * not {@code null}, the value is in the range {@code [2 .. 2^31-1]}. If enabled, this setting takes priority over
     * {@link #bddOpCacheRatio}.
     */
    public Integer bddOpCacheSize = CifBddSettingsDefaults.BDD_OP_CACHE_SIZE_DEFAULT;

    /** The initial BDD variable ordering and domain interleaving. */
    public String bddVarOrderInit = CifBddSettingsDefaults.VAR_ORDER_INIT_DEFAULT;

    /**
     * Whether to apply the DCSH variable ordering algorithm to improve the initial variable ordering ({@code true}), or
     * not apply it ({@code false}).
     */
    public boolean bddDcshEnabled = CifBddSettingsDefaults.DCSH_ENABLED_DEFAULT;

    /**
     * Whether to apply the FORCE variable ordering algorithm to improve the initial variable ordering ({@code true}),
     * or not apply it ({@code false}).
     */
    public boolean bddForceEnabled = CifBddSettingsDefaults.FORCE_ENABLED_DEFAULT;

    /**
     * Whether to apply the sliding window variable ordering algorithm to improve the initial variable ordering
     * ({@code true}), or not apply it ({@code false}).
     */
    public boolean bddSlidingWindowEnabled = CifBddSettingsDefaults.SLIDING_WINDOW_ENABLED_DEFAULT;

    /**
     * The maximum length of the window to use for the BDD sliding window variable ordering algorithm. The value is in
     * the range {@code [1 .. 12]}.
     */
    public int bddSlidingWindowMaxLen = CifBddSettingsDefaults.SLIDING_WINDOW_MAX_LEN_DEFAULT;

    /** The advanced BDD variable ordering and domain interleaving. */
    public String bddVarOrderAdvanced = CifBddSettingsDefaults.VAR_ORDER_ADVANCED_DEFAULT;

    /** The algorithm to use to create hyper-edges for BDD variable ordering. */
    public BddHyperEdgeAlgo bddHyperEdgeAlgo = CifBddSettingsDefaults.HYPER_EDGE_ALGO_DEFAULT;

    /**
     * The maximum number of BDD nodes for which to convert a BDD to a readable CNF/DNF representation for the debug
     * output. {@code null} indicates no maximum. If not {@code null}, the value is in the non-negative range
     * {@code [0 .. 2^31-1]}.
     */
    public Integer bddDebugMaxNodes = CifBddSettingsDefaults.BDD_DEBUG_MAX_NODES_DEFAULT;

    /**
     * The maximum number of BDD true paths for which to convert a BDD to a readable CNF/DNF representation for the
     * debug output. {@code null} indicates no maximum. If not {@code null}, the value is in the non-negative range
     * {@code [0 .. 1.7e308]}.
     */
    public Double bddDebugMaxPaths = CifBddSettingsDefaults.BDD_DEBUG_MAX_PATHS_DEFAULT;

    /** The granularity of edges to use in the BDD representation of the CIF specification. */
    public EdgeGranularity edgeGranularity = CifBddSettingsDefaults.EDGE_GRANULARITY_DEFAULT;

    /** The edge ordering to use for backward reachability computations. */
    public String edgeOrderBackward = CifBddSettingsDefaults.EDGE_ORDER_BACKWARD_DEFAULT;

    /** The edge ordering to use for forward reachability computations. */
    public String edgeOrderForward = CifBddSettingsDefaults.EDGE_ORDER_FORWARD_DEFAULT;

    /** Whether duplicate events are allowed for custom edge orders. */
    public EdgeOrderDuplicateEventAllowance edgeOrderAllowDuplicateEvents = CifBddSettingsDefaults.EDGE_ORDER_ALLOW_DUPLICATES_EVENTS_DEFAULT;

    /**
     * Whether to use the edge workset algorithm to dynamically choose the best edge to apply during reachability
     * computations ({@code true}), or not ({@code false}).
     */
    public boolean doUseEdgeWorksetAlgo = CifBddSettingsDefaults.DO_USE_WORKSET_ALGO_DEFAULT;

    /** The kinds of statistics to print. */
    public EnumSet<CifBddStatistics> cifBddStatistics = CifBddSettingsDefaults.CIF_BDD_STATISTICS_DEFAULT.clone();

    /**
     * Constructor for the {@link CifBddSettings} class. Sets {@link CifBddSettingsDefaults default} values for the
     * settings.
     */
    public CifBddSettings() {
        // Make sure the defaults are valid.
        checkSettings();
    }

    /** Check that the settings have valid values, for as much as it can be checked locally. */
    protected void checkSettings() {
        // Check BDD debug max nodes.
        if (bddDebugMaxNodes != null && bddDebugMaxNodes < 0) {
            String msg = fmt("BDD debug max nodes value \"%s\" is not in the range [0 .. 2^31-1].", bddDebugMaxNodes);
            throw new InvalidOptionException(msg);
        }

        // Check BDD debug max paths.
        if (bddDebugMaxPaths != null && Double.isNaN(bddDebugMaxPaths)) {
            throw new InvalidOptionException("BDD debug max paths value must not be NaN.");
        }
        if (bddDebugMaxPaths != null && bddDebugMaxPaths < 0) {
            String msg = fmt("BDD debug max paths value \"%s\" is not in the range [0 .. 1.7e308].", bddDebugMaxPaths);
            throw new InvalidOptionException(msg);
        }

        // Check BDD library initial node table size.
        if (bddInitNodeTableSize < 1) {
            String msg = fmt("BDD library initial node table size \"%s\" is not in the range [1 .. 2^31-1].",
                    bddInitNodeTableSize);
            throw new InvalidOptionException(msg);
        }

        // Check BDD library operation cache ratio.
        if (Double.isNaN(bddOpCacheRatio)) {
            throw new InvalidOptionException("BDD library operation cache ratio must not be NaN.");
        }
        if (bddOpCacheRatio < 0.01 || bddOpCacheRatio > 1000) {
            String msg = fmt("BDD library operation cache ratio \"%s\" is not in the range [0.01 .. 1000].",
                    bddOpCacheRatio);
            throw new InvalidOptionException(msg);
        }

        // Check BDD library operation cache size.
        if (bddOpCacheSize != null && bddOpCacheSize < 2) {
            String msg = fmt("BDD library operation cache size \"%s\" is not in the range [2 .. 2^31-1].",
                    bddOpCacheSize);
            throw new InvalidOptionException(msg);
        }

        // Check sliding window maximum window length.
        if (bddSlidingWindowMaxLen < 1 || bddSlidingWindowMaxLen > 12) {
            String msg = fmt("BDD sliding window size \"%s\" is not in the range [1 .. 12].", bddSlidingWindowMaxLen);
            throw new InvalidOptionException(msg);
        }
    }
}
