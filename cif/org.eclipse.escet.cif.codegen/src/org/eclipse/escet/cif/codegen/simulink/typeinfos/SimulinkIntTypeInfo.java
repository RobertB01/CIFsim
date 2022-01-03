//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.codegen.c89.typeinfos.C89IntTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/**
 * Simulink type generator for the integer type.
 *
 * <p>
 * Integer ranges are ignored in the Simulink target.
 * </p>
 */
public class SimulinkIntTypeInfo extends C89IntTypeInfo {
    /**
     * Constructor for {@link SimulinkIntTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     */
    public SimulinkIntTypeInfo(boolean genLocalFunctions, CifType cifType) {
        super(genLocalFunctions, cifType);
    }

    @Override
    public String getTargetType() {
        return "int_T";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof SimulinkIntTypeInfo;
    }

    @Override
    public int hashCode() {
        return SimulinkIntTypeInfo.class.hashCode();
    }
}
