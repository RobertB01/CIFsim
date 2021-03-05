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
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;

/** Class to represent an Java enumeration class. */
public class JavaEnum extends JavaFile {
    /** Values of the enumeration type. */
    Set<String> enumValues;

    /**
     * Constructor of the {@link JavaEnum} class.
     *
     * @param name Name of the enumeration.
     */
    public JavaEnum(String name) {
        super(null, name);
        enumValues = set();
    }

    @Override
    public Box toBox() {
        // Get text with the package line and the imports.
        VBox box = new VBox(0);
        boxifyPreamble(box);

        box.add(fmt("public enum %s {", getClassName()));

        // Add the names of the values.
        List<String> lst = sortedstrings(enumValues);
        VBox entries = new VBox(INDENT_SIZE);
        int i = lst.size() - 1;
        for (String line: lst) {
            line += (i == 0) ? ";" : ",";
            entries.add(line);
            i--;
        }
        box.add(entries);

        box.add("");
        VBox vb = new VBox(INDENT_SIZE);
        boxifyStandardVariables(vb);

        boxifyMethods(vb);

        box.add(vb);
        box.add("}");
        return box;
    }

    /**
     * Add a value to the enumeration.
     *
     * @param name Name of the new value.
     */
    public void addValue(String name) {
        boolean b = enumValues.add(name);
        Assert.check(b); // New element must always be added.
    }
}
