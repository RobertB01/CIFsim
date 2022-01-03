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

package org.eclipse.escet.cif.simulator.output.plotviz;

/** Plot visualization mode. */
public enum PlotVisualizationMode {
    /** Automatically choose between live and postponed. */
    AUTO,

    /** Live plotting is performed during simulation. */
    LIVE,

    /** Plotting is postponed until the end of the simulation. */
    POSTPONED;
}
