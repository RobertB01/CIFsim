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

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

/** JavaScript code utility methods. */
public class JavaScriptCodeUtils {
    /** Constructor for the {@link JavaScriptCodeUtils} class. */
    private JavaScriptCodeUtils() {
        // Static class.
    }

    /**
     * JavaScript language keywords, reserved identifiers and identifiers with special meaning.
     *
     * @see <a href="https://www.w3schools.com/js/js_reserved.asp">Keywords, JavaScript Language
     *     Specification</a>
     */
    public static final Set<String> JAVASCRIPT_IDS = set(
            // Reserved keywords.
            "abstract", "arguments", "await",
            "boolean", "break", "byte",
            "case", "catch", "char", "class", "const", "continue",
            "debugger", "default", "delete", "do", "double",
            "else", "enum", "eval", "export", "extends",
            "false", "final", "finally", "float", "for", "function",
            "goto",
            "if", "implements", "import", "in", "instanceof", "int", "interface",
            "let", "long",
            "native", "new", "null",
            "package", "private", "protected", "public",
            "return",
            "short", "static", "super", "switch", "synchronized",
            "this", "throw", "throws", "transient", "true", "try", "typeof",
            "var", "void", "volatile",
            "while", "with",
            "yield");

    /**
     * Returns a JavaScript compatible name for the given {@code name}, that does not conflict with JavaScript's reserved
     * identifiers. Conflicting identifiers are prefixed with an underscore character ({@code _}).
     *
     * @param name The name to make JavaScript compatible.
     * @return The JavaScript compatible name.
     * @see #JAVASCRIPT_IDS
     */
    public static String makeJavaScriptName(String name) {
        if (!JavaScriptCodeUtils.JAVASCRIPT_IDS.contains(name)) {
            return name;
        }
        return "_" + name;
    }
}
