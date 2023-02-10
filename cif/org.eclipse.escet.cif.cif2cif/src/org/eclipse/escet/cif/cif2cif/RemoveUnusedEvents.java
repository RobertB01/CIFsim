//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Sets.difference;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;

/**
 * In-place transformation that removes unused events.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 */
public class RemoveUnusedEvents extends CifWalker implements CifToCifTransformation {
    /** All events encountered so far. */
    private Set<Event> allEvents = set();

    /** All events used. */
    private Set<Event> allUsedEvents = set();

    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating unused events from a CIF specification with component "
                    + "definitions is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Walk the specification.
        walkSpecification(spec);

        // Identify and remove unused events.
        Set<Event> unusedEvents = difference(allEvents, allUsedEvents);
        for (Event ue: unusedEvents) {
            EMFHelper.removeFromParentContainment(ue);
        }
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp) {
        allEvents.addAll(filter(comp.getDeclarations(), Event.class));
    }

    @Override
    protected void walkEventExpression(EventExpression eventRef) {
        Event event = eventRef.getEvent();
        allUsedEvents.add(event);
    }
}
