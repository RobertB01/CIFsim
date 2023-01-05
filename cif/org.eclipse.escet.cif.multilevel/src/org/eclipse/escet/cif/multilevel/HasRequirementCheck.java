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

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;

/**
 * CIF check that the specification has at least one requirement element (a requirement automaton or a state/event
 * exclusion requirement invariant).
 */
public class HasRequirementCheck extends CifCheck {
    /** Found number of requirements. */
    private int numReqs;

    @Override
    protected void preprocessSpecification(Specification obj, CifCheckViolations arg) {
        numReqs = 0;
    }

    @Override
    protected void postprocessSpecification(Specification obj, CifCheckViolations violations) {
        if (numReqs < 1) {
            violations.add(null,
                    new LiteralMessage("specification has neither a requirement automaton nor a state/event exclusion "
                            + "requirement invariant"));
        }
    }

    @Override
    protected void preprocessInvariant(Invariant inv, CifCheckViolations arg) {
        // Bail out if not a requirement invariant.
        if (inv.getSupKind() != SupKind.REQUIREMENT) {
            return;
        }

        // Only count state/event exclusion invariants.
        if (inv.getInvKind() == InvKind.EVENT_DISABLES || inv.getInvKind() == InvKind.EVENT_NEEDS) {
            numReqs++;
        }
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations arg) {
        if (aut.getKind() == SupKind.REQUIREMENT) {
            numReqs++;
        }
    }
}
