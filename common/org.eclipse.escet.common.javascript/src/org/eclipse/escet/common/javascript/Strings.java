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

package org.eclipse.escet.common.javascript;

import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

/** Helper methods for working with strings. */
public final class Strings {
    /** Constructor for the {@link Strings} class. */
    private Strings() {
        // Static class.
    }

    /**
     * Converts a string to a JavaScript string literal. Applies escaping, and adds the surrounding double quotes. Can also
     * handle {@code null} values.
     *
     * <p>
     * To only apply the JavaScript string literal escaping, use the {@link StringEscapeUtils#escapeJava} method.
     * </p>
     *
     * @param s The string to convert to a JavaScript string literal.
     * @return The string as JavaScript string literal, or {@code "<null>"}.
     */
    public static String stringToJavaScript(String s) {
        // StringEscapeUtils.escapeJava also works for JavaScript
        return (s == null) ? "<null>" : "\"" + StringEscapeUtils.escapeJava(s) + "\"";
    }

    /**
     * Converts an array of strings to JavaScript string literals. Can also handle {@code null} values.
     *
     * @param sa The array of strings to convert to a JavaScript string literals.
     * @return The array of strings as JavaScript string literals.
     */
    public static String stringArrayToJavaScript(String[] sa) {
        List<String> rslt = listc(sa.length);
        for (String s: sa) {
            rslt.add(stringToJavaScript(s));
        }
        return String.join(", ", rslt);
    }
}
