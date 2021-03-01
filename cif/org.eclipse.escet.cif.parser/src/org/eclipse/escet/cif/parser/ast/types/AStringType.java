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

package org.eclipse.escet.cif.parser.ast.types;

import org.eclipse.escet.common.position.metamodel.position.Position;

/** String type. */
public class AStringType extends ACifType {
    /**
     * Constructor for the {@link AStringType} class.
     *
     * @param position Position information.
     */
    public AStringType(Position position) {
        super(position);
    }
}
