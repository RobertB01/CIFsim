//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Code point reader.
 *
 * <p>
 * Java's {@link InputStream} represents a stream of bytes. Such streams can be wrapped by a {@link Reader}, which
 * converts the stream of bytes into a stream of UTF-16 encoded {@link Character characters}. This class can be used to
 * wrap such readers, to convert the stream of characters into a stream of Unicode code points.
 * </p>
 *
 * <p>
 * This class does not extend the {@link Reader} class. While the {@link Reader#read} returns an integer value, which
 * could hold a Unicode code point, the documentation of that method clearly states that only a limited number of values
 * are allowed to be returned. That is, only UTF-16 encoded {@link Character characters} may be returned. Returning
 * arbitrary Unicode code points would violate the method's contract.
 * </p>
 *
 * <p>
 * This reader simply converts UTF-16 encoded characters to Unicode code points, without regards for invalid code
 * points. According to the {@code unicode.org} website's <a href="http://unicode.org/faq/utf_bom.html">FAQ - UTF-8,
 * UTF-16, UTF-32 & BOM</a> page, and in particular Sections <a href="http://unicode.org/faq/utf_bom.html#utf16-7">Are
 * there any 16-bit values that are invalid?</a> and <a href="http://unicode.org/faq/utf_bom.html#utf16-8">Are there any
 * paired surrogates that are invalid?</a>, noncharacters and unpaired surrogates give invalid code points. According to
 * Section <a href="http://unicode.org/faq/utf_bom.html#gen8">Are there any byte sequences that are not generated by a
 * UTF? How should I interpret them?</a>, in such case, an error may be generated, the data may be filtered out, or the
 * data may be replaced by a replacement character. This class does non of the above, and is thus non-conformant, if the
 * reader used to produce UTF-16 encoded characters is non-conformant. In such cases, it is up to the user of this class
 * to take action to make the process conformant. Java's built-in readers however, should all be conformant.
 * </p>
 */
public class CodePointReader {
    /** Reader to use to read UTF-16 encoded characters. */
    private final Reader reader;

    /** Buffered UTF-16 encoded character. Data only valid if {@link #bufferAvailable} is {@code true}. */
    private int buffer;

    /** Whether a {@link #buffer buffered character} is available. */
    private boolean bufferAvailable = false;

    /**
     * Constructor for the {@link CodePointReader} class. Uses buffering for the input stream. Uses {@code "UTF-8"} as
     * {@link java.nio.charset.Charset encoding}.
     *
     * @param stream The input stream, from which to read bytes.
     */
    public CodePointReader(InputStream stream) {
        this(stream, "UTF-8", true);
    }

    /**
     * Constructor for the {@link CodePointReader} class. Uses buffering for the input stream.
     *
     * @param stream The input stream, from which to read bytes.
     * @param encoding The name of the {@link java.nio.charset.Charset encoding} to use to interpret the stream of
     *     bytes.
     */
    public CodePointReader(InputStream stream, String encoding) {
        this(stream, encoding, true);
    }

    /**
     * Constructor for the {@link CodePointReader} class. Uses {@code "UTF-8"} as {@link java.nio.charset.Charset
     * encoding}.
     *
     * @param stream The input stream, from which to read bytes.
     * @param buffered Whether to buffer the input stream.
     */
    public CodePointReader(InputStream stream, boolean buffered) {
        this(stream, "UTF-8", buffered);
    }

    /**
     * Constructor for the {@link CodePointReader} class.
     *
     * @param stream The input stream, from which to read bytes.
     * @param encoding The name of the {@link java.nio.charset.Charset encoding} to use to interpret the stream of
     *     bytes.
     * @param buffered Whether to buffer the input stream.
     */
    public CodePointReader(InputStream stream, String encoding, boolean buffered) {
        this(readerForStream(stream, encoding), buffered);
    }

    /**
     * Constructor for the {@link CodePointReader} class. Uses buffering for the input reader.
     *
     * @param reader The input reader, from which to read UTF-8 encoded characters.
     */
    public CodePointReader(Reader reader) {
        this(reader, true);
    }

    /**
     * Constructor for the {@link CodePointReader} class.
     *
     * @param reader The input reader, from which to read UTF-8 encoded characters.
     * @param buffered Whether to buffer the input reader.
     */
    public CodePointReader(Reader reader, boolean buffered) {
        if (buffered) {
            reader = new BufferedReader(reader);
        }
        this.reader = reader;
    }

    /**
     * Constructs and returns a reader for an input stream, using the given encoding to convert the stream of bytes into
     * a stream of UTF-16 encoded characters.
     *
     * @param stream The input stream, from which to read bytes.
     * @param encoding The name of the {@link java.nio.charset.Charset encoding} to use to interpret the stream of
     *     bytes.
     * @return The input reader, from which to read UTF-8 encoded characters.
     */
    private static Reader readerForStream(InputStream stream, String encoding) {
        try {
            return new InputStreamReader(stream, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Invalid encoding.", e);
        }
    }

    /**
     * Reads a single code point. This method will block until a code point is available, an I/O error occurs, or the
     * end of the stream is reached.
     *
     * @return The code point read, as a non-negative integer, or {@code -1} if the end of the stream has been reached.
     * @exception IOException In case of an I/O error.
     */
    public int read() throws IOException {
        // Get next character, either from buffer, or from input reader.
        int c1;
        if (bufferAvailable) {
            c1 = buffer;
            bufferAvailable = false;
        } else {
            c1 = reader.read();
        }

        // If the current character and next character form a surrogate pair,
        // combine them. Otherwise, return the current character.
        if (Character.isHighSurrogate((char)c1)) {
            int c2 = reader.read();
            if (Character.isLowSurrogate((char)c2)) {
                // Convert surrogate pair to Unicode code point.
                return Character.toCodePoint((char)c1, (char)c2);
            }
            buffer = c2;
            bufferAvailable = true;
        }
        return c1;
    }
}
