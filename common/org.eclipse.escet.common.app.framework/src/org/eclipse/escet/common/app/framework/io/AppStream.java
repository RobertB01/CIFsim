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
     * (0x0a) and the '\r' character as '\r' byte (0x0d) are supported.
     */
    private final Charset charset = Charset.forName("UTF-8");

    /** Whether to replace each EOL sequence in the stream with the value of the 'new line bytes' property. */
    private boolean convertNewLines = true;

    /** New line bytes to use for new lines. */
    private byte[] newline = Strings.NL.getBytes(charset);

    /**
     * If {@code true}, write provided non-EOL characters to the output and check for a {@code '\r'} character as first
     * part of EOL detection in the output to write. If {@code false}, scan for an optional {@code '\n'} character and
     * write an EOL to the output.
     */
    private boolean beforeCrTest = true;

    /** Whether a {@code '\r'} character was detected before checking for a {@code '\n'} character. */
    private boolean seenCR = false;

    /**
     * Returns the character set to use to encode strings to bytes.
     *
     * @return The character set.
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Set the 'convert new lines' property of the stream. If enabled, EOL sequences are converted to platform specific
     * or custom new line sequences set by the 'new line bytes' property. If disabled, EOL sequences in the stream are
     * left as they are. The property is enabled by default, but can be disabled to allow third party code to write to
     * the stream without changing the characters in the stream.
     *
     * @param convertNewLines Whether to convert EOL sequences to platform specific or custom new lines ({@code true})
     *     or leave EOL sequences as they are found in the stream ({@code false}).
     * @see #getConvertNewLines
     */
    public void setConvertNewLines(boolean convertNewLines) {
        synchronized (this) {
            this.convertNewLines = convertNewLines;
        }
    }

    /**
     * Get the 'convert new lines' property of the stream. If enabled, EOL sequences are converted to platform specific
     * or custom new line characters as set by the 'new line bytes' property. If disabled, EOL sequences are left as
     * they are found in the stream. The property is enabled by default, but can be disabled to allow third party code
     * to write to the stream without changing the characters in the stream.
     *
     * @return {@code true} if the property is enabled, {@code false} otherwise.
     * @see #setConvertNewLines
     * @see #setPlatformNewLineBytes
     * @see #setUnixNewLineBytes
     * @see #setWindowsNewLineBytes
     * @see #setNewLineBytes
     * @see #getNewLineBytes
     */
    public boolean getConvertNewLines() {
        synchronized (this) {
            return convertNewLines;
        }
    }

    /**
     * Set the 'new line bytes' property of the stream. The 'new line bytes' are the platform specific or custom bytes
     * to write when an EOL sequence is to be written and the 'convert new lines' property is enabled. They are also
     * written when {@link #newline} is called. By default platform specific new line bytes are used, but the new line
     * bytes can be changed to write custom EOL sequences.
     *
     * @param bytes The new line bytes to use.
     * @see #setConvertNewLines
     * @see #getConvertNewLines
     * @see #setPlatformNewLineBytes
     * @see #setUnixNewLineBytes
     * @see #setWindowsNewLineBytes
     * @see #getNewLineBytes
     */
    public void setNewLineBytes(byte[] bytes) {
        synchronized (this) {
            this.newline = bytes;
        }
    }

    /**
     * Set the 'new line bytes' property of the stream to use the new line bytes of the current platform. The
     * platform-specific new line bytes are used when an EOL sequence is to be written and the 'convert new lines'
     * property is enabled. They are also written when {@link #newline} is called.
     *
     * @see #setConvertNewLines
     * @see #getConvertNewLines
     * @see #setUnixNewLineBytes
     * @see #setWindowsNewLineBytes
     * @see #setNewLineBytes
     * @see #getNewLineBytes
     */
    public void setPlatformNewLineBytes() {
        synchronized (this) {
            this.newline = Strings.NL.getBytes(charset);
        }
    }

    /**
     * Set the 'new line bytes' property of the stream to use Unix new line bytes. Unix new line bytes ('\n') are used
     * when an EOL sequence is to be written and the 'convert new lines' property is enabled. They are also written when
     * {@link #newline} is called.
     *
     * @see #setConvertNewLines
     * @see #getConvertNewLines
     * @see #setPlatformNewLineBytes
     * @see #setWindowsNewLineBytes
     * @see #setNewLineBytes
     * @see #getNewLineBytes
     */
    public void setUnixNewLineBytes() {
        synchronized (this) {
            this.newline = new byte[] {'\n'};
        }
    }

    /**
     * Set the 'new line bytes' property of the stream to use Microsoft Windows new line bytes. Microsoft Windows new
     * line bytes ('\r' followed by '\n') are used when an EOL sequence is to be written and the 'convert new lines'
     * property is enabled. They are also written when {@link #newline} is called.
     *
     * @see #setConvertNewLines
     * @see #getConvertNewLines
     * @see #setPlatformNewLineBytes
     * @see #setUnixNewLineBytes
     * @see #setNewLineBytes
     * @see #getNewLineBytes
     */
    public void setWindowsNewLineBytes() {
        synchronized (this) {
            this.newline = new byte[] {'\r', '\n'};
        }
    }

    /**
     * Get the 'new line bytes' property of the stream. The 'new line bytes' are the platform specific or custom bytes
     * to write when an EOL sequence is to be written and the 'convert new lines' property is enabled. They are also
     * written when {@link #newline} is called. By default platform specific new line bytes are used, but the new line
     * bytes can be changed to write custom EOL sequences.
     *
     * @return The new line bytes being used.
     * @see #setConvertNewLines
     * @see #getConvertNewLines
     * @see #setPlatformNewLineBytes
     * @see #setUnixNewLineBytes
     * @see #setWindowsNewLineBytes
     * @see #setNewLineBytes
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
            if (beforeCrTest) {
                // Collect non-EOL characters of the current line.
                int eolIdx = curIdx;
                while (eolIdx < bytes.length && bytes[eolIdx] != '\r' && bytes[eolIdx] != '\n') {
                    eolIdx++;
                }

                // If non-EOL characters exist, write them to the output.
                if (eolIdx > curIdx) {
                    // Write the non-EOL bytes to the output.
                    writeImpl(bytes, curIdx, eolIdx - curIdx);
                    curIdx = eolIdx;

                    // We didn't get the entire line, wait for more input.
                    if (curIdx >= bytes.length) {
                        return;
                    }
                }

                // Invariant: eolIdx == curIdx && curIdx < bytes.length.
                //
                // We ran out of non-EOL characters and have not reached the end of the bytes, so there must be an EOL
                // here. That can be "\r", "\n", or "\r\n". In the latter case it's possible that the "\n" is not in the
                // supplied bytes of this call.
                //
                // Here the test of "\r" is performed, which recognizes "\r" completely, "\n" not at all, and the first
                // character of "\r\n". After skipping over "\r" the "beforeCrTest" boolean is reset to denote we've
                // seen "\r" and must check for a "\n" now. That recognizes the "\n" case and the "\r\n" case.
                seenCR = bytes[curIdx] == '\r';
                if (seenCR) {
                    curIdx++;
                }
                beforeCrTest = false;
            } else {
                // Second step in detecting a EOL. We have checked for CR already before and advanced the index if it
                // existed.
                //
                // Next, check for a NL character.
                boolean seenNL = bytes[curIdx] == '\n';
                if (seenNL) {
                    curIdx++;
                }

                // Write new line.
                if (convertNewLines) {
                    newLineInternal();
                } else {
                    if (seenCR) {
                        writeImpl((byte)'\r');
                    }
                    if (seenNL) {
                        writeImpl((byte)'\n');
                    }
                    flushInternal();
                }

                beforeCrTest = true; // Back to normal text-line processing.
                seenCR = false; // Not actually needed but nice.

                // And continue processing with the text of the next line.
            }
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
            writeInternal(new byte[] {b});
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
     * @see #setNewLineBytes
     * @see #getNewLineBytes
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
        if (!beforeCrTest) {
            // Closing while in the middle of detecting an EOL sequence, where we found a '\r', assuming there was no
            // '\n' intended.

            // Write new line.
            if (convertNewLines) {
                newLineInternal();
            } else {
                if (seenCR) {
                    writeImpl((byte)'\r');
                }
            }
        }

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
        // Print stack trace to print stream wrapper of this stream.
        PrintStream printer = asPrintStream();
        ex.printStackTrace(printer);

        // Flush to ensure print stream does not buffer anything.
        printer.flush();
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
