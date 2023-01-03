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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.common.java.Assert;

/** Code generation context, where some variables are renamed for reading. */
public class ReadRenameCodeContext extends CodeContext {
    /** Parent code generation context. */
    private final CodeContext parentContext;

    /** Table with renamed variables for reading. */
    private Map<VariableWrapper, VariableInformation> renameTable = map();

    /**
     * Constructor of the {@link ReadRenameCodeContext} class.
     *
     * @param parentContext Parent code generation context.
     */
    public ReadRenameCodeContext(CodeContext parentContext) {
        super(parentContext.codeGen);
        this.parentContext = parentContext;
    }

    /**
     * Add an entry in the read rename table.
     *
     * @param var Variable to add.
     * @param varInfo Information to return when querying the variable for reading.
     */
    public void addReadRename(VariableWrapper var, VariableInformation varInfo) {
        Assert.check(varInfo.isTempVar);
        renameTable.put(var, varInfo);
    }

    @Override
    public VariableInformation getReadVarInfo(VariableWrapper var) {
        VariableInformation varInfo = renameTable.get(var);
        if (varInfo == null) {
            varInfo = parentContext.getReadVarInfo(var);
        }
        return varInfo;
    }

    @Override
    public VariableInformation getWriteVarInfo(Declaration decl) {
        // Bypass read rename lookup when writing the variable.
        return parentContext.getWriteVarInfo(decl);
    }
}
