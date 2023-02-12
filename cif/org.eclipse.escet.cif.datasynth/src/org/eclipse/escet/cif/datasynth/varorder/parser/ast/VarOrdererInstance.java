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

package org.eclipse.escet.cif.datasynth.varorder.parser.ast;

import org.eclipse.escet.common.java.TextPosition;

/** Variable order(s) or variable orderer(s). */
public abstract class VarOrderOrOrdererInstance {
    /** The position of the Variable order(s) or variable orderer(s). */
    public final TextPosition position;

    /**
     * Constructor for the {@link VarOrderOrOrdererInstance} class.
     *
     * @param position The position of the variable order(s) or variable orderer(s).
     */
    public VarOrderOrOrdererInstance(TextPosition position) {
        this.position = position;
    }
}
