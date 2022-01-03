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

package org.eclipse.escet.cif.simulator.options;

/** The different modes of the {@link AskToTerminateOption}. */
public enum AskToTerminateMode {
    /** Ask user to confirm termination of the simulator. */
    ON,

    /** Terminate the simulator automatically. */
    OFF,

    /** Let simulator decide. */
    AUTO;
}
