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
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;

/**
 * CIF check that does not allow the use of event 'tau', whether explicit or implicit.
 *
 * @note This check is included in {@link EventOnlyWithControllabilityCheck}.
 */
public class EventNoTauCheck extends CifCheck {
    @Override
    protected void preprocessTauExpression(TauExpression tauExpr, CifCheckViolations violations) {
        // Explicit tau.
        // Report violation on the closest named ancestor of the tau expression: a location or an automaton.
        violations.add(tauExpr, new ReportObjectTypeDescrMessage(),
                new LiteralMessage("has an edge with explicitly event \"tau\" on it"));
    }

    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        // Implicit tau.
        if (edge.getEvents().isEmpty()) {
            // Report violation on the closest named ancestor of the edge: a location or an automaton.
            violations.add(edge, new ReportObjectTypeDescrMessage(),
                    new LiteralMessage("has an edge with implicitly event \"tau\" on it"));
        }
    }
}
