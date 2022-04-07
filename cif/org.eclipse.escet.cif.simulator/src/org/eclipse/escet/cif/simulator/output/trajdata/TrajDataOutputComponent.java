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

package org.eclipse.escet.cif.simulator.output.trajdata;

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.intToStr;
import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.simulator.output.NullSimulatorOutputComponent;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateFilterer;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;
import org.eclipse.escet.cif.simulator.runtime.meta.StateObjectType;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.ode.Trajectories;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** CIF simulator output component that writes trajectory data to a text file. */
public class TrajDataOutputComponent extends NullSimulatorOutputComponent {
    /** The absolute local file system path to the trajectory data file. */
    private final String path;

    /** Trajectory data file stream. May be {@code null} after output component cleanup. */
    private AppStream stream;

    /**
     * The meta data of the state objects, in the order that they are to be included in the output. Is {@code null}
     * until initialized for the initial state. The first object is always variable 'time'.
     */
    private List<RuntimeStateObjectMeta> metas;

    /** The header text. Is {@code null} until initialized for the initial state. */
    private String header;

    /**
     * The width of each column. The items match {@link #metas}. The value is {@code null} until initialized for the
     * initial state. It remains {@code null} if {@link #sep} equals {@code null}.
     */
    private int[] widths;

    /**
     * The number of spaces to use to separate columns in the trajectory data file, or {@code null} to disable
     * prettifying.
     */
    private final Integer sep;

    /** Whether the output component has {@link #finish finished}. */
    private boolean finished = false;

    /** Constructor for the {@link TrajDataOutputComponent} class. */
    public TrajDataOutputComponent() {
        path = TrajDataFileOption.getAbsPath();
        sep = TrajDataSepOption.getSep();
        open();
    }

    /** Opens the trajectory data file for writing. */
    private void open() {
        Assert.check(stream == null);
        try {
            stream = new FileAppStream(path);
        } catch (InputOutputException e) {
            String msg = fmt("Failed to open trajectory data file \"%s\".", path);
            throw new InputOutputException(msg, e);
        }
    }

    /** Closes the trajectory data file, if it is currently open. */
    private void close() {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    @Override
    public void initialState(RuntimeState state) {
        // Get state object meta data, filtered based on object type and data
        // type.
        metas = list();
        for (RuntimeStateObjectMeta meta: state.spec.stateObjectsMeta) {
            // Filter out automata.
            if (meta.type == StateObjectType.AUTOMATON) {
                continue;
            }

            // Keep numeric and boolean types.
            Object value = state.getVarValue(meta);
            if (!isSupportedType(value)) {
                continue;
            }

            // Add state object meta data.
            metas.add(meta);
        }

        // Filter state objects based on their absolute names.
        String filtersTxt = TrajDataFiltersOption.getFilters();
        metas = RuntimeStateFilterer.filter(metas, filtersTxt, "Trajectory data",
                "included in the trajectory data file, except for variable \"time\"");

        // Make sure variable 'time' is always included.
        if (metas.isEmpty() || first(metas).type != StateObjectType.TIME) {
            metas.add(0, first(state.spec.stateObjectsMeta));
        }

        // Initialize header.
        List<String> names = listc(metas.size());
        for (RuntimeStateObjectMeta meta: metas) {
            names.add(meta.name);
        }
        header = "# " + String.join(" ", names);

        // Initialize the widths.
        if (sep != null) {
            // Initialize to length of the variable name.
            widths = new int[metas.size()];
            for (int i = 0; i < metas.size(); i++) {
                widths[i] = metas.get(i).name.length();
            }

            // Account for '# ' in header comment, before 'time'.
            widths[0] += 2;
        }
    }

    /**
     * Is the given value of a supported type?
     *
     * @param value The value.
     * @return {@code true} if the value is of a supported type, {@code false} otherwise.
     */
    private boolean isSupportedType(Object value) {
        return value instanceof Boolean || value instanceof Integer || value instanceof Double;
    }

    /**
     * Converts a given value to a textual representation, closely resembling the CIF ASCII syntax.
     *
     * @param value The value to convert.
     * @return A textual representation of the value.
     */
    private String valueToText(Object value) {
        if (value instanceof Boolean) {
            return ((Boolean)value) ? "1" : "0";
        }
        if (value instanceof Integer) {
            return intToStr((Integer)value);
        }
        if (value instanceof Double) {
            return realToStr((Double)value);
        }
        throw new RuntimeException("Unexpected value: " + value);
    }

    @Override
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, RuntimeState targetState,
            Boolean interrupted)
    {
        // Skip non-time transitions.
        if (!(transition instanceof TimeTransition<?>)) {
            return;
        }

        // Print header.
        stream.println(header);

        // Get trajectories.
        TimeTransition<?> ttrans = (TimeTransition<?>)transition;
        Trajectories trajs = ttrans.getTrajectories();

        // Get source time, target time, and potential intermediate time points
        // to output.
        List<Double> times = trajs.getTimes();
        double sourceTime = sourceState.getTime();
        double targetTime = targetState.getTime();

        // Output for source state.
        outputLine(sourceState);

        // For the intermediate time points, output them for the times between
        // the source time and the target time. Note that if the delay chosen
        // is smaller than the maximum delay, some of the potential
        // intermediate time points may be larger than the target time.
        for (int idx = 0; idx < times.size(); idx++) {
            // Get time for current index.
            double time = times.get(idx);

            // Skip source state time point.
            if (time <= sourceTime) {
                continue;
            }

            // As soon as we hit the target state time point, we stop.
            if (time >= targetTime) {
                break;
            }

            // Output for intermediate time point.
            RuntimeState state = ttrans.getTargetStateForIndex(idx);
            outputLine(state);
        }

        // Output for target state.
        outputLine(targetState);
    }

    /**
     * Outputs a line to the trajectory data file for the given state.
     *
     * @param state The state.
     */
    private void outputLine(RuntimeState state) {
        // Initialize line.
        StringBuilder line = new StringBuilder();

        // Process all state objects.
        for (int i = 0; i < metas.size(); i++) {
            // Get value.
            RuntimeStateObjectMeta meta = metas.get(i);
            Object value = state.getVarValue(meta);

            // Add text.
            String txt = valueToText(value);
            if (i > 0) {
                line.append(" ");
            }
            line.append(txt);

            // Update width.
            if (sep != null && txt.length() > widths[i]) {
                widths[i] = txt.length();
            }
        }

        // Print line.
        stream.println(line.toString());
    }

    @Override
    public void simulationEnded(SimulationResult rslt, RuntimeState state) {
        // Finish, as we won't get more simulation output.
        finish();
    }

    @Override
    public void cleanup() {
        // Finish, even if simulation crashed.
        finish();
    }

    /**
     * The output component has finished. Close the trajectory data file, and prettify if requested.
     *
     * <p>
     * This method may be invoked multiple times, but the final tasks are only performed once.
     * </p>
     */
    private void finish() {
        // Finish only once.
        if (finished) {
            return;
        }
        finished = true;

        // Close the trajectory data file, and prettify it.
        close();
        if (sep != null) {
            prettify();
        }
    }

    /** Prettify the trajectory data file. */
    private void prettify() {
        // Rename trajectory data file.
        String pathTmp = path + ".tmp";
        Path p = java.nio.file.Paths.get(path);
        Path ptmp = java.nio.file.Paths.get(pathTmp);
        try {
            Files.move(p, ptmp, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            String msg = fmt("Failed to rename trajectory data file \"%s\" to \"%s\".", path, pathTmp);
            throw new InputOutputException(msg, e);
        }

        // Open the files, and process them line by line. Afterwards, close the
        // files, regardless of whether we succeeded.
        BufferedReader input = null;
        try {
            // Open both.
            open();
            try {
                input = new BufferedReader(new FileReader(pathTmp));
            } catch (IOException e) {
                String msg = fmt("Failed to open trajectory data file \"%s\".", pathTmp);
                throw new InputOutputException(msg, e);
            }

            // Process, line by line.
            String line;
            while (true) {
                // Get next line.
                try {
                    line = input.readLine();
                } catch (IOException e) {
                    String msg = fmt("Failed to read from trajectory data file \"%s\".", pathTmp);
                    throw new InputOutputException(msg, e);
                }
                if (line == null) {
                    break;
                }

                // Create and write pretty line.
                StringBuilder prettyLine = new StringBuilder();
                boolean commentLine = line.startsWith("#");
                if (commentLine) {
                    line = line.substring(2);
                }
                String[] parts = StringUtils.split(line, ' ');
                if (commentLine) {
                    parts[0] = "# " + parts[0];
                }
                for (int i = 0; i < parts.length; i++) {
                    if (i > 0) {
                        prettyLine.append(Strings.spaces(sep));
                    }
                    prettyLine.append(parts[i]);
                    if (i != parts.length - 1) {
                        int cnt = widths[i] - parts[i].length();
                        if (cnt > 0) {
                            prettyLine.append(Strings.spaces(cnt));
                        }
                    }
                }
                stream.println(prettyLine.toString());
            }
        } finally {
            // Close both.
            close();
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                String msg = fmt("Failed to close trajectory data file \"%s\".", pathTmp);
                throw new InputOutputException(msg, e);
            }

            // Remove temporary file.
            try {
                Files.delete(ptmp);
            } catch (IOException e) {
                String msg = fmt("Failed to delete trajectory data file \"%s\".", pathTmp);
                throw new InputOutputException(msg, e);
            }
        }
    }

    /**
     * Returns the option category with options for this output component.
     *
     * @return The option category with options for this output component.
     */
    @SuppressWarnings("rawtypes")
    public static OptionCategory getOptions() {
        List<OptionCategory> subCats = list();

        List<Option> opts = list();
        opts.add(Options.getInstance(TrajDataOption.class));
        opts.add(Options.getInstance(TrajDataFileOption.class));
        opts.add(Options.getInstance(TrajDataFiltersOption.class));
        opts.add(Options.getInstance(TrajDataSepOption.class));

        return new OptionCategory("Trajectory data", "Trajectory data file output options.", subCats, opts);
    }
}
