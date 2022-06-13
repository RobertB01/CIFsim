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

import org.eclipse.escet.cif.common.CifEdgeUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;

/**
 * CIF check that does not allow the use of event 'tau', whether explicit or implicit.
 *
 * @note This check is included in {@link OnlyEventsWithControllabilityCheck}.
 */
public class NoTauCheck extends CifCheck {
    @Override
    protected void preprocessTauExpression(TauExpression tauExpr, CifCheckViolations violations) {
        // Explicit tau.
        EdgeEvent edgeEvent = (EdgeEvent)tauExpr.eContainer();
        Edge edge = (Edge)edgeEvent.eContainer();
        Location loc = CifEdgeUtils.getSource(edge);
        if (loc.getName() != null) {
            violations.add(loc, "location has an edge with explicitly event \"tau\" on it");
        } else {
            violations.add((Automaton)loc.eContainer(), "automaton has an edge with explicitly event \"tau\" on it");
        }
    }

    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        // Implicit tau.
        if (edge.getEvents().isEmpty()) {
            Location loc = CifEdgeUtils.getSource(edge);
            if (loc.getName() != null) {
                violations.add(loc, "location has an edge with implicitly event \"tau\" on it");
            } else {
                violations.add((Automaton)loc.eContainer(),
                        "automaton has an edge with implicitly event \"tau\" on it");
            }
        }
    }
}
