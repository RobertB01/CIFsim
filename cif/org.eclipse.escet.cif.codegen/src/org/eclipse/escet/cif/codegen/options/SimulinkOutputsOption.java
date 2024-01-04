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

package org.eclipse.escet.cif.codegen.options;

import static org.eclipse.escet.common.app.framework.options.processing.PatternMatchingOptionProcessing.makeMatcher;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;
import org.eclipse.escet.common.app.framework.options.processing.PatternMatchingOptionProcessing.OptionMatcher;

/** Option to specify the output variables of the generated Simulink S-Function. */
public class SimulinkOutputsOption extends StringOption {
    /** constructor of the {@link SimulinkOutputsOption} class. */
    public SimulinkOutputsOption() {
        super(
                // name
                "Simulink outputs",

                // description
                "A comma-separated list of variables that should be made available as output from the generated "
                        + "Simulink S-Function block. The \"*\" character may be used to match zero or more "
                        + "characters. If nothing is specified, all variables are made available, use \"nothing\" "
                        + "to get no output variables at all.",

                // cmdShort
                null,

                // cmdLong
                "output-variables",

                // cmdValue
                "VARIABLES",

                // defaultValue, add everything.
                "*",

                // emptyAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                "A comma-separated list of variables that should be made available as output from the generated "
                        + "Simulink S-Function block. The \"*\" character may be used to match zero or more "
                        + "characters. If nothing is specified, all variables are made available, use \"nothing\" "
                        + "to get no output variables at all.",

                // optDialogLabelText
                "Simulink outputs:");
    }

    /**
     * Get a matcher object to decide what variables to add to the output. Returns {@code null} if no variables need to
     * be matched.
     *
     * @return The matcher object to decide what variables to add to the output, {@code null} if no variables should be
     *     made available.
     */
    public static OptionMatcher getMatcher() {
        String optValue = Options.get(SimulinkOutputsOption.class);
        if ("nothing".equals(optValue)) {
            return null;
        }
        return makeMatcher(optValue, true, false, false);
    }
}
