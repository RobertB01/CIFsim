//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.simulink.typeinfos;

import static org.eclipse.escet.cif.codegen.c89.C89DataValue.makeValue;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.c89.typeinfos.C89RealTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/** Simulink type generator for the real type. */
public class SimulinkRealTypeInfo extends C89RealTypeInfo {
    /**
     * Constructor for {@link SimulinkRealTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     */
    public SimulinkRealTypeInfo(boolean genLocalFunctions, CifType cifType) {
        super(genLocalFunctions, cifType);
    }

    @Override
    public String getTargetType() {
        return "real_T";
    }

    @Override
    public ExprCode convertTimeExpression(Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(makeValue("cstate[0]"));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof SimulinkRealTypeInfo;
    }

    @Override
    public int hashCode() {
        return SimulinkRealTypeInfo.class.hashCode();
    }
}
