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

import org.apache.commons.math3.linear.RealMatrix;

/** DSM data after clustering. */
public class Dsm {
    /**
     * Mapping of new node indices from original node indices.
     *
     * <p>
     * Each entry {@code i} contains the original node index.
     * </p>
     */
    public int[] nodeShuffle;

    /** Adjacency matrix in the clustered node order. */
    public RealMatrix adjacencies;

    /** Labels of the nodes in the clustered node order. */
    public Label[] labels;

    /** Bus and cluster groups. */
    public Group rootGroup;

    /**
     * Constructor of the {@link Dsm} class.
     *
     * @param nodeShuffle Mapping of new node indices from original node indices.
     * @param adjacencies Adjacency matrix in the clustered node order.
     * @param labels Labels of the nodes in the clustered node order.
     * @param rootGroup Bus and cluster groups.
     */
    public Dsm(int[] nodeShuffle, RealMatrix adjacencies, Label[] labels, Group rootGroup) {
        this.nodeShuffle = nodeShuffle;
        this.adjacencies = adjacencies;
        this.labels = labels;
        this.rootGroup = rootGroup;
    }

    /**
     * Copy constructor of the {@link Dsm} class.
     *
     * @param dsm The DSM to shallow copy.
     */
    public Dsm(Dsm dsm) {
        this(dsm.nodeShuffle, dsm.adjacencies, dsm.labels, dsm.rootGroup);
    }
}
