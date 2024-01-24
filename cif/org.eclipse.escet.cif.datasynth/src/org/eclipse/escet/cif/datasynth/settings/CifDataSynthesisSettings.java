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

    /**
     * Get whether to warn for events that are never enabled in the input specification or always disabled by the
     * synthesized supervisor.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#DO_NEVER_ENABLED_EVENTS_WARN_DEFAULT} is the default value.
     * </p>
     *
     * @return {@code true} to warn, {@code false} to not warn.
     */
    public boolean getDoNeverEnabledEventsWarn() {
        return doNeverEnabledEventsWarn;
    }

    /**
     * Set whether to warn for events that are never enabled in the input specification or always disabled by the
     * synthesized supervisor.
     *
     * @param doNeverEnabledEventsWarn {@code true} to warn, {@code false} to not warn.
     */
    public void setDoNeverEnabledEventsWarn(boolean doNeverEnabledEventsWarn) {
        this.doNeverEnabledEventsWarn = doNeverEnabledEventsWarn;
        checkSettings();
    }

    /**
     * Get the way that state requirement invariants are enforced.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#STATE_REQ_INV_ENFORCE_MODE_DEFAULT} is the default value.
     * </p>
     *
     * @return The way that state requirement invariants are enforced.
     */
    public StateReqInvEnforceMode getStateReqInvEnforceMode() {
        return stateReqInvEnforceMode;
    }

    /**
     * Set the way that state requirement invariants are enforced.
     *
     * @param stateReqInvEnforceMode The way that state requirement invariants are enforced.
     */
    public void setStateReqInvEnforceMode(StateReqInvEnforceMode stateReqInvEnforceMode) {
        this.stateReqInvEnforceMode = stateReqInvEnforceMode;
        checkSettings();
    }

    /**
     * Get the order in which the fixed-point computations are to be performed during synthesis.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#FIXED_POINT_COMPUTATIONS_ORDER_DEFAULT} is the default value.
     * </p>
     *
     * @return The order in which the fixed-point computations are to be performed during synthesis.
     */
    public FixedPointComputationsOrder getFixedPointComputationsOrder() {
        return fixedPointComputationsOrder;
    }

    /**
     * Set the order in which the fixed-point computations are to be performed during synthesis.
     *
     * @param fixedPointComputationsOrder The order in which the fixed-point computations are to be performed during
     *     synthesis.
     */
    public void setFixedPointComputationsOrder(FixedPointComputationsOrder fixedPointComputationsOrder) {
        this.fixedPointComputationsOrder = fixedPointComputationsOrder;
        checkSettings();
    }

    /**
     * Get whether to perform forward reachability during synthesis.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#DO_FORWARD_REACH_DEFAULT} is the default value.
     * </p>
     *
     * @return {@code true} to perform forward reachability, {@code false} to not perform it.
     */
    public boolean getDoForwardReach() {
        return doForwardReach;
    }

    /**
     * Set whether to perform forward reachability during synthesis.
     *
     * @param doForwardReach {@code true} to perform forward reachability, {@code false} to not perform it.
     */
    public void setDoForwardReach(boolean doForwardReach) {
        this.doForwardReach = doForwardReach;
        checkSettings();
    }

    /**
     * Get the name of the resulting supervisor automaton. It is a valid {@link CifValidationUtils#isValidIdentifier CIF
     * identifier}.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#SUPERVISOR_NAME_DEFAULT} is the default value.
     * </p>
     *
     * @return The name of the resulting supervisor automaton.
     */
    public String getSupervisorName() {
        return supervisorName;
    }

    /**
     * Set the name of the resulting supervisor automaton. It must be a valid
     * {@link CifValidationUtils#isValidIdentifier CIF identifier}.
     *
     * @param supervisorName The name of the resulting supervisor automaton.
     */
    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
        checkSettings();
    }

    /**
     * Get the namespace of the resulting supervisor, or {@code null} to use the empty namespace. If not {@code null},
     * it is a valid {@link CifValidationUtils#isValidName CIF name}.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#SUPERVISOR_NAMESPACE_DEFAULT} is the default value.
     * </p>
     *
     * @return The namespace of the resulting supervisor, or {@code null}.
     */
    public String getSupervisorNamespace() {
        return supervisorNamespace;
    }

    /**
     * Set the namespace of the resulting supervisor, or {@code null} to use the empty namespace. If not {@code null},
     * it must be a valid {@link CifValidationUtils#isValidName CIF name}.
     *
     * @param supervisorNamespace The namespace of the resulting supervisor, or {@code null}.
     */
    public void setSupervisorNamespace(String supervisorNamespace) {
        this.supervisorNamespace = supervisorNamespace;
        checkSettings();
    }

    /**
     * Get the BDD output mode, indicating how to convert BDDs to CIF for the output of synthesis.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#BDD_OUTPUT_MODE_DEFAULT} is the default value.
     * </p>
     *
     * @return The BDD output mode.
     */
    public BddOutputMode getBddOutputMode() {
        return bddOutputMode;
    }

    /**
     * Set the BDD output mode, indicating how to convert BDDs to CIF for the output of synthesis.
     *
     * @param bddOutputMode The BDD output mode.
     */
    public void setBddOutputMode(BddOutputMode bddOutputMode) {
        this.bddOutputMode = bddOutputMode;
        checkSettings();
    }

    /**
     * Get the prefix to use for BDD related names in the output. It is a valid
     * {@link CifValidationUtils#isValidIdentifier CIF identifier}.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#BDD_OUTPUT_NAME_PREFIX} is the default value.
     * </p>
     *
     * @return The prefix to use for BDD related names in the output.
     */
    public String getBddOutputNamePrefix() {
        return bddOutputNamePrefix;
    }

    /**
     * Set the prefix to use for BDD related names in the output. It must be a valid
     * {@link CifValidationUtils#isValidIdentifier CIF identifier}.
     *
     * @param bddOutputNamePrefix The prefix to use for BDD related names in the output.
     */
    public void setBddOutputNamePrefix(String bddOutputNamePrefix) {
        this.bddOutputNamePrefix = bddOutputNamePrefix;
        checkSettings();
    }

    /**
     * Get the BDD predicate simplifications to perform.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#BDD_SIMPLIFICATIONS_DEFAULT} is the default value.
     * </p>
     *
     * @return The BDD predicate simplifications to perform.
     */
    public EnumSet<BddSimplify> getBddSimplifications() {
        return bddSimplifications;
    }

    /**
     * Set the BDD predicate simplifications to perform.
     *
     * @param bddSimplifications The BDD predicate simplifications to perform.
     */
    public void setBddSimplifications(EnumSet<BddSimplify> bddSimplifications) {
        this.bddSimplifications = bddSimplifications;
        checkSettings();
    }

    /**
     * Get the kinds of statistics to print.
     *
     * <p>
     * {@link CifDataSynthesisSettingsDefaults#SYNTHESIS_STATISTICS_DEFAULT} is the default value.
     * </p>
     *
     * @return The kinds of statistics to print.
     */
    public EnumSet<SynthesisStatistics> getSynthesisStatistics() {
        return synthesisStatistics;
    }

    /**
     * Set the kinds of statistics to print.
     *
     *
     * @param synthesisStatistics The kinds of statistics to print.
     */
    public void setSynthesisStatistics(EnumSet<SynthesisStatistics> synthesisStatistics) {
        this.synthesisStatistics = synthesisStatistics;
        checkSettings();
    }

    /**
     * Get the absolute or relative path to the continuous performance statistics output file.
     *
     * <p>
     * By default, no path is set ({@code null}). If continuous performance statistics are
     * {@link #getSynthesisStatistics enabled}, this path must be {@link #setContinuousPerformanceStatisticsFilePath
     * set}.
     * </p>
     *
     * @return The absolute or relative path to the continuous performance statistics output file.
     */
    public String getContinuousPerformanceStatisticsFilePath() {
        return continuousPerformanceStatisticsFilePath;
    }

    /**
     * Set the absolute or relative path to the continuous performance statistics output file.
     *
     * @param continuousPerformanceStatisticsFilePath The absolute or relative path to the continuous performance
     *     statistics output file.
     */
    public void setContinuousPerformanceStatisticsFilePath(String continuousPerformanceStatisticsFilePath) {
        this.continuousPerformanceStatisticsFilePath = continuousPerformanceStatisticsFilePath;
        checkSettings();
    }

    /**
     * Get the absolute path to the continuous performance statistics output file.
     *
     * <p>
     * By default, no path is set ({@code null}). If continuous performance statistics are
     * {@link #getSynthesisStatistics enabled}, this path must be {@link #setContinuousPerformanceStatisticsFileAbsPath
     * set}.
     * </p>
     *
     * @return The absolute or relative path to the continuous performance statistics output file.
     */
    public String getContinuousPerformanceStatisticsFileAbsPath() {
        return continuousPerformanceStatisticsFileAbsPath;
    }

    /**
     * Set the absolute path to the continuous performance statistics output file.
     *
     * @param continuousPerformanceStatisticsFileAbsPath The absolute path to the continuous performance statistics
     *     output file.
     */
    public void setContinuousPerformanceStatisticsFileAbsPath(String continuousPerformanceStatisticsFileAbsPath) {
        this.continuousPerformanceStatisticsFileAbsPath = continuousPerformanceStatisticsFileAbsPath;
        checkSettings();
    }

    @Override
    protected void checkSettings() {
        // Check CIF/BDD settings.
        super.checkSettings();

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
