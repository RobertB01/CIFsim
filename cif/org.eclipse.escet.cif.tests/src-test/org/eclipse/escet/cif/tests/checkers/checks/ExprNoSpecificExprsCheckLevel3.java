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

package org.eclipse.escet.cif.tests.checkers.checks;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;

/**
 * {@link ExprNoSpecificExprsCheck} with all level 3 disalloweds enabled.
 *
 * <p>
 * The different levels allow testing more logic (increased code coverage). E.g., disallowing
 * {@link NoSpecificExpr#PROJECTION_EXPRS} includes disallowing {@link NoSpecificExpr#PROJECTION_EXPRS_LISTS}, meaning
 * that if the former is disallowed, the latter is not checked. The former has level 1 (one underscore in the enum
 * literal name) and the latter has level 2 (two underscores in the enum literal name), so testing these disalloweds
 * separately leads to increased code coverage.
 * </p>
 */
@SuppressWarnings("javadoc")
public class ExprNoSpecificExprsCheckLevel3 extends ExprNoSpecificExprsCheck {
    /** Constructor for the {@link ExprNoSpecificExprsCheckLevel3} class. */
    public ExprNoSpecificExprsCheckLevel3() {
        super(Arrays.stream(NoSpecificExpr.values()).filter(v -> StringUtils.countMatches(v.name(), "_") == 3)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(NoSpecificExpr.class))));
    }
}
