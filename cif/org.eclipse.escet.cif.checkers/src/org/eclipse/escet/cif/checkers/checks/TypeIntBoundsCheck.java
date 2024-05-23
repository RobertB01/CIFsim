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
import org.eclipse.escet.cif.common.CifAnnotationUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;

/** Check that verifies all ranged integer types are within the specified boundaries. */
public class TypeIntBoundsCheck extends CifCheck {
    /** Whether to check all integer types, including integer types without limits. */
    private final boolean checkRangeless;

    /** Smallest allowed value of the lower bound of an integer type, {@code null} means {@link Integer#MIN_VALUE}. */
    private final int minLower;

    /** Largest allowed value of the lower bound of an integer type, {@code null} means {@link Integer#MAX_VALUE}. */
    private final int maxLower;

    /** Smallest allowed value of the upper bound of an integer type, {@code null} means {@link Integer#MIN_VALUE}. */
    private final int minUpper;

    /** Largest allowed value of the upper bound of an integer type {@code null} means {@link Integer#MAX_VALUE}. */
    private final int maxUpper;

    /** Whether to disable checking of integer types in annotations. */
    private boolean ignoreAnnotations;

    /**
     * Constructor of the {@link TypeIntBoundsCheck} class.
     *
     * @param checkRangeless Whether to check all integer types, including integer types without limits.
     * @param minLowerBound Smallest allowed value of the lower bound of an integer type, {@code null} means
     *     {@link Integer#MIN_VALUE}.
     * @param maxLowerBound Largest allowed value of the lower bound of an integer type, {@code null} means
     *     {@link Integer#MAX_VALUE}.
     * @param minUpperBound Smallest allowed value of the upper bound of an integer type, {@code null} means
     *     {@link Integer#MIN_VALUE}.
     * @param maxUpperBound Largest allowed value of the upper bound of an integer type {@code null} means
     *     {@link Integer#MAX_VALUE}.
     */
    public TypeIntBoundsCheck(boolean checkRangeless, Integer minLowerBound, Integer maxLowerBound,
            Integer minUpperBound, Integer maxUpperBound)
    {
        this.checkRangeless = checkRangeless;
        minLower = (minLowerBound == null) ? Integer.MIN_VALUE : minLowerBound;
        maxLower = (maxLowerBound == null) ? Integer.MAX_VALUE : maxLowerBound;
        minUpper = (minUpperBound == null) ? Integer.MIN_VALUE : minUpperBound;
        maxUpper = (maxUpperBound == null) ? Integer.MAX_VALUE : maxUpperBound;
    }

    /**
     * Disable checking of integer types in annotations.
     *
     * @return The check instance, for daisy-chaining.
     */
    public TypeIntBoundsCheck ignoreAnnotations() {
        return ignoreAnnotations(true);
    }

    /**
     * Configure whether to disable checking of integer types in annotations.
     *
     * @param ignore {@code true} to disable, {@code false} to enable.
     * @return The check instance, for daisy-chaining.
     */
    public TypeIntBoundsCheck ignoreAnnotations(boolean ignore) {
        this.ignoreAnnotations = ignore;
        return this;
    }

    @Override
    protected void preprocessIntType(IntType intType, CifCheckViolations violations) {
        // Skip the check, if applicable.
        if (!checkRangeless && CifTypeUtils.isRangeless(intType)) {
            return;
        }
        if (ignoreAnnotations && CifAnnotationUtils.isObjInAnnotation(intType)) {
            return;
        }

        // Do the check.
        int lower = CifTypeUtils.getLowerBound(intType);
        if (lower < minLower) {
            violations.add(intType, "Integer type lower bound is less than %d", minLower);
        }
        if (lower > maxLower) {
            violations.add(intType, "Integer type lower bound is greater than %d", maxLower);
        }

        int upper = CifTypeUtils.getUpperBound(intType);
        if (upper < minUpper) {
            violations.add(intType, "Integer type upper bound is less than %d", minUpper);
        }
        if (upper > maxUpper) {
            violations.add(intType, "Integer type upper bound is greater than %d", maxUpper);
        }
    }
}
