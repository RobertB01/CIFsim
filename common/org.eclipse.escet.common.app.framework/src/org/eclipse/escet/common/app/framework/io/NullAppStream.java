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

package org.eclipse.escet.common.app.framework.io;

/** {@link AppStream} that does not actually write anything. */
public class NullAppStream extends AppStream {
    /** Singleton instance of the {@link NullAppStream} class. */
    public static final AppStream NULL_APP_STREAM = new NullAppStream();

    @Override
    protected void writeImpl(byte b) {
        // Ignore.
    }

    @Override
    protected void writeImpl(byte[] bytes, int offset, int length) {
        // Ignore.
    }

    @Override
    protected void flushImpl() {
        // Ignore.
    }

    @Override
    protected void closeImpl() {
        // Ignore.
    }
}
