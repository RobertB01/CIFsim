//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2mcrl2.storage;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.cif2mcrl2.Cif2Mcrl2PreChecker;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.java.Assert;

/** Storage of information of an automaton. */
public class AutomatonData {
    /** Automaton stored in this object. */
    public final Automaton aut;

    /** Absolute name of the automaton in CIF. */
    public final String name;

    /** Initial location of the automaton. */
    public final Location initialLocation;

    /** Discrete variables used in the automaton, with mapping to their representatives in the translation. */
    public Map<DiscVariable, VariableData> vars;

    /**
     * Constructor of the {@link AutomatonData} class.
     *
     * @param aut Automaton to store in the object.
     * @param initialLocation Initial location of the automaton.
     */
    public AutomatonData(Automaton aut, Location initialLocation) {
        this.aut = aut;
        name = CifTextUtils.getAbsName(aut, false);
        this.initialLocation = initialLocation;
        vars = null;
    }

    /**
     * Find discrete variables in an automaton.
     *
     * <p>
     * Copy of {@link Cif2Mcrl2PreChecker#checkAutomaton}, but without checking on acceptable constructs.
     * </p>
     *
     * @param variableMap Mapping of discrete variables in the meta model to their representation in the translation.
     */
    public void addAutomatonVars(Map<DiscVariable, VariableData> variableMap) {
        Set<DiscVariable> dvs = set();
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                dvs.addAll(getEdgeReadVariables(edge));
                dvs.addAll(getEdgeWriteVariables(edge));
            }
        }

        vars = map();
        for (DiscVariable dv: dvs) {
            vars.put(dv, variableMap.get(dv));
        }
    }

    /**
     * Get the read variables of an edge.
     *
     * @param edge Edge to inspect.
     * @return Set of variables being read in the edge.
     */
    public Set<VariableData> getReadVariables(Edge edge) {
        Set<VariableData> vds = set();
        for (DiscVariable dv: getEdgeReadVariables(edge)) {
            vds.add(vars.get(dv));
        }
        return vds;
    }

    /**
     * Get the read variables of an edge.
     *
     * @param edge Edge to inspect.
     * @return Set of variables being read in the edge.
     */
    private static Set<DiscVariable> getEdgeReadVariables(Edge edge) {
        Set<DiscVariable> dvs = set();

        for (Update upd: edge.getUpdates()) {
            Assignment asg = (Assignment)upd;
            dvs.addAll(findExpressionVars(asg.getValue()));
        }
        for (Expression e: edge.getGuards()) {
            dvs.addAll(findExpressionVars(e));
        }
        return dvs;
    }

    /**
     * Get the written variables of an edge.
     *
     * @param edge Edge to inspect.
     * @return Set of variables being written in the edge.
     */
    public Set<VariableData> getWriteVariables(Edge edge) {
        Set<VariableData> vds = set();
        for (DiscVariable dv: getEdgeWriteVariables(edge)) {
            vds.add(vars.get(dv));
        }
        return vds;
    }

    /**
     * Get the written variables of an edge.
     *
     * @param edge Edge to inspect.
     * @return Set of variables being written in the edge.
     */
    private static Set<DiscVariable> getEdgeWriteVariables(Edge edge) {
        Set<DiscVariable> dvs = set();

        for (Update upd: edge.getUpdates()) {
            Assignment asg = (Assignment)upd;
            dvs.addAll(findExpressionVars(asg.getAddressable()));
        }
        return dvs;
    }

    /**
     * Find discrete variables in an expression.
     *
     * <p>
     * Copy of {@link Cif2Mcrl2PreChecker#checkExpression}, but without checking on acceptable constructs.
     * </p>
     *
     * @param e Expression to inspect.
     * @return Used discrete variables in the expression.
     */
    private static Set<DiscVariable> findExpressionVars(Expression e) {
        CifType t = CifTypeUtils.normalizeType(e.getType());

        if (t instanceof BoolType) {
            if (e instanceof BoolExpression) {
                return set();
            } else if (e instanceof BinaryExpression) {
                BinaryExpression be = (BinaryExpression)e;
                Set<DiscVariable> dvs = set();
                dvs.addAll(findExpressionVars(be.getLeft()));
                dvs.addAll(findExpressionVars(be.getRight()));
                return dvs;
            } else if (e instanceof UnaryExpression) {
                UnaryExpression ue = (UnaryExpression)e;
                return findExpressionVars(ue.getChild());
            }
            DiscVariableExpression dve = (DiscVariableExpression)e;
            return set(dve.getVariable());
        }

        if (t instanceof IntType) {
            if (e instanceof IntExpression) {
                return set();
            } else if (e instanceof BinaryExpression) {
                BinaryExpression be = (BinaryExpression)e;
                Set<DiscVariable> dvs = set();
                dvs.addAll(findExpressionVars(be.getLeft()));
                dvs.addAll(findExpressionVars(be.getRight()));
                return dvs;
            } else if (e instanceof UnaryExpression) {
                UnaryExpression ue = (UnaryExpression)e;
                return findExpressionVars(ue.getChild());
            }
            DiscVariableExpression dve = (DiscVariableExpression)e;
            return set(dve.getVariable());
        }

        if (t instanceof EnumType) {
            if (e instanceof EnumLiteralExpression) {
                return set();
            }
            DiscVariableExpression dve = (DiscVariableExpression)e;
            return set(dve.getVariable());
        }

        throw new RuntimeException("Unexpected type: " + t);
    }

    /**
     * Get the events belonging to an edge.
     *
     * <p>
     * Copy of part of {@link Cif2Mcrl2PreChecker#checkAutomaton}, but without checking on allowed constructs.
     * </p>
     *
     * @param edge Edge to inspect.
     * @return The events of the edge.
     */
    public Set<Event> getEvents(Edge edge) {
        Set<Event> events = set();
        for (EdgeEvent ee: edge.getEvents()) {
            EventExpression e = (EventExpression)ee.getEvent();
            Event evt = e.getEvent();
            Assert.notNull(evt);
            events.add(evt);
        }
        return events;
    }

    /**
     * Get the alphabet of the automaton.
     *
     * @return Alphabet of the automaton.
     */
    public Set<Event> getAlphabet() {
        return CifEventUtils.getAlphabet(aut);
    }
}
