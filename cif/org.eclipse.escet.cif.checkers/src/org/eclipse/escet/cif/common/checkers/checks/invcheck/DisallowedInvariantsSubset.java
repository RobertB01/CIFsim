//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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
     * Compute whether this disallowed invariant subset includes the invariant with the given aspect values.
     *
     * @param supKind Supervisory aspect kind to test.
     * @param invKind Invariant aspect kind to test.
     * @param placeKind Place aspect kind to test.
     * @return Whether an invariant with the combined given aspects is disallowed.
     */
    public boolean isDisallowed(SupKind supKind, InvKind invKind, PlaceKind placeKind) {
        return noSupKind.isDisallowed(supKind) && noInvKind.isDisallowed(invKind)
                && noPlaceKind.isDisallowed(placeKind);
    }

    /**
     * Add the provided invariant as a violation to be reported for this subset.
     *
     * @param inv Invariant to report.
     * @param violations The violations collected so far, is modified in-place.
     */
    public void addViolation(Invariant inv, CifCheckViolations violations) {
        // Example: "plant state invariant in a location".
        // Some aspects may not have text.
        String text = null;
        text = addAspectText(text, noSupKind);
        text = addAspectText(text, noInvKind);
        text = (text == null) ? "an invariant" : text + " invariant";
        text = addAspectText(text, noPlaceKind);

        violations.add(inv, new IfReportOnAncestorMessage("has"), new IfReportOnSelfMessage("is"),
                new LiteralMessage(text));
    }

    /**
     * Compare this disallowed subset with the given right side, and decide which of the sets are larger.
     *
     * @param right Right side to compare with.
     * @return Which of the sets are larger.
     */
    public SubSetRelation compareSubset(DisallowedInvariantsSubset right) {
        SubSetRelation noSupKindRelation = noSupKind.compareSubset(right.noSupKind);
        SubSetRelation noInvKindRelation = noInvKind.compareSubset(right.noInvKind);
        SubSetRelation noPlaceKindRelation = noPlaceKind.compareSubset(right.noPlaceKind);
        return SubSetRelation.getRelation(
                noSupKindRelation.leftLarger || noInvKindRelation.leftLarger || noPlaceKindRelation.leftLarger,
                noSupKindRelation.rightLarger || noInvKindRelation.rightLarger || noPlaceKindRelation.rightLarger);
    }

    /**
     * Append the report of an aspect to the report string. Also prepends a matching article the first time.
     *
     * @param reportText Collected report text so far, is {@code null} if no text has been added so far.
     * @param noKind Aspect to query for report and possibly article text.
     * @return The updated report text.
     */
    private static String addAspectText(String reportText, NoKindInterface<?> noKind) {
        String aspectReportText = noKind.getReportText();

        // Aspect has nothing to say.
        if (aspectReportText == null) {
            return reportText;
        }

        // First aspect with text.
        if (reportText == null) {
            return noKind.getArticleText() + " " + aspectReportText;
        }

        // Append the aspect text.
        return reportText + " " + aspectReportText;
    }
}
