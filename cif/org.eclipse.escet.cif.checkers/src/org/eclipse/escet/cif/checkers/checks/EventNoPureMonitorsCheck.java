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

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** CIF check that does not allow events to be used only as monitor events. */
public class EventNoPureMonitorsCheck extends CifCheck {
    /** Events that are definitely not always used as monitor event. */
    private final Set<Event> specNonMonitorEvents = set();

    /** Events that may be used as monitor event only. */
    private final Set<Event> specSuspectedMonitorEvents = set();

    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        // Optimistically assume the automaton has no monitor events.
        Set<Event> autNonMonitorsEvents = CifEventUtils.getAlphabet(aut);
        Set<Event> autMonitorsEvents = CifEventUtils.getMonitors(aut, autNonMonitorsEvents);

        // Adjust optimism for found monitor events of the automaton.
        autNonMonitorsEvents.removeAll(autMonitorsEvents);

        // To check correctness of the code below, 9 fake events are assumed to exist. Their name encodes their state
        // in the spec* and aut* variables above. Each event has 2 letters. The first letter is the state of the event
        // with respect to the spec* variables. The second letter does the same with respect to the aut* variables:
        // - Events that have been proven to be non-monitor in the specification: "m_".
        // - Events that are suspected monitor events in the specification (no counter evidence found so far): "M_"
        // - Events that are currently unknown in the specification: "?_".
        // - Events that have been found non-monitor in the automaton: "_m".
        // - Events that are monitor events in the automaton: "_M".
        // - Events that are not found/used by the automaton: "_?".
        //
        // For example:
        // - Event Mm is known as suspected monitor in the specification and used as non-monitor event in the automaton.
        // - Event ?M is unknown by the specification and used as monitor in the automaton.

        // Start situation:
        //
        // specNonMonitorEvents       = "m_" = {mm, mM, m?},
        // specSuspectedMonitorEvents = "M_" = {Mm, MM, M?}
        // autNonMonitorsEvents       = "_m" = {mm, Mm, ?m}
        // autMonitorsEvents          = "_M" = {mM, MM, ?M}

        // Updating the specification sets with the information of the automaton:
        //
        // specNonMonitorEvents = {mm, mM, m?} union {mm, Mm, ?m} = {mm, mM, Mm, m?, ?m}
        specNonMonitorEvents.addAll(autNonMonitorsEvents); // Union of everything non-monitor.

        // specSuspectedMonitorEvents = {Mm, MM, M?} \ {mm, Mm, ?m} = {MM, M?}
        specSuspectedMonitorEvents.removeAll(autNonMonitorsEvents); // Remove suspicion due to new counter evidence.

        // autMonitorsEvents = {mM, MM, ?M} \ {mm, mM, Mm, m?, ?m} = {MM, ?M}
        autMonitorsEvents.removeAll(specNonMonitorEvents); // Reduce new monitors to not counter-proofed knowledge.

        // specSuspectedMonitorEvents = {MM, M?} union {MM, ?M} = {MM, M?, ?M}
        specSuspectedMonitorEvents.addAll(autMonitorsEvents); // Add the remaining new monitors as suspected.

        // Overall result:
        //
        // specNonMonitorEvents = {mm, mM, m?} to {mm, mM, Mm, m?, ?m}: Mm and ?m are added.
        // specSuspectedMonitorEvents = {Mm, MM, M?} to {MM, M?, ?M}: Mm removed, ?M added.
        //
        // In short:
        // - Spec event sets are still disjunct.
        //   --> Correct.
        // - ?? event is the only event missing after the update.
        //   --> Correct as ?? is not used at all.
        // - Mm was proven to be non-monitor and moved to the specNonMonitorEvents set.
        //   --> Correct as the automaton uses Mm as a non-monitor event.
        // - ?m was a newly found non-monitor event in the specification.
        //   --> Correct as the automaton uses ?m as a non-monitor event.
        // - ?M was a newly found suspected monitor event in the specification.
        //   --> Correct as the automaton uses ?M as a monitor event and there is no other counter evidence.
    }

    @Override
    protected void postprocessSpecification(Specification spec, CifCheckViolations violations) {
        for (Event evt: specSuspectedMonitorEvents) {
            violations.add(evt, "Event is only used as a monitor event");
        }
    }
}
