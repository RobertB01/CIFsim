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

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.EventRefSet;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;


public class AssignmentPostChecker {
    /**
     * Checks the specification for various constraints and dubious situations (see {@link EventsPostChecker}).
     *
     * <p>
     * We need to check this after the elimination of component definition/instantiation to ensure proper equality
     * checking of events (mostly related to event parameters and component parameters).
     * </p>
     *
     * @param comp The component to check, recursively. The component must not include any component
     *     definitions/instantiations.
     * @param env The post check environment to use.
     */
    public static void check(ComplexComponent comp, CifPostCheckEnv env) {
        // Recursively check for groups.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                check((ComplexComponent)child, env);
            }
            return;
        }

        // Check for automaton.
        check((Automaton)comp, env);
    }

    private static void check(Automaton aut, CifPostCheckEnv env) {
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                for (Update update: edge.getUpdates()) {
                    check(update, env);
                }
            }
        }
    }

    private static void check(Update update, CifPostCheckEnv env) {
        if (update instanceof Assignment) {
            Assignment assignment = (Assignment)update;
            CifType valueType = assignment.getValue().getType();
            CifType addrType = assignment.getAddressable().getType();
            if (!CifTypeUtils.checkTypeCompat(addrType, valueType, RangeCompat.OVERLAP)) {
                env.addProblem(ErrMsg.ASGN_TYPE_VALUE_MISMATCH, update.getPosition(), CifTextUtils.typeToStr(valueType),
                        CifTextUtils.typeToStr(addrType));
                // Non-fatal error.
            }
        } else if (update instanceof IfUpdate) {
            IfUpdate ifUpdate = (IfUpdate)update;
        } else {
            throw new RuntimeException("Unknown update: " + update.toString());
        }
    }
}
