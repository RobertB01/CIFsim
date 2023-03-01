//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.statements;

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;

/** Generic code selection statement. */
public class PlcSelectionStatement extends PlcStatement {
    /** Conditional choices to consider in the code selection. */
    public List<PlcSelectChoice> condChoices;

    /** Choice to perform if none of the {@link #condChoices} can be chosen. */
    public List<PlcStatement> elseStats;

    /**
     * Constructor of the {@link PlcSelectionStatement} class.
     *
     * @param condChoices Conditional choices to consider in the code selection.
     * @param elseStats Choice to perform if none of the {@link #condChoices} can be chosen.
     */
    public PlcSelectionStatement(List<PlcSelectChoice> condChoices, List<PlcStatement> elseStats) {
        this.condChoices = condChoices;
        this.elseStats = elseStats;
    }

    /** A choice in the selection statement. */
    public static class PlcSelectChoice {
        /** Condition that must hold to select this choice. */
        public PlcExpression guard;

        /** Statements to execute if the guard holds. */
        public List<PlcStatement> thenStats;

        /**
         * Constructor of the {@link PlcSelectChoice} class.
         *
         * @param guard Condition that must hold to select this choice.
         * @param thenStats Statements to execute if the guard holds.
         */
        public PlcSelectChoice(PlcExpression guard, List<PlcStatement> thenStats) {
            this.guard = guard;
            this.thenStats = thenStats;
        }
    }
}
