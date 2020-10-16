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

package org.eclipse.escet.cif.common;

import java.util.regex.Pattern;

/** CIF validation utility methods. */
public class CifValidationUtils {
    /** Constructor for the {@link CifValidationUtils} class. */
    private CifValidationUtils() {
        // Static class.
    }

    /** Regular expression pattern for matching CIF identifiers. */
    private static final Pattern ID_PAT = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    /**
     * Is the given identifier a valid CIF identifier?
     *
     * @param id The identifier to check.
     * @return {@code true} if the given identifier is a valid CIF identifier, {@code false} otherwise.
     */
    public static boolean isValidIdentifier(String id) {
        return ID_PAT.matcher(id).matches();
    }

    /**
     * Is the given absolute name a valid CIF name? Valid CIF names consist of one or more CIF identifiers, separated by
     * dots ({@code "."}).
     *
     * @param name The name to check.
     * @return {@code true} if the given name is a valid, {@code false} otherwise.
     */
    public static boolean isValidName(String name) {
        // Split on '.'. Keep leading/trailing/empty items.
        String[] ids = name.split("\\.", -1);

        // Need at least one identifier, and all identifiers must be valid.
        if (ids.length < 1) {
            return false;
        }
        for (String id: ids) {
            if (!isValidIdentifier(id)) {
                return false;
            }
        }
        return true;
    }
}
