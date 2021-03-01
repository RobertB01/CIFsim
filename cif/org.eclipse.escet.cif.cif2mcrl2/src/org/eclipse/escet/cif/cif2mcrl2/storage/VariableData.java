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

package org.eclipse.escet.cif.cif2mcrl2.storage;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;

/** Storage class for a variable with its properties. */
public class VariableData {
    /** Variable represented by this class. */
    public final DiscVariable variable;

    /** Absolute name of the variable in CIF. */
    public final String name;

    /** Initial value of the variable. */
    public final String initialValue;

    /** Whether the variable should have a 'value' action to query its current value in mCRL2. */
    private boolean hasValueAction = false;

    /**
     * Constructor of the {@link VariableData} class.
     *
     * @param variable Variable to store.
     * @param initialValue Initial value to store.
     */
    public VariableData(DiscVariable variable, String initialValue) {
        this.variable = variable;
        name = CifTextUtils.getAbsName(variable, false);
        this.initialValue = initialValue;
    }

    /**
     * Get type of the variable.
     *
     * @return The type of the variable.
     */
    public CifType getType() {
        CifType tp = CifTypeUtils.normalizeType(variable.getType());
        return tp;
    }

    /**
     * Get lower limit of the variable type, if available.
     *
     * @return Lower limit of the type of the variable, if available.
     */
    public Integer getLowerLimit() {
        // TODO: Generalize CifTypeUtils.isRangeLess.
        CifType tp = getType();
        if (!(tp instanceof IntType)) {
            return null;
        }
        IntType it = (IntType)tp;
        if (CifTypeUtils.isRangeless(it)) {
            return null;
        }
        return CifTypeUtils.getLowerBound(it);
    }

    /**
     * Get upper limit of the variable type, if available.
     *
     * @return Upper limit of the type of the variable, if available.
     */
    public Integer getUpperLimit() {
        // TODO: Generalize CifTypeUtils.isRangeless.
        CifType tp = getType();
        if (!(tp instanceof IntType)) {
            return null;
        }
        IntType it = (IntType)tp;
        if (CifTypeUtils.isRangeless(it)) {
            return null;
        }
        return CifTypeUtils.getUpperBound(it);
    }

    /**
     * Set whether the variable has an 'value' action in mCRL2 to query its value.
     *
     * @param hasValueAction If set, the variable should get a 'value' action.
     * @see #getHasValueAction
     */
    public void setValueAction(boolean hasValueAction) {
        this.hasValueAction = hasValueAction;
    }

    /**
     * Retrieve whether the variable has an 'value' action in mCRL2 to query its value.
     *
     * @return {@code true} if a 'value' action should be generated, else {@code false}.
     * @see #setValueAction
     */
    public boolean getHasValueAction() {
        return hasValueAction;
    }
}
