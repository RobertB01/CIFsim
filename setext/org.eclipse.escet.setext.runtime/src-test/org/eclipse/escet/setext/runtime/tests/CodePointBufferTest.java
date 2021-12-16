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

package org.eclipse.escet.setext.runtime.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.escet.setext.runtime.CodePointBuffer;
import org.junit.Test;

/** Unit tests for the {@link CodePointBuffer} class. */
@SuppressWarnings("javadoc")
public class CodePointBufferTest {
    @Test
    public void testAddRead() {
        // Start with buffer size 10.
        CodePointBuffer buf = new CodePointBuffer(10);

        // Add 30 code points, which requires resize to 20 and 40.
        for (int i = 0; i < 30; i++) {
            buf.add(i + 1);
        }

        // Read 30 code points and see if they match what we put in there.
        for (int i = 0; i < 30; i++) {
            assertEquals(i + 1, buf.read());
        }
    }

    @Test
    public void testReadBeyondBuffer() {
        CodePointBuffer buf = new CodePointBuffer();
        buf.add(123);
        assertEquals(123, buf.read());
        try {
            buf.read();
            throw new AssertionError("Read beyond buffer succeeded...");
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testReadUnread() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(1);
        buf.add(2);
        buf.add(3);
        assertEquals(1, buf.read());
        assertEquals(2, buf.read());
        assertEquals(3, buf.read());

        buf.unread(1);
        assertEquals(3, buf.read());

        buf.unread(2);
        assertEquals(2, buf.read());
        assertEquals(3, buf.read());

        buf.unread(3);
        assertEquals(1, buf.read());
        assertEquals(2, buf.read());
        assertEquals(3, buf.read());

        buf.unread(1);
        buf.unread(1);
        assertEquals(2, buf.read());
        assertEquals(3, buf.read());

        buf.unread(1);
        buf.unread(1);
        assertEquals(2, buf.read());
        buf.unread(1);
        buf.unread(1);
        assertEquals(1, buf.read());
    }

    @Test
    public void testUnreadBeyondBuffer() {
        CodePointBuffer buf = new CodePointBuffer();

        try {
            buf.unread(1);
            throw new AssertionError("Unread beyond buffer succeeded...");
        } catch (IllegalArgumentException e) {
            // Expected.
        }

        buf.add(1);
        buf.read();
        buf.unread(1);
        buf.read();
        try {
            buf.unread(2);
            throw new AssertionError("Unread beyond buffer succeeded...");
        } catch (IllegalArgumentException e) {
            // Expected.
        }

        buf.add(2);
        buf.read();
        buf.unread(1);
        buf.read();
        buf.unread(2);
        buf.read();
        buf.read();
        try {
            buf.unread(3);
            throw new AssertionError("Unread beyond buffer succeeded...");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    @Test
    public void testPeekEndOfFile() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(1);
        assertEquals(1, buf.read());
        assertEquals(false, buf.peekEndOfFile());

        buf.add(2);
        assertEquals(2, buf.read());
        assertEquals(false, buf.peekEndOfFile());

        buf.add(3);
        assertEquals(3, buf.read());
        assertEquals(false, buf.peekEndOfFile());

        buf.add(-1);
        assertEquals(false, buf.peekEndOfFile());

        assertEquals(-1, buf.read());
        assertEquals(false, buf.peekEndOfFile());

        assertEquals((char)1, (char)buf.removePrefix(1));
        assertEquals(false, buf.peekEndOfFile());

        assertEquals((char)3, (char)buf.removePrefix(2));
        assertEquals(true, buf.peekEndOfFile());
    }

    @Test
    public void testPeekEndOfFileBeyondPosition() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(1);
        buf.add(2);

        try {
            buf.peekEndOfFile();

            String msg = "Peeking beyond current position succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testPeekEndOfFileBeyondBuffer() {
        CodePointBuffer buf = new CodePointBuffer();

        try {
            buf.peekEndOfFile();

            String msg = "Peeking beyond buffer succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    @Test
    public void testPollPrefix() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add('a');
        buf.add('b');
        buf.add('c');
        buf.add('d');
        buf.add('e');
        buf.add(-1);

        assertEquals('a', buf.read());
        assertEquals("a", buf.pollPrefix(1));

        assertEquals('b', buf.read());
        assertEquals('c', buf.read());
        assertEquals("b", buf.pollPrefix(1));
        assertEquals("c", buf.pollPrefix(1));

        assertEquals('d', buf.read());
        assertEquals('e', buf.read());
        assertEquals("de", buf.pollPrefix(2));

        assertEquals(-1, buf.read());
        assertEquals(null, buf.pollPrefix(1));

        try {
            buf.read();
            throw new AssertionError("Read beyond buffer succeeded...");
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testPollPrefixBeyondPosition() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(1);
        buf.add(2);

        try {
            buf.pollPrefix(2);

            String msg = "Polling beyond current position succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testPollPrefixBeyondBuffer() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(1);

        try {
            buf.pollPrefix(2);

            String msg = "Polling beyond buffer succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    @Test
    public void testPollPrefixEofAndMore() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(-1);
        buf.add('a');
        assertEquals(-1, buf.read());
        assertEquals('a', buf.read());

        try {
            buf.pollPrefix(2);

            String msg = "Polling beyond eof succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testPollPrefixNonFirstEof() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add('a');
        buf.add(-1);
        assertEquals('a', buf.read());
        assertEquals(-1, buf.read());

        try {
            buf.pollPrefix(2);

            String msg = "Polling non-first eof succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testRemovePrefix() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add('a');
        buf.add('b');
        buf.add('c');
        buf.add('d');
        buf.add('e');
        buf.add(-1);

        assertEquals('a', buf.read());
        assertEquals('a', (char)buf.removePrefix(1));

        assertEquals('b', buf.read());
        assertEquals('c', buf.read());
        assertEquals("b", buf.pollPrefix(1));
        assertEquals('c', (char)buf.removePrefix(1));

        assertEquals('d', buf.read());
        assertEquals('e', buf.read());
        assertEquals('e', (char)buf.removePrefix(2));

        assertEquals(-1, buf.read());
        assertEquals(null, buf.removePrefix(1));

        try {
            buf.read();
            throw new AssertionError("Read beyond buffer succeeded...");
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testRemovePrefixBeyondPosition() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(1);
        buf.add(2);

        try {
            buf.removePrefix(2);

            String msg = "Removing beyond current position succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testRemovePrefixBeyondBuffer() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(1);

        try {
            buf.removePrefix(2);

            String msg = "Removing beyond buffer succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    @Test
    public void testRemovePrefixEofAndMore() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add(-1);
        buf.add('a');
        assertEquals(-1, buf.read());
        assertEquals('a', buf.read());

        try {
            buf.removePrefix(2);

            String msg = "Removing beyond eof succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testRemovePrefixNonFirstEof() {
        CodePointBuffer buf = new CodePointBuffer();

        buf.add('a');
        buf.add(-1);
        assertEquals('a', buf.read());
        assertEquals(-1, buf.read());

        try {
            buf.removePrefix(2);

            String msg = "Removing non-first eof succeeded...";
            throw new AssertionError(msg);
        } catch (IllegalStateException e) {
            // Expected.
        }
    }

    @Test
    public void testCyclicBehavior() {
        CodePointBuffer buf = new CodePointBuffer(19);

        int roll = 0;
        for (int i = 0; i < 1024; i += 3) {
            // Add/read.
            for (int j = 0; j < roll; j++) {
                buf.add(i + j + 1);
            }
            for (int j = 0; j < roll; j++) {
                assertEquals(i + j + 1, buf.read());
            }

            // No more read.
            try {
                buf.read();
                throw new AssertionError("Read beyond buffer succeeded...");
            } catch (IllegalStateException e) {
                // Expected.
            }

            // Unread all at once.
            buf.unread(roll);

            // No more unread.
            try {
                buf.unread(1);
                throw new AssertionError("Unread beyond buffer succeeded...");
            } catch (IllegalArgumentException e) {
                // Expected.
            }

            // Read again.
            for (int j = 0; j < roll; j++) {
                assertEquals(i + j + 1, buf.read());
            }

            // Unread one by one.
            for (int j = 0; j < roll; j++) {
                buf.unread(1);
            }

            // No more unread.
            try {
                buf.unread(1);
                throw new AssertionError("Unread beyond buffer succeeded...");
            } catch (IllegalArgumentException e) {
                // Expected.
            }

            // Read again.
            for (int j = 0; j < roll; j++) {
                assertEquals(i + j + 1, buf.read());
            }

            // Poll prefix.
            for (int k = 0; k < roll; k++) {
                String actual = buf.pollPrefix(1);
                String expected = Character.toString((char)(i + k + 1));
                assertEquals(expected, actual);
            }

            // Update roll.
            roll++;
            roll %= 13;
        }
    }
}
