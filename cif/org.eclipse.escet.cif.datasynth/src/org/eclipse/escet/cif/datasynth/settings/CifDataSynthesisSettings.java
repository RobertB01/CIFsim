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

import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** CIF data-based synthesis settings. */
public class CifDataSynthesisSettings {
    /**
     * Function that indicates whether termination has been requested. Once it returns {@code true}, it must return
     * {@code true} also on subsequent calls.
     */
    public final Supplier<Boolean> shouldTerminate;

    /** Callback for debug output. */
    public final DebugNormalOutput debugOutput;

    /** Callback for normal output. */
    public final DebugNormalOutput normalOutput;

    /** Callback for warning output. */
    public final WarnOutput warnOutput;

    /** The initial size of the node table of the BDD library. Value is in the range [1 .. 2^31-1]. */
    public final int bddInitNodeTableSize;

    /**
     * The ratio of the size of the operation cache of the BDD library to the size of the node table of the BDD library.
     * Value is in the range [0.01 .. 1000]. This setting has no effect if {@link #bddOpCacheSize} is non-{@code null}.
     */
    public final double bddOpCacheRatio;

    /**
     * The fixed size of the operation cache of the BDD library. Value is in the range [2 .. 2^31-1]. Use {@code null}
     * to disable a fixed cache size. If enabled, this setting takes priority over {@link #bddOpCacheRatio}.
     */
    public final Integer bddOpCacheSize;

    /**
     * The prefix to use for BDD related names in the output. It is a valid {@link CifValidationUtils#isValidIdentifier
     * CIF identifier}.
     */
    public final String bddOutputNamePrefix;

    /** The BDD output mode, indicating how to convert BDDs to CIF for the output of synthesis. */
    public final BddOutputMode bddOutputMode;

    /** The BDD predicate simplifications to perform. */
    public final EnumSet<BddSimplify> bddSimplifications;

    /** The initial BDD variable ordering and domain interleaving. */
    public final String bddVarOrderInit;

    /**
     * Whether to apply the sliding window variable ordering algorithm to improve the initial variable ordering
     * ({@code true}), or not apply it ({@code false}).
     */
    public final boolean bddSlidingWindowEnabled;

    /**
     * The maximum length of the window to use for the BDD sliding window variable ordering algorithm. Must be an
     * integer number in the range [1 .. 12].
     */
    public final int bddSlidingWindowMaxLen;

    /** The advanced BDD variable ordering and domain interleaving. */
    public final String bddVarOrderAdvanced;

    /** The absolute or relative path to the continuous performance statistics output file. */
    public final String continuousPerformanceStatisticsFilePath;

    /** The absolute path to the continuous performance statistics output file. */
    public final String continuousPerformanceStatisticsFileAbsPath;

    /** The granularity of edges to use during synthesis. */
    public final EdgeGranularity edgeGranularity;

    /** The edge ordering to use for backward reachability computations. */
    public final String edgeOrderBackward;

    /** The edge ordering to use for forward reachability computations. */
    public final String edgeOrderForward;

    /** Whether duplicate events are allowed for custom edge orders. */
    public final EdgeOrderDuplicateEventAllowance edgeOrderAllowDuplicateEvents;

    /**
     * Whether to use the edge workset algorithm to dynamically choose the best edge to apply during reachability
     * computations ({@code true}), or not ({@code false}).
     */
    public final boolean doUseEdgeWorksetAlgo;

    /**
     * Whether to warn for events that are never enabled in the input specification or always disabled by the
     * synthesized supervisor ({@code true}) or don't warn ({@code false}).
     */
    public final boolean doNeverEnabledEventsWarn;

    /** The order in which the fixed-point computations are to be performed during synthesis. */
    public final FixedPointComputationsOrder fixedPointComputationsOrder;

    /** Whether to perform forward reachability during synthesis ({@code true}) or omit it ({@code false}). */
    public final boolean doForwardReach;

    /** Whether to warn for plants that reference requirement state ({@code true}) or don't warn ({@code false}). */
    public final boolean doPlantsRefReqsWarn;

    /** The way that state requirement invariants are enforced. */
    public final StateReqInvEnforceMode stateReqInvEnforceMode;

    /**
     * The name of the resulting supervisor automaton. It is a valid {@link CifValidationUtils#isValidIdentifier CIF
     * identifier}.
     */
    public final String supervisorName;

    /**
     * The namespace of the resulting supervisor, or {@code null} to use the empty namespace. If not {@code null}, it is
     * a valid {@link CifValidationUtils#isValidName CIF name}.
     */
    public final String supervisorNamespace;

    /** The kinds of statistics to print. */
    public final EnumSet<SynthesisStatistics> synthesisStatistics;

    /**
     * Constructor for the {@link CifDataSynthesisSettings} class.
     *
     * @param shouldTerminate Function that indicates whether termination has been requested. Once it returns
     *     {@code true}, it must return {@code true} also on subsequent calls.
     * @param debugOutput Callback for debug output.
     * @param normalOutput Callback for normal output.
     * @param warnOutput Callback for warning output.
     * @param bddInitNodeTableSize The initial size of the node table of the BDD library. Value must be in the range [1
     *     .. 2^31-1].
     * @param bddOpCacheRatio The ratio of the size of the operation cache of the BDD library to the size of the node
     *     table of the BDD library. Value must be in the range [0.01 .. 1000]. This setting has no effect if
     *     {@code bddOpCacheSize} is non-{@code null}.
     * @param bddOpCacheSize The fixed size of the operation cache of the BDD library. Value must be in the range [2 ..
     *     2^31-1]. Use {@code null} to disable a fixed cache size. If enabled, this setting takes priority over
     *     {@code bddOpCacheRatio}.
     * @param bddOutputNamePrefix The prefix to use for BDD related names in the output. Must be a valid
     *     {@link CifValidationUtils#isValidIdentifier CIF identifier}.
     * @param bddOutputMode The BDD output mode, indicating how to convert BDDs to CIF for the output of synthesis.
     * @param bddSimplifications The BDD predicate simplifications to perform.
     * @param bddVarOrderInit The initial BDD variable ordering and domain interleaving.
     * @param bddSlidingWindowEnabled Whether to apply the sliding window variable ordering algorithm to improve the
     *     initial variable ordering ({@code true}), or not apply it ({@code false}).
     * @param bddSlidingWindowMaxLen The maximum length of the window to use for the BDD sliding window variable
     *     ordering algorithm. Must be an integer number in the range [1 .. 12].
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
            DebugNormalOutput normalOutput, WarnOutput warnOutput, int bddInitNodeTableSize, double bddOpCacheRatio,
            Integer bddOpCacheSize, String bddOutputNamePrefix, BddOutputMode bddOutputMode,
            EnumSet<BddSimplify> bddSimplifications, String bddVarOrderInit, boolean bddSlidingWindowEnabled,
            int bddSlidingWindowMaxLen, String bddVarOrderAdvanced, String continuousPerformanceStatisticsFilePath,
            String continuousPerformanceStatisticsFileAbsPath, EdgeGranularity edgeGranularity,
            String edgeOrderBackward, String edgeOrderForward,
            EdgeOrderDuplicateEventAllowance edgeOrderAllowDuplicateEvents, boolean doUseEdgeWorksetAlgo,
            boolean doNeverEnabledEventsWarn, FixedPointComputationsOrder fixedPointComputationsOrder,
            boolean doForwardReach, boolean doPlantsRefReqsWarn, StateReqInvEnforceMode stateReqInvEnforceMode,
            String supervisorName, String supervisorNamespace, EnumSet<SynthesisStatistics> synthesisStatistics)
    {
        // Store settings.
        this.shouldTerminate = shouldTerminate;
        this.debugOutput = debugOutput;
        this.normalOutput = normalOutput;
        this.warnOutput = warnOutput;
        this.bddInitNodeTableSize = bddInitNodeTableSize;
        this.bddOpCacheRatio = bddOpCacheRatio;
        this.bddOpCacheSize = bddOpCacheSize;
        this.bddOutputNamePrefix = bddOutputNamePrefix;
        this.bddOutputMode = bddOutputMode;
        this.bddSimplifications = bddSimplifications;
        this.bddVarOrderInit = bddVarOrderInit;
        this.bddSlidingWindowEnabled = bddSlidingWindowEnabled;
        this.bddSlidingWindowMaxLen = bddSlidingWindowMaxLen;
        this.bddVarOrderAdvanced = bddVarOrderAdvanced;
        this.continuousPerformanceStatisticsFilePath = continuousPerformanceStatisticsFilePath;
        this.continuousPerformanceStatisticsFileAbsPath = continuousPerformanceStatisticsFileAbsPath;
        this.edgeGranularity = edgeGranularity;
        this.edgeOrderBackward = edgeOrderBackward;
        this.edgeOrderForward = edgeOrderForward;
        this.edgeOrderAllowDuplicateEvents = edgeOrderAllowDuplicateEvents;
        this.doUseEdgeWorksetAlgo = doUseEdgeWorksetAlgo;
        this.doNeverEnabledEventsWarn = doNeverEnabledEventsWarn;
        this.fixedPointComputationsOrder = fixedPointComputationsOrder;
        this.doForwardReach = doForwardReach;
        this.doPlantsRefReqsWarn = doPlantsRefReqsWarn;
        this.stateReqInvEnforceMode = stateReqInvEnforceMode;
        this.supervisorName = supervisorName;
        this.supervisorNamespace = supervisorNamespace;
        this.synthesisStatistics = synthesisStatistics;

        // Check BDD library initial node table size.
        if (bddInitNodeTableSize < 1) {
            String msg = fmt("BDD library initial node table size \"%s\" is not in the range [1 .. 2^31-1].",
                    bddInitNodeTableSize);
            throw new InvalidOptionException(msg);
        }

        // Check BDD library operation cache ratio.
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

        // Check BDD output name prefix.
        if (!CifValidationUtils.isValidIdentifier(bddOutputNamePrefix)) {
            String msg = fmt("BDD output name prefix \"%s\" is not a valid CIF identifier.", bddOutputNamePrefix);
            throw new InvalidOptionException(msg);
        }

        // Check sliding window maximum window length.
        if (bddSlidingWindowMaxLen < 1 || bddSlidingWindowMaxLen > 12) {
            String msg = fmt("BDD sliding window size \"%s\" is not in the range [1 .. 12].", bddSlidingWindowMaxLen);
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
