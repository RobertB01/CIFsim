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

package org.eclipse.escet.common.app.framework.javacompiler;

import java.util.Locale;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/**
 * Java compiler option.
 *
 * @see RuntimeJavaCompiler#getJavaCompiler
 */
public class JavaCompilerOption extends EnumOption<JavaCompilerName> {
    /** Constructor for the {@link JavaCompilerOption} class. */
    public JavaCompilerOption() {
        super(
                // name,
                "Java compiler",

                // description
                "The Java compiler implementation to use. Specify \"jdk\" to use the Java compiler provided by "
                        + "the Java Development Kit (requires the use of a JDK, a JRE is not sufficient), or "
                        + "\"eclipse\" to use the Eclipse Compiler for Java (ecj) which is part of the "
                        + "Eclipse Java Development Tools (JDT). [DEFAULT=jdk].",

                // cmdShort
                null,

                // cmdLong
                "java-compiler",

                // cmdValue
                "JCOMPILER",

                // defaultValue
                JavaCompilerName.JDK,

                // showInDialog
                true,

                // optDialogDescr
                "The Java compiler implementation to use.");
    }

    @Override
    protected String getDialogText(JavaCompilerName value) {
        switch (value) {
            case JDK:
                return "Java compiler from the Java Development Kit (requires a JDK, a JRE is not sufficient)";
            case ECLIPSE:
                return "Eclipse Compiler for Java (ecj), part of the Eclipse Java Development Tools (JDT)";
        }
        throw new RuntimeException("Unknown value: " + value);
    }

    /**
     * Returns the name of the Java compiler implementation to use.
     *
     * @return The name of the Java compiler implementation to use.
     * @see RuntimeJavaCompiler#getJavaCompiler
     */
    public static String getCompilerName() {
        String name = Options.get(JavaCompilerOption.class).name();
        return name.toLowerCase(Locale.US);
    }
}
