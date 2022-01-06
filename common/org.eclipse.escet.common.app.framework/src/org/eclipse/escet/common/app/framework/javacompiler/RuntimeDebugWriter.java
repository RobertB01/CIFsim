//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.javacompiler;

import java.io.CharArrayWriter;
import java.io.IOException;

/** Runtime compiler output writer that prints a stack trace to stdout upon writing to it, for debugging. */
public class RuntimeDebugWriter extends CharArrayWriter {
    @Override
    public void write(int c) {
        // Print stack trace for debugging.
        System.out.println(getClass().getSimpleName());
        RuntimeDebugUtil.printStackTrace();

        // Write output to writer.
        super.write(c);
    }

    @Override
    public void write(char[] cbuf) throws IOException {
        // Print stack trace for debugging.
        System.out.println(getClass().getSimpleName());
        RuntimeDebugUtil.printStackTrace();

        // Write output to writer.
        super.write(cbuf);
    }

    @Override
    public void write(char[] c, int off, int len) {
        // Print stack trace for debugging.
        System.out.println(getClass().getSimpleName());
        RuntimeDebugUtil.printStackTrace();

        // Write output to writer.
        super.write(c, off, len);
    }

    @Override
    public void write(String str) throws IOException {
        // Print stack trace for debugging.
        System.out.println(getClass().getSimpleName());
        RuntimeDebugUtil.printStackTrace();

        // Write output to writer.
        super.write(str);
    }

    @Override
    public void write(String arg0, int arg1, int arg2) {
        // Print stack trace for debugging.
        System.out.println(getClass().getSimpleName());
        RuntimeDebugUtil.printStackTrace();

        // Write output to writer.
        super.write(arg0, arg1, arg2);
    }
}
