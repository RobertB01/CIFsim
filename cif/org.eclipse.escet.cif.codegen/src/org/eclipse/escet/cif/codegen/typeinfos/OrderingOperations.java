//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.typeinfos;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;

/** Interface for implementing ordering operations. */
public interface OrderingOperations {
    /**
     * Convert a 'less-than' expression.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination.
     * @param ctxt Code context of the expression.
     * @return The generated expression in the target language.
     */
    public abstract ExprCode convertLessThan(BinaryExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert a 'less-equal' expression.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination.
     * @param ctxt Code context of the expression.
     * @return The generated expression in the target language.
     */
    public abstract ExprCode convertLessEqual(BinaryExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert a 'greater-equal' expression.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination.
     * @param ctxt Code context of the expression.
     * @return The generated expression in the target language.
     */
    public abstract ExprCode convertGreaterEqual(BinaryExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert a 'greater-than' expression.
     *
     * @param expr Expression to convert to the target language.
     * @param dest Storage destination.
     * @param ctxt Code context of the expression.
     * @return The generated expression in the target language.
     */
    public abstract ExprCode convertGreaterThan(BinaryExpression expr, Destination dest, CodeContext ctxt);
}
