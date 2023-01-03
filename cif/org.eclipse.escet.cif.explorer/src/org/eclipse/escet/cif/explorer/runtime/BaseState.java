//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.List;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifLocationUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Base class for states, symbol table with values used for evaluation. */
public abstract class BaseState {
    /** Managing object of the exploration. */
    protected final Explorer explorer;

    /** Current location of each automaton in the specification. */
    public final Location[] locations;

    /** Current value of each variable in the state. */
    public final Object[] values;

    /**
     * Unique number of this state, negative until the state is accepted as authorative state. After generating all
     * states, they are renumbered to make them consecutive.
     *
     * <p>
     * Assigning a number twice has the advantage that debug output is readable, although the numbers are not
     * consecutive.
     * </p>
     */
    public int stateNumber = -1;

    /**
     * Constructor of the {@link BaseState} class.
     *
     * @param explorer Managing object of the exploration.
     * @param locations Current location of each automaton in the specification.
     * @param values Current value of each variable in the state.
     */
    public BaseState(Explorer explorer, Location[] locations, Object[] values) {
        this.explorer = explorer;
        this.locations = locations;
        this.values = values;
    }

    /**
     * Evaluate a given expression in this state.
     *
     * @param expr Expression to evaluate.
     * @param commVal Value being communicated if available, else {@code null}.
     * @return Value of the expression in this state.
     * @throws CifEvalException When evaluation fails. Should be caught, and re-thrown, after adding context information
     *     to it.
     */
    public Object eval(Expression expr, Object commVal) throws CifEvalException {
        return explorer.evaluator.eval(expr, this, commVal);
    }

    /**
     * Outgoing edges from this state. {@code null} means successor states have not been computed yet.
     *
     * <p>
     * Do not access directly, use {@link #getOutgoingEdges} instead.
     * </p>
     */
    public List<ExplorerEdge> outgoingEdges = null;

    /**
     * Incoming edges from successor states, for the set of states computed so far. As more states are discovered, new
     * incoming edges may be added.
     *
     * <p>
     * Do not access directly, use {@link #getIncomingEdges} instead.
     * </p>
     */
    public List<ExplorerEdge> incomingEdges = list();

    /**
     * Get the transitions that are possible from this state. Compute the new states first, if necessary.
     *
     * @return Transitions from this state.
     */
    public List<ExplorerEdge> getOutgoingEdges() {
        if (outgoingEdges == null) {
            outgoingEdges = list();
            explorer.computeOutgoing(this, false);
        }
        return outgoingEdges;
    }

    /**
     * Get newly discovered successor states. The returned set is always a subset of the successor states, but is highly
     * dependent on exploration order. Use {@link #getOutgoingEdges} instead to get the full set of successor states.
     *
     * @return Newly discovered states while looking for immediate successor states.
     */
    public List<BaseState> getNewSuccessorStates() {
        Assert.check(outgoingEdges == null);
        outgoingEdges = list();
        return explorer.computeOutgoing(this, true);
    }

    /**
     * Get the transitions found so far that lead to this state.
     *
     * <p>
     * As the exploration process progresses, new transitions may be added.
     * </p>
     *
     * @return Transitions found so far that lead to this state.
     */
    public List<ExplorerEdge> getIncomingEdges() {
        return incomingEdges;
    }

    /**
     * Remove an incoming edge from the state.
     *
     * @param edge Edge to remove.
     */
    public void removeIncoming(ExplorerEdge edge) {
        // Since we have an edge that should be removed, incomingEdges != null.
        int lastEdge = incomingEdges.size() - 1;
        for (int i = 0; i <= lastEdge; i++) {
            if (incomingEdges.get(i) == edge) {
                if (i < lastEdge) {
                    incomingEdges.set(i, incomingEdges.get(lastEdge));
                }
                incomingEdges.remove(lastEdge);
                return;
            }
        }
        throw new RuntimeException("Could not find edge.");
    }

    /**
     * Remove an outgoing edge from the state.
     *
     * @param edge Edge to remove.
     */
    public void removeOutgoing(ExplorerEdge edge) {
        // Since we have an edge that should be removed, outgoingEdges != null.
        int lastEdge = outgoingEdges.size() - 1;
        for (int i = 0; i <= lastEdge; i++) {
            if (outgoingEdges.get(i) == edge) {
                if (i < lastEdge) {
                    outgoingEdges.set(i, outgoingEdges.get(lastEdge));
                }
                outgoingEdges.remove(lastEdge);
                return;
            }
        }
        throw new RuntimeException("Could not find edge.");
    }

    /**
     * Check whether the state is initial.
     *
     * @return {@code true} if the state is initial, {@code false} otherwise.
     */
    public abstract boolean isInitial();

    /**
     * Check whether the state is marked.
     *
     * @return {@code true} if the state is marked, {@code false} otherwise.
     */
    public abstract boolean isMarked();

    /**
     * Retrieve a value from the symbol table.
     *
     * @param var Variable being queried for its value.
     * @return The value of the variable.
     */
    public abstract Object getVarValue(PositionObject var);

    /**
     * Set or update a value in the symbol table.
     *
     * @param var Variable being given a value.
     * @param value The value of the variable.
     */
    public abstract void setVarValue(PositionObject var, Object value);

    /**
     * Get the location of an automaton.
     *
     * @param autIndex Index of the automaton.
     * @return Current location of the queried automaton.
     */
    public abstract Location getCurrentLocation(int autIndex);

    /**
     * Get the expression associated with the algebraic variable in the current state.
     *
     * @param algVar Algebraic variable being queried.
     * @return Expression denoting the current value of the variable.
     */
    public abstract Expression getAlgExpression(AlgVariable algVar);

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BaseState)) {
            return false;
        }
        BaseState otherVal = (BaseState)other;
        return Arrays.equals(locations, otherVal.locations) && Arrays.equals(values, otherVal.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(locations) + Arrays.hashCode(values) * 911;
    }

    /**
     * Print the state onto the box output stream, in a format suitable for close inspection, for example while
     * debugging.
     *
     * @param box Output stream.
     */
    public void printDebug(CodeBox box) {
        box.add(fmt("State %d:", stateNumber));
        box.indent();

        box.add("Initial: " + (isInitial() ? "true" : "false"));
        box.add("Marked: " + (isMarked() ? "true" : "false"));

        if (locations.length > 0) {
            box.add();
            box.add("Locations:");
            box.indent();
            for (Location loc: locations) {
                box.add(CifTextUtils.getLocationText1(loc));
            }
            box.dedent(); // End of the locations.
        }

        if (values.length > 0) {
            box.add();
            box.add("Valuation:");
            box.indent();
            String[] varNames = explorer.getVariableNames();
            for (int i = 0; i < values.length; i++) {
                String valueText = CifEvalUtils.objToStr(values[i]);
                box.add(fmt("%s = %s", varNames[i], valueText));
            }
            box.dedent(); // End of valuation.
        }

        printOtherStateInformation(box);

        if (outgoingEdges != null && !outgoingEdges.isEmpty()) {
            box.add();
            box.add("Edges:");
            box.indent();
            for (ExplorerEdge edge: outgoingEdges) {
                String eventName, sendValue, target;

                eventName = (edge.event == null) ? "tau" : CifTextUtils.getAbsName(edge.event);
                sendValue = (edge.commValue == null) ? "" : " value " + CifEvalUtils.objToStr(edge.commValue);
                target = fmt(" goto state %d", edge.next.stateNumber);
                box.add("edge " + eventName + sendValue + target);
            }
            box.dedent(); // End of edges.
        }

        box.dedent(); // End of the state.
    }

    /**
     * Output additional information related to an explored state.
     *
     * <p>
     * When overriding and adding output, first generate an empty line.
     * </p>
     *
     * @param box Output stream.
     */
    protected void printOtherStateInformation(CodeBox box) {
        // By default, print nothing.
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();

        // Current locations of automata.
        for (int i = 0; i < locations.length; i++) {
            if (txt.length() > 0) {
                txt.append(", ");
            }
            txt.append(CifTextUtils.getAbsName(explorer.automata[i]));
            txt.append(": ");
            Location loc = locations[i];
            if (loc == null) {
                txt.append("?");
            } else {
                txt.append(CifLocationUtils.getName(loc));
            }
        }

        // Values of all variables.
        for (int i = 0; i < values.length; i++) {
            if (txt.length() > 0) {
                txt.append(", ");
            }
            txt.append(CifTextUtils.getAbsName(explorer.variables[i]));
            txt.append(": ");
            Object value = values[i];
            if (value == null) {
                txt.append("?");
            } else {
                txt.append(CifEvalUtils.objToStr(value));
            }
        }

        return txt.toString();
    }
}
