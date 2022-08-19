//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.analysis;

/** Information about a removed location. */
public class RemovedLocationInfo {
    /** Removed location. */
    public final int loc;

    /**
     * The reason why the location was removed. Can be because it was found to be non-coreachable, non-reachable or it
     * was locally detected to be a deadlock non-marked location.
     */
    public final RemovedLocationReason reason;

    /**
     * Constructor of the {@link RemovedLocationInfo} class.
     *
     * @param loc Removed location.
     * @param reason The reason why the location was removed.
     */
    public RemovedLocationInfo(int loc, RemovedLocationReason reason) {
        this.loc = loc;
        this.reason = reason;
    }
}
