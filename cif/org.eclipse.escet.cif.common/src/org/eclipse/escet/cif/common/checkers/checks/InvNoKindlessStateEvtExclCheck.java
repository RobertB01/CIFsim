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
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnAncestorMessage;
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnSelfMessage;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

/** CIF check that does not allow kindless state/event exclusion invariants (without a supervisory kind). */
public class InvNoKindlessStateEvtExclCheck extends CifCheck {
    @Override
    protected void preprocessInvariant(Invariant inv, CifCheckViolations violations) {
        if (inv.getInvKind() == InvKind.EVENT_NEEDS || inv.getInvKind() == InvKind.EVENT_DISABLES) {
            SupKind supKind = inv.getSupKind();
            if (supKind == SupKind.NONE) {
                // The closest named ancestor of the invariant is the invariant itself, or a location or a component.
                violations.add(inv, new ReportObjectTypeDescrMessage(), new IfReportOnAncestorMessage("has"),
                        new IfReportOnSelfMessage("is"),
                        new LiteralMessage("a kindless state/event exclusion invariant, lacking a supervisory kind"));
            }
        }
    }
}
