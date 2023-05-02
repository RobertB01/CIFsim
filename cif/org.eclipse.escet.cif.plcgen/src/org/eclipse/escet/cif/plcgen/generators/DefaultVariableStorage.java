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

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcType;
import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;

/** Class for handling storage and retrieval of globally used variables in the PLC program. */
public class DefaultVariableStorage implements VariableStorage {
    /** PLC target to generate code for. */
    @SuppressWarnings("unused")
    private final PlcTarget target;

    /** Type generator. */
    private final TypeGenerator typeGen;

    /** PLC code storage and writer. */
    private final PlcCodeStorage codeStorage;

    /** Generator for obtaining clash-free names in the generated code. */
    private final NameGenerator nameGenerator;

    /** Names of converted declarations. */
    private final Map<Declaration, PlcVariable> variables = map();

    /**
     * Constructor of the {@link DefaultVariableStorage} class.
     *
     * @param target PLC target to generate code for.
     * @param typeGen Type generator.
     * @param codeStorage PLC code storage and writer.
     * @param nameGenerator Generator for obtaining clash-free names in the generated code.
     */
    public DefaultVariableStorage(PlcTarget target, TypeGenerator typeGen, PlcCodeStorage codeStorage,
            NameGenerator nameGenerator)
    {
        this.target = target;
        this.typeGen = typeGen;
        this.codeStorage = codeStorage;
        this.nameGenerator = nameGenerator;
    }

    @Override
    public void addStateVariable(Declaration decl, CifType type) {
        PlcType varType = typeGen.convertType(type);
        String varName = nameGenerator.generateGlobalName(decl);
        PlcVariable plcVar = new PlcVariable(varName, varType);
        variables.put(decl, plcVar);
        codeStorage.addStateVariable(plcVar);
    }
}
