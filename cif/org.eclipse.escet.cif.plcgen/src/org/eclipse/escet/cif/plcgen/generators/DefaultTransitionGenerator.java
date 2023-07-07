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

import org.eclipse.escet.cif.plcgen.conversion.expressions.ExprGenerator;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;

/** Generator for creating PLC code to perform CIF event transitions in the PLC. */
public class DefaultTransitionGenerator implements TransitionGenerator {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /**
     * The event transitions to generate. Is {@code null} until the transitions are provided with
     * {@link #setTransitions}.
     */
    private CifEventTransition transitions = null;

    /**
     * Constructor of the {@link DefaultTransitionGenerator} class.
     *
     * @param target PLC target to generate code for.
     */
    public DefaultTransitionGenerator(PlcTarget target) {
        this.target = target;
    }

    @Override
    public void setTransitions(CifEventTransition transitions) {
        this.transitions = transitions;
    }

    @Override
    public void generate() {
        ExprGenerator mainExprGen = target.getCodeStorage().getExprGenerator();

        // TODO Implement code generation of event transition function code.
    }
}
