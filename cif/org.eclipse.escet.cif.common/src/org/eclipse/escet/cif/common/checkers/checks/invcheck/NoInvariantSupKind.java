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

import java.util.EnumSet;

import org.eclipse.escet.cif.metamodel.cif.SupKind;

/** Invariant disallowance values for the supervisory kind aspect. */
public enum NoInvariantSupKind implements NoKindInterface<SupKind> {
    /** No invariants allowed. */
    ALL_KINDS(EnumSet.allOf(SupKind.class), null, null),

    /** No invariants allowed in plants. */
    PLANT(EnumSet.of(SupKind.PLANT), "a", "plant"),

    /** No invariants allowed in requirements. */
    REQUIREMENT(EnumSet.of(SupKind.REQUIREMENT), "a", "requirement"),

    /** No invariants allowed in supervisors. */
    SUPERVISOR(EnumSet.of(SupKind.SUPERVISOR), "a", "supervisor"),

    /** No invariants allowed in components without a supervisory kind. */
    KINDLESS(EnumSet.of(SupKind.NONE), "a", "kindless");

    /** The number of values in the aspect enumeration {@link SupKind}. */
    public static final int NUMBER_OF_VALUES = SupKind.values().length;

    /** Values that are disallowed of the {@link SupKind} aspect. */
    private final EnumSet<SupKind> disallowedValues;

    /** Article to use in front of the {@link #reportText}, {@code null} if there is no report text. */
    private final String articleText;

    /** Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}. */
    private final String reportText;

    /**
     * Constructor of the {@link NoInvariantSupKind} enumeration class.
     *
     * @param disallowedValues Values that are disallowed of the {@link SupKind} aspect.
     * @param articleText Article to use in front of the report text, {@code null} if there is no report text.
     * @param reportText Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}.
     */
    private NoInvariantSupKind(EnumSet<SupKind> disallowedValues, String articleText, String reportText) {
        this.disallowedValues = disallowedValues;
        this.articleText = articleText;
        this.reportText = reportText;
    }

    @Override
    public boolean isDisallowed(SupKind value) {
        return disallowedValues.contains(value);
    }

    /**
     * Compare the disallowance set of this instance with the set of the given right side, and decide which of the sets
     * are larger.
     *
     * @param right Right side to compare against.
     * @return Which of the sets are larger.
     */
    public SubSetRelation compareSubset(NoInvariantSupKind right) {
        return SubSetRelation.compare(disallowedValues, right.disallowedValues);
    }

    @Override
    public String getArticleText() {
        return articleText;
    }

    @Override
    public String getReportText() {
        return reportText;
    }
}
