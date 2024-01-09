//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;

/** Check that verifies all ranged integer types are within the specified boundaries. */
public class IntegerTypeBoundsCheck extends CifCheck {
    /** Whether to check all integer types, including integer types without limits. */
    private final boolean checkRangeless;

    /** Smallest allowed value of the lower bound of an integer type. */
    private final int minLower;

    /** Largest allowed value of the lower bound of an integer type. */
    private final int maxLower;

    /** Smallest allowed value of the upper bound of an integer type. */
    private final int minUpper;

    /** Largest allowed value of the upper bound of an integer type. */
    private final int maxUpper;

    /**
     * Constructor of the {@link IntegerTypeBoundsCheck} class.
     *
     * @param checkRangeless Whether to check all integer types, including integer types without limits.
     * @param minLowerBound Smallest allowed value of the lower bound of an integer type.
     * @param maxLowerBound Largest allowed value of the lower bound of an integer type.
     * @param minUpperBound Smallest allowed value of the upper bound of an integer type.
     * @param maxUpperBound Largest allowed value of the upper bound of an integer type.
     */
    public IntegerTypeBoundsCheck(boolean checkRangeless, Integer minLowerBound, Integer maxLowerBound,
            Integer minUpperBound, Integer maxUpperBound)
    {
        this.checkRangeless = checkRangeless;
        minLower = (minLowerBound == null) ? Integer.MIN_VALUE : minLowerBound;
        maxLower = (maxLowerBound == null) ? Integer.MAX_VALUE : maxLowerBound;
        minUpper = (minUpperBound == null) ? Integer.MIN_VALUE : minUpperBound;
        maxUpper = (maxUpperBound == null) ? Integer.MAX_VALUE : maxUpperBound;
    }

    @Override
    protected void preprocessIntType(IntType intType, CifCheckViolations violations) {
        if (!checkRangeless && CifTypeUtils.isRangeless(intType)) {
            return;
        }

        int lower = CifTypeUtils.getLowerBound(intType);
        if (lower < minLower) {
            violations.add(intType, "Lowest allowed integer value is less than %d", minLower);
        }
        if (lower > maxLower) {
            violations.add(intType, "Lowest allowed integer value is more than %d", maxLower);
        }

        int upper = CifTypeUtils.getUpperBound(intType);
        if (upper < minUpper) {
            violations.add(intType, "Highest allowed integer value is less than %d", minUpper);
        }
        if (upper > maxUpper) {
            violations.add(intType, "Highest allowed integer value is more than %d", maxUpper);
        }
    }
}
