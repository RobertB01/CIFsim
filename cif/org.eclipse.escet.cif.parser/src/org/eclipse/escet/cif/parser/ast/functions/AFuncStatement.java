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

package org.eclipse.escet.cif.parser.ast.functions;

import org.eclipse.escet.cif.parser.ast.ACifObject;
import org.eclipse.escet.common.java.TextPosition;

/** A statement of an internal function. */
public abstract class AFuncStatement extends ACifObject {
    /**
     * Constructor for the {@link AFuncStatement} class.
     *
     * @param position Position information.
     */
    public AFuncStatement(TextPosition position) {
        super(position);
    }
}
