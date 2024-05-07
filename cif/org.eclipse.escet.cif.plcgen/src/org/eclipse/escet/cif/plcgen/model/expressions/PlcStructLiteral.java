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

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.common.java.Assert;

/** Expression describing a structure value. */
public class PlcStructLiteral extends PlcExpression {
    /** Values of the structure. */
    public final List<PlcNamedValue> values;

    /**
     * Constructor of the {@link PlcStructLiteral} class.
     *
     * @param values Values of the structure.
     * @param type Type of the literal.
     */
    public PlcStructLiteral(List<PlcNamedValue> values, PlcStructType type) {
        super(type);
        this.values = Collections.unmodifiableList(values);

        Assert.areEqual(values.size(), type.fields.size());
        Assert.check(IntStream.range(0, values.size())
                .allMatch(i -> values.get(i).name.equals(type.fields.get(i).fieldName)));
    }
}
