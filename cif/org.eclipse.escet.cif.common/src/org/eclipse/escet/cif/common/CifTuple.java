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

package org.eclipse.escet.cif.common;

import java.util.ArrayList;

/**
 * CIF tuple value. Used to represent tuple values for the {@link CifEvalUtils} class. Note that this class is not
 * strongly typed for the elements.
 */
public class CifTuple extends ArrayList<Object> {
    /** Constructor for the {@link CifTuple} class. */
    public CifTuple() {
        // Creates an empty tuple.
    }

    /**
     * Constructor for the {@link CifTuple} class.
     *
     * @param tuple The tuple to copy.
     */
    public CifTuple(CifTuple tuple) {
        super(tuple);
    }

    /**
     * Constructor for the {@link CifTuple} class.
     *
     * @param initialCapacity The initial capacity of the tuple.
     */
    public CifTuple(int initialCapacity) {
        super(initialCapacity);
    }
}
