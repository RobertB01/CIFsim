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

package org.eclipse.escet.cif.plcgen.generators;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;

/** Interface for generators that generate code for handling continuous variables. */
public interface ContinuousVariablesGenerator {
    /**
     * Add a new continuous variable to generate code for.
     *
     * @param contVar New continuous variable to add.
     */
    void addVariable(ContVariable contVar);

    /**
     * Add the necessary information to the PLC code storage to support continuous variables delivered to the instance
     * with {@link #addVariable} previously.
     */
    void process();

    /**
     * Get a code generator for the provided variable.
     *
     * @param cvar Variable to generate code for.
     * @return A code generator for the provided variable.
     */
    public PlcTimerCodeGenerator getPlcTimerCodeGen(ContVariable cvar);

    /** PLC code generation support for a single continuous variable. */
    public interface PlcTimerCodeGenerator {
        /** Add the variables of the timer to the provided PLC code storage. */
        public void addInstanceVariables();

        /**
         * Generate code to update the variable that stores the remaining time of the timer in the state.
         *
         * @return The code to update the variable of the remaining time for the timer, and a set of temporary variables
         *     to return to the main program expression generator after the code has been (logically) executed.
         */
        public List<PlcStatement> generateRemainingUpdate();

        /**
         * Copy the new value of the continuous variable in the state to the timer.
         *
         * @return Statements performing the assignment.
         */
        public List<PlcStatement> generateAssignPreset();
    }
}
