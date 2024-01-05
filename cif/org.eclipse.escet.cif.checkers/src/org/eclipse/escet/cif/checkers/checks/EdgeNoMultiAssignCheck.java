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
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;

/**
 * CIF check that does not allow multi-assignments on edges. This check does not disallow multiple assignments on a
 * single edge.
 *
 * @note This check is included in {@link EdgeOnlySimpleAssignmentsCheck}.
 */
public class EdgeNoMultiAssignCheck extends CifCheck {
    @Override
    protected void preprocessAssignment(Assignment asgn, CifCheckViolations violations) {
        if (asgn.getAddressable() instanceof TupleExpression) {
            violations.add(asgn, "Edge has a multi-assignment");
        }
    }
}
