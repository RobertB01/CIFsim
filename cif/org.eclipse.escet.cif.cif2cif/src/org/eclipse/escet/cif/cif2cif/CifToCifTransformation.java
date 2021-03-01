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

package org.eclipse.escet.cif.cif2cif;

import org.eclipse.escet.cif.metamodel.cif.Specification;

/** In-place CIF to CIF transformation. */
public interface CifToCifTransformation {
    /**
     * Performs the in-place transformation.
     *
     * @param spec The CIF specification for which to perform the transformation. The specification is modified
     *     in-place.
     * @throws CifToCifPreconditionException If a precondition is violated.
     */
    public void transform(Specification spec);
}
