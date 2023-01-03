//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

/**
 * The kind of ranged type range compatibility to check for. Used by the {@link CifTypeUtils#checkTypeCompat} method.
 */
public enum RangeCompat {
    /** Ignore type ranges. */
    IGNORE,

    /** Check for equal ranges. */
    EQUAL,

    /** Check for containment of the second range in the first range. */
    CONTAINED,

    /** Check for range overlap. */
    OVERLAP;
}
