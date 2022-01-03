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

/** Terminal description with end user readable text. */
public class TerminalDescription extends SeTextObject {
    /** The description text. */
    public final String description;

    /**
     * Constructor for the {@link TerminalDescription} class.
     *
     * @param description The description text.
     * @param position Position information.
     */
    public TerminalDescription(String description, Position position) {
        super(position);
        this.description = description;
    }
}
