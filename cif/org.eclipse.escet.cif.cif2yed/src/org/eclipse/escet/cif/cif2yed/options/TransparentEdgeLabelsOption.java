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

package org.eclipse.escet.cif.cif2yed.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to make edge labels transparent. */
public class TransparentEdgeLabelsOption extends BooleanOption {
    /** Constructor for the {@link TransparentEdgeLabelsOption} class. */
    public TransparentEdgeLabelsOption() {
        super(
                // name
                "Transparent edge labels",

                // description
                "Whether to use a transparent background for edge labels (BOOL=yes), "
                        + "or use a solid white background (BOOL=no). [DEFAULT=no]",

                // cmdShort
                't',

                // cmdLong
                "transparent-edge-labels",

                // cmdValue
                "BOOL",

                // defaultValue
                false,

                // showInDialog
                true,

                // optDialogDescr
                "Enable option to use a transparent background for edge labels, "
                        + "disable option to use a solid white background.",

                // optDialogCheckboxText
                "Use transparent edge labels");
    }

    /**
     * Should a transparent background be used for edge labels?
     *
     * @return {@code true} to use a transparent background for edge labels, {@code false} to use a solid white
     *     background.
     */
    public static boolean isEnabled() {
        return Options.get(TransparentEdgeLabelsOption.class);
    }
}
