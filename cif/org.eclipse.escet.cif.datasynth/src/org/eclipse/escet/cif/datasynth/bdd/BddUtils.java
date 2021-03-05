//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.app.framework.output.OutputProvider.doout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.lang.reflect.Method;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.datasynth.spec.SynthesisAutomaton;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDDomain;
import com.github.javabdd.BDDFactory;
import com.github.javabdd.BDDFactory.GCStats;
import com.github.javabdd.BDDFactory.ReorderStats;

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
     * @param variable The synthesis variable.
     * @param newDomain Whether to return a predicate for the pre/old domain of the variable ({@code false}) or the
     *     post/new domain of the variable ({@code true}).
     * @param factory The BDD factory to use.
     * @return The possible values of the variable being assigned.
     */
    public static BDD getVarDomain(SynthesisVariable variable, boolean newDomain, BDDFactory factory) {
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
     * @param aut The synthesis automaton.
     * @return The textual representation of the BDD.
     */
    public static String bddToStr(BDD bdd, SynthesisAutomaton aut) {
        // If one of the specific maximum counts is exceeded, don't actually
        // convert the BDD to a CNF/DNF predicate, for performance reasons.
        if (aut.debugMaxNodes != null || aut.debugMaxPaths != null) {
            // Get node count and true path count.
            int nc = bdd.nodeCount();
            double tpc = bdd.pathCount();

            boolean skip = (aut.debugMaxNodes != null && nc > aut.debugMaxNodes)
                    || (aut.debugMaxPaths != null && tpc > aut.debugMaxPaths);
            if (skip) {
                return fmt("<bdd %,dn %,.0fp>", nc, tpc);
            }
        }

        // Convert BDD to CNF/DNF predicate.
        Expression pred = bddToCifPred(bdd, aut);
        return CifTextUtils.exprToStr(pred);
    }

    /**
     * By default BDD factory callbacks print information to stdout/stderr. This method registers custom callbacks, that
     * by default don't do anything. This prevents output being printed to stdout/stderr. If requested, the callbacks
     * may print some statistics, using the application framework.
     *
     * @param factory The BDD factory for which to override the default callbacks.
     * @param doGcStats Whether to output BDD GC statistics.
     * @param doResizeStats Whether to output BDD resize statistics.
     */
    public static void setBddCallbacks(BDDFactory factory, boolean doGcStats, boolean doResizeStats) {
        Class<?> cls = BddUtils.class;
        Class<?>[] cbParams;
        Method callback;

        // Register BDD garbage collection callback.
        doGcStats = doGcStats && doout();
        String gcMethodName = doGcStats ? "bddGcStatsCallback" : "bddGcNullCallback";
        cbParams = new Class<?>[] {int.class, GCStats.class};
        try {
            callback = cls.getDeclaredMethod(gcMethodName, cbParams);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        factory.registerGCCallback(null, callback);

        // Register BDD variable reordering callback.
        cbParams = new Class<?>[] {boolean.class, ReorderStats.class};
        try {
            callback = cls.getDeclaredMethod("bddReOrderNullCallback", cbParams);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        factory.registerReorderCallback(null, callback);

        // Register BDD internal node array resize callback.
        doResizeStats = doResizeStats && doout();
        String resizeMethodName = doResizeStats ? "bddResizeStatsCallback" : "bddResizeNullCallback";
        cbParams = new Class<?>[] {int.class, int.class};
        try {
            callback = cls.getDeclaredMethod(resizeMethodName, cbParams);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
        factory.registerResizeCallback(null, callback);
    }

    /**
     * Callback invoked when the BDD library performs garbage collection on its internal data structures. Does not do
     * anything.
     *
     * @param pre Whether the callback is invoked just before garbage collection ({@code 1}) or just after it
     *     ({@code 0}).
     * @param stats The garbage collection statistics.
     */
    public static void bddGcNullCallback(int pre, GCStats stats) {
        // Callback that does nothing.
    }

    /**
     * Callback invoked when the BDD library performs garbage collection on its internal data structures. Prints
     * statistics.
     *
     * @param pre Whether the callback is invoked just before garbage collection ({@code 1}) or just after it
     *     ({@code 0}).
     * @param stats The garbage collection statistics.
     */
    public static void bddGcStatsCallback(int pre, GCStats stats) {
        StringBuilder txt = new StringBuilder();
        txt.append("BDD ");
        txt.append((pre == 1) ? "pre " : "post");
        txt.append(" garbage collection: #");
        txt.append(fmt("%,d", stats.num + 1 - ((pre == 1) ? 0 : 1)));
        txt.append(", ");
        txt.append(fmt("%,13d", stats.freenodes));
        txt.append(" of ");
        txt.append(fmt("%,13d", stats.nodes));
        txt.append(" nodes free");
        if (pre == 0) {
            txt.append(", ");
            txt.append(fmt("%,13d", stats.time));
            txt.append(" ms, ");
            txt.append(fmt("%,13d", stats.sumtime));
            txt.append(" ms total");
        }
        out(txt.toString());
    }

    /**
     * Callback invoked when the BDD library performs variable reordering.
     *
     * @param pre Whether the callback is invoked just before variable reordering ({@code true}) or just after it
     *     ({@code false}).
     * @param stats The variable reordering statistics.
     */
    public static void bddReOrderNullCallback(boolean pre, ReorderStats stats) {
        // Callback that does nothing.
    }

    /**
     * Callback invoked when the BDD library resizes its internal node array. Does not do anything.
     *
     * @param oldSize The old size of the internal node array.
     * @param newSize The new size of the internal node array.
     */
    public static void bddResizeNullCallback(int oldSize, int newSize) {
        // Callback that does nothing.
    }

    /**
     * Callback invoked when the BDD library resizes its internal node array. Prints statistics.
     *
     * @param oldSize The old size of the internal node array.
     * @param newSize The new size of the internal node array.
     */
    public static void bddResizeStatsCallback(int oldSize, int newSize) {
        out("BDD node table resize: from %,13d nodes to %,13d nodes", oldSize, newSize);
    }
}
