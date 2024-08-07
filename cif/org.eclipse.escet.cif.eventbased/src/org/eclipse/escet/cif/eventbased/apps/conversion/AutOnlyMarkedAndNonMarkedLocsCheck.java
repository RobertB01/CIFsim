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

package org.eclipse.escet.cif.eventbased.apps.conversion;

import java.util.List;

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.checkers.checks.CompNoMarkerPredsCheck;
import org.eclipse.escet.cif.checkers.checks.LocOnlyStaticEvalMarkerPredsCheck;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * Check that only allows automata that have both marked and non-marked locations. Note that:
 *
 * <ul>
 * <li>Locations for which it can not statically be determined whether they are marked or not, are ignored. If any such
 * location exists in an automaton, no violations are reported for the automaton. This is OK for the event-based tools,
 * since we separately check for statically-evaluable marker predicates in locations, using
 * {@link LocOnlyStaticEvalMarkerPredsCheck}.</li>
 * <li>Marker predicates in components are not considered. This is OK for the event-based tools, since we separately
 * check {@link CompNoMarkerPredsCheck}.</li>
 * <li>Reachability of locations is not considered.</li>
 * </ul>
 */
public class AutOnlyMarkedAndNonMarkedLocsCheck extends CifCheckNoCompDefInst {
    @Override
    protected void preprocessAutomaton(Automaton aut, CifCheckViolations violations) {
        // Check for presence of marked and non-marked locations.
        boolean hasMarked = false;
        boolean hasNonMarked = false;
        for (Location loc: aut.getLocations()) {
            List<Expression> markeds = loc.getMarkeds();

            // Check for non-marked location.
            if (markeds.isEmpty() || CifValueUtils.isTriviallyFalse(markeds, false, true)) {
                hasNonMarked = true;
                if (hasMarked) {
                    return;
                }
                continue;
            }

            // Check for marked location.
            if (!markeds.isEmpty() && CifValueUtils.isTriviallyTrue(markeds, false, true)) {
                hasMarked = true;
                if (hasNonMarked) {
                    return;
                }
                continue;
            }

            // Can't decide statically.
            return;
        }

        // Check for only marked or only non-marked locations.
        if (!hasMarked) {
            violations.add(aut, "Automaton has only non-marked locations");
        }
        if (!hasNonMarked) {
            violations.add(aut, "Automaton has only marked locations");
        }
    }
}
