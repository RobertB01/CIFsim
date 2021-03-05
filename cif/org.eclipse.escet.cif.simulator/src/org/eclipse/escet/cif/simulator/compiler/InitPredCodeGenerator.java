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

import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;
import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.box.CodeBox;

/** Initialization predicate code generator. */
public class InitPredCodeGenerator {
    /** Constructor for the {@link InitPredCodeGenerator} class. */
    private InitPredCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the initialization predicates of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeInitPreds(Specification spec, CifCompilerContext ctxt) {
        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("InitPreds");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Initialization predicates. */");
        h.add("public final class InitPreds {");

        // Add body.
        CodeBox c = file.body;

        // Add 'evalInitPreds' method.
        c.add("public static boolean evalInitPreds(State state) {");
        c.indent();

        gencodeComponent(spec, ctxt, c);

        c.add();
        c.add("// All initialization predicates satisfied.");
        c.add("return true;");

        c.dedent();
        c.add("}");
    }

    /**
     * Generates initialization predicate evaluation code for the initialization predicates of the component
     * (recursively). This does not include the initialization predicates of the locations.
     *
     * @param comp The component.
     * @param ctxt The compiler context to use.
     * @param c The code box to which to add the code.
     */
    private static void gencodeComponent(ComplexComponent comp, CifCompilerContext ctxt, CodeBox c) {
        // Generate locally.
        String absName = getAbsName(comp);
        if (!comp.getInitials().isEmpty()) {
            c.add("// Initialization predicates for \"%s\".", absName);
        }

        for (Expression init: comp.getInitials()) {
            // Start of 'try'.
            c.add("try {");
            c.indent();

            // Actual initialization predicate evaluation.
            String initTxt = exprToStr(init);
            String compTxt = CifTextUtils.getComponentText2(comp);
            c.add("if (!(%s)) {", gencodeExpr(init, ctxt, "state"));
            c.indent();
            c.add("warn(\"Initialization predicate \\\"%s\\\" of %s is not satisfied.\");", escapeJava(initTxt),
                    escapeJava(compTxt));
            c.add("return false;");
            c.dedent();
            c.add("}");

            // End of 'try'.
            c.dedent();
            c.add("} catch (CifSimulatorException e) {");
            c.indent();
            c.add("throw new CifSimulatorException(\"Evaluation of initialization predicate \\\"%s\\\" of %s "
                    + "failed.\", e, state);", escapeJava(initTxt), escapeJava(compTxt));
            c.dedent();
            c.add("}");
        }

        // Generate recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                gencodeComponent((ComplexComponent)child, ctxt, c);
            }
        }
    }
}
