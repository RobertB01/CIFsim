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

    /** Name of the storage identifier in the target language. */
    public final String targetName;

    /** If set, the variable is a reference to the data value. */
    public boolean isReference;

    /** Whether the variable is a temporary copy of the 'real' variable. */
    public boolean isTempVar = false;

    /**
     * Constructor of the {@link VariableInformation} class.
     *
     * @param typeInfo Type information of the variable.
     * @param name Name of the variable.
     * @param targetVariableName Name of the variable in the target language.
     * @param targetName Name of the storage identifier in the target language.
     * @param isReference If set, the variable is a reference to the data value.
     */
    public VariableInformation(TypeInfo typeInfo, String name, String targetVariableName, String targetName,
            boolean isReference)
    {
        this.typeInfo = typeInfo;
        this.name = name;
        this.targetVariableName = targetVariableName;
        this.targetName = targetName;
        this.isReference = isReference;
    }
}
