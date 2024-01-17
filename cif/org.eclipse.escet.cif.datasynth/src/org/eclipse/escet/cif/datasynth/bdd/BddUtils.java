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

package org.eclipse.escet.cif.datasynth.bdd;

import static org.eclipse.escet.cif.datasynth.bdd.BddToCif.bddToCifPred;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.datasynth.settings.CifBddSettings;
import org.eclipse.escet.cif.datasynth.settings.CifBddStatistics;
import org.eclipse.escet.cif.datasynth.spec.CifBddSpec;
import org.eclipse.escet.cif.datasynth.spec.CifBddVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FileSizes;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.java.output.DebugNormalOutput;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDDomain;
import com.github.javabdd.BDDFactory;
import com.github.javabdd.BDDFactory.CacheStats;
import com.github.javabdd.BDDFactory.GCStats;
import com.github.javabdd.BDDFactory.MaxMemoryStats;
import com.github.javabdd.BDDFactory.MaxUsedBddNodesStats;

/** BDD utility methods. */
public class BddUtils {
    /** Constructor for the {@link BddUtils} class. */
    private BddUtils() {
        // Static class.
    }

    /**
     * Returns the minimum number of bits need to represent the given decimal value in a binary representation.
     *
     * @param value The decimal value. Must be non-negative.
     * @return The minimum number of bits.
     */
    public static int getMinimumBits(int value) {
        int count = 0;
        while (value > 0) {
            count++;
            value = value >> 1;
        }
        return count;
    }

    /**
     * Returns the domain predicate of the given variable. That is, a predicate that is 'true' if and only if the
     * variable has a valid value.
     *
     * <p>
     * The result of this method includes only the values of the CIF domain of the variable, not the BDD domain of the
     * variable. For a variable 'x' of type 'int[1..2]', two BDD variables are created, which can represent values of
     * type 'int[0..3]'. Values '0' and '3' are not valid values of variable 'x'. This method returns 'x = 1 or x = 2'.
     * </p>
     *
     * @param variable The CIF/BDD variable.
     * @param newDomain Whether to return a predicate for the pre/old domain of the variable ({@code false}) or the
     *     post/new domain of the variable ({@code true}).
     * @param factory The BDD factory to use.
     * @return The possible values of the variable being assigned.
     */
    public static BDD getVarDomain(CifBddVariable variable, boolean newDomain, BDDFactory factory) {
        // Get minimum and maximum value of the domain.
        int min = variable.lower;
        int max = variable.upper;

        // Add possible values to the result.
        BDDDomain domain = newDomain ? variable.domainNew : variable.domain;
        BDD rslt = factory.zero();
        for (int i = min; i <= max; i++) {
            rslt = rslt.orWith(domain.ithVar(i));
        }
        return rslt;
    }

    /**
     * Converts a BDD to a textual representation that closely resembles CIF ASCII syntax.
     *
     * @param bdd The BDD.
     * @param cifBddSpec The CIF/BDD specification.
     * @return The textual representation of the BDD.
     */
    public static String bddToStr(BDD bdd, CifBddSpec cifBddSpec) {
        // If one of the specific maximum counts is exceeded, don't actually
        // convert the BDD to a CNF/DNF predicate, for performance reasons.
        if (cifBddSpec.settings.bddDebugMaxNodes != null || cifBddSpec.settings.bddDebugMaxPaths != null) {
            // Get node count and true path count.
            int nc = bdd.nodeCount();
            double tpc = bdd.pathCount();

            boolean skip = (cifBddSpec.settings.bddDebugMaxNodes != null && nc > cifBddSpec.settings.bddDebugMaxNodes)
                    || (cifBddSpec.settings.bddDebugMaxPaths != null && tpc > cifBddSpec.settings.bddDebugMaxPaths);
            if (skip) {
                return fmt("<bdd %,dn %,.0fp>", nc, tpc);
            }
        }

        // Convert BDD to CNF/DNF predicate.
        Expression pred = bddToCifPred(bdd, cifBddSpec);
        return CifTextUtils.exprToStr(pred);
    }

    /**
     * If requested, register BDD factory callbacks that print some statistics.
     *
     * @param factory The BDD factory for which to register the callbacks.
     * @param doGcStats Whether to output BDD GC statistics, if normal output is enabled.
     * @param doResizeStats Whether to output BDD resize statistics, if normal output is enabled.
     * @param doContinuousPerformanceStats Whether to output continuous BDD performance statistics.
     * @param normalOutput Callback for normal output.
     * @param continuousOpMisses The list into which to collect continuous operation misses samples.
     * @param continuousUsedBddNodes The list into which to collect continuous used BDD nodes statistics samples.
     */
    public static void registerBddCallbacks(BDDFactory factory, boolean doGcStats, boolean doResizeStats,
            boolean doContinuousPerformanceStats, DebugNormalOutput normalOutput, List<Long> continuousOpMisses,
            List<Integer> continuousUsedBddNodes)
    {
        // Register BDD garbage collection callback.
        if (doGcStats && normalOutput.isEnabled()) {
            factory.registerGcStatsCallback((stats, pre) -> bddGcStatsCallback(stats, pre, normalOutput));
        }

        // Register BDD internal node array resize callback.
        if (doResizeStats && normalOutput.isEnabled()) {
            factory.registerResizeStatsCallback(
                    (oldSize, newSize) -> bddResizeStatsCallback(oldSize, newSize, normalOutput));
        }

        // Register continuous BDD performance statistics callback.
        if (doContinuousPerformanceStats) {
            factory.registerContinuousStatsCallback((n, o) -> {
                continuousOpMisses.add(o);
                continuousUsedBddNodes.add(n);
            });
        }
    }

    /**
     * Callback invoked when the BDD library performs garbage collection on its internal data structures. Prints
     * statistics.
     *
     * @param stats The garbage collection statistics.
     * @param pre Whether the callback is invoked just before garbage collection ({@code true}) or just after it
     *     ({@code false}).
     * @param normalOutput Callback for normal output.
     */
    private static void bddGcStatsCallback(GCStats stats, boolean pre, DebugNormalOutput normalOutput) {
        StringBuilder txt = new StringBuilder();
        txt.append("BDD ");
        txt.append(pre ? "pre " : "post");
        txt.append(" garbage collection: #");
        txt.append(fmt("%,d", stats.num + 1 - (pre ? 0 : 1)));
        txt.append(", ");
        txt.append(fmt("%,13d", stats.freenodes));
        txt.append(" of ");
        txt.append(fmt("%,13d", stats.nodes));
        txt.append(" nodes free");
        if (!pre) {
            txt.append(", ");
            txt.append(fmt("%,13d", stats.time));
            txt.append(" ms, ");
            txt.append(fmt("%,13d", stats.sumtime));
            txt.append(" ms total");
        }
        normalOutput.line(txt.toString());
    }

    /**
     * Callback invoked when the BDD library resizes its internal node array. Prints statistics.
     *
     * @param oldSize The old size of the internal node array.
     * @param newSize The new size of the internal node array.
     * @param normalOutput Callback for normal output.
     */
    private static void bddResizeStatsCallback(int oldSize, int newSize, DebugNormalOutput normalOutput) {
        normalOutput.line("BDD node table resize: from %,13d nodes to %,13d nodes", oldSize, newSize);
    }

    /**
     * Prints the BDD factory cache statistics, maximum used BDD nodes statistics, and maximum memory usage statistics,
     * if enabled in the settings. Also writes the continuous BDD performance statistics to a file, if enabled in the
     * settings.
     *
     * @param factory The BDD factory.
     * @param settings The settings to use.
     * @param continuousOpMisses The list into which to collect continuous operation misses samples.
     * @param continuousUsedBddNodes The list into which to collect continuous used BDD nodes statistics samples.
     * @param continuousPerformanceStatisticsFilePath The absolute or relative path to the continuous performance
     *     statistics output file.
     * @param continuousPerformanceStatisticsFileAbsPath The absolute path to the continuous performance statistics
     *     output file.
     */
    public static void printStats(BDDFactory factory, CifBddSettings settings, List<Long> continuousOpMisses,
            List<Integer> continuousUsedBddNodes, String continuousPerformanceStatisticsFilePath,
            String continuousPerformanceStatisticsFileAbsPath)
    {
        // Check what statistics to print.
        boolean doContinuousPerformanceStats = settings.cifBddStatistics.contains(CifBddStatistics.BDD_PERF_CONT);
        boolean doCacheStats = settings.cifBddStatistics.contains(CifBddStatistics.BDD_PERF_CACHE);
        boolean doMaxBddNodesStats = settings.cifBddStatistics.contains(CifBddStatistics.BDD_PERF_MAX_NODES);
        boolean doMaxMemoryStats = settings.cifBddStatistics.contains(CifBddStatistics.MAX_MEMORY);

        // Print the statistics.
        if (doCacheStats) {
            BddUtils.printBddCacheStats(factory.getCacheStats(), settings.normalOutput);
        }
        if (doContinuousPerformanceStats) {
            settings.debugOutput.line("Writing continuous BDD performance statistics file \"%s\".",
                    continuousPerformanceStatisticsFilePath);
            BddUtils.printBddContinuousPerformanceStats(continuousOpMisses, continuousUsedBddNodes,
                    continuousPerformanceStatisticsFilePath, continuousPerformanceStatisticsFileAbsPath);
        }
        if (doMaxBddNodesStats) {
            BddUtils.printBddMaxUsedBddNodesStats(factory.getMaxUsedBddNodesStats(), settings.normalOutput);
        }
        if (doMaxMemoryStats) {
            BddUtils.printMaxMemoryStats(factory.getMaxMemoryStats(), settings.normalOutput);
        }
    }

    /**
     * Print the BDD factory cache statistics.
     *
     * @param stats The BDD factory cache statistics.
     * @param normalOutput Callback for normal output.
     */
    public static void printBddCacheStats(CacheStats stats, DebugNormalOutput normalOutput) {
        // Create grid.
        GridBox grid = new GridBox(7, 2, 0, 1);

        grid.set(0, 0, "Node creation requests:");
        grid.set(1, 0, "Node creation chain accesses:");
        grid.set(2, 0, "Node creation cache hits:");
        grid.set(3, 0, "Node creation cache misses:");
        grid.set(4, 0, "Operation count:");
        grid.set(5, 0, "Operation cache hits:");
        grid.set(6, 0, "Operation cache misses:");

        grid.set(0, 1, str(stats.uniqueAccess));
        grid.set(1, 1, str(stats.uniqueChain));
        grid.set(2, 1, str(stats.uniqueHit));
        grid.set(3, 1, str(stats.uniqueMiss));
        grid.set(4, 1, str(stats.opAccess));
        grid.set(5, 1, str(stats.opHit));
        grid.set(6, 1, str(stats.opMiss));

        // Print statistics.
        normalOutput.line("BDD cache statistics:");
        for (String line: grid.getLines()) {
            normalOutput.line("  " + line);
        }
    }

    /**
     * Print the BDD factory maximum used BDD nodes statistics.
     *
     * @param stats The BDD factory maximum used BDD nodes statistics.
     * @param normalOutput Callback for normal output.
     */
    public static void printBddMaxUsedBddNodesStats(MaxUsedBddNodesStats stats, DebugNormalOutput normalOutput) {
        normalOutput.line(fmt("Maximum used BDD nodes: %d.", stats.getMaxUsedBddNodes()));
    }

    /**
     * Print the maximum memory usage statistics.
     *
     * @param stats The maximum memory usage statistics.
     * @param normalOutput Callback for normal output.
     */
    public static void printMaxMemoryStats(MaxMemoryStats stats, DebugNormalOutput normalOutput) {
        long maxMemoryBytes = stats.getMaxMemoryBytes();
        normalOutput.line(fmt("Maximum used memory: %d bytes = %s.", maxMemoryBytes,
                FileSizes.formatFileSize(maxMemoryBytes, false)));
    }

    /**
     * Print the continuous BDD performance statistics to a file.
     *
     * @param operationsSamples The collected continuous operation misses samples.
     * @param nodesSamples The collected continuous used BDD nodes statistics samples.
     * @param filePath The absolute or relative path to the continuous performance statistics output file.
     * @param absFilePath The absolute path to the continuous performance statistics output file.
     */
    public static void printBddContinuousPerformanceStats(List<Long> operationsSamples, List<Integer> nodesSamples,
            String filePath, String absFilePath)
    {
        // Get number of data points.
        Assert.areEqual(operationsSamples.size(), nodesSamples.size());
        int numberOfDataPoints = operationsSamples.size();

        // Start the actual printing.
        try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(absFilePath));
             Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8))
        {
            writer.write("Operations,Used BBD nodes");
            writer.write(Strings.NL);
            long lastOperations = -1;
            int lastNodes = -1;
            for (int i = 0; i < numberOfDataPoints; i++) {
                // Only print new data points.
                long nextOperations = operationsSamples.get(i);
                int nextNodes = nodesSamples.get(i);
                if (nextOperations != lastOperations || nextNodes != lastNodes) {
                    lastOperations = nextOperations;
                    lastNodes = nextNodes;
                    writer.write(fmt("%d,%d", lastOperations, lastNodes));
                    writer.write(Strings.NL);
                }
            }
        } catch (IOException e) {
            throw new InputOutputException(
                    fmt("Failed to write continuous BDD performance statistics file \"%s\".", filePath), e);
        }
    }
}
