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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Derivative code generator. */
public class DerivativeCodeGenerator {
    /** Constructor for the {@link DerivativeCodeGenerator} class. */
    private DerivativeCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the derivatives of the continuous variables of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeDerivatives(Specification spec, CifCompilerContext ctxt) {
        // Collect continuous variables.
        List<ContVariable> variables = list();
        collectContVars(spec, variables);

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("Derivatives");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Derivatives of all the continuous variables (excl. 'time'). */");
        h.add("public final class Derivatives {");

        // Add body.
        CodeBox c = file.body;

        // Add methods for the derivatives.
        for (ContVariable var: variables) {
            if (!c.isEmpty()) {
                c.add();
            }
            c.add("public static double %s(State state) {", ctxt.getDerivativeMethodName(var));
            c.indent();

            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Try to find derivative in declaration.
            boolean found = false;
            if (var.getDerivative() != null) {
                c.add("return %s;", gencodeExpr(var.getDerivative(), ctxt, "state"));
                found = true;
            }

            // Try to find derivative in equations of the component.
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
                gencodeDerivativesPerLoc(var, ctxt, c);
            }

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of the derivative of continuous variable \\\"%s\\\" "
                    + "failed.\", e, state);", getAbsName(var));
            c.dedent();
            c.add("}");

            // End of the method.
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Generate Java code from the equations of the locations, for the body of the method of the derivative of the given
     * continuous variable.
     *
     * @param var The continuous variable.
     * @param ctxt The compiler context to use.
     * @param c The code box in which to generate the code.
     */
    private static void gencodeDerivativesPerLoc(ContVariable var, CifCompilerContext ctxt, CodeBox c) {
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
     * Collect the continuous variables defined in the component (recursively).
     *
     * @param comp The component.
     * @param variables The continuous variables collected so far. Is modified in-place.
     */
    public static void collectContVars(ComplexComponent comp, List<ContVariable> variables) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof ContVariable) {
                variables.add((ContVariable)decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectContVars((ComplexComponent)child, variables);
            }
        }
    }
}
