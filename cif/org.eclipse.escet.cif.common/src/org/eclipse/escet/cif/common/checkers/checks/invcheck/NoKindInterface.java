//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks.invcheck;

/**
 * Interface for the disallowance enumeration on an aspect of an invariant.
 *
 * @param <A> Aspect enumeration type.
 */
public interface NoKindInterface<A extends Enum<?>> {
    /**
     * Get number of values in aspect enumeration A.
     *
     * @return The number of values in the aspect enumeration.
     */
    public int numberOfValues();

    /**
     * Return whether the given aspect value is covered by the disallowance value.
     *
     * @param value Aspect value to test.
     * @return Whether the aspect is in fact disallowed on the given aspect.
     */
    public boolean covers(A value);

    /**
     * Retrieve how relevant reporting disallowance on this aspect is.
     *
     * @return Relevance of reporeting disallowance, higher value is more relevant.
     */
    public int getReportRelevance();

    /**
     * Retrieve the text to produce for this aspect for reporting the disallowed invariant.
     *
     * @return Text to produce for this aspect for reporting disallowance, is {@code null} if there is nothing to
     *     report.
     */
    public String getReportText();
}
