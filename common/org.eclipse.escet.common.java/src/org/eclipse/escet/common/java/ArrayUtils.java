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

package org.eclipse.escet.common.java;

import java.lang.reflect.Array;

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

    /**
     * Reverses the given arrow.
     *
     * @param <T> The type of the elements of the array.
     * @param array The array to reverse. Is not modified.
     * @return The reverse array.
     */
    public static <T> T[] reverse(T[] array) {
        int n = array.length;
        @SuppressWarnings("unchecked")
        T[] reversed = (T[])Array.newInstance(array.getClass().getComponentType(), n);
        for (int i = 0; i < n; i++) {
            reversed[n - i - 1] = array[i];
        }
        return reversed;
    }
}
