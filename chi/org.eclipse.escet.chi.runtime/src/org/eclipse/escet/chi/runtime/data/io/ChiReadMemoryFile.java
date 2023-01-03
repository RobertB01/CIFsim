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

package org.eclipse.escet.chi.runtime.data.io;

import org.eclipse.escet.common.java.Assert;

/** File handle for reading text from memory. */
public class ChiReadMemoryFile extends ChiFileHandle {
    /** Text of the 'file'. */
    private String input;

    /** Index of the current character. */
    private int index;

    /** Marked position in the input stream. */
    private int markedPos;

    /**
     * Constructor for the {@link ChiReadMemoryFile} class.
     *
     * @param input Text to read.
     */
    public ChiReadMemoryFile(String input) {
        super("_memory_file_", "r");
        this.input = input;
        index = 0;
        markedPos = 0;
    }

    @Override
    public int read() {
        if (index < input.length()) {
            int k = input.charAt(index);
            index++;
            return k;
        } else {
            return -1;
        }
    }

    @Override
    public void markStream(int count) {
        markedPos = index;
    }

    @Override
    public void resetStream() {
        index = markedPos;
    }

    @Override
    public void write(String data) {
        Assert.fail("No writing allowed.");
    }

    @Override
    public boolean isClosed() {
        // Memory stream does not have the notion of closed.
        // Closing also doesn't release resources, so the precise moment
        // of considering a memory stream closed is not that relevant.
        return index >= input.length();
    }

    @Override
    public void close() {
        index = input.length();
        markedPos = input.length();
        // Close always succeeds, and nothing has to be done.
    }
}
