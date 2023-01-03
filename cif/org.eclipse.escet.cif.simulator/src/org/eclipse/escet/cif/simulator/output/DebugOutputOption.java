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
import java.util.Locale;

import org.eclipse.escet.cif.simulator.options.ProfilingOption;
import org.eclipse.escet.common.app.framework.options.EnumSetOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.java.Assert;

/** Debug console output option. */
public class DebugOutputOption extends EnumSetOption<DebugOutputType> {
    /** The default literals for this option. */
    public static final EnumSet<DebugOutputType> DEFAULTS = getDefaults();

    /** Constructor for the {@link DebugOutputOption} class. */
    public DebugOutputOption() {
        super("Debug output", getCmdLineDescription(), 'd', "debug", "DEBUG", DEFAULTS, true,
                "Indicate which debug output should be printed to the console. If any debug output is enabled, "
                        + "the debug output mode is automatically enabled.",
                DebugOutputType.class);
    }

    /**
     * Returns the types of debug output to print to the console, for the {@link OutputMode#DEBUG} output mode. Note
     * that it is best to cache the results of this method, if the result is used throughout the simulation.
     *
     * @return The types of debug output to print to the console.
     */
    public static EnumSet<DebugOutputType> getDebugTypes() {
        return Options.get(DebugOutputOption.class);
    }

    /**
     * Should the given type of debug output be printed to the console, for the current output mode? Note that it is
     * best to cache the results of this method, if the result is used throughout the simulation.
     *
     * @param type The type of debug output.
     * @return {@code true} if it should be printed, {@code false} otherwise.
     */
    public static boolean doPrint(DebugOutputType type) {
        return getDebugTypes().contains(type);
    }

    @Override
    public void postProcessValue(EnumSet<DebugOutputType> value) {
        // If profiling is enabled, disable all debugging.
        if (Options.get(ProfilingOption.class)) {
            // Clear the requested debugging.
            clear();
        }

        // If any debugging is requested, enable the debugging output mode.
        if (!value.isEmpty()) {
            Options.set(OutputModeOption.class, OutputMode.DEBUG);
        }
    }

    /**
     * Returns the default literals for this option.
     *
     * @return The default literals for this option.
     */
    private static EnumSet<DebugOutputType> getDefaults() {
        return EnumSet.noneOf(DebugOutputType.class);
    }

    @Override
    public String getDialogText(DebugOutputType literal) {
        return "Debug the " + literal.description;
    }

    /**
     * Returns the command line arguments help text for this option.
     *
     * @return The command line arguments help text for this option.
     */
    private static String getCmdLineDescription() {
        StringBuilder b = new StringBuilder();
        b.append("Specify comma separated names of the desired types of console debug output. If any debug output "
                + "is specified, the debug output mode is automatically enabled. Specify ");

        DebugOutputType[] literals;
        literals = DebugOutputType.class.getEnumConstants();
        Assert.check(literals.length > 1);

        for (int i = 0; i < literals.length - 1; i++) {
            if (i > 0) {
                b.append(", ");
            }

            DebugOutputType literal = literals[i];
            String litTxt = getCmdLineDescription(literal);
            b.append(litTxt);
        }

        b.append(", and/or ");
        b.append(getCmdLineDescription(literals[literals.length - 1]));
        b.append(". Prefix a name with \"+\" to add it on top of the defaults, or with \"-\" to remove it from the "
                + "defaults.");

        return b.toString();
    }

    /**
     * Returns the command line arguments help text for the given literal.
     *
     * @param literal The enumeration literal.
     * @return The command line arguments help text for the given literal.
     */
    private static String getCmdLineDescription(DebugOutputType literal) {
        String valueTxt = literal.name().toLowerCase(Locale.US).replace('_', '-');
        boolean isDefault = DEFAULTS.contains(literal);
        return fmt("\"%s\" %sto debug the %s", valueTxt, isDefault ? "(default) " : "", literal.description);
    }
}
