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

package org.eclipse.escet.cif.eventbased.apps.conversion;

import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;

/** Helper class in conversions between CIF and the event-based algorithms. */
public class ApplicationHelper {
    /** Private constructor of the {@link ApplicationHelper} class. */
    private ApplicationHelper() {
        // Static class.
    }

    /**
     * Construct a mapping for finding events by name.
     *
     * @param events Events to obtain by name.
     * @return Mapping from the name of events to the events themselves.
     */
    public static Map<String, Event> buildEventMap(Set<Event> events) {
        Map<String, Event> evtMap = mapc(events.size());
        for (Event evt: events) {
            boolean b = evtMap.put(evt.name, evt) == null;
            Assert.check(b);
        }
        return evtMap;
    }

    /**
     * Make a selection by name from a set of events.
     *
     * @param evtNames Names of the events to select.
     * @param events The available events.
     * @return The selected events.
     */
    public static Set<Event> selectEvents(String[] evtNames, Set<Event> events) {
        Map<String, Event> evtMap = buildEventMap(events);
        Set<Event> evts = setc(evtNames.length);
        for (String evtName: evtNames) {
            Event evt = evtMap.get(evtName);
            if (evt == null) {
                String msg = fmt("Event \"%s\" not found in the alphabet.", evtName);
                throw new InvalidInputException(msg);
            }
            evts.add(evt);
        }
        return evts;
    }
}
