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

package org.eclipse.escet.cif.parser.ast.automata;

import org.eclipse.escet.cif.parser.ast.ACifObject;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** An update of an edge. */
public abstract class AUpdate extends ACifObject {
    /**
     * Constructor for the {@link AUpdate} class.
     *
     * @param position Position information.
     */
    public AUpdate(Position position) {
        super(position);
    }
}
