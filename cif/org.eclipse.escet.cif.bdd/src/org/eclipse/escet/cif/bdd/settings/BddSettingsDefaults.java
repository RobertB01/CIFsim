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

/** Defaults for BDD-related {@link CifBddSettings settings}. */
public class BddSettingsDefaults {
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

    /** Constructor for the {@link BddSettingsDefaults} class. */
    private BddSettingsDefaults() {
        // Static class.
    }
}
