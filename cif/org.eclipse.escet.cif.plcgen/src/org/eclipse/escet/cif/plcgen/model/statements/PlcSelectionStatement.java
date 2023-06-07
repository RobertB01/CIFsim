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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;

/** Generic code selection statement. */
public class PlcSelectionStatement extends PlcStatement {
    /** Conditional choices to consider in the code selection. */
    public List<PlcSelectChoice> condChoices;

    /** Choice to perform if none of the {@link #condChoices} can be chosen. */
    public List<PlcStatement> elseStats;

    /** Constructor of the {@link PlcSelectionStatement} class. */
    public PlcSelectionStatement() {
        this(list(), list());
    }

    /**
     * Constructor of the {@link PlcSelectionStatement} class.
     *
     * @param condChoice Conditional choice to consider in the code selection.
     */
    public PlcSelectionStatement(PlcSelectChoice condChoice) {
        this(list(condChoice));
    }

    /**
     * Constructor of the {@link PlcSelectionStatement} class.
     *
     * @param condChoices Conditional choices to consider in the code selection.
     */
    public PlcSelectionStatement(List<PlcSelectChoice> condChoices) {
        this(condChoices, list());
    }

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
         * @param thenStat Statement to execute if the guard holds.
         */
        public PlcSelectChoice(PlcExpression guard, PlcStatement thenStat) {
            this(guard, list(thenStat));
        }

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

        /**
         * Make a copy of the selection choice.
         *
         * @return The newly created copy of the selection choice.
         */
        public PlcSelectChoice copy() {
            return new PlcSelectChoice(guard, PlcStatement.copy(thenStats));
        }
    }

    @Override
    public PlcStatement copy() {
        List<PlcSelectChoice> choices = listc(condChoices.size());
        condChoices.stream().map(PlcSelectChoice::copy).collect(Collectors.toCollection(() -> choices));
        return new PlcSelectionStatement(choices, PlcStatement.copy(elseStats));
    }
}
