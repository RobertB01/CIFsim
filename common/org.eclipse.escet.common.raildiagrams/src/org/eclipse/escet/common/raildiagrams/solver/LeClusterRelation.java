//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.solver;

/** An {@link LeRelation} between equality clusters. */
public class LeClusterRelation {
    /** The relation between variables of both clusters (one variable from each). */
    public final LeRelation leRelation;

    /** The cluster with the smaller number (named {@code a}) in the relation. */
    public final EqualityCluster smallerCluster;

    /** The cluster with the bigger number (named {@code b}) in the relation. */
    public final EqualityCluster biggerCluster;

    /**
     * Constructor of the {@link LeClusterRelation} class.
     *
     * @param leRelation The relation between variables of both clusters (one variable from each).
     * @param smallerCluster The cluster with the smaller number (named {@code a}) in the relation.
     * @param biggerCluster The cluster with the bigger number (named {@code b}) in the relation.
     */
    public LeClusterRelation(LeRelation leRelation, EqualityCluster smallerCluster, EqualityCluster biggerCluster) {
        this.leRelation = leRelation;
        this.smallerCluster = smallerCluster;
        this.biggerCluster = biggerCluster;
    }
}
