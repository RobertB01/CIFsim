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

package org.eclipse.escet.cif.multilevel;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;

/** CIF check that the specification has at least one plant element (an input variable or a plant automaton). */
public class HasPlantCheck extends CifCheck {
    /** Number of plants found in the specification. */
    private int numPlants;

    @Override
    protected void preprocessSpecification(Specification obj, CifCheckViolations violations) {
        numPlants = 0;
    }

    @Override
    protected void postprocessSpecification(Specification obj, CifCheckViolations violations) {
        if (numPlants < 1) {
            violations.add(null,
                    new LiteralMessage("specification has neither a plant automaton nor an input variable"));
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations arg) {
        if (aut.getKind() == SupKind.PLANT) {
            numPlants++;
        }
    }

    @Override
    protected void preprocessInputVariable(InputVariable inp, CifCheckViolations arg) {
        if (!(normalizeType(inp.getType()) instanceof VoidType)) {
            numPlants++;
        }
    }
}
