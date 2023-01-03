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

package org.eclipse.escet.cif.simulator.output;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.options.InputMode;
import org.eclipse.escet.cif.simulator.options.InputModeOption;
import org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption;
import org.eclipse.escet.cif.simulator.options.ProfilingOption;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateFilterer;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.StreamOutputComponent;
import org.eclipse.escet.common.java.Assert;

/**
 * CIF simulator output component that writes all information to steams ({@link System#out} and {@link System#err} by
 * default).
 */
public final class SimulatorStreamOutputComponent extends StreamOutputComponent implements SimulatorOutputComponent {
    /** Whether profiling is enabled. Not available until {@link #initialState} is invoked. */
    private boolean profiling;

    /**
     * The types of normal output to print to the console, for the {@link OutputMode#NORMAL} and
     * {@link OutputMode#DEBUG} output modes.
     */
    private EnumSet<NormalOutputType> outTypes;

    /** Whether to print the values of the algebraic variables as part of the state. */
    private boolean printStateAlg;

    /**
     * Whether to print the values of the derivatives of the continuous variables (excluding variable 'time') as part of
     * the state.
     */
    private boolean printStateDerivs;

    /** Whether a possible transition should be printed, if it is the only possible transition. */
    private boolean printSingle;

    /** Whether the possible transitions should be printed, if there is more than one possible transition. */
    private boolean printMulti;

    /**
     * The meta data of the filtered state objects, in the order that they are printed. Is {@code null} until set by
     * {@link #initializeOptions}. Remains {@code null} if state filtering is disabled or if no initial state is
     * available for filtering.
     */
    private List<RuntimeStateObjectMeta> metas;

    /**
     * The time at which profiling started to count, in nanoseconds, relative to some fixed but arbitrary time. The
     * value is irrelevant for as long as the simulation has not yet started, and remains irrelevant if profiling is
     * disabled.
     */
    private long lastTime;

    /**
     * The number of transitions performed since {@link #lastTime}. The value is irrelevant for as long as the
     * simulation has not yet started, and remains irrelevant if profiling is disabled.
     */
    private int count;

    /**
     * Constructor for the {@link SimulatorStreamOutputComponent}.
     *
     * @param out The output stream to use.
     * @param err The error stream to use.
     */
    public SimulatorStreamOutputComponent(AppStream out, AppStream err) {
        super(out, err);
    }

    /**
     * Initialize the options.
     *
     * @param state The initial state. May be {@code null} if initialization failed.
     */
    private void initializeOptions(RuntimeState state) {
        // If already initialized, skip initialization.
        if (outTypes != null) {
            return;
        }

        // Initialize profiling.
        profiling = ProfilingOption.isEnabled();
        if (profiling) {
            lastTime = System.nanoTime();
        }

        // Initialize output types.
        outTypes = NormalOutputOption.getOutputTypes();
        printStateAlg = outTypes.contains(NormalOutputType.STATE_ALG_VARS);
        printStateDerivs = outTypes.contains(NormalOutputType.STATE_DERIVS);

        // Initialize transition printing.
        if (outTypes.contains(NormalOutputType.TRANS_ALWAYS)) {
            printSingle = true;
            printMulti = true;
        } else if (outTypes.contains(NormalOutputType.TRANS_DEFAULT)) {
            InputMode imode = InputModeOption.getInputMode();
            boolean single = InteractiveAutoChooseOption.autoChooseSingle(InteractiveAutoChooseOption.getFilters());

            printSingle = imode == InputMode.CONSOLE && !single;
            printMulti = true;
        } else if (outTypes.contains(NormalOutputType.TRANS_MINIMAL)) {
            InputMode imode = InputModeOption.getInputMode();
            boolean single = InteractiveAutoChooseOption.autoChooseSingle(InteractiveAutoChooseOption.getFilters());

            printSingle = imode == InputMode.CONSOLE && !single;
            printMulti = imode == InputMode.CONSOLE;
        } else {
            printSingle = false;
            printMulti = false;
        }

        // Initialize filtered state objects meta data.
        if (state != null) {
            String filtersTxt = NormalOutputStateFiltersOption.getFilters();
            metas = state.spec.stateObjectsMeta;
            metas = RuntimeStateFilterer.filter(metas, filtersTxt, "Normal console output",
                    "printed to the console as part of states.");
        }
    }

    @Override
    public void initialState(RuntimeState state) {
        // Initialize options.
        initializeOptions(state);

        // Output initial state.
        if (outTypes.contains(NormalOutputType.STATE_INIT)) {
            out.println("Initial state: " + state.toSingleLineString(metas, printStateAlg, printStateDerivs));
            out.println();
        }
    }

    @Override
    public void possibleTransitions(List<Transition<?>> transitions, SimulationResult result) {
        // Skip if no transitions possible.
        if (transitions.isEmpty()) {
            return;
        }

        // Skip if never printing anything.
        if (!printSingle && !printMulti) {
            return;
        }

        // Print if requested for the given number of transitions.
        int count = transitions.size();
        if ((count == 1 && printSingle) || (count > 1 && printMulti)) {
            out.println("Possible transitions:");
            int maxNrLength = String.valueOf(transitions.size() - 1).length();
            for (int i = 0; i < transitions.size(); i++) {
                Transition<?> transition = transitions.get(i);
                out.println(fmt("  #%" + maxNrLength + "d: %s", i + 1, transition));
            }
            out.println();
        }
    }

    @Override
    public void transitionChosen(RuntimeState sourceState, Transition<?> transition,
            ChosenTargetTime chosenTargetTime)
    {
        if (outTypes.contains(NormalOutputType.CHOSEN_TRANS)) {
            String txt;
            if (transition instanceof TimeTransition<?>) {
                Assert.check(transition instanceof TimeTransition);
                double sourceTime = sourceState.getTime();
                double delay = chosenTargetTime.getDelay();
                txt = fmt("delaying for %s time units at time %s", delay, sourceTime);
            } else {
                txt = transition.toString();
            }
            out.println("Transition: " + txt);
        }
    }

    @Override
    public void intermediateTrajectoryState(RuntimeState state) {
        if (outTypes.contains(NormalOutputType.STATE_INTERMEDIATE)) {
            out.println("Frame: " + state.toSingleLineString(metas, printStateAlg, printStateDerivs));
        }
    }

    @Override
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, RuntimeState targetState,
            Boolean interrupted)
    {
        if (outTypes.contains(NormalOutputType.INTERRUPTED_TRANS)) {
            if (interrupted != null && interrupted) {
                double delay = targetState.getTime() - sourceState.getTime();
                out.println(fmt("Time transition interrupted after %s time units.", delay));
            }
        }

        if (outTypes.contains(NormalOutputType.STATE_TARGET)) {
            out.println("State: " + targetState.toSingleLineString(metas, printStateAlg, printStateDerivs));
            out.println();
        }

        // Display/update profiling information.
        if (profiling) {
            count++;
            long nanoSecs = System.nanoTime() - lastTime;
            if (nanoSecs >= 1e9) {
                // At least one second of wall clock time elapsed.
                double transPerSec = count / (nanoSecs / 1e9);
                out.println(fmt("%.2f transitions/second", transPerSec));

                lastTime = System.nanoTime();
                count = 0;
            }
        }
    }

    @Override
    public void simulationEnded(SimulationResult rslt, RuntimeState state) {
        // Initialize options, in case the initial state was not given.
        initializeOptions(state);

        // Print final state.
        if (state != null) {
            if (outTypes.contains(NormalOutputType.STATE_FINAL)
                    || (rslt == SimulationResult.DEADLOCK && outTypes.contains(NormalOutputType.STATE_DEADLOCK)))
            {
                out.println(fmt("%s state: %s", (rslt == SimulationResult.DEADLOCK) ? "Deadlock" : "Final",
                        state.toSingleLineString(metas, printStateAlg, printStateDerivs)));
            }
        }

        // Print simulation result.
        if (outTypes.contains(NormalOutputType.SIM_RSLT)) {
            switch (rslt) {
                case INIT_FAILED:
                    out.println("Simulation ended due to initialization failure.");
                    break;

                case DEADLOCK:
                    out.println("Simulation resulted in deadlock.");
                    break;

                case ENDTIME_REACHED:
                    out.println("Simulation ended due to reaching the user-provided simulation end time.");
                    break;

                case USER_TERMINATED:
                    out.println("Simulation was terminated per the user's request.");
                    break;
            }
        }
    }

    @Override
    public boolean hasVisualInterface() {
        return false;
    }

    @Override
    public boolean supportsRealTimeSimulation() {
        return false;
    }
}
