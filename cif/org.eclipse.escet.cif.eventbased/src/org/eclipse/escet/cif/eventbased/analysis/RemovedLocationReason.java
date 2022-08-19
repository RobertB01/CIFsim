//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

/** Reason for why a location was removed during synthesis. */
public enum RemovedLocationReason {
    /** Location is not marked and locally found to be a deadlock state. */
    isBlocking,

    /** Location was found to be non-coreachable. */
    isNotCoreachable,

    /** Location has become non-reachable. */
    isNotReachable
}
