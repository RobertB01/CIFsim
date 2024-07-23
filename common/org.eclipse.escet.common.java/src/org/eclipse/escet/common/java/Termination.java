//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java;

/**
 * Termination request querying support.
 *
 * <p>
 * Cooperative termination involves a user requesting termination, and an application or library regularly checking
 * whether it has been requested. Termination checks should be done often enough to ensure that the application/library
 * is responsive enough to termination requests. It should not be done too often, as that may impact performance.
 * </p>
 *
 * <p>
 * A termination request can't be revoked. One termination is requested, it remains requested, and can't be cancelled.
 * </p>
 */
@FunctionalInterface
public interface Termination {
    /** A {@link Termination} instance that never indicates that termination has been requested. */
    public static final Termination NEVER = () -> false;

    /**
     * Query whether {@link Termination termination} has been requested.
     *
     * <p>
     * This method should return {@code false} for as long as termination has not been requested. Once termination has
     * been requested, it should return {@code true}. Since termination requests can't be cancelled, this method must
     * keep returning {@code true} once it has returned {@code true}.
     * </p>
     *
     * @return {@code true} if termination has been requested, {@code false} if it has not been requested.
     */
    public boolean isRequested();
}
