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

import org.eclipse.escet.cif.common.checkers.supportcode.CifCheck;
import org.eclipse.escet.cif.common.checkers.supportcode.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;

/**
 * CIF check that allows updates on edges only if they are simple assignments, i.e., no 'if' updates, multi-assignments
 * and partial variable assignments.
 *
 * @note This check includes {@link EdgeNoIfUpdatesCheck}, {@link EdgeNoMultiAssignCheck} and
 *     {@link EdgeNoPartialVarAssignCheck}.
 */
public class EdgeOnlySimpleAssignmentsCheck extends CifCheck {
    /** No 'if' updates check. */
    private EdgeNoIfUpdatesCheck noIf = new EdgeNoIfUpdatesCheck();

    /** No multi-assignments check. */
    private EdgeNoMultiAssignCheck noMulti = new EdgeNoMultiAssignCheck();

    /** No partial variable assignments check. */
    private EdgeNoPartialVarAssignCheck noPartial = new EdgeNoPartialVarAssignCheck();

    @Override
    protected void preprocessIfUpdate(IfUpdate update, CifCheckViolations violations) {
        noIf.preprocessIfUpdate(update, violations);
    }

    @Override
    protected void preprocessAssignment(Assignment asgn, CifCheckViolations violations) {
        noMulti.preprocessAssignment(asgn, violations);
        noPartial.preprocessAssignment(asgn, violations);
    }
}
