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
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;

/**
 * CIF check that does not allow 'if' updates on edges.
 *
 * @note This check is included in {@link EdgeOnlySimpleAssignmentsCheck}.
 */
public class EdgeNoIfUpdatesCheck extends CifCheck {
    @Override
    protected void preprocessIfUpdate(IfUpdate update, CifCheckViolations violations) {
        // Determine whether this assignment is part of an edge update.
        EObject parent = update.eContainer();
        while (!(parent instanceof Edge) && !(parent instanceof SvgIn)) {
            parent = parent.eContainer();
        }

        if (parent instanceof Edge) {
            violations.add(update, "Edge has an 'if' update");
        }
    }
}
