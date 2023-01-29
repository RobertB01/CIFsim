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

import java.util.List;

import org.eclipse.escet.common.java.TextPosition;

/** Variable orders or variable orderers. */
public class VarOrderOrOrdererMultiInstance extends VarOrderOrOrdererInstance {
    /** The variable orders or variable orderers. */
    public final List<VarOrderOrOrdererInstance> instances;

    /**
     * Constructor for the {@link VarOrderOrOrdererMultiInstance} class.
     *
     * @param position The position of the variable order(s) or variable orderer(s).
     * @param instances The variable orders or variable orderers.
     */
    public VarOrderOrOrdererMultiInstance(TextPosition position, List<VarOrderOrOrdererInstance> instances) {
        super(position);
        this.instances = instances;
    }
}
