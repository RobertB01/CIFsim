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

package org.eclipse.escet.cif.multilevel.ciftodmm;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;

/**
 * CIF check that only allows specifications with at least one plant element (an input variable or a plant automaton).
 */
public class SpecHasPlantCheck extends CifCheck {
    /** Number of plants found in the specification. */
    private int numPlants;

    @Override
    protected void preprocessSpecification(Specification spec, CifCheckViolations violations) {
        numPlants = 0;
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        if (numPlants < 1) {
            violations.add(spec, "Specification has neither a plant automaton nor an input variable");
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        if (aut.getKind() == SupKind.PLANT) {
            numPlants++;
        }
    }

    @Override
    protected void preprocessInputVariable(InputVariable inp, CifCheckViolations violations) {
        numPlants++;
    }
}
