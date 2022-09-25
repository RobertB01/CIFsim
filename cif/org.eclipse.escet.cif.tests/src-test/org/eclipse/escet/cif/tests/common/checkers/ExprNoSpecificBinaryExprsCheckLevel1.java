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
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificBinaryExprsCheck;

/** {@link ExprNoSpecificBinaryExprsCheck} with all level 1 disalloweds enabled. */
public class ExprNoSpecificBinaryExprsCheckLevel1 extends ExprNoSpecificBinaryExprsCheck {
    /** Constructor for the {@link ExprNoSpecificBinaryExprsCheckLevel1} class. */
    public ExprNoSpecificBinaryExprsCheckLevel1() {
        super(Arrays.stream(NoSpecificBinaryOp.values()).filter(v -> StringUtils.countMatches(v.name(), "_") == 1)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(NoSpecificBinaryOp.class))));
    }
}
