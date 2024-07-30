//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Strings.makeElementsChoiceText;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;

/** CIF check that allows automata only if they are of certain supervisory kinds. */
public class AutOnlySpecificSupKindsCheck extends CifCheck {
    /** Set of allowed kinds. */
    private final EnumSet<SupKind> allowedKinds;

    /**
     * Constructor of the {@link AutOnlySpecificSupKindsCheck} class.
     *
     * @param allowedKinds Allowed kinds of automata in the specification.
     */
    public AutOnlySpecificSupKindsCheck(SupKind... allowedKinds) {
        this(EnumSet.copyOf(Arrays.asList(allowedKinds)));
    }

    /**
     * Constructor of the {@link AutOnlySpecificSupKindsCheck} class.
     *
     * @param allowedKinds Allowed kinds of automata in the specification.
     */
    public AutOnlySpecificSupKindsCheck(EnumSet<SupKind> allowedKinds) {
        this.allowedKinds = allowedKinds;
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        if (!allowedKinds.contains(aut.getKind())) {
            violations.add(aut, "Automaton is a %s automaton rather than a %s automaton",
                    CifTextUtils.kindToStr(aut.getKind()),
                    makeElementsChoiceText(allowedKinds, CifTextUtils::kindToStr));
        }
    }
}
