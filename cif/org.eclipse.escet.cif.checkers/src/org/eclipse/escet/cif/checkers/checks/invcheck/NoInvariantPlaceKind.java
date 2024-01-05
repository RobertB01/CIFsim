//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

import java.util.EnumSet;

/** Invariant disallowance values for the place kind aspect. */
public enum NoInvariantPlaceKind implements NoKindInterface<PlaceKind> {
    /** No invariants allowed both in locations and in component bodies. */
    ALL_PLACES(EnumSet.allOf(PlaceKind.class), null),

    /** No invariants allowed in locations. */
    LOCATIONS(EnumSet.of(PlaceKind.LOCATION), "in a location"),

    /** No invariants allowed in components. */
    COMPONENTS(EnumSet.of(PlaceKind.COMPONENT), "in a component");

    /** The number of values in the aspect enumeration {@link PlaceKind}. */
    public static final int NUMBER_OF_VALUES = PlaceKind.values().length;

    /** Values that are disallowed of the {@link PlaceKind} aspect. */
    private final EnumSet<PlaceKind> disallowedValues;

    /** Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}. */
    private final String reportText;

    /**
     * Constructor of the {@link NoInvariantPlaceKind} enumeration class.
     *
     * @param disallowedValues Values that are disallowed of the {@link PlaceKind} aspect.
     * @param reportText Text to report on this aspect if an invariant is found to be disallowed, may be {@code null}.
     */
    private NoInvariantPlaceKind(EnumSet<PlaceKind> disallowedValues, String reportText) {
        this.disallowedValues = disallowedValues;
        this.reportText = reportText;
    }

    @Override
    public boolean isDisallowed(PlaceKind value) {
        return disallowedValues.contains(value);
    }

    /**
     * Compare the disallowance set of this instance with the set of the given right side, and decide which of the sets
     * are larger.
     *
     * @param right Right side to compare against.
     * @return Which of the sets are larger.
     */
    public SubSetRelation compareSubset(NoInvariantPlaceKind right) {
        return SubSetRelation.compare(disallowedValues, right.disallowedValues);
    }

    @Override
    public String getReportText() {
        return reportText;
    }
}
