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

package org.eclipse.escet.cif.parser.ast.types;

import org.eclipse.escet.common.java.TextPosition;

/** Boolean type. */
public class ABoolType extends ACifType {
    /**
     * Constructor for the {@link ABoolType} class.
     *
     * @param position Position information.
     */
    public ABoolType(TextPosition position) {
        super(position);
    }
}
