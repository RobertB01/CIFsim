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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;

/**
 * CIF check that allows automata only if they have exactly one initial location. Automata for which this cannot be
 * determined statically, are also not allowed.
 */
public class AutOnlyWithOneInitLocCheck extends CifCheck {
    /**
     * The number of initial locations found for the automaton being checked. Only valid while checking an automaton. Is
     * set to {@code -1} to disable this check due to evaluation errors in initialization predicates.
     */
    private int initLocCount;

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        // Reset initial locations counter.
        initLocCount = 0;
    }

    @Override
    protected void postprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        // There must be exactly one initial location.
        if (initLocCount == 0) {
            violations.add(aut, "Automaton has no initial location");
        } else if (initLocCount > 1) {
            violations.add(aut, "Automaton has multiple initial locations");
        } // Skip if check is disabled (negative value).
    }

    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Skip location parameters.
        EObject parent = loc.eContainer();
        if (parent instanceof LocationParameter) {
            return;
        }

        // Determine whether location is an initial location by statically evaluating the location's initialization
        // predicates.
        boolean initial = !loc.getInitials().isEmpty();
        for (Expression initPred: loc.getInitials()) {
            String errMsg = null;
            Expression errExpr = initPred;
            try {
                initial &= evalPred(initPred, true, true);
            } catch (UnsupportedException e) {
                // Can only fail if there is at least one predicate.
                errMsg = "as one of its initialization predicates cannot be statically evaluated";
            } catch (CifEvalException e) {
                if (e.expr != null) {
                    errExpr = e.expr;
                }
                // Can only fail if there is at least one predicate.
                errMsg = "as evaluating one of its initialization predicates results in an evaluation error";
            }
            if (errMsg != null) {
                // Report violation.
                violations.add(errExpr, "Failed to determine whether a location is an initial location, " + errMsg);

                // Disable initial location count checking.
                initLocCount = -1;
            }
        }

        // Update number of initial locations, if not disabled for this automaton.
        if (initial && initLocCount != -1) {
            initLocCount++;
        }
    }
}
