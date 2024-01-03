//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm;

import static org.eclipse.escet.common.dsm.BusDetectionAlgorithm.NO_BUS;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.output.BlackHoleOutputProvider;
import org.eclipse.escet.common.java.output.DebugNormalOutput;

/**
 * Input data and configuration for the clustering algorithm.
 *
 * <p>
 * Only the adjacency matrix, the labels and the debug output stream need to be initially set during construction of the
 * instance. All other parameters can be freely changed, giving a lot of freedom in tuning them for the problem at hand.
 * </p>
 */
public class ClusterInput {
    /** Stream for sending debug output. */
    public final DebugNormalOutput debugOut;

    /** Adjacency matrix of the nodes, {@code (i, j)} is the non-negative weight of node {@code i} to node {@code j}. */
    public final RealMatrix adjacencies;

    /** Names of the nodes. */
    public final Label[] labels;

    /** Evaporation constant. Named μ in the algorithm, between 1.5 and 3.5, default 2.5. */
    public double evap;

    /**
     * Matrix exponentiation factor (number of steps taken each iteration). Named α in the algorithm, default 2, usually
     * doesn't need adjustment.
     */
    public int stepCount;

    /** Inflation coefficient. Named β in the algorithm, between 1.5 and 3.5, default 2.5. */
    public double inflation;

    /** Numeric convergence limit. Default value 1e-4. */
    public double epsilon;

    /** Bus detection algorithm. */
    public BusDetectionAlgorithm busDetectionAlgorithm;

    /**
     * Tuning factor for selecting bus nodes. The effect of the tuning factor depends on the chosen algorithm.
     *
     * <p>
     * Never directly change the value, but use the {@link #setBusInclusionFactor} method instead.
     * </p>
     */
    public double busInclusion;

    /**
     * Constructor of the {@link ClusterInput} class.
     *
     * <p>
     * Clustering is configured with the default parameter values.
     * </p>
     *
     * @param adjacencies Adjacency graph of the nodes, {@code (i, j)} is the non-negative weight of node {@code i} to
     *     node {@code j}.
     * @param labels Names of the nodes.
     * @param debugOut Stream for sending debug output. Use {@code null} to disable debug output.
     */
    public ClusterInput(RealMatrix adjacencies, Label[] labels, DebugNormalOutput debugOut) {
        this(adjacencies, labels, 2.5, 2, 2.5, 1e-4, NO_BUS, 2, debugOut);
    }

    /**
     * Constructor of the {@link ClusterInput} class.
     *
     * @param adjacencies Adjacency graph of the nodes, {@code (i, j)} is the non-negative weight of node {@code i} to
     *     node {@code j}.
     * @param labels Names of the nodes.
     * @param evap Evaporation constant.
     * @param stepCount Matrix exponentiation factor (number of steps taken each iteration).
     * @param inflation Inflation coefficient.
     * @param epsilon Convergence limit.
     * @param busDetectionAlgorithm The bus detection algorithm to apply.
     * @param busInclusion Tuning factor for the bus detection algorithm.
     * @param debugOut Stream for sending debug output. Use {@code null} to disable debug output.
     */
    public ClusterInput(RealMatrix adjacencies, Label[] labels, double evap, int stepCount, double inflation,
            double epsilon, BusDetectionAlgorithm busDetectionAlgorithm, double busInclusion,
            DebugNormalOutput debugOut)
    {
        this.adjacencies = adjacencies;
        this.labels = labels;
        this.evap = evap;
        this.stepCount = stepCount;
        this.inflation = inflation;
        this.epsilon = epsilon;
        this.busDetectionAlgorithm = busDetectionAlgorithm;
        setBusInclusionFactor(busInclusion);
        this.debugOut = (debugOut != null) ? debugOut : new BlackHoleOutputProvider().getDebugOutput();
    }

    /**
     * Set the bus inclusion factor to the supplied new value. Checks are performed whether the newly supplied value is
     * compatible with the chosen bus detection algorithm.
     *
     * <p>
     * The following checks are performed depending on the chosen bus detection algorithm:
     * <ul>
     * <li>{@link BusDetectionAlgorithm#NO_BUS}: no checks are performed.</li>
     * <li>{@link BusDetectionAlgorithm#FIX_POINT}: the new value should be between 1.0 and 4.0 (boundaries
     * included).</li>
     * <li>{@link BusDetectionAlgorithm#TOP_K}: the new value will first be truncated, as the algorithm only handles
     * integer values. The truncated value should be between 0 and the number of elements in the DSM (boundaries
     * included).</li>
     * </ul>
     * </p>
     *
     * @param newValue The new value for the bus inclusion factor.
     * @throws InvalidInputException When the new value does not satisfy the checks.
     * @throws UnsupportedException When an unknown bus detection algorithm is stored in this class.
     */
    public void setBusInclusionFactor(double newValue) {
        switch (busDetectionAlgorithm) {
            case NO_BUS:
                busInclusion = newValue;
                break;

            case FIX_POINT:
                if (newValue >= 1.0 && newValue <= 4.0) {
                    busInclusion = newValue;
                } else {
                    String msg = fmt("Bus factor values for the fixed-point algorithm are only allowed to be between "
                            + "1.0 and 4.0 (including the boundaries). The supplied value of %f is outside "
                            + "this interval.", newValue);
                    throw new InvalidInputException(msg);
                }
                break;

            case TOP_K:
                newValue = ((int)newValue);
                if (newValue >= 0 && newValue <= adjacencies.getRowDimension()) {
                    busInclusion = newValue;
                } else {
                    String msg = fmt(
                            "Bus factor values for the top-k algorithm are only allowed to be between "
                                    + "0 and the number of elements of the DSM (which is %d in this case). The "
                                    + "supplied value of %f is outside this interval.",
                            adjacencies.getRowDimension(), newValue);
                    throw new InvalidInputException(msg);
                }
                break;

            default:
                String msg = fmt("Unsupported bus detection algorithm encountered: %s.", busDetectionAlgorithm);
                throw new UnsupportedException(msg);
        }
    }
}
