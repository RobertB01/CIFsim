//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Expression containing some calculation code, and a value. */
public class CodeExpression extends SimpleExpression {
    /** Lines of code. */
    private final List<String> lines;

    /**
     * Constructor of the {@link CodeExpression} class.
     *
     * @param lines Lines of code to execute before the value becomes available.
     * @param value Value text describing the value to retrieve after execution.
     * @param expr Position information associated with this converted expression.
     */
    public CodeExpression(List<String> lines, String value, PositionObject expr) {
        super(value, expr);
        this.lines = lines;
    }

    @Override
    public List<String> getCode() {
        return lines;
    }
}
