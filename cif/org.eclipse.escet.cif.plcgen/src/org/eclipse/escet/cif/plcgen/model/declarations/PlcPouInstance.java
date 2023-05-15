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

package org.eclipse.escet.cif.plcgen.model.declarations;

/** PLC POU instance. */
public class PlcPouInstance {
    /** The name of the POU instance. */
    public final String name;

    /** The POU being instantiated. */
    public final PlcPou pou;

    /**
     * Constructor for the {@link PlcPouInstance} class.
     *
     * @param name The name of the POU instance.
     * @param pou The POU being instantiated.
     */
    public PlcPouInstance(String name, PlcPou pou) {
        this.name = name;
        this.pou = pou;
    }
}
