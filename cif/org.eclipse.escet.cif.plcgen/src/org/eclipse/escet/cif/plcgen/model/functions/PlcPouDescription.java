//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.functions;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcPou;
import org.eclipse.escet.common.java.Assert;

/** Function description of a POU. */
public class PlcPouDescription extends PlcBasicFuncDescription {
    /**
     * Constructor of the {@link PlcPouDescription}.
     *
     * @param pou The POU to describe.
     */
    public PlcPouDescription(PlcPou pou) {
        super(PlcFuncOperation.UNKNOWN, pou.name, makeParameters(pou), PlcFuncNotation.NOT_INFIX, pou.retType,
                PlcFuncTypeExtension.NEVER);
    }

    /**
     * Construct the parameter descriptions of the provided POU.
     *
     * @param pou POU to describe.
     * @return The parameter descriptions of the given POU.
     */
    private static PlcParameterDescription[] makeParameters(PlcPou pou) {
        // Construct space for the parameters descriptions.
        int numParams = pou.inputVars.size() + pou.inOutVars.size() + pou.outputVars.size();
        PlcParameterDescription[] params = new PlcParameterDescription[numParams];

        // Fill the space.
        int index = 0;
        for (PlcBasicVariable plcVar: pou.inputVars) {
            params[index] = new PlcParameterDescription(plcVar.varName, PlcParamDirection.INPUT_ONLY, plcVar.type);
            index++;
        }
        for (PlcBasicVariable plcVar: pou.inOutVars) {
            params[index] = new PlcParameterDescription(plcVar.varName, PlcParamDirection.INPUT_OUTPUT, plcVar.type);
            index++;
        }
        for (PlcBasicVariable plcVar: pou.outputVars) {
            params[index] = new PlcParameterDescription(plcVar.varName, PlcParamDirection.OUTPUT_ONLY, plcVar.type);
            index++;
        }
        Assert.areEqual(index, params.length);

        // And return the parameter descriptions.
        return params;
    }
}
