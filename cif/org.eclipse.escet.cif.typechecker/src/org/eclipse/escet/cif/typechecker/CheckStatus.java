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

package org.eclipse.escet.cif.typechecker;

/** The completeness status of type checking of a {@link SymbolTableEntry}. */
public enum CheckStatus {
    /** Type checking has not been started, or is currently in progress. */
    NONE,

    /**
     * Type checking 'for use' is completed. Remaining type checking has not been started or is currently in progress.
     */
    USE,

    /**
     * Type checking 'for use' failed. Resolving references to the entry will also fail, potentially causing a ripple
     * effect.
     */
    USE_FAILED,

    /** Type checking is fully completed. */
    FULL;
}
