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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.cif.datasynth.settings.CifDataSynthesisSettingsDefaults;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** Supervisor name option. */
public class SupervisorNameOption extends StringOption {
    /** Constructor for the {@link SupervisorNameOption} class. */
    public SupervisorNameOption() {
        super("Supervisor name", "The name of the resulting supervisor automaton. [DEFAULT=\"sup\"]", 's', "sup-name",
                "SNAME", CifDataSynthesisSettingsDefaults.SUPERVISOR_NAME_DEFAULT, false, true,
                "The name of the resulting supervisor automaton.", "Name:");
    }

    /**
     * Returns the name of the resulting supervisor automaton.
     *
     * @return The name of the resulting supervisor automaton.
     */
    public static String getSupervisorName() {
        return Options.get(SupervisorNameOption.class);
    }
}
