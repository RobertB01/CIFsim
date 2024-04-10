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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;

/**
 * CIF check that does not allow partial variable assignments on edges.
 *
 * @note This check is included in {@link EdgeOnlySimpleAssignmentsCheck}.
 */
public class EdgeNoPartialVarAssignCheck extends CifCheck {
    @Override
    protected void preprocessAssignment(Assignment asgn, CifCheckViolations violations) {
        // Skip assignments that are not part of an edge update.
        EObject parent = asgn.eContainer();
        while (!(parent instanceof Edge) && !(parent instanceof SvgIn)) {
            parent = parent.eContainer();
        }
        if (!(parent instanceof Edge)) {
            return;
        }

        // Check addressable of the assignment on an edge.
        checkAddressable(asgn, asgn.getAddressable(), violations);
    }

    /**
     * Check an addressable.
     *
     * @param asgn The assignment.
     * @param addressable The (part of the) addressable of the assignment to check.
     * @param violations The violations collected so far. Is extended in-place.
     */
    private void checkAddressable(Assignment asgn, Expression addressable, CifCheckViolations violations) {
        if (addressable instanceof TupleExpression tupleAddr) {
            // Check each part of the multi-assignment.
            for (Expression field: tupleAddr.getFields()) {
                checkAddressable(asgn, field, violations);
            }
        } else if (addressable instanceof ProjectionExpression) {
            // Report violation for the projection.
            violations.add(addressable, "Edge has a partial variable assignment");
        }
    }
}
