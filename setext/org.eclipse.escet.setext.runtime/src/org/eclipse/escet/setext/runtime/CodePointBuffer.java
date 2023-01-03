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

package org.eclipse.escet.setext.runtime;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/**
 * Buffer for storing Unicode code points.
 *
 * <p>
 * This class should outperform {@code List<Integer>}, as this class uses an {@code int[]} array, instead of a
 * {@code Object[]} array. Furthermore, the API is designed and optimized specifically for {@link Scanner scanners}.
 * </p>
 */
public class CodePointBuffer {
    /**
     * The code points buffer. Positive values represent Unicode code points. Value {@code -1} indicates the end-of-file
     * marker.
     */
    private int[] buffer;

    /** The number of code points in the buffer. */
    private int count = 0;

    /** The 0-based index into {@link #buffer} of the head of the buffer. */
    private int head = 0;

    /**
     * The 0-based index into {@link #buffer} of the element directly after the tail of the buffer.
     *
     * <p>
     * This index should be unequal to {@link #head} for non-empty buffers, which implies that {@link #buffer} is never
     * entirely used.
     * </p>
     *
     * <p>
     * The following invariant always holds: {@code count == 0 <=> head == tail}.
     * </p>
     */
    private int tail = 0;

    /**
     * The 0-based index into the logical buffer, of the current position of the buffer. The following invariant always
     * holds: {@code 0 <= position <= count}. That is, it is a position inside the buffer, or at the element directly
     * after the tail of the buffer.
     */
    private int position = 0;

    /** Constructor for the {@link CodePointBuffer} class, with an initial buffer size of {@code 1024} code points. */
    public CodePointBuffer() {
        this(1024);
    }

    /**
     * Constructor for the {@link CodePointBuffer} class.
     *
     * @param bufferSize The initial buffer size, in code points.
     */
    public CodePointBuffer(int bufferSize) {
        buffer = new int[bufferSize];
    }

    /** Enlarge internal buffer such that at least 'minimalElements' values fit in the buffer. */
    private void doubleBufferSize() {
        // Allocate new buffer.
        int newSize = buffer.length * 2;
        Assert.check(newSize >= 0); // Overflow detection.
        int[] newBuffer = new int[newSize];

        // Copy values from old buffer to new buffer, adjusting head and tail
        // if needed.
        if (head <= tail) {
            // [0 .. h - 1] + [h .. t - 1] + [t .. l ]
            // [0 .. h - 1] + [h .. t - 1] + [t .. l + l]

            // Just copy over the continuous region of values.
            System.arraycopy(buffer, head, newBuffer, head, count);
        } else {
            // [0 .. t - 1] + [t .. h - 1] + [h .. l - 1]
            // [0 .. t - 1] + [t .. h + l - 1] + [h + l .. l + l - 1]

            // Copy over tail at same place. Move head to the end of the new
            // buffer.
            System.arraycopy(buffer, 0, newBuffer, 0, tail);
            System.arraycopy(buffer, head, newBuffer, head + buffer.length, buffer.length - head);
            head += buffer.length;
        }

        // Replace buffers.
        buffer = newBuffer;
    }

    /**
     * Adds a code point to the buffer, increasing the buffer size as needed.
     *
     * @param codePoint The code point to add.
     */
    public void add(int codePoint) {
        // Compute next position (position just after tail).
        int next = tail + 1;
        if (next == buffer.length) {
            next = 0;
        }

        // Case distinction on whether the buffer is full (except for the
        // one entry that is never used).
        if (next != head) {
            // Buffer not full.
            buffer[tail] = codePoint;
            tail = next;
        } else {
            // Buffer full. Double buffer size. We either kept data in the
            // same place, or moved the head up.
            doubleBufferSize();
            buffer[tail] = codePoint;
            tail++;
        }
        count++;
    }

    /**
     * Normalizes a 0-based index into the logical buffer (this class), into a 0-based index into the cyclic buffer
     * (field {@link #buffer}).
     *
     * @param index The 0-based index into the logical buffer (this class).
     * @return The 0-based index into the cyclic buffer (field {@link #buffer}).
     * @throws IndexOutOfBoundsException When the given index is too small or too large, such that it is out of bounds.
     */
    private int normalizeIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index < 0");
        }
        if (index >= count) {
            throw new IndexOutOfBoundsException("index >= count");
        }
        int rslt = head + index;
        Assert.check(rslt >= 0); // Detect overflow.
        if (rslt >= buffer.length) {
            rslt -= buffer.length;
        }
        return rslt;
    }

    /**
     * Can a code point be read?
     *
     * @return {@code true} if a code point can be read, {@code false} if insufficient data is available.
     */
    public boolean canRead() {
        // Only if the position is after the buffer, can we not read.
        return position != count;
    }

    /**
     * Reads the code point at the current position.
     *
     * @return The code point at the current position.
     * @throws IllegalStateException If there is insufficient data in the buffer.
     */
    public int read() {
        if (!canRead()) {
            String msg = "Insufficient data in the buffer.";
            throw new IllegalStateException(msg);
        }
        int index = normalizeIndex(position);
        int rslt = buffer[index];
        position++;
        return rslt;
    }

    /**
     * Unreads the given amount of code points.
     *
     * @param amount The number of code points to unread. Must be a non-negative number.
     * @throws IllegalArgumentException If there is insufficient data in the buffer.
     */
    public void unread(int amount) {
        Assert.check(amount >= 0);
        if (position < amount) {
            String msg = "Insufficient data in the buffer.";
            throw new IllegalArgumentException(msg);
        }
        position -= amount;
    }

    /**
     * Peeks the buffer, to see whether end-of-file is at the beginning of the buffer. Does not modify the buffer or the
     * position within the buffer.
     *
     * @return Whether end-of-file is at the beginning of the buffer.
     */
    public boolean peekEndOfFile() {
        if (1 > count) {
            String msg = "Insufficient data in the buffer.";
            throw new IllegalArgumentException(msg);
        }
        if (position < 1) {
            String msg = "Peeking beyond current buffer position.";
            throw new IllegalStateException(msg);
        }

        return buffer[head] == -1;
    }

    /**
     * Removes the prefix, of the given length, from the buffer, and returns it as a {@link String}.
     *
     * <p>
     * A special case is end-of-file at the beginning of the buffer. In such a case, if only a single code point is
     * requested, {@code null} is returned. It is not allowed to poll end-of-file with any other code point.
     * </p>
     *
     * @param length The length of the prefix to 'poll'.
     * @return The prefix of the buffer with the given length, or {@code null} to indicate end-of-file.
     * @throws IllegalArgumentException If there is insufficient data in the buffer.
     * @throws IllegalStateException If end-of-file is polled with other code points; If polling goes beyond the current
     *     buffer position.
     */
    public String pollPrefix(int length) {
        // Precondition checks.
        if (length > count) {
            String msg = "Insufficient data in the buffer.";
            throw new IllegalArgumentException(msg);
        }
        if (position < length) {
            String msg = "Polling beyond current buffer position.";
            throw new IllegalStateException(msg);
        }

        // Get prefix text.
        StringBuilder rslt;
        if (length == 1 && buffer[head] == -1) {
            // Special case for end-of-file.
            rslt = null;
        } else {
            // Convert prefix to string. StringBuilder initial size optimized
            // for 'Basic Multilingual Plane' and 'Supplementary Multilingual
            // Plane'.
            rslt = new StringBuilder(length);
            int index = head;
            for (int i = 0; i < length; i++) {
                int codePoint = buffer[index];
                if (codePoint == -1) {
                    String msg = "Illegal polling of end-of-file with other code points.";
                    throw new IllegalStateException(msg);
                }
                rslt.append(Strings.codePointToStr(codePoint));
                index++;
                if (index == buffer.length) {
                    index = 0;
                }
            }
        }

        // Remove prefix.
        head += length;
        Assert.check(head >= 0); // Overflow check.
        head %= buffer.length;
        count -= length;
        position -= length;

        // Return prefix, converted to string.
        return (rslt == null) ? null : rslt.toString();
    }

    /**
     * Removes the prefix, of the given length, from the buffer, and returns the last removed character.
     *
     * <p>
     * A special case is end-of-file at the beginning of the buffer. In such a case, if only a single code point is
     * requested, {@code null} is returned. It is not allowed to remove end-of-file with any other code point.
     * </p>
     *
     * @param length The length of the prefix to remove.
     * @return The last character of the prefix of the buffer with the given length, or {@code null} to indicate
     *     end-of-file.
     * @throws IllegalArgumentException If there is insufficient data in the buffer.
     * @throws IllegalStateException If end-of-file is removed with other code points; If removing goes beyond the
     *     current buffer position.
     */
    public Character removePrefix(int length) {
        // Precondition checks.
        if (length > count) {
            String msg = "Insufficient data in the buffer.";
            throw new IllegalArgumentException(msg);
        }
        if (position < length) {
            String msg = "Removing beyond current buffer position.";
            throw new IllegalStateException(msg);
        }

        int rslt;
        if (length == 1 && buffer[head] == -1) {
            // Special case for end-of-file.
            rslt = -1;
        } else {
            // Get last character of prefix.
            int index = head;
            rslt = -1;
            for (int i = 0; i < length; i++) {
                int codePoint = buffer[index];
                if (codePoint == -1) {
                    String msg = "Illegal removing of end-of-file with other code points.";
                    throw new IllegalStateException(msg);
                }
                rslt = codePoint;
                index++;
                if (index == buffer.length) {
                    index = 0;
                }
            }
        }

        // Remove prefix.
        head += length;
        Assert.check(head >= 0); // Overflow check.
        head %= buffer.length;
        count -= length;
        position -= length;

        // Return last character, except in case of end-of-file.
        return (rslt == -1) ? null : (char)rslt;
    }
}
