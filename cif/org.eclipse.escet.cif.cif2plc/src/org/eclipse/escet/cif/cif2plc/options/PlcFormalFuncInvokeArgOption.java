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

package org.eclipse.escet.cif.cif2plc.options;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Formal function invocation (arguments based) option. */
public class PlcFormalFuncInvokeArgOption extends EnumOption<PlcFormalFuncInvokeArg> {
    /** Constructor for the {@link PlcFormalFuncInvokeArgOption} class. */
    public PlcFormalFuncInvokeArgOption() {
        super("Formal function invocation (arguments based)",
                "Specify for which functions to use formal invocation syntax in the generated PLC code. Specify "
                        + "\"all\" for all functions, \"multi\" for functions with more than one argument, or "
                        + "\"none\" for none of the functions. [DEFAULT=none]",
                null, "formal-finvoke-arg", "ARG", PlcFormalFuncInvokeArg.NONE, true,
                "For which functions should formal invocation syntax be used in the generated PLC code?");
    }

    @Override
    protected String getDialogText(PlcFormalFuncInvokeArg value) {
        switch (value) {
            case ALL:
                return "For all functions";
            case MULTI:
                return "For functions with more than one argument";
            case NONE:
                return "For none of the functions";
            default:
                throw new RuntimeException("Unknown value: " + value);
        }
    }

    /**
     * For which functions should formal invocation syntax be used in the generated PLC code?
     *
     * @return Value indicating for which functions formal invocation syntax should be used in the generated PLC code.
     */
    public static PlcFormalFuncInvokeArg getValue() {
        return Options.get(PlcFormalFuncInvokeArgOption.class);
    }
}
