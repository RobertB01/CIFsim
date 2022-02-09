//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifCollectUtils.collectEvents;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/**
 * Basic class to convert the controllability status of events. It contains one class to convert all events to
 * controllable events and one class to convert all events to uncontrollable events.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 */
public final class ConvertEventsContr {
    @SuppressWarnings("javadoc")
    private ConvertEventsContr() {
        // Static class.
    }

    /**
     * Converts the controllability status of events in the given specification to the desired controllability status.
     *
     * <p>
     * Event names are changed if they use the specific prefix "c_" or "u_".
     * </p>
     *
     * @param spec The specification to adapt.
     * @param toControllable Set to {@code true} to convert to controllable events or {@code false} to convert to
     *     uncontrollable events.
     */
    public static void convert(Specification spec, boolean toControllable) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Converting event to uncontrollable from a CIF specification "
                    + "with component definitions is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        List<Event> events = list();
        collectEvents(spec, events);

        // Get global names of the events.
        Set<String> eventNames = setc(events.size());
        for (Event event: events) {
            eventNames.add(getAbsName(event));
        }

        String prefixToReplace = toControllable ? "u_" : "c_";
        String prefixReplacement = toControllable ? "c_" : "u_";

        for (Event event: events) {
            if (event.getControllable() != null && event.getControllable() != toControllable) {
                event.setControllable(toControllable);
                String name = event.getName();
                if (name.startsWith(prefixToReplace)) {
                    setUniqueEventName(event, name.replaceFirst(prefixToReplace, prefixReplacement), eventNames);
                }
            }
        }
    }

    /**
     * Construct a new non-existing name for an event. The construction is based on the global names of the events, as
     * they need to be unique. Changes the event in-place.
     *
     * <p>
     * The set of event names is modified in-place to reflect the name change of the event.
     * </p>
     *
     * @param event Event for which a new name needs to be created. Modified in-place.
     * @param newName The new suggested name.
     * @param eventNames Currently available global event names. Modified in-place.
     */
    private static void setUniqueEventName(Event event, String newName, Set<String> eventNames) {
        // If new name is same as old name, nothing has to be changed.
        if (event.getName().equals(newName)) {
            return;
        }

        // Construct the first suggestion of a new global name.
        String oldGlobalName = getAbsName(event);
        String suggestionLocalName = newName;
        String suggestionGlobalName;
        int index = oldGlobalName.lastIndexOf(".");
        if (index >= 0) {
            suggestionGlobalName = oldGlobalName.substring(0, index + 1) + newName;
        } else {
            suggestionGlobalName = newName;
        }

        // If the new name is already in use, we need to adapt the suggestion.
        if (eventNames.contains(suggestionGlobalName)) {
            int suffix = 1;
            while (true) {
                String newGlobalName = fmt("%s%d", suggestionGlobalName, suffix);
                if (!eventNames.contains(newGlobalName)) {
                    suggestionLocalName = fmt("%s%d", newName, suffix);
                    break;
                }
                suffix++;
            }
        }

        eventNames.remove(getAbsName(event));
        event.setName(suggestionLocalName);
        eventNames.add(getAbsName(event));
    }

    /**
     * In-place transformation that convert all events to controllable events.
     *
     * <p>
     * Precondition: Specifications with component definitions/instantiations are currently not supported.
     * </p>
     */
    public static class ConvertEventsToContr implements CifToCifTransformation {
        @Override
        public void transform(Specification spec) {
            convert(spec, true);
        }
    }

    /**
     * In-place transformation that convert all events to uncontrollable events.
     *
     * <p>
     * Precondition: Specifications with component definitions/instantiations are currently not supported.
     * </p>
     */
    public static class ConvertEventsToUncontr implements CifToCifTransformation {
        @Override
        public void transform(Specification spec) {
            convert(spec, false);
        }
    }
}
