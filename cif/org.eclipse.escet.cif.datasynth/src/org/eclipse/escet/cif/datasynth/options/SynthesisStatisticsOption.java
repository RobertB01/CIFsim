//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.options;

import java.util.EnumSet;

import org.eclipse.escet.common.app.framework.options.EnumSetOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Synthesis statistics option. */
public class SynthesisStatisticsOption extends EnumSetOption<SynthesisStatistics> {
    /** Constructor for the {@link SynthesisStatisticsOption} class. */
    public SynthesisStatisticsOption() {
        super(
                // name
                "Statistics",

                // description
                "The kinds of statistics to print. Specify comma separated names of the statistics. " +

                        "Specify \"timing\" for timing statistics, "
                        + "\"bdd-gc\" for BDD garbage collection statistics, and/or "
                        + "\"bdd-resize\" for BDD node table resize statistics. " +

                        "By default, no statistics are printed. "
                        + "Prefix a name with \"+\" to add it on top of the defaults, "
                        + "or with \"-\" to remove it from the defaults.",

                // cmdShort
                null,

                // cmdLong
                "stats",

                // cmdValue
                "STATS",

                // defaultValue
                EnumSet.noneOf(SynthesisStatistics.class),

                // showInDialog
                true,

                // optDialogDescr
                "The kinds of statistics to print.",

                // enumClass
                SynthesisStatistics.class);
    }

    @Override
    protected String getDialogText(SynthesisStatistics stat) {
        switch (stat) {
            case TIMING:
                return "Timing";
            case BDD_GC:
                return "BDD garbage collection";
            case BDD_RESIZE:
                return "BDD node table resize";
        }
        throw new RuntimeException("Unknown statistic: " + stat);
    }

    /**
     * Returns the statistics to print.
     *
     * @return The statistics to print.
     */
    public static EnumSet<SynthesisStatistics> getStatistics() {
        return Options.get(SynthesisStatisticsOption.class);
    }
}
