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

package org.eclipse.escet.tooldef.common;

import java.util.regex.Pattern;

/** ToolDef validation utility methods. */
public class ToolDefValidationUtils {
    /** Constructor for the {@link ToolDefValidationUtils} class. */
    private ToolDefValidationUtils() {
        // Static class.
    }

    /** Regular expression pattern for matching ToolDef identifiers. */
    private static final Pattern ID_PAT = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    /**
     * Is the given identifier a valid ToolDef identifier?
     *
     * @param id The identifier to check.
     * @return {@code true} if the given identifier is a valid ToolDef identifier, {@code false} otherwise.
     */
    public static boolean isValidIdentifier(String id) {
        return ID_PAT.matcher(id).matches();
    }
}
