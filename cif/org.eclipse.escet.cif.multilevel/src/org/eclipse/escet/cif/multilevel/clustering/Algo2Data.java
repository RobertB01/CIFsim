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

import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.java.Assert;

/**
 * Wrapper class for the input and output of Algorithm 2.
 *
 * <p>
 * As the input data and the output data is the same, this class is used for both purposes. It also provides some
 * information collection functions from its data to reduce the clutter in the code of
 * {@link ComputeMultiLevelTree#update} that implements the algorithm.
 * </p>
 */
public class Algo2Data {
    /** Unclustered plant group relations, modified in-place. */
    public final RealMatrix p;

    /** Requirement group rows to plant group columns, modified in-place. */
    public final RealMatrix rp;

    /**
     * Constructor of the {@link Algo2Data} class.
     *
     * @param p Unclustered plant group relations.
     * @param rp Requirement group rows to plant group columns.
     */
    public Algo2Data(RealMatrix p, RealMatrix rp) {
        Assert.check(p.isSquare());

        this.p = p;
        this.rp = rp;
    }
}
