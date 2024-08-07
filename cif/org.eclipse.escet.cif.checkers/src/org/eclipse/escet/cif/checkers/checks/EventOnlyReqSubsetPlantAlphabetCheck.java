//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.java.Sets;

/**
 * Check that disallows events that are in the requirement alphabet, but not in the plant alphabet.
 *
 * <p>
 * This check considers only the synchronization alphabet, not the send and receive alphabets. It is typically a good
 * idea to combine this check with the {@link EventNoChannelsCheck}.
 * </p>
 */
public class EventOnlyReqSubsetPlantAlphabetCheck extends CifCheckNoCompDefInst {
    /** Events that are in the plant alphabet of the specification. */
    private Set<Event> plantAlphabet = set();

    /** Events that are in the requirement alphabet of the specification. */
    private Set<Event> requirementAlphabet = set();

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        if (aut.getKind() == SupKind.PLANT) {
            plantAlphabet.addAll(CifEventUtils.getAlphabet(aut));
        } else if (aut.getKind() == SupKind.REQUIREMENT) {
            requirementAlphabet.addAll(CifEventUtils.getAlphabet(aut));
        }
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        Set<Event> violationEvents = Sets.difference(requirementAlphabet, plantAlphabet);
        for (Event evt: violationEvents) {
            violations.add(evt, "Event is in the requirement alphabet, but not in the plant alphabet");
        }
    }
}
