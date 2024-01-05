//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2cif;

/** In-place transformation that removes print and print file declarations. */
public class RemovePrintDecls extends RemoveIoDecls {
    /** Constructor for the {@link RemovePrintDecls} class. */
    public RemovePrintDecls() {
        super(true, false);
    }
}
