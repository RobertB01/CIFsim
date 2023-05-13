//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.explorer;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;

/** CIF check for requirements that will be explored as plants. */
public class RequirementAsPlantChecker extends CifCheck {
    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        if (aut.getKind() == SupKind.REQUIREMENT) {
            violations.add(aut, "Requirement automaton will be explored as a plant");
        }
    }

    @Override
    protected void preprocessInvariant(Invariant inv, CifCheckViolations violations) {
        if (inv.getSupKind() == SupKind.REQUIREMENT) {
            violations.add(inv, "Requirement invariant will be explored as a plant");
        }
    }
}
