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

package org.eclipse.escet.chi.codegen.java;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.box.Boxable;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;

/** Class representing a generated Java file (with a class or enumeration). */
public abstract class JavaFile implements Boxable {
    /** Fully qualified class name. */
    private final String fqClassname;

    /** Imports (map of import name to static/non-static importing). */
    protected Map<String, Boolean> imports;

    /** Methods of the class. */
    private List<JavaMethod> methods;

    /**
     * Constructor of the {@link JavaFile} class.
     *
     * @param packageName Name of the package or {@code null}.
     * @param name Name of the file.
     */
    public JavaFile(String packageName, String name) {
        fqClassname = (packageName == null || packageName.isEmpty()) ? name : packageName + "." + name;
        imports = map();
        methods = list();
    }

    /**
     * Return the class name of the java class.
     *
     * @return The class name of the java class.
     */
    public String getClassName() {
        int idx = fqClassname.lastIndexOf('.');
        if (idx == -1) {
            return fqClassname;
        }
        return fqClassname.substring(idx + 1);
    }

    /**
     * Get the package name of the java class, if it exists.
     *
     * @return The package name of the java class, or {@code null}.
     */
    public String getPackageName() {
        int idx = fqClassname.lastIndexOf('.');
        if (idx == -1) {
            return null;
        }
        return fqClassname.substring(0, idx);
    }

    /**
     * Get the fully qualified class name.
     *
     * @return The fully qualified class name.
     */
    public String getFQclassname() {
        return fqClassname;
    }

    /**
     * Add an import to the imported classes of the java class.
     *
     * @param importName Name of the imported class (fully qualified name).
     * @param staticImport Whether the import is static.
     */
    public void addImport(String importName, boolean staticImport) {
        Boolean b = imports.get(importName);
        Assert.check(b == null || b == staticImport);

        if (b == null) {
            imports.put(importName, staticImport);
        }
    }

    /**
     * Add a number of non-static imports at the same time.
     *
     * @param imports Imports to add.
     */
    public void addImports(List<String> imports) {
        for (String line: imports) {
            addImport(line, false);
        }
    }

    /**
     * Add a method to the class.
     *
     * @param method Method to add.
     */
    public void addMethod(JavaMethod method) {
        methods.add(method);
    }

    /**
     * Generate the package line and the imports.
     *
     * @param box Box being used to store the output.
     */
    protected void boxifyPreamble(VBox box) {
        boolean addemptyLine = false;

        // Package line.
        String packageName = getPackageName();
        if (packageName != null) {
            box.add("package " + packageName + ";");
            addemptyLine = true;
        }

        // Imports.
        if (!imports.isEmpty()) {
            if (addemptyLine) {
                box.add("");
            }
            for (Map.Entry<String, Boolean> entry: imports.entrySet()) {
                String s = "import";
                if (entry.getValue()) {
                    s += " static";
                }
                box.add(s + " " + entry.getKey() + ";");
            }
            addemptyLine = true;
        }

        if (addemptyLine) {
            box.add("");
        }
        box.add("@SuppressWarnings(\"javadoc\")");
    }

    /**
     * Generate a number of standard variables for use by the methods.
     *
     * @param box Box being used for output.
     */
    protected void boxifyStandardVariables(VBox box) {
        // Add ALWAYS and NEVER constants.
        box.add("private static final boolean ALWAYS = true;");
        box.add("private static final boolean NEVER = false;");
    }

    /**
     * Add the methods to the box output.
     *
     * @param box Box to append the method texts to.
     */
    protected void boxifyMethods(VBox box) {
        for (JavaMethod m: methods) {
            box.add(m.generateBox(), true);
        }
    }
}
