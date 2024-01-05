//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

/** Reference name token. */
public class Name extends SeTextObject {
    /** Reference name, without any {@code $} characters. */
    public final String name;

    /**
     * Constructor for the {@link Name} class.
     *
     * @param name Reference name. May include {@code $} characters.
     * @param position Position information.
     */
    public Name(String name, TextPosition position) {
        super(position);
        this.name = name.replace("$", "");
    }
}
