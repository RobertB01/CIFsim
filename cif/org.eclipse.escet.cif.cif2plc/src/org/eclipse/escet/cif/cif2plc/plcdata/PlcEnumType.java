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

package org.eclipse.escet.cif.cif2plc.plcdata;

import java.util.List;

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.TextBox;

/** PLC enum type. */
public class PlcEnumType extends PlcType {
    /** The literals of the enum type. */
    public final List<String> literals;

    /**
     * Constructor for the {@link PlcEnumType} class.
     *
     * @param literals The literals of the enum type.
     */
    public PlcEnumType(List<String> literals) {
        this.literals = literals;
    }

    @Override
    public Box toBox() {
        return new TextBox("(%s)", String.join(", ", literals));
    }
}
