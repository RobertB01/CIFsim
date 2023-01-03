//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that simplifies CIF specifications, by applying the following non-value related
 * simplifications:
 * <ul>
 * <li>Duplicate information is removed. For instance, duplicate events in the alphabet are removed.</li>
 * <li>Self-loops with a target location are simplified by removing the target location.</li>
 * </ul>
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported. This is to make
 * sure we don't suffer from the false negatives of the {@link CifEventUtils#areSameEventRefs} method.
 * </p>
 *
 * @see SimplifyValues
 */
public class SimplifyOthers extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "The \"simplify-others\" CIF to CIF transformation currently does not support CIF "
                    + "specifications with component definitions.";
            throw new CifToCifPreconditionException(msg);
        }

        // Perform actual transformation.
        walkSpecification(spec);
    }

    @Override
    protected void preprocessAutomaton(Automaton aut) {
        // Remove duplicate events in monitor set.
        Monitors monitors = aut.getMonitors();
        if (monitors != null) {
            removeDuplicateEventRefs(monitors.getEvents());
        }
    }

    @Override
    protected void preprocessAlphabet(Alphabet alphabet) {
        // Remove duplicate events in alphabet.
        removeDuplicateEventRefs(alphabet.getEvents());
    }

    @Override
    protected void preprocessEdge(Edge edge) {
        // If it is a self-loop with a target location, remove the target
        // location.
        Location src = (Location)edge.eContainer();
        Location tgt = edge.getTarget();
        if (tgt != null && src == tgt) {
            edge.setTarget(null);
        }

        // Remove duplicate events on edge. Since we can't have different uses
        // in a single automaton, we can only have duplicates for the same use.
        // Multiple synchronizing uses or receive uses are processed. Multiple
        // sends however are ignored, as they could potentially send different
        // values.
        List<Expression> eventRefs = list();
        for (EdgeEvent edgeEvent: edge.getEvents()) {
            if (edgeEvent instanceof EdgeSend) {
                continue;
            }
            eventRefs.add(edgeEvent.getEvent());
        }
        removeDuplicateEventRefs(eventRefs);
    }

    /**
     * Removes duplicate events references from the list of event references.
     *
     * <p>
     * If the event reference is contained in an {@link EdgeEvent}, the {@link EdgeEvent} is removed from its parent
     * instead.
     * </p>
     *
     * @param eventRefs The event references. May be modified in-place.
     */
    private void removeDuplicateEventRefs(List<Expression> eventRefs) {
        for (int i = eventRefs.size() - 1; i >= 0; i--) {
            Expression eventRef = eventRefs.get(i);
            boolean duplicate = false;
            for (int j = eventRefs.size() - 1; j > i; j--) {
                if (CifEventUtils.areSameEventRefs(eventRef, eventRefs.get(j))) {
                    duplicate = true;
                    break;
                }
            }
            if (duplicate) {
                if (eventRefs instanceof EList) {
                    // Normal event references obtained from a metamodel
                    // feature of type EList<Expression>.
                    eventRefs.remove(i);
                } else {
                    // Edge event references obtained from a metamodel
                    // feature of type EList<EdgeEvent>.
                    Assert.check(eventRefs instanceof ArrayList);
                    EdgeEvent edgeEvent = (EdgeEvent)eventRef.eContainer();
                    EMFHelper.removeFromParentContainment(edgeEvent);
                }
            }
        }
    }
}
