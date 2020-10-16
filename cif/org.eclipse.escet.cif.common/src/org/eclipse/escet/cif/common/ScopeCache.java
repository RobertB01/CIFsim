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

import java.util.LinkedHashMap;
import java.util.Set;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Cache results of the {@link CifScopeUtils#getSymbolNamesForScope} method.
 *
 * <p>
 * Keeps a set of symbol names associated with each computed scope.
 * </p>
 */
public class ScopeCache extends LinkedHashMap<PositionObject, Set<String>> {
    // Nothing needs to be done.
}
