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

package org.eclipse.escet.cif.common;

/**
 * Event equality mode.
 *
 * <p>
 * Two events, referred to via different component parameters of the same type (component definition), refer to the same
 * event if those parameters are instantiated with the same component. If however they are instantiated with different
 * components, then the references point to different events. Since it can not always be determined from the via
 * component parameter references which case we have, we can either be {@link #OPTIMISTIC} or {@link #PESSIMISTIC}.
 * </p>
 *
 * <p>
 * Methods that need to determine equality of event reference expressions need the user to specify the event equality to
 * use. If the user knows that no events are referred to via parameters, the user may use {@code null} to indicate this.
 * </p>
 */
public enum EventEquality {
    /**
     * Optimistic event equality: considers two events referred to via different component parameters of the same type
     * (component definition) to be equal, given that the remainder of the reference is equal as well.
     */
    OPTIMISTIC,

    /**
     * Pessimistic event equality: considers two events referred to via different component parameters of the same type
     * (component definition) to be different, regardless of whether the remainder of the reference is equal as well.
     */
    PESSIMISTIC,
}
