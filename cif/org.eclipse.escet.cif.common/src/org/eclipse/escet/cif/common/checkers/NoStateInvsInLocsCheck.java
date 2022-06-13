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

package org.eclipse.escet.cif.common.checkers;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;

/** CIF check that does not allow state invariants in locations. */
public class NoStateInvsInLocsCheck extends CifCheck {
    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Skip location parameters.
        EObject parent = loc.eContainer();
        if (parent instanceof LocationParameter) {
            return;
        }

        // Check for violation.
        for (Invariant inv: loc.getInvariants()) {
            if (inv.getInvKind() == InvKind.STATE) {
                if (loc.getName() != null) {
                    violations.add(loc, "location has a state invariant");
                } else {
                    violations.add((Automaton)loc.eContainer(), "automaton has a location with a state invariant");
                }
            }
        }
    }
}
