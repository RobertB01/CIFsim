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
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;

/** Interface for generators that construct PLC code for performing CIF events in the PLC. */
public interface TransitionGenerator {
    /**
     * Setup the transition generator.
     *
     * @param allTransitions Descriptions of all the event transitions that should be generated.
     */
    void setup(List<CifEventTransition> allTransitions);

    /**
     * Generate event transition statements for a scope.
     *
     * <p>
     * The generated code sets the given {@code isProgressVar} variable when a transition is taken. The generator also
     * usually adds temporary variables into the scope that are needed for the generated code to function.
     * </p>
     *
     * @param transSeqs One or more event sequences. Each event sequence is to be converted to a PLC statement sequence.
     *     All provided transitions must have been setup already during the {@link #setup} call. All generated code is
     *     assumed to be executed in the same scope.
     * @param exprGen Expression generator for the scope of the generated code.
     * @param isProgressVar The variable to set if an event transition is performed.
     * @return The sequence of statements for each event sequence.
     */
    List<List<PlcStatement>> generate(List<List<CifEventTransition>> transSeqs,
            ExprGenerator exprGen, PlcBasicVariable isProgressVar);
}
