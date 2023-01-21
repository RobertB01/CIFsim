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
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnAncestorMessage;
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnSelfMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;

/** CIF check that does not allow urgent locations. */
public class LocNoUrgentCheck extends CifCheck {
    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Note that location parameters are never urgent.
        if (loc.isUrgent()) {
            // Report violation on the location, or on its automaton in case the location has no name.
            violations.add(loc, new ReportObjectTypeDescrMessage(),
                    new IfReportOnAncestorMessage("has an urgent location"), new IfReportOnSelfMessage("is urgent"));
        }
    }
}
