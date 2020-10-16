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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/** Java code utility methods. */
public class JavaCodeUtils {
    /** Constructor for the {@link JavaCodeUtils} class. */
    private JavaCodeUtils() {
        // Static class.
    }

    /**
     * Java language keywords and other reserved identifiers.
     *
     * @see <a href="http://docs.oracle.com/javase/tutorial/java/nutsandbolts/_keywords.html">Java Language Keywords</a>
     */
    public static final Set<String> JAVA_IDS = set(
            // Keywords.
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", // Reserved.
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "for", "goto", // Reserved.
            "if", "implements", "import", "instanceof", "int", "interface", "long", "new", "package", "private",
            "protected", "public", "return", "short", "static", "strictfp", "switch", "synchronized", "this", "throw",
            "throws", "transient", "try", "void", "volatile",

            // Others.
            "false", "null", "true");

    /**
     * Returns a Java compatible name for the given {@code name}, that does not conflict with Java's reserved
     * identifiers. Conflicting identifiers are prefixed with an underscore character ({@code _}).
     *
     * @param name The name to make Java compatible.
     * @return The Java compatible name.
     */
    public static String makeJavaName(String name) {
        if (!JavaCodeUtils.JAVA_IDS.contains(name)) {
            return name;
        }
        return "_" + name;
    }

    /**
     * Generates formatted imports. The imports are generated in an Eclipse compatible formatting, such that 'Organize
     * imports' (using default settings) does not result in any changes to the generated code.
     *
     * <p>
     * Imports starting with "{@code java.lang.}" are omitted from the result, as there is no need to import them.
     * Similarly, imports in the same package as the {@code pkg} are omitted.
     * </p>
     *
     * @param imports The imports, as absolute names of the imported types and methods. If they are methods, they should
     *     be prefixed with "{@code static }".
     * @param pkg The package of the file for which the imports are generated. May be {@code null} if not known.
     * @return The generated code for the imports.
     */
    public static List<String> formatImports(Collection<String> imports, String pkg) {
        // Get static and non-static imports.
        List<String> staticImports = list();
        List<String> otherImports = list();
        for (String imp: imports) {
            if (imp.startsWith("static ")) {
                staticImports.add(imp);
            } else {
                otherImports.add(imp);
            }
        }

        // Sort imports for determinism.
        Collections.sort(staticImports);
        Collections.sort(otherImports);

        // Initialize result.
        List<String> rslt = list();

        // Process each of the sorted imports.
        String lastImportStart = null;
        for (String imp: concat(staticImports, otherImports)) {
            // Skip useless imports, for the 'java.lang', but not its sub-packages.
            if (imp.startsWith("java.lang.") && StringUtils.countMatches(imp, ".") == 2) {
                continue;
            }

            // Skip useless imports, for primitives.
            if (imp.equals("boolean")) {
                continue;
            } else if (imp.equals("byte")) {
                continue;
            } else if (imp.equals("char")) {
                continue;
            } else if (imp.equals("short")) {
                continue;
            } else if (imp.equals("int")) {
                continue;
            } else if (imp.equals("long")) {
                continue;
            } else if (imp.equals("float")) {
                continue;
            } else if (imp.equals("double")) {
                continue;
            }

            // Skip imports in same package.
            if (pkg != null) {
                int idx = imp.lastIndexOf('.');
                Assert.check(idx != 0);
                String impPkg = (idx < 0) ? "" : Strings.slice(imp, 0, idx);
                if (impPkg.equals(pkg)) {
                    continue;
                }
            }

            // Add empty separator line between different top level packages.
            if (lastImportStart != null && !imp.startsWith(lastImportStart)) {
                rslt.add("");
            }
            lastImportStart = Strings.slice(imp, 0, 1);

            // Add import.
            rslt.add(fmt("import %s;", imp));
        }

        return rslt;
    }
}
