//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.chi.codegen.types.TypeID;

/** Formal parameter in Java. */
public class JavaParameter {
    /** The type. */
    public final String type;

    /** The name. */
    public final String name;

    /**
     * Constructor for the {@link JavaParameter} class.
     *
     * @param tid Type id of the type of the parameter.
     * @param name Name of the parameter.
     */
    public JavaParameter(TypeID tid, String name) {
        this(tid.getJavaType(), name);
    }

    /**
     * Constructor for the {@link JavaParameter} class.
     *
     * @param type Name of the type of the parameter.
     * @param name Name of the parameter.
     */
    public JavaParameter(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * Convert a sequence of java parameters to text.
     *
     * @param args Sequence of java parameters ({@code null} means no parameters needed.
     * @return Text containing the parameters.
     */
    public static String convertParamsToString(List<JavaParameter> args) {
        if (args == null) {
            return "";
        }

        String txt = "";
        for (JavaParameter jp: args) {
            if (!txt.isEmpty()) {
                txt += ", ";
            }
            txt += jp.type + " " + jp.name;
        }
        return txt;
    }
}
