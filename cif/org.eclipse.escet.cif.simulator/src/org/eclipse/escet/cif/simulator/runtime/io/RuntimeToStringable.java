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

package org.eclipse.escet.cif.simulator.runtime.io;

/**
 * Interface for objects that can be converted to a textual representation, at runtime, by the
 * {@link RuntimeValueToString} class. The textual representations closely resemble the CIF ASCII syntax.
 */
public interface RuntimeToStringable {
    @Override
    public abstract String toString();
}
