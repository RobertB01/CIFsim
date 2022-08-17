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
import org.eclipse.escet.cif.common.checkers.supportcode.CifCheck;
import org.eclipse.escet.cif.common.checkers.supportcode.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.common.java.Assert;

/**
 * CIF check that does not allow 'if' updates on edges.
 *
 * @note This check is included in {@link EdgeOnlySimpleAssignmentsCheck}.
 */
public class EdgeNoIfUpdatesCheck extends CifCheck {
    @Override
    protected void preprocessIfUpdate(IfUpdate update, CifCheckViolations violations) {
        // Get location.
        EObject ancestor = update;
        while (!(ancestor instanceof Location)) {
            ancestor = ancestor.eContainer();
        }
        Assert.check(ancestor instanceof Location);
        Location loc = (Location)ancestor;

        // Report violation.
        if (loc.getName() != null) {
            violations.add(loc, "location has an edge with an 'if' update");
        } else {
            violations.add((Automaton)loc.eContainer(), "automaton has an edge with an 'if' update");
        }
    }
}
