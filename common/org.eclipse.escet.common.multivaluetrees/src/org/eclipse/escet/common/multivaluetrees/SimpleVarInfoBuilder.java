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

/** {@link VarInfo} builder class for variables without external connections. */
public class SimpleVarInfoBuilder extends VarInfoBuilder<SimpleVariable> {
    /**
     * Constructor of the {@link SimpleVarInfoBuilder} class.
     *
     * @param numUseKinds Number of use-kinds for a single variable.
     * @see VarInfoBuilder
     */
    public SimpleVarInfoBuilder(int numUseKinds) {
        super(numUseKinds);
    }

    @Override
    protected String getName(SimpleVariable var) {
        return var.name;
    }

    @Override
    protected int getLowerBound(SimpleVariable var) {
        return var.lowestValue;
    }

    @Override
    protected int getNumValues(SimpleVariable var) {
        return var.length;
    }
}
