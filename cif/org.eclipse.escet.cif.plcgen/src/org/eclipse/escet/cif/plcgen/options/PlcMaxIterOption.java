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

package org.eclipse.escet.cif.plcgen.options;

import org.eclipse.escet.common.app.framework.options.IntegerOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** PLC maximum iterations option. */
public class PlcMaxIterOption extends IntegerOption {
    /** Constructor for the {@link PlcMaxIterOption} class. */
    public PlcMaxIterOption() {
        super(
                // name
                "PLC maximum iterations",

                // description
                "The maximum number of iterations of the main loop of the main program body, per execution of the "
                        + "main program body. The number must be positive. Use \"inf\" to allow an infinite number of "
                        + "iterations (no restriction). [DEFAULT=100]",

                // cmdShort
                'x',

                // cmdLong
                "max-iter",

                // cmdValue
                "ITERS",

                // defaultValue
                100,

                // minimumValue
                1,

                // maximumValue
                Integer.MAX_VALUE,

                // pageIncrementValue
                10,

                // showInDialog
                true,

                // optDialogDescr
                "The maximum number of iterations of the main loop of the main program body, per execution of the "
                        + "main program body.",

                // optDialogLabelText
                "Maximum iterations:",

                // hasSpecialValue
                true,

                // defaultNormalValue
                100,

                // specialValueSyntax
                "inf",

                // optDialogSpecialText
                "Infinite number of iterations (no restriction)",

                // optDialogNormalText
                "Finite number of iterations");
    }

    /**
     * Returns the maximum number of iterations of the main loop of the main program body, per execution of the main
     * program body.
     *
     * @return The maximum number of iterations, or {@code null} for no maximum.
     */
    public static Integer getMaxIter() {
        return Options.get(PlcMaxIterOption.class);
    }
}
