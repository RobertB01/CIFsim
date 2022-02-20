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

package org.eclipse.escet.chi.runtime.data.io;

import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/** Dummy file handle to catch uses of uninitialized 'file' objects. */
public class DummyChiFileHandle extends ChiFileHandle {
    /** Shared instance of the dummy file handle. */
    public static final DummyChiFileHandle INSTANCE = new DummyChiFileHandle();

    /** Constructor of the {@link DummyChiFileHandle} class. */
    public DummyChiFileHandle() {
        super(null, "");
    }

    @Override
    public int read() {
        throw new ChiSimulatorException("File handle is not initialized.");
    }

    @Override
    public void markStream(int count) {
        throw new ChiSimulatorException("File handle is not initialized.");
    }

    @Override
    public void resetStream() {
        throw new ChiSimulatorException("File handle is not initialized.");
    }

    @Override
    public void write(String data) {
        throw new ChiSimulatorException("File handle is not initialized.");
    }

    @Override
    public boolean isClosed() {
        throw new ChiSimulatorException("File handle is not initialized.");
    }

    @Override
    public void close() {
        throw new ChiSimulatorException("File handle is not initialized.");
    }
}
