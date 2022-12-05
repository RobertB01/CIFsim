//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks.invcheck;

import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnAncestorMessage;
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnSelfMessage;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

/** Storage of a subset of disallowed invariants, also handles checking and reporting. */
public class DisallowedInvariantsSubset {
    /** Disallowed invariant subset on the supervisory kind aspect. */
    private final NoInvariantSupKind noSupKind;

    /** Disallowed invariant subset on the invariant kind aspect. */
    private final NoInvariantKind noInvKind;

    /** Disallowed invariant subset on the place kind aspect. */
    private final NoInvariantPlaceKind noPlaceKind;

    /**
     * Constructor of the {@link DisallowedInvariantsSubset} class.
     *
     * @param noSupKind Disallowed invariant subset on the supervisory kind aspect.
     * @param noInvKind Disallowed invariant subset on the invariant kind aspect.
     * @param noPlaceKind Disallowed invariant subset on the place kind aspect.
     */
    public DisallowedInvariantsSubset(NoInvariantSupKind noSupKind, NoInvariantKind noInvKind,
            NoInvariantPlaceKind noPlaceKind)
    {
        this.noSupKind = noSupKind;
        this.noInvKind = noInvKind;
        this.noPlaceKind = noPlaceKind;
    }

    /**
     * Get the report relevance of the stored subset of invariants.
     *
     * @return The report relevance of the stored subset of invariants.
     */
    public int getReportRelevance() {
        return noSupKind.getReportRelevance() + noInvKind.getReportRelevance() + noPlaceKind.getReportRelevance();
    }

    /**
     * Compute whether this disallowed invariant subset includes the invariant with the given aspect values.
     *
     * @param supKind Supervisory aspect kind to test.
     * @param invKind Invariant aspect kind to test.
     * @param placeKind Place aspect kind to test.
     * @return Whether an invariant with the combined given aspects is covered by this disallowed subset.
     */
    public boolean covers(SupKind supKind, InvKind invKind, PlaceKind placeKind) {
        return noSupKind.covers(supKind) && noInvKind.covers(invKind) && noPlaceKind.covers(placeKind);
    }

    /**
     * Report the provided invariant as a violation that is covered by this subset.
     *
     * @param inv Invariant to report.
     * @param violations The violations collected so far, is modified in-place.
     */
    public void report(Invariant inv, CifCheckViolations violations) {
        // Example: "plant state invariant in a location"
        // Some aspects may not have text, which makes the code somewhat complicated.
        String text = concatText(noSupKind.getReportText(), noInvKind.getReportText());
        text = concatText(text, "invariant");
        text = concatText(text, noPlaceKind.getReportText());

        violations.add(inv, new ReportObjectTypeDescrMessage(), new IfReportOnAncestorMessage("has"), new
                 IfReportOnSelfMessage("is"), new LiteralMessage(text));
    }

    /**
     * Concatenate two strings separated with a space.
     *
     * @param prevText Already added text, may be {@code null}.
     * @param newText Text to append, may be {@code null}.
     * @return The concatenated text if at least one of the arguments is not {@code null}, else {@code null}.
     */
    private static String concatText(String prevText, String newText) {
        if (prevText == null) {
            return newText;
        } else if (newText == null) {
            return prevText;
        } else {
            return prevText + " " + newText;
        }
    }
}
