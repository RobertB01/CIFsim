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

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;

/**
 * CIF check that does not allow 'if' updates on edges.
 *
 * @note This check is included in {@link EdgeOnlySimpleAssignmentsCheck}.
 */
public class EdgeNoIfUpdatesCheck extends CifCheck {
    @Override
    protected void preprocessIfUpdate(IfUpdate update, CifCheckViolations violations) {
        violations.add(update, "Edge has an 'if' update");
    }
}
