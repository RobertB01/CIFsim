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

package org.eclipse.escet.cif.codegen.typeinfos;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/** Type information of a container type. */
public abstract class ContainerTypeInfo extends TypeInfo {
    /** Type information of the child types. */
    public final TypeInfo[] childInfos;

    /**
     * Constructor of the {@link ContainerTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param childInfos Child type information.
     */
    public ContainerTypeInfo(CifType cifType, TypeInfo[] childInfos) {
        super(cifType);
        this.childInfos = childInfos;
    }

    /**
     * Get the size of the container, if it can be statically determined.
     *
     * @return Size of the container, or {@code -1} if it cannot be statically determined.
     */
    public abstract int getSize();

    /**
     * Normalize the static index value of a projection operation on a statically fixed size container type.
     *
     * @param index Index value of the projection.
     * @return Non-negative index value of the projection, {@code -1} if the index value cannot be statically computed,
     *     or {@code -2} if the index value is out of valid projection range.
     */
    public int normalizeIndex(int index) {
        return normalizeIndex(index, getSize());
    }

    /**
     * Normalize the static index value of a projection operation on a statically fixed size container type.
     *
     * @param index Index value of the projection.
     * @param size Length of the container, or {@code -1} if size is not statically known.
     * @return Non-negative index value of the projection, {@code -1} if the index value cannot be statically computed,
     *     or {@code -2} if the index value is out of valid projection range.
     */
    public static int normalizeIndex(int index, int size) {
        if (size <= 0) {
            return -1;
        }
        if (index >= 0) {
            if (index < size) {
                return index;
            }
            return -2;
        }
        // Negative index, convert to positive index.
        index = size + index;
        if (index >= 0) {
            return index;
        }
        return -2;
    }
}
