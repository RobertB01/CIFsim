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

package org.eclipse.escet.cif.codegen.options;

/** Code generator target language. */
public enum TargetLanguage {
    /** Java. */
    JAVA("Java"),

    /** C89. */
    C89("C89"),

    /** C99. */
    C99("C99"),

    /** Simulink. */
    SIMULINK("Simulink");

    /** The name of the target language. End-user readable. */
    public final String readableName;

    /**
     * Constructor for the {@link TargetLanguage} enumeration.
     *
     * @param readableName The name of the target language. End-user readable.
     */
    private TargetLanguage(String readableName) {
        this.readableName = readableName;
    }
}
