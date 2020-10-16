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

package org.eclipse.escet.cif.codegen.c89.typeinfos;

/** Interface for additional information that can be queried from a type. */
public interface C89TypeInfo {
    /**
     * Whether the type can decide equality by performing a raw memcmp on the object at runtime.
     *
     * @return {@code true} if memcmp on the object produces valid results, {@code false} otherwise.
     */
    public abstract boolean supportRawMemCmp();

    /**
     * Whether the type is small enough to use values.
     *
     * @return {@code true} if the type uses values directly, {@code false} if the type uses references when passing
     *     data around.
     */
    public abstract boolean useValues();

    /**
     * Get the name of the 'type print' function of the type.
     *
     * @param rawString Whether to output the string as-is, otherwise escape \n, \t, \", and \.
     * @return The name of the 'type print' function.
     */
    public abstract String getTypePrintName(boolean rawString);
}
