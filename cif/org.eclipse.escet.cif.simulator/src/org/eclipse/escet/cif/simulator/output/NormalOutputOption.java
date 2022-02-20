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

/** Normal console output option. */
public class NormalOutputOption extends EnumSetOption<NormalOutputType> {
    /** The default literals for this option. */
    public static final EnumSet<NormalOutputType> DEFAULTS = getDefaults();

    /** Constructor for the {@link NormalOutputOption} class. */
    public NormalOutputOption() {
        super("Normal output", getCmdLineDescription(), 'o', "output", "OUTPUT", DEFAULTS, true,
                "Indicate which output should be printed to the console, for the \"normal\" and \"debug\" output "
                        + "modes.",
                NormalOutputType.class);
    }

    /**
     * Returns the types of normal output to print to the console, for the {@link OutputMode#NORMAL} and
     * {@link OutputMode#DEBUG} output modes. Note that it is best to cache the results of this method, if the result is
     * used throughout the simulation.
     *
     * @return The types of normal output to print to the console.
     */
    public static EnumSet<NormalOutputType> getOutputTypes() {
        return Options.get(NormalOutputOption.class);
    }

    /**
     * Should the given type of normal output be printed to the console, for the current output mode? Note that it is
     * best to cache the results of this method, if the result is used throughout the simulation.
     *
     * @param type The type of normal output.
     * @return {@code true} if it should be printed, {@code false} otherwise.
     */
    public static boolean doPrint(NormalOutputType type) {
        return getOutputTypes().contains(type);
    }

    @Override
    public void postProcessValue(EnumSet<NormalOutputType> value) {
        // If profiling is enabled, disable all normal output.
        if (Options.get(ProfilingOption.class)) {
            // Clear the requested normal output.
            clear();
        }

        // If no debugging is requested (and the output mode will thus not
        // become debug mode for that reason), and the output mode does not
        // include normal output, disable all normal output.
        if (OutputModeOption.getOutputMode() != OutputMode.NORMAL
                && OutputModeOption.getOutputMode() != OutputMode.DEBUG && DebugOutputOption.getDebugTypes().isEmpty())
        {
            // Clear the requested normal output.
            clear();
        }
    }

    /**
     * Returns the default literals for this option.
     *
     * @return The default literals for this option.
     */
    private static EnumSet<NormalOutputType> getDefaults() {
        return EnumSet.of(NormalOutputType.STATE_INIT, NormalOutputType.STATE_TARGET, NormalOutputType.STATE_ALG_VARS,
                NormalOutputType.STATE_DERIVS, NormalOutputType.TRANS_DEFAULT, NormalOutputType.CHOSEN_TRANS,
                NormalOutputType.INTERRUPTED_TRANS, NormalOutputType.SIM_RSLT, NormalOutputType.SEEDS,
                NormalOutputType.PRINT);
    }

    @Override
    public String getDialogText(NormalOutputType literal) {
        return "The " + literal.description;
    }

    /**
     * Returns the command line arguments help text for this option.
     *
     * @return The command line arguments help text for this option.
     */
    private static String getCmdLineDescription() {
        StringBuilder b = new StringBuilder();
        b.append("Specify comma separated names of the desired types of console output, for the \"normal\" and "
                + "\"debug\" output modes. Specify ");

        NormalOutputType[] literals;
        literals = NormalOutputType.class.getEnumConstants();
        Assert.check(literals.length > 1);

        for (int i = 0; i < literals.length - 1; i++) {
            if (i > 0) {
                b.append(", ");
            }

            NormalOutputType literal = literals[i];
            String litTxt = getCmdLineDescription(literal);
            b.append(litTxt);
        }

        b.append(", and/or ");
        b.append(getCmdLineDescription(literals[literals.length - 1]));
        b.append(". Prefix a name with \"+\" to add it on top of the defaults, or with \"-\" to remove it from "
                + "the defaults.");

        return b.toString();
    }

    /**
     * Returns the command line arguments help text for the given literal.
     *
     * @param literal The enumeration literal.
     * @return The command line arguments help text for the given literal.
     */
    private static String getCmdLineDescription(NormalOutputType literal) {
        String valueTxt = literal.name().toLowerCase(Locale.US).replace('_', '-');
        boolean isDefault = DEFAULTS.contains(literal);
        return fmt("\"%s\" %sto print the %s", valueTxt, isDefault ? "(default) " : "", literal.description);
    }
}
