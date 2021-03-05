//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.parser.ast.iodecls.svg;

import org.eclipse.escet.cif.parser.ast.ACifObject;

/** The event to choose for a CIF/SVG {@link ASvgIn input mapping}. */
public abstract class ASvgInEvent extends ACifObject {
    /** Constructor for the {@link ASvgInEvent} class. */
    public ASvgInEvent() {
        super(null);
    }
}
