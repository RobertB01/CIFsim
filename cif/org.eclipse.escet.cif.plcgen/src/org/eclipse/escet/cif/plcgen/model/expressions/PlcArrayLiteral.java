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

import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;

/** Expression describing an array value. */
public class PlcArrayLiteral extends PlcExpression {
    /** Values of the array. */
    public final List<PlcExpression> values;

    /**
     * Constructor of the {@link PlcArrayLiteral} class.
     *
     * @param values Values of the array.
     */
    public PlcArrayLiteral(List<PlcExpression> values) {
        // TODO: Disallow empty arrays.
        // TODO: Check all types of the supplied values.
        super(new PlcArrayType(0, values.size() - 1, values.get(0).type));
        this.values = Collections.unmodifiableList(values);
    }
}
