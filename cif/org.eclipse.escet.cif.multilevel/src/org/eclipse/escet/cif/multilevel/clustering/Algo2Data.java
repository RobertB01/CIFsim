//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.clustering;

import java.util.BitSet;

import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSetIterator;

/**
 * Wrapper class for the input and output of Algorithm 2.
 *
 * <p>
 * As the input data and the output data is the same, this class is used for both purposes. It also provides some
 * information collection functions from its data to reduce the clutter in the code of
 * {@link ComputeMultiLevelTree#update} the implements the algorithm.
 * </p>
 */
public class Algo2Data {
    /** Unclustered plant group relations, modified in-place. */
    public final RealMatrix p;

    /** Requirement group rows to plant group columns, modified in-place. */
    public final RealMatrix rp;

    /** Plant groups to include in the node, modified in-place. */
    public final BitSet plantGroups;

    /** Requirement groups to include in the node, modified in-place. */
    public final BitSet reqGroups;

    /**
     * Constructor of the {@link Algo2Data} class.
     *
     * @param p Unclustered plant group relations.
     * @param rp Requirement group rows to plant group columns.
     * @param plantGroups Plant groups to include in the node.
     * @param reqGroups Requirement groups to include in the node.
     */
    public Algo2Data(RealMatrix p, RealMatrix rp, BitSet plantGroups, BitSet reqGroups) {
        Assert.check(p.isSquare());

        this.p = p;
        this.rp = rp;
        this.plantGroups = plantGroups;
        this.reqGroups = reqGroups;
    }

    /**
     * Find all requirement groups that are related to both plant groups.
     *
     * @param plantGrp1 First plant group.
     * @param plantGrp2 Second plant group.
     * @return Requirements that relate to both plant groups.
     */
    public BitSet collectRequirementsForPlantGroupPair(int plantGrp1, int plantGrp2) {
        BitSet reqGroups = new BitSet();
        for (int row = 0; row < rp.getRowDimension(); row++) {
            if (rp.getEntry(row, plantGrp1) != 0 && rp.getEntry(row, plantGrp2) != 0) {
                reqGroups.set(row);
            }
        }
        return reqGroups;
    }

    /**
     * Collect the plant groups that relate to at least one of the given requirement groups.
     *
     * @param reqGroups Requirement groups to match with.
     * @return The plant groups that relate to at least one of the given requirement groups.
     */
    public BitSet collectPlantGroupsForRequirementGroups(BitSet reqGroups) {
        BitSet plantGroups = new BitSet();
        for (int col = 0; col < rp.getColumnDimension(); col++) {
            for (int reqGrp: new BitSetIterator(reqGroups)) {
                if (rp.getEntry(reqGrp, col) != 0) {
                    plantGroups.set(col);
                    break;
                }
            }
        }
        return plantGroups;
    }

    /**
     * Clear the given requirement group rows in the {@link #rp} matrix.
     *
     * @param reqGroups Requirement rows to clear.
     */
    public void clearPlantGroupsOfRequirementGroups(BitSet reqGroups) {
        for (int row: new BitSetIterator(reqGroups)) {
            for (int col = 0; col < rp.getColumnDimension(); col++) {
                rp.setEntry(row, col, 0);
            }
        }
    }
}
