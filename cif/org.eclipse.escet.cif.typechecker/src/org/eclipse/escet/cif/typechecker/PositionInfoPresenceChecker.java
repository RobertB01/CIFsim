//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFPath;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Check a CIF specification to ensure all {@link PositionObject} instances have position information. */
class PositionInfoPresenceChecker extends CifWalker {
    /**
     * Check the given specification.
     *
     * @param spec The specification to check.
     */
    public void check(Specification spec) {
        walkSpecification(spec);
    }

    @Override
    protected void preprocessPositionObject(PositionObject posObj) {
        if (posObj.getPosition() == null) {
            throw new AssertionError(new EMFPath(posObj, null).toString());
        }
    }
}
