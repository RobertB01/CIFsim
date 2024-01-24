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
import java.util.function.Supplier;

import org.eclipse.escet.cif.bdd.settings.AllowNonDeterminism;
import org.eclipse.escet.cif.bdd.settings.BddHyperEdgeAlgo;
import org.eclipse.escet.cif.bdd.settings.CifBddSettings;
import org.eclipse.escet.cif.bdd.settings.EdgeGranularity;
import org.eclipse.escet.cif.bdd.settings.EdgeOrderDuplicateEventAllowance;
import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

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
     * Constructor for the {@link CifDataSynthesisSettings} class.
     *
     * @param shouldTerminate Function that indicates whether termination has been requested. Once it returns
     *     {@code true}, it must return {@code true} also on subsequent calls.
     * @param debugOutput Callback for debug output.
     * @param normalOutput Callback for normal output.
     * @param warnOutput Callback for warning output.
     * @param bddDcshEnabled Whether to apply the DCSH variable ordering algorithm to improve the initial variable
     *     ordering ({@code true}), or not apply it ({@code false}).
     * @param bddDebugMaxNodes The maximum number of BDD nodes for which to convert a BDD to a readable CNF/DNF
     *     representation for the debug output. Use {@code null} to not set a maximum. If not {@code null}, the value
     *     must be in the non-negative range {@code [0 .. 2^31-1]}.
     * @param bddDebugMaxPaths The maximum number of BDD true paths for which to convert a BDD to a readable CNF/DNF
     *     representation for the debug output. Use {@code null} to not set a maximum. If not {@code null}, the value
     *     must be in the non-negative range {@code [0 .. 1.7e308]}.
     * @param bddForceEnabled Whether to apply the FORCE variable ordering algorithm to improve the initial variable
     *     ordering ({@code true}), or not apply it ({@code false}).
     * @param bddHyperEdgeAlgo The algorithm to use to create hyper-edges for BDD variable ordering.
     * @param bddInitNodeTableSize The initial size of the node table of the BDD library. The value must be in the
     *     positive range {@code [1 .. 2^31-1]}.
     * @param bddOpCacheRatio The ratio of the size of the operation cache of the BDD library to the size of the node
     *     table of the BDD library. The value must be in the range {@code [0.01 .. 1000]}. This setting only has an
     *     effect if {@code bddOpCacheSize} is {@code null}.
     * @param bddOpCacheSize The fixed size of the operation cache of the BDD library. The value must be in the range
     *     {@code [2 .. 2^31-1]}. Use {@code null} to disable a fixed cache size. If enabled, this setting takes
     *     priority over {@code bddOpCacheRatio}.
     * @param bddOutputNamePrefix The prefix to use for BDD related names in the output. Must be a valid
     *     {@link CifValidationUtils#isValidIdentifier CIF identifier}.
     * @param bddOutputMode The BDD output mode, indicating how to convert BDDs to CIF for the output of synthesis.
     * @param bddSimplifications The BDD predicate simplifications to perform.
     * @param bddVarOrderInit The initial BDD variable ordering and domain interleaving.
     * @param bddSlidingWindowEnabled Whether to apply the sliding window variable ordering algorithm to improve the
     *     initial variable ordering ({@code true}), or not apply it ({@code false}).
     * @param bddSlidingWindowMaxLen The maximum length of the window to use for the BDD sliding window variable
     *     ordering algorithm. The value must be in the range {@code [1 .. 12]}.
     * @param bddVarOrderAdvanced The advanced BDD variable ordering and domain interleaving.
     * @param continuousPerformanceStatisticsFilePath The absolute or relative path to the continuous performance
     *     statistics output file.
     * @param continuousPerformanceStatisticsFileAbsPath The absolute path to the continuous performance statistics
     *     output file.
     * @param edgeGranularity The granularity of edges to use during synthesis.
     * @param edgeOrderBackward The edge ordering to use for backward reachability computations.
     * @param edgeOrderForward The edge ordering to use for forward reachability computations.
     * @param edgeOrderAllowDuplicateEvents Whether duplicate events are allowed for custom edge orders.
     * @param doUseEdgeWorksetAlgo Whether to use the edge workset algorithm to dynamically choose the best edge to
     *     apply during reachability computations ({@code true}), or not ({@code false}).
     * @param doNeverEnabledEventsWarn Whether to warn for events that are never enabled in the input specification or
     *     always disabled by the synthesized supervisor ({@code true}) or don't warn ({@code false}).
     * @param fixedPointComputationsOrder The order in which the fixed-point computations are to be performed during
     *     synthesis.
     * @param doForwardReach Whether to perform forward reachability during synthesis ({@code true}) or omit it
     *     ({@code false}).
     * @param doPlantsRefReqsWarn Whether to warn for plants that reference requirement state ({@code true}) or don't
     *     warn ({@code false}).
     * @param stateReqInvEnforceMode The way that state requirement invariants are enforced.
     * @param supervisorName The name of the resulting supervisor automaton. Must be a valid
     *     {@link CifValidationUtils#isValidIdentifier CIF identifier}.
     * @param supervisorNamespace The namespace of the resulting supervisor, or {@code null} to use the empty namespace.
     *     If not {@code null}, it must be a valid {@link CifValidationUtils#isValidName CIF name}.
     * @param synthesisStatistics The kinds of statistics to print.
     */
    public CifDataSynthesisSettings(Supplier<Boolean> shouldTerminate, DebugNormalOutput debugOutput,
            DebugNormalOutput normalOutput, WarnOutput warnOutput, boolean bddDcshEnabled, Integer bddDebugMaxNodes,
            Double bddDebugMaxPaths, boolean bddForceEnabled, BddHyperEdgeAlgo bddHyperEdgeAlgo,
            int bddInitNodeTableSize, double bddOpCacheRatio, Integer bddOpCacheSize, String bddOutputNamePrefix,
            BddOutputMode bddOutputMode, EnumSet<BddSimplify> bddSimplifications, String bddVarOrderInit,
            boolean bddSlidingWindowEnabled, int bddSlidingWindowMaxLen, String bddVarOrderAdvanced,
            String continuousPerformanceStatisticsFilePath, String continuousPerformanceStatisticsFileAbsPath,
            EdgeGranularity edgeGranularity, String edgeOrderBackward, String edgeOrderForward,
            EdgeOrderDuplicateEventAllowance edgeOrderAllowDuplicateEvents, boolean doUseEdgeWorksetAlgo,
            boolean doNeverEnabledEventsWarn, FixedPointComputationsOrder fixedPointComputationsOrder,
            boolean doForwardReach, boolean doPlantsRefReqsWarn, StateReqInvEnforceMode stateReqInvEnforceMode,
            String supervisorName, String supervisorNamespace, EnumSet<SynthesisStatistics> synthesisStatistics)
    {
        // Pass on the CIF/BDD-related settings.
        //
        // Do not allow non-determinism for controllable events. An external supervisor can't force the correct edge to
        // be taken, if only the updates (includes location pointer variable assignment for target location) are
        // different. For uncontrollable events non-determinism is not a problem, as the supervisor won't restrict edges
        // for uncontrollable events.
        super(shouldTerminate, debugOutput, normalOutput, warnOutput, AllowNonDeterminism.UNCONTROLLABLE,
                bddDcshEnabled, bddDebugMaxNodes, bddDebugMaxPaths, bddForceEnabled, bddHyperEdgeAlgo,
                bddInitNodeTableSize, bddOpCacheRatio, bddOpCacheSize, bddVarOrderInit, bddSlidingWindowEnabled,
                bddSlidingWindowMaxLen, bddVarOrderAdvanced, edgeGranularity, edgeOrderBackward, edgeOrderForward,
                edgeOrderAllowDuplicateEvents, doUseEdgeWorksetAlgo, doPlantsRefReqsWarn,
                SynthesisStatistics.toCifBdd(synthesisStatistics));

        // Store settings.
        this.bddOutputNamePrefix = bddOutputNamePrefix;
        this.bddOutputMode = bddOutputMode;
        this.bddSimplifications = bddSimplifications;
        this.continuousPerformanceStatisticsFilePath = continuousPerformanceStatisticsFilePath;
        this.continuousPerformanceStatisticsFileAbsPath = continuousPerformanceStatisticsFileAbsPath;
        this.doNeverEnabledEventsWarn = doNeverEnabledEventsWarn;
        this.fixedPointComputationsOrder = fixedPointComputationsOrder;
        this.doForwardReach = doForwardReach;
        this.stateReqInvEnforceMode = stateReqInvEnforceMode;
        this.supervisorName = supervisorName;
        this.supervisorNamespace = supervisorNamespace;
        this.synthesisStatistics = synthesisStatistics;

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
