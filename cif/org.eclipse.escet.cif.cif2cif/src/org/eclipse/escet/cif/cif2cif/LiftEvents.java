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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.filter;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;

/**
 * In-place transformation that lifts event declarations to the specification.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations and/or groups are currently not supported.
 * </p>
 *
 * <p>
 * Since declarations of the specifications are merged with the events of the automata, renaming may be necessary to
 * ensure uniquely named declarations.
 * </p>
 */
public class LiftEvents implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Lifting event declarations for a CIF specification with component definitions is "
                    + "currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Get events from automata.
        List<Event> autEvents = list();
        for (Component component: spec.getComponents()) {
            // Check precondition (no groups).
            if (component instanceof Group) {
                String msg = "Lifting event declarations for a CIF specification with groups is currently not "
                        + "supported.";
                throw new CifToCifPreconditionException(msg);
            }

            // Get events from automaton.
            Assert.check(component instanceof Automaton);
            Automaton aut = (Automaton)component;

            autEvents.addAll(filter(aut.getDeclarations(), Event.class));
        }

        // Get names of objects in the specification scope.
        Set<String> specNames = CifScopeUtils.getSymbolNamesForScope(spec, null);

        // Rename events to absolute names. Collect the absolute names.
        Set<String> liftedNames = set();
        for (Event event: autEvents) {
            String absName = CifTextUtils.getAbsName(event, false).replace('.', '_');
            event.setName(absName);
            liftedNames.add(absName);
        }

        // Lift events.
        spec.getDeclarations().addAll(autEvents);

        // Rename lifted events to unique names, if needed.
        Set<String> usedNames = Sets.copy(specNames);
        for (Event event: autEvents) {
            if (usedNames.contains(event.getName())) {
                // Rename event.
                String oldName = event.getName();
                String newName = CifScopeUtils.getUniqueName(oldName, usedNames, liftedNames);
                event.setName(newName);
                warn("Event \"%s\" is renamed to \"%s\".", oldName, newName);
            }
            usedNames.add(event.getName());
        }
    }
}
