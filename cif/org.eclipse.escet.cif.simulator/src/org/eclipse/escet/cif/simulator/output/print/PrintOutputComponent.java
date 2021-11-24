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

package org.eclipse.escet.cif.simulator.output.print;

import static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.EVENT;
import static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.FINAL;
import static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.INITIAL;
import static org.eclipse.escet.cif.simulator.output.print.PrintTransitionKind.TIME;

import org.eclipse.escet.cif.simulator.CifSimulatorContext;
import org.eclipse.escet.cif.simulator.output.NormalOutputType;
import org.eclipse.escet.cif.simulator.output.NullSimulatorOutputComponent;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;

/** Print I/O declarations output component, for a single file/target. */
public class PrintOutputComponent extends NullSimulatorOutputComponent {
    /** Whether printing of output is enabled. */
    private final boolean enabled;

    /** The print I/O declarations. */
    private final RuntimePrintDecls printDecls;

    /**
     * Constructor for the {@link PrintOutputComponent} class.
     *
     * @param printDecls The print I/O declarations.
     * @param ctxt The simulator runtime context.
     */
    public PrintOutputComponent(RuntimePrintDecls printDecls, CifSimulatorContext ctxt) {
        this.enabled = ctxt.normal.contains(NormalOutputType.PRINT);
        this.printDecls = printDecls;
        printDecls.init(ctxt);
    }

    @Override
    public void initialState(RuntimeState state) {
        // Print for 'initial' virtual transition.
        if (!enabled) {
            return;
        }

        printDecls.print(state, state, INITIAL, -1);
    }

    @Override
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, RuntimeState targetState,
            Boolean interrupted)
    {
        // Skip if print output is disabled.
        if (!enabled) {
            return;
        }

        // We print in this method instead of 'transitionChosen', as here both
        // the pre/source and post/target states are available. For most
        // transitions the target state is already available when the
        // transition is calculated/created, but for time transitions, they
        // can be interrupted by the input component during the transition (if
        // real-time simulation is enabled). As such, for all transitions we
        // only have the full information in this method.
        if (transition instanceof EventTransition) {
            EventTransition<?> eventTrans = (EventTransition<?>)transition;
            int eventIdx = eventTrans.event.idx;
            printDecls.print(sourceState, targetState, EVENT, eventIdx);
        } else if (transition instanceof TimeTransition) {
            printDecls.print(sourceState, targetState, TIME, -1);
        } else if (transition instanceof HistoryTransition) {
            // Don't print anything for history transitions. The print output
            // has already been generated in the past.
        } else {
            throw new RuntimeException("Unknown transition: " + transition);
        }
    }

    @Override
    public void simulationEnded(SimulationResult rslt, RuntimeState state) {
        // Print for 'final' virtual transition.
        if (!enabled) {
            return;
        }

        printDecls.print(state, state, FINAL, -1);

        // Close streams, now that no more output will be written.
        printDecls.close();
    }

    @Override
    public void cleanup() {
        // Ensure streams are always closed, even if simulation crashed.
        printDecls.close();
    }
}
