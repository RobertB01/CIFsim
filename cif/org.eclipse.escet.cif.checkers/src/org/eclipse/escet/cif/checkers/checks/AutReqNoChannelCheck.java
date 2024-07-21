//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;

/** CIF check that does not requirement automata to be senders or receivers for channels. */
public class AutReqNoChannelCheck extends CifCheckNoCompDefInst {
    /** Whether a requirement automaton is currently being checked. */
    private boolean inRequirementAutomaton;

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        if (aut.getKind() == SupKind.REQUIREMENT) {
            inRequirementAutomaton = true;
        }
    }

    @Override
    protected void postprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        inRequirementAutomaton = false;
    }

    @Override
    protected void preprocessEdgeSend(EdgeSend edgeSend, CifCheckViolations violations) {
        if (inRequirementAutomaton) {
            violations.add(edgeSend, "Requirement automaton sends over a channel");
        }
    }

    @Override
    protected void preprocessEdgeReceive(EdgeReceive edgeReceive, CifCheckViolations violations) {
        if (inRequirementAutomaton) {
            violations.add(edgeReceive, "Requirement automaton receives from a channel");
        }
    }
}
