//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import java.util.EnumSet;

/** Defaults for BDD-related {@link CifBddSettings settings}. */
public class CifBddSettingsDefaults {
    /** The default value of the {@link CifBddSettings#doPlantsRefReqsWarn} setting. */
    public static final boolean DO_PLANTS_REF_REQS_WARN_DEFAULT = true;

    /** The default value of the {@link CifBddSettings#allowNonDeterminism} setting. */
    public static final AllowNonDeterminism ALLOW_NON_DETERMINISM_DEFAULT = AllowNonDeterminism.UNCONTROLLABLE;

    /** The default value of the {@link CifBddSettings#bddInitNodeTableSize} setting. */
    public static final int BDD_INIT_NODE_TABLE_SIZE_DEFAULT = 100_000;

    /** The default value of the {@link CifBddSettings#bddOpCacheRatio} setting. */
    public static final double BDD_OP_CACHE_RATIO_DEFAULT = 1.0;

    /** The default value of the {@link CifBddSettings#bddOpCacheSize} setting. */
    public static final Integer BDD_OP_CACHE_SIZE_DEFAULT = null;

    /** The default value of the {@link CifBddSettings#bddVarOrderInit} setting. */
    public static final String VAR_ORDER_INIT_DEFAULT = "sorted";

    /** The default value of the {@link CifBddSettings#bddDcshEnabled} setting. */
    public static final boolean DCSH_ENABLED_DEFAULT = true;

    /** The default value of the {@link CifBddSettings#bddForceEnabled} setting. */
    public static final boolean FORCE_ENABLED_DEFAULT = true;

    /** The default value of the {@link CifBddSettings#bddSlidingWindowEnabled} setting. */
    public static final boolean SLIDING_WINDOW_ENABLED_DEFAULT = true;

    /** The default value of the {@link CifBddSettings#bddSlidingWindowMaxLen} setting. */
    public static final int SLIDING_WINDOW_MAX_LEN_DEFAULT = 4;

    /** The default value of the {@link CifBddSettings#bddVarOrderAdvanced} setting. */
    public static final String VAR_ORDER_ADVANCED_DEFAULT = "basic";

    /** The default value of the {@link CifBddSettings#bddHyperEdgeAlgo} setting. */
    public static final BddHyperEdgeAlgo HYPER_EDGE_ALGO_DEFAULT = BddHyperEdgeAlgo.DEFAULT;

    /** The default value of the {@link CifBddSettings#bddDebugMaxNodes} setting. */
    public static final Integer BDD_DEBUG_MAX_NODES_DEFAULT = 10;

    /** The default value of the {@link CifBddSettings#bddDebugMaxPaths} setting. */
    public static final Double BDD_DEBUG_MAX_PATHS_DEFAULT = 10.0;

    /** The default value of the {@link CifBddSettings#edgeGranularity} setting. */
    public static final EdgeGranularity EDGE_GRANULARITY_DEFAULT = EdgeGranularity.PER_EVENT;

    /** The default value of the {@link CifBddSettings#edgeOrderBackward} setting. */
    public static final String EDGE_ORDER_BACKWARD_DEFAULT = "model";

    /** The default value of the {@link CifBddSettings#edgeOrderForward} setting. */
    public static final String EDGE_ORDER_FORWARD_DEFAULT = "model";

    /** The default value of the {@link CifBddSettings#edgeOrderAllowDuplicateEvents} setting. */
    public static final EdgeOrderDuplicateEventAllowance EDGE_ORDER_ALLOW_DUPLICATES_EVENTS_DEFAULT = EdgeOrderDuplicateEventAllowance.DISALLOWED;

    /** The default value of the {@link CifBddSettings#doUseEdgeWorksetAlgo} setting. */
    public static final boolean DO_USE_WORKSET_ALGO_DEFAULT = false;

    /**
     * The default value of the {@link CifBddSettings#cifBddStatistics} setting. Do not modify this set, but
     * {@link EnumSet#clone} it first.
     */
    public static final EnumSet<CifBddStatistics> CIF_BDD_STATISTICS_DEFAULT = EnumSet.noneOf(CifBddStatistics.class);

    /** Constructor for the {@link CifBddSettingsDefaults} class. */
    private CifBddSettingsDefaults() {
        // Static class.
    }
}
