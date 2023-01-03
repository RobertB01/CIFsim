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
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Strings;

/** Enumeration code generator. */
public class EnumCodeGenerator {
    /** Constructor for the {@link EnumCodeGenerator} class. */
    private EnumCodeGenerator() {
        // Static class.
    }

    /**
     * Generate Java code for the enumerations of the specification.
     *
     * @param ctxt The compiler context to use.
     */
    public static void gencodeEnums(CifCompilerContext ctxt) {
        // Get representatives mapping.
        Map<EnumDecl, EnumDecl> reprsMap = ctxt.getEnumDeclReprs();

        // Get names of the enumerations, per representative.
        Map<EnumDecl, List<String>> namesMap = map();
        for (Entry<EnumDecl, EnumDecl> entry: reprsMap.entrySet()) {
            EnumDecl equalEnum = entry.getKey();
            EnumDecl repr = entry.getValue();

            List<String> names = namesMap.get(repr);
            if (names == null) {
                names = list();
                namesMap.put(repr, names);
            }
            names.add(getAbsName(equalEnum));
        }

        // Generate a class for each representative.
        for (Entry<EnumDecl, List<String>> entry: namesMap.entrySet()) {
            // Get representative, names, and literals.
            EnumDecl enumDecl = entry.getKey();
            List<EnumLiteral> lits = enumDecl.getLiterals();
            List<String> names = entry.getValue();

            // Add new code file.
            String className = ctxt.getEnumClassName(enumDecl);
            JavaCodeFile file = ctxt.addCodeFile(className);

            // Add header.
            CodeBox h = file.header;
            String absName = getAbsName(enumDecl);
            h.add("/** Enumeration representative \"%s\", for enumerations:", absName);
            h.add(" * <ul>");
            for (String name: names) {
                h.add(" *  <li>%s</li>", name);
            }
            h.add(" * </ul>");
            h.add(" */");
            h.add("public enum %s implements RuntimeToStringable {", className);

            // Add literals.
            CodeBox c = file.body;
            for (int i = 0; i < lits.size() - 1; i++) {
                EnumLiteral lit = lits.get(i);
                c.add("%s(\"%s\"),", ctxt.getEnumConstName(lit), lit.getName());
            }
            c.add("%s(\"%s\");", ctxt.getEnumConstName(last(lits)), last(lits).getName());

            // Add 'getEnumCifName' method.
            c.add();
            c.add("public static String getEnumCifName() {");
            c.indent();
            c.add("return %s;", Strings.stringToJava(absName));
            c.dedent();
            c.add("}");

            // Add 'cifLiteralName' variable.
            c.add();
            c.add("private final String cifLiteralName;");

            // Add constructor.
            c.add();
            c.add("%s(String cifLiteralName) {", className);
            c.indent();
            c.add("this.cifLiteralName = cifLiteralName;");
            c.dedent();
            c.add("}");

            // Add 'toString' method.
            c.add();
            c.add("@Override");
            c.add("public String toString() {");
            c.indent();
            c.add("return cifLiteralName;");
            c.dedent();
            c.add("}");
        }
    }
}
