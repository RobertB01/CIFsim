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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.java.Strings;

/**
 * Output stream that provides the following functionality:
 * <ul>
 * <li>Thread-safe writing.</li>
 * <li>Buffering.</li>
 * <li>Automatic line-based flushing.</li>
 * <li>Application framework exceptions in case of I/O errors.</li>
 * <li>UTF-8 encoding.</li>
 * <li>Platform specific or custom new line characters.</li>
 * </ul>
 *
 * <p>
 * This class does not implement any Java I/O interfaces or extend any Java I/O classes. This prevents having to conform
 * to their API and exceptions.
 * </p>
 */
public abstract class AppStream implements Closeable {
    /**
     * The character set to use to encode strings to bytes. Only encodings that encode the '\n' character as a '\n' byte
     * (0x0a) are supported.
     */
    private final Charset charset = Charset.forName("UTF-8");

    /** Whether to convert '\n' characters to platform new lines. */
    private boolean convertNewLines = true;

    /** New line bytes to use for new lines. */
    private byte[] newline = Strings.NL.getBytes(charset);

    /**
     * Set the 'convert new lines' property of the stream. If enabled, '\n' characters are converted to platform
     * specific or custom new line characters as set by the 'new line bytes' property. If disabled, '\n' characters are
     * left as they are. The property is enabled by default, but can be disabled to allow third party code to write to
     * the stream using custom new lines.
     *
     * @param convertNewLines Whether to convert '\n' characters to platform specific or custom new lines ({@code true})
     *     or leave '\n' characters as they are ({@code false}).
     */
    public void setConvertNewLines(boolean convertNewLines) {
        synchronized (this) {
            this.convertNewLines = convertNewLines;
        }
    }

    /**
     * Get the 'convert new lines' property of the stream. If enabled, '\n' characters are converted to platform
     * specific or custom new line characters as set by the 'new line bytes' property. If disabled, '\n' characters are
     * left as they are. The property is enabled by default, but can be disabled to allow third party code to write to
     * the stream using custom new lines.
     *
     * @return {@code true} if the property is enabled, {@code false} otherwise.
     */
    public boolean getConvertNewLines() {
        synchronized (this) {
            return convertNewLines;
        }
    }

    /**
     * Set the 'new line bytes' property of the stream. The 'new line bytes' are the platform specific or custom bytes
     * to write when a new line is to be written, and also instead of each '\n' character if the 'convert new lines'
     * property is enabled. By default platform specific new line bytes are used, but the new line bytes can be changed
     * to write custom new lines.
     *
     * @param bytes The new line bytes to use.
     */
    public void setNewLineBytes(byte[] bytes) {
        synchronized (this) {
            this.newline = bytes;
        }
    }

    /**
     * Set the 'new line bytes' property of the stream to use Linux (or Mac OS X) new line bytes. Linux new line bytes
     * ('\n') are used when a new line is to be written. The '\n' characters that are written as part of the user
     * provided content are written without modification.
     */
    public void setLinuxNewLineBytes() {
        synchronized (this) {
            this.newline = new byte[] {'\n'};
        }
    }

    /**
     * Set the 'new line bytes' property of the stream to use Microsoft Windows new line bytes. Microsoft Windows new
     * line bytes ('\r' followed by '\n') are used when a new line is to be written. The '\n' characters that are
     * written as part of the user provided content are converted to the Microsoft windows new line bytes as well, if
     * the 'convert new lines' property is enabled.
     */
    public void setWindowsNewLineBytes() {
        synchronized (this) {
            this.newline = new byte[] {'\r', '\n'};
        }
    }

    /**
     * Get the 'new line bytes' property of the stream. The 'new line bytes' are the platform specific or custom bytes
     * to write when a new line is to be written, and also instead of each '\n' character if the 'convert new lines'
     * property is enabled. By default platform specific new line bytes are used, but the new line bytes can be changed
     * to write custom new lines.
     *
     * @return The new line bytes being used.
     */
    public byte[] getNewLineBytes() {
        synchronized (this) {
            return newline;
        }
    }

    /**
     * Writes bytes to the underlying stream.
     *
     * @param bytes The bytes to write.
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#write(byte[])
     */
    public void write(byte[] bytes) {
        synchronized (this) {
            writeInternal(bytes);
        }
    }

    /**
     * Writes bytes to the underlying stream. Internal method without thread safety. Perform automatic line-based
     * flushing.
     *
     * @param bytes The bytes to write.
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#write(byte[])
     */
    private void writeInternal(byte[] bytes) {
        int curIdx = 0;
        while (curIdx < bytes.length) {
            // Look for next new line. Assumes '\n' is present in the byte
            // encoding for new lines.
            int nlIdx = ArrayUtils.indexOf(bytes, (byte)'\n', curIdx);

            // Write remainder if no new line found.
            if (nlIdx == -1) {
                writeImpl(bytes, curIdx, bytes.length - curIdx);
                break;
            }

            // Write part up to next new line.
            writeImpl(bytes, curIdx, nlIdx - curIdx);

            // Write new line.
            if (convertNewLines) {
                newLineInternal();
            } else {
                writeImpl((byte)'\n');
                flushInternal();
            }

            // Continue past the detected '\n'.
            curIdx = nlIdx + 1;
        }
    }

    /**
     * Writes bytes to the underlying stream. It may be assumed that:
     * <ul>
     * <li>{@code offset >= 0}</li>
     * <li>{@code length >= 0}</li>
     * <li>{@code offset + length <= bytes.length}</li>
     * </ul>
     *
     * @param bytes The bytes to write.
     * @param offset The 0-based offset into the bytes.
     * @param length The number of bytes to write.
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#write(byte[])
     */
    protected abstract void writeImpl(byte[] bytes, int offset, int length);

    /**
     * Writes a single byte to the underlying stream.
     *
     * @param b The byte to write.
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#write(int)
     */
    public void write(byte b) {
        synchronized (this) {
            writeInternal(b);
        }
    }

    /**
     * Writes a single byte to the underlying stream. Internal method without thread safety. Perform automatic
     * line-based flushing.
     *
     * @param b The byte to write.
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#write(int)
     */
    private void writeInternal(byte b) {
        // Assumes '\n' is present in the encoded bytes for new lines.
        if (b == '\n') {
            if (convertNewLines) {
                newLineInternal();
            } else {
                writeImpl(b);
                flushInternal();
            }
        } else {
            writeImpl(b);
        }
    }

    /**
     * Writes a single byte to the underlying stream.
     *
     * @param b The byte to write.
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#write(int)
     */
    protected abstract void writeImpl(byte b);

    /**
     * Writes a single new line to the underlying stream, and flushes. The configured platform specific or custom
     * {@link #newline} bytes are used.
     *
     * @throws InputOutputException In case of an I/O error.
     */
    public void newLine() {
        synchronized (this) {
            newLineInternal();
        }
    }

    /**
     * Writes a single new line to the underlying stream, and flushes. The configured platform specific or custom
     * {@link #newline} bytes are used. Internal method without thread safety.
     *
     * @throws InputOutputException In case of an I/O error.
     */
    private void newLineInternal() {
        // Write configured platform specific or custom new line bytes.
        writeImpl(newline, 0, newline.length);

        // Automatic line-based flushing.
        flushInternal();
    }

    /**
     * Flushes the underlying stream.
     *
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#flush()
     */
    public void flush() {
        synchronized (this) {
            flushInternal();
        }
    }

    /**
     * Flushes the underlying stream. Internal method without thread safety.
     *
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#flush()
     */
    private void flushInternal() {
        flushImpl();
    }

    /**
     * Flushes the underlying stream.
     *
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#flush()
     */
    protected abstract void flushImpl();

    /**
     * Flushes and closes the underlying stream.
     *
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#close()
     */
    @Override
    public void close() {
        synchronized (this) {
            closeInternal();
        }
    }

    /**
     * Flushes and closes the underlying stream. Internal method without thread safety.
     *
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#close()
     */
    private void closeInternal() {
        // Flush before closing. This ensures we flush it, and not the close
        // method, which may also hide flushing errors.
        flushInternal();

        // Close the stream.
        closeImpl();
    }

    /**
     * Closes the underlying stream.
     *
     * @throws InputOutputException In case of an I/O error.
     * @see OutputStream#close()
     */
    protected abstract void closeImpl();

    /**
     * Writes a string to the underlying stream.
     *
     * @param s The string to write.
     * @throws InputOutputException In case of an I/O error.
     */
    public void print(String s) {
        synchronized (this) {
            printInternal(s);
        }
    }

    /**
     * Writes a formatted string to the underlying stream.
     *
     * @param s The string to format.
     * @param args The format arguments.
     * @throws InputOutputException In case of an I/O error.
     * @see #print(String)
     * @see Strings#fmt
     */
    public void printf(String s, Object... args) {
        print(fmt(s, args));
    }

    /**
     * Writes a new line to the underlying stream.
     *
     * @throws InputOutputException In case of an I/O error.
     */
    public void println() {
        newLine();
    }

    /**
     * Writes a string to the underlying stream, followed by a new line.
     *
     * @param s The string to write.
     * @throws InputOutputException In case of an I/O error.
     */
    public void println(String s) {
        synchronized (this) {
            printInternal(s);
            newLineInternal();
        }
    }

    /**
     * Writes a formatted string to the underlying stream, followed by a new line.
     *
     * @param s The string to format.
     * @param args The format arguments.
     * @throws InputOutputException In case of an I/O error.
     * @see #print(String)
     * @see Strings#fmt
     */
    public void printfln(String s, Object... args) {
        println(fmt(s, args));
    }

    /**
     * Writes a string to the underlying stream. Internal method without thread safety.
     *
     * @param s The string to write.
     * @throws InputOutputException In case of an I/O error.
     */
    private void printInternal(String s) {
        byte[] bytes = s.getBytes(charset);
        writeInternal(bytes);
    }

    /**
     * Prints a stack trace to the underlying stream.
     *
     * @param ex The exception for which to print the stack trace.
     * @see Throwable#printStackTrace
     */
    public void printStackTrace(Throwable ex) {
        synchronized (this) {
            printStackTraceInternal(ex);
        }
    }

    /**
     * Prints a stack trace to the underlying stream. Internal method without thread safety.
     *
     * @param ex The exception for which to print the stack trace.
     * @see Throwable#printStackTrace
     */
    public void printStackTraceInternal(Throwable ex) {
        // Disable new line conversion, as Throwable.printStackTrace uses
        // platform new lines.
        boolean oldConvertNewLines = convertNewLines;
        convertNewLines = false;

        // Print stack trace to print stream wrapper of this stream.
        PrintStream printer = asPrintStream();
        ex.printStackTrace(printer);

        // Flush to ensure print stream does not buffer anything.
        printer.flush();

        // Restore new line conversion property value.
        convertNewLines = oldConvertNewLines;
    }

    /**
     * Wraps the {@link AppStream} in an {@link OutputStream} for compatibility with APIs that require an
     * {@link OutputStream}.
     *
     * <p>
     * The caller should ensure that after writing, all output is flushed, to flush all data from the
     * {@link OutputStream} to the {@link AppStream}, in case the {@link OutputStream} is wrapped with an additional
     * buffering stream, to avoid additional buffering outside the reach of the {@link AppStream}.
     * </p>
     *
     * @return A freshly created wrapper {@link OutputStream} for this {@link AppStream}.
     */
    public OutputStream asOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                AppStream.this.write((byte)b);
            }
        };
    }

    /**
     * Wraps the {@link AppStream} in a {@link PrintStream} for compatibility with APIs that require a
     * {@link PrintStream}.
     *
     * <p>
     * The caller should ensure that after writing, all output is flushed, to flush all data from the
     * {@link PrintStream} to the {@link AppStream}, to avoid additional buffering outside the reach of the
     * {@link AppStream}.
     * </p>
     *
     * @return A freshly created wrapper {@link PrintStream} for this {@link AppStream}.
     */
    public PrintStream asPrintStream() {
        try {
            // Use automatic flushing and our character set, for compatibility.
            return new PrintStream(asOutputStream(), true, charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wraps the {@link AppStream} in a {@link Writer} for compatibility with APIs that require a {@link Writer}.
     *
     * <p>
     * The caller should ensure that after writing, all output is flushed, to flush all data from the {@link Writer} to
     * the {@link AppStream}, to avoid additional buffering outside the reach of the {@link AppStream}.
     * </p>
     *
     * @return A freshly created wrapper {@link Writer} for this {@link AppStream}.
     */
    public Writer asWriter() {
        // Use our character set, for compatibility.
        return new OutputStreamWriter(asOutputStream(), charset);
    }
}
