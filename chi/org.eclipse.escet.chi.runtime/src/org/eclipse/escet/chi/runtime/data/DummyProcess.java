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

package org.eclipse.escet.chi.runtime.data;

import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/** Dummy process class to detect use of un-initialized process instance variables. */
public class DummyProcess extends CoreProcess<DummyProcess> {
    /** Shared dummy process instance value. */
    public static final DummyProcess INSTANCE = new DummyProcess();

    /** Constructor of the {@link DummyProcess} class. */
    public DummyProcess() {
        super(RunResult.DUMMY);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ChiSimulatorException On <b>any attempt to use</b> the dummy process instance.
     */
    @Override
    public RunResult getState() {
        String msg = "Non-initialized process instance should not be used.";
        throw new ChiSimulatorException(msg);
    }
}
