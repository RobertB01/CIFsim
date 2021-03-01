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

package org.eclipse.escet.cif.common;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.exceptions.EndUserException;

/**
 * Thrown in case of expression evaluation failures.
 *
 * <p>
 * Note that this is not an {@link ApplicationException}, as the caller of the evaluation function should wrap the
 * exception to provide context about which expression failed to evaluate, in a larger context. As such, this is also a
 * checked exception.
 * </p>
 */
public class CifEvalException extends Exception implements EndUserException {
    /**
     * The expression that failed to evaluate. May be {@code null} if not available. Primarily used to retrieve position
     * information.
     */
    public final Expression expr;

    /**
     * Constructor for the {@link CifEvalException} class.
     *
     * @param message The message describing the evaluation failure.
     * @param expr The expression that failed to evaluate. May be {@code null} if not available. Primarily used to
     *     retrieve position information.
     */
    public CifEvalException(String message, Expression expr) {
        super(message);
        this.expr = expr;
    }
}
