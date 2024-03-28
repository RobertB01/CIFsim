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

import static org.eclipse.escet.common.java.Sets.difference;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** CIF check that does not allow events to be used only as monitor events. */
public class EventNoPureMonitorsCheck extends CifCheckNoCompDefInst {
    /** Events used in the specification. */
    private final Set<Event> specAlphabet = set();

    /** Events that are definitely not always used as monitor event. */
    private final Set<Event> specNonMonitorEvents = set();

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        // Compute alphabet and non-monitor events of the automaton.
        Set<Event> autAlphabet = CifEventUtils.getAlphabet(aut);
        Set<Event> autMonitorsEvents = CifEventUtils.getMonitors(aut, autAlphabet);
        Set<Event> autNonMonitorEvents = difference(autAlphabet, autMonitorsEvents);

        // And update the specification-wide variables.
        specAlphabet.addAll(autAlphabet);
        specNonMonitorEvents.addAll(autNonMonitorEvents);
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        for (Event evt: specAlphabet) {
            if (!specNonMonitorEvents.contains(evt)) {
                violations.add(evt, "Event is only used as a monitor event");
            }
        }
    }
}
