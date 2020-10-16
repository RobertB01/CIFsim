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

package org.eclipse.escet.common.app.framework.io;

import java.io.UnsupportedEncodingException;

/** {@link AppStream} that writes to memory. */
public class MemAppStream extends AppStream {
    /** The buffer to use. */
    private byte[] buf;

    /** The number of bytes in the buffer. */
    private int size = 0;

    /** Constructor for the {@link MemAppStream} class, with an initial capacity of 32 bytes. */
    public MemAppStream() {
        this(32);
    }

    /**
     * Constructor for the {@link MemAppStream} class.
     *
     * @param initCapacity The initial capacity in bytes.
     */
    public MemAppStream(int initCapacity) {
        buf = new byte[initCapacity];
    }

    @Override
    protected void writeImpl(byte b) {
        if (size == buf.length) {
            byte[] old = buf;
            buf = new byte[old.length * 2];
            System.arraycopy(old, 0, buf, 0, size);
        }
        buf[size] = b;
        size++;
    }

    @Override
    protected void writeImpl(byte[] bytes, int offset, int length) {
        for (int i = offset; i < offset + length; i++) {
            writeImpl(bytes[i]);
        }
    }

    @Override
    protected void flushImpl() {
        // Ignore.
    }

    @Override
    protected void closeImpl() {
        // Ignore.
    }

    @Override
    public String toString() {
        try {
            return new String(buf, 0, size, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
