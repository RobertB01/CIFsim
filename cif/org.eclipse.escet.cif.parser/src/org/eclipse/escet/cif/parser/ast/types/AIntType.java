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

/** Ranged integer type. */
public class AIntType extends ACifType {
    /** The range of the integer type, or {@code null}. */
    public final ARange range;

    /**
     * Constructor for the {@link AIntType} class.
     *
     * @param range The range of the integer type, or {@code null}.
     * @param position Position information.
     */
    public AIntType(ARange range, Position position) {
        super(position);
        this.range = range;
    }
}
