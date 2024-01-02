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

package org.eclipse.escet.cif.checkers.checks;

import static org.eclipse.escet.cif.common.CifEvalUtils.evalPred;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

/** CIF check that allows marker predicates in locations only if they can be evaluated statically. */
public class LocOnlyStaticEvalMarkerPredsCheck extends CifCheck {
    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Note that location parameters never have marker predicates.
        for (Expression marked: loc.getMarkeds()) {
            try {
                evalPred(marked, false, true);
            } catch (UnsupportedException e) {
                violations.add(marked, "Location has a marker predicate that cannot be evaluated statically");
            } catch (CifEvalException e) {
                Expression reportExpr = (e.expr != null) ? e.expr : marked;
                violations.add(reportExpr, "Location has a marker predicate that cannot be evaluated statically, "
                        + "as evaluating it results in an evaluation error");
            }
        }
    }
}
