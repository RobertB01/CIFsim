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
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;

/**
 * CIF check that does not allow the use of event 'tau', whether explicit or implicit.
 *
 * @note This check is included in {@link EventOnlyWithControllabilityCheck}.
 */
public class EventNoTauCheck extends CifCheck {
    @Override
    protected void preprocessTauExpression(TauExpression tauExpr, CifCheckViolations violations) {
        violations.add(tauExpr, "Edge has an explicit 'tau' event");
    }

    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        if (edge.getEvents().isEmpty()) {
            violations.add(edge, "Edge has an implicit 'tau' event");
        }
    }
}
