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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.common.java.Assert;

/**
 * CIF check that does not allow multi-assignments on edges. This check does not disallow multiple assignments on a
 * single edge.
 *
 * @note This check is included in {@link OnlySimpleAssignmentsCheck}.
 */
public class NoMultiAssignOnEdgesCheck extends CifCheck {
    @Override
    protected void preprocessAssignment(Assignment asgn) {
        if (asgn.getAddressable() instanceof TupleExpression) {
            // Get location.
            EObject ancestor = asgn;
            while (!(ancestor instanceof Location)) {
                ancestor = ancestor.eContainer();
            }
            Assert.check(ancestor instanceof Location);
            Location loc = (Location)ancestor;

            // Report violation.
            if (loc.getName() != null) {
                addViolation(loc, "location has an edge with a multi-assignment");
            } else {
                addViolation((Automaton)loc.eContainer(), "automaton has an edge with a multi-assignment");
            }
        }
    }
}
