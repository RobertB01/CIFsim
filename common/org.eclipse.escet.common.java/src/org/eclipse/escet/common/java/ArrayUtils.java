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

package org.eclipse.escet.common.java;

/** Array helper methods. */
public class ArrayUtils {
    /** Constructor for the {@link ArrayUtils} class. */
    private ArrayUtils() {
        // Static class.
    }

    /**
     * Creates an array with the given elements.
     *
     * @param <T> The type of the elements of the resulting array.
     * @param elems The elements of the new array.
     * @return The newly created array.
     */
    @SafeVarargs
    public static <T> T[] array(T... elems) {
        return elems;
    }
}
