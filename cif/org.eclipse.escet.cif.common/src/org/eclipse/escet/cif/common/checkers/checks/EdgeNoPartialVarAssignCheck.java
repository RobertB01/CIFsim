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
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;

/**
 * CIF check that does not allow partial variable assignments on edges.
 *
 * @note This check is included in {@link EdgeOnlySimpleAssignmentsCheck}.
 */
public class EdgeNoPartialVarAssignCheck extends CifCheck {
    @Override
    protected void preprocessAssignment(Assignment asgn, CifCheckViolations violations) {
        if (asgn.getAddressable() instanceof ProjectionExpression) {
            // Report violation on the closest named ancestor of the assignment: a location or an automaton.
            violations.add(asgn, new ReportObjectTypeDescrMessage(),
                    new LiteralMessage("has an edge with a partial variable assignment"));
        }
    }
}
