//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators.io;

/** Available kinds of I/O table entries. */
public enum IoKind {
    /** IO entry is used for input into the PLC. */
    INPUT("input"),

    /** IO entry is used for output from the PLC. */
    OUTPUT("output");

    /** Name of the IO kind. */
    public final String name;

    /**
     * Constructor of the {@link IoKind} class.
     *
     * @param name Name of the IO kind.
     */
    private IoKind(String name) {
        this.name = name;
    }

    /**
     * Get the IO kind expressed by the given string if possible.
     *
     * @param name Name to match to an IO kind.
     * @return The IO kind that could be matched or {@code null} if no match could be found.
     */
    public static IoKind getIoTypeByName(String name) {
        for (IoKind ioType: IoKind.values()) {
            if (name.equalsIgnoreCase(ioType.name)) {
                return ioType;
            }
        }
        return null;
    }
}
