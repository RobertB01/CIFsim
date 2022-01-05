//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.multivaluetrees;

/** Abstraction of an operation performed on two integer values. */
public interface BinaryOperation {
    /**
     * Perform a computation with both arguments and return the result.
     *
     * @param leftValue Left hand side of the operation.
     * @param rightValue Right hand side of the operation.
     * @return Result value of the operation, or {@code null} if it cannot be performed.
     */
    public Integer perform(Integer leftValue, Integer rightValue);
}
