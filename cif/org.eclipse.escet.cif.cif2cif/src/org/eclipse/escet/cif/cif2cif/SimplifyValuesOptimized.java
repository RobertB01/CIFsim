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

/**
 * In-place transformation that simplifies CIF specifications, exactly as the {@link SimplifyValues} transformation, but
 * with optimizations for literal expressions.
 */
public class SimplifyValuesOptimized extends SimplifyValues {
    /** Constructor for the {@link SimplifyValuesOptimized} class. */
    public SimplifyValuesOptimized() {
        super(true, true);
    }
}
