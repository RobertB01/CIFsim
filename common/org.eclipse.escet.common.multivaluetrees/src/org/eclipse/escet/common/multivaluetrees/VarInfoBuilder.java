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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.java.Assert;

/**
 * Class for constructing an {@link VarInfo} array for us in multi-value trees.
 *
 * @param <V> Variable class linked to {@link VarInfo} instances.
 */
public abstract class VarInfoBuilder<V> {
    /** Number of uses of a variable within the application. */
    public final int numSubIndices;

    /**
     * All the {@link VarInfo} instances ordered by variable.
     *
     * <p>
     * The array indices correspond with the kind of use (one use for one variable corresponds with one
     * {@link VarInfo}).
     * </p>
     */
    private final Map<V, VarInfo[]> varInfosByVariable = map();

    /**
     * All the variables, accessible by {@link VarInfo} instance.
     *
     * <p>
     * The kind of use of the variable can be found in {@link VarInfo#subIndex} of the key.
     * </p>
     */
    private final Map<VarInfo, V> variableByVarInfo = map();

    /**
     * All {@link VarInfo} instances, ordered by level in the tree.
     *
     * <p>
     * Smaller indices are near the root, highest indices are near the {@link Tree#ZERO} and {@link Tree#ONE} nodes.
     * </p>
     */
    public final List<VarInfo> varInfos = list();

    /**
     * Constructor of the {@link VarInfoBuilder} class.
     *
     * @param numSubIndices Number of uses for a variable.
     * @note Each use gets a non-negative number up to and excluding 'numSubIndices'. Multi-value code doesn't use or
     *     care about these numbers, for consistency between applications it's recommended to use {@code 0} for reading,
     *     and {@code 1} for writing a variable.
     */
    public VarInfoBuilder(int numSubIndices) {
        this.numSubIndices = numSubIndices;
        varInfos.add(null); // First entry is not used.
    }

    /**
     * Elementary method to construct a {@link VarInfo} from a variable and its use.
     *
     * <p>
     * It is allowed to skip creation of some uses for a variable if so desired. The corresponding {@link VarInfo}
     * instance will not be created, and querying it will yield {@code null}. You cannot insert nodes in the tree for
     * non-existing {@link VarInfo} instances.
     * </p>
     *
     * @param var Variable to associate with the new {@link VarInfo} instance.
     * @param subIndex Kind of use of the variable for the new {@link VarInfo} instance.
     * @return The created {@link VarInfo} instance.
     * @note While allowed, it is not required to store the returned instance. The builder has facilities to query them
     *     afterwards.
     * @throws AssertionError If the combination of variable and kind of use has already been created before in this
     *     builder.
     */
    public VarInfo addVariable(V var, int subIndex) {
        Assert.check(subIndex >= 0 && subIndex < numSubIndices);

        VarInfo[] vis = varInfosByVariable.get(var);
        if (vis == null) {
            vis = new VarInfo[numSubIndices];
            varInfosByVariable.put(var, vis);
        }
        Assert.check(vis[subIndex] == null, "Varinfo is already created.");

        String name = getName(var);
        int lower = getLowerBound(var);
        int length = getNumValues(var);
        VarInfo vi = new VarInfo(varInfos.size(), name, subIndex, lower, length);
        varInfos.add(vi);
        vis[subIndex] = vi;
        variableByVarInfo.put(vi, var);
        return vi;
    }

    /**
     * Add {@link VarInfo} instances for every use of the variable.
     *
     * @param var Variable to add.
     * @return The created {@link VarInfo} instances, index in the array corresponds with kind of use of the variable.
     * @note While allowed, it is not required to store the returned array. The builder has facilities to query them
     *     afterwards.
     */
    public VarInfo[] addVariable(V var) {
        for (int subIndex = 0; subIndex < numSubIndices; subIndex++) {
            addVariable(var, subIndex);
        }
        return varInfosByVariable.get(var);
    }

    /**
     * Create {@link VarInfo} instances for all uses of all given variables. Nodes for different uses of the same
     * variable will be near each other, with lower numbered use closest to the root.
     *
     * @param vars Variables to add.
     * @note Uses of the first given variable will be closer to the root, uses of the last variable will be closer to
     *     the {@link Tree#ZERO} and {@link Tree#ONE} nodes.
     */
    public void addVariablesGroupOnVariable(List<V> vars) {
        for (V var: vars) {
            addVariable(var);
        }
    }

    /**
     * Create {@link VarInfo} instances for all uses of all given variables. Nodes for the same use of the different
     * variables will be near each other, with the first variable closest to the root.
     *
     * @param vars Variables to add.
     * @note Uses of the first given variable will be closer to the root, uses of the last variable will be closer to
     *     the {@link Tree#ZERO} and {@link Tree#ONE} nodes.
     */
    public void addVariablesGroupOnSubIndex(List<V> vars) {
        for (int subIndex = 0; subIndex < numSubIndices; subIndex++) {
            for (V var: vars) {
                addVariable(var, subIndex);
            }
        }
    }

    /**
     * Get all {@link VarInfo} entries for a variable.
     *
     * @param var Variable to query.
     * @return The {@link VarInfo} array for all uses.
     * @note If not all {@link VarInfo} entries have been created, the returned array may contain {@code null} values.
     */
    public VarInfo[] getVarInfos(V var) {
        VarInfo[] varInfos = varInfosByVariable.get(var);
        Assert.notNull(varInfos);
        return varInfos;
    }

    /**
     * Get the {@link VarInfo} entry for a particular use of a variable.
     *
     * @param var Variable to query.
     * @param subIdx Index of the kind of use of the variable.
     * @return The {@link VarInfo} entry related to that use of the variable.
     * @throws AssertionError If the {@link VarInfo} entry does not exist.
     */
    public VarInfo getVarInfo(V var, int subIdx) {
        VarInfo[] varInfos = getVarInfos(var);
        Assert.notNull(varInfos[subIdx]);
        return varInfos[subIdx];
    }

    /**
     * Retrieve the variable related to a {@link VarInfo}.
     *
     * @param varInfo {@link VarInfo} instance to use.
     * @return Variable associated with the given {@link VarInfo} instance.
     * @note The same variable may be associated with several different {@link VarInfo} instances.
     */
    public V getVariable(VarInfo varInfo) {
        V var = variableByVarInfo.get(varInfo);
        Assert.notNull(var);
        return var;
    }

    /**
     * Get the name of the supplied variable.
     *
     * @param var Variable to use.
     * @return Name of the variable.
     */
    protected abstract String getName(V var);

    /**
     * Get the lowest allowed value of the variable.
     *
     * @param var Variable to use.
     * @return The lowest allowed value of the variable.
     */
    protected abstract int getLowerBound(V var);

    /**
     * Get the number of allowed values of the variable.
     *
     * @param var Variable to use.
     * @return Number of allowed values of the variable.
     */
    protected abstract int getNumValues(V var);
}
