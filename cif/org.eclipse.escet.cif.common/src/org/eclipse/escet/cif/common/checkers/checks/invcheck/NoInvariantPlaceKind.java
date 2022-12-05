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

/** Invariant disallowance values for the place kind aspect. */
public enum NoInvariantPlaceKind implements NoKindInterface<PlaceKind> {
    /** No invariants allowed both in locations and in component bodies. */
    ALL_PLACES(EnumSet.allOf(PlaceKind.class), ReportPriority.GLOBAL, null),

    /** No invariants allowed in locations. */
    LOCATIONS(EnumSet.of(PlaceKind.LOCATION), ReportPriority.ELEMENT, "in a location"),

    /** No invariants allowed in components. */
    COMPONENTS(EnumSet.of(PlaceKind.COMPONENT), ReportPriority.ELEMENT, "in a component");

    /** The number of values in the aspect enumeration {@link PlaceKind}. */
    public static final int NUMBER_OF_VALUES = PlaceKind.values().length;

    /** Values that are disallowed of the {@link PlaceKind} aspect. */
    private final EnumSet<PlaceKind> disallowedValues;

    /** Relevance of reporting on the {@link PlaceKind} disallowance aspect. */
    private final ReportPriority reportPriority;

    /** Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}. */
    private final String reportText;

    /**
     * Constructor of the {@link NoInvariantPlaceKind} enumeration class.
     *
     * @param disallowedValues Values that are disallowed of the {@link PlaceKind} aspect.
     * @param reportPriority Relevance of reporting on the {@link PlaceKind} disallowance aspect.
     * @param reportText Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}.
     */
    private NoInvariantPlaceKind(EnumSet<PlaceKind> disallowedValues, ReportPriority reportPriority, String reportText) {
        this.disallowedValues = disallowedValues;
        this.reportPriority = reportPriority;
        this.reportText = reportText;
    }

    @Override
    public boolean covers(PlaceKind value) {
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
