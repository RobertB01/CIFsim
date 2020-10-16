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
import static org.eclipse.escet.cif.simulator.compiler.CifCompilerContext.CONT_SUB_STATE_FIELD_NAME;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.PairTextComparer;

/** ODE solver code generator. */
public class SolverCodeGenerator {
    /** Constructor for the {@link SolverCodeGenerator} class. */
    private SolverCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the ODE solver for the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeSolver(Specification spec, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("Solver");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Runtime ODE solver. */");
        h.add("public final class Solver extends OdeSolver<State> {");

        // Add body.
        CodeBox c = file.body;

        // Get the continuous variables of the specification.
        List<ContVariable> vars = list();
        DerivativeCodeGenerator.collectContVars(spec, vars);

        // Sort continuous variables on absolute names. This leads to more
        // readable debug output.
        List<Pair<String, ContVariable>> contNamesVars = list();
        for (ContVariable var: vars) {
            contNamesVars.add(pair(getAbsName(var, false), var));
        }
        Collections.sort(contNamesVars, new PairTextComparer<ContVariable>());
        for (int i = 0; i < contNamesVars.size(); i++) {
            vars.set(i, contNamesVars.get(i).right);
        }

        // Use dummy variable if needed.
        int count = vars.size();
        boolean useDummyVar = (count == 0);
        if (useDummyVar) {
            count = 1;
        }

        // Add 'SOLVER' field.
        c.add("private static Solver SOLVER;");

        // Add 'CONT_VAR_NAMES' field.
        c.add();
        c.add("public static String[] CONT_VAR_NAMES = {");
        c.indent();
        for (ContVariable var: vars) {
            c.add("\"%s\",", getAbsName(var));
        }
        if (useDummyVar) {
            c.add("\"<dummy>\",");
        }
        c.dedent();
        c.add("};");

        // Add constructor.
        c.add();
        c.add("public Solver() {");
        c.indent();
        c.add("super(%s);", str(useDummyVar));
        c.dedent();
        c.add("}");

        // Add 'getSolver' method. Static initialization doesn't work, due to
        // initialization order.
        c.add();
        c.add("public static Solver getSolver() {");
        c.indent();
        c.add("if (SOLVER == null) SOLVER = new Solver();");
        c.add("return SOLVER;");
        c.dedent();
        c.add("}");

        // Add 'initY' method.
        c.add();
        c.add("@Override");
        c.add("protected double[] initY(State state) {");
        c.indent();

        c.add("double[] y = new double[%d];", count);

        if (useDummyVar) {
            c.add("y[0] = 0.0; // dummy variable");
        } else {
            for (int i = 0; i < count; i++) {
                ContVariable var = vars.get(i);
                c.add("y[%d] = state.%s.%s;", i, ctxt.getContVarSubStateName(var), ctxt.getContVarFieldName(var));
            }
        }

        c.add("return y;");

        c.dedent();
        c.add("}");

        // Add 'checkValues' method.
        c.add();
        c.add("@Override");
        c.add("public void checkValues(double time, double[] values) {");
        c.indent();

        c.add("if (!isValidValue(time)) throwValueError(time, \"time\");");
        if (!useDummyVar) {
            for (int i = 0; i < count; i++) {
                ContVariable var = vars.get(i);
                c.add("if (!isValidValue(values[%d])) throwValueError(values[%d], \"%s\");", i, i, getAbsName(var));
            }
        }

        c.dedent();
        c.add("}");

        // Add 'getDimension' method.
        c.add();
        c.add("@Override");
        c.add("public int getDimension() {");
        c.indent();
        c.add("return %d;", count);
        c.dedent();
        c.add("}");

        // Add 'getContVarName' method.
        c.add();
        c.add("@Override");
        c.add("protected String getContVarName(int idx) {");
        c.indent();
        c.add("return CONT_VAR_NAMES[idx];");
        c.dedent();
        c.add("}");

        // Add 'makeState' method.
        c.add();
        c.add("@Override");
        c.add("public State makeState(State state, double t, double[] y, boolean copy) {");
        c.indent();

        c.add("State rslt = copy ? State.copy(state) : state;");
        c.add("if (copy) rslt.%s = rslt.%s.copy();", CONT_SUB_STATE_FIELD_NAME, CONT_SUB_STATE_FIELD_NAME);
        c.add("rslt.%s.time = t;", CONT_SUB_STATE_FIELD_NAME);

        if (!useDummyVar) {
            Set<Automaton> automata = set();
            for (int i = 0; i < count; i++) {
                // Get variable.
                ContVariable var = vars.get(i);

                // Copy sub-state, if not yet done.
                EObject parent = var.eContainer();
                if (parent instanceof Automaton) {
                    if (!automata.contains(parent)) {
                        Automaton aut = (Automaton)parent;
                        automata.add(aut);
                        c.add("if (copy) rslt.%s = rslt.%s.copy();", ctxt.getAutSubStateFieldName(aut),
                                ctxt.getAutSubStateFieldName(aut));
                    }
                }

                // Set value of continuous variable.
                c.add("rslt.%s.%s = (y[%d] == -0.0) ? 0.0 : y[%d];", ctxt.getContVarSubStateName(var),
                        ctxt.getContVarFieldName(var), i, i);
            }
        }

        c.add("return rslt;");
        c.dedent();
        c.add("}");

        // Add 'computeDerivatives' method.
        c.add();
        c.add("@Override");
        c.add("protected void computeDerivatives(State state, double[] yDot) {");
        c.indent();

        if (useDummyVar) {
            c.add("yDot[0] = 1.0; // dummy variable");
        } else {
            for (int i = 0; i < count; i++) {
                ContVariable var = vars.get(i);
                c.add("yDot[%d] = %s(state);", i, ctxt.getDerivativeMethodName(var));
            }
        }

        c.dedent();
        c.add("}");
    }
}
