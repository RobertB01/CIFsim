//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import java.util.List;

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
        this.values = values;
    }
}