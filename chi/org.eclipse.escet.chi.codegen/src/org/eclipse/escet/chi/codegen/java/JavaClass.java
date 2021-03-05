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

package org.eclipse.escet.chi.codegen.java;

import static org.eclipse.escet.chi.codegen.Constants.INDENT_SIZE;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;

/** Class representing a generated Java class in the code generation process. */
public class JavaClass extends JavaFile {
    /** Is it an abstract class? */
    private boolean isAbstract;

    /** Base class name, if any. */
    private final String baseclassName;

    /** Interfaces, if any. */
    private List<String> interfaceNames;

    /** Local variables. */
    private List<String> variables;

    /**
     * Constructor of the {@code JavaClass} class.
     *
     * @param packageName Name of the package or {@code null}.
     * @param isAbstract Is it an abstract class {@code true} if so.
     * @param name Name of the class.
     * @param baseclassName Name of the base class or {@code null}.
     * @param interfaceNames Names of implemented interfaces, or {@code null}.
     */
    public JavaClass(String packageName, boolean isAbstract, String name, String baseclassName,
            List<String> interfaceNames)
    {
        super(packageName, name);

        if (interfaceNames == null) {
            interfaceNames = list();
        }

        this.isAbstract = isAbstract;
        this.baseclassName = baseclassName;
        this.interfaceNames = interfaceNames;
        this.variables = list();
    }

    /**
     * Add a local variable to the class.
     *
     * <p>
     * Variable are assumed to need only a single line.
     * </p>
     *
     * @param var Variable line to add.
     */
    public void addVariable(String var) {
        variables.add(var);
    }

    @Override
    public Box toBox() {
        VBox box = new VBox(0);
        boxifyPreamble(box);

        // Class header line.
        String clsHeader = "public ";
        if (isAbstract) {
            clsHeader += "abstract ";
        }
        clsHeader += "class " + getClassName();
        if (baseclassName != null) {
            clsHeader += " extends " + baseclassName;
        }
        if (interfaceNames != null && !interfaceNames.isEmpty()) {
            String s = "";
            for (String iName: interfaceNames) {
                if (!s.isEmpty()) {
                    s += ", ";
                }
                s += iName;
            }
            clsHeader += " implements " + s;
        }
        box.add(clsHeader + " {");
        VBox body = new VBox(INDENT_SIZE);

        boxifyStandardVariables(body);

        // Variables.
        if (!variables.isEmpty()) {
            for (String var: variables) {
                body.add(var);
            }
        }

        // Add methods.
        boxifyMethods(body);

        box.add(body);
        box.add("}");
        return box;
    }
}
