//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.interpreter;

import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;

/** ToolDef interpreter option to invoke a specific tool rather than the entire script. */
public class ToolDefInvokeOption extends StringOption {
    /** The option description. */
    private static final String DESCRIPTION = "The tool to invoke, and its arguments, in ToolDef syntax. "
            + "For example: \"mytool(1, true)\". "
            + "If the option is not used, the entire ToolDef script is executed instead. "
            + "By default, the entire script is executed.";

    /** Constructor for the {@link ToolDefInvokeOption} class. */
    public ToolDefInvokeOption() {
        super(
                // name
                "Tool invocation",

                // description
                DESCRIPTION,

                // cmdShort
                'i',

                // cmdLong
                "invoke",

                // cmdValue
                "INVOCATION",

                // defaultValue
                null,

                // emptyAsNull
                true,

                // showInDialog
                true,

                // optDialogDescr
                DESCRIPTION,

                // optDialogLabelText
                "Invocation:");
    }

    /**
     * Returns the value of the {@link ToolDefInvokeOption} option.
     *
     * @return The value of the {@link ToolDefInvokeOption} option.
     */
    public static String getInvocationText() {
        return Options.get(ToolDefInvokeOption.class);
    }
}
