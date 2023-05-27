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

package org.eclipse.escet.cif.common.checkers.checks;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;

/** CIF check that does not allow urgent edges. */
public class EdgeNoUrgentCheck extends CifCheck {
    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        if (edge.isUrgent()) {
            violations.add(edge, "Edge is urgent");
        }
    }
}
