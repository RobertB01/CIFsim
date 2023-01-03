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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.app.framework.javacompiler.JavaInputFileObject;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.JavaCodeUtils;

/** A single Java code file. */
public class JavaCodeFile extends JavaInputFileObject {
    /** The package name of the Java type. */
    public final String packageName;

    /** The name of the Java type, without package name. */
    public final String name;

    /**
     * The imports of the Java code file. Should contains the absolute type and method names. For abstract methods, the
     * {@code "static "} prefix must be included before the absolute name. May be modified in-place, by adding
     * additional imports.
     */
    public final List<String> imports = list();

    /** The code for the header of the Java type. May be modified in-place. */
    public final CodeBox header = new MemoryCodeBox();

    /** The code for the body of the Java type. May be modified in-place. */
    public final CodeBox body = new MemoryCodeBox();

    /**
     * Constructor for the {@link JavaCodeFile} class.
     *
     * @param packageName The package name of the Java type.
     * @param name The name of the Java type, without package name.
     */
    public JavaCodeFile(String packageName, String name) {
        // Provide absolute name to super constructor.
        super(getAbsClassName(packageName, name));

        // Store locally.
        this.packageName = packageName;
        this.name = name;
    }

    /**
     * Returns the absolute (class) name of the Java type for which this is the code file.
     *
     * @return The absolute (class) name of the Java type.
     */
    public String getAbsClassName() {
        return getAbsClassName(packageName, name);
    }

    /**
     * Returns the absolute (class) name of a Java type, given the package name and type name.
     *
     * @param packageName The package name of the Java type.
     * @param name The name of the Java type, without package name.
     * @return The absolute (class) name of the Java type.
     */
    private static String getAbsClassName(String packageName, String name) {
        return packageName + (packageName.isEmpty() ? "" : ".") + name;
    }

    /**
     * Returns the code of the Java code file as a {@link VBox}.
     *
     * @return The code of the Java code file as a {@link VBox}.
     */
    public VBox toBox() {
        // Initialize empty code file.
        VBox code = new VBox();

        // Add package.
        code.add(new TextBox("package %s;", packageName));
        code.add();

        // Add imports.
        List<String> formattedImports;
        formattedImports = JavaCodeUtils.formatImports(imports, packageName);
        for (String imp: formattedImports) {
            code.add(imp);
        }
        if (!formattedImports.isEmpty()) {
            code.add();
        }

        // Add type header.
        code.add(header);

        // Add indented body.
        VBox indentedBody = new VBox(4);
        indentedBody.add(body);
        code.add(indentedBody);

        // Close type.
        code.add("}");

        // Return the complete code, as a VBox.
        return code;
    }

    @Override
    public CharSequence getCharContent() {
        return toBox().toBuilder();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JavaCodeFile)) {
            return false;
        }
        JavaCodeFile other = (JavaCodeFile)obj;
        return this.absClassName.equals(other.absClassName);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ absClassName.hashCode();
    }
}
