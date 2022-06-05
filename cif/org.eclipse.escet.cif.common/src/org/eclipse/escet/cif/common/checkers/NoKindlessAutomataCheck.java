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

package org.eclipse.escet.cif.common.checkers;

import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;

/** CIF check that does not allow kindless/regular automata. */
public class NoKindlessAutomataCheck extends CifCheck {
    @Override
    protected void preprocessAutomaton(Automaton aut) {
        if (aut.getKind() == SupKind.NONE) {
            addViolation(aut, "automaton is a kindless/regular automaton");
        }
    }
}
