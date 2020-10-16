//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Lists.concat;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Specification code generator. */
public class SpecCodeGenerator {
    /** Constructor for the {@link SpecCodeGenerator} class. */
    private SpecCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the runtime specification.
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeSpec(CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("Spec");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Runtime specification. */");
        h.add("public final class Spec extends RuntimeSpec<State> {");

        // Add body.
        CodeBox c = file.body;

        // Add singleton fields.
        c.add("public static final Spec SPEC = new Spec();");
        c.add("public static final RuntimeMonitorEdge<State> MONITOR_EDGE = new RuntimeMonitorEdge<>();");

        // Add event fields (also for 'tau').
        c.add();
        for (Event event: ctxt.getEvents()) {
            c.add("public static final %s %s = new %s();", ctxt.getEventClassName(event), ctxt.getEventFieldName(event),
                    ctxt.getEventClassName(event));
        }

        // Add 'initAutomata' method.
        c.add();
        c.add("@Override");
        c.add("protected void initAutomata() {");
        c.indent();
        List<Automaton> automata = ctxt.getAutomata();
        for (Automaton aut: automata) {
            c.add("automata.add(new %s());", ctxt.getAutClassName(aut));
        }
        c.dedent();
        c.add("}");

        // Add 'initStateObjectsMeta' method.
        List<Declaration> stateVars = ctxt.getStateVars();
        List<AlgVariable> algVars = ctxt.getAlgVars();
        List<PositionObject> stateObjs = concat(automata, stateVars, algVars);

        int subMethodCount = (int)Math.ceil(stateObjs.size() / 1000d);
        int automataOffset = 0;
        int stateVarsOffset = automataOffset + automata.size();
        int algVarsOffset = stateVarsOffset + stateVars.size();

        c.add();
        c.add("@Override");
        c.add("protected void initStateObjectsMeta() {");
        c.indent();
        for (int i = 0; i < subMethodCount; i++) {
            c.add("initStateObjectsMeta%d();", i);
        }
        c.dedent();
        c.add("}");

        c.add();
        c.add("private void initStateObjectsMeta0() {");
        c.indent();
        for (int i = 0; i < stateObjs.size(); i++) {
            // New sub method.
            if ((i > 0) && (i % 1000 == 0)) {
                c.dedent();
                c.add("}");

                c.add();
                c.add("private void initStateObjectsMeta%d() {", i / 1000);
                c.indent();
            }

            // New object.
            PositionObject stateObj = stateObjs.get(i);
            if (stateObj instanceof Automaton) {
                Automaton aut = (Automaton)stateObj;
                c.add("stateObjectsMeta.add(new RuntimeStateObjectMeta(%d, StateObjectType.AUTOMATON, \"%s\"));",
                        i - automataOffset, getAbsName(aut));
            } else if (stateObj instanceof DiscVariable) {
                DiscVariable var = (DiscVariable)stateObj;
                c.add("stateObjectsMeta.add(new RuntimeStateObjectMeta(%d, StateObjectType.DISCRETE, \"%s\"));",
                        i - stateVarsOffset, getAbsName(var));
            } else if (stateObj instanceof ContVariable) {
                ContVariable var = (ContVariable)stateObj;
                String absVarName = getAbsName(var);
                c.add("stateObjectsMeta.add(new RuntimeStateObjectMeta(%d, StateObjectType.CONTINUOUS, \"%s\"));",
                        i - stateVarsOffset, absVarName);
                c.add("stateObjectsMeta.add(new RuntimeStateObjectMeta(%d, StateObjectType.DERIVATIVE, \"%s\"));",
                        i - stateVarsOffset, absVarName + "'");
            } else if (stateObj instanceof AlgVariable) {
                AlgVariable var = (AlgVariable)stateObj;
                c.add("stateObjectsMeta.add(new RuntimeStateObjectMeta(%d, StateObjectType.ALGEBRAIC, \"%s\"));",
                        i - algVarsOffset, getAbsName(var));
            } else {
                throw new RuntimeException("Unknown state object: " + stateObj);
            }
        }
        c.dedent();
        c.add("}");

        // Add 'initEvents' method (for all events, including 'tau').
        List<Event> events = ctxt.getEvents();
        subMethodCount = (int)Math.ceil(events.size() / 1000d);

        c.add();
        c.add("@Override");
        c.add("protected void initEvents() {");
        c.indent();
        for (int i = 0; i < subMethodCount; i++) {
            c.add("initEvents%d();", i);
        }
        c.dedent();
        c.add("}");

        c.add();
        c.add("private void initEvents0() {");
        c.indent();
        for (int i = 0; i < events.size(); i++) {
            // New sub method.
            if ((i > 0) && (i % 1000 == 0)) {
                c.dedent();
                c.add("}");

                c.add();
                c.add("private void initEvents%d() {", i / 1000);
                c.indent();
            }

            // Next event.
            Event event = events.get(i);
            c.add("events.add(%s);", ctxt.getEventFieldName(event));
        }
        c.dedent();
        c.add("}");

        // Add 'initEventData' method (for all events, excluding 'tau').
        subMethodCount = (int)Math.ceil(events.size() / 100d);

        c.add();
        c.add("@Override");
        c.add("protected void initEventData() {");
        c.indent();
        for (int i = 0; i < subMethodCount; i++) {
            c.add("initEventData%d();", i);
        }
        c.dedent();
        c.add("}");

        c.add();
        c.add("private void initEventData0() {");
        c.indent();
        c.add("List<List<RuntimeEdge<State>>> outer;");
        c.add("List<RuntimeEdge<State>> inner;");
        for (int i = 0; i < events.size(); i++) {
            // New sub method.
            if ((i > 0) && (i % 100 == 0)) {
                c.dedent();
                c.add("}");

                c.add();
                c.add("private void initEventData%d() {", i / 100);
                c.indent();
                c.add("List<List<RuntimeEdge<State>>> outer;");
                c.add("List<RuntimeEdge<State>> inner;");
            }

            // Next event. Skip 'tau' event.
            Event event = events.get(i);
            if (event == ctxt.tauEvent) {
                continue;
            }

            c.add();
            c.add("// Event \"%s\".", getAbsName(event));

            // Sync data.
            int syncAutCount = ctxt.getSyncAuts(event).size();
            c.add("outer = listc(%d);", syncAutCount);
            c.add("for (int i = 0; i < %d; i++) {", syncAutCount);
            c.indent();
            c.add("inner = listc(1);");
            c.add("outer.add(inner);");
            c.dedent();
            c.add("}");
            c.add("syncData.add(outer);");

            // Send/receive data.
            c.add();
            if (event.getType() == null) {
                c.add("sendData.add(null);");
                c.add("recvData.add(null);");
            } else {
                c.add("inner = listc(1);");
                c.add("sendData.add(inner);");
                c.add("inner = listc(1);");
                c.add("recvData.add(inner);");
            }
        }
        c.dedent();
        c.add("}");

        // Add 'createInitialState' method.
        c.add();
        c.add("@Override");
        c.add("public State createInitialState(RuntimeSpec<?> spec) {");
        c.indent();
        c.add("return State.create(spec);");
        c.dedent();
        c.add("}");

        // Add 'copyState' method.
        c.add();
        c.add("@Override");
        c.add("protected State copyState(State state) {");
        c.indent();
        c.add("return State.copy(state);");
        c.dedent();
        c.add("}");

        // Add 'hasTauEdge' method.
        c.add();
        c.add("@Override");
        c.add("public boolean hasTauEdge() {");
        c.indent();
        c.add("return %s;", ctxt.hasTauEdge);
        c.dedent();
        c.add("}");

        // Add 'evalInitPreds' method.
        c.add();
        c.add("@Override");
        c.add("protected boolean evalInitPreds(State state) {");
        c.indent();
        c.add("return InitPreds.evalInitPreds(state);");
        c.dedent();
        c.add("}");

        // Add 'evalStateInvPreds' method.
        c.add();
        c.add("@Override");
        c.add("protected boolean evalStateInvPreds(State state, boolean initial) {");
        c.indent();
        c.add("return StateInvPreds.evalStateInvPreds(state, initial);");
        c.dedent();
        c.add("}");

        // Add 'evalUrgLocs' method.
        c.add();
        c.add("@Override");
        c.add("protected boolean evalUrgLocs(State state) {");
        c.indent();
        c.add("return UrgLocs.evalUrgLocs(state);");
        c.dedent();
        c.add("}");

        // Add 'evalUrgEdges' method.
        c.add();
        c.add("@Override");
        c.add("protected boolean evalUrgEdges(State state) {");
        c.indent();
        c.add("return UrgEdges.evalUrgEdges(state);");
        c.dedent();
        c.add("}");

        // Add 'getOdeStateEvents' method.
        c.add();
        c.add("@Override");
        c.add("protected List<OdeStateEvent<State>> getOdeStateEvents(State state) {");
        c.indent();
        c.add("return OdeStateEvents.getOdeStateEvents(state);");
        c.dedent();
        c.add("}");

        // Add 'getOdeSolver' method.
        c.add();
        c.add("@Override");
        c.add("public OdeSolver<State> getOdeSolver() {");
        c.indent();
        c.add("return Solver.getSolver();");
        c.dedent();
        c.add("}");

        // Add 'getCifSvgDecls' method. Only called once, so we can freely
        // create new instances here.
        c.add();
        c.add("@Override");
        c.add("public List<RuntimeCifSvgDecls> getCifSvgDecls() {");
        c.indent();
        c.add("List<RuntimeCifSvgDecls> rslt = listc(%d);", ctxt.svgFileCount);
        Assert.check(ctxt.svgFileCount != -1);
        for (int i = 0; i < ctxt.svgFileCount; i++) {
            c.add("rslt.add(new CifSvg%d());", i);
        }
        c.add("return rslt;");
        c.dedent();
        c.add("}");

        // Add 'getPrintDecls' method. Only called once, so we can freely
        // create new instances here.
        c.add();
        c.add("@Override");
        c.add("public List<RuntimePrintDecls> getPrintDecls() {");
        c.indent();
        c.add("List<RuntimePrintDecls> rslt = listc(%d);", ctxt.printFileCount);
        Assert.check(ctxt.printFileCount != -1);
        for (int i = 0; i < ctxt.printFileCount; i++) {
            c.add("rslt.add(new CifPrint%d());", i);
        }
        c.add("return rslt;");
        c.dedent();
        c.add("}");
    }
}
