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

package org.eclipse.escet.setext.parser.ast.regex;

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Regular expression character(s). */
public abstract class RegExChars extends RegEx {
    /**
     * Constructor for the {@link RegExChars} class.
     *
     * @param position Position information.
     */
    public RegExChars(Position position) {
        super(position);
    }
}
