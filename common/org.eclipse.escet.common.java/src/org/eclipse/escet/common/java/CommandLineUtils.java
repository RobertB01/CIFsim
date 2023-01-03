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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

/** Helper class with date/time related helper methods. */
public class CommandLineUtils {
    /** Constructor for the {@link CommandLineUtils} class. */
    private CommandLineUtils() {
        // Static class.
    }

    /**
     * Parses command line arguments.
     *
     * <p>
     * Syntax:
     * <ul>
     * <li>{@link Character#isWhitespace Whitespace} outside of arguments is ignored. In other words, whitespace
     * separates arguments.</li>
     * <li>To include whitespace in an argument, the argument (or a part of it), may be quoted, by putting it between
     * double quotes ({@code "}) or single quotes ({@code '}).</li>
     * <li>Characters may be escaped by prefixing them with a backslash ({@code \}). This is particularly useful for
     * single/double quotes and escape characters. Escapes work the same inside and outside of quoted parts.</li>
     * </ul>
     * </p>
     *
     * @param args The arguments, as a single text.
     * @return The arguments, with separate text per argument.
     * @throws IllegalArgumentException If the quotes in {@code args} are unbalanced.
     * @see #combineArgs
     */
    @SuppressWarnings("null")
    public static String[] parseArgs(String args) {
        List<String> rslt = list();
        int pos = 0;
        boolean inArg = false;
        boolean inDoubleQuote = false;
        boolean inSingleQuote = false;
        boolean inEscape = false;
        StringBuilder b = null;

        while (pos < args.length()) {
            char c = args.charAt(pos);

            if (!inArg) {
                if (Character.isWhitespace(c)) {
                    // Skip whitespace outside of arguments.
                    pos++;
                } else {
                    // Start new argument with fresh buffer. The actual
                    // character is processed in the next iteration.
                    b = new StringBuilder();
                    inArg = true;
                }
            } else if (inDoubleQuote) {
                if (inEscape) {
                    // Keep escaped character.
                    inEscape = false;
                    b.append(c);
                    pos++;
                } else if (c == '"') {
                    // End of double quoted part of argument.
                    inDoubleQuote = false;
                    pos++;
                } else if (c == '\\') {
                    // Start of escaped character.
                    inEscape = true;
                    pos++;
                } else {
                    // Just some character of the argument.
                    b.append(c);
                    pos++;
                }
            } else if (inSingleQuote) {
                if (inEscape) {
                    // Keep escaped character.
                    inEscape = false;
                    b.append(c);
                    pos++;
                } else if (c == '\'') {
                    // End of single quoted part of argument.
                    inSingleQuote = false;
                    pos++;
                } else if (c == '\\') {
                    // Start of escaped character.
                    inEscape = true;
                    pos++;
                } else {
                    // Just some character of the argument.
                    b.append(c);
                    pos++;
                }
            } else {
                if (inEscape) {
                    // Keep escaped character.
                    inEscape = false;
                    b.append(c);
                    pos++;
                } else if (Character.isWhitespace(c)) {
                    // End of argument.
                    inArg = false;
                    rslt.add(b.toString());
                    pos++;
                } else if (c == '"') {
                    // Start of double quoted part of argument.
                    inDoubleQuote = true;
                    pos++;
                } else if (c == '\'') {
                    // Start of single quoted part of argument.
                    inSingleQuote = true;
                    pos++;
                } else if (c == '\\') {
                    // Start of escaped character.
                    inEscape = true;
                    pos++;
                } else {
                    // Just some character of the argument.
                    b.append(c);
                    pos++;
                }
            }
        }

        // Check for premature end of input.
        if (inEscape) {
            String msg = fmt("Premature end of input: missing escaped character in \"%s\".", args);
            throw new IllegalArgumentException(msg);
        }

        if (inSingleQuote) {
            String msg = fmt("Premature end of input: missing end of single quote in \"%s\".", args);
            throw new IllegalArgumentException(msg);
        }

        if (inDoubleQuote) {
            String msg = fmt("Premature end of input: missing end of double quote in \"%s\".", args);
            throw new IllegalArgumentException(msg);
        }

        // Add last remaining argument, if any.
        if (inArg) {
            rslt.add(b.toString());
        }

        // Return the arguments.
        return rslt.toArray(new String[0]);
    }

    /**
     * Combines command line arguments into a single string, applying quoting and escaping where needed.
     *
     * @param args The arguments, with separate text per argument.
     * @return The arguments, as a single text.
     * @see #parseArgs
     */
    public static String combineArgs(String[] args) {
        StringBuilder rslt = new StringBuilder();
        for (String arg: args) {
            // Separate arguments with a space.
            if (rslt.length() > 0) {
                rslt.append(" ");
            }

            // Escape and quote the argument.
            if (Strings.containsWhitespace(arg)) {
                // Escape all escape characters and double quotes.
                arg = arg.replace("\\", "\\\\").replace("\"", "\\\"");

                // Add quoting.
                arg = "\"" + arg + "\"";
            } else {
                // Escape all escape characters and single/double quotes.
                arg = arg.replace("\\", "\\\\").replace("\"", "\\\"").replace("'", "\\'");
            }

            // Add argument.
            rslt.append(arg);
        }

        return rslt.toString();
    }
}
