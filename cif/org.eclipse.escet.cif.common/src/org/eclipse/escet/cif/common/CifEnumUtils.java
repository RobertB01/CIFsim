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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;

/** CIF enumeration utility methods. */
public class CifEnumUtils {
    /** Constructor for the {@link CifEnumUtils} class. */
    private CifEnumUtils() {
        // Static class.
    }

    /**
     * Returns a mapping from enumeration declarations to their representatives, which may be themselves.
     *
     * @param enumDecls The enumeration declarations to consider.
     * @return The mapping from enumeration declarations to their representatives.
     */
    public static Map<EnumDecl, EnumDecl> getEnumDeclReprs(List<EnumDecl> enumDecls) {
        // Determine equal enumerations, and their representatives.
        Map<EnumDeclEqHashWrap, List<EnumDeclEqHashWrap>> commonMap = map();
        for (EnumDecl enumDecl: enumDecls) {
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
        for (Entry<EnumDeclEqHashWrap, List<EnumDeclEqHashWrap>> elem: commonMap.entrySet()) {
            EnumDeclEqHashWrap representative = elem.getKey();
            List<EnumDeclEqHashWrap> equalEnums = elem.getValue();
            for (EnumDeclEqHashWrap equalEnum: equalEnums) {
                rslt.put(equalEnum.enumDecl, representative.enumDecl);
            }
        }
        return rslt;
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
