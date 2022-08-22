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

package org.eclipse.escet.cif.common.checkers.checks;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;

/** CIF check that does not allow urgent edges. */
public class EdgeNoUrgentCheck extends CifCheck {
    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        if (edge.isUrgent()) {
            Location loc = (Location)edge.eContainer();
            if (loc.getName() != null) {
                violations.add(loc, "location has an urgent edge");
            } else {
                violations.add((Automaton)loc.eContainer(), "automaton has an urgent edge");
            }
        }
    }
}
