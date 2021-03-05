//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.runtime.model;

import static org.eclipse.escet.cif.simulator.runtime.io.RuntimeValueToString.runtimeToString;

import java.util.List;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.input.InputComponent;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.io.RuntimeValueToString;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;
import org.eclipse.escet.cif.simulator.runtime.meta.StateObjectType;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;

/**
 * Runtime state representation.
 *
 * <p>
 * Derived classes should have a field per sub-state (state of an automaton or the state of the continuous variables
 * declared outside automata, including continuous variable 'time').
 * </p>
 */
public abstract class RuntimeState {
    /** The specification for which this is a state. */
    public final RuntimeSpec<?> spec;

    /**
     * Constructor for the {@link RuntimeState} class.
     *
     * @param spec The specification for which this is a state.
     */
    public RuntimeState(RuntimeSpec<?> spec) {
        this.spec = spec;
    }

    /**
     * Returns the value of variable 'time' in this state.
     *
     * @return The value of variable 'time' in this state.
     */
    public abstract double getTime();

    /**
     * Returns a textual representation of the value of variable 'time', in this state. The textual representation
     * closely resembles the CIF ASCII syntax.
     *
     * @return A textual representation of the value of variable 'time', in this state.
     */
    public String getTimeText() {
        return CifSimulatorMath.realToStr(getTime());
    }

    /**
     * Returns the number of automata present in the specification.
     *
     * @return The number of automata present in the specification.
     */
    public int getAutCount() {
        return spec.automata.size();
    }

    /**
     * Returns the name of the current location of the given automata.
     *
     * @param idx The 0-based index of the automaton, in {@link RuntimeSpec#automata}.
     * @return The name of the current location of the given automata.
     */
    public abstract String getAutCurLocName(int idx);

    /**
     * Returns the names of the current locations of the automata. Each entry in the result matches the corresponding
     * entry in {@link #spec}.{@link RuntimeSpec#automata automata}.
     *
     * @return The names of the current locations of the automata.
     */
    public String[] getAutCurLocNames() {
        String[] rslt = new String[getAutCount()];
        for (int i = 0; i < rslt.length; i++) {
            rslt[i] = getAutCurLocName(i);
        }
        return rslt;
    }

    /**
     * Returns the number of the state variables, excluding the location pointer variables and variable 'time'.
     *
     * @return The number of the state variables.
     */
    public int getStateVarCount() {
        return getStateVarNames().length;
    }

    /**
     * Returns the name of the given state variable. State variables do not include the location pointer variables and
     * variable 'time'.
     *
     * @param idx The 0-based index of the state variable.
     * @return The name of the given state variable.
     */
    public String getStateVarName(int idx) {
        return getStateVarNames()[idx];
    }

    /**
     * Returns the names of the state variables, excluding the location pointer variables and variable 'time'. The names
     * are sorted in ascending order.
     *
     * <p>
     * It is assumed that implementations return the same cached array on every invocation.
     * </p>
     *
     * @return The names of the state variables.
     */
    public abstract String[] getStateVarNames();

    /**
     * Returns the value of the state variable with the given index.
     *
     * @param idx The 0-based index of the variable, in the result of {@link #getStateVarNames}.
     * @return The value of the state variable.
     */
    public abstract Object getStateVarValue(int idx);

    /**
     * Returns the values of the state variables. State variables do not include the location pointer variables and
     * variable 'time'.
     *
     * @return The values of the state variables.
     */
    public Object getStateVarValues() {
        Object[] rslt = new Object[getStateVarCount()];
        for (int i = 0; i < rslt.length; i++) {
            rslt[i] = getStateVarValue(i);
        }
        return rslt;
    }

    /**
     * Returns the value of the derivative of the continuous state variable with the given index.
     *
     * @param idx The 0-based index of the variable, in the result of {@link #getStateVarNames}. Must be a continuous
     *     variable.
     * @return The value of the derivative of the continuous state variable.
     */
    public abstract double getStateVarDerValue(int idx);

    /**
     * Returns the number of available algebraic variables.
     *
     * @return The number of available algebraic variables.
     */
    public int getAlgVarCount() {
        return getAlgVarNames().length;
    }

    /**
     * Returns the name of the given algebraic variable.
     *
     * @param idx The 0-based index of the algebraic variable, in the result of {@link #getAlgVarNames}.
     * @return The name of the given algebraic variable.
     */
    public String getAlgVarName(int idx) {
        return getAlgVarNames()[idx];
    }

    /**
     * Returns the names of the algebraic variables. The names are sorted in ascending order.
     *
     * <p>
     * It is assumed that implementations return the same cached array on every invocation.
     * </p>
     *
     * @return The names of the algebraic variables.
     */
    public abstract String[] getAlgVarNames();

    /**
     * Returns the value of the algebraic variable with the given index.
     *
     * @param idx The 0-based index of the variable, in the result of {@link #getAlgVarNames}.
     * @return The value of the algebraic variable.
     */
    public abstract Object getAlgVarValue(int idx);

    /**
     * Returns the values of the algebraic variables.
     *
     * @return The values of the algebraic variables.
     */
    public Object[] getAlgVarValues() {
        Object[] rslt = new Object[getAlgVarCount()];
        for (int i = 0; i < rslt.length; i++) {
            rslt[i] = getAlgVarValue(i);
        }
        return rslt;
    }

    /**
     * Returns the value of the variable referred to by the given runtime state object meta data.
     *
     * @param objMeta The runtime state object meta data for the variable for which to return the value.
     * @return The value of the variable.
     * @throws IllegalArgumentException If the state object meta data does not refer to a variable.
     */
    public Object getVarValue(RuntimeStateObjectMeta objMeta) {
        switch (objMeta.type) {
            case AUTOMATON:
                throw new IllegalArgumentException("objMeta not an automaton");

            case TIME:
                return getTime();

            case DISCRETE:
            case CONTINUOUS:
                return getStateVarValue(objMeta.idx);

            case DERIVATIVE:
                return getStateVarDerValue(objMeta.idx);

            case ALGEBRAIC:
                return getAlgVarValue(objMeta.idx);
        }

        // Should never get here.
        throw new RuntimeException("Unknown type: " + objMeta.type);
    }

    /**
     * Returns a textual representation of the value of the state object referred to by the given runtime state object
     * meta data. For automata, the name of the current location is returned. For variables, the textual representation
     * of the value is obtained via {@link RuntimeValueToString#runtimeToString}.
     *
     * @param objMeta The runtime state object meta data for the state object for which to return a textual
     *     representation of the value.
     * @return The textual representation of the value of the state object.
     */
    public String getStateObjValueText(RuntimeStateObjectMeta objMeta) {
        switch (objMeta.type) {
            case AUTOMATON:
                return getAutCurLocName(objMeta.idx);

            case TIME:
                return getTimeText();

            case DISCRETE:
            case CONTINUOUS:
                return runtimeToString(getStateVarValue(objMeta.idx));

            case DERIVATIVE:
                return runtimeToString(getStateVarDerValue(objMeta.idx));

            case ALGEBRAIC:
                return runtimeToString(getAlgVarValue(objMeta.idx));
        }

        // Should never get here.
        throw new RuntimeException("Unknown type: " + objMeta.type);
    }

    /**
     * Checks the initialization predicates and state invariants on the (initial) state.
     *
     * @return {@code true} if all the initialization and state invariant predicates evaluated to {@code true},
     *     {@code false} otherwise.
     */
    public abstract boolean checkInitialization();

    /**
     * Calculates the transitions that are possible from this state. After this method has been invoked, the possible
     * transitions can be retrieved from {@link RuntimeSpec#transitions}.
     *
     * <p>
     * Implementations should simply invoke the {@link RuntimeSpec#calcTransitions calcTransitions} from the
     * implementation of the specification.
     * </p>
     *
     * @param endTime The user-provided simulation end time, or {@code null} for infinite.
     * @param maxDelay The maximum delay for a single time transition, or {@code null} for infinite. A maximum delay of
     *     {@code 0.0} disallows time passage.
     * @see RuntimeSpec#calcTransitions
     */
    public abstract void calcTransitions(Double endTime, Double maxDelay);

    /**
     * Calculates the time transition from this state. The transition, is added to {@link RuntimeSpec#transitions}. This
     * method is only used for the re-computation of time transitions, and not for the first calculation of a time
     * transition for this state. The caller should clear {@link RuntimeSpec#transitions} prior to calling this method.
     *
     * @param endTime The user-provided simulation end time, or {@code null} for infinite.
     */
    public abstract void calcTimeTransition(Double endTime);

    /**
     * Let the input component choose a transition to take.
     *
     * @param state The source state of the transitions.
     * @param transitions The transitions that are possible from the current state. May be empty if no transitions are
     *     possible.
     * @param result Indicates the simulation result, in case no transitions are possible. Is {@code null} if at least
     *     one transition is possible. Is {@link SimulationResult#DEADLOCK} or {@link SimulationResult#ENDTIME_REACHED}
     *     otherwise.
     * @return The transition that is chosen to be taken.
     * @throws SimulatorExitException If simulation is to stop.
     * @see InputComponent#chooseTransition
     */
    public abstract Transition<?> chooseTransition(RuntimeState state, List<Transition<?>> transitions,
            SimulationResult result);

    /**
     * Let the input component choose the target time of the time transition to take, given the allowed interval
     * {@code (getTime() .. maxTargetTime]}. Returning {@code null} is allowed, to indicate the maximum allowed target
     * time.
     *
     * @param maxTargetTime The maximum allowed target time.
     * @return The chosen target time of the time transition to take.
     */
    public abstract ChosenTargetTime chooseTargetTime(double maxTargetTime);

    /**
     * Let the input component return a value indicating the maximum allowed end time, for the next time transition to
     * calculate.
     *
     * @return The maximum allowed end time, for the next time transition to calculate, or {@code null} to not impose
     *     any additional restrictions on the maximum allowed end time.
     */
    public abstract Double getNextMaxEndTime();

    /**
     * Returns a textual representation of the state, as multiple lines of text. The textual representation closely
     * resembles the CIF ASCII syntax.
     *
     * <p>
     * Note that the {@link #toSingleLineString} method return a similar representation, using a single line.
     * </p>
     *
     * <p>
     * The output does not include the algebraic variables and derivatives of the continuous variables.
     * </p>
     *
     * @return A textual representation of the state, as multiple lines of text.
     */
    @Override
    public String toString() {
        return toString(null, false, false);
    }

    /**
     * Returns a textual representation of the state, as multiple lines of text. The textual representation closely
     * resembles the CIF ASCII syntax.
     *
     * <p>
     * Note that the {@link #toSingleLineString} method return a similar representation, using a single line.
     * </p>
     *
     * @param metas The state objects to include. May be {@code null} to include all state objects. The algebraic
     *     variables and derivatives may be further filtered using the remaining arguments to this method.
     * @param includeAlgVars Whether to include the algebraic variables.
     * @param includeDerivs Whether to include the derivatives of the continuous variables (excluding variable 'time').
     * @return A textual representation of the state, as multiple lines of text.
     */
    public String toString(List<RuntimeStateObjectMeta> metas, boolean includeAlgVars, boolean includeDerivs) {
        return toString("\n", metas, includeAlgVars, includeDerivs);
    }

    /**
     * Returns a textual representation of the state, as a single line of text. The textual representation closely
     * resembles the CIF ASCII syntax.
     *
     * <p>
     * Note that the {@link #toString} method return a similar representation, using multiple lines.
     * </p>
     *
     * @param metas The state objects to include. May be {@code null} to include all state objects. The algebraic
     *     variables and derivatives may be further filtered using the remaining arguments to this method.
     * @param includeAlgVars Whether to include the algebraic variables.
     * @param includeDerivs Whether to include the derivatives of the continuous variables (excluding variable 'time').
     * @return A textual representation of the state, as a single line of text.
     */
    public String toSingleLineString(List<RuntimeStateObjectMeta> metas, boolean includeAlgVars,
            boolean includeDerivs)
    {
        return toString(", ", metas, includeAlgVars, includeDerivs);
    }

    /**
     * Returns a textual representation of the state, with a custom separator between the textual representations of the
     * individual state objects. The textual representation closely resembles the CIF ASCII syntax.
     *
     * @param separator The separator to use between the textual representations of the individual state objects.
     * @param metas The state objects to include. May be {@code null} to include all state objects. The algebraic
     *     variables and derivatives may be further filtered using the remaining arguments to this method.
     * @param includeAlgVars Whether to include the algebraic variables.
     * @param includeDerivs Whether to include the derivatives of the continuous variables (excluding variable 'time').
     * @return A textual representation of the state.
     */
    public String toString(String separator, List<RuntimeStateObjectMeta> metas, boolean includeAlgVars,
            boolean includeDerivs)
    {
        StringBuilder rslt = new StringBuilder();
        boolean first = true;
        if (metas == null) {
            metas = spec.stateObjectsMeta;
        }
        for (RuntimeStateObjectMeta meta: metas) {
            // Skip object, if requested.
            if (meta.type == StateObjectType.ALGEBRAIC && !includeAlgVars) {
                continue;
            }
            if (meta.type == StateObjectType.DERIVATIVE && !includeDerivs) {
                continue;
            }

            // Add separator.
            if (first) {
                first = false;
            } else {
                rslt.append(separator);
            }

            // Add state object.
            rslt.append(meta.name);
            rslt.append("=");
            rslt.append(getStateObjValueText(meta));
        }
        return rslt.toString();
    }
}
