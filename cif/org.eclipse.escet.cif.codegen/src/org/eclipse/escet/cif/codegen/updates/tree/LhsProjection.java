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

package org.eclipse.escet.cif.codegen.updates.tree;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;

/** Projection of a left hand side. */
public abstract class LhsProjection {
    /**
     * Get the type of the container that is projected.
     *
     * @return The type of the container that is projected.
     */
    public abstract CifType getContainerType();

    /**
     * Get the type of the selected part.
     *
     * @return The type of the selected part.
     */
    public abstract CifType getPartType();

    @Override
    public abstract String toString();
}
