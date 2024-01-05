//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;

/**
 * CIF check that allows events only if they are declared as controllable or uncontrollable.
 *
 * @note This check includes {@link EventNoTauCheck}.
 */
public class EventOnlyWithControllabilityCheck extends CifCheck {
    @Override
    protected void preprocessEvent(Event event, CifCheckViolations violations) {
        if (event.getControllable() == null) {
            violations.add(event, "Event is not declared as controllable or uncontrollable");
        }
    }

    @Override
    protected void preprocessTauExpression(TauExpression tauExpr, CifCheckViolations violations) {
        // Explicit tau.
        violations.add(tauExpr, "Edge has an explicit 'tau' event, which is not controllable or uncontrollable");
    }

    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        // Implicit tau.
        if (edge.getEvents().isEmpty()) {
            violations.add(edge, "Edge has an implicit 'tau' event, which is not controllable or uncontrollable");
        }
    }
}
