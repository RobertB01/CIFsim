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

package org.eclipse.escet.cif.simulator.input.trace;

import static org.eclipse.escet.cif.simulator.runtime.SimulationResult.USER_TERMINATED;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.simulator.input.AutomaticInputComponent;
import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.input.InputComponent;
import org.eclipse.escet.cif.simulator.options.TraceInputFileOption;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeEvent;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;

/**
 * Input component that automatically takes decisions without any user interaction, based on a user provided trace file.
 *
 * @param <S> The type of state objects to use.
 * @see TraceInputFileOption
 */
public class TraceInputComponent<S extends RuntimeState> extends InputComponent<S> {
    /** The value of the 'strict' option. Is {@code null} if not yet known. */
    private Boolean strict;

    /** The value of the 'time' option. Is {@code null} if not yet known. */
    private TimeMode timeMode;

    /** The trace commands from the trace file. Is {@code null} until the trace file is processed. */
    private List<TraceCommand> commands;

    /** The 0-based index of the current command, in {@link #commands}. */
    private int curCommandIdx = 0;

    /** Whether to allow time delay before the next event, in explicit time mode. Is {@code false} otherwise. */
    private boolean allowDelay;

    /**
     * The automatic input component to use to defer multiple choices. Only used if {@link #strict} mode is disabled. Is
     * {@code null} until the trace file is processed.
     */
    private AutomaticInputComponent<S> autoInput;

    /**
     * Constructor for the {@link TraceInputComponent} class.
     *
     * @param spec The specification. The specification has not yet been {@link RuntimeSpec#init initialized}.
     */
    public TraceInputComponent(RuntimeSpec<S> spec) {
        super(spec);
    }

    @Override
    public void init() {
        // Get trace file path.
        String path = TraceInputFileOption.getPath();
        String absPath = Paths.resolve(path);

        // Read, check, and process the trace file.
        try {
            readTraceFile(path, absPath);
        } catch (InvalidInputException ex) {
            String msg = fmt("Trace file \"%s\" is invalid.", path);
            throw new InvalidInputException(msg, ex);
        }
    }

    /**
     * Read, check, and process the trace file.
     *
     * @param path The absolute or relative local file system path to the trace file.
     * @param absPath The absolute local file system path to the trace file.
     * @throws InputOutputException In case of an I/O error.
     * @throws InvalidInputException If the trace file is invalid.
     */
    private void readTraceFile(String path, String absPath) {
        // Get lines of text from trace file.
        List<String> lines;
        try {
            InputStream stream = new FileInputStream(absPath);
            stream = new BufferedInputStream(stream);
            lines = IOUtils.readLines(stream, "UTF-8");
        } catch (IOException ex) {
            String msg = fmt("Failed to read trace file \"%s\".", path);
            throw new InputOutputException(msg, ex);
        }

        // Trim lines. Ignore empty and comment lines.
        List<String> lines2 = listc(lines.size());
        for (String line: lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            lines2.add(line);
        }
        lines = lines2;

        // Process options.
        int i = 0;
        while (i < lines.size()) {
            String line = lines.get(i);
            if (!line.startsWith("option")) {
                break;
            }
            i++;

            String[] parts = StringUtils.split(line);
            boolean valid = true;
            if (parts.length != 3) {
                valid = false;
            }
            if (valid && !parts[0].equals("option")) {
                valid = false;
            }
            if (valid) {
                switch (parts[1]) {
                    case "time":
                    case "strict":
                        break;

                    default:
                        valid = false;
                }
            }

            if (valid && parts[1].equals("strict")) {
                switch (parts[2]) {
                    case "on":
                        strict = true;
                        break;
                    case "off":
                        strict = false;
                        break;
                    default:
                        valid = false;
                }
            }
            if (valid && parts[1].equals("time")) {
                switch (parts[2]) {
                    case "off":
                        timeMode = TimeMode.OFF;
                        break;
                    case "implicit":
                        timeMode = TimeMode.IMPLICIT;
                        break;
                    case "explicit":
                        timeMode = TimeMode.EXPLICIT;
                        break;
                    default:
                        valid = false;
                }
            }

            if (!valid) {
                String msg = fmt("Invalid option: \"%s\".", line);
                throw new InvalidInputException(msg);
            }
        }

        // Set default option values, if not specified in trace file.
        if (strict == null) {
            strict = false;
        }
        if (timeMode == null) {
            timeMode = TimeMode.IMPLICIT;
        }

        // Process remaining commands.
        commands = listc(lines.size() - i);
        while (i < lines.size()) {
            String line = lines.get(i);
            i++;

            if (line.startsWith("option")) {
                // 'option * *'
                String msg = fmt("Option after non-option: \"%s\".", line);
                throw new InvalidInputException(msg);
            } else if (line.startsWith("event")) {
                // 'event NAME'
                String[] parts = StringUtils.split(line);
                boolean valid = true;
                if (parts.length != 2) {
                    valid = false;
                }
                if (valid && !parts[0].equals("event")) {
                    valid = false;
                }

                if (!valid) {
                    String msg = fmt("Invalid event command: \"%s\".", line);
                    throw new InvalidInputException(msg);
                }

                int eventIdx = -1;
                for (RuntimeEvent<S> event: spec.events) {
                    String name = event.name.replace("$", "");
                    if (name.equals(parts[1])) {
                        eventIdx = event.idx;
                        break;
                    }
                }

                if (eventIdx == -1) {
                    String msg = fmt("Event \"%s\" not found.", parts[1]);
                    throw new InvalidInputException(msg);
                }

                commands.add(new EventTraceCommand(eventIdx));
            } else if (line.startsWith("time")) {
                // 'time'
                if (!line.equals("time")) {
                    String msg = fmt("Invalid time command: \"%s\".", line);
                    throw new InvalidInputException(msg);
                }
                if (timeMode != TimeMode.EXPLICIT) {
                    String msg = "Time command only allowed for explicit time mode.";
                    throw new InvalidInputException(msg);
                }

                commands.add(new TimeTraceCommand());
            } else {
                // Unknown.
                String msg = fmt("Unknown command: \"%s\".", line);
                throw new InvalidInputException(msg);
            }
        }

        // Initialize automatic input component.
        if (!strict) {
            autoInput = new AutomaticInputComponent<>(spec);
        }
    }

    @Override
    public Transition<S> chooseTransition(S state, List<Transition<S>> transitions, SimulationResult result) {
        // Handle no transitions possible.
        if (transitions.isEmpty()) {
            throw new SimulatorExitException(result);
        }

        // If no more commands present, we are done.
        if (curCommandIdx == commands.size()) {
            throw new SimulatorExitException(USER_TERMINATED);
        }

        // Get current command.
        TraceCommand cmd = commands.get(curCommandIdx);

        // Process command.
        if (cmd instanceof TimeTraceCommand) {
            // Time command.
            Assert.check(timeMode == TimeMode.EXPLICIT);

            // We may delay before the next event.
            allowDelay = true;

            // Proceed with next command.
            curCommandIdx++;
            Assert.check(result == null);
            return chooseTransition(state, transitions, result);
        } else if (cmd instanceof EventTraceCommand) {
            // Event command.
            EventTraceCommand evtCmd = (EventTraceCommand)cmd;
            RuntimeEvent<S> event = spec.events.get(evtCmd.idx);

            // Filter to the chosen event.
            List<Transition<S>> filtered = listc(transitions.size());
            for (Transition<S> transition: transitions) {
                if (transition instanceof EventTransition) {
                    EventTransition<S> etrans = (EventTransition<S>)transition;
                    if (etrans.event == event) {
                        filtered.add(transition);
                    }
                }
            }

            // If no event transition possible, delay for a bit, if allowed
            // and possible.
            if (filtered.isEmpty() && (timeMode == TimeMode.IMPLICIT || allowDelay)) {
                // If a time transition exists, it is the last transition.
                // There can never be more than one time transition, so
                // 'strict' mode doesn't apply.
                Transition<S> lastTrans = null;
                if (!transitions.isEmpty()) {
                    lastTrans = last(transitions);
                }
                if (lastTrans instanceof TimeTransition) {
                    // Choose the time transition. Keep with current command
                    // as we still want to choose the event, later on.
                    return lastTrans;
                }
            }

            // Check deadlock.
            if (filtered.isEmpty()) {
                warn("No transition found for event \"%s\" from the trace input.", event.name);
                throw new SimulatorExitException(SimulationResult.DEADLOCK);
            }

            // Check strict.
            if (strict && filtered.size() > 1) {
                String msg = fmt("Multiple transitions are possible for event \"%s\" from the trace input, but "
                        + "strict mode is enabled, which does not allow multiple matches.", event.name);
                throw new CifSimulatorException(msg);
            }

            // We'll make a choice, so next choice uses next command. Reset
            // allowing a delay until we see the next 'time' command.
            curCommandIdx++;
            allowDelay = false;

            // Not really a choice if only one filtered match.
            if (filtered.size() == 1) {
                return first(filtered);
            }

            // Defer to automatic input component.
            Assert.check(result == null);
            return autoInput.chooseTransition(state, filtered, result);
        } else {
            // Unknown command.
            throw new RuntimeException("Unknown cmd: " + cmd);
        }
    }

    @Override
    public ChosenTargetTime chooseTargetTime(S state, double maxTargetTime) {
        Assert.check(timeMode != TimeMode.OFF);

        // Choose full time transition.
        return new ChosenTargetTime(state.getTime(), maxTargetTime, true);
    }
}
