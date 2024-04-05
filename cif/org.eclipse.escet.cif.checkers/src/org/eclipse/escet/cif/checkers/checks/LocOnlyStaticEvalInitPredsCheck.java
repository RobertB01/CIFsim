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

import static org.eclipse.escet.cif.common.CifEvalUtils.evalPred;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

/** CIF check that allows initialization predicates in locations only if they can be evaluated statically. */
public class LocOnlyStaticEvalInitPredsCheck extends CifCheck {
    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Note that location parameters never have initialization predicates.
        for (Expression initial: loc.getInitials()) {
            try {
                evalPred(initial, true, true);
            } catch (UnsupportedException e) {
                violations.add(initial, "Location has an initialization predicate that cannot be evaluated statically");
            } catch (CifEvalException e) {
                Expression reportExpr = (e.expr != null) ? e.expr : initial;
                violations.add(reportExpr, "Location has an initialization predicate that cannot be evaluated "
                        + "statically, as evaluating it results in an evaluation error");
            }
        }
    }
}
