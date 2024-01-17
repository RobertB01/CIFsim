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

import java.util.function.Supplier;

import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** CIF/BDD-related settings. */
public class CifBddSettings {
    /**
     * Function that indicates whether termination has been requested. Once it returns {@code true}, it must return
     * {@code true} also on subsequent calls.
     */
    public final Supplier<Boolean> shouldTerminate;

    /** Callback for debug output. */
    public final DebugNormalOutput debugOutput;

    /** Callback for warning output. */
    public final WarnOutput warnOutput;

    /**
     * Whether to apply the DCSH variable ordering algorithm to improve the initial variable ordering ({@code true}), or
     * not apply it ({@code false}).
     */
    public final boolean bddDcshEnabled;

    /**
     * The maximum number of BDD nodes for which to convert a BDD to a readable CNF/DNF representation for the debug
     * output. Value is in the range [0 .. 2^31-1]. {@code null} indicates no maximum.
     */
    public final Integer bddDebugMaxNodes;

    /**
     * The maximum number of BDD true paths for which to convert a BDD to a readable CNF/DNF representation for the
     * debug output. Value is in the range [0 .. 1.7e308]. {@code null} indicates no maximum.
     */
    public final Double bddDebugMaxPaths;

    /**
     * Whether to apply the FORCE variable ordering algorithm to improve the initial variable ordering ({@code true}),
     * or not apply it ({@code false}).
     */
    public final boolean bddForceEnabled;

    /** The algorithm to use to create hyper-edges for BDD variable ordering. */
    public final BddHyperEdgeAlgo bddHyperEdgeAlgo;

    /** The initial size of the node table of the BDD library. Value is in the range [1 .. 2^31-1]. */
    public final int bddInitNodeTableSize;

    /**
     * The ratio of the size of the operation cache of the BDD library to the size of the node table of the BDD library.
     * Value is in the range [0.01 .. 1000]. This setting has no effect if {@link #bddOpCacheSize} is non-{@code null}.
     */
    public final double bddOpCacheRatio;

    /**
     * The fixed size of the operation cache of the BDD library. Value is in the range [2 .. 2^31-1]. {@code null} means
     * a fixed cache size is disabled. If enabled, this setting takes priority over {@link #bddOpCacheRatio}.
     */
    public final Integer bddOpCacheSize;

    /** The initial BDD variable ordering and domain interleaving. */
    public final String bddVarOrderInit;

    /**
     * Whether to apply the sliding window variable ordering algorithm to improve the initial variable ordering
     * ({@code true}), or not apply it ({@code false}).
     */
    public final boolean bddSlidingWindowEnabled;

    /**
     * The maximum length of the window to use for the BDD sliding window variable ordering algorithm. Is an integer
     * number in the range [1 .. 12].
     */
    public final int bddSlidingWindowMaxLen;

    /** The advanced BDD variable ordering and domain interleaving. */
    public final String bddVarOrderAdvanced;

    /** The granularity of edges to use in the BDD representation of the CIF specification. */
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

    /** Whether to warn for plants that reference requirement state ({@code true}) or don't warn ({@code false}). */
    public final boolean doPlantsRefReqsWarn;

    /**
     * Constructor for the {@link CifBddSettings} class.
     *
     * @param shouldTerminate Function that indicates whether termination has been requested. Once it returns
     *     {@code true}, it must return {@code true} also on subsequent calls.
     * @param debugOutput Callback for debug output.
     * @param warnOutput Callback for warning output.
     * @param bddDcshEnabled Whether to apply the DCSH variable ordering algorithm to improve the initial variable
     *     ordering ({@code true}), or not apply it ({@code false}).
     * @param bddDebugMaxNodes The maximum number of BDD nodes for which to convert a BDD to a readable CNF/DNF
     *     representation for the debug output. Value must be in the range [0 .. 2^31-1]. Use {@code null} to not set a
     *     maximum.
     * @param bddDebugMaxPaths The maximum number of BDD true paths for which to convert a BDD to a readable CNF/DNF
     *     representation for the debug output. Value must be in the range [0 .. 1.7e308]. Use {@code null} to not set a
     *     maximum.
     * @param bddForceEnabled Whether to apply the FORCE variable ordering algorithm to improve the initial variable
     *     ordering ({@code true}), or not apply it ({@code false}).
     * @param bddHyperEdgeAlgo The algorithm to use to create hyper-edges for BDD variable ordering.
     * @param bddInitNodeTableSize The initial size of the node table of the BDD library. Value must be in the range [1
     *     .. 2^31-1].
     * @param bddOpCacheRatio The ratio of the size of the operation cache of the BDD library to the size of the node
     *     table of the BDD library. Value must be in the range [0.01 .. 1000]. This setting has no effect if
     *     {@code bddOpCacheSize} is non-{@code null}.
     * @param bddOpCacheSize The fixed size of the operation cache of the BDD library. Value must be in the range [2 ..
     *     2^31-1]. Use {@code null} to disable a fixed cache size. If enabled, this setting takes priority over
     *     {@code bddOpCacheRatio}.
     * @param bddVarOrderInit The initial BDD variable ordering and domain interleaving.
     * @param bddSlidingWindowEnabled Whether to apply the sliding window variable ordering algorithm to improve the
     *     initial variable ordering ({@code true}), or not apply it ({@code false}).
     * @param bddSlidingWindowMaxLen The maximum length of the window to use for the BDD sliding window variable
     *     ordering algorithm. Must be an integer number in the range [1 .. 12].
     * @param bddVarOrderAdvanced The advanced BDD variable ordering and domain interleaving.
     * @param edgeGranularity The granularity of edges to use in the BDD representation of the CIF specification.
     * @param edgeOrderBackward The edge ordering to use for backward reachability computations.
     * @param edgeOrderForward The edge ordering to use for forward reachability computations.
     * @param edgeOrderAllowDuplicateEvents Whether duplicate events are allowed for custom edge orders.
     * @param doUseEdgeWorksetAlgo Whether to use the edge workset algorithm to dynamically choose the best edge to
     *     apply during reachability computations ({@code true}), or not ({@code false}).
     * @param doPlantsRefReqsWarn Whether to warn for plants that reference requirement state ({@code true}) or don't
     *     warn ({@code false}).
     */
    public CifBddSettings(Supplier<Boolean> shouldTerminate, DebugNormalOutput debugOutput, WarnOutput warnOutput,
            boolean bddDcshEnabled, Integer bddDebugMaxNodes, Double bddDebugMaxPaths, boolean bddForceEnabled,
            BddHyperEdgeAlgo bddHyperEdgeAlgo, int bddInitNodeTableSize, double bddOpCacheRatio, Integer bddOpCacheSize,
            String bddVarOrderInit, boolean bddSlidingWindowEnabled, int bddSlidingWindowMaxLen,
            String bddVarOrderAdvanced, EdgeGranularity edgeGranularity, String edgeOrderBackward,
            String edgeOrderForward, EdgeOrderDuplicateEventAllowance edgeOrderAllowDuplicateEvents,
            boolean doUseEdgeWorksetAlgo, boolean doPlantsRefReqsWarn)
    {
        // Store settings.
        this.shouldTerminate = shouldTerminate;
        this.debugOutput = debugOutput;
        this.warnOutput = warnOutput;
        this.bddDcshEnabled = bddDcshEnabled;
        this.bddDebugMaxNodes = bddDebugMaxNodes;
        this.bddDebugMaxPaths = bddDebugMaxPaths;
        this.bddForceEnabled = bddForceEnabled;
        this.bddHyperEdgeAlgo = bddHyperEdgeAlgo;
        this.bddInitNodeTableSize = bddInitNodeTableSize;
        this.bddOpCacheRatio = bddOpCacheRatio;
        this.bddOpCacheSize = bddOpCacheSize;
        this.bddVarOrderInit = bddVarOrderInit;
        this.bddSlidingWindowEnabled = bddSlidingWindowEnabled;
        this.bddSlidingWindowMaxLen = bddSlidingWindowMaxLen;
        this.bddVarOrderAdvanced = bddVarOrderAdvanced;
        this.edgeGranularity = edgeGranularity;
        this.edgeOrderBackward = edgeOrderBackward;
        this.edgeOrderForward = edgeOrderForward;
        this.edgeOrderAllowDuplicateEvents = edgeOrderAllowDuplicateEvents;
        this.doUseEdgeWorksetAlgo = doUseEdgeWorksetAlgo;
        this.doPlantsRefReqsWarn = doPlantsRefReqsWarn;

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
