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

package org.eclipse.escet.cif.codegen;

/** Storage of a code fragment in a target language representing a data value or a reference to a data value. */
public interface DataValue {
    /**
     * Get a code fragment to access to the data itself. This method should always succeed.
     *
     * @return Code fragment accessing the data.
     */
    public abstract String getData();

    /**
     * Get a reference or pointer to the data.
     *
     * <p>
     * This method may fail. It is assumed that the context has sufficient information to decide whether it is safe to
     * call this method.
     * </p>
     *
     * @return Code fragment pointing to the data.
     */
    public abstract String getReference();

    /**
     * Is the data value a reference?
     *
     * <p>
     * Use this only when your code wants to generate different output depending on the kind of value. That is, the code
     * to generate adapts itself to this value being a reference or not. If you want to force having a reference (or a
     * data value), test for {@link #canBeReferenced} instead.
     * </p>
     *
     * @return {@code true} if the data is a reference, {@code false} if it is the value itself.
     */
    public abstract boolean isReferenceValue();

    /**
     * Can the data value be accessed as a reference?
     *
     * @return {@code true} if the data value can be accessed as a reference, else {@code false}.
     */
    public abstract boolean canBeReferenced();
}
