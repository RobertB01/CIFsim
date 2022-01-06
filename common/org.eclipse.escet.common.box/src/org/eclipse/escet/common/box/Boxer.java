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

package org.eclipse.escet.common.box;

/**
 * Interface for classes that provide {@link Boxable} functionality for classes that themselves can not implement
 * {@link Boxable}, such as standard Java classes, closed source third party library classes, etc.
 *
 * @param <T> The type of objects that this boxer can box.
 */
public interface Boxer<T> {
    /** Default, thread-safe {@link Boxer} that can box {@link Boxable} objects. */
    public static Boxer<Boxable> DEFAULT = new Boxer<>() {
        @Override
        public Box box(Boxable obj) {
            return obj.toBox();
        }
    };

    /**
     * Returns a {@link Box} representation of the given object.
     *
     * @param obj The object for which to return a {@link Box} representation.
     * @return A {@link Box} representation of the object.
     */
    public Box box(T obj);
}
