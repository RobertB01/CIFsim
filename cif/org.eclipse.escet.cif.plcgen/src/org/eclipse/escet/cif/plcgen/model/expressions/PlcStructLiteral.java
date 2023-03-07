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

/** Expression describing a structure value. */
public class PlcStructLiteral extends PlcExpression {
    /** Values of the structure. */
    public final List<PlcNamedValue> values;

    /**
     * Constructor of the {@link PlcStructLiteral} class.
     *
     * @param values Values of the structure.
     */
    public PlcStructLiteral(List<PlcNamedValue> values) {
        this.values = values;
    }
}
