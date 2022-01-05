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

package org.eclipse.escet.setext.parser.ast;

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Base class of SeText classes. */
public abstract class SeTextObject {
    /** Position information, or {@code null} if not available. */
    public Position position;

    /**
     * Constructor for the {@link SeTextObject} class.
     *
     * @param position Position information, or {@code null} if not available.
     */
    public SeTextObject(Position position) {
        this.position = position;
    }
}
