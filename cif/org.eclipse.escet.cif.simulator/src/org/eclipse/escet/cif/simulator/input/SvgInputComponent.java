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

package org.eclipse.escet.cif.simulator.input;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.escet.cif.simulator.output.svgviz.RuntimeCifSvgDecls;
import org.eclipse.escet.cif.simulator.output.svgviz.SvgOutputComponent;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.svg.selector.SvgSelector;

/**
 * SVG input component. Can be used in combination with one or more {@link SvgOutputComponent SVG output components} to
 * control the input to the simulator. By clicking on interactive elements of the SVG images, the user can control which
 * event transitions are chosen. Remaining choices are deferred to an {@link AutomaticInputComponent automatic input
 * component}.
 *
 * @param <S> The type of state objects to use.
 */
public final class SvgInputComponent<S extends RuntimeState> extends InputComponent<S> {
    /** The SVG output components coupled to this SVG input component. Is modified in-place. */
    private final List<SvgOutputComponent> outputComponents = list();

    /** Mapping from SVG selectors to the corresponding CIF/SVG declarations. Is modified in-place. */
    private final Map<SvgSelector, RuntimeCifSvgDecls> svgSelectMap = map();

    /**
     * For each event in the specification, whether it can be chosen by clicking on an interactive SVG element. Such
     * events are never deferred to the {@link #deferInputComp}. Is {@code null} until set by the first call to the
     * {@link #initialize} method.
     */
    private boolean[] interactiveEvents = null;

    /**
     * Per event, whether it is disabled for the current state/transition. Is {@code null} until first set by the
     * {@link #updateDisabledEvents} method.
     */
    private boolean[] lastDisabledEvents = null;

    /** The {@link AutomaticInputComponent} to which to defer all remaining choices. */
    private final AutomaticInputComponent<S> deferInputComp;

    /**
     * The queue of SVG element ids of the interactive SVG elements on which the user has clicked, but that have not yet
     * been used as input for the simulator. Each entry also includes the SVG selector that added it, to allow
     * retrieving the correct CIF/SVG input mappings to process the entry.
     *
     * @see SvgSelector#idQueue
     */
    public final Queue<Pair<SvgSelector, String>> queue = new ConcurrentLinkedQueue<>();

    /**
     * Constructor for the {@link SvgInputComponent} class.
     *
     * @param spec The specification. The specification has not yet been {@link RuntimeSpec#init initialized}.
     */
    public SvgInputComponent(RuntimeSpec<S> spec) {
        super(spec);
        deferInputComp = new AutomaticInputComponent<>(spec);
    }

    /**
     * Initializes the SVG input component, by coupling it to an SVG output component. This method may be invoked
     * multiple times, to couple multiple SVG output components, for different SVG files.
     *
     * @param component The SVG output component to which to connect.
     */
    public void initialize(SvgOutputComponent component) {
        // Add output component.
        RuntimeCifSvgDecls cifSvgDecls = component.cifSvgDecls;
        outputComponents.add(component);

        // Update interactive events.
        boolean[] interactiveEvents = cifSvgDecls.getInteractiveEvents();
        if (this.interactiveEvents == null) {
            this.interactiveEvents = interactiveEvents;
        } else {
            Assert.check(this.interactiveEvents.length == interactiveEvents.length);
            for (int i = 0; i < interactiveEvents.length; i++) {
                this.interactiveEvents[i] |= interactiveEvents[i];
            }
        }

        // Create SVG selector and couple it to the SVG input/output.
        SvgSelector selector = new SvgSelector(component.canvas, cifSvgDecls.getInteractiveIds(), queue);
        svgSelectMap.put(selector, cifSvgDecls);
        component.renderThread.selector = selector;
    }

    @Override
    public void updateDisabledEvents(S state, boolean[] disabled) {
        if (queue.isEmpty()) {
            // If queue is empty, disable all interactive events.
            System.arraycopy(interactiveEvents, 0, disabled, 0, disabled.length);
        } else {
            // Get event for head of the queue.
            Pair<SvgSelector, String> idPair = queue.peek();
            SvgSelector selector = idPair.left;
            String id = idPair.right;
            RuntimeCifSvgDecls cifSvgDecls = svgSelectMap.get(selector);
            int eventIdx = cifSvgDecls.applyInput(id, state);

            // Disable all events of the specification, except for the one that
            // corresponds to the head of the queue.
            Arrays.fill(disabled, true);
            disabled[eventIdx] = false;
        }

        // Store the events for later use. No need to copy, as the same array
        // is reused every time.
        lastDisabledEvents = disabled;
    }

    @SuppressWarnings("null")
    @Override
    public Transition<S> chooseTransition(S state, List<Transition<S>> transitions, SimulationResult result) {
        // If no transitions possible, stop simulating.
        if (transitions.isEmpty()) {
            throw new SimulatorExitException(result);
        }

        // Peek the queue for an SVG element id.
        Pair<SvgSelector, String> idPair = queue.peek();

        // Map the SVG element id to an event by using the CIF/SVG input
        // mappings.
        RuntimeCifSvgDecls cifSvgDecls = null;
        String id = null;
        int eventIdx = -1;
        if (idPair != null) {
            // Get id, CIF/SVG declarations, and event index.
            SvgSelector selector = idPair.left;
            id = idPair.right;
            cifSvgDecls = svgSelectMap.get(selector);
            eventIdx = cifSvgDecls.applyInput(id, state);

            if (lastDisabledEvents[eventIdx]) {
                // We disabled the event, as it was not the head of the queue.
                // Therefore no event transitions were calculated for it. Since
                // the event is now the head of the queue, we delay the
                // processing of the queue until the next transition.
                idPair = null;
            } else {
                // We didn't ask to skip calculation for this event, so we
                // should have a transition for it. We remove it from the
                // queue.
                queue.poll();
            }
        }

        // If the queue was empty, defer the choice to the automatic input
        // component.
        if (idPair == null) {
            // Filter out the transitions for interactive SVG events.
            List<Transition<S>> transitions2 = list();
            for (Transition<S> trans: transitions) {
                if (trans instanceof EventTransition) {
                    int idx = ((EventTransition<?>)trans).event.idx;
                    if (interactiveEvents[idx]) {
                        continue;
                    }
                }
                transitions2.add(trans);
            }

            // If there are no transitions left, we have a deadlock.
            if (transitions2.isEmpty()) {
                throw new SimulatorExitException(SimulationResult.DEADLOCK);
            }

            // Defer the choice.
            Assert.check(result == null);
            return deferInputComp.chooseTransition(state, transitions2, result);
        }

        // Debug SVG inputs.
        if (cifSvgDecls.debug) {
            String msg = fmt("SVG input (\"%s\") id \"%s\": event \"%s\"", cifSvgDecls.getSvgRelPath(), id,
                    spec.events.get(eventIdx).name);
            cifSvgDecls.dbg.println(msg);
        }

        // Find the matching transition(s).
        List<Transition<S>> matches = list();
        for (Transition<S> trans: transitions) {
            if (trans instanceof EventTransition) {
                if (((EventTransition<?>)trans).event.idx == eventIdx) {
                    matches.add(trans);
                }
            }
        }

        // If no matches, we have deadlock.
        if (matches.isEmpty()) {
            // Deadlock.
            warn("The SVG element (\"%s\") with id \"%s\" was clicked, but the corresponding event \"%s\" is not "
                    + "enabled in the current state, leading to deadlock.", cifSvgDecls.getSvgRelPath(), id,
                    spec.events.get(eventIdx).name);
            throw new SimulatorExitException(SimulationResult.DEADLOCK);
        }

        // Defer the choice to the automatic input component.
        Assert.check(result == null);
        return deferInputComp.chooseTransition(state, matches, result);
    }

    @Override
    public Double getNextMaxEndTime(S state) {
        // Only events can be chosen by clicking on interactive SVG elements,
        // which means that time transitions are always deferred to the
        // automatic input component.
        return deferInputComp.getNextMaxEndTime(state);
    }

    @Override
    public ChosenTargetTime chooseTargetTime(S state, double maxTargetTime) {
        // Only events can be chosen by clicking on interactive SVG elements,
        // which means that time transitions are always deferred to the
        // automatic input component.
        return deferInputComp.chooseTargetTime(state, maxTargetTime);
    }

    @Override
    public boolean interruptTimeTrans() {
        // Non-empty queue means interrupt, to choose an interactive event.
        return !queue.isEmpty();
    }
}
