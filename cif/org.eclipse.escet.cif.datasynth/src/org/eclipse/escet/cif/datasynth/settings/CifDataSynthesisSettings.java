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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;

import org.eclipse.escet.cif.bdd.settings.AllowNonDeterminism;
import org.eclipse.escet.cif.bdd.settings.CifBddSettings;
import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** CIF data-based synthesis settings. */
public class CifDataSynthesisSettings extends CifBddSettings {
    /**
     * Whether to warn for events that are never enabled in the input specification or always disabled by the
     * synthesized supervisor ({@code true}) or don't warn ({@code false}).
     */
    public boolean doNeverEnabledEventsWarn = CifDataSynthesisSettingsDefaults.DO_NEVER_ENABLED_EVENTS_WARN_DEFAULT;

    /** The way that state requirement invariants are enforced. */
    public StateReqInvEnforceMode stateReqInvEnforceMode = CifDataSynthesisSettingsDefaults.STATE_REQ_INV_ENFORCE_MODE_DEFAULT;

    /** The order in which the fixed-point computations are to be performed during synthesis. */
    public FixedPointComputationsOrder fixedPointComputationsOrder = CifDataSynthesisSettingsDefaults.FIXED_POINT_COMPUTATIONS_ORDER_DEFAULT;

    /** Whether to perform forward reachability during synthesis ({@code true}) or omit it ({@code false}). */
    public boolean doForwardReach = CifDataSynthesisSettingsDefaults.DO_FORWARD_REACH_DEFAULT;

    /**
     * The name of the resulting supervisor automaton. It is a valid {@link CifValidationUtils#isValidIdentifier CIF
     * identifier}.
     */
    public String supervisorName = CifDataSynthesisSettingsDefaults.SUPERVISOR_NAME_DEFAULT;

    /**
     * The namespace of the resulting supervisor, or {@code null} to use the empty namespace. If not {@code null}, it is
     * a valid {@link CifValidationUtils#isValidName CIF name}.
     */
    public String supervisorNamespace = CifDataSynthesisSettingsDefaults.SUPERVISOR_NAMESPACE_DEFAULT;

    /** The BDD output mode, indicating how to convert BDDs to CIF for the output of synthesis. */
    public BddOutputMode bddOutputMode = CifDataSynthesisSettingsDefaults.BDD_OUTPUT_MODE_DEFAULT;

    /**
     * The prefix to use for BDD related names in the output. It is a valid {@link CifValidationUtils#isValidIdentifier
     * CIF identifier}.
     */
    public String bddOutputNamePrefix = CifDataSynthesisSettingsDefaults.BDD_OUTPUT_NAME_PREFIX;

    /** The BDD predicate simplifications to perform. */
    public EnumSet<BddSimplify> bddSimplifications = CifDataSynthesisSettingsDefaults.BDD_SIMPLIFICATIONS_DEFAULT
            .clone();

    /** The kinds of statistics to print. */
    public EnumSet<SynthesisStatistics> synthesisStatistics = CifDataSynthesisSettingsDefaults.SYNTHESIS_STATISTICS_DEFAULT
            .clone();

    /** The absolute or relative path to the continuous performance statistics output file. */
    public String continuousPerformanceStatisticsFilePath = null;

    /** The absolute path to the continuous performance statistics output file. */
    public String continuousPerformanceStatisticsFileAbsPath = null;

    /**
     * Constructor for the {@link CifDataSynthesisSettings} class. Sets {@link CifDataSynthesisSettingsDefaults default}
     * values for the settings.
     */
    public CifDataSynthesisSettings() {
        // Do not allow non-determinism for controllable events. An external supervisor can't force the correct edge to
        // be taken, if only the updates (includes location pointer variable assignment for target location) are
        // different. For uncontrollable events non-determinism is not a problem, as the supervisor won't restrict edges
        // for uncontrollable events.
        this.allowNonDeterminism = AllowNonDeterminism.UNCONTROLLABLE;

        // Make sure the defaults are valid.
        checkSettings();
    }

    /** Check that the settings have valid values, for as much as it can be checked locally. */
    private void checkSettings() {
        // Check BDD output name prefix.
        if (!CifValidationUtils.isValidIdentifier(bddOutputNamePrefix)) {
            String msg = fmt("BDD output name prefix \"%s\" is not a valid CIF identifier.", bddOutputNamePrefix);
            throw new InvalidOptionException(msg);
        }

        // Check supervisor name.
        if (!CifValidationUtils.isValidIdentifier(supervisorName)) {
            String msg = fmt("Supervisor name \"%s\" is not a valid CIF identifier.", supervisorName);
            throw new InvalidOptionException(msg);
        }

        // Check supervisor namespace.
        if (supervisorNamespace != null && !CifValidationUtils.isValidName(supervisorNamespace)) {
            String msg = fmt("Supervisor namespace \"%s\" is invalid.", supervisorNamespace);
            throw new InvalidOptionException(msg);
        }
    }
}
