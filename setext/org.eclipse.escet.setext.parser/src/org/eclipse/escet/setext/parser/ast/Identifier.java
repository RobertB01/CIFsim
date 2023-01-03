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

package org.eclipse.escet.setext.parser.ast;

import org.eclipse.escet.common.java.TextPosition;

/** Plain identifier token. */
public class Identifier extends SeTextObject {
    /** Identifier, without any {@code $} characters. */
    public final String id;

    /**
     * Constructor for the {@link Identifier} class.
     *
     * @param id Identifier. May include {@code $} characters.
     * @param position Position information.
     */
    public Identifier(String id, TextPosition position) {
        super(position);
        this.id = id.replace("$", "");
    }
}
