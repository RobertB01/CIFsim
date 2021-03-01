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

package org.eclipse.escet.cif.eventdisabler;

import static java.util.Collections.EMPTY_SET;
import static org.eclipse.escet.cif.common.CifEventUtils.getAlphabet;
import static org.eclipse.escet.cif.common.CifScopeUtils.getSymbolNamesForScope;
import static org.eclipse.escet.cif.common.CifScopeUtils.getUniqueName;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifValueUtils.makeFalse;
import static org.eclipse.escet.cif.common.CifValueUtils.makeTrue;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlphabet;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newGroup;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.cif.eventdisabler.options.EventNamesFileOption;
import org.eclipse.escet.cif.eventdisabler.options.EventNamesOption;
import org.eclipse.escet.cif.eventdisabler.options.EventUsage;
import org.eclipse.escet.cif.eventdisabler.options.EventUsageOption;
import org.eclipse.escet.cif.eventdisabler.options.IncludeInputSpecOption;
import org.eclipse.escet.cif.eventdisabler.options.SvgInputEventsOption;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEvent;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIf;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventIfEntry;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgInEventSingle;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.Assert;

/** CIF event disabler. */
public class EventDisabler {
    /** The input specification. May be modified in-place, if the {@link #resultSpec} is this input specification. */
    private final Specification inputSpec;

    /** The supplied event names. */
    private final Set<String> eventNames;

    /** The alphabet of the input specification. */
    private final Set<Event> alphabet;

    /**
     * Mapping from absolute event names to the actual events, for all events declared in the input specification. May
     * include events not in the alphabet of the input specification, although that is the exception.
     */
    private final Map<String, Event> eventMap;

    /** The result specification. */
    private final Specification resultSpec;

    /**
     * Constructor for the {@link EventDisabler} class.
     *
     * @param inputSpec The input specification. May potentially be modified in-place.
     */
    private EventDisabler(Specification inputSpec) {
        // Private constructor to force use of static method.
        this.inputSpec = inputSpec;
        eventNames = getEventNames(inputSpec);
        checkEventNames();
        alphabet = set();
        eventMap = map();
        collectEvents(inputSpec);
        resultSpec = getResultSpec();
    }

    /**
     * Disable events in the given input specification. The result of this operation depends on the options of the event
     * disabler application.
     *
     * @param inputSpec The input specification. May potentially be modified in-place.
     * @return The output specification.
     */
    public static Specification disableEvents(Specification inputSpec) {
        // Initialize event disabler.
        EventDisabler disabler = new EventDisabler(inputSpec);

        // Get the events to disable. Also adds the events to the
        // specification, if not yet present.
        List<Event> eventsToDisable = disabler.getEventsToDisable();

        // Add new automaton that disables the events.
        disabler.addAutomaton(eventsToDisable);

        // Return the result specification.
        return disabler.resultSpec;
    }

    /**
     * Get the supplied event names, from the options.
     *
     * @param spec The input specification.
     * @return The absolute names of the supplied events.
     */
    private static Set<String> getEventNames(Specification spec) {
        // At least one of the options should be used.
        String eventNames = EventNamesOption.getEventNames();
        String eventFile = EventNamesFileOption.getFilePath();
        boolean useSvgInput = SvgInputEventsOption.isEnabled();

        if (eventNames == null && eventFile == null && !useSvgInput) {
            String msg = "No events specified. Use one of the options to specify events.";
            throw new InvalidOptionException(msg);
        }

        // Initialize event names.
        Set<String> rslt = set();

        // Handle option with event names. Split on commas and spaces.
        if (eventNames != null) {
            for (String eventName: StringUtils.split(eventNames, ", ")) {
                rslt.add(eventName);
            }
        }

        // Handle option with event names file. Read the lines from the text
        // file, remove empty lines, and trim the remaining lines.
        if (eventFile != null) {
            String path = Paths.resolve(eventFile);
            List<String> lines;
            try {
                InputStream stream = new FileInputStream(path);
                stream = new BufferedInputStream(stream);
                lines = IOUtils.readLines(stream, "UTF-8");
            } catch (IOException e) {
                String msg = fmt("Failed to read \"%s\".", eventFile);
                throw new InputOutputException(msg, e);
            }

            for (String line: lines) {
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty()) {
                    continue;
                }
                if (trimmedLine.startsWith("#")) {
                    continue;
                }
                rslt.add(trimmedLine);
            }
        }

        // Handle option to use the SVG input events.
        if (useSvgInput) {
            collectSvgInputEventNames(spec, rslt);
        }

        // Return the event names supplied via all the options.
        return rslt;
    }

    /**
     * Collects the absolute names of the SVG input events, for the given component, recursively.
     *
     * @param comp The component from which to collect, recursively.
     * @param names The already collected names. Is extended in-place.
     */
    private static void collectSvgInputEventNames(ComplexComponent comp, Set<String> names) {
        // Collect locally from SVG input mappings.
        for (IoDecl decl: comp.getIoDecls()) {
            if (!(decl instanceof SvgIn)) {
                continue;
            }

            SvgInEvent svgInEvt = ((SvgIn)decl).getEvent();
            if (svgInEvt instanceof SvgInEventSingle) {
                Expression evtRef = ((SvgInEventSingle)svgInEvt).getEvent();
                Event evt = ((EventExpression)evtRef).getEvent();
                names.add(CifTextUtils.getAbsName(evt, false));
            } else {
                Assert.check(svgInEvt instanceof SvgInEventIf);
                for (SvgInEventIfEntry entry: ((SvgInEventIf)svgInEvt).getEntries()) {
                    Expression evtRef = entry.getEvent();
                    Event evt = ((EventExpression)evtRef).getEvent();
                    names.add(CifTextUtils.getAbsName(evt, false));
                }
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectSvgInputEventNames((ComplexComponent)child, names);
            }
        }
    }

    /** Check the supplied event names. They must be valid absolute names. */
    private void checkEventNames() {
        for (String eventName: eventNames) {
            if (!CifValidationUtils.isValidName(eventName)) {
                String msg = fmt("Event name \"%s\" is not a valid absolute name for a CIF event.", eventName);
                throw new InvalidInputException(msg);
            }
        }
    }

    /**
     * Collect the events from the given component, and put them in a mapping from absolute event names to the events.
     * Also collects the alphabet of the given component.
     *
     * <p>
     * The non-recursive call should provide the specification, to get the events and alphabet of the entire
     * specification.
     * </p>
     *
     * @param comp The component for which to recursively collect the events and alphabet.
     * @see #alphabet
     * @see #eventMap
     */
    private void collectEvents(ComplexComponent comp) {
        // Add event declarations.
        for (Declaration decl: comp.getDeclarations()) {
            if (!(decl instanceof Event)) {
                continue;
            }
            eventMap.put(getAbsName(decl, false), (Event)decl);
        }

        // Automaton.
        if (comp instanceof Automaton) {
            alphabet.addAll(getAlphabet((Automaton)comp));
            return;
        }

        // Group.
        for (Component child: ((Group)comp).getComponents()) {
            collectEvents((ComplexComponent)child);
        }
    }

    /**
     * Get the result specification. If the input specification is to be included, the input specification is returned,
     * and may be modified in-place. Otherwise, an empty specification is created, to be filled later.
     *
     * @return The result specification.
     */
    private Specification getResultSpec() {
        return IncludeInputSpecOption.includeInputSpec() ? inputSpec : newSpecification();
    }

    /**
     * Returns the events to disable. Also adds the events to the specification, if not yet present.
     *
     * @return The events to disable. They are guaranteed to exist in the result specification after this method
     *     returns.
     */
    private List<Event> getEventsToDisable() {
        // Get event usage, and use it to determine how to get the events.
        EventUsage usage = EventUsageOption.getUsage();

        // Get events to disable.
        List<Event> rslt = list();
        switch (usage) {
            case DISABLE:
                // Disable the supplied events.
                for (String eventName: eventNames) {
                    rslt.add(getEventToDisable(eventName));
                }
                break;

            case ALPHABET:
                // Disable all supplied events, that are not in the alphabet.
                for (String eventName: eventNames) {
                    Event event = eventMap.get(eventName);
                    if (event == null) {
                        // Not in input spec, thus not in its alphabet.
                        rslt.add(getEventToDisable(eventName));
                    } else if (!alphabet.contains(event)) {
                        // Not in input spec alphabet.
                        rslt.add(getEventToDisable(eventName));
                    } // else: in input spec alphabet, not disabled.
                }
                break;
        }

        // Return the events to disable.
        return rslt;
    }

    /**
     * Returns the event with the given name, from the result specification. If the event does not yet exist in the
     * result specification, it is added. The goal of the caller must be to disable this event.
     *
     * @param absName The absolute name of the event.
     * @return The event.
     */
    private Event getEventToDisable(String absName) {
        // If already exists in the result specification, because it includes
        // the input specification, return that event.
        Event origEvent = eventMap.get(absName);
        if (inputSpec == resultSpec && origEvent != null) {
            return origEvent;
        }

        // Event does not exist in the result specification.
        String[] names = StringUtils.split(absName, '.');

        // Find/create scope that declares the event.
        ComplexComponent scope = resultSpec;
        SCOPE_PATH:
        for (int i = 0; i < names.length - 1; i++) {
            // Get next child scope name.
            String name = names[i];

            // Make sure no conflicting declaration with that name exists.
            for (Declaration decl: scope.getDeclarations()) {
                if (decl.getName().equals(name)) {
                    String msg = fmt("Can't disable event \"%s\": \"%s\" is not a component.", absName,
                            StringUtils.join(names, '.', 0, i + 1));
                    throw new InvalidInputException(msg);
                }
            }

            // Automata can't have child scopes.
            if (scope instanceof Automaton) {
                String msg = fmt("Can't disable event \"%s\": \"%s\" is an automaton.", absName,
                        StringUtils.join(names, '.', 0, i));
                throw new InvalidInputException(msg);
            }

            // Find existing child scope.
            Group scopeGroup = (Group)scope;
            for (Component child: scopeGroup.getComponents()) {
                if (child.getName().equals(name)) {
                    scope = (ComplexComponent)child;
                    continue SCOPE_PATH;
                }
            }

            // Create new child scope.
            Group newChildGroup = newGroup();
            newChildGroup.setName(name);
            scopeGroup.getComponents().add(newChildGroup);
            scope = newChildGroup;
        }

        // Find declaration with conflicting name.
        String name = names[names.length - 1];
        for (Declaration decl: scope.getDeclarations()) {
            if (decl.getName().equals(name)) {
                Assert.check(!(decl instanceof Event));
                String msg = fmt("Can't disable event \"%s\": \"%s\" exists, but is not an event.", absName, absName);
                throw new InvalidInputException(msg);
            }
        }

        // Find component with conflicting name.
        if (scope instanceof Group) {
            for (Component child: ((Group)scope).getComponents()) {
                if (child.getName().equals(name)) {
                    String msg = fmt("Can't disable event \"%s\": \"%s\" exists, but is a component.", absName,
                            absName);
                    throw new InvalidInputException(msg);
                }
            }
        }

        // Add new event declaration.
        Event rslt = newEvent();
        rslt.setName(name);
        scope.getDeclarations().add(rslt);

        // Set controllability.
        Boolean ctrl = null;
        if (origEvent != null) {
            ctrl = origEvent.getControllable();
        } else {
            if (name.startsWith("c_")) {
                ctrl = true;
            }
            if (name.startsWith("u_")) {
                ctrl = false;
            }
        }
        rslt.setControllable(ctrl);

        // Return new event.
        return rslt;
    }

    /**
     * Adds a new automaton to the result specification, that disables the given events.
     *
     * @param eventsToDisable The events to disable.
     */
    private void addAutomaton(List<Event> eventsToDisable) {
        // If no events to disable, don't event add an automaton.
        if (eventsToDisable.isEmpty()) {
            return;
        }

        // Create new automaton.
        Automaton aut = newAutomaton();

        // Set alphabet to the disabled events.
        Alphabet autAlphabet = newAlphabet();
        aut.setAlphabet(autAlphabet);
        for (Event event: eventsToDisable) {
            EventExpression eventRef = newEventExpression();
            eventRef.setEvent(event);
            eventRef.setType(newBoolType());
            autAlphabet.getEvents().add(eventRef);
        }

        // Add single initial location.
        Location loc = newLocation();
        loc.getInitials().add(makeTrue());
        aut.getLocations().add(loc);

        // Add edge to disable the events.
        Edge edge = newEdge();
        loc.getEdges().add(edge);

        edge.getGuards().add(makeFalse());

        for (Event event: eventsToDisable) {
            EventExpression eventRef = newEventExpression();
            eventRef.setEvent(event);
            eventRef.setType(newBoolType());

            EdgeEvent edgeEvent = newEdgeEvent();
            edgeEvent.setEvent(eventRef);

            edge.getEvents().add(edgeEvent);
        }

        // Set automaton name.
        Set<String> names = getSymbolNamesForScope(resultSpec, null);
        String autName = "event_disabler";
        if (names.contains(autName)) {
            autName = getUniqueName(autName, names, EMPTY_SET);
        }
        aut.setName(autName);

        // Add automaton to result specification.
        resultSpec.getComponents().add(aut);
    }
}
