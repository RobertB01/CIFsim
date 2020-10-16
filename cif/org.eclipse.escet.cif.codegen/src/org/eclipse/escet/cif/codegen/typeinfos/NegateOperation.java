//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;

/** Interface for implementing negate operation. */
public interface NegateOperation {
    /**
     * Convert a 'negate' (a '-x') expression.
     *
     * @param expr Negate expression.
     * @param dest Storage destination.
     * @param ctxt Code context of the expression.
     * @return The generated expression in the target language.
     */
    public abstract ExprCode convertNegate(UnaryExpression expr, Destination dest, CodeContext ctxt);
}
