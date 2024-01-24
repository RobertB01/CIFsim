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

/** Supervisor namespace option. */
public class SupervisorNamespaceOption extends StringOption {
    /** Constructor for the {@link SupervisorNamespaceOption} class. */
    public SupervisorNamespaceOption() {
        super(
                // name
                "Supervisor namespace",

                // description
                "The namespace of the resulting supervisor. By default, the empty namespace is used.",

                // cmdShort
                'n',

                // cmdLong
                "sup-namespace",

                // cmdValue
                "NS",

                // defaultValue
                CifDataSynthesisSettingsDefaults.SUPERVISOR_NAMESPACE_DEFAULT,

                // emptyAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                "The namespace of the resulting supervisor.",

                // optDialogLabelText
                "Namespace:");
    }

    /**
     * Returns the namespace of the resulting supervisor automaton, or {@code null} for empty namespace.
     *
     * @return The namespace of the resulting supervisor.
     */
    public static String getNamespace() {
        return Options.get(SupervisorNamespaceOption.class);
    }
}
