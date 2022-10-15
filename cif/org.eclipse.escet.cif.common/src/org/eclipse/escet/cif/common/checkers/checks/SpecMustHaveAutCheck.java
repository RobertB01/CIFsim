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

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;

/** CIF check that allows specifications only if they have at least one automaton. */
public class SpecMustHaveAutCheck extends CifCheck {
    /**
     * Whether an automaton has been encountered in the specification being checked. Only valid when checking a
     * specification
     */
    private boolean autEncountered;

    @Override
    protected void preprocessSpecification(Specification spec, CifCheckViolations violations) {
        // Initialize, no automaton encountered yet.
        autEncountered = false;
    }

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        // Found an automaton.
        autEncountered = true;
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        // At least one automaton.
        if (!autEncountered) {
            violations.add(null, new ReportObjectTypeDescrMessage(), new LiteralMessage("has no automata"));
        }
    }
}
