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
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;

/** CIF check that allows automata only if they are of certain supervisory kinds. */
public class AutOnlySpecificSupKindsCheck extends CifCheck {
    /** Set of allowed kinds. */
    private final EnumSet<SupKind> allowedKinds;

    /**
     * Constructor of the {@link AutOnlySpecificSupKindsCheck} class.
     *
     * @param allowedKind Allowed kind of automata in the specification.
     */
    public AutOnlySpecificSupKindsCheck(SupKind allowedKind) {
        this(allowedKind, allowedKind, allowedKind);
    }

    /**
     * Constructor of the {@link AutOnlySpecificSupKindsCheck} class.
     *
     * @param allowedKind1 Allowed kind of automata in the specification.
     * @param allowedKind2 Allowed kind of automata in the specification.
     */
    public AutOnlySpecificSupKindsCheck(SupKind allowedKind1, SupKind allowedKind2) {
        this(allowedKind1, allowedKind2, allowedKind2);
    }

    /**
     * Constructor of the {@link AutOnlySpecificSupKindsCheck} class.
     *
     * @param allowedKind1 Allowed kind of automata in the specification.
     * @param allowedKind2 Allowed kind of automata in the specification.
     * @param allowedKind3 Allowed kind of automata in the specification.
     */
    public AutOnlySpecificSupKindsCheck(SupKind allowedKind1, SupKind allowedKind2, SupKind allowedKind3) {
        this.allowedKinds = EnumSet.of(allowedKind1, allowedKind2, allowedKind3);
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        if (!allowedKinds.contains(aut.getKind())) {
            violations.add(aut, new ReportObjectTypeDescrMessage(),
                    new LiteralMessage("is a %s automaton, not a %s automaton",
                            CifTextUtils.kindToStr(aut.getKind()),
                            makeElementsChoiceText(allowedKinds, CifTextUtils::kindToStr)));
        }
    }
}
