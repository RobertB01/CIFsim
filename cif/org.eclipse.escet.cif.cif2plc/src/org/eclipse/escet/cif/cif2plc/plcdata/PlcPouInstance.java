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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.TextBox;

/** PLC POU instance. */
public class PlcPouInstance extends PlcObject {
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

    @Override
    public Box toBox() {
        return toBox(null);
    }

    /**
     * Returns a {@link Box} representation of this object.
     *
     * @param taskName The name of the task on which to instantiate the POU, or {@code null} if not applicable.
     * @return A {@link Box} representation of this object.
     */
    public Box toBox(String taskName) {
        String taskTxt = (taskName == null) ? "" : fmt(" WITH %s", taskName);
        return new TextBox("PROGRAM %s%s: %s;", name, taskTxt, pou.name);
    }
}
