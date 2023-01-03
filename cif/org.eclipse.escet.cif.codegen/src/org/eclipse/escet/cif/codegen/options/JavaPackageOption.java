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

package org.eclipse.escet.cif.codegen.options;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Java package option. */
public class JavaPackageOption extends StringOption {
    /** Constructor for the {@link JavaPackageOption} class. */
    public JavaPackageOption() {
        super(
                // name
                "Java package",

                // description
                "The Java package in which to generate Java code. If not specified, the code prefix is used.",

                // cmdShort
                null,

                // cmdLong
                "java-package",

                // cmdValue
                "PACKAGE",

                // defaultValue
                null,

                // emptyAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                "The Java package in which to generate Java code. If not specified, the code prefix is used.",

                // optDialogLabelText
                "Java package:");
    }

    /**
     * Returns the java package in which to generate Java code.
     *
     * @return The java package in which to generate Java code.
     */
    public static String getPackage() {
        String javaPackage = Options.get(JavaPackageOption.class);
        if (javaPackage == null) {
            javaPackage = CodePrefixOption.getPrefix();
        }
        return javaPackage;
    }
}
