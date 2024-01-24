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

package org.eclipse.escet.cif.datasynth.settings;

import java.util.EnumSet;

/** Defaults for CIF data-based synthesis-related {@link CifDataSynthesisSettings settings}. */
public class CifDataSynthesisSettingsDefaults {
    /** The default value of the {@link CifDataSynthesisSettings#doNeverEnabledEventsWarn} setting. */
    public static final boolean DO_NEVER_ENABLED_EVENTS_WARN_DEFAULT = true;

    /** The default value of the {@link CifDataSynthesisSettings#stateReqInvEnforceMode} setting. */
    public static final StateReqInvEnforceMode STATE_REQ_INV_ENFORCE_MODE_DEFAULT = StateReqInvEnforceMode.ALL_CTRL_BEH;

    /** The default value of the {@link CifDataSynthesisSettings#fixedPointComputationsOrder} setting. */
    public static final FixedPointComputationsOrder FIXED_POINT_COMPUTATIONS_ORDER_DEFAULT = FixedPointComputationsOrder.NONBLOCK_CTRL_REACH;

    /** The default value of the {@link CifDataSynthesisSettings#doForwardReach} setting. */
    public static final boolean DO_FORWARD_REACH_DEFAULT = false;

    /** The default value of the {@link CifDataSynthesisSettings#supervisorName} setting. */
    public static final String SUPERVISOR_NAME_DEFAULT = "sup";

    /** The default value of the {@link CifDataSynthesisSettings#supervisorNamespace} setting. */
    public static final String SUPERVISOR_NAMESPACE_DEFAULT = null;

    /** The default value of the {@link CifDataSynthesisSettings#bddOutputMode} setting. */
    public static final BddOutputMode BDD_OUTPUT_MODE_DEFAULT = BddOutputMode.NORMAL;

    /** The default value of the {@link CifDataSynthesisSettings#bddOutputNamePrefix} setting. */
    public static final String BDD_OUTPUT_NAME_PREFIX = "bdd";

    /**
     * The default value of the {@link CifDataSynthesisSettings#bddSimplifications} setting. Do not modify this set, but
     * {@link EnumSet#clone} it first.
     */
    public static final EnumSet<BddSimplify> BDD_SIMPLIFICATIONS_DEFAULT = EnumSet.allOf(BddSimplify.class);

    /**
     * The default value of the {@link CifDataSynthesisSettings#synthesisStatistics} setting. Do not modify this set,
     * but {@link EnumSet#clone} it first.
     */
    public static final EnumSet<SynthesisStatistics> SYNTHESIS_STATISTICS_DEFAULT = EnumSet
            .noneOf(SynthesisStatistics.class);

    /** Constructor for the {@link CifDataSynthesisSettingsDefaults} class. */
    private CifDataSynthesisSettingsDefaults() {
        // Static class.
    }
}
