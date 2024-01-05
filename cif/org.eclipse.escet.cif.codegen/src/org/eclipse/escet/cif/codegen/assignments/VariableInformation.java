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

package org.eclipse.escet.cif.codegen.assignments;

import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;

/** All information about a variable in the scope. */
public class VariableInformation {
    /** Type information of the variable. */
    public final TypeInfo typeInfo;

    /** Name of the variable. */
    public final String name;

    /** Name of the variable in the target language. */
    public final String targetVariableName;

    /** Code to refer to the storage for the variable in the target language. */
    public final String targetRef;

    /** Whether the variable is a temporary copy of the 'real' variable. */
    public final boolean isTempVar;

    /**
     * Constructor of the {@link VariableInformation} class.
     *
     * @param typeInfo Type information of the variable.
     * @param name Name of the variable.
     * @param targetVariableName Name of the variable in the target language.
     * @param targetRef Code to refer to the storage for the variable in the target language.
     * @param isTempVar Whether the variable is a temporary copy of the 'real' variable.
     */
    public VariableInformation(TypeInfo typeInfo, String name, String targetVariableName, String targetRef,
            boolean isTempVar)
    {
        this.typeInfo = typeInfo;
        this.name = name;
        this.targetVariableName = targetVariableName;
        this.targetRef = targetRef;
        this.isTempVar = isTempVar;
    }
}
