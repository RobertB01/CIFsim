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
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
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
        Map<EnumDecl, EnumDecl> reprsMap = ctxt.getEnumReprs();

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
                c.add("%s,", lits.get(i).getName());
            }
            c.add("%s;", last(lits).getName());

            // Add 'getCifName' method.
            c.add();
            c.add("public static String getCifName() {");
            c.indent();
            c.add("return %s;", Strings.stringToJava(absName));
            c.dedent();
            c.add("}");

            // Add 'toString' method.
            c.add();
            c.add("@Override");
            c.add("public String toString() {");
            c.indent();
            c.add("return name();");
            c.dedent();
            c.add("}");
        }
    }

    /**
     * Returns a mapping from enumerations to their representatives, which may be themselves.
     *
     * @param spec The specification.
     * @return The mapping from enumerations to their representatives.
     */
    public static Map<EnumDecl, EnumDecl> getEnumReprs(Specification spec) {
        // Collect all the enumerations from the specification.
        List<EnumDecl> enums = list();
        collectEnums(spec, enums);

        // Determine equal enumerations, and their representatives.
        Map<EnumDeclEqHashWrap, List<EnumDeclEqHashWrap>> commonMap = map();
        for (EnumDecl enumDecl: enums) {
            EnumDeclEqHashWrap wrap = new EnumDeclEqHashWrap(enumDecl);
            List<EnumDeclEqHashWrap> commonList = commonMap.get(wrap);
            if (commonList == null) {
                commonList = list(wrap);
                commonMap.put(wrap, commonList);
            } else {
                commonList.add(wrap);
            }
        }

        // Create representative mapping.
        Map<EnumDecl, EnumDecl> rslt = map();
        Set<Entry<EnumDeclEqHashWrap, List<EnumDeclEqHashWrap>>> elems;
        elems = commonMap.entrySet();
        for (Entry<EnumDeclEqHashWrap, List<EnumDeclEqHashWrap>> elem: elems) {
            EnumDeclEqHashWrap representative = elem.getKey();
            List<EnumDeclEqHashWrap> equalEnums = elem.getValue();
            for (EnumDeclEqHashWrap equalEnum: equalEnums) {
                rslt.put(equalEnum.enumDecl, representative.enumDecl);
            }
        }
        return rslt;
    }

    /**
     * Collect the enumerations defined in the component (recursively).
     *
     * @param comp The component.
     * @param enums The enumerations collected so far. Is modified in-place.
     */
    private static void collectEnums(ComplexComponent comp, List<EnumDecl> enums) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof EnumDecl) {
                enums.add((EnumDecl)decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectEnums((ComplexComponent)child, enums);
            }
        }
    }

    /** Wrapper around enumeration declarations for equality and hashing. */
    private static class EnumDeclEqHashWrap {
        /** The enumeration declaration. */
        public final EnumDecl enumDecl;

        /** The enumeration literal names of {@link #enumDecl}. */
        public final List<String> literals;

        /** The hash of {@link #literals}. */
        public final int literalsHash;

        /**
         * Constructor for the {@link EnumDeclEqHashWrap} class.
         *
         * @param enumDecl The enumeration declaration.
         */
        public EnumDeclEqHashWrap(EnumDecl enumDecl) {
            this.enumDecl = enumDecl;
            this.literals = CifTypeUtils.getLiteralNames(enumDecl);
            this.literalsHash = literals.hashCode();
        }

        @Override
        public int hashCode() {
            return literalsHash;
        }

        @Override
        public boolean equals(Object obj) {
            // See also 'CifTypeUtils.areEnumsCompatible'.
            if (this == obj) {
                return true;
            }
            EnumDeclEqHashWrap other = (EnumDeclEqHashWrap)obj;
            return literals.equals(other.literals);
        }
    }
}
