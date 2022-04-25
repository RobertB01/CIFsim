//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm;

/** Label base class. */
public class Label {
    /** The name of the label used for printing. */
    private final String name;

    /**
     * Constructor for the {@link Label} class, with name of the label.
     *
     * @param name The name of the label.
     */
    public Label(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
