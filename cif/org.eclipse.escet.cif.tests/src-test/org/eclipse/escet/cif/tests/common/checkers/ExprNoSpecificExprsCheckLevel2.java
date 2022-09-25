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

package org.eclipse.escet.cif.tests.common.checkers;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck;

/** {@link ExprNoSpecificExprsCheck} with all level 2 disalloweds enabled. */
public class ExprNoSpecificExprsCheckLevel2 extends ExprNoSpecificExprsCheck {
    /** Constructor for the {@link ExprNoSpecificExprsCheckLevel2} class. */
    public ExprNoSpecificExprsCheckLevel2() {
        super(Arrays.stream(NoSpecificExpr.values()).filter(v -> StringUtils.countMatches(v.name(), "_") == 2)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(NoSpecificExpr.class))));
    }
}
