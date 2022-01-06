//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.simulator.compiler.ExprCodeGenerator.gencodeExpr;
import static org.eclipse.escet.cif.simulator.compiler.TypeCodeGenerator.gencodeType;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.common.ConstantOrderer;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Constant code generator. */
public class ConstCodeGenerator {
    /** Constructor for the {@link ConstCodeGenerator} class. */
    private ConstCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the constants of the specification.
     *
     * @param spec The specification.
     * @param ctxt The compiler context to use.
     */
    public static void gencodeConsts(Specification spec, CifCompilerContext ctxt) {
        // Collect constants.
        List<Constant> constants = list();
        collectConstants(spec, constants);

        // Add new code file.
        JavaCodeFile file = ctxt.addCodeFile("Constants");

        // Add header.
        CodeBox h = file.header;
        h.add("/** Constants. */");
        h.add("public final class Constants {");

        // Add body.
        CodeBox c = file.body;

        // Order constants by dependencies.
        constants = new ConstantOrderer().computeOrder(constants);
        Assert.notNull(constants);

        // Add fields for the constants. Note that evaluation failures can not
        // occur, since the type checker already evaluated the constants.
        // Furthermore, the values of constants can always be computed in
        // finite time, as user-defined functions are not allowed.
        for (Constant constant: constants) {
            // There is no need to catch runtime errors here, as the type
            // checker already evaluated the constant without errors.
            c.add("public static final %s %s = %s;", gencodeType(constant.getType(), ctxt),
                    ctxt.getConstFieldName(constant), gencodeExpr(constant.getValue(), ctxt, null));
        }
    }

    /**
     * Collect the constants defined in the component (recursively).
     *
     * @param comp The component.
     * @param constants The constant collected so far. Is modified in-place.
     */
    private static void collectConstants(ComplexComponent comp, List<Constant> constants) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Constant) {
                constants.add((Constant)decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectConstants((ComplexComponent)child, constants);
            }
        }
    }
}
