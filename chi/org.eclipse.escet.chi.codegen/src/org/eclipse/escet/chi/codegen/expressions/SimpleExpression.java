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

package org.eclipse.escet.chi.codegen.expressions;

import static java.util.Collections.emptyList;
import static org.eclipse.escet.common.java.Assert.check;

import java.util.List;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Expression without code (just a value). */
public class SimpleExpression extends ExpressionBase {
    /** Value of the expression. */
    private final String value;

    /** Has the value been read? */
    private boolean valueRead;

    /**
     * Constructor of the {@link SimpleExpression} class.
     *
     * @param value Text describing the value of the expression.
     * @param position Position information of the expression represented by this value.
     */
    public SimpleExpression(String value, PositionObject position) {
        super(position);
        this.value = value;
        valueRead = false;
    }

    @Override
    public List<String> getCode() {
        return emptyList();
    }

    @Override
    public String getValue(boolean reRead) {
        check(valueRead == reRead);
        valueRead = true;
        return value;
    }
}
