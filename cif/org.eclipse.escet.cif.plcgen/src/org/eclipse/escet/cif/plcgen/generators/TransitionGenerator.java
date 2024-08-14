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

import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;

/** Interface for generators that construct PLC code for performing CIF events in the PLC. */
public interface TransitionGenerator {
    /**
     * Setup the transition generator.
     *
     * @param allTransitions Descriptions of all the event transitions that should be generated.
     */
    void setup(List<CifEventTransition> allTransitions);

    /**
     * Generate the event transition functions.
     *
     * @param exprGen Expression generator for the scope of the generated code.
     * @param isProgressVar The variable to set if an event transition is performed.
     */
    void generate(ExprGenerator exprGen, PlcBasicVariable isProgressVar);
}
