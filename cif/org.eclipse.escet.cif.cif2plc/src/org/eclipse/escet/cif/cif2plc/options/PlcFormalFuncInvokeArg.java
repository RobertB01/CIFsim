//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

/**
 * When should formal function invocation syntax be used in the PLC code, based on the number of arguments of the
 * function?
 */
public enum PlcFormalFuncInvokeArg {
    /** For all functions. */
    ALL,

    /** For functions with more than one argument. */
    MULTI,

    /** For none of the functions. */
    NONE;
}
