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

package org.eclipse.escet.cif.cif2plc.options;

import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Formal function invocation (function kind based) option. */
public class PlcFormalFuncInvokeFuncOption extends EnumOption<PlcFormalFuncInvokeFunc> {
    /** Constructor for the {@link PlcFormalFuncInvokeFuncOption} class. */
    public PlcFormalFuncInvokeFuncOption() {
        super("Formal function invocation (function kind based)",
                "Specify for which functions to use formal invocation syntax in the generated PLC code. Specify "
                        + "\"all\" for all functions, \"std\" for standard library/conversion functions only, "
                        + "or \"others\" for all but the standard library/conversion functions. [DEFAULT=others]",
                null, "formal-finvoke-func", "FUNC", PlcFormalFuncInvokeFunc.OTHERS, true,
                "For which functions should formal invocation syntax be used in the generated PLC code?");
    }

    @Override
    protected String getDialogText(PlcFormalFuncInvokeFunc value) {
        switch (value) {
            case ALL:
                return "For all functions";

            case STD:
                return "For standard library/conversion functions only";

            case OTHERS:
                return "For all but standard library/conversion functions";

            default:
                throw new RuntimeException("Unknown value: " + value);
        }
    }

    /**
     * For which functions should formal invocation syntax be used in the generated PLC code?
     *
     * @return Value indicating for which functions formal invocation syntax should be used in the generated PLC code.
     */
    public static PlcFormalFuncInvokeFunc getValue() {
        return Options.get(PlcFormalFuncInvokeFuncOption.class);
    }
}
