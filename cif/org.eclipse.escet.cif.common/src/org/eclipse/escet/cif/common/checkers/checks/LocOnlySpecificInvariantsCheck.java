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

import java.util.EnumSet;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnAncestorMessage;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;

/** CIF check that allows invariants in locations only if they are of specific invariant kinds. */
public class LocOnlySpecificInvariantsCheck extends CifCheck {
    /** Set with invariant kinds for state event exclusion invariants. */
    private static final EnumSet<InvKind> STATE_EVENT_EXCL_KINDS = EnumSet.of(InvKind.EVENT_DISABLES,
            InvKind.EVENT_NEEDS);

    /** Whether to allow state invariants in locations. */
    private final boolean allowStateInvariants;

    /** Whether to allow state/event exclusion invariants in locations. */
    private final boolean allowStateEventExclusionInvariants;

    /**
     * Constructor of the {@link LocOnlySpecificInvariantsCheck} class.
     *
     * @param allowStateInvariants Whether to allow state invariants in locations.
     * @param allowStateEventExclusionInvariants Whether to allow state/event exclusion invariants in locations.
     */
    public LocOnlySpecificInvariantsCheck(boolean allowStateInvariants, boolean allowStateEventExclusionInvariants) {
        this.allowStateInvariants = allowStateInvariants;
        this.allowStateEventExclusionInvariants = allowStateEventExclusionInvariants;
    }

    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Note that location parameters never have invariants.
        for (Invariant inv: loc.getInvariants()) {
            if (!allowStateInvariants && inv.getInvKind() == InvKind.STATE) {
                // Report violation on the location, or on its automaton in case the location has no name.
                violations.add(loc, new ReportObjectTypeDescrMessage(), new LiteralMessage("has"),
                        new IfReportOnAncestorMessage("a location with"), new LiteralMessage("a state invariant"));
            }
            if (!allowStateEventExclusionInvariants && STATE_EVENT_EXCL_KINDS.contains(inv.getInvKind())) {
                // Report violation on the location, or on its automaton in case the location has no name.
                violations.add(loc, new ReportObjectTypeDescrMessage(), new LiteralMessage("has"),
                        new IfReportOnAncestorMessage("a location with"),
                        new LiteralMessage("a state/event exclusion invariant"));
            }
        }
    }
}
