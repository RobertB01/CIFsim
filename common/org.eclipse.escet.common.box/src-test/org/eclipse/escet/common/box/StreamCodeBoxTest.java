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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.app.framework.io.MemAppStream;

/** Unit tests for the {@link MemoryCodeBox} class. */
public class StreamCodeBoxTest extends CodeBoxTest {
    /** In-memory stream. */
    protected MemAppStream stream = new MemAppStream();

    @Override
    protected CodeBox createCodeBox() {
        return new MemoryStreamCodeBox();
    }

    @Override
    protected CodeBox createCodeBox(int indentAmount) {
        return new MemoryStreamCodeBox(indentAmount);
    }

    /** {@link StreamCodeBox} with in-memory storage. Kind of defeats the purpose, but is ideal for testing. */
    private class MemoryStreamCodeBox extends StreamCodeBox {
        /** Constructor for the {@link MemoryStreamCodeBox} class, with default indentation amount. */
        public MemoryStreamCodeBox() {
            super(stream);
        }

        /**
         * Constructor for the {@link MemoryStreamCodeBox} class, with custom indentation amount.
         *
         * @param indentAmount The indentation amount, the amount of spaces to indent per indentation level.
         */
        public MemoryStreamCodeBox(int indentAmount) {
            super(stream, indentAmount);
        }

        @Override
        public List<String> getLines() {
            if (isEmpty()) {
                return list();
            }

            String text = stream.toString();
            return list(text.split("\\r?\\n"));
        }
    }
}
