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

package org.eclipse.escet.cif.cif2mcrl2.tree;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.cif2mcrl2.NameMaps;
import org.eclipse.escet.cif.cif2mcrl2.storage.AutomatonData;
import org.eclipse.escet.cif.cif2mcrl2.storage.EventVarUsage;
import org.eclipse.escet.cif.cif2mcrl2.storage.VariableData;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.HBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;

/** Automaton process leaf node of the instance tree. */
public class AutomatonProcessNode extends ProcessNode {
    /** Automaton represented by this node. */
    public final AutomatonData autData;

    /**
     * Constructor of the {@link AutomatonProcessNode} class.
     *
     * @param name Name of the node.
     * @param autData Automaton represented by this node.
     */
    public AutomatonProcessNode(String name, AutomatonData autData) {
        super(name);
        this.autData = autData;
    }

    @Override
    protected String getCifName() {
        return "automaton " + autData.name;
    }

    @Override
    public void deriveActions(Set<VariableData> localVars) {
        valueVars = set();
        for (VariableData vd: autData.vars.values()) {
            if (localVars.contains(vd)) {
                valueVars.add(vd);
            }
        }

        availProcessVars = set();
        eventVarUse = map();

        // Traverse through all edges, extract variable use, and merge it in
        // the 'eventVarUse' map.
        for (Location loc: autData.aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                Set<VariableData> readVars, writeVars;
                readVars = autData.getReadVariables(edge);
                writeVars = autData.getWriteVariables(edge);
                for (Event evt: autData.getEvents(edge)) {
                    EventVarUsage evu = eventVarUse.get(evt);
                    boolean firstEdge = false; // First edge with this event?
                    if (evu == null) {
                        evu = new EventVarUsage(evt);
                        eventVarUse.put(evt, evu);
                        firstEdge = true;
                    }
                    evu.addReadEdgeAccess(readVars, firstEdge, localVars);
                    evu.addWriteEdgeAccess(writeVars, firstEdge, localVars);
                }
            }
        }

        // Check that all events of the alphabet have been added. Extend the
        // 'eventVarUse' map, if necessary.
        for (Event evt: autData.getAlphabet()) {
            if (!eventVarUse.containsKey(evt)) {
                EventVarUsage evu = new EventVarUsage(evt);
                eventVarUse.put(evt, evu);
            }
        }
    }

    @Override
    public void addDefinitions(NameMaps names, Set<VariableData> localVars, VBox code) {
        String behProcName = names.getBehaviorProcess(autData.aut);
        String locSortName = names.getLocationSortName(autData.aut);
        String locVarName = names.getLocationVariableName(autData.aut);

        // Location sort definition.
        HBox locNamesBox = new HBox(" | ");
        for (Location loc: autData.aut.getLocations()) {
            locNamesBox.add(names.getLocationName(loc, autData.aut));
        }
        code.add(new HBox("sort ", locSortName, " = struct ", locNamesBox, ";"));
        code.add();

        // 'value' actions of the local variables.
        boolean addedAct = false;
        for (VariableData vd: autData.vars.values()) {
            if (localVars.contains(vd)) {
                if (!vd.getHasValueAction()) {
                    continue;
                }
                String varVal = names.getVariableValue(vd.variable);
                String typeName = names.getTypeName(vd.getType());
                code.add("act " + varVal + " : " + typeName + ";");
                addedAct = true;
            }
        }
        if (addedAct) {
            code.add();
        }

        // Behavior process definition
        // a. Process header.
        String selfloopParams = locVarName;
        String params = locVarName + " : " + locSortName;
        for (VariableData vd: autData.vars.values()) {
            if (localVars.contains(vd)) {
                String varName = names.getVariableName(vd.variable);
                String typeName = names.getTypeName(vd.getType());
                params += ", " + varName + " : " + typeName;
                selfloopParams += ", " + varName;
            }
        }
        code.add(fmt("proc %s(%s) =", behProcName, params));

        // b. 'value' action for every local variable.
        for (VariableData vd: autData.vars.values()) {
            if (localVars.contains(vd)) {
                if (!vd.getHasValueAction()) {
                    continue;
                }
                String varVal = names.getVariableValue(vd.variable);
                String varName = names.getVariableName(vd.variable);
                code.add(fmt("  %s(%s) . %s(%s) +", varVal, varName, behProcName, selfloopParams));
            }
        }

        // c. Count edges.
        int edgeCount = 0;
        for (Location loc: autData.aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                edgeCount += edge.getEvents().size();
            }
        }

        // d. Generate edge code.
        int idx = 0;
        int last = edgeCount - 1;
        for (Location loc: autData.aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                for (EdgeEvent ee: edge.getEvents()) {
                    Box b = makeEdge(names, localVars, loc, edge, ee);
                    b = new HBox("  ", b, (idx == last) ? ";" : " +");
                    code.add(b);
                    idx++;
                }
            }
        }
        code.add(); // Add empty line as separation for the next definition.
    }

    /**
     * Construct an edge of the behavior process.
     *
     * @param names Mapping of CIF elements to mCRL2 names.
     * @param localVars Local variables of the system.
     * @param loc Source location.
     * @param edge Edge to convert.
     * @param event Event to convert.
     * @return The created edge.
     */
    private Box makeEdge(NameMaps names, Set<VariableData> localVars, Location loc, Edge edge, EdgeEvent event) {
        Set<VariableData> readVars = autData.getReadVariables(edge);

        List<String> sums = list();
        List<String> conds = list();
        List<String> actions = list();

        // Generate sums over external variables.
        for (VariableData vd: readVars) {
            if (localVars.contains(vd)) {
                continue;
            }
            String varName = names.getVariableName(vd.variable);
            String varType = names.getTypeName(vd.variable.getType());
            sums.add(fmt("sum %s : %s . ", varName, varType));

            // Add upper and lower boundaries of the external variable, if known.
            Integer limit = vd.getLowerLimit();
            if (limit != null) {
                conds.add(fmt("(%s >= %d)", varName, limit));
            }
            limit = vd.getUpperLimit();
            if (limit != null) {
                conds.add(fmt("(%s <= %d)", varName, limit));
            }
        }
        // External variables that are written but not read don't need sums.

        // Add "location check and guards -> "
        String locVar = names.getLocationVariableName(autData.aut);
        String locName = names.getLocationName(loc, autData.aut);
        conds.add(fmt("(%s == %s)", locVar, locName));
        if (!edge.getGuards().isEmpty()) {
            conds.add(makeExpression(names, edge.getGuards()));
        }

        // Add the event.
        EventExpression eex = (EventExpression)event.getEvent();
        actions.add(names.getEventName(eex.getEvent()));

        // Add behavior read actions.
        for (VariableData vd: readVars) {
            if (localVars.contains(vd)) {
                continue;
            }
            String varName = names.getVariableName(vd.variable);
            String behRead = names.getBehRead(vd.variable);
            actions.add(fmt("%s(%s)", behRead, varName));
        }

        // Add write actions, save local var updates.
        Map<VariableData, String> newLocalValues = map();
        for (Update upd: edge.getUpdates()) {
            Assignment asg = (Assignment)upd;
            String asgVal = makeExpression(names, asg.getValue());

            DiscVariableExpression dve = (DiscVariableExpression)asg.getAddressable();
            VariableData wVar = autData.vars.get(dve.getVariable());
            Assert.notNull(wVar); // Note: Duplicate check, already done above.
            if (localVars.contains(wVar)) {
                Integer limit = wVar.getLowerLimit();
                if (limit != null) {
                    conds.add(fmt("(%s >= %d)", asgVal, limit));
                }
                limit = wVar.getUpperLimit();
                if (limit != null) {
                    conds.add(fmt("(%s <= %d)", asgVal, limit));
                }
                newLocalValues.put(wVar, asgVal);
                continue;
            }
            String behWrite = names.getBehWrite(dve.getVariable());
            actions.add(fmt("%s(%s)", behWrite, asgVal));
        }

        // Perform recursive call.
        String behProcName = names.getBehaviorProcess(autData.aut);
        String targetLocName = (edge.getTarget() == null) ? names.getLocationVariableName(autData.aut)
                : names.getLocationName(edge.getTarget(), autData.aut);
        String params = targetLocName;
        for (VariableData vd: autData.vars.values()) {
            if (localVars.contains(vd)) {
                String nVal = newLocalValues.get(vd);
                if (nVal == null) {
                    nVal = names.getVariableName(vd.variable);
                }
                params += ", " + nVal;
            }
        }

        HBox line = new HBox();
        for (String s: sums) {
            line.add(s);
        }
        Assert.check(!conds.isEmpty());
        if (conds.size() == 1) {
            line.add(conds.get(0) + " -> ");
        } else {
            line.add("(");
            boolean first = true;
            for (String c: conds) {
                if (!first) {
                    line.add(" && ");
                }
                line.add(c);
                first = false;
            }
            line.add(") -> ");
        }

        boolean first = true;
        for (String a: actions) {
            if (!first) {
                line.add(" | ");
            }
            first = false;
            line.add(a);
        }
        line.add(fmt(" . %s(%s)", behProcName, params));
        return line;
    }

    /**
     * Construct a string denoting the computation in 'exprs'. Expressions are considered to be a conjunction of
     * booleans.
     *
     * @param names Mapping of CIF elements to mCRL2 names.
     * @param exprs Expressions to convert.
     * @return Text denoting the computation.
     */
    private String makeExpression(NameMaps names, List<Expression> exprs) {
        if (exprs.isEmpty()) {
            return "true";
        }
        String line = "";
        for (Expression e: exprs) {
            if (!line.isEmpty()) {
                line += " && ";
            }
            line += makeExpression(names, e);
        }
        return line;
    }

    /**
     * Construct a string denoting the computation in 'expr'.
     *
     * @param names Mapping of CIF elements to mCRL2 names.
     * @param expr Expression to convert.
     * @return Text denoting the computation.
     */
    private String makeExpression(NameMaps names, Expression expr) {
        if (expr instanceof BoolExpression) {
            BoolExpression be = (BoolExpression)expr;
            return be.isValue() ? "true" : "false";
        } else if (expr instanceof BinaryExpression) {
            BinaryExpression be = (BinaryExpression)expr;
            String left = makeExpression(names, be.getLeft());
            String right = makeExpression(names, be.getRight());
            switch (be.getOperator()) {
                case CONJUNCTION:
                    return "(" + left + " && " + right + ")";
                case DISJUNCTION:
                    return "(" + left + " || " + right + ")";
                case EQUAL:
                    return "(" + left + " == " + right + ")";
                case GREATER_EQUAL:
                    return "(" + left + " >= " + right + ")";
                case GREATER_THAN:
                    return "(" + left + " > " + right + ")";
                case LESS_EQUAL:
                    return "(" + left + " <= " + right + ")";
                case LESS_THAN:
                    return "(" + left + " < " + right + ")";
                case UNEQUAL:
                    return "(" + left + " != " + right + ")";
                case ADDITION:
                    return "(" + left + " + " + right + ")";
                case MULTIPLICATION:
                    return "(" + left + " * " + right + ")";
                case SUBTRACTION:
                    return "(" + left + " - " + right + ")";
                default:
                    Assert.fail(fmt("Unexpected binary operator %s found.", be.getOperator()));
            }
        } else if (expr instanceof UnaryExpression) {
            UnaryExpression ue = (UnaryExpression)expr;
            String child = makeExpression(names, ue.getChild());
            switch (ue.getOperator()) {
                case INVERSE:
                    return "!" + child;
                case NEGATE:
                    return "-" + child;
                case PLUS:
                    return child;
                default:
                    Assert.fail(fmt("Unexpected unary operator %s found.", ue.getOperator()));
            }
        } else if (expr instanceof DiscVariableExpression) {
            DiscVariableExpression de = (DiscVariableExpression)expr;
            return names.getVariableName(de.getVariable());
        } else if (expr instanceof IntExpression) {
            IntExpression ie = (IntExpression)expr;
            return Integer.toString(ie.getValue());
        } else if (expr instanceof EnumLiteralExpression) {
            EnumLiteralExpression ele = (EnumLiteralExpression)expr;
            return names.getEnumLitName(ele.getLiteral());
        }
        throw new RuntimeException("Unexpected expression: " + expr);
    }

    @Override
    public void addInstantiations(NameMaps names, Set<VariableData> localVars, VBox code) {
        String behProcName = names.getBehaviorProcess(autData.aut);

        // X(<locvar + local variables>)
        Location initLoc = autData.initialLocation;
        String params = names.getLocationName(initLoc, autData.aut);
        for (VariableData vd: autData.vars.values()) {
            if (localVars.contains(vd)) {
                params += fmt(", %s", vd.initialValue);
            }
        }

        code.add(fmt("%s(%s)", behProcName, params));
    }
}
