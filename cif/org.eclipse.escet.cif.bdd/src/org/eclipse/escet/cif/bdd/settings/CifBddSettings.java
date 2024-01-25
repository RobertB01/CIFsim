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

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.output.BlackHoleOutputProvider;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** CIF/BDD-related settings. */
public class CifBddSettings {
    /** Whether modification of the settings is allowed. */
    protected boolean modificationAllowed = true;

    /**
     * Function that indicates whether termination has been requested. Once it returns {@code true}, it must return
     * {@code true} also on subsequent calls.
     */
    private Supplier<Boolean> shouldTerminate = () -> false;

    /** Callback for debug output. */
    private DebugNormalOutput debugOutput = new BlackHoleOutputProvider().getDebugOutput();

    /** Callback for normal output. */
    private DebugNormalOutput normalOutput = new BlackHoleOutputProvider().getNormalOutput();

    /** Callback for warning output. */
    private WarnOutput warnOutput = new BlackHoleOutputProvider().getWarnOutput();

    /** Whether to warn for plants that reference requirement state ({@code true}) or don't warn ({@code false}). */
    private boolean doPlantsRefReqsWarn = CifBddSettingsDefaults.DO_PLANTS_REF_REQS_WARN_DEFAULT;

    /** Events for which to allow non-determinism. */
    private AllowNonDeterminism allowNonDeterminism = CifBddSettingsDefaults.ALLOW_NON_DETERMINISM_DEFAULT;

    /**
     * The initial size of the node table of the BDD library. The value is in the positive range {@code [1 .. 2^31-1]}.
     */
    private int bddInitNodeTableSize = CifBddSettingsDefaults.BDD_INIT_NODE_TABLE_SIZE_DEFAULT;

    /**
     * The ratio of the size of the operation cache of the BDD library to the size of the node table of the BDD library.
     * The value is in the range {@code [0.01 .. 1000]}. This setting only has an effect if {@link #bddOpCacheSize} is
     * {@code null}.
     */
    private double bddOpCacheRatio = CifBddSettingsDefaults.BDD_OP_CACHE_RATIO_DEFAULT;

    /**
     * The fixed size of the operation cache of the BDD library. {@code null} means a fixed cache size is disabled. If
     * not {@code null}, the value is in the range {@code [2 .. 2^31-1]}. If enabled, this setting takes priority over
     * {@link #bddOpCacheRatio}.
     */
    private Integer bddOpCacheSize = CifBddSettingsDefaults.BDD_OP_CACHE_SIZE_DEFAULT;

    /** The initial BDD variable ordering and domain interleaving. */
    private String bddVarOrderInit = CifBddSettingsDefaults.VAR_ORDER_INIT_DEFAULT;

    /**
     * Whether to apply the DCSH variable ordering algorithm to improve the initial variable ordering ({@code true}), or
     * not apply it ({@code false}).
     */
    private boolean bddDcshEnabled = CifBddSettingsDefaults.DCSH_ENABLED_DEFAULT;

    /**
     * Whether to apply the FORCE variable ordering algorithm to improve the initial variable ordering ({@code true}),
     * or not apply it ({@code false}).
     */
    private boolean bddForceEnabled = CifBddSettingsDefaults.FORCE_ENABLED_DEFAULT;

    /**
     * Whether to apply the sliding window variable ordering algorithm to improve the initial variable ordering
     * ({@code true}), or not apply it ({@code false}).
     */
    private boolean bddSlidingWindowEnabled = CifBddSettingsDefaults.SLIDING_WINDOW_ENABLED_DEFAULT;

    /**
     * The maximum length of the window to use for the BDD sliding window variable ordering algorithm. The value is in
     * the range {@code [1 .. 12]}.
     */
    private int bddSlidingWindowMaxLen = CifBddSettingsDefaults.SLIDING_WINDOW_MAX_LEN_DEFAULT;

    /** The advanced BDD variable ordering and domain interleaving. */
    private String bddVarOrderAdvanced = CifBddSettingsDefaults.VAR_ORDER_ADVANCED_DEFAULT;

    /** The algorithm to use to create hyper-edges for BDD variable ordering. */
    private BddHyperEdgeAlgo bddHyperEdgeAlgo = CifBddSettingsDefaults.HYPER_EDGE_ALGO_DEFAULT;

    /**
     * The maximum number of BDD nodes for which to convert a BDD to a readable CNF/DNF representation for the debug
     * output. {@code null} indicates no maximum. If not {@code null}, the value is in the non-negative range
     * {@code [0 .. 2^31-1]}.
     */
    private Integer bddDebugMaxNodes = CifBddSettingsDefaults.BDD_DEBUG_MAX_NODES_DEFAULT;

    /**
     * The maximum number of BDD true paths for which to convert a BDD to a readable CNF/DNF representation for the
     * debug output. {@code null} indicates no maximum. If not {@code null}, the value is in the non-negative range
     * {@code [0 .. 1.7e308]}.
     */
    private Double bddDebugMaxPaths = CifBddSettingsDefaults.BDD_DEBUG_MAX_PATHS_DEFAULT;

    /** The granularity of edges to use in the BDD representation of the CIF specification. */
    private EdgeGranularity edgeGranularity = CifBddSettingsDefaults.EDGE_GRANULARITY_DEFAULT;

    /** The edge ordering to use for backward reachability computations. */
    private String edgeOrderBackward = CifBddSettingsDefaults.EDGE_ORDER_BACKWARD_DEFAULT;

    /** The edge ordering to use for forward reachability computations. */
    private String edgeOrderForward = CifBddSettingsDefaults.EDGE_ORDER_FORWARD_DEFAULT;

    /** Whether duplicate events are allowed for custom edge orders. */
    private EdgeOrderDuplicateEventAllowance edgeOrderAllowDuplicateEvents = CifBddSettingsDefaults.EDGE_ORDER_ALLOW_DUPLICATES_EVENTS_DEFAULT;

    /**
     * Whether to use the edge workset algorithm to dynamically choose the best edge to apply during reachability
     * computations ({@code true}), or not ({@code false}).
     */
    private boolean doUseEdgeWorksetAlgo = CifBddSettingsDefaults.DO_USE_WORKSET_ALGO_DEFAULT;

    /** The kinds of statistics to print. */
    private EnumSet<CifBddStatistics> cifBddStatistics = CifBddSettingsDefaults.CIF_BDD_STATISTICS_DEFAULT.clone();

    /**
     * Constructor for the {@link CifBddSettings} class. Sets {@link CifBddSettingsDefaults default} values for the
     * settings.
     */
    public CifBddSettings() {
        // Make sure the defaults are valid.
        checkSettings();
    }

    /**
     * Get whether modification of the settings is allowed.
     *
     * @return {@code true} if modification is allowed, {@code false} if it is not allowed.
     */
    public boolean getModificationAllowed() {
        return modificationAllowed;
    }

    /**
     * Set whether modification of the settings is allowed.
     *
     * @param modificationAllowed {@code true} if modification is allowed, {@code false} if it is not allowed.
     */
    public void setModificationAllowed(boolean modificationAllowed) {
        this.modificationAllowed = modificationAllowed;
    }

    /**
     * Get the function that indicates whether termination has been requested. Once it returns {@code true}, it returns
     * {@code true} also on subsequent calls.
     *
     * <p>
     * By default, the function always returns {@code false}.
     * </p>
     *
     * @return The function that indicates whether termination has been requested.
     */
    public Supplier<Boolean> getShouldTerminate() {
        return shouldTerminate;
    }

    /**
     * Set the function that indicates whether termination has been requested. Once it returns {@code true}, it must
     * returns {@code true} also on subsequent calls.
     *
     * @param shouldTerminate The function that indicates whether termination has been requested..
     */
    public void setShouldTerminate(Supplier<Boolean> shouldTerminate) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.shouldTerminate = shouldTerminate;
        checkSettings();
    }

    /**
     * Get the callback for debug output.
     *
     * <p>
     * By default, the callback ignores all output.
     * </p>
     *
     * @return The callback for debug output.
     */
    public DebugNormalOutput getDebugOutput() {
        return debugOutput;
    }

    /**
     * Set the callback for debug output.
     *
     * @param debugOutput The callback for debug output.
     */
    public void setDebugOutput(DebugNormalOutput debugOutput) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.debugOutput = debugOutput;
        checkSettings();
    }

    /**
     * Get the callback for normal output.
     *
     * <p>
     * By default, the callback ignores all output.
     * </p>
     *
     * @return The callback for normal output.
     */
    public DebugNormalOutput getNormalOutput() {
        return normalOutput;
    }

    /**
     * Set the callback for normal output.
     *
     * @param normalOutput The callback for normal output.
     */
    public void setNormalOutput(DebugNormalOutput normalOutput) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.normalOutput = normalOutput;
        checkSettings();
    }

    /**
     * Get the callback for warning output.
     *
     * <p>
     * By default, the callback ignores all output.
     * </p>
     *
     * @return The callback for warning output.
     */
    public WarnOutput getWarnOutput() {
        return warnOutput;
    }

    /**
     * Set the callback for warning output.
     *
     * @param warnOutput The callback for warning output.
     */
    public void setWarnOutput(WarnOutput warnOutput) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.warnOutput = warnOutput;
        checkSettings();
    }

    /**
     * Get whether to warn for plants that reference requirement state.
     *
     * <p>
     * {@link CifBddSettingsDefaults#DO_PLANTS_REF_REQS_WARN_DEFAULT} is the default value.
     * </p>
     *
     * @return {@code true} to warn, or {@code false} to not warn.
     */
    public boolean getDoPlantsRefReqsWarn() {
        return doPlantsRefReqsWarn;
    }

    /**
     * Set whether to warn for plants that reference requirement state.
     *
     * @param doPlantsRefReqsWarn {@code true} to warn, or {@code false} to not warn.
     */
    public void setDoPlantsRefReqsWarn(boolean doPlantsRefReqsWarn) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.doPlantsRefReqsWarn = doPlantsRefReqsWarn;
        checkSettings();
    }

    /**
     * Get events for which to allow non-determinism.
     *
     * <p>
     * {@link CifBddSettingsDefaults#ALLOW_NON_DETERMINISM_DEFAULT} is the default value.
     * </p>
     *
     * @return Events for which to allow non-determinism.
     */
    public AllowNonDeterminism getAllowNonDeterminism() {
        return allowNonDeterminism;
    }

    /**
     * Set events for which to allow non-determinism.
     *
     * @param allowNonDeterminism Events for which to allow non-determinism.
     */
    public void setAllowNonDeterminism(AllowNonDeterminism allowNonDeterminism) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.allowNonDeterminism = allowNonDeterminism;
        checkSettings();
    }

    /**
     * Get the initial size of the node table of the BDD library. The value is in the positive range
     * {@code [1 .. 2^31-1]}.
     *
     * <p>
     * {@link CifBddSettingsDefaults#BDD_INIT_NODE_TABLE_SIZE_DEFAULT} is the default value.
     * </p>
     *
     * @return The initial size of the node table of the BDD library.
     */
    public int getBddInitNodeTableSize() {
        return bddInitNodeTableSize;
    }

    /**
     * Set the initial size of the node table of the BDD library. The value must be in the positive range
     * {@code [1 .. 2^31-1]}.
     *
     * @param bddInitNodeTableSize The initial size of the node table of the BDD library.
     */
    public void setBddInitNodeTableSize(int bddInitNodeTableSize) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddInitNodeTableSize = bddInitNodeTableSize;
        checkSettings();
    }

    /**
     * Get the ratio of the size of the operation cache of the BDD library to the size of the node table of the BDD
     * library. The value is in the range {@code [0.01 .. 1000]}. This setting only has an effect if
     * {@link #getBddOpCacheSize} returns {@code null}.
     *
     * <p>
     * {@link CifBddSettingsDefaults#BDD_OP_CACHE_RATIO_DEFAULT} is the default value.
     * </p>
     *
     * @return The ratio of the size of the operation cache of the BDD library to the size of the node table of the BDD
     *     library.
     */
    public double getBddOpCacheRatio() {
        return bddOpCacheRatio;
    }

    /**
     * Set the ratio of the size of the operation cache of the BDD library to the size of the node table of the BDD
     * library. The value must be in the range {@code [0.01 .. 1000]}. This setting only has an effect if
     * {@link #getBddOpCacheSize} returns {@code null}.
     *
     * @param bddOpCacheRatio The ratio of the size of the operation cache of the BDD library to the size of the node
     *     table of the BDD library.
     */
    public void setBddOpCacheRatio(double bddOpCacheRatio) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddOpCacheRatio = bddOpCacheRatio;
        checkSettings();
    }

    /**
     * Get the fixed size of the operation cache of the BDD library. {@code null} means a fixed cache size is disabled.
     * If not {@code null}, the value is in the range {@code [2 .. 2^31-1]}. If enabled, this setting takes priority
     * over {@link #getBddOpCacheRatio}.
     *
     * <p>
     * {@link CifBddSettingsDefaults#BDD_OP_CACHE_SIZE_DEFAULT} is the default value.
     * </p>
     *
     * @return The fixed size of the operation cache of the BDD library, or {@code null}.
     */
    public Integer getBddOpCacheSize() {
        return bddOpCacheSize;
    }

    /**
     * Set the fixed size of the operation cache of the BDD library. {@code null} means a fixed cache size is disabled.
     * If not {@code null}, the value must be in the range {@code [2 .. 2^31-1]}. If enabled, this setting takes
     * priority over {@link #getBddOpCacheRatio}.
     *
     * @param bddOpCacheSize The fixed size of the operation cache of the BDD library, or {@code null}.
     */
    public void setBddOpCacheSize(Integer bddOpCacheSize) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddOpCacheSize = bddOpCacheSize;
        checkSettings();
    }

    /**
     * Get the initial BDD variable ordering and domain interleaving.
     *
     * <p>
     * {@link CifBddSettingsDefaults#VAR_ORDER_INIT_DEFAULT} is the default value.
     * </p>
     *
     * @return The initial BDD variable ordering and domain interleaving.
     */
    public String getBddVarOrderInit() {
        return bddVarOrderInit;
    }

    /**
     * Set the initial BDD variable ordering and domain interleaving.
     *
     * @param bddVarOrderInit The initial BDD variable ordering and domain interleaving.
     */
    public void setBddVarOrderInit(String bddVarOrderInit) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddVarOrderInit = bddVarOrderInit;
        checkSettings();
    }

    /**
     * Get whether to apply the DCSH variable ordering algorithm to improve the initial variable ordering.
     *
     * <p>
     * {@link CifBddSettingsDefaults#DCSH_ENABLED_DEFAULT} is the default value.
     * </p>
     *
     * @return {@code true} to apply DCSH, or {@code false} to not apply it.
     */
    public boolean getBddDcshEnabled() {
        return bddDcshEnabled;
    }

    /**
     * Set whether to apply the DCSH variable ordering algorithm to improve the initial variable ordering.
     *
     * @param bddDcshEnabled {@code true} to apply DCSH, or {@code false} to not apply it.
     */
    public void setBddDcshEnabled(boolean bddDcshEnabled) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddDcshEnabled = bddDcshEnabled;
        checkSettings();
    }

    /**
     * Get whether to apply the FORCE variable ordering algorithm to improve the initial variable ordering.
     *
     * <p>
     * {@link CifBddSettingsDefaults#FORCE_ENABLED_DEFAULT} is the default value.
     * </p>
     *
     * @return {@code true} to apply FORCE, or {@code false} to not apply it.
     */
    public boolean getBddForceEnabled() {
        return bddForceEnabled;
    }

    /**
     * Set whether to apply the FORCE variable ordering algorithm to improve the initial variable ordering.
     *
     * @param bddForceEnabled {@code true} to apply FORCE, or {@code false} to not apply it.
     */
    public void setBddForceEnabled(boolean bddForceEnabled) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddForceEnabled = bddForceEnabled;
        checkSettings();
    }

    /**
     * Get whether to apply the sliding window variable ordering algorithm to improve the initial variable ordering.
     *
     * <p>
     * {@link CifBddSettingsDefaults#SLIDING_WINDOW_ENABLED_DEFAULT} is the default value.
     * </p>
     *
     * @return {@code true} to apply sliding window, or {@code false} to not apply it.
     */
    public boolean getBddSlidingWindowEnabled() {
        return bddSlidingWindowEnabled;
    }

    /**
     * Set whether to apply the sliding window variable ordering algorithm to improve the initial variable ordering.
     *
     * @param bddSlidingWindowEnabled {@code true} to apply sliding window, or {@code false} to not apply it.
     */
    public void setBddSlidingWindowEnabled(boolean bddSlidingWindowEnabled) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddSlidingWindowEnabled = bddSlidingWindowEnabled;
        checkSettings();
    }

    /**
     * Get the maximum length of the window to use for the BDD sliding window variable ordering algorithm. The value is
     * in the range {@code [1 .. 12]}.
     *
     * <p>
     * {@link CifBddSettingsDefaults#SLIDING_WINDOW_MAX_LEN_DEFAULT} is the default value.
     * </p>
     *
     * @return The maximum length of the window to use for the BDD sliding window variable ordering algorithm.
     */
    public int getBddSlidingWindowMaxLen() {
        return bddSlidingWindowMaxLen;
    }

    /**
     * Set the maximum length of the window to use for the BDD sliding window variable ordering algorithm. The value
     * must be in the range {@code [1 .. 12]}.
     *
     * @param bddSlidingWindowMaxLen The maximum length of the window to use for the BDD sliding window variable
     *     ordering algorithm.
     */
    public void setBddSlidingWindowMaxLen(int bddSlidingWindowMaxLen) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddSlidingWindowMaxLen = bddSlidingWindowMaxLen;
        checkSettings();
    }

    /**
     * Get the advanced BDD variable ordering and domain interleaving.
     *
     * <p>
     * {@link CifBddSettingsDefaults#VAR_ORDER_ADVANCED_DEFAULT} is the default value.
     * </p>
     *
     * @return The advanced BDD variable ordering and domain interleaving.
     */
    public String getBddVarOrderAdvanced() {
        return bddVarOrderAdvanced;
    }

    /**
     * Set the advanced BDD variable ordering and domain interleaving.
     *
     * @param bddVarOrderAdvanced The advanced BDD variable ordering and domain interleaving.
     */
    public void setBddVarOrderAdvanced(String bddVarOrderAdvanced) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddVarOrderAdvanced = bddVarOrderAdvanced;
        checkSettings();
    }

    /**
     * Get the algorithm to use to create hyper-edges for BDD variable ordering.
     *
     * <p>
     * {@link CifBddSettingsDefaults#HYPER_EDGE_ALGO_DEFAULT} is the default value.
     * </p>
     *
     * @return The algorithm to use to create hyper-edges for BDD variable ordering.
     */
    public BddHyperEdgeAlgo getBddHyperEdgeAlgo() {
        return bddHyperEdgeAlgo;
    }

    /**
     * Set the algorithm to use to create hyper-edges for BDD variable ordering.
     *
     * @param bddHyperEdgeAlgo The algorithm to use to create hyper-edges for BDD variable ordering.
     */
    public void setBddHyperEdgeAlgo(BddHyperEdgeAlgo bddHyperEdgeAlgo) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddHyperEdgeAlgo = bddHyperEdgeAlgo;
        checkSettings();
    }

    /**
     * Get the maximum number of BDD nodes for which to convert a BDD to a readable CNF/DNF representation for the debug
     * output. {@code null} indicates no maximum. If not {@code null}, the value is in the non-negative range
     * {@code [0 .. 2^31-1]}.
     *
     * <p>
     * {@link CifBddSettingsDefaults#BDD_DEBUG_MAX_NODES_DEFAULT} is the default value.
     * </p>
     *
     * @return The maximum number of BDD nodes, or {@code null}.
     */
    public Integer getBddDebugMaxNodes() {
        return bddDebugMaxNodes;
    }

    /**
     * Set the maximum number of BDD nodes for which to convert a BDD to a readable CNF/DNF representation for the debug
     * output. {@code null} indicates no maximum. If not {@code null}, the value must be in the non-negative range
     * {@code [0 .. 2^31-1]}.
     *
     * @param bddDebugMaxNodes The maximum number of BDD nodes, or {@code null}.
     */
    public void setBddDebugMaxNodes(Integer bddDebugMaxNodes) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddDebugMaxNodes = bddDebugMaxNodes;
        checkSettings();
    }

    /**
     * Get the maximum number of BDD true paths for which to convert a BDD to a readable CNF/DNF representation for the
     * debug output. {@code null} indicates no maximum. If not {@code null}, the value is in the non-negative range
     * {@code [0 .. 1.7e308]}.
     *
     * <p>
     * {@link CifBddSettingsDefaults#BDD_DEBUG_MAX_PATHS_DEFAULT} is the default value.
     * </p>
     *
     * @return The maximum number of BDD true paths, or {@code null}.
     */
    public Double getBddDebugMaxPaths() {
        return bddDebugMaxPaths;
    }

    /**
     * Set the maximum number of BDD true paths for which to convert a BDD to a readable CNF/DNF representation for the
     * debug output. {@code null} indicates no maximum. If not {@code null}, the value must be in the non-negative range
     * {@code [0 .. 1.7e308]}.
     *
     * @param bddDebugMaxPaths The maximum number of BDD true paths, or {@code null}.
     */
    public void setBddDebugMaxPaths(Double bddDebugMaxPaths) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.bddDebugMaxPaths = bddDebugMaxPaths;
        checkSettings();
    }

    /**
     * Get the granularity of edges to use in the BDD representation of the CIF specification.
     *
     * <p>
     * {@link CifBddSettingsDefaults#EDGE_GRANULARITY_DEFAULT} is the default value.
     * </p>
     *
     * @return The granularity of edges.
     */
    public EdgeGranularity getEdgeGranularity() {
        return edgeGranularity;
    }

    /**
     * Set the granularity of edges to use in the BDD representation of the CIF specification.
     *
     * @param edgeGranularity The granularity of edges.
     */
    public void setEdgeGranularity(EdgeGranularity edgeGranularity) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.edgeGranularity = edgeGranularity;
        checkSettings();
    }

    /**
     * Get the edge ordering to use for backward reachability computations.
     *
     * <p>
     * {@link CifBddSettingsDefaults#EDGE_ORDER_BACKWARD_DEFAULT} is the default value.
     * </p>
     *
     * @return The edge ordering to use for backward reachability computations.
     */
    public String getEdgeOrderBackward() {
        return edgeOrderBackward;
    }

    /**
     * Set the edge ordering to use for backward reachability computations.
     *
     * @param edgeOrderBackward The edge ordering to use for backward reachability computations.
     */
    public void setEdgeOrderBackward(String edgeOrderBackward) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.edgeOrderBackward = edgeOrderBackward;
        checkSettings();
    }

    /**
     * Get the edge ordering to use for forward reachability computations.
     *
     * <p>
     * {@link CifBddSettingsDefaults#EDGE_ORDER_FORWARD_DEFAULT} is the default value.
     * </p>
     *
     * @return The edge ordering to use for forward reachability computations.
     */
    public String getEdgeOrderForward() {
        return edgeOrderForward;
    }

    /**
     * Set the edge ordering to use for forward reachability computations.
     *
     * @param edgeOrderForward The edge ordering to use for forward reachability computations.
     */
    public void setEdgeOrderForward(String edgeOrderForward) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.edgeOrderForward = edgeOrderForward;
        checkSettings();
    }

    /**
     * Get whether duplicate events are allowed for custom edge orders.
     *
     * <p>
     * {@link CifBddSettingsDefaults#EDGE_ORDER_ALLOW_DUPLICATES_EVENTS_DEFAULT} is the default value.
     * </p>
     *
     * @return Whether duplicate events are allowed for custom edge orders.
     */
    public EdgeOrderDuplicateEventAllowance getEdgeOrderAllowDuplicateEvents() {
        return edgeOrderAllowDuplicateEvents;
    }

    /**
     * Set whether duplicate events are allowed for custom edge orders.
     *
     * @param edgeOrderAllowDuplicateEvents Whether duplicate events are allowed for custom edge orders.
     */
    public void setEdgeOrderAllowDuplicateEvents(EdgeOrderDuplicateEventAllowance edgeOrderAllowDuplicateEvents) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.edgeOrderAllowDuplicateEvents = edgeOrderAllowDuplicateEvents;
        checkSettings();
    }

    /**
     * Get whether to use the edge workset algorithm to dynamically choose the best edge to apply during reachability
     * computations.
     *
     * <p>
     * {@link CifBddSettingsDefaults#DO_USE_WORKSET_ALGO_DEFAULT} is the default value.
     * </p>
     *
     * @return {@code true} to use the edge workset algorithm, {@code false} to not use it.
     */
    public boolean getDoUseEdgeWorksetAlgo() {
        return doUseEdgeWorksetAlgo;
    }

    /**
     * Set whether to use the edge workset algorithm to dynamically choose the best edge to apply during reachability
     * computations.
     *
     * @param doUseEdgeWorksetAlgo {@code true} to use the edge workset algorithm, {@code false} to not use it.
     */
    public void setDoUseEdgeWorksetAlgo(boolean doUseEdgeWorksetAlgo) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.doUseEdgeWorksetAlgo = doUseEdgeWorksetAlgo;
        checkSettings();
    }

    /**
     * Get the kinds of statistics to print.
     *
     * <p>
     * {@link CifBddSettingsDefaults#CIF_BDD_STATISTICS_DEFAULT} is the default value.
     * </p>
     *
     * @return The kinds of statistics to print.
     */
    public EnumSet<CifBddStatistics> getCifBddStatistics() {
        return cifBddStatistics;
    }

    /**
     * Set the kinds of statistics to print.
     *
     * @param cifBddStatistics The kinds of statistics to print.
     */
    public void setCifBddStatistics(EnumSet<CifBddStatistics> cifBddStatistics) {
        Assert.check(modificationAllowed, "Modification is not allowed.");
        this.cifBddStatistics = cifBddStatistics;
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
