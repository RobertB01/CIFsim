//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model;

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;

/** Utility functions for model classes. */
public class PlcModelUtils {
    /** Constructor of the static {@link PlcModelUtils} class. */
    private PlcModelUtils() {
        // Static class.
    }

    /**
     * Does executing the given statements (likely) have an effect?
     *
     * @param statements Statements to analyze.
     * @return Whether the statement are (likely) having an effect.
     * @note The function assumes all statements except the {@link PlcCommentLine} have an effect.
     */
    public static boolean isNonEmptyCode(List<PlcStatement> statements) {
        for (PlcStatement plcStat: statements) {
            if (!(plcStat instanceof PlcCommentLine)) {
                return true;
            }
        }
        return false;
    }
}
