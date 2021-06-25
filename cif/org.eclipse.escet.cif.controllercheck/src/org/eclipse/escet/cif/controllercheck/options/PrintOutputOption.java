//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.options;

import org.eclipse.escet.common.app.framework.options.BooleanOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Clustering with bus option. */
public class PrintOutputOption extends BooleanOption {
    /** Constructor for the {@link PrintOutputOption} class. */
    public PrintOutputOption() {
        super(// name.
              "Print control loops",

              // description.
              "Whether to print the events that appear in control loops in the console (BOOL=yes) or not (BOOL=no). "
              + "[DEFAULT=yes]",

              // cmdShort.
              null,

              // cmdLong.
              "printEvents",

              // cmdValue.
              "BOOL",

              // defaultValue.
              true,

              // showInDialog.
              true,

              // optDialogDescr.
              "Print events that appear in control loops to the console.",

              // optDialogCheckboxText.
              "Print events.");
    }

    /**
     * Should the output be printed to the console?
     *
     * @return {@code true} if output printing is requested, {@code false} otherwise.
     */
    public static boolean print() {
        return Options.get(PrintOutputOption.class);
    }
}
