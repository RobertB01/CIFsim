//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks.invcheck;

/**
 * Interface for the disallowance enumeration on an aspect of an invariant.
 *
 * @param <E> Aspect enumeration type.
 */
public interface NoKindInterface<E extends Enum<E>> {
    /**
     * Return whether the aspect is disallowed for the given value.
     *
     * @param value Aspect value to test.
     * @return Whether the aspect is disallowed for the given value.
     */
    public boolean isDisallowed(E value);

    /**
     * Retrieve the text to produce for this aspect for reporting the disallowed invariant.
     *
     * @return Text to produce for this aspect for reporting disallowance, is {@code null} if there is nothing to
     *     report.
     */
    public String getReportText();
}
