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

package org.eclipse.escet.cif.datasynth.varorder;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.java.Assert;

/** Combination of multiple algorithms for automatic variable ordering. */
public class CombiVarOrderer extends AutoVarOrderer {
    /** The child algorithms to apply, in order. */
    public List<AutoVarOrderer> children = list();

    @Override
    protected void initializeAlgo() {
        // Nothing to do.
    }

    @Override
    protected void cleanupAlgo() {
        // Nothing to do.
    }

    @Override
    protected void computeOrder() {
        Assert.check(children.size() >= 2);

        for (AutoVarOrderer child: children) {
            // Initialize child, with best solution so far.
            child.dbgEnabled = this.dbgEnabled;
            child.varCnt = this.varCnt;
            child.curIndices = this.bestIndices;
            child.newIndices = this.bestIndices.clone();
            child.bestIndices = this.bestIndices.clone();
            child.edges = this.edges;
            child.initializeAlgo();

            // Compute order with child.
            child.computeOrder();

            // Copy child best order to self.
            this.bestIndices = child.bestIndices;

            // Cleanup child.
            child.cleanupAlgo();
            child.varCnt = -1;
            child.curIndices = null;
            child.newIndices = null;
            child.bestIndices = null;
            child.edges = null;
        }
    }
}
