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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Lists.cast;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifMath;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTuple;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.exceptions.InvalidModelException;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.ListProductIterator;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** State space explorer that unfolds the untimed state space expressed by a given CIF specification. */
public class Explorer {
    /** Automata of the specification. */
    public final Automaton[] automata;

    /**
     * Automata of the specification with multiple potential initial locations. Is a subset of {@link #automata}. Is
     * {@code null} after initial states have been constructed.
     */
    private List<Automaton> multiInitAuts;

    /** Variables of the specification. */
    public final PositionObject[] variables;

    /**
     * Discrete variables of the specification, with multiple potential initial values. Is a subset of
     * {@link #variables}. Is {@code null} after initial states have been constructed.
     */
    private List<DiscVariable> multiInitVars;

    /**
     * Names of the variables, filled on first request.
     *
     * @see #getVariableNames
     */
    private String[] variableNames = null;

    /** Mapping of automata, locations and variables to index positions in {@link ExplorerState}. */
    public final Map<PositionObject, Integer> indices;

    /** Algebraic variables of the specification. */
    public final Map<AlgVariable, CollectedAlgVariable> algVariables;

    /** State invariants ordered by location. The {@code null} location is used for invariants of components. */
    public final Map<Location, CollectedInvariants> stateInvs;

    /** Marker predicates of components. */
    public final List<Expression> markeds;

    /**
     * Initialization predicates ordered by location. The {@code null} location is used for initialization predicates of
     * components.
     */
    public final Map<Location, List<Expression>> initialLocs;

    /**
     * Already explored states. {@code null} means the exploration has not started yet, or exploration has finished and
     * no initial state exists.
     */
    public Map<BaseState, BaseState> states = null;

    /** Information how each event is being used. */
    public final List<EventUsage> eventUsages;

    /** Evaluator for expressions in the specification. */
    public final ExpressionEval evaluator = new ExpressionEval();

    /** Unused numbers for numbering states. */
    private int freshStateNumber = 1;

    /** If not {@code null}, newly found states while computing outgoing edges. */
    private List<BaseState> newStates;

    /** State factory. */
    private final AbstractStateFactory stateFactory;

    /**
     * Constructor of the {@link Explorer} class.
     *
     * @param automata Automata of the specification.
     * @param multiInitAuts Automata of the specification with multiple potential initial locations. Must be a subset of
     *     the automata.
     * @param variables Variables of the specification.
     * @param multiInitVars Discrete variables of the specification, with multiple potential initial values. Must be a
     *     subset of the variables.
     * @param algVariables Algebraic variables of the specification.
     * @param stateInvs State invariants ordered by location. The {@code null} location is used for invariants of
     *     components.
     * @param stateEvtExclInvs Per event, state/event exclusion invariants ordered by location. The {@code null}
     *     location is used for invariants of components.
     * @param markeds Marker predicates of components.
     * @param initialLocs Initialization predicates ordered by location. The {@code null} location is used for
     *     initialization predicates of components.
     * @param stateFactory State factory.
     */
    public Explorer(Automaton[] automata, List<Automaton> multiInitAuts, PositionObject[] variables,
            List<DiscVariable> multiInitVars, Map<AlgVariable, CollectedAlgVariable> algVariables,
            Map<Location, CollectedInvariants> stateInvs,
            Map<Event, Map<Location, CollectedInvariants>> stateEvtExclInvs, List<Expression> markeds,
            Map<Location, List<Expression>> initialLocs, AbstractStateFactory stateFactory)
    {
        this.automata = automata;
        this.multiInitAuts = multiInitAuts;
        this.variables = variables;
        this.multiInitVars = multiInitVars;
        this.algVariables = algVariables;
        this.stateInvs = stateInvs;
        this.markeds = markeds;
        this.initialLocs = initialLocs;
        this.stateFactory = stateFactory;

        eventUsages = ExplorerBuilder.getSynchronizingEventMap(automata, stateEvtExclInvs);

        // Fill indices map (automata, locations and variables to their index
        // in the state).
        indices = map();
        for (int i = 0; i < automata.length; i++) {
            indices.put(automata[i], i);
            for (Location loc: automata[i].getLocations()) {
                indices.put(loc, i);
            }
        }
        for (int i = 0; i < variables.length; i++) {
            indices.put(variables[i], i);
        }
    }

    /**
     * Get the initial states of the specification.
     *
     * @param app The application to use to check for termination.
     * @return The initial states if they exist, else {@code null}.
     */
    public List<BaseState> getInitialStates(Application<?> app) {
        // Clear state space.
        states = null;

        // Determine potential initial states.
        List<List<Object>> statesValues = getPotentialInitialStates();
        if (statesValues == null) {
            statesValues = list((List<Object>)null);
        } else if (statesValues.isEmpty()) {
            statesValues.add(null);
        }

        // Determine initial states.
        states = map();
        for (int stateIdx = 0; stateIdx < statesValues.size(); stateIdx++) {
            // Check for termination.
            if (app.isTerminationRequested()) {
                break;
            }

            // Create initial state.
            List<Object> stateValues = statesValues.get(stateIdx);
            InitialState state = stateFactory.makeInitial(this);

            // Partially fill the state with known values.
            state.autLocs = mapc(multiInitAuts.size());
            state.varVals = mapc(multiInitVars.size());
            if (stateValues != null) {
                for (int i = 0; i < multiInitAuts.size(); i++) {
                    state.autLocs.put(multiInitAuts.get(i), (Location)stateValues.get(i));
                }
                int offset = multiInitAuts.size();
                for (int i = 0; i < multiInitVars.size(); i++) {
                    state.varVals.put(multiInitVars.get(i), (Expression)stateValues.get(offset + i));
                }
            }

            // Compute full state. Skip if not a valid initial state.
            NoInitialStateReason invalidReason = null;
            try {
                invalidReason = state.computeCompletely();
            } catch (NoInitialStateException ex) {
                invalidReason = ex.reason;
            }

            if (invalidReason != null) {
                // If this is the only state, inform the user of the reason
                // that the initial state doesn't exist. If there are multiple
                // initial states, we don't want to see warnings for all
                // combinations of values, as some combinations may be
                // obviously useless. Also, we don't want thousands of warnings
                // printed to the console, especially if we end up with some
                // valid initial states.
                if (stateValues == null) {
                    OutputProvider.warn(invalidReason.getMessage());
                }

                // Skip state.
                continue;
            }

            // Add new state.
            addNewState(null, null, state);
        }

        // Release memory.
        multiInitAuts = null;
        multiInitVars = null;

        // Check for no initial states.
        if (states.isEmpty()) {
            states = null;
            return null;
        }

        // Return the initial states.
        return new ArrayList<>(states.keySet());
    }

    /**
     * Returns all combinations of initial values and locations for all potential initial states. If there is only one
     * potential initial state, {@code null} is returned.
     *
     * @return For each potential initial state: for each {@link #multiInitAuts automaton with multiple potential
     *     initial locations}, the initial location, followed by for each {@link #multiInitVars variable with multiple
     *     potential initial values}, the initial value expression. {@code null} is returned if only one potential
     *     initial location is present.
     */
    private List<List<Object>> getPotentialInitialStates() {
        // Detect one potential initial state.
        if (multiInitAuts.isEmpty() && multiInitVars.isEmpty()) {
            return null;
        }

        // Multiple potential initial states. Count the number of states.
        double stateCnt = 1;
        for (Automaton aut: multiInitAuts) {
            stateCnt *= getPotentialInitialLocCount(aut);
        }
        for (DiscVariable var: multiInitVars) {
            stateCnt *= CifValueUtils.getPossibleValueCount(var.getType());
        }

        // Detect too many potential initial state to hold in a list.
        if (stateCnt > Integer.MAX_VALUE) {
            String cntTxt = Double.isInfinite(stateCnt) ? "infinite" : CifMath.realToStr(stateCnt);
            String msg = fmt("Too many potential initial states to store: %s.", cntTxt);
            throw new UnsupportedException(msg);
        }

        // Get potential initial values/locations per variable/automaton.
        int objCnt = multiInitAuts.size() + multiInitVars.size();
        List<List<Object>> objsValues = listc(objCnt);
        for (Automaton aut: multiInitAuts) {
            List<Object> objValues = cast(getPotentialInitialLocs(aut));
            objsValues.add(objValues);
        }
        for (DiscVariable var: multiInitVars) {
            List<Object> objValues = cast(getPotentialInitialValues(var));
            objsValues.add(objValues);
        }

        // Create all combinations, one for each potential initial state.
        List<List<Object>> statesValues = listc((int)stateCnt);
        ListProductIterator<Object> iter = new ListProductIterator<>(objsValues);
        while (iter.hasNext()) {
            List<Object> stateValues = iter.next();
            statesValues.add(stateValues);
        }
        return statesValues;
    }

    /**
     * Returns the number of potential initial locations for the given automaton.
     *
     * @param aut The automaton.
     * @return The number of potential initial locations.
     */
    private double getPotentialInitialLocCount(Automaton aut) {
        double cnt = 0;
        for (Location loc: aut.getLocations()) {
            if (!loc.getInitials().isEmpty()) {
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * Returns the potential initial locations for the given automaton.
     *
     * @param aut The automaton.
     * @return The potential initial locations.
     */
    private List<Location> getPotentialInitialLocs(Automaton aut) {
        List<Location> locs = list();
        for (Location loc: aut.getLocations()) {
            if (!loc.getInitials().isEmpty()) {
                locs.add(loc);
            }
        }
        return locs;
    }

    /**
     * Returns the potential initial values for the given variable.
     *
     * @param var The variable.
     * @return The potential initial values.
     */
    private List<Expression> getPotentialInitialValues(DiscVariable var) {
        VariableValue values = var.getValue();
        Assert.notNull(values);
        if (values.getValues().isEmpty()) {
            // Any value in its domain.
            return CifValueUtils.getPossibleValues(var.getType());
        } else {
            // Specific initial values. May contain expressions that evaluate
            // to the same value.
            return values.getValues();
        }
    }

    /**
     * Compute transitions from the provided originating state.
     *
     * @param origState Start state.
     * @param collectNewStates Whether the computation should store newly found states.
     * @return {@code null} if {@code collectNewStates} is unset, else the successor states from {@code origState} that
     *     were not found at the moment of the call.
     */
    public List<BaseState> computeOutgoing(BaseState origState, boolean collectNewStates) {
        // Set 'newStates'.
        if (collectNewStates) {
            newStates = list();
        } else {
            newStates = null;
        }

        // For each automaton find the explicit edges with true guards.
        List<Map<Event, List<ChosenEdge>>> autEdges;
        autEdges = getAutomataEdges(origState);

        TransitionData transData = new TransitionData(origState, automata.length);

        // Try performing 'tau' events. These can't be prevented by state/event
        // exclusion invariants.
        transData.event = null; // Select 'tau' for the transitions.
        List<List<ChosenEdge>> syncSteps = makeListSteps(automata.length);
        for (int i = 0; i < automata.length; i++) {
            List<ChosenEdge> tauEdges = autEdges.get(i).get(null);
            if (tauEdges == null) {
                continue;
            }

            syncSteps.set(i, tauEdges);
            pickSyncEdge(transData, i, syncSteps);
            syncSteps.set(i, null);
        }

        // Try performing synchronizing events. These may be prevented by
        // state/event exclusion invariants.
        List<List<ChosenEdge>> sendSteps = makeListSteps(automata.length);
        List<List<ChosenEdge>> recvSteps = makeListSteps(automata.length);
        // TODO: This tries every event, maybe it can be limited to events that
        // are actually used on edges? (Assuming no events are monitored
        // by all.)
        for (EventUsage usage: eventUsages) {
            if (!checkParticipation(autEdges, usage)) {
                continue;
            }

            transData.event = usage.event; // Select the event.

            if (!checkStateEventExclInvs(usage, transData)) {
                continue;
            }

            if (usage.isChannel) {
                for (int idx: usage.sendIndices) {
                    sendSteps.set(idx, autEdges.get(idx).get(usage.event));
                }
                for (int idx: usage.recvIndices) {
                    recvSteps.set(idx, autEdges.get(idx).get(usage.event));
                }
            }
            for (int idx: usage.alphabetIndices) {
                List<ChosenEdge> edges = autEdges.get(idx).get(usage.event);
                if (edges == null || edges.isEmpty()) {
                    // Participates, but no edges -> must be a monitor.
                    Assert.check(usage.monitorAuts[idx]);
                } else {
                    syncSteps.set(idx, edges);
                }
            }

            if (usage.isChannel) {
                // TODO: The pickSendEdge could be merged into here.
                pickSendEdge(transData, sendSteps, recvSteps, syncSteps);

                for (int idx: usage.sendIndices) {
                    sendSteps.set(idx, null);
                }
                for (int idx: usage.recvIndices) {
                    recvSteps.set(idx, null);
                }
            } else {
                pickSyncEdge(transData, 0, syncSteps);
            }

            for (int idx: usage.alphabetIndices) {
                syncSteps.set(idx, null);
            }
        }

        // Return the newStates data.
        if (newStates == null) {
            return null;
        }

        List<BaseState> result = newStates;
        newStates = null; // Clean up the explorer variable.
        return result;
    }

    /**
     * Pick an edge for sending, and try to construct states.
     *
     * @param transData Storage area for computing the transition.
     * @param sendSteps Senders that can participate (select one).
     * @param recvSteps Receivers that can participate (select one).
     * @param syncSteps Automata that participate (no send/receive).
     */
    private void pickSendEdge(TransitionData transData, List<List<ChosenEdge>> sendSteps,
            List<List<ChosenEdge>> recvSteps, List<List<ChosenEdge>> syncSteps)
    {
        for (int i = 0; i < automata.length; i++) {
            List<ChosenEdge> chosenEdges = sendSteps.get(i);
            if (chosenEdges == null) {
                continue;
            }

            transData.sendIdx = i;
            for (ChosenEdge ce: chosenEdges) {
                transData.sendEdge = ce;
                pickRecvEdge(transData, recvSteps, syncSteps);
            }
            transData.sendEdge = null;
        }
        transData.sendIdx = -1;
    }

    /**
     * Pick an edge for receiving, and try to construct states.
     *
     * @param transData Storage area for computing the transition.
     * @param recvSteps Receivers that can participate (select one).
     * @param syncSteps Automata that participate (no send/receive).
     */
    private void pickRecvEdge(TransitionData transData, List<List<ChosenEdge>> recvSteps,
            List<List<ChosenEdge>> syncSteps)
    {
        for (int i = 0; i < automata.length; i++) {
            List<ChosenEdge> chosenEdges = recvSteps.get(i);
            if (chosenEdges == null) {
                continue;
            }

            for (ChosenEdge ce: chosenEdges) {
                transData.edges[i] = ce;
                pickSyncEdge(transData, 0, syncSteps);
            }
            transData.edges[i] = null;
        }
    }

    /**
     * Pick an edge for synchronizing, and try to construct states.
     *
     * @param transData Storage area for computing the transition.
     * @param idx Smallest index for finding a participating synchronizing automaton.
     * @param syncSteps Automata that participate (no send/receive).
     */
    private void pickSyncEdge(TransitionData transData, int idx, List<List<ChosenEdge>> syncSteps) {
        while (idx < automata.length && syncSteps.get(idx) == null) {
            idx++;
        }

        if (idx < automata.length) {
            // Found a participating automaton, select its edges.
            for (ChosenEdge ce: syncSteps.get(idx)) {
                transData.edges[idx] = ce;
                pickSyncEdge(transData, idx + 1, syncSteps);
            }
            transData.edges[idx] = null;
            return;
        }

        // Selected edges for every participating automaton, try to create a
        // new state for it.
        constructNewState(transData);
    }

    /**
     * Try to construct a new state for the provided transition.
     *
     * @param transData Transition being performed.
     */
    private void constructNewState(TransitionData transData) {
        ExplorerState newState = stateFactory.makeExplorerState(transData.event, transData.origState);

        BaseState origState = transData.origState;
        ChosenEdge sendEdge = transData.sendEdge;

        // Set new Locations.
        if (sendEdge != null) {
            Location newLoc = sendEdge.edge.getTarget();
            if (newLoc != null) {
                newState.locations[transData.sendIdx] = newLoc;
            }
        }
        for (int i = 0; i < automata.length; i++) {
            if (transData.edges[i] == null) {
                continue;
            }
            Location newLoc = transData.edges[i].edge.getTarget();
            if (newLoc != null) {
                newState.locations[i] = newLoc;
            }
        }

        // Derive sent value.
        Object sendValue = null;
        if (sendEdge != null) {
            EdgeSend edgeSend = (EdgeSend)sendEdge.edgeEvent;
            if (edgeSend.getValue() != null) {
                try {
                    sendValue = origState.eval(edgeSend.getValue(), null);
                } catch (CifEvalException ex) {
                    String msg = fmt("Failed to compute value to send \"%s\" for event \"%s\" in state \"%s\".",
                            CifTextUtils.exprToStr(edgeSend.getValue()), CifTextUtils.getAbsName(transData.event),
                            origState.toString());
                    throw new InvalidModelException(msg, ex);
                }
            }
        }

        // Perform updates (send edge, receive and synchronizing edges).
        if (sendEdge != null) {
            performUpdates(sendEdge.edge.getUpdates(), sendValue, origState, newState);
        }
        for (int i = 0; i < automata.length; i++) {
            if (transData.edges[i] == null) {
                continue;
            }

            performUpdates(transData.edges[i].edge.getUpdates(), sendValue, origState, newState);
        }

        // Add 'newState' to the set of states, possibly after unfolding by the state factory.
        if (stateFactory.unfoldExplorerState) {
            for (BaseState state: stateFactory.doUnfoldExplorerState(transData, newState)) {
                addNewState(transData, sendValue, state);
            }
        } else {
            addNewState(transData, sendValue, newState);
        }
    }

    /**
     * Add a new state to the collection of states, if its state invariants hold.
     *
     * @param transData Transition taken to arrive at the new state. Is {@code null} for initial states.
     * @param sendValue Value being sent during the transition. May be {@code null} if not applicable.
     * @param newState State to be added.
     */
    @SuppressWarnings("unused")
    private void addNewState(TransitionData transData, Object sendValue, BaseState newState) {
        // Find authorative state.
        BaseState authNewState = states.get(newState);
        if (authNewState == null) {
            // State did not exist, check the state invariants. Only for
            // non-initial states, as for initial states it is already
            // checked before.
            if (transData != null) {
                if (!checkStateInvs(newState, stateInvs.get(null))) {
                    return;
                }
                for (Location loc: newState.locations) {
                    if (!checkStateInvs(newState, stateInvs.get(loc))) {
                        return;
                    }
                }
            }

            // State invariants hold, this state really exists, and is
            // authorative.
            newState.stateNumber = getFreshStateNumber();
            states.put(newState, newState);
            authNewState = newState;

            // If requested, copy the new state into the 'newStates' list.
            // Only for non-initial states.
            if (transData != null && newStates != null) {
                newStates.add(authNewState);
            }
        }

        // Add edge (the edge inserts itself between both states). Only for
        // non-initial states.
        if (transData != null) {
            new ExplorerEdge(transData.origState, authNewState, transData.event, sendValue);
        }
    }

    /** After generation of all states, renumber the found states, so the numbering is consecutive. */
    public void renumberStates() {
        int idx = 1;
        for (BaseState state: states.values()) {
            state.stateNumber = idx++;
        }
    }

    /**
     * From the provided state, find the edges that are active for each automaton.
     *
     * <p>
     * For all edges in the result, the guard holds.
     * </p>
     *
     * @param state Originating state.
     * @return For each automaton, the edges enabled for that automaton, ordered by event.
     */
    private List<Map<Event, List<ChosenEdge>>> getAutomataEdges(BaseState state) {
        List<Map<Event, List<ChosenEdge>>> autEdges = listc(automata.length);
        for (Location loc: state.locations) {
            Map<Event, List<ChosenEdge>> edges = map();
            for (Edge edge: loc.getEdges()) {
                if (!evalGuards(edge.getGuards(), state, null)) {
                    continue;
                }

                // Add edge + edge events.
                if (edge.getEvents().isEmpty()) {
                    addEdge(edge, null, edges);
                } else {
                    for (EdgeEvent eve: edge.getEvents()) {
                        addEdge(edge, eve, edges);
                    }
                }
            }
            autEdges.add(edges); // Add the edges of automaton 'loc'.
        }
        return autEdges;
    }

    /**
     * Check that sufficient required automata participate in the event to make it feasible.
     *
     * @param autEdges For each automaton, the edges enabled for that automaton, ordered by event.
     * @param usage The event to investigate.
     * @return Whether the event can be performed.
     */
    private boolean checkParticipation(List<Map<Event, List<ChosenEdge>>> autEdges, EventUsage usage) {
        // For channels, check that we have a sender and a receiver.
        if (usage.isChannel) {
            boolean sendFound = false;
            for (int idx: usage.sendIndices) {
                if (autEdges.get(idx).containsKey(usage.event)) {
                    sendFound = true;
                    break;
                }
            }
            if (!sendFound) {
                return false;
            }

            boolean recvFound = false;
            for (int idx: usage.recvIndices) {
                if (autEdges.get(idx).containsKey(usage.event)) {
                    recvFound = true;
                    break;
                }
            }
            if (!recvFound) {
                return false;
            }
        }

        // Check that all automata with the event in their alphabet also
        // can synchronize.
        for (int idx: usage.alphabetIndices) {
            if (usage.monitorAuts[idx]) {
                continue;
            }
            if (autEdges.get(idx).containsKey(usage.event)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Construct an empty steps structure (for every automaton, a list of edges to choose from).
     *
     * @param length Length of the structure (number of automata).
     * @return An empty steps structure.
     */
    private static List<List<ChosenEdge>> makeListSteps(int length) {
        List<List<ChosenEdge>> steps = listc(length);
        for (int i = 0; i < length; i++) {
            steps.add(null);
        }
        return steps;
    }

    /**
     * Add an edge and its edge-event to the collection of available options to choose from.
     *
     * @param edge Edge that can be chosen.
     * @param eve Edge event of the edge that should be chosen, {@code null} means a tau event is to be performed.
     * @param edges Found available edges so far, ordered by event, where a tau event is represented by a {@code null}
     *     key.
     */
    private static void addEdge(Edge edge, EdgeEvent eve, Map<Event, List<ChosenEdge>> edges) {
        // Extract the event from 'eve'.
        Event event;
        Expression evtExpr = (eve == null) ? null : eve.getEvent();
        if (evtExpr == null || evtExpr instanceof TauExpression) {
            event = null;
        } else {
            event = ((EventExpression)evtExpr).getEvent();
        }

        // Add edge to the list of the found event.
        List<ChosenEdge> chosens = edges.get(event);
        if (chosens == null) {
            chosens = list();
            edges.put(event, chosens);
        }
        chosens.add(new ChosenEdge(edge, eve));
    }

    /**
     * Evaluate the guards of a condition.
     *
     * @param guards Guards to evaluate.
     * @param state State context for the evaluation.
     * @param sendValue Value being sent, or {@code null}.
     * @return Value of the (conjunction of the) guards.
     */
    private static boolean evalGuards(List<Expression> guards, BaseState state, Object sendValue) {
        for (Expression expr: guards) {
            try {
                Boolean val = (Boolean)state.eval(expr, sendValue);
                if (!val) {
                    return false;
                }
            } catch (CifEvalException ex) {
                // Guards in conditions should not fail.
                String msg = fmt("Failed to compute value of guard \"%s\" in state \"%s\".",
                        CifTextUtils.exprToStr(expr), state.toString());
                throw new InvalidModelException(msg, ex);
            }
        }
        return true;
    }

    /**
     * Check the given state invariants in the provided state.
     *
     * @param state State to use for evaluating the invariants.
     * @param cinvs State invariants to check. May be {@code null} if no invariants to check.
     * @return Whether the state invariants hold in the state.
     */
    private boolean checkStateInvs(BaseState state, CollectedInvariants cinvs) {
        if (cinvs == null) {
            return true;
        }

        return checkStateInvs(state, cinvs.getNoneInvariants()) && checkStateInvs(state, cinvs.getPlantInvariants())
                && checkStateInvs(state, cinvs.getRequirementInvariants())
                && checkStateInvs(state, cinvs.getSupervisorInvariants());
    }

    /**
     * Check the given state invariants in the provided state.
     *
     * @param state State to use for evaluating the invariants.
     * @param invs State invariants to check.
     * @return Whether the state invariants hold in the state.
     */
    private boolean checkStateInvs(BaseState state, List<Invariant> invs) {
        for (Invariant inv: invs) {
            try {
                Object val = state.eval(inv.getPredicate(), null);
                boolean b = (boolean)val;
                if (!b) {
                    return false;
                }
            } catch (CifEvalException ex) {
                String msg = fmt("Failed to compute value of invariant \"%s\" in state \"%s\".",
                        CifTextUtils.invToStr(inv, false), state.toString());
                throw new InvalidModelException(msg, ex);
            }
        }
        return true;
    }

    /**
     * Check the state/exclusion invariants for the given event transition.
     *
     * @param usage The event usage data of the event of the transition.
     * @param transData The transition data of the transition.
     * @return Whether the event is allowed by the invariants.
     */
    private boolean checkStateEventExclInvs(EventUsage usage, TransitionData transData) {
        Assert.check(usage.event == transData.event);

        // Check global state/event exclusion invariants.
        BaseState state = transData.origState;
        CollectedInvariants invs = usage.stateEvtExclInvs.get(null);
        if (!checkStateEventExclInvs(state, invs)) {
            return false;
        }

        // Check location state/event exclusion invariants.
        for (Location loc: state.locations) {
            CollectedInvariants locInvs = usage.stateEvtExclInvs.get(loc);
            if (!checkStateEventExclInvs(state, locInvs)) {
                return false;
            }
        }

        // All state/event exclusion invariants allow the event.
        return true;
    }

    /**
     * Check the given state/event exclusion invariants in the provided state.
     *
     * @param state State to use for evaluating the invariants.
     * @param cinvs State/event exclusion invariants to check. May be {@code null} if no invariants to check.
     * @return Whether the state/event exclusion invariants allow the event.
     */
    private boolean checkStateEventExclInvs(BaseState state, CollectedInvariants cinvs) {
        if (cinvs == null) {
            return true;
        }

        return checkStateEventExclInvs(state, cinvs.getNoneInvariants())
                && checkStateEventExclInvs(state, cinvs.getPlantInvariants())
                && checkStateEventExclInvs(state, cinvs.getRequirementInvariants())
                && checkStateEventExclInvs(state, cinvs.getSupervisorInvariants());
    }

    /**
     * Check the given state/event exclusion invariants in the provided state.
     *
     * @param state State to use for evaluating the invariants.
     * @param invs State/event exclusion invariants to check.
     * @return Whether the state/event exclusion invariants allow the event.
     */
    private boolean checkStateEventExclInvs(BaseState state, List<Invariant> invs) {
        for (Invariant inv: invs) {
            try {
                Object val = state.eval(inv.getPredicate(), null);
                boolean b = (boolean)val;
                switch (inv.getInvKind()) {
                    case EVENT_DISABLES:
                        // If predicate holds, event is disabled.
                        if (b) {
                            return false;
                        }
                        break;

                    case EVENT_NEEDS:
                        // If predicate doesn't hold, event is disabled.
                        if (!b) {
                            return false;
                        }
                        break;

                    default:
                        String msg = "Not a state/event excl inv: " + inv;
                        throw new RuntimeException(msg);
                }
            } catch (CifEvalException ex) {
                String msg = fmt("Failed to compute value of invariant \"%s\" in state \"%s\".",
                        CifTextUtils.invToStr(inv, false), state.toString());
                throw new InvalidModelException(msg, ex);
            }
        }
        return true;
    }

    /**
     * Perform the provided updates.
     *
     * @param updates Updates to perform.
     * @param sendValue Value communicated with the event, or {@code null}.
     * @param origState Originating state.
     * @param newState Storage of the results.
     */
    private void performUpdates(List<Update> updates, Object sendValue, BaseState origState, BaseState newState) {
        for (Update upd: updates) {
            performUpdate(upd, sendValue, origState, newState);
        }
    }

    /**
     * Perform an update.
     *
     * @param upd Update to perform.
     * @param sendValue Value communicated with the event, or {@code null}.
     * @param origState Originating state (values for the RHS).
     * @param newState Target state (storage for updated variables).
     */
    private void performUpdate(Update upd, Object sendValue, BaseState origState, BaseState newState) {
        // Recursively unfold the if/elif/else updates.
        if (upd instanceof IfUpdate) {
            IfUpdate iu = (IfUpdate)upd;
            boolean guard = evalGuards(iu.getGuards(), origState, sendValue);
            if (guard) {
                performUpdates(iu.getThens(), sendValue, origState, newState);
                return;
            }
            for (ElifUpdate eu: iu.getElifs()) {
                guard = evalGuards(eu.getGuards(), origState, sendValue);
                if (guard) {
                    performUpdates(eu.getThens(), sendValue, origState, newState);
                    return;
                }
            }
            performUpdates(iu.getElses(), sendValue, origState, newState);
            return;
        }

        // Perform the assignment.
        if (upd instanceof Assignment) {
            Assignment asg = (Assignment)upd;

            Object rhs;
            try {
                rhs = origState.eval(asg.getValue(), sendValue);
            } catch (CifEvalException ex) {
                String msg = fmt("Failed to compute value to assign for assignment \"%s := %s\" in state \"%s\".",
                        CifTextUtils.exprToStr(asg.getAddressable()), CifTextUtils.exprToStr(asg.getValue()),
                        origState.toString());
                throw new InvalidModelException(msg, ex);
            }

            checkTypeRangeBoundaries(asg.getAddressable().getType(), asg.getValue().getType(), true, rhs,
                    asg.getAddressable(), asg.getValue(), origState);

            assignValue(rhs, asg.getAddressable(), sendValue, origState, newState);
        }
    }

    /**
     * Check that the assigned rhs value fits in the range of the lhs addressable.
     *
     * @param lhsType Type of the lhs addressable.
     * @param rhsType Type of the rhs value.
     * @param performTypecheck If set, perform a type check on the lhs and rhs types before checking the values. Unset
     *     when the result is already known to fail and the actual values need to be verified instead.
     * @param rhsValue Value to assign.
     * @param asgnLhs Left hand side of the assignment to be performed (for reporting errors).
     * @param asgnRhs Right hand side of the assignment to be performed (for reporting errors).
     * @param state State used for the evaluation.
     */
    @SuppressWarnings("unchecked")
    protected void checkTypeRangeBoundaries(CifType lhsType, CifType rhsType, boolean performTypecheck, Object rhsValue,
            Expression asgnLhs, Expression asgnRhs, BaseState state)
    {
        // If fully contained, no need to do any additional checking.
        final RangeCompat inRange = RangeCompat.CONTAINED;

        lhsType = CifTypeUtils.normalizeType(lhsType);
        rhsType = CifTypeUtils.normalizeType(rhsType);
        if (performTypecheck && CifTypeUtils.checkTypeCompat(lhsType, rhsType, inRange)) {
            return;
        }

        // rhsType is not contained in lhsType, perform value checking.
        // Most types are dealt with inside the type check above.

        if (lhsType instanceof IntType) {
            IntType lhsInt = (IntType)lhsType;
            int rhsVal = (int)rhsValue;
            if (lhsInt.getLower() != null && lhsInt.getLower() > rhsVal) {
                String msg = fmt("Assigned value %d is too small in assignment \"%s := %s\" in state \"%s\".", rhsVal,
                        CifTextUtils.exprToStr(asgnLhs), CifTextUtils.exprToStr(asgnRhs), state.toString());
                throw new InvalidModelException(msg);
            }
            if (lhsInt.getUpper() != null && lhsInt.getUpper() < rhsVal) {
                String msg = fmt("Assigned value %d is too big in assignment \"%s := %s\" in state \"%s\".", rhsVal,
                        CifTextUtils.exprToStr(asgnLhs), CifTextUtils.exprToStr(asgnRhs), state.toString());
                throw new InvalidModelException(msg);
            }
            return;
        } else if (lhsType instanceof DictType) {
            DictType lhsDict = (DictType)lhsType;
            DictType rhsDict = (DictType)rhsType;
            if (!CifTypeUtils.checkTypeCompat(lhsDict.getKeyType(), rhsDict.getKeyType(), inRange)) {
                Map<Object, Object> rhsVal = (Map<Object, Object>)rhsValue;
                for (Object val: rhsVal.keySet()) {
                    checkTypeRangeBoundaries(lhsDict.getKeyType(), rhsDict.getKeyType(), false, val, asgnLhs, asgnRhs,
                            state);
                }
            }
            if (!CifTypeUtils.checkTypeCompat(lhsDict.getValueType(), rhsDict.getValueType(), inRange)) {
                Map<Object, Object> rhsVal = (Map<Object, Object>)rhsValue;
                for (Object val: rhsVal.values()) {
                    checkTypeRangeBoundaries(lhsDict.getValueType(), rhsDict.getValueType(), false, val, asgnLhs,
                            asgnRhs, state);
                }
            }
            return;
        } else if (lhsType instanceof ListType) {
            ListType lhsList = (ListType)lhsType;
            ListType rhsList = (ListType)rhsType;
            List<Object> rhsVal = (List<Object>)rhsValue;
            int rhsSize = rhsVal.size();
            if (lhsList.getLower() != null && lhsList.getLower() > rhsSize) {
                String msg = fmt(
                        "Assigned list \"%s\" has too few elements in assignment \"%s := %s\" in state \"%s\".",
                        CifEvalUtils.objToStr(rhsVal), CifTextUtils.exprToStr(asgnLhs), CifTextUtils.exprToStr(asgnRhs),
                        state.toString());
                throw new InvalidModelException(msg);
            }
            if (lhsList.getUpper() != null && lhsList.getUpper() < rhsSize) {
                String msg = fmt(
                        "Assigned list \"%s\" has too many elements in assignment \"%s := %s\" in state \"%s\".",
                        CifEvalUtils.objToStr(rhsVal), CifTextUtils.exprToStr(asgnLhs), CifTextUtils.exprToStr(asgnRhs),
                        state.toString());
                throw new InvalidModelException(msg);
            }
            for (Object val: rhsVal) {
                checkTypeRangeBoundaries(lhsList.getElementType(), rhsList.getElementType(), true, val, asgnLhs,
                        asgnRhs, state);
            }
            return;
        } else if (lhsType instanceof SetType) {
            SetType lhsSet = (SetType)lhsType;
            SetType rhsSet = (SetType)rhsType;
            Set<Object> rhsVal = (Set<Object>)rhsValue;
            for (Object val: rhsVal) {
                checkTypeRangeBoundaries(lhsSet.getElementType(), rhsSet.getElementType(), true, val, asgnLhs, asgnRhs,
                        state);
            }
            return;
        } else if (lhsType instanceof TupleType) {
            TupleType lhsTuple = (TupleType)lhsType;
            TupleType rhsTuple = (TupleType)rhsType;
            CifTuple rhsVal = (CifTuple)rhsValue;
            for (int i = 0; i < rhsVal.size(); i++) {
                checkTypeRangeBoundaries(lhsTuple.getFields().get(i).getType(), rhsTuple.getFields().get(i).getType(),
                        true, rhsVal.get(i), asgnLhs, asgnRhs, state);
            }
            return;
        } else {
            String msg = fmt("Unexpected type %s encountered.", lhsType);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Assign a value to a variable.
     *
     * @param value Value to assign.
     * @param addr Variable (part of variable) to assign to.
     * @param sendValue Value being communicated with the event if available, else {@code null}.
     * @param origState Originating state.
     * @param newState State to assign into. Must not be the same state as the originating state.
     */
    protected void assignValue(Object value, Expression addr, Object sendValue, BaseState origState,
            BaseState newState)
    {
        if (addr instanceof ProjectionExpression) {
            assignProjection(value, (ProjectionExpression)addr, sendValue, origState, newState);
            return;
        } else if (addr instanceof TupleExpression) {
            TupleExpression a = (TupleExpression)addr;
            CifTuple ct = (CifTuple)value;
            Assert.check(ct.size() == a.getFields().size());
            for (int i = 0; i < ct.size(); i++) {
                assignValue(ct.get(i), a.getFields().get(i), sendValue, origState, newState);
            }
            return;
        } else if (addr instanceof DiscVariableExpression) {
            DiscVariableExpression a = (DiscVariableExpression)addr;
            DiscVariable dv = a.getVariable();
            newState.setVarValue(dv, value);
            return;
        } else if (addr instanceof ContVariableExpression) {
            ContVariableExpression a = (ContVariableExpression)addr;
            ContVariable cv = a.getVariable();
            newState.setVarValue(cv, value);
            return;
        } else {
            String msg = fmt("Found unexpected LHS %s in assignment.", addr);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Assign a projected variable.
     *
     * @param value Value to assign.
     * @param addr Address to assign the value to.
     * @param sendValue Value being communicated with the event if available, else {@code null}.
     * @param origState Originating state for evaluation of index expressions.
     * @param newState State to assign into.
     */
    private void assignProjection(Object value, ProjectionExpression addr, Object sendValue, BaseState origState,
            BaseState newState)
    {
        Expression subAddr = addr;
        while (subAddr instanceof ProjectionExpression) {
            ProjectionExpression e = (ProjectionExpression)subAddr;
            subAddr = e.getChild();
        }
        Assert.check(subAddr instanceof DiscVariableExpression);
        DiscVariableExpression a = (DiscVariableExpression)subAddr;
        DiscVariable dv = a.getVariable();
        Object val = newState.getVarValue(dv);

        val = modifyValue(val, (ProjectionExpression)subAddr.eContainer(), value, addr, origState, sendValue);
        newState.setVarValue(dv, val);
    }

    /**
     * Modify a value, setting 'newValue' at 'root', while making shallow copies on the path from 'expr' to 'root' of
     * the value.
     *
     * <p>
     * Note that the path is upside down, 'expr' is a recursively contained child of 'root'.
     * </p>
     *
     * @param value Value to shallowly copy and modify.
     * @param expr Path node to the 'root'.
     * @param newValue New value to set at 'root'.
     * @param root Top projection expression denoting the end.
     * @param origState Originating state, used for evaluation of the index expressions.
     * @param sendValue Value being communicated with the event if available, else {@code null}.
     * @return The modified value.
     */
    @SuppressWarnings("unchecked")
    private Object modifyValue(Object value, ProjectionExpression expr, Object newValue, ProjectionExpression root,
            BaseState origState, Object sendValue)
    {
        // Evaluate projection index.
        Object index;
        try {
            index = origState.eval(expr.getIndex(), sendValue);
        } catch (CifEvalException ex) {
            String msg = fmt("Failed to compute projection index \"%s\" in state \"%s\".",
                    CifTextUtils.exprToStr(expr.getIndex()), origState.toString());
            throw new InvalidModelException(msg, ex);
        }

        // Handle based on kind of projected data type.
        if (value instanceof CifTuple) {
            // Make shallow clone.
            CifTuple value2 = (CifTuple)value;
            CifTuple val = new CifTuple(value2.size());
            val.addAll(value2);

            // Check and convert index.
            int idx = (int)index;
            Assert.check(idx >= 0 && idx < val.size());

            // Walk further up to replace child nodes.
            if (expr != root) {
                newValue = modifyValue(val.get(idx), (ProjectionExpression)expr.eContainer(), newValue, root, origState,
                        sendValue);
            }

            // Assign modified value.
            val.set(idx, newValue);
            return val;
        } else if (value instanceof List<?>) {
            // Make shallow clone.
            List<Object> value2 = (List<Object>)value;
            List<Object> val = listc(value2.size());
            val.addAll(value2);

            // Check and convert index.
            if (val.isEmpty()) {
                String msg = fmt("Cannot index into an empty list while assigning a value to projected variable \"%s\" "
                        + "in state \"%s\".", CifTextUtils.exprToStr(root), origState.toString());
                throw new InvalidModelException(msg);
            }
            int idx = (int)index;
            if (idx >= val.size()) {
                String msg = fmt(
                        "List index \"%d\" is too large, biggest allowed index is \"%d\", "
                                + "while assigning a value to projected variable \"%s\" in state \"%s\".",
                        idx, val.size() - 1, CifTextUtils.exprToStr(root), origState.toString());
                throw new InvalidModelException(msg);
            }
            if (idx < 0) {
                if (idx + val.size() < 0) {
                    String msg = fmt(
                            "List index %d is too small, smallest allowed index is \"%d\", while assigning "
                                    + "a value to projected variable \"%s\" in state \"%s\".",
                            index, -val.size(), CifTextUtils.exprToStr(root), origState.toString());
                    throw new InvalidModelException(msg);
                }
                idx += val.size();
            }

            // Walk further up to replace child nodes.
            if (expr != root) {
                newValue = modifyValue(val.get(idx), (ProjectionExpression)expr.eContainer(), newValue, root, origState,
                        sendValue);
            }

            // Assign modified value.
            val.set(idx, newValue);
            return val;
        } else if (value instanceof Map<?, ?>) {
            Map<Object, Object> value2 = (Map<Object, Object>)value;
            Map<Object, Object> val = mapc(value2.size());
            val.putAll(value2);

            // Walk further up to replace child nodes.
            if (expr != root) {
                // Not at root, key must exist.
                if (!val.containsKey(index)) {
                    String msg = "Key error, value \"%s\" is not a key in the dictionary, while assigning a "
                            + "value to projected variable \"%s\" in state \"%s\".";
                    msg = fmt(msg, index, CifTextUtils.exprToStr(root), origState.toString());
                    throw new InvalidModelException(msg);
                }

                newValue = modifyValue(val.get(index), (ProjectionExpression)expr.eContainer(), newValue, root,
                        origState, sendValue);
            }

            // Assign modified value, or create new entry.
            val.put(index, newValue);
            return val;
        } else {
            String msg = fmt("Do not know how to modify value %s.", value);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Get a unique number for a new state in the exploration.
     *
     * @return Unique number (up to max int).
     */
    public int getFreshStateNumber() {
        return freshStateNumber++;
    }

    /**
     * Get the names of the variables in the state.
     *
     * @return Names of the variables in the state.
     */
    public String[] getVariableNames() {
        if (variableNames != null) {
            return variableNames;
        }

        variableNames = new String[variables.length];
        for (int i = 0; i < variables.length; i++) {
            variableNames[i] = CifTextUtils.getAbsName(variables[i]);
        }
        return variableNames;
    }

    /** Minimizes the edges of the explored state space, by removing duplicate edges. */
    public void minimizeEdges() {
        // If no states, then no edges to minimize.
        if (states == null) {
            return;
        }

        // Process all states.
        for (BaseState state: states.keySet()) {
            // Initialize.
            List<ExplorerEdge> edges = state.getOutgoingEdges();
            Map<Event, Set<BaseState>> eventMap = mapc(edges.size());

            // Process all edges.
            Iterator<ExplorerEdge> edgesIter = edges.iterator();
            while (edgesIter.hasNext()) {
                ExplorerEdge edge = edgesIter.next();

                // Get already encountered target states for this event.
                Set<BaseState> targetStates = eventMap.get(edge.event);
                if (targetStates == null) {
                    // New event: no duplicate. Update mapping and continue.
                    targetStates = set(edge.next);
                    eventMap.put(edge.event, targetStates);
                    continue;
                }

                // Check for duplicate edge target state for the event of this
                // edge.
                if (targetStates.contains(edge.next)) {
                    edgesIter.remove();
                } else {
                    targetStates.add(edge.next);
                }
            }
        }
    }
}
