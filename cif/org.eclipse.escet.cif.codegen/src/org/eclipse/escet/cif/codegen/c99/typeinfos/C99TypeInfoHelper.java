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

package org.eclipse.escet.cif.codegen.c99.typeinfos;

import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;

/** Assistant class with support code handling C99 type information objects. */
public class C99TypeInfoHelper {
    /** Constructor of the {@link C99TypeInfoHelper} class. */
    private C99TypeInfoHelper() {
        // Static class.
    }

    /**
     * Find out whether a type supports plain memcmp to check for equality.
     *
     * @param ti Type information object to query.
     * @return {@code true} if the type can be tested with {@code memcmp}, else {@code false}.
     */
    public static boolean typeSupportsRawMemCmp(TypeInfo ti) {
        if (ti instanceof C99TypeInfo) {
            C99TypeInfo cti = (C99TypeInfo)ti;
            return cti.supportRawMemCmp();
        }
        return false; // Safe default.
    }

    /**
     * Find out whether a type uses plain values or references to values to pass data around.
     *
     * @param ti Type information object to query.
     * @return {@code true} if the type always uses values, {@code false} if the type uses references.
     */
    public static boolean typeUsesValues(TypeInfo ti) {
        if (ti instanceof C99TypeInfo) {
            C99TypeInfo cti = (C99TypeInfo)ti;
            return cti.useValues();
        }
        throw new RuntimeException("Cannot decide about value usage for type " + str(ti));
    }

    /**
     * Get the name of the 'type print' for this type.
     *
     * @param ti Type information object to query.
     * @param rawString Whether to output the string as-is, otherwise escape \n, \t, and \.
     * @return The function name for the 'type print' function of the type.
     */
    public static String typeGetTypePrintName(TypeInfo ti, boolean rawString) {
        if (ti instanceof C99TypeInfo) {
            C99TypeInfo cti = (C99TypeInfo)ti;
            return cti.getTypePrintName(rawString);
        }
        throw new RuntimeException("Cannot decide about value usage for type " + str(ti));
    }
}
