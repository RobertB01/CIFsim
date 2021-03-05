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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.CONT_SUB_STATE_FIELD_NAME;
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.LOC_POINTER_TYPE;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Pair.pair;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Pair;

/** State and sub-state code generator. */
public class StateCodeGenerator {
    /** Constructor for the {@link StateCodeGenerator} class. */
    private StateCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the runtime state of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeState(Specification spec, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("State");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Runtime state. */");
        h.add("public final class State extends RuntimeState {");

        // Add body.
        CodeBox c = file.body;

        // Get state variables.
        List<Declaration> vars = ctxt.getStateVars();

        // Add 'STATE_VAR_NAMES' field.
        c.add("private static String[] STATE_VAR_NAMES = {");
        c.indent();
        for (Declaration var: vars) {
            c.add("\"%s\",", getAbsName(var));
        }
        c.dedent();
        c.add("};");

        // Add sub-state fields.
        c.add();
        for (Automaton aut: ctxt.getAutomata()) {
            c.add("public State%s %s;", ctxt.getAutClassName(aut), ctxt.getAutSubStateFieldName(aut));
        }
        c.add("public StateCont %s;", CONT_SUB_STATE_FIELD_NAME);

        // Add constructor.
        c.add();
        c.add("// Private constructor, to force the use of create/copy.");
        c.add("private State(RuntimeSpec<?> spec) {");
        c.indent();
        c.add("super(spec);");
        c.dedent();
        c.add("}");

        // Add 'create' method.
        c.add();
        c.add("public static State create(RuntimeSpec<?> spec) {");
        c.indent();
        c.add("State rslt = new State(spec);");
        for (Automaton aut: ctxt.getAutomata()) {
            c.add("rslt.%s = new State%s();", ctxt.getAutSubStateFieldName(aut), ctxt.getAutClassName(aut));
        }
        c.add("rslt.%s = new StateCont();", CONT_SUB_STATE_FIELD_NAME);
        c.add("new StateInit().initState(spec, rslt);");
        c.add("return rslt;");
        c.dedent();
        c.add("}");

        // Add 'copy' method.
        c.add();
        c.add("public static State copy(State state) {");
        c.indent();
        c.add("State rslt = new State(state.spec);");
        for (Automaton aut: ctxt.getAutomata()) {
            c.add("rslt.%s = state.%s;", ctxt.getAutSubStateFieldName(aut), ctxt.getAutSubStateFieldName(aut));
        }
        c.add("rslt.%s = state.%s;", CONT_SUB_STATE_FIELD_NAME, CONT_SUB_STATE_FIELD_NAME);
        c.add("return rslt;");
        c.dedent();
        c.add("}");

        // Add 'getTime' method.
        c.add();
        c.add("@Override");
        c.add("public double getTime() {");
        c.indent();
        c.add("return %s.time;", CONT_SUB_STATE_FIELD_NAME);
        c.dedent();
        c.add("}");

        // Add 'getStateVarNames' method.
        c.add();
        c.add("@Override");
        c.add("public String[] getStateVarNames() {");
        c.indent();
        c.add("return STATE_VAR_NAMES;");
        c.dedent();
        c.add("}");

        // Add 'getStateVarValue' method.
        c.add();
        c.add("@Override");
        c.add("public Object getStateVarValue(int idx) {");
        c.indent();

        c.add("switch (idx) {");
        c.indent();

        for (int i = 0; i < vars.size(); i++) {
            Declaration var = vars.get(i);
            EObject parent = var.eContainer();
            if (parent instanceof Automaton) {
                String varField;
                if (var instanceof DiscVariable) {
                    varField = ctxt.getDiscVarFieldName((DiscVariable)var);
                } else {
                    varField = ctxt.getContVarFieldName((ContVariable)var);
                }

                Automaton aut = (Automaton)parent;
                c.add("case %d: return %s.%s;", i, ctxt.getAutSubStateFieldName(aut), varField);
            } else {
                c.add("case %d: return %s.%s;", i, CONT_SUB_STATE_FIELD_NAME,
                        ctxt.getContVarFieldName((ContVariable)var));
            }
        }
        c.add("default: throw new RuntimeException(\"Unknown idx: \" + idx);");

        c.dedent();
        c.add("}");

        c.dedent();
        c.add("}");

        // Add 'getStateVarDerValue' method.
        c.add();
        c.add("@Override");
        c.add("public double getStateVarDerValue(int idx) {");
        c.indent();

        c.add("switch (idx) {");
        c.indent();

        for (int i = 0; i < vars.size(); i++) {
            Declaration var = vars.get(i);
            if (var instanceof ContVariable) {
                ContVariable cvar = (ContVariable)var;
                c.add("case %d: return Derivatives.%s(this);", i, ctxt.getDerivativeMethodName(cvar));
            }
        }
        c.add("default: throw new RuntimeException(\"Unknown idx or not a continuous variable: \" + idx);");

        c.dedent();
        c.add("}");

        c.dedent();
        c.add("}");

        // Add 'getAlgVarNames' method.
        c.add();
        c.add("@Override");
        c.add("public String[] getAlgVarNames() {");
        c.indent();
        c.add("return AlgVars.ALG_VAR_NAMES;");
        c.dedent();
        c.add("}");

        // Add 'getAlgVarValue' method.
        c.add();
        c.add("@Override");
        c.add("public Object getAlgVarValue(int idx) {");
        c.indent();

        c.add("switch (idx) {");
        c.indent();

        List<AlgVariable> algVars = ctxt.getAlgVars();
        for (int i = 0; i < algVars.size(); i++) {
            AlgVariable var = algVars.get(i);
            c.add("case %d: return AlgVars.%s(this);", i, ctxt.getAlgVarMethodName(var));
        }
        c.add("default: throw new RuntimeException(\"Unknown idx: \" + idx);");

        c.dedent();
        c.add("}");

        c.dedent();
        c.add("}");

        // Add 'getAutCurLocName' method.
        c.add();
        c.add("@Override");
        c.add("public String getAutCurLocName(int idx) {");
        c.indent();

        List<Automaton> automata = ctxt.getAutomata();
        if (!automata.isEmpty()) {
            c.add("int locIdx;");
            c.add("switch (idx) {");
            c.indent();

            for (int i = 0; i < automata.size(); i++) {
                Automaton aut = automata.get(i);
                c.add("case %d: locIdx = %s.%s; break;", i, ctxt.getAutSubStateFieldName(aut),
                        ctxt.getLocationPointerFieldName(aut));
            }

            c.add("default:");
            c.indent();
        }

        c.add("throw new RuntimeException(\"Invalid aut idx: \" + idx);");

        if (!automata.isEmpty()) {
            c.dedent();

            c.dedent();
            c.add("}");

            c.add("return spec.automata.get(idx).getLocName(locIdx);");
        }

        c.dedent();
        c.add("}");

        // Add 'checkInitialization' method.
        c.add();
        c.add("@Override");
        c.add("public boolean checkInitialization() {");
        c.indent();
        c.add("if (!SPEC.evalInitPreds(this)) return false;");
        c.add("if (!SPEC.evalStateInvPreds(this, true)) return false;");
        c.add("return true;");
        c.dedent();
        c.add("}");

        // Add 'calcTransitions' method.
        c.add();
        c.add("@Override");
        c.add("public void calcTransitions(Double endTime, Double maxDelay) {");
        c.indent();
        c.add("SPEC.calcTransitions(this, endTime, maxDelay);");
        c.dedent();
        c.add("}");

        // Add 'calcTimeTransition' method.
        c.add();
        c.add("@Override");
        c.add("public void calcTimeTransition(Double endTime) {");
        c.indent();
        c.add("SPEC.calcTimeTransition(this, endTime, null);");
        c.dedent();
        c.add("}");

        // Add 'chooseTransition' method.
        c.add();
        c.add("@Override");
        c.add("public Transition<?> chooseTransition(RuntimeState state, List<Transition<?>> transitions, "
                + "SimulationResult result) {");
        c.indent();
        c.add("@SuppressWarnings({\"rawtypes\", \"unchecked\"})");
        c.add("List<Transition<State>> transitions2 = (List)transitions;");
        c.add("return SPEC.input.chooseTransition((State)state, transitions2, result);");
        c.dedent();
        c.add("}");

        // Add 'chooseTargetTime' method.
        c.add();
        c.add("@Override");
        c.add("public ChosenTargetTime chooseTargetTime(double maxTargetTime) {");
        c.indent();
        c.add("return SPEC.input.chooseTargetTime(this, maxTargetTime);");
        c.dedent();
        c.add("}");

        // Add 'getNextMaxEndTime' method.
        c.add();
        c.add("@Override");
        c.add("public Double getNextMaxEndTime() {");
        c.indent();
        c.add("return SPEC.input.getNextMaxEndTime(this);");
        c.dedent();
        c.add("}");
    }

    /**
     * Generate Java code for the runtime sub-states of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeSubStates(Specification spec, CifCompilerContext ctxt) {
        for (Automaton aut: ctxt.getAutomata()) {
            gencodeSubState(aut, ctxt);
        }
        gencodeSubStateCont(spec, ctxt);
    }

    /**
     * Generate Java code for the runtime sub-state of an automaton.
     *
     * @param aut The automaton.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeSubState(Automaton aut, CifCompilerContext ctxt) {
        // Add new code file.
        String className = "State" + ctxt.getAutClassName(aut);
        JavaCodeFile file = ctxt.addCodeFile(className);

        // Add header.
        String absName = getAbsName(aut);
        CodeBox h = file.header;
        h.add("/** Runtime sub-state for automaton \"%s\". */", absName);
        h.add("public final class %s {", className);

        // Add body.
        CodeBox c = file.body;

        // Add field for the location pointer variable.
        c.add("public %s %s;", LOC_POINTER_TYPE, ctxt.getLocationPointerFieldName(aut));

        // Add fields for discrete and continuous variables.
        for (Declaration decl: aut.getDeclarations()) {
            if (decl instanceof DiscVariable) {
                DiscVariable var = (DiscVariable)decl;
                c.add("public %s %s;", gencodeType(var.getType(), ctxt), ctxt.getDiscVarFieldName(var));
            } else if (decl instanceof ContVariable) {
                ContVariable var = (ContVariable)decl;
                c.add("public double %s;", ctxt.getContVarFieldName(var));
            }
        }

        // Add 'copy' method.
        c.add();
        c.add("public %s copy() {", className);
        c.indent();
        c.add("%s rslt = new %s();", className, className);

        c.add("rslt.%s = %s;", ctxt.getLocationPointerFieldName(aut), ctxt.getLocationPointerFieldName(aut));

        for (Declaration decl: aut.getDeclarations()) {
            if (decl instanceof DiscVariable) {
                DiscVariable var = (DiscVariable)decl;
                c.add("rslt.%s = %s;", ctxt.getDiscVarFieldName(var), ctxt.getDiscVarFieldName(var));
            } else if (decl instanceof ContVariable) {
                ContVariable var = (ContVariable)decl;
                c.add("rslt.%s = %s;", ctxt.getContVarFieldName(var), ctxt.getContVarFieldName(var));
            }
        }

        c.add("return rslt;");
        c.dedent();
        c.add("}");
    }

    /**
     * Generate Java code for the runtime sub-state for the continuous variables declared outside of the automata
     * (including variable 'time').
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    private static void gencodeSubStateCont(Specification spec, CifCompilerContext ctxt) {
        // Collect continuous variables declared outside automata.
        List<ContVariable> variables = list();
        collectContVarsNotInAut(spec, variables);

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("StateCont");

        // Add header.
        CodeBox h = file.header;
        h.add("/**");
        h.add(" * Runtime sub-state for continuous variables declared outside the automata,");
        h.add(" * and variable 'time'.");
        h.add(" */");
        h.add("public final class StateCont {");

        // Add body.
        CodeBox c = file.body;

        // Add field for 'time'.
        c.add("public double time;");

        // Add fields for the continuous variables.
        for (ContVariable var: variables) {
            c.add("public double %s;", ctxt.getContVarFieldName(var));
        }

        // Add 'copy' method.
        c.add();
        c.add("public StateCont copy() {");
        c.indent();
        c.add("StateCont rslt = new StateCont();");

        c.add("rslt.time = time;");

        for (ContVariable var: variables) {
            c.add("rslt.%s = %s;", ctxt.getContVarFieldName(var), ctxt.getContVarFieldName(var));
        }

        c.add("return rslt;");
        c.dedent();
        c.add("}");
    }

    /**
     * Collect the continuous variables declared in the given component (recursively), which are declared outside of
     * automata.
     *
     * @param comp The component.
     * @param vars The continuous variables collected so far. Is modified in-place.
     */
    private static void collectContVarsNotInAut(ComplexComponent comp, List<ContVariable> vars) {
        // Don't collect in automata.
        if (comp instanceof Automaton) {
            return;
        }

        // Collect locally in groups (including the specification).
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof ContVariable) {
                vars.add((ContVariable)decl);
            }
        }

        // Collect recursively in the children of the group.
        for (Component child: ((Group)comp).getComponents()) {
            collectContVarsNotInAut((ComplexComponent)child, vars);
        }
    }

    /**
     * Collect the (discrete and continuous) variables that are part of the state, and are declared in the given
     * component (recursively).
     *
     * @param comp The component.
     * @param vars The variables collected so far, as pairs of absolute names of the variables and the variables
     *     themselves. Is modified in-place.
     */
    public static void collectStateVars(ComplexComponent comp, List<Pair<String, Declaration>> vars) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof DiscVariable || decl instanceof ContVariable) {
                vars.add(pair(getAbsName(decl, false), decl));
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectStateVars((ComplexComponent)child, vars);
            }
        }
    }
}
