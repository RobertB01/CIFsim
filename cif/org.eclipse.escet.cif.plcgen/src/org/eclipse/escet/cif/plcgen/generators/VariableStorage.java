//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators;

import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.plcgen.conversion.expressions.CifDataProvider;

/** Interface for storing and retrieving globally used variables in the PLC program. */
public interface VariableStorage {
    /**
     * Add a state variable to the storage.
     *
     * @param variable Variable to add, should be a {@link DiscVariable} or {@link InputVariable}.
     * @param type CIF type of the variable.
     */
    void addStateVariable(Declaration variable, CifType type);

    /** Make the variables ready for use in the PLC code. */
    void process();

    /**
     * Provide access to PLC equivalents of the globally used CIF state for expression generation.
     *
     * @return Access to PLC equivalents of the globally used CIF state for expression generation.
     */
    public CifDataProvider getCifDataProvider();
}
