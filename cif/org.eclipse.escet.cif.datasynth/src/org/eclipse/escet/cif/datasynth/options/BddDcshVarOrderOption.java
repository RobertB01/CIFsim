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

import org.eclipse.escet.cif.bdd.settings.BddSettingsDefaults;
import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** BDD DCSH variable ordering option. */
public class BddDcshVarOrderOption extends BooleanOption {
    /** Constructor for the {@link BddDcshVarOrderOption} class. */
    public BddDcshVarOrderOption() {
        super(
                // name
                "BDD DCSH variable ordering algorithm",

                // description
                "Whether to apply the DCSH variable ordering algorithm to improve the initial variable ordering "
                        + "(BOOL=yes), or not apply it (BOOL=no). [DEFAULT=yes]",

                // cmdShort
                null,

                // cmdLong
                "dcsh-order",

                // cmdValue
                "BOOL",

                // defaultValue
                BddSettingsDefaults.DCSH_ENABLED_DEFAULT,

                // showInDialog
                true,

                // optDialogDescr
                "Whether to apply the DCSH variable ordering algorithm to improve the initial variable ordering.",

                // optDialogLabelText
                "Apply DCSH variable ordering algorithm");
    }

    /**
     * Should the DCSH variable ordering algorithm be applied?
     *
     * @return {@code true} to apply the algorithm, {@code false} otherwise.
     */
    public static boolean isEnabled() {
        return Options.get(BddDcshVarOrderOption.class);
    }
}
