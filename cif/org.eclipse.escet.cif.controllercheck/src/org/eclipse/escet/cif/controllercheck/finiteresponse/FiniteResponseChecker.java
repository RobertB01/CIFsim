package org.eclipse.escet.cif.controllercheck.finiteresponse;

import static org.eclipse.escet.cif.common.CifCollectUtils.collectAutomata;
import static org.eclipse.escet.cif.common.CifCollectUtils.collectDiscAndInputVariables;
import static org.eclipse.escet.cif.common.CifEventUtils.getAlphabet;
import static org.eclipse.escet.cif.common.CifEventUtils.getEvents;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifValueUtils.createConjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.createDisjunction;
import static org.eclipse.escet.cif.controllercheck.finiteresponse.EventLoopSearch.searchEventLoops;
import static org.eclipse.escet.cif.databased.multivaluesynthesis.MvSpecBuilder.convertVariables;
import static nl.tue.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.intersection;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;
import static java.util.Collections.EMPTY_LIST; // XXX

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.cif.controllercheck.options.PrintOutputOption;
import org.eclipse.escet.cif.databased.multivaluesynthesis.MvSpecBuilder;
import org.eclipse.escet.cif.databased.multivaluetrees.Node;
import org.eclipse.escet.cif.databased.multivaluetrees.Tree;
import org.eclipse.escet.cif.databased.multivaluetrees.TreeVariable;
import org.eclipse.escet.cif.databased.multivaluetrees.VarInfo;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Sets;

/**
 * Class for checking a Cif 3 specification has finite response.
 */
public class FiniteResponseChecker {
    /** Automata of the specification. */
    private List<Automaton> automata = list();

    /** Variables of the specification. */
    private List<Declaration> variables = list();

    /**
     * The forcible event set. Iteratively, this set is updated if an event is found in the alphabet
     * of an automaton, but not in any of its potential forcible-event loops.
     */
    private Set<Event> forcibleEvents = set();

    /** Whether the forcible events have changed after the last computation of the forcible independent variables. */
    private boolean forcibleEventsChanged = true;

    /** Mapping between events and the variables updated by edges labeled with that event. */
    private Map<Event, Set<Declaration>> eventVarUpdate;

    /** Variables that are not forcible independent, i.e., their value is can be updated by a forcible event. */
    VarInfo[] nonFIVinfos;

    /** Mapping between events and their global guard as a MDD node. */
    private Map<Event, Node> globalGuards;

    /** Builder for the MDD tree. */
    private MvSpecBuilder builder;

    /**
     * Performs the finite response checker for a Cif 3 specification.
     *
     * @param spec The specification to check for finite response.
     * @return {@code true} if the specification has finite response.
     */
    public boolean checkSystem(Specification spec) {
        // Initially, the forcible events are the controllable events in the system.
        collectAutomata(spec, automata);
        collectDiscAndInputVariables(spec, variables);
        collectControllableEvents(spec, forcibleEvents);
        eventVarUpdate = getEventVarUpdate();

        if (automata.isEmpty()) {
            OutputProvider.out("The specification has finite response.");
            OutputProvider.out("Warning: the specification contains 0 automata.");
            return true;
        }

        if (forcibleEvents.isEmpty()) {
            OutputProvider.out("The specification has finite response.");
            OutputProvider.out("Warning: the specification contains 0 forcible events.");
            return true;
        }

        // Construct the MDD tree.
        final int readIndex = 0;
        final int writeIndex = 1;
        TreeVariable[] treeVars = convertVariables(variables, readIndex, writeIndex);
        builder = new MvSpecBuilder(treeVars, readIndex, writeIndex);

        // Get the global guards in the tree.
        globalGuards = collectGlobalGuards(forcibleEvents);

        // Check all automata for forcible-event loops. If an automata has a forcible event in its alphabet, but not
        // in any of its potential forcible loops, then this event is removed from the forcible-event set. We keep
        // repeating the search until the forcible event set is not updated anymore or the set is empty.
        int oldSize;
        int iterationNumber = 1;

        do {
            OutputProvider.dbg(fmt("Iteration %d.", iterationNumber));
            iterationNumber++;
            oldSize = forcibleEvents.size();

            for (Automaton aut: automata) {
                checkAutomaton(aut);
            }
        } while (oldSize != forcibleEvents.size() && !forcibleEvents.isEmpty());

        if (!forcibleEvents.isEmpty()) {
            OutputProvider.out("The specification does not have finite response, at least one forcible-event " +
                               "loop was found.");

            if (PrintOutputOption.print()) {
                OutputProvider.out("The following events might still occur in a forcible loop.");
                for (Event event: forcibleEvents) {
                    OutputProvider.out(getAbsName(event) + ", ");
                }
            }

        } else {
            OutputProvider.out("The specification has finite response.");
        }

        return forcibleEvents.isEmpty();
    }

    /**
     * Checks an automaton on the existence of potential forcible-event loops, i.e., loops in the automaton that are
     * not forcible unconnectable. This function removes events from {@code forcibleEvents} if they occur in the
     * alphabet of the automaton, but not in any potential loop.
     *
     * @param aut The automaton to check for potential forcible-event loops.
     */
    private void checkAutomaton(Automaton aut) {
        // Check if the automaton has any forcible events in its alphabet.
        if (isEmptyIntersection(getAlphabet(aut), forcibleEvents)) return;

        // Find the forcible-event loops in the automata. Here we ignore guards and updates.
        Set<EventLoop> forcibleLoops = searchEventLoops(aut, forcibleEvents);

        // Print the results.
        if (!forcibleLoops.isEmpty()) {
            OutputProvider.dbg();
            OutputProvider.dbg(fmt("The following events have initially been encountered in a forcible-event loop " +
                                   "of automaton %s", aut.getName()));
            for (EventLoop eventLoop: forcibleLoops) OutputProvider.dbg("* " + eventLoop.toString());
            OutputProvider.dbg(); // Print empty line.
        }

        // Calculate the non forcible independent variables. As we later have to abstract from these in the
        // global guards. Variables are cached, only calculate when the forcible event set has changed.
        if (forcibleEventsChanged) {
            forcibleEventsChanged = false;

            BitSet bits = new BitSet(builder.tree.varInfoLevels.length);
            for (Event evt: forcibleEvents) {
                for (Declaration var: eventVarUpdate.getOrDefault(evt, set())) {
                    VarInfo varInfo = builder.tree.getVarInfo(var, 0);

                    bits.set(varInfo.level);
                }
            }

            nonFIVinfos = new VarInfo[bits.cardinality()];
            int nextFree = 0;
            for (int level = bits.nextSetBit(0); level >= 0; level = bits.nextSetBit(level + 1)) {
                nonFIVinfos[nextFree] = builder.tree.varInfoLevels[level];
                nextFree++;
            }
        }

        // Collect which events occur in potential-forcible loops.
        Set<Event> evtsInPotentialFLoop = set();

        // Check for if the loop is forcible unconnectable, if not, it is a potential forcible loop in the system.
        for (EventLoop forcibleLoop: forcibleLoops) {
            if (isUnconnectable(forcibleLoop, nonFIVinfos)) {
                OutputProvider.dbg(fmt("%s is forcible-unconnectable", forcibleLoop.toString()));

            } else {
                OutputProvider.dbg(fmt("Warning: %s is not forcible-unconnectable", forcibleLoop.toString()));
                evtsInPotentialFLoop.addAll(forcibleLoop.events);
            }
        }

        // Determine which events are in the alphabet of the automaton, but not in any of its potential forcible loops.
        Set<Event> eventsInAlphabetNotInLoop = Sets.difference(getAlphabet(aut), evtsInPotentialFLoop);

        // Remove the events that are in the alphabet of the automaton, but not in any of its potential forcible loops
        // from the forcible event set. If the set changes, update ForcibleEventsChanged.
        forcibleEventsChanged = forcibleEvents.removeAll(eventsInAlphabetNotInLoop);
    }

    /**
     * Checks whether the forcible loop is forcible unconnectable. Forcible unconnectabiility is checked after
     * abstracting from the events in nonFIV.
     *
     * @param forcibleLoop The loop to check to be forcible unconnectable.
     * @param nonFIV The variables that are updated by forcible events, not forcible independent variables.
     * @return {@code true} if the loop is forcible unconnectable, {@code false} otherwise.
     */
    private boolean isUnconnectable(EventLoop forcibleLoop, VarInfo[] nonFIV) {
        Node n = Tree.ONE;
        for (Event evt: forcibleLoop.events) {
            Node g = globalGuards.get(evt);
            Node gAbstract = builder.tree.variableAbstractions(g, nonFIV);
            n = builder.tree.conjunct(n, gAbstract);
            if (n == Tree.ZERO) return true;
        }
        return false;
    }

    /**
     * Constructs the mapping between events and the variables that are updated by edges labeled by that event.
     *
     * @return The constructed mapping.
     */
    private Map<Event, Set<Declaration>> getEventVarUpdate() {
        Map<Event, Set<Declaration>> eventVarUpdate = map();
        // Iterate over all edges in the specification, and determine their updates.
        for (Automaton aut: automata) {
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    for (Update update: edge.getUpdates()) {
                        if (update instanceof Assignment) {
                            Assignment assignment = (Assignment)update;
                            collectEventsAddressable(assignment.getAddressable(), getEvents(edge), eventVarUpdate);

                        } else {
                            // 'If' updates are not supported.
                            throw new AssertionError(fmt("Unexpected update encountered: '%s'.", update.toString()));
                        }
                    }
                }
            }
        }
        return eventVarUpdate;
    }

    /**
     * Collects the relations between events and the variable from an addressable.
     *
     * @param addressable The addressable to collect, may only be a discrete variable.
     * @param events The events that are labeled on the edge with this addressable.
     * @param eventVarUpdate The map in which to save the 'event updates variable' information.
     */
    private void collectEventsAddressable(Expression addressable, Set<Event> events, Map<Event,
                                           Set<Declaration>> eventVarUpdate)
    {
        if (addressable instanceof DiscVariableExpression) {
            DiscVariable adressedVar = ((DiscVariableExpression)addressable).getVariable();

            // Add the 'event updates variable' information in the map.
            for (Event evt: events) {
                Set<Declaration> vars = eventVarUpdate.getOrDefault(evt, set());
                vars.add(adressedVar);
                eventVarUpdate.put(evt, vars);
            }

        } else {
            // Partial assignments and multi-assignments are not supported.
            throw new AssertionError(fmt("Unexpected addressable encountered: '%s'.", addressable.toString()));
        }
    }

    /**
     * Constructs a mapping between events and their global guards as a MDD node. The event is enabled in the
     * specification if and only if its global guard evaluates to {@code true}. Multiple guards on a single edge
     * are combined with 'and'. Guards for an event that is labeled on more than one edge in an automaton are
     * combined with 'or'.
     *
     * @param events The events for which to collect the global guards.
     * @return A mapping between events and their global guards as a MDD node.
     */
    public Map<Event, Node> collectGlobalGuards(Set<Event> events) {
        Map<Event, List<Expression>> eventGlobalGuard = map();

        for (Automaton aut: automata) {
            Map<Event, List<Expression>> eventAutomGuard = map();
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    Set<Event> intersection = intersection(getEvents(edge), events);
                    if (intersection.isEmpty() || edge.getGuards().isEmpty()) continue;

                    // An edge with a guard, labeled with an event from the supplied set has been found. If the edge
                    // contains multiple guards, the guards are combined via 'and'.
                    for (Event evt: intersection) {
                        List<Expression> edgeGuards = eventAutomGuard.getOrDefault(evt, list());
                        edgeGuards.add(createConjunction(deepclone(edge.getGuards())));
                        eventAutomGuard.put(evt, edgeGuards);
                    }
                }
            }

            // An event is enabled in an automaton if at least one edge with that event is enabled. Hence,
            // the automaton guards are combined via 'or'.
            for (Entry<Event, List<Expression>> entry: eventAutomGuard.entrySet()) {
                List<Expression> globalGuard = eventGlobalGuard.getOrDefault(entry.getKey(), list());
                globalGuard.add(createDisjunction(entry.getValue()));
                eventGlobalGuard.put(entry.getKey(), globalGuard);
            }
        }

        Map<Event, Node> eventNode = mapc(events.size());
        for (Event event: events) {
            List<Expression> globalGuard = eventGlobalGuard.getOrDefault(event, EMPTY_LIST);
            Node nodeGuard = globalGuard.isEmpty() ? Tree.ONE : builder.getPredicateConvertor().convert(globalGuard).get(1);
            eventNode.put(event, nodeGuard);
        }
        return eventNode;
    }

    /**
     * Collect the controllable events declared in the given component (recursively).
     *
     * @param comp The component.
     * @param ctrlEvents The controllable events collected so far. Is modified in-place.
     */
    public static void collectControllableEvents(ComplexComponent comp, Set<Event> ctrlEvents) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Event && ((Event)decl).getControllable()) ctrlEvents.add((Event)decl);
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectControllableEvents((ComplexComponent)child, ctrlEvents);
            }
        }
    }
}
