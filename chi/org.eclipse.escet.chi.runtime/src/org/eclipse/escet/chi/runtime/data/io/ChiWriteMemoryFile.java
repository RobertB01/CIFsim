//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.java.Assert;

/** Class for writing in-memory output. */
public class ChiWriteMemoryFile extends ChiFileHandle {
    /** Whether the memory file is open for writing. */
    private boolean open;

    /** String storage for the data written. */
    private StringBuilder builder;

    /** Construct a new memory file to write to. */
    public ChiWriteMemoryFile() {
        super("_memory_file_", "w");
        open = true;
        builder = new StringBuilder();
    }

    @Override
    public int read() {
        Assert.fail("No reading allowed.");
        return 0;
    }

    @Override
    public void markStream(int count) {
        Assert.fail("No marking allowed.");
    }

    @Override
    public void resetStream() {
        Assert.fail("No resetting allowed.");
    }

    @Override
    public void write(String data) {
        if (open) {
            builder.append(data);
        }
    }

    @Override
    public boolean isClosed() {
        return open;
    }

    @Override
    public void close() {
        open = false;
    }

    /**
     * Get the data written to the memory file.
     *
     * @return The collected text written to the stream.
     */
    public String getData() {
        return builder.toString();
    }
}
