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
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Pair.pair;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;

/** Algebraic variables code generator. */
public class AlgVarCodeGenerator {
    /** Constructor for the {@link AlgVarCodeGenerator} class. */
    private AlgVarCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the algebraic variables of the specification.
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeAlgVars(CifCompilerContext ctxt) {
        // Get algebraic variables.
        List<AlgVariable> variables = ctxt.getAlgVars();

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("AlgVars");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Algebraic variables. */");
        h.add("public final class AlgVars {");

        // Add body.
        CodeBox c = file.body;

        // Add 'ALG_VAR_NAMES' field.
        c.add("public static String[] ALG_VAR_NAMES = {");
        c.indent();
        for (AlgVariable var: variables) {
            c.add("\"%s\",", getAbsName(var));
        }
        c.dedent();
        c.add("};");

        // Add methods for the algebraic variables.
        for (AlgVariable var: variables) {
            // Method header.
            c.add();
            c.add("public static %s %s(State state) {", gencodeType(var.getType(), ctxt),
                    ctxt.getAlgVarMethodName(var));
            c.indent();

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Try to find value in declaration.
            boolean found = false;
            if (var.getValue() != null) {
                c.add("return %s;", gencodeExpr(var.getValue(), ctxt, "state"));
                found = true;
            }

            // Try to find value in equations of the component.
            if (!found) {
                ComplexComponent comp = (ComplexComponent)var.eContainer();
                for (Equation eq: comp.getEquations()) {
                    if (eq.getVariable() == var) {
                        c.add("return %s;", gencodeExpr(eq.getValue(), ctxt, "state"));
                        found = true;
                        break;
                    }
                }
            }

            // Find equations per location.
            if (!found) {
                gencodeAlgVarsPerLoc(var, ctxt, c);
            }

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of algebraic variable \\\"%s\\\" failed.\", e, "
                    + "state);", getAbsName(var));
            c.dedent();
            c.add("}");

            // End of the method.
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Generate Java code from the equations of the locations, for the body of the method of the given algebraic
     * variable.
     *
     * @param var The algebraic variable.
     * @param ctxt The compiler context to use.
     * @param c The code box in which to generate the code.
     */
    private static void gencodeAlgVarsPerLoc(AlgVariable var, CifCompilerContext ctxt, CodeBox c) {
        // Get automaton.
        Automaton aut = (Automaton)var.eContainer();

        // Generate 'switch' on location pointer.
        c.add("switch (state.%s.%s) {", ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));
        c.indent();

        // Generate a 'case' per location.
        List<Location> locs = aut.getLocations();
        for (int locIdx = 0; locIdx < locs.size(); locIdx++) {
            Location loc = locs.get(locIdx);

            boolean found = false;
            for (Equation eq: loc.getEquations()) {
                if (eq.getVariable() == var) {
                    c.add("case %s: return %s;", ctxt.getLocationValueText(loc, locIdx),
                            gencodeExpr(eq.getValue(), ctxt, "state"));
                    found = true;
                    break;
                }
            }
            Assert.check(found);
        }

        // Generate 'default' for paranoia checking.
        c.add("default: throw new RuntimeException(\"Invalid lp value: \" + state.%s.%s);",
                ctxt.getAutSubStateFieldName(aut), ctxt.getLocationPointerFieldName(aut));

        // Close 'switch'.
        c.dedent();
        c.add("}");
    }

    /**
     * Collect the algebraic variables defined in the component (recursively).
     *
     * @param comp The component.
     * @param vars The variables collected so far, as pairs of absolute names of the variables and the variables
     *     themselves. Is modified in-place.
     */
    public static void collectAlgVars(ComplexComponent comp, List<Pair<String, AlgVariable>> vars) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof AlgVariable) {
                vars.add(pair(getAbsName(decl, false), (AlgVariable)decl));
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectAlgVars((ComplexComponent)child, vars);
            }
        }
    }
}
