//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifEvalUtils.evalPred;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

/** CIF check that allows guards of edges only if they can be evaluated statically. */
public class EdgeOnlyStaticEvalGuardPredsCheck extends CifCheck {
    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        for (Expression guard: edge.getGuards()) {
            try {
                evalPred(guard, false, true);
            } catch (UnsupportedException e) {
                violations.add(guard, "Edge has a guard that cannot be evaluated statically");
            } catch (CifEvalException e) {
                Expression reportExpr = (e.expr != null) ? e.expr : guard;
                violations.add(reportExpr, "Edge has a guard that cannot be evaluated statically, "
                        + "as evaluating it results in an evaluation error");
            }
        }
    }
}
