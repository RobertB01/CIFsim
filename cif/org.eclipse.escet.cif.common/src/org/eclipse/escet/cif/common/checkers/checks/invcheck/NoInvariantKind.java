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

import org.eclipse.escet.cif.metamodel.cif.InvKind;

/** Invariant disallowance values for the invariant kind aspect. */
public enum NoInvariantKind implements NoKindInterface<InvKind> {
    /** No invariants allowed. */
    ALL_KINDS(EnumSet.allOf(InvKind.class), ReportPriority.GLOBAL, null, null),

    /** No state invariants allowed. */
    STATE(EnumSet.of(InvKind.STATE), ReportPriority.GROUP, "a", "state"),

    /** No state/event exclusion invariants allowed. */
    STATE_EVENT(EnumSet.of(InvKind.EVENT_DISABLES, InvKind.EVENT_NEEDS), ReportPriority.GROUP, "a",
            "state/event exclusion"),

    /** No state/event disables invariants allowed. */
    STATE_EVENT_DISABLES(EnumSet.of(InvKind.EVENT_DISABLES), ReportPriority.ELEMENT, "a",
            "'disables' state/event exclusion"),

    /** No state/event needs invariants allowed. */
    STATE_EVENT_NEEDS(EnumSet.of(InvKind.EVENT_NEEDS), ReportPriority.ELEMENT, "a", "'needs' state/event exclusion");

    /** The number of values in the aspect enumeration {@link InvKind}. */
    public static final int NUMBER_OF_VALUES = InvKind.values().length;

    /** Values that are disallowed of the {@link InvKind} aspect. */
    private final EnumSet<InvKind> disallowedValues;

    /** Relevance of reporting on the {@link InvKind} disallowance aspect. */
    private final ReportPriority reportPriority;

    /** Article to use in front of the {@link #reportText}, {@code null} if there is no report text. */
    private final String articleText;

    /** Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}. */
    private final String reportText;

    /**
     * Constructor of the {@link NoInvariantKind} enumeration class.
     *
     * @param disallowedValues Values that are disallowed of the {@link InvKind} aspect.
     * @param reportPriority Relevance of reporting on the {@link InvKind} disallowance aspect.
     * @param articleText Article to use in front of the report text, {@code null} if there is no report text.
     * @param reportText Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}.
     */
    private NoInvariantKind(EnumSet<InvKind> disallowedValues, ReportPriority reportPriority, String articleText,
            String reportText)
    {
        this.disallowedValues = disallowedValues;
        this.reportPriority = reportPriority;
        this.articleText = articleText;
        this.reportText = reportText;
    }

    @Override
    public boolean isDisallowed(InvKind value) {
        return disallowedValues.contains(value);
    }

    @Override
    public int getReportRelevance() {
        return reportPriority.getReportRelevance();
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
