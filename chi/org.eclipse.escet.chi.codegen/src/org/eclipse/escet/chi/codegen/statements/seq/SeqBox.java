//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen.statements.seq;

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Sequential statement stored in a box. */
public class SeqBox extends Seq {
    /** Box containing the statement. */
    private final Box box;

    /**
     * Constructor of the {@link SeqBox} class.
     *
     * @param box Box to store.
     * @param chiobj Position of the statement.
     */
    public SeqBox(Box box, PositionObject chiobj) {
        super(chiobj);
        this.box = box;
    }

    @Override
    public Box boxify() {
        return box;
    }
}
