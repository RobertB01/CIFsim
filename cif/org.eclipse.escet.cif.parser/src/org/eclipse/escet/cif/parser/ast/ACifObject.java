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

package org.eclipse.escet.cif.parser.ast;

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Base class of all classes in the CIF parser AST. */
public abstract class ACifObject {
    /** Position information. */
    public final Position position;

    /**
     * Constructor for the {@link ACifObject} class.
     *
     * @param position Position information, or {@code null} if not available.
     */
    public ACifObject(Position position) {
        this.position = position;
    }
}
