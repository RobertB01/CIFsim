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

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;

/**
 * Code generator for continuous variables handling.
 *
 * <p>
 * We restrict continuous variables to timers, as that is the primary use of CIF continuous variables in controllers.
 * Setting a value for the delay should be possible, as well as detecting that the delay has reached its end. A
 * secondary use is to check how much time is still remaining (which seems more relevant than how long ago the timer has
 * been started).
 * </p>
 * <p>
 * In the PLC, TON timers are used. One TON block for each continuous CIF variable. A TON timer can be started by
 * calling it with a preset time value (in seconds). There is a second way to call the TON block to just update output
 * for passed time since the last call. After each call the TON block outputs its current value. That value starts at
 * {@code 0} when setting a value, and increases with time until it reaches the preset value after 'preset value'
 * seconds. The output does not increase further after reaching the preset value even if more time passes.
 * </p>
 * <p>
 * To query remaining time of a running timer in CIF, it's simplest for the users if they can state such a query as
 * {@code val <= N} to query whether remaining time is equal or less than {@code N} seconds. Testing for timeout then
 * becomes {@code val <= 0}. This implies CIF continuous variables must have derivative {@code -1} and assigned values
 * to continuous variables must be non-negative.
 * </p>
 * <p>
 * As the PLC output value goes up, in the implementation the remaining time in the PLC must be computed by subtracting
 * the output value from the last preset value of the TON block. Since the output value stops increasing when the
 * timeout has been reached, a comparison in CIF against purely negative values will fail in the implementation. To
 * ensure the CIF notion of continuous variables is maintained in the PLC implementation, only comparisons of continuous
 * variables against a single contiguous range with at least one non-negative value is allowed.
 * </p>
 * <p>
 * Translation of continuous variables is as follows. For each continuous variable there is a preset variable {@code P},
 * a state variable for the remaining time {@code R}, and a {@code TON} variable containing the timer instance.
 * <ul>
 * <li>Before the input phase, the {@code R} variable is updated by calling
 * {@code TON(PT := P, IN := TRUE, Q => B, ET => V);} and computing {@code R := SEL(B, P - V, 0.0);} with temporary
 * variables {@code V and B}.</li>
 * <li>Obtaining the remaining time of the timer reads variable {@code R}.</li>
 * <li>Assigning a new value to the continuous variable changes {@code R} and {@code P}, and resets the timer with
 * {@code TON(PT := P, IN := FALSE);} followed by starting the timer with {@code TON(PT := P, IN := TRUE);}.</li>
 * <li>Comparing the continuous variable against a value {@code V} using comparison operator {@code c} generates the
 * infix expression {@code R c V}.</li>
 * </ul>
 * The effect of the above is that for the transitions, time has stopped but all timers keep running.
 * </p>
 */
public class DefaultContinuousVariablesGenerator implements ContinuousVariablesGenerator {
    /** PLC target to generate code for. */
    private final PlcTarget target;

    /** Continuous variables in the specification with an instance to generate code for each variable. */
    private final Map<ContVariable, PlcTimerCodeGenerator> timers = map();

    /**
     * Constructor of the {@link DefaultContinuousVariablesGenerator} class.
     *
     * @param target PLC target to generate code for.
     */
    public DefaultContinuousVariablesGenerator(PlcTarget target) {
        this.target = target;
    }

    @Override
    public void addVariable(ContVariable contVar) {
        // XXX to implement.
    }

    @Override
    public void process() {
        // XXX to implement.
    }

    @Override
    public PlcTimerCodeGenerator getPlcTimerCodeGen(ContVariable cvar) {
        // XXX to implement.
        return null;
    }
}
