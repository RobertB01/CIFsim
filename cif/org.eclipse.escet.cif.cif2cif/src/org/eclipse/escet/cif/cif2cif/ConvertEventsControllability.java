//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2018, 2022 Contributors to the Eclipse Foundation
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

import static java.util.Collections.emptySet;
import static org.eclipse.escet.cif.common.CifCollectUtils.collectEvents;
import static org.eclipse.escet.cif.common.CifScopeUtils.getScope;
import static org.eclipse.escet.cif.common.CifScopeUtils.getSymbolNamesForScope;
import static org.eclipse.escet.cif.common.CifScopeUtils.getUniqueName;
import static org.eclipse.escet.cif.common.CifScopeUtils.hasCompDefInst;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.ScopeCache;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/**
 * Basic class to convert events with a controllability status.
 *
 * <p>
 * It also contains an interface class to convert all uncontrollable events to controllable events and an interface
 * class to convert all controllable events to uncontrollable events.
 * </p>
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 */
public final class ConvertEventsControllability {
    @SuppressWarnings("javadoc")
    private ConvertEventsControllability() {
        // Static class.
    }

    /**
     * Converts events with a controllability status in the given specification to the desired controllability status.
     *
     * <p>
     * Event names of modified events are changed if they use the specific prefix "c_" or "u_".
     * </p>
     *
     * @param spec The specification to adapt.
     * @param toControllable Set to {@code true} to convert uncontrollable events to controllable events or
     *     {@code false} to convert controllable events to uncontrollable events.
     */
    public static void convert(Specification spec, boolean toControllable) {
        // Check no component definition/instantiation precondition.
        if (hasCompDefInst(spec)) {
            String eventSource = toControllable ? "uncontrollable" : "controllable";
            String eventDestination = toControllable ? "controllable" : "uncontrollable";
            String msg = fmt("Converting %s events to %s events", eventSource, eventDestination);
            throw new CifToCifPreconditionException(
                    msg + " in a CIF specification with component definitions is currently not supported.");
        }

        // Collect the events of the specification.
        List<Event> events = list();
        collectEvents(spec, events);

        // Change the events.
        String prefixToReplace = toControllable ? "u_" : "c_";
        String prefixReplacement = toControllable ? "c_" : "u_";

        ScopeCache scopeCache = new ScopeCache();
        for (Event event: events) {
            if (event.getControllable() != null && event.getControllable() != toControllable) {
                event.setControllable(toControllable);

                // Change the name if necessary.
                String name = event.getName();
                Set<String> localNames = getSymbolNamesForScope(getScope(event), scopeCache);
                if (name.startsWith(prefixToReplace)) {
                    String newName = prefixReplacement + name.substring(prefixToReplace.length());

                    // If the new name also exists, find a better name.
                    if (localNames.contains(newName)) {
                        newName = getUniqueName(newName, localNames, emptySet());
                    }

                    event.setName(newName);
                    localNames.remove(name);
                    localNames.add(newName);
                }
                // Else name was not modified, no need to rename.
            }
        }
    }

    /**
     * In-place transformation that convert all uncontrollable events to controllable events.
     *
     * <p>
     * Precondition: Specifications with component definitions/instantiations are currently not supported.
     * </p>
     */
    public static class ConvertUncntrlEventsToCntrl implements CifToCifTransformation {
        @Override
        public void transform(Specification spec) {
            convert(spec, true);
        }
    }

    /**
     * In-place transformation that convert all controllable events to uncontrollable events.
     *
     * <p>
     * Precondition: Specifications with component definitions/instantiations are currently not supported.
     * </p>
     */
    public static class ConvertCntrlEventsToUncntrl implements CifToCifTransformation {
        @Override
        public void transform(Specification spec) {
            convert(spec, false);
        }
    }
}
