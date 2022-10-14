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

package org.eclipse.escet.cif.common.checkers.checks;

import static org.eclipse.escet.common.java.Strings.makeElementsChoiceText;

import java.util.EnumSet;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnAncestorMessage;
import org.eclipse.escet.cif.common.checkers.messages.IfReportOnSelfMessage;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;

/** CIF check that allows invariants only if they are of certain supervisory kinds. */
public class InvOnlySpecificSupKindsCheck extends CifCheck {
    /** Set of allowed kinds. */
    private final EnumSet<SupKind> allowedKinds;

    /**
     * Constructor of the {@link InvOnlySpecificSupKindsCheck} class.
     *
     * @param allowedKind Allowed kind of invariants in the specification.
     */
    public InvOnlySpecificSupKindsCheck(SupKind allowedKind) {
        this(allowedKind, allowedKind, allowedKind);
    }

    /**
     * Constructor of the {@link InvOnlySpecificSupKindsCheck} class.
     *
     * @param allowedKind1 Allowed kind of invariants in the specification.
     * @param allowedKind2 Allowed kind of invariants in the specification.
     */
    public InvOnlySpecificSupKindsCheck(SupKind allowedKind1, SupKind allowedKind2) {
        this(allowedKind1, allowedKind2, allowedKind2);
    }

    /**
     * Constructor of the {@link InvOnlySpecificSupKindsCheck} class.
     *
     * @param allowedKind1 Allowed kind of invariants in the specification.
     * @param allowedKind2 Allowed kind of invariants in the specification.
     * @param allowedKind3 Allowed kind of invariants in the specification.
     */
    public InvOnlySpecificSupKindsCheck(SupKind allowedKind1, SupKind allowedKind2, SupKind allowedKind3) {
        this.allowedKinds = EnumSet.of(allowedKind1, allowedKind2, allowedKind3);
    }

    @Override
    protected void preprocessInvariant(Invariant inv, CifCheckViolations violations) {
        if (!allowedKinds.contains(inv.getSupKind())) {
            violations.add(inv, new ReportObjectTypeDescrMessage(), new IfReportOnAncestorMessage("has"),
                    new IfReportOnSelfMessage("is"),
                    new LiteralMessage("a %s invariant, which is not a %s invariant",
                            CifTextUtils.kindToStr(inv.getSupKind()),
                            makeElementsChoiceText(allowedKinds, CifTextUtils::kindToStr)));
        }
    }
}
