//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
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
            violations.add(event, new ReportObjectTypeDescrMessage(),
                    new LiteralMessage("is not declared as controllable or uncontrollable"));
        }
    }

    @Override
    protected void preprocessTauExpression(TauExpression tauExpr, CifCheckViolations violations) {
        // Explicit tau.
        // Report violation on the closest named ancestor of the tau expression: a location or an automaton.
        violations.add(tauExpr, new ReportObjectTypeDescrMessage(), new LiteralMessage(
                "has an edge with an explicit \"tau\" event on it, which is not controllable or uncontrollable"));
    }

    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        // Implicit tau.
        if (edge.getEvents().isEmpty()) {
            // Report violation on the closest named ancestor of the edge: a location or an automaton.
            violations.add(edge, new ReportObjectTypeDescrMessage(), new LiteralMessage(
                    "has an edge with an implicit \"tau\" event on it, which is not controllable or uncontrollable"));
        }
    }
}