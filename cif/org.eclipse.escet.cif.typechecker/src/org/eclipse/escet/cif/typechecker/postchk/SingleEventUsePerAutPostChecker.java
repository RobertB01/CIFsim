//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.common.java.Sets;

/** 'Automaton.uniqueUsagePerEvent' constraint checker, for the 'post' type checking phase. */
public class SingleEventUsePerAutPostChecker {
    /** Constructor for the {@link SingleEventUsePerAutPostChecker} class. */
    private SingleEventUsePerAutPostChecker() {
        // Static class.
    }

    /**
     * Checks the specification for violations of the 'Automaton.uniqueUsagePerEvent' constraint.
     *
     * <p>
     * We need to check this after the elimination of component definition/instantiation to ensure proper equality
     * checking of events (mostly related to event parameters), to not get any false positives.
     * </p>
     *
     * @param spec The specification to check, recursively. The specification must not include any component
     *     definitions/instantiations.
     * @param env The post check environment to use.
     */
    public static void check(Specification spec, CifPostCheckEnv env) {
        // Optimize by skipping check if specification has no channels. If the
        // specification has no channels, we can't use sends/receives, and thus
        // automata can only use events in one way, and won't violate the
        // constraint.
        if (!hasChannel(spec)) {
            return;
        }

        // Normal check.
        checkComponent(spec, env);
    }

    /**
     * Checks a component for violations of the 'Automaton.uniqueUsagePerEvent' constraint.
     *
     * <p>
     * We need to check this after the elimination of component definition/instantiation to ensure proper equality
     * checking of events (mostly related to event parameters), to not get any false positives.
     * </p>
     *
     * @param comp The component to check, recursively. This component must not include any component
     *     definitions/instantiations.
     * @param env The post check environment to use.
     */
    private static void checkComponent(ComplexComponent comp, CifPostCheckEnv env) {
        // Recursively check for groups.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                checkComponent((ComplexComponent)child, env);
            }
            return;
        }

        // Check for automaton.
        Automaton aut = (Automaton)comp;

        // Get send/receive uses.
        Set<Event> sndEvents = set();
        Set<Event> rcvEvents = set();
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                for (EdgeEvent edgeEvent: edge.getEvents()) {
                    // Add send/receive uses. Note that we can't send/receive
                    // over 'tau', so there is no need to detect 'tau' here.
                    if (edgeEvent instanceof EdgeSend) {
                        Expression eventRef = edgeEvent.getEvent();
                        Event event = ((EventExpression)eventRef).getEvent();
                        sndEvents.add(event);
                    } else if (edgeEvent instanceof EdgeReceive) {
                        Expression eventRef = edgeEvent.getEvent();
                        Event event = ((EventExpression)eventRef).getEvent();
                        rcvEvents.add(event);
                    }
                }
            }
        }

        // If no uses of send/receive, then all uses are 'synchronization'
        // uses, for which we have already ensured they are in the alphabet.
        if (sndEvents.isEmpty() && rcvEvents.isEmpty()) {
            return;
        }

        // Get alphabet.
        Set<Event> alphabet = CifEventUtils.getAlphabet(aut);

        // Check send/receive uses against alphabet.
        Set<Event> sndAlphaOverlap = Sets.intersection(sndEvents, alphabet);
        Set<Event> rcvAlphaOverlap = Sets.intersection(rcvEvents, alphabet);
        if (!sndAlphaOverlap.isEmpty()) {
            for (Event event: sndAlphaOverlap) {
                env.addProblem(ErrMsg.EVENT_AUT_USAGE_CONFLICT, aut.getPosition(), getAbsName(event), getAbsName(aut),
                        "send", "synchronize");
                // Non-fatal problem.
            }
        }
        if (!rcvAlphaOverlap.isEmpty()) {
            for (Event event: rcvAlphaOverlap) {
                env.addProblem(ErrMsg.EVENT_AUT_USAGE_CONFLICT, aut.getPosition(), getAbsName(event), getAbsName(aut),
                        "receive", "synchronize");
                // Non-fatal problem.
            }
        }

        // Check send uses against receive uses.
        Set<Event> sndRcvOverlap = Sets.intersection(sndEvents, rcvEvents);
        if (!sndRcvOverlap.isEmpty()) {
            for (Event event: sndRcvOverlap) {
                env.addProblem(ErrMsg.EVENT_AUT_USAGE_CONFLICT, aut.getPosition(), getAbsName(event), getAbsName(aut),
                        "send", "receive");
                // Non-fatal problem.
            }
        }
    }

    /**
     * Does the given component or any of its descendants declare a channel?
     *
     * @param comp The component to check.
     * @return {@code true} if the component or any of its descendants declares a channel, {@code false} otherwise.
     */
    private static boolean hasChannel(ComplexComponent comp) {
        // Check declarations.
        for (Declaration decl: comp.getDeclarations()) {
            if (!(decl instanceof Event)) {
                continue;
            }
            if (((Event)decl).getType() != null) {
                return true;
            }
        }

        // Done for automata.
        if (comp instanceof Automaton) {
            return false;
        }

        // Recursively check for groups.
        for (Component child: ((Group)comp).getComponents()) {
            if (hasChannel((ComplexComponent)child)) {
                return true;
            }
        }
        return false;
    }
}
