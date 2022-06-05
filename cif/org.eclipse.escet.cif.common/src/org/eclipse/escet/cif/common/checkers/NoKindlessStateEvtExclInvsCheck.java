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

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;

/** CIF check that does not allow kindless state/event exclusion invariants. */
public class NoKindlessStateEvtExclInvsCheck extends CifCheck {
    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        for (Invariant inv: comp.getInvariants()) {
            if (inv.getInvKind() == InvKind.EVENT_NEEDS || inv.getInvKind() == InvKind.EVENT_DISABLES) {
                SupKind supKind = inv.getSupKind();
                if (supKind == SupKind.NONE) {
                    addViolation(comp, "component has a kindless state/event exclusion invariant");
                }
            }
        }
    }

    @Override
    protected void preprocessLocation(Location loc) {
        for (Invariant inv: loc.getInvariants()) {
            if (inv.getInvKind() == InvKind.EVENT_NEEDS || inv.getInvKind() == InvKind.EVENT_DISABLES) {
                SupKind supKind = inv.getSupKind();
                if (supKind == SupKind.NONE) {
                    if (loc.getName() != null) {
                        addViolation(loc, "location has a kindless state/event exclusion invariant");
                    } else {
                        addViolation((Automaton)loc.eContainer(),
                                "automaton has a location with a kindless state/event exclusion invariant");
                    }
                }
            }
        }
    }
}
