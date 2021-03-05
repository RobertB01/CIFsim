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

package org.eclipse.escet.tooldef.typechecker;

/** Exception to indicate an import cycle is detected. */
public class ImportCycleException extends RuntimeException {
    /** The globally unique (absolute) reference text of the imported file that resulted in the cycle. */
    public final String absSrcRef;

    /** The text describing the import cycle that was found. */
    public final String cycleTxt;

    /**
     * Constructor for the {@link ImportCycleException} class.
     *
     * @param absSrcRef The globally unique (absolute) reference text of the imported file that resulted in the cycle.
     * @param cycleTxt The text describing the import cycle that was found.
     */
    public ImportCycleException(String absSrcRef, String cycleTxt) {
        this.absSrcRef = absSrcRef;
        this.cycleTxt = cycleTxt;
    }
}
