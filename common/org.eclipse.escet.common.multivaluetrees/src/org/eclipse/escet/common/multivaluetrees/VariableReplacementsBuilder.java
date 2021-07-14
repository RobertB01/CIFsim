//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.multivaluetrees;

import java.util.TreeSet;

import org.eclipse.escet.common.java.Assert;

/**
 * Class for constructing an array of {@link VariableReplacement}, for use in {@link Tree#adjacentReplacements}.
 *
 * @param <V> Variable type used by the {@link VarInfo} builder.
 */
public class VariableReplacementsBuilder<V> {
    /** Variable to {@link VarInfo} storage. */
    public VarInfoBuilder<V> varInfoBuilder;

    /** Ordered collection of added variable replacements. */
    private TreeSet<VariableReplacement> replacements;

    /** Remove all added replacements. */
    public void clear() {
        replacements.clear();
    }

    /**
     * Get the number of stored replacements.
     *
     * @return Number of stored replacements.
     */
    public int size() {
        return replacements.size();
    }

    /**
     * Add a variable replacement to move values from 'oldUse' to 'newUse' for the provided variable.
     *
     * @param var Variable to change in the replacement.
     * @param oldUse Usage of the variable being read and eliminated.
     * @param newUse Usage of the variable being changed.
     */
    public void addReplacement(V var, int oldUse, int newUse) {
        VarInfo[] varInfos = varInfoBuilder.getVarInfos(var);
        Assert.notNull(varInfos[oldUse]);
        Assert.notNull(varInfos[newUse]);
        addReplacement(varInfos[oldUse], varInfos[newUse]);
    }

    /**
     * Add a variable replacement to move values from 'oldVar' and 'oldUse' to 'newVar' and 'newUse' for the provided
     * variables.
     *
     * <p>
     * Note that due to adjacency requirements in {@link Tree#adjacentReplacements}, this cannot be used to perform
     * arbitrary variable replacements.
     * </p>
     *
     * @param oldVar Variable to read in the replacement.
     * @param oldUse Usage of the variable being read and eliminated.
     * @param newVar Variable to write in the replacement.
     * @param newUse Usage of the variable being changed.
     */
    public void addReplacement(V oldVar, int oldUse, V newVar, int newUse) {
        VarInfo oldInfo = varInfoBuilder.getVarInfo(oldVar, oldUse);
        VarInfo newInfo = varInfoBuilder.getVarInfo(newVar, newUse);
        addReplacement(oldInfo, newInfo);
    }

    /**
     * Add a variable replacement to move values from 'oldInfo' to 'newInfo'.
     *
     * <p>
     * Note that due to adjacency requirements in {@link Tree#adjacentReplacements}, this cannot be used to perform
     * arbitrary variable replacements.
     * </p>
     *
     * @param oldInfo Variable to read in the replacement.
     * @param newInfo Variable to write in the replacement.
     */
    public void addReplacement(VarInfo oldInfo, VarInfo newInfo) {
        addReplacement(new VariableReplacement(oldInfo, newInfo));
    }

    /**
     * Add the provided replacement to the collection.
     *
     * <p>
     * Note that due to adjacency requirements in {@link Tree#adjacentReplacements}, this cannot be used to perform
     * arbitrary variable replacements.
     * </p>
     *
     * @param varRepl Replacement to add.
     */
    public void addReplacement(VariableReplacement varRepl) {
        replacements.add(varRepl);
    }

    /**
     * Retrieve the added variable replacements, sorted for use in {@link Tree#adjacentReplacements}.
     *
     * @return The added replacements.
     */
    public VariableReplacement[] getReplacements() {
        VariableReplacement[] varRepls = new VariableReplacement[replacements.size()];
        varRepls = replacements.toArray(varRepls);
        clear();
        return varRepls;
    }

    /**
     * Constructor of the {@link VariableReplacementsBuilder} class.
     *
     * @param varInfoBuilder Storage of the relation between node levels and variables.
     */
    public VariableReplacementsBuilder(VarInfoBuilder<V> varInfoBuilder) {
        this.varInfoBuilder = varInfoBuilder;
        replacements = new TreeSet<>();
    }
}
