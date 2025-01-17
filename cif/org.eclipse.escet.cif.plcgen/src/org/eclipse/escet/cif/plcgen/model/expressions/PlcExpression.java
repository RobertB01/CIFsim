//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.expressions;

import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.common.java.Assert;

/** Base class for expressions. */
public abstract class PlcExpression {
    /** Type of the expression. */
    public final PlcType type;

    /**
     * Constructor of the {@link PlcExpression} class.
     *
     * @param type Type of the expression.
     */
    public PlcExpression(PlcType type) {
        this.type = type;

        Assert.notNull(type);
    }

    @Override
    public int hashCode() {
        throw new AssertionError("Hashing is not generally supported.");
    }

    @Override
    public boolean equals(Object obj) {
        throw new AssertionError("Equality is not generally supported.");
    }

    @Override
    public abstract String toString();
}
