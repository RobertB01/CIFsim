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
    ALL_KINDS(EnumSet.allOf(SupKind.class), ReportPriority.GLOBAL, null),

    /** No invariants allowed in plants. */
    PLANT(EnumSet.of(SupKind.PLANT), ReportPriority.ELEMENT, "plant"),

    /** No invariants allowed in requirements. */
    REQUIREMENT(EnumSet.of(SupKind.REQUIREMENT), ReportPriority.ELEMENT, "requirement"),

    /** No invariants allowed in supervisors. */
    SUPERVISOR(EnumSet.of(SupKind.SUPERVISOR), ReportPriority.ELEMENT, "supervisor"),

    /** No invariants allowed in components without supervisor kind. */
    KINDLESS(EnumSet.of(SupKind.NONE), ReportPriority.ELEMENT, "kindless");

    /** The number of values in the aspect enumeration {@link SupKind}. */
    public static final int NUMBER_OF_VALUES = SupKind.values().length;

    /** Values that are disallowed of the {@link SupKind} aspect. */
    private final EnumSet<SupKind> disallowedValues;

    /** Relevance of reporting on the {@link SupKind} disallowance aspect. */
    private final ReportPriority reportPriority;

    /** Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}. */
    private final String reportText;

    /**
     * Constructor of the {@link NoInvariantSupKind} enumeration class.
     *
     * @param disallowedValues Values that are disallowed of the {@link SupKind} aspect.
     * @param reportPriority Relevance of reporting on the {@link SupKind} disallowance aspect.
     * @param reportText Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}.
     */
    private NoInvariantSupKind(EnumSet<SupKind> disallowedValues, ReportPriority reportPriority, String reportText) {
        this.disallowedValues = disallowedValues;
        this.reportPriority = reportPriority;
        this.reportText = reportText;
    }

    @Override
    public boolean covers(SupKind value) {
        return disallowedValues.contains(value);
    }

    @Override
    public int getReportRelevance() {
        return reportPriority.getReportRelevance();
    }

    @Override
    public String getReportText() {
        return reportText;
    }
}
