//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.simulator.runtime.SimulationResult.USER_TERMINATED;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.ResetTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.cif.simulator.runtime.transitions.UndoTransition;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.java.exceptions.InputOutputException;

/**
 * Console input component that interactively asks the user for input, via a stream (stdin by default). Using the
 * {@link InteractiveAutoChooseOption} option, the interactive input component can be made semi-automatic.
 *
 * @param <S> The type of state objects to use.
 */
public final class InteractiveConsoleInputComponent<S extends RuntimeState> extends InteractiveInputComponent<S> {
    /**
     * The streams to use to read input, to write output (usually requests for input), warning, and errors (invalid
     * input supplied, etc).
     */
    private final AppStreams streams;

    /**
     * Constructor for the {@link InteractiveConsoleInputComponent}.
     *
     * @param spec The specification. The specification has not yet been {@link RuntimeSpec#init initialized}.
     * @param streams The streams to use to read input, to write output (usually requests for input), warning, and
     *     errors (invalid input supplied, etc).
     */
    public InteractiveConsoleInputComponent(RuntimeSpec<S> spec, AppStreams streams) {
        super(spec);
        this.streams = streams;
    }

    @Override
    protected boolean needAutomaticInputComponent() {
        // Only if semi-automatic mode is enabled.
        return autoTime || autoTimeDur || ArrayUtils.contains(autoEvents, true);
    }

    @Override
    protected Transition<S> chooseTransitionInteractive(S state, List<Transition<S>> transitions) {
        // Ask the question.
        String question = fmt("Select a transition ([1..%d]). Enter q to quit. "
                + "Enter h for help and additional commands. Confirm with <ENTER>: ", transitions.size());
        streams.out.print(question);
        streams.out.flush();

        // Keep looping until we have a satisfactory answer.
        while (true) {
            // Get the answer (the user's choice). If the end of the input
            // stream is used, terminate the simulation.
            String choice;
            try {
                choice = streams.in.readLine();
            } catch (IOException ex) {
                throw new InputOutputException("Could not read input.", ex);
            }
            if (choice == null) {
                // End of input stream. Terminate simulation.
                throw new SimulatorExitException(USER_TERMINATED);
            }
            choice = choice.trim();

            // Check for application termination.
            spec.ctxt.checkTermination();

            // Handle empty and quit.
            if (choice.length() == 0) {
                choice = "1";
            }
            if (choice.equals("q") || choice.equals("Q")) {
                throw new SimulatorExitException(USER_TERMINATED);
            }

            // Handle help.
            if (choice.equals("h") || choice.equals("H")) {
                streams.out.println();
                printHelp(true);
                streams.out.println();
                streams.out.print("Please make another choice: ");
                streams.out.flush();
                continue;
            }

            // Handle reset.
            if (choice.equals("r") || choice.equals("R")) {
                // Perform reset, if possible.
                if (history == null) {
                    streams.err.print("Can't reset simulation, as history is disabled. Please try again: ");
                    streams.err.flush();
                    continue;
                } else if (!history.canReset(state)) {
                    streams.err.print("Can't reset simulation, as initial state is already the current state. "
                            + "Please try again: ");
                    streams.err.flush();
                    continue;
                } else {
                    @SuppressWarnings("unchecked")
                    S target = (S)history.reset();
                    return new ResetTransition<>(state, target);
                }
            }

            // Handle undo.
            if (choice.startsWith("u") || choice.startsWith("U")) {
                // Get count.
                String remainder = choice.substring(1).trim();
                int count;
                if (remainder.isEmpty()) {
                    // No explicit count, use implicit count of 1.
                    count = 1;
                } else {
                    // Convert the choice to a number.
                    while (true) {
                        try {
                            count = Integer.parseInt(remainder);
                        } catch (NumberFormatException ex) {
                            streams.err
                                    .print(fmt("\"%s\" is not a valid integer number. Please try again: ", remainder));
                            streams.err.flush();
                            continue;
                        }

                        // Check interval bounds.
                        if (count < 1) {
                            streams.err.print(fmt("\"%s\" is not a positive number. Please try again: ", count));
                            streams.err.flush();
                            continue;
                        }

                        // Count is OK.
                        break;
                    }
                }

                // Perform undo, if possible.
                if (history == null) {
                    streams.err.print("Can't undo last transition, as history is disabled. Please try again: ");
                    streams.err.flush();
                    continue;
                } else if (!history.isUndoEnabled()) {
                    streams.err.print("Can't undo last transition, as undo is disabled. Please try again: ");
                    streams.err.flush();
                    continue;
                } else if (!history.canUndo(state, count)) {
                    streams.err.print(
                            fmt("Can't undo %d transition%s, as that many transitions have not been taken, or the undo "
                                    + "buffer is too small. Please try again: ", count, (count == 1) ? "" : "s"));
                    streams.err.flush();
                    continue;
                } else {
                    @SuppressWarnings("unchecked")
                    S target = (S)history.undo(count);
                    return new UndoTransition<>(state, target, count);
                }
            }

            // Convert the choice to a number.
            int nr;
            try {
                nr = Integer.parseInt(choice);
            } catch (NumberFormatException ex) {
                streams.err.print(fmt("\"%s\" is not a valid integer number. Please try again: ", choice));
                streams.err.flush();
                continue;
            }

            // Check interval bounds.
            if (nr < 1 || nr > transitions.size()) {
                streams.err.print(fmt("\"%s\" is not part of the interval [1..%d]. Please try again: ", choice,
                        transitions.size()));
                streams.err.flush();
                continue;
            }

            // Return the chosen transition.
            return transitions.get(nr - 1);
        }
    }

    @Override
    protected HistoryTransition<S> chooseTransitionInteractive(S state, SimulationResult result) {
        // Ask the question.
        String question;
        switch (result) {
            case DEADLOCK:
                question = "Simulation resulted in deadlock. ";
                break;

            case ENDTIME_REACHED:
                question = "User-provided simulation end time reached. ";
                break;

            default:
                throw new RuntimeException("Unexpected result: " + result);
        }
        question += "Enter q to quit. Enter h for help and additional commands. Confirm with <ENTER>: ";
        streams.out.print(question);
        streams.out.flush();

        // Keep looping until we have a satisfactory answer.
        while (true) {
            // Check for application termination. If requested, don't ask user
            // to choose. Instead, use the simulation result.
            if (spec.ctxt.appEnvData.isTerminationRequested()) {
                throw new SimulatorExitException(result);
            }

            // Get the answer (the user's choice). If the end of the input
            // stream is used, terminate the simulation.
            String choice;
            try {
                choice = streams.in.readLine();
            } catch (IOException ex) {
                throw new InputOutputException("Could not read input.", ex);
            }
            if (choice == null) {
                // End of input stream. Terminate simulation.
                throw new SimulatorExitException(result);
            }
            choice = choice.trim();

            // Check for application termination, as we just interactively
            // asked for input, which could have taken some time. If requested,
            // use the simulation result, rather than user-requested
            // termination.
            if (spec.ctxt.appEnvData.isTerminationRequested()) {
                throw new SimulatorExitException(result);
            }

            // Handle quit. Use original simulation result instead of
            // user-requested termination.
            if (choice.equals("q") || choice.equals("Q")) {
                throw new SimulatorExitException(result);
            }

            // Handle help.
            if (choice.equals("h") || choice.equals("H")) {
                streams.out.println();
                printHelp(false);
                streams.out.println();
                streams.out.print("Please make another choice: ");
                streams.out.flush();
                continue;
            }

            // Handle reset.
            if (choice.equals("r") || choice.equals("R")) {
                // Perform reset, if possible.
                if (history == null) {
                    streams.err.print("Can't reset simulation, as history is disabled. Please try again: ");
                    streams.err.flush();
                    continue;
                } else if (!history.canReset(state)) {
                    streams.err.print("Can't reset simulation, as initial state is already the current state. "
                            + "Please try again: ");
                    streams.err.flush();
                    continue;
                } else {
                    @SuppressWarnings("unchecked")
                    S target = (S)history.reset();
                    return new ResetTransition<>(state, target);
                }
            }

            // Handle undo.
            if (choice.startsWith("u") || choice.startsWith("U")) {
                // Get count.
                String remainder = choice.substring(1).trim();
                int count;
                if (remainder.isEmpty()) {
                    // No explicit count, use implicit count of 1.
                    count = 1;
                } else {
                    // Convert the choice to a number.
                    while (true) {
                        try {
                            count = Integer.parseInt(remainder);
                        } catch (NumberFormatException ex) {
                            streams.err
                                    .print(fmt("\"%s\" is not a valid integer number. Please try again: ", remainder));
                            streams.err.flush();
                            continue;
                        }

                        // Check interval bounds.
                        if (count < 1) {
                            streams.err.print(fmt("\"%s\" is not a positive number. Please try again: ", count));
                            streams.err.flush();
                            continue;
                        }

                        // Count is OK.
                        break;
                    }
                }

                // Perform undo, if possible.
                if (history == null) {
                    streams.err.print("Can't undo last transition, as history is disabled. Please try again: ");
                    streams.err.flush();
                    continue;
                } else if (!history.isUndoEnabled()) {
                    streams.err.print("Can't undo last transition, as undo is disabled. Please try again: ");
                    streams.err.flush();
                    continue;
                } else if (!history.canUndo(state, count)) {
                    streams.err.print(
                            fmt("Can't undo %d transition%s, as that many transitions have not been taken, or the undo "
                                    + "buffer is too small. Please try again: ", count, (count == 1) ? "" : "s"));
                    streams.err.flush();
                    continue;
                } else {
                    @SuppressWarnings("unchecked")
                    S target = (S)history.undo(count);
                    return new UndoTransition<>(state, target, count);
                }
            }

            // Invalid choice.
            streams.err.print(fmt("\"%s\" is not a valid choice. Please try again: ", choice));
            streams.err.flush();
            continue;
        }
    }

    /**
     * Prints the interactive console input component help text to the console.
     *
     * @param canChooseTransitions Whether actual transitions can be chosen.
     */
    private void printHelp(boolean canChooseTransitions) {
        streams.out.println("Interactive console input command help:");
        if (canChooseTransitions) {
            streams.out.println("  <nothing, only ENTER>    Choose transition 1.");
            streams.out.println("  <number>                 Choose transition <number>.");
        }
        streams.out.println("  r                        Reset simulation to initial state.");
        streams.out.println("  u                        Undo a single transition.");
        streams.out.println("  u <number>               Undo <number> transition(s).");
        streams.out.println("  q                        Quit simulation.");
        streams.out.println("  h                        Show this help text.");
    }

    @Override
    protected ChosenTargetTime chooseTargetTimeInteractive(S state, double maxTargetTime) {
        // Ask the question.
        double sourceTime = state.getTime();
        double maxDelay = maxTargetTime - sourceTime;
        String question = fmt("Select a duration from (0 .. %s]. Empty choice equals maximum delay. Enter q to quit. "
                + "Confirm with <ENTER>: ", maxDelay);
        streams.out.print(question);
        streams.out.flush();

        // Keep looping until we have a satisfactory answer.
        while (true) {
            // Get the answer (the user's choice). If the end of the input
            // stream is used, terminate the simulation.
            String choice;
            try {
                choice = streams.in.readLine();
            } catch (IOException ex) {
                throw new InputOutputException("Could not read input.", ex);
            }
            if (choice == null) {
                // End of input stream. Terminate simulation.
                throw new SimulatorExitException(USER_TERMINATED);
            }
            choice = choice.trim();

            // Check for application termination.
            spec.ctxt.checkTermination();

            // Handle quit.
            if (choice.equals("q") || choice.equals("Q")) {
                throw new SimulatorExitException(USER_TERMINATED);
            }

            // Handle empty. Choose the maximum delay, and thus the maximum
            // target time.
            if (choice.length() == 0) {
                return new ChosenTargetTime(sourceTime, maxTargetTime, true);
            }

            // Parse the real value.
            double delay;
            try {
                delay = CifSimulatorMath.strToReal(choice);
            } catch (CifSimulatorException ex) {
                streams.err.print(fmt("\"%s\" is not a valid real number. Please try again: ", choice));
                streams.err.flush();
                continue;
            }

            // Check interval bounds.
            if (delay <= 0 || maxDelay < delay) {
                streams.err.print(
                        fmt("\"%s\" is not part of the interval (0 .. %s]. Please try again: ", choice, maxDelay));
                streams.err.flush();
                continue;
            }

            // Get target time, and make sure it is past the current time.
            double targetTime = sourceTime + delay;
            if (targetTime == sourceTime) {
                streams.err.print(fmt(
                        "Target time \"%s\" after delay \"%s\" is too close to the current time \"%s\". "
                                + "Please try again: ",
                        CifSimulatorMath.realToStr(targetTime), choice, CifSimulatorMath.realToStr(sourceTime)));
                streams.err.flush();
                continue;
            }

            // Make sure that target time is not past the maximum target time.
            // Due to the code above, mathematically that should never happen.
            // However, due to limited-precision binary floating-point
            // representations being used, it is possible.
            if (targetTime > maxTargetTime) {
                targetTime = maxTargetTime;
            }

            // Return the chosen target time.
            return new ChosenTargetTime(sourceTime, targetTime, false);
        }
    }
}
