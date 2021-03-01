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

package org.eclipse.escet.cif.simulator.runtime.model;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.ode.sampling.StepNormalizer;
import org.apache.commons.math3.ode.sampling.StepNormalizerBounds;
import org.apache.commons.math3.ode.sampling.StepNormalizerMode;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.cif.simulator.CifSimulatorContext;
import org.eclipse.escet.cif.simulator.input.AutomaticInputComponent;
import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.input.InputComponent;
import org.eclipse.escet.cif.simulator.input.InteractiveConsoleInputComponent;
import org.eclipse.escet.cif.simulator.input.InteractiveGuiInputComponent;
import org.eclipse.escet.cif.simulator.input.SvgInputComponent;
import org.eclipse.escet.cif.simulator.input.trace.TraceInputComponent;
import org.eclipse.escet.cif.simulator.options.CompleteModeOption;
import org.eclipse.escet.cif.simulator.options.DistributionSeedOption;
import org.eclipse.escet.cif.simulator.options.EnvironmentEventsOption;
import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.options.InputMode;
import org.eclipse.escet.cif.simulator.options.InputModeOption;
import org.eclipse.escet.cif.simulator.options.MaxTimePointTolOption;
import org.eclipse.escet.cif.simulator.output.DebugOutputType;
import org.eclipse.escet.cif.simulator.output.NormalOutputOption;
import org.eclipse.escet.cif.simulator.output.NormalOutputType;
import org.eclipse.escet.cif.simulator.output.print.RuntimePrintDecls;
import org.eclipse.escet.cif.simulator.output.svgviz.RuntimeCifSvgDecls;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;
import org.eclipse.escet.cif.simulator.runtime.meta.StateObjectType;
import org.eclipse.escet.cif.simulator.runtime.ode.OdeSolver;
import org.eclipse.escet.cif.simulator.runtime.ode.OdeStateEvent;
import org.eclipse.escet.cif.simulator.runtime.ode.Trajectories;
import org.eclipse.escet.cif.simulator.runtime.transitions.ActualTargetTime;
import org.eclipse.escet.cif.simulator.runtime.transitions.CommunicationTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.ListProductIterator;

/**
 * Runtime specification representation.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class RuntimeSpec<S extends RuntimeState> {
    /** The class loader to use to load additional resources, or {@code null} if not available. */
    public ClassLoader resourceClassLoader;

    /** The input component. */
    public final InputComponent<S> input;

    /** Is {@link CompleteModeOption complete mode} enabled? */
    public final boolean complete;

    /** The model time delta used for real-time simulation, or {@code null} if real-time simulation is disabled. */
    private final Double modelTimeDelta;

    /** The simulator runtime context. Is {@code null} until set by the {@link #init} method. */
    public CifSimulatorContext ctxt;

    /**
     * The maximum time point tolerance, in ulps.
     *
     * @see MaxTimePointTolOption
     */
    public final int maxTimePointTol;

    /**
     * The automata of the specification. Filled by the {@link #initAutomata} method. The automata must be added in
     * sorted order (ascending based on their absolute names).
     */
    public final List<RuntimeAutomaton<S>> automata = list();

    /**
     * The static meta data on the objects from the runtime state. Filled by the {@link #initStateObjectsMeta} method.
     * The objects must be added in sorted order (ascending based on their absolute names). If initialized, contains at
     * least one item, for variable 'time'. Variable 'time' is always present as first item (at index zero).
     */
    public final List<RuntimeStateObjectMeta> stateObjectsMeta = list();

    /**
     * The events of the specification. Filled by the {@link #initEvents} method. The events must be added in sorted
     * order (ascending based on their absolute names). The last event however, must always be the 'tau' event.
     */
    public final List<RuntimeEvent<S>> events = list();

    /**
     * The data that indicates which events are enabled (guard-wise). The data consists of an entry per event, with an
     * entry per automaton that synchronizes over that event. The inner lists of edges represent the edges that are
     * enabled (guard-wise) for the current location of the corresponding automaton, for the corresponding event.
     *
     * <p>
     * This field is initialized by the {@link #initEventData} method. The lists are kept during the entire simulation.
     * The inner lists and cleared and filled in-place with edges, during the simulation.
     * </p>
     *
     * <p>
     * There is no entry in this field for the 'tau' event. Instead, {@link #tauData} is filled.
     * </p>
     *
     * <p>
     * The edges are really of type {@link RuntimeSyncEdge}, but we can't use that as element type and still iterate
     * over them later, as {@link RuntimeEdge}.
     * </p>
     */
    public final List<List<List<RuntimeEdge<S>>>> syncData = list();

    /**
     * The data that indicates which senders are enabled (guard-wise). The data consists of an entry per event. The
     * inner lists of edges represent the edges that are enabled (guard-wise) for the current location of the automaton
     * that can send, for the corresponding event. For non-channel events, the inner lists are {@code null}.
     *
     * <p>
     * This field is initialized by the {@link #initEventData} method. The lists are kept during the entire simulation.
     * The inner lists and cleared and filled in-place with edges, during the simulation.
     * </p>
     *
     * <p>
     * There is no entry in this field for the 'tau' event. Instead, {@link #tauData} is filled.
     * </p>
     *
     * <p>
     * The edges are really of type {@link RuntimeSendEdge}, but we can't use that as element type and still iterate
     * over them later, as {@link RuntimeEdge}.
     * </p>
     */
    public final List<List<RuntimeEdge<S>>> sendData = list();

    /**
     * The data that indicates which receivers are enabled (guard-wise). The data consists of an entry per event. The
     * inner lists of edges represent the edges that are enabled (guard-wise) for the current location of the automaton
     * that can receive, for the corresponding event. For non-channel events, the inner lists are {@code null}.
     *
     * <p>
     * This field is initialized by the {@link #initEventData} method. The lists are kept during the entire simulation.
     * The inner lists and cleared and filled in-place with edges, during the simulation.
     * </p>
     *
     * <p>
     * There is no entry in this field for the 'tau' event. Instead, {@link #tauData} is filled.
     * </p>
     *
     * <p>
     * The edges are really of type {@link RuntimeReceiveEdge}, but we can't use that as element type and still iterate
     * over them later, as {@link RuntimeEdge}.
     * </p>
     */
    public final List<List<RuntimeEdge<S>>> recvData = list();

    /**
     * The edges for the 'tau' event that are enabled (guard-wise) in the current state, for all automata of the
     * specification.
     *
     * <p>
     * Note that each edge is individually wrapped in a list, as needed by the {@link #calcTransitions} method. That
     * method needs a list of edges per transition. Since the 'tau' event does not synchronize, we end up with lists
     * with exactly one element.
     * </p>
     *
     * <p>
     * The edges are really of type {@link RuntimeSyncEdge}, but we can't use that as element type and still iterate
     * over them later, as {@link RuntimeEdge}.
     * </p>
     */
    public final List<List<RuntimeEdge<S>>> tauData = list();

    /**
     * The transitions that are possible from the current state. Event transitions are always before time transitions.
     * Since there can be at most one time transition, only the last transition can be a time transition.
     *
     * <p>
     * This list is modified in-place during the simulation.
     * </p>
     */
    public final List<Transition<S>> transitions = listc(32);

    /**
     * Information about events being channels. For each {@link #events event}, it indicates whether the event is a
     * channel ({@code true}) or not ({@code false}). Is {@code null} until initialized by the {@link #init} method.
     */
    public boolean[] channels;

    /**
     * Information about the urgency of the events. For each {@link #events event}, it indicates whether the event is
     * considered urgent. Is {@code null} until initialized by the {@link #init} method.
     */
    public boolean[] urgent;

    /**
     * Information about the disabledness of events. Per event, whether or not the environment disables the event. If
     * the environment disables an event, calculation of the possible event transitions are skipped for that event. Is
     * {@code null} until initialized in {@link #init}. Is modified in-place during simulation.
     *
     * @see #calcTransitions
     * @see InputComponent#updateDisabledEvents
     */
    public boolean[] disabledEvents;

    /**
     * The next seed to use to construct a new random number generator. May be {@code null} to indicate a random seed in
     * the interval [1 .. 2^30] should be used for the first random number generator.
     *
     * @see #getNextSeed
     */
    private Integer seed;

    /** Constructor for the {@link RuntimeSpec} class. */
    public RuntimeSpec() {
        // Create input component.
        InputMode imode = InputModeOption.getInputMode();
        switch (imode) {
            case AUTO:
                input = new AutomaticInputComponent<>(this);
                break;

            case TRACE:
                input = new TraceInputComponent<>(this);
                break;

            case CONSOLE:
                AppStreams streams = AppEnv.getStreams();
                input = new InteractiveConsoleInputComponent<>(this, streams);
                break;

            case GUI:
                input = new InteractiveGuiInputComponent<>(this);
                break;

            case SVG:
                input = new SvgInputComponent<>(this);
                break;

            default:
                throw new RuntimeException("Unknown input mode: " + imode);
        }

        // Store option values.
        complete = CompleteModeOption.isEnabled();
        modelTimeDelta = FrameRateOption.getModelTimeDelta();
        seed = DistributionSeedOption.getInitialSeed();
        maxTimePointTol = MaxTimePointTolOption.getMaxTimePointTol();

        // Store the seed. This is useful for crash reports.
        AppEnv.setProperty("org.eclipse.escet.cif.simulator.distributions.seed",
                (seed == null) ? "n/a" : seed.toString());
    }

    /**
     * Initializes the data structures of this class.
     *
     * <p>
     * This method can't be called from the constructor, as that would cause problems in the static initialization
     * order.
     * </p>
     *
     * @param ctxt The simulator runtime context.
     */
    public void init(CifSimulatorContext ctxt) {
        // Store simulator context in specification.
        this.ctxt = ctxt;

        // Initialize solver.
        OdeSolver<S> solver = getOdeSolver();
        solver.ctxt = ctxt;
        solver.debug = ctxt.debug.contains(DebugOutputType.ODE);
        solver.dbg = solver.debug ? ctxt.appEnvData.getStreams().out : null;

        // Initialize events, including 'tau'.
        initEvents();
        Assert.check(last(events).isTauEvent);

        // Initialize event data structured, used during simulation to store
        // information about enabled events. Event 'tau' is excluded.
        initEventData();
        Assert.check(syncData.size() == events.size() - 1);
        Assert.check(sendData.size() == events.size() - 1);
        Assert.check(recvData.size() == events.size() - 1);

        // Initialize channel data. Last entry is for 'tau'.
        channels = new boolean[events.size()];
        for (int i = 0; i < sendData.size(); i++) {
            channels[i] = (sendData.get(i) != null);
        }
        channels[channels.length - 1] = false;

        // Obtain urgency information, and initialize disabled events data.
        urgent = EnvironmentEventsOption.getUrgentEvents(this);
        disabledEvents = new boolean[events.size()];

        // Initialize automata related data structures.
        initAutomata();

        // Initialize static state objects meta data related data structures.
        initStateObjectsMeta();
        Collections.sort(stateObjectsMeta, RuntimeStateObjectMeta.SORTER);
        RuntimeStateObjectMeta timeObjMeta = new RuntimeStateObjectMeta(0, StateObjectType.TIME, "time");
        stateObjectsMeta.add(0, timeObjMeta);

        // Allow additional initialization of the input component, now that the
        // specification has been fully initialized.
        input.init();
    }

    /**
     * Returns the next seed to use to construct a random number generator for a stochastic distribution.
     *
     * @return The seed to use.
     */
    public int getNextSeed() {
        // Get random initial seed, if requested.
        if (seed == null) {
            seed = new Random().nextInt(1 << 30) + 1;
            if (NormalOutputOption.doPrint(NormalOutputType.SEEDS)) {
                out("Using seed %d for the first random generator for a stochastic distribution.", seed);
            }

            // Store the seed. This is useful for crash reports.
            AppEnv.setProperty("org.eclipse.escet.cif.simulator.distributions.seed", seed.toString());
        }

        // Get the seed to return.
        int rslt = seed;

        // Increase the seed for the next request.
        seed++;
        if (seed > (1 << 30)) {
            seed = 1;
        }

        // Return the seed;
        return rslt;
    }

    /** Initializes {@link #automata}, by filling it. */
    protected abstract void initAutomata();

    /**
     * Initializes {@link #stateObjectsMeta}, by filling it. The order of the objects does not matter, as they will be
     * sorted after this method is invoked. Variable 'time' should not be added by this method.
     */
    protected abstract void initStateObjectsMeta();

    /** Initializes {@link #events}, by filling it. */
    protected abstract void initEvents();

    /**
     * Initializes {@link #syncData}, {@link #sendData}, {@link #recvData}, and {@link #tauData}, by filling them with
     * empty data.
     */
    protected abstract void initEventData();

    /**
     * Creates the initial state for the specification.
     *
     * @param spec The specification.
     * @return The newly created initial state.
     */
    public abstract S createInitialState(RuntimeSpec<?> spec);

    /**
     * Creates a shallow copy of the given state. The sub-states are the exact same objects (instances) as the given
     * state.
     *
     * @param state The state to copy.
     * @return A shallow copy of the given state.
     */
    protected abstract S copyState(S state);

    /**
     * Does the specification have an edge for the 'tau' event? Edges can have a 'tau' event either explicitly
     * ({@link TauExpression} as edge event), or implicitly (edges without edge events).
     *
     * @return {@code true} if the specification has at least one 'tau' edge, {@code false} otherwise.
     */
    public abstract boolean hasTauEdge();

    /**
     * Evaluates the initialization predicates for the given state.
     *
     * @param state The state to use to evaluate the initialization predicates.
     * @return {@code true} if all the initialization predicates evaluated to {@code true}, {@code false} otherwise.
     */
    protected abstract boolean evalInitPreds(S state);

    /**
     * Evaluates the state invariants for the given state.
     *
     * @param state The state to use to evaluate the state invariants.
     * @param initial Whether to evaluate for an initial state ({@code true}) or a non-initial state ({@code false}).
     * @return {@code true} if all the state invariants evaluated to {@code true}, {@code false} otherwise.
     */
    protected abstract boolean evalStateInvPreds(S state, boolean initial);

    /**
     * Evaluates the urgency of the locations for the given state.
     *
     * @param state The state to use to evaluate the urgency of the locations.
     * @return {@code true} if any of the locations of the given state is urgent, {@code false} otherwise.
     */
    protected abstract boolean evalUrgLocs(S state);

    /**
     * Evaluates the guards of the urgent outgoing edges for the given state.
     *
     * @param state The state to use to evaluate the guards of the urgent outgoing edges.
     * @return {@code true} if any of the urgent outgoing edges is enabled guard-wise, {@code false} otherwise.
     */
    protected abstract boolean evalUrgEdges(S state);

    /**
     * Returns the ODE state events (time dependent guard predicates of the outgoing edges of the current locations), to
     * use for the calculation of the maximum delay.
     *
     * @param state The state to use to retrieve the ODE state events.
     * @return The ODE state events.
     */
    protected abstract List<OdeStateEvent<S>> getOdeStateEvents(S state);

    /**
     * Returns the ODE solver for the specification. Note that for each specification, a single solver instance should
     * be re-used throughout the simulation. As such, this method should return the same instance on each call.
     *
     * @return The ODE solver for the specification.
     */
    public abstract OdeSolver<S> getOdeSolver();

    /**
     * Returns the CIF/SVG declarations, per SVG file.
     *
     * @return The CIF/SVG declarations, per SVG file.
     */
    public abstract List<RuntimeCifSvgDecls> getCifSvgDecls();

    /**
     * Returns the print I/O declarations, per output file/target.
     *
     * @return The print I/O declarations, per output file/target.
     */
    public abstract List<RuntimePrintDecls> getPrintDecls();

    /**
     * Calculates the transitions that are possible from the given state. After this method has been invoked, the
     * possible transitions can be retrieved from {@link #transitions}.
     *
     * @param source The source state, for which to calculate the possible transitions.
     * @param endTime The user-provided simulation end time, or {@code null} for infinite.
     * @param maxDelay The maximum delay for a single time transition, or {@code null} for infinite. A maximum delay of
     *     {@code 0.0} disallows time passage.
     * @see RuntimeState#calcTransitions
     */
    public void calcTransitions(S source, Double endTime, Double maxDelay) {
        // Clear left over transitions.
        transitions.clear();

        // Figure out which events are disabled by the environment.
        input.updateDisabledEvents(source, disabledEvents);

        // Calculate event transitions for all events.
        boolean timeAllowed = true;
        for (int eventIdx = 0; eventIdx < events.size(); eventIdx++) {
            // Check for application termination.
            ctxt.checkTermination();

            // If the environment disables the event, skip it.
            if (disabledEvents[eventIdx]) {
                continue;
            }

            // Calculate event transitions for the event.
            boolean anyTransitions = calcEventTransitions(source, eventIdx);

            // If urgent event transitions possible, then no time transition.
            if (anyTransitions && urgent[eventIdx]) {
                timeAllowed = false;
            }

            // In non-complete mode, we are done if we have any transitions.
            if (anyTransitions && !complete) {
                return;
            }
        }

        // Calculate time transition.
        if (timeAllowed) {
            calcTimeTransition(source, endTime, maxDelay);
        }
    }

    /**
     * Calculates the event transitions that are possible from the given state, for the given event. The transitions, if
     * any, are added to {@link #transitions}.
     *
     * @param source The source state, for which to calculate the possible event transitions.
     * @param eventIdx The 0-based index of the event for which to calculate the transitions.
     * @return {@code true} if transitions are possible for the given event, {@code false} otherwise.
     */
    private boolean calcEventTransitions(S source, int eventIdx) {
        // Get the event.
        RuntimeEvent<S> event = events.get(eventIdx);

        // Fill data for this event.
        boolean proceed = event.fillData(source);

        // If transition not possible (guard-wise), we are done for this event.
        if (!proceed) {
            return false;
        }

        // Is the event allowed by the state/event exclusions invariants?
        proceed = event.allowedByInvs(source);
        if (!proceed) {
            return false;
        }

        // Is the event a channel?
        boolean isChannel = channels[eventIdx];

        // Get an iterator to iterate over the possible combinations of
        // edges for this event.
        Iterator<List<RuntimeEdge<S>>> iter;
        if (event.isTauEvent) {
            iter = tauData.iterator();
        } else if (isChannel) {
            // Get data for this channel.
            List<List<RuntimeEdge<S>>> syncs = syncData.get(eventIdx);
            List<RuntimeEdge<S>> sends = sendData.get(eventIdx);
            List<RuntimeEdge<S>> recvs = recvData.get(eventIdx);

            // Get transition count.
            int count = sends.size() * recvs.size();
            for (List<RuntimeEdge<S>> edges: syncs) {
                count *= edges.size();
            }
            Assert.check(count > 0);

            // Create iterator. We optimize for exactly one possible
            // transition, as that is by far the most common case. It is
            // slightly less common than for synchronizing events (see below),
            // but we still create a special case.
            if (count == 1) {
                List<RuntimeEdge<S>> edges = listc(syncs.size() + 2);
                edges.add(first(sends));
                edges.add(first(recvs));
                for (List<RuntimeEdge<S>> edgeList: syncs) {
                    edges.add(first(edgeList));
                }
                iter = list(edges).iterator();
            } else {
                List<List<RuntimeEdge<S>>> full = listc(syncs.size() + 2);
                full.add(sends);
                full.add(recvs);
                full.addAll(syncs);
                iter = new ListProductIterator<>(full);
            }
        } else {
            // Get data for this non-channel event.
            List<List<RuntimeEdge<S>>> data = syncData.get(eventIdx);

            // Get transition count.
            int count = 1;
            for (List<RuntimeEdge<S>> edges: data) {
                count *= edges.size();
            }
            Assert.check(count > 0);

            // Create iterator. We optimize for exactly one possible
            // transition, as that is by far the most common case (since we
            // only get multiple ones if we have multiple outgoing edges for
            // the same event in the same source location).
            if (count == 1) {
                int autCount = data.size();
                List<RuntimeEdge<S>> edges = listc(autCount);
                for (int i = 0; i < autCount; i++) {
                    edges.add(first(data.get(i)));
                }
                iter = list(edges).iterator();
            } else {
                iter = new ListProductIterator<>(data);
            }
        }

        // Construct transitions, from the possible transitions.
        boolean anyTransitions = false;
        while (iter.hasNext()) {
            // Check for application termination.
            ctxt.checkTermination();

            // Get next edges.
            List<RuntimeEdge<S>> edges = iter.next();

            // Shallow copy source state to target state.
            S target = copyState(source);

            // For channels, get send value, and apply sender/receiver updates.
            Object sendValue = null;
            int edgeIndex = 0;
            if (isChannel) {
                // Get send edge.
                RuntimeSendEdge<S> sndEdge;
                RuntimeReceiveEdge<S> rcvEdge;
                sndEdge = (RuntimeSendEdge<S>)edges.get(0);
                rcvEdge = (RuntimeReceiveEdge<S>)edges.get(1);

                // Check for application termination.
                ctxt.checkTermination();

                // Get send value.
                sendValue = sndEdge.evalSendValue(source);

                // Apply updates.
                sndEdge.update(source, target);
                rcvEdge.update(source, target, sendValue);

                // Skip send/receive edges in remainder.
                edgeIndex = 2;
            }

            // For all events, apply updates of synchronizing edges.
            for (; edgeIndex < edges.size(); edgeIndex++) {
                // Get synchronization edge.
                RuntimeSyncEdge<S> syncEdge;
                syncEdge = (RuntimeSyncEdge<S>)edges.get(edgeIndex);

                // Check for application termination.
                ctxt.checkTermination();

                // Apply updates.
                syncEdge.update(source, target);
            }

            // Check target state invariants.
            boolean invs = evalStateInvPreds(target, false);
            if (!invs) {
                continue;
            }

            // Transition is possible. Create and add it.
            Transition<S> trans;
            if (isChannel) {
                trans = new CommunicationTransition<>(source, event, target, sendValue);
            } else {
                trans = new EventTransition<>(source, event, target);
            }
            transitions.add(trans);
            anyTransitions = true;

            // In non-complete mode, if we have a transition, we are done.
            if (!complete) {
                return true;
            }
        }

        // Return value indicating whether transitions are possible for this
        // event.
        return anyTransitions;
    }

    /**
     * Calculates the time transition from the given state. The transition, if possible, is added to
     * {@link #transitions}. This method should only be called by the {@link #calcTransitions} method, and
     * implementations of the {@link RuntimeState#calcTimeTransition} method.
     *
     * @param source The source state, for which to calculate the possible time transition.
     * @param endTime The user-provided simulation end time, or {@code null} for infinite.
     * @param maxDelay The maximum delay for a single time transition, or {@code null} for infinite. A maximum delay of
     *     {@code 0.0} disallows time passage.
     */
    public void calcTimeTransition(S source, Double endTime, Double maxDelay) {
        // If maximum delay is zero, then time passage is disallowed. We check
        // this first, to optimize for that case.
        if (maxDelay != null && maxDelay == 0.0) {
            return;
        }

        // We know that no urgent events have an event transition. We should
        // still check for urgent locations, and urgent edges.
        if (evalUrgLocs(source)) {
            return;
        }
        if (evalUrgEdges(source)) {
            return;
        }

        // We know that we may start delaying. Get the ODE solver.
        OdeSolver<S> solver = getOdeSolver();

        // Obtain the ODE state events (timed guard predicates) to use to
        // determine the maximum delay interval.
        List<OdeStateEvent<S>> events = getOdeStateEvents(source);

        // Get the maximum end time for the time transition.
        double maxTime;
        if (endTime == null && maxDelay == null) {
            // No restrictions, use 100.0 as implicit maxDelay. See also the
            // MaxDelayOption class.
            maxTime = source.getTime() + 100.0;
        } else if (endTime == null && maxDelay != null) {
            // No end time, but we do have a maximum delay.
            maxTime = source.getTime() + maxDelay;
        } else if (endTime != null && maxDelay == null) {
            // No maximum delay, but we do have an end time.
            maxTime = endTime;
        } else if (endTime != null && maxDelay != null) {
            // We have both an end time and a maximum delay.
            maxTime = source.getTime() + maxDelay;
            if (endTime < maxTime) {
                maxTime = endTime;
            }
        } else {
            throw new RuntimeException(); // Never reached.
        }

        // Check maximum end time for overflow.
        if (Double.isInfinite(maxTime)) {
            String msg = "The maximum end time for the next time transition is \"+inf\", which is not supported. "
                    + "This indicates an overflow. It might be possible to prevent this by setting a shorter maximum "
                    + "delay for time transitions.";
            throw new UnsupportedException(msg);
        }

        // Solve the 'Initial Value Problem' (IVP).
        Trajectories trajectories = solver.solveIVP(source, events, maxTime);
        if (trajectories == null) {
            return;
        }

        // A time transition is possible. Create and add it.
        TimeTransition<S> trans;
        trans = new TimeTransition<>(this, source, trajectories);
        transitions.add(trans);
    }

    /**
     * Takes the time transition by dealing with the intermediate frames, and the possible interruption of the time
     * transition.
     *
     * <p>
     * For intermediate frames, we use multiples of the real-time model time delta. We use the same algorithm as used by
     * the {@link StepNormalizer} (forward integration, {@link StepNormalizerMode#MULTIPLES multiples} mode,
     * {@link StepNormalizerBounds#LAST last} end point), to ensure that we can use the exact trajectory data points.
     * Note that to fully duplicate the calculation, we use {@link FastMath} as well, instead of Java's {@link Math}
     * class, or our own {@link CifSimulatorMath} class. However, the step normalizer compensates for duplicate output
     * points for each step, while we can only compensate for the first step of the entire time transition. As such, we
     * have time points very close to the ones of the step normalizer, but not always exactly those points.
     * </p>
     *
     * @param transition The time transition to take.
     * @param chosenTargetTime The chosen target time of the time transition to take.
     * @return The actual target time of the time transition.
     */
    public ActualTargetTime takeTimeTransition(TimeTransition<S> transition, ChosenTargetTime chosenTargetTime) {
        // If real-time simulation is disabled, we don't send intermediate
        // frames to the output components. We thus don't get interrupted.
        if (modelTimeDelta == null) {
            return new ActualTargetTime(chosenTargetTime);
        }

        // Initialize times and deltas. This includes the calculation of the
        // next multiple of the model time delta (nextTime).
        double startTime = transition.source.getTime();
        double endTime = chosenTargetTime.targetTime;
        double modelDelta = modelTimeDelta;

        double nextTime = (FastMath.floor(startTime / modelDelta) + 1);
        nextTime *= modelDelta;
        if (Precision.equals(nextTime, startTime, 1)) {
            nextTime += modelDelta;
        }

        // Keep sending intermediate trajectory states to the output, until we
        // have reached the end of the trajectory, or until we are interrupted.
        S istate;
        while (true) {
            // Check for application termination.
            ctxt.checkTermination();

            // Can't perform real-time simulation without a real-time output
            // component. If test mode is enabled, real-time delays are always
            // skipped, and we can't ever perform real-time simulation, but it
            // is still useful to allow that for testing.
            if (ctxt.realTime && !ctxt.testMode && !ctxt.provider.supportsRealTimeSimulation()) {
                // Stop simulation at the request of the user.
                throw new SimulatorExitException(SimulationResult.USER_TERMINATED);
            }

            // Stop if we have reached the end of the chosen delay.
            if (nextTime >= endTime) {
                // Uninterrupted. Return original chosen target time.
                return new ActualTargetTime(chosenTargetTime);
            }

            // Get intermediate state. We know the exact time point (or a very
            // close one) is present, as the ODE solver fixed output step size
            // is set to 'modelDelta'.
            istate = transition.getTargetState(nextTime, true);

            // Update the requested time to the time of the actual time point,
            // to keep in sync with trajectory time points, and to avoid
            // accumulating deltas.
            nextTime = istate.getTime();

            // Stop if we have reached the end of the chosen delay, with the
            // updated time. This way, intermediate output is only generated
            // for intermediate states, strictly before the chosen end time.
            if (nextTime >= endTime) {
                continue;
            }

            // Give intermediate state to output components.
            ctxt.provider.intermediateTrajectoryState(istate);

            // Poll input component for interruption. Note that intermediate
            // trajectory states are always after the start of the time
            // transition, so we never ask to interrupt for a zero length time
            // transition.
            if (input.interruptTimeTrans()) {
                return new ActualTargetTime(chosenTargetTime, nextTime);
            }

            // Update for next iteration.
            nextTime += modelDelta;
        }
    }
}
