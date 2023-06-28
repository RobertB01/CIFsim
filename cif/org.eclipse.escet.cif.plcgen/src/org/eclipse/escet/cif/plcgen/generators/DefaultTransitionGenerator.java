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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.plcgen.conversion.PlcFunctionAppls;
import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;

/** Generator for creating PLC code to perform CIF event transitions in the PLC. */
public class DefaultTransitionGenerator implements TransitionGenerator {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /**
     * The event transitions to generate. Is {@code null} until the transitions are provided with
     * {@link #setTransitions}.
     */
    private List<CifEventTransition> eventTransitions = null;

    /** Expression generator for the main program. */
    private ExprGenerator mainExprGen;

    /** Generation of standard PLC functions. */
    private PlcFunctionAppls funcAppls;

    /**
     * Constructor of the {@link DefaultTransitionGenerator} class.
     *
     * @param target PLC target to generate code for.
     */
    public DefaultTransitionGenerator(PlcTarget target) {
        this.target = target;
    }

    @Override
    public void setTransitions(List<CifEventTransition> eventTransitions) {
        this.eventTransitions = eventTransitions;
    }

    @Override
    public void generate() {
        List<PlcStatement> statements = generateCode();
        // TODO Push generated code to the PLC storage generator.
    }

    /**
     * Generate PLC code for all given event transitions.
     *
     * <p>
     * The high-level entry point to generate event transition code for all events. The high level view of the code is:
     * <pre>
     * isProgress := FALSE;
     *
     * isFeasible := &lt;test-if-eventA-is-enabled&gt;;
     * IF isFeasible THEN
     *     isProgress := TRUE;
     *     &lt;perform-transition-of-eventA&gt;
     * END_IF;
     *
     * isFeasible := &lt;test-if-eventB-is-enabled&gt;;
     * IF isFeasible THEN
     *     isProgress := TRUE;
     *     &lt;perform-transition-of-eventB&gt;
     * END_IF;
     *
     * ... // Other transitions are omitted.
     * </pre> More details on the implementation of testing and performing can be found in the
     * {@link #generateEventTransition} method.
     * </p>
     *
     * @return The generated PLC event transition code.
     */
    List<PlcStatement> generateCode() {
        mainExprGen = target.getCodeStorage().getExprGenerator();
        PlcVariable isProgressVar = target.getCodeStorage().getIsProgressVariable();
        funcAppls = new PlcFunctionAppls(target);

        // As all transition code is generated in main program context, only one generated statements list exists and
        // various variables that store decisions in the process can be re-used between different events.
        List<PlcStatement> statements = list();
        for (CifEventTransition eventTransition: eventTransitions) {
            statements.addAll(generateEventTransition(eventTransition, isProgressVar));
        }
        mainExprGen.releaseTempVariable(isProgressVar);
        return statements;
    }

    /**
     * Generate PLC code for testing and performing of the provided event transition.
     *
     * @param eventTransition Event transition to generate code for.
     * @param isProgressVar PLC variable to set if the event transition is performed.
     * @return The generated code for testing and performing the event in the PLC.
     */
    private List<PlcStatement> generateEventTransition(CifEventTransition eventTransition, PlcVariable isProgressVar) {
        return List.of(); // Temporary stub.
    }
}
