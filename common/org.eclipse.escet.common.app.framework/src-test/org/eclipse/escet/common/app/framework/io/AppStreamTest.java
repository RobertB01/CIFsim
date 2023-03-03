//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import static org.junit.Assert.assertEquals;

import org.eclipse.escet.common.java.Assert;
import org.junit.Test;

/** EOL handling tests in {@link AppStream}. */
public class AppStreamTest {
    /**
     * Write the given texts to a new {@code AppStream} with disabled EOL sequence conversion, and extract the
     * resulting text.
     *
     * @param texts Text to write, each element is written as a single call to {@link AppStream#write(byte[])}.
     * @return The resulting text from the stream after writing all texts and closing the stream.
     */
    private String writeRawTexts(String... texts) {
        MemAppStream stream = new MemAppStream();
        stream.setConvertNewLines(false);

        convertTexts(stream, texts);
        return stream.toString();
    }

    /**
     * Write the given texts to a new {@code AppStream} with Unix EOL sequences, and extract the resulting text.
     *
     * @param texts Text to write, each element is written as a single call to {@link AppStream#write(byte[])}.
     * @return The resulting text from the stream after writing all texts and closing the stream.
     */
    private String writeLinuxTexts(String... texts) {
        MemAppStream stream = new MemAppStream();
        stream.setConvertNewLines(true);
        stream.setLinuxNewLineBytes();

        convertTexts(stream, texts);
        return stream.toString();
    }

    /**
     * Write the given texts to a new {@code AppStream} with Microsoft Windows EOL sequences, and extract the resulting
     * text.
     *
     * @param texts Text to write, each element is written as a single call to {@link AppStream#write(byte[])}.
     * @return The resulting text from the stream after writing all texts and closing the stream.
     */
    private String writeWindowsTexts(String... texts) {
        MemAppStream stream = new MemAppStream();
        stream.setConvertNewLines(true);
        stream.setWindowsNewLineBytes();

        convertTexts(stream, texts);
        return stream.toString();
    }

    /**
     * Write the given texts to the given stream and close the stream.
     *
     * @param stream Stream to write to.
     * @param texts Text to write, each element is written as a single call to {@link AppStream#write(byte[])}.
     */
    private void convertTexts(AppStream stream, String... texts) {
        for (int i = 0; i < texts.length; i++) {
            String text = texts[i];
            byte[] bytes = new byte[text.length()];
            for (int j = 0; j < text.length(); j++) {
                int c = text.charAt(j);
                Assert.check(c >= 0 && c < 128); // Assuming ASCII.
                bytes[j] = (byte)c;
            }
            stream.write(bytes);
        }

        stream.close();
    }

    @Test
    @SuppressWarnings("javadoc")
    public void rawTextWriteTest() {
        // Write single string.
        assertEquals("", writeRawTexts());
        assertEquals("ax", writeRawTexts("ax"));
        assertEquals("a\rx", writeRawTexts("a\rx"));
        assertEquals("a\nx", writeRawTexts("a\nx"));
        assertEquals("a\r\nx", writeRawTexts("a\r\nx"));
        assertEquals("a\n\rx", writeRawTexts("a\n\rx"));

        assertEquals("ax", writeRawTexts("a", "x"));
        assertEquals("a\rx", writeRawTexts("a", "\rx"));
        assertEquals("a\nx", writeRawTexts("a", "\nx"));
        assertEquals("a\r\nx", writeRawTexts("a", "\r\nx"));
        assertEquals("a\n\rx", writeRawTexts("a", "\n\rx"));

        assertEquals("a\rx", writeRawTexts("a\r", "x"));
        assertEquals("a\nx", writeRawTexts("a\n", "x"));
        assertEquals("a\r\nx", writeRawTexts("a\r", "\nx"));
        assertEquals("a\n\rx", writeRawTexts("a\n", "\rx"));

        assertEquals("a\r\nx", writeRawTexts("a\r\n", "x"));
        assertEquals("a\n\rx", writeRawTexts("a\n\r", "x"));

        // Write single characters.
        assertEquals("ax", writeRawTexts("a", "x"));
        assertEquals("a\rx", writeRawTexts("a", "\r", "x"));
        assertEquals("a\nx", writeRawTexts("a", "\n", "x"));
        assertEquals("a\r\nx", writeRawTexts("a", "\r", "\n", "x"));
        assertEquals("a\n\rx", writeRawTexts("a", "\n", "\r", "x"));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void linuxTextWriteTest() {
        // Write single string.
        assertEquals("", writeLinuxTexts());
        assertEquals("ax", writeLinuxTexts("ax"));
        assertEquals("a\nx", writeLinuxTexts("a\rx"));
        assertEquals("a\nx", writeLinuxTexts("a\nx"));
        assertEquals("a\nx", writeLinuxTexts("a\r\nx"));
        assertEquals("a\n\nx", writeLinuxTexts("a\n\rx")); // \n\r is seen as 2 EOL sequences.

        assertEquals("ax", writeLinuxTexts("a", "x"));
        assertEquals("a\nx", writeLinuxTexts("a", "\rx"));
        assertEquals("a\nx", writeLinuxTexts("a", "\nx"));
        assertEquals("a\nx", writeLinuxTexts("a", "\r\nx"));
        assertEquals("a\n\nx", writeLinuxTexts("a", "\n\rx")); // \n\r is seen as 2 EOL sequences.

        assertEquals("a\nx", writeLinuxTexts("a\r", "x"));
        assertEquals("a\nx", writeLinuxTexts("a\n", "x"));
        assertEquals("a\nx", writeLinuxTexts("a\r", "\nx"));
        assertEquals("a\n\nx", writeLinuxTexts("a\n", "\rx")); // \n\r is seen as 2 EOL sequences.

        assertEquals("a\nx", writeLinuxTexts("a\r\n", "x"));
        assertEquals("a\n\nx", writeLinuxTexts("a\n\r", "x")); // \n\r is seen as 2 EOL sequences.

        // Write single characters.
        assertEquals("ax", writeLinuxTexts("a", "x"));
        assertEquals("a\nx", writeLinuxTexts("a", "\r", "x"));
        assertEquals("a\nx", writeLinuxTexts("a", "\n", "x"));
        assertEquals("a\nx", writeLinuxTexts("a", "\r", "\n", "x"));
        assertEquals("a\n\nx", writeLinuxTexts("a", "\n", "\r", "x")); // \n\r is seen as 2 EOL sequences.
    }

    @Test
    @SuppressWarnings("javadoc")
    public void windowsTextWriteTest() {
        // Write single string.
        assertEquals("", writeWindowsTexts());
        assertEquals("ax", writeWindowsTexts("ax"));
        assertEquals("a\r\nx", writeWindowsTexts("a\rx"));
        assertEquals("a\r\nx", writeWindowsTexts("a\nx"));
        assertEquals("a\r\nx", writeWindowsTexts("a\r\nx"));
        assertEquals("a\r\n\r\nx", writeWindowsTexts("a\n\rx")); // \n\r is seen as 2 EOL sequences.

        assertEquals("ax", writeWindowsTexts("a", "x"));
        assertEquals("a\r\nx", writeWindowsTexts("a", "\rx"));
        assertEquals("a\r\nx", writeWindowsTexts("a", "\nx"));
        assertEquals("a\r\nx", writeWindowsTexts("a", "\r\nx"));
        assertEquals("a\r\n\r\nx", writeWindowsTexts("a", "\n\rx")); // \n\r is seen as 2 EOL sequences.

        assertEquals("a\r\nx", writeWindowsTexts("a\r", "x"));
        assertEquals("a\r\nx", writeWindowsTexts("a\n", "x"));
        assertEquals("a\r\nx", writeWindowsTexts("a\r", "\nx"));
        assertEquals("a\r\n\r\nx", writeWindowsTexts("a\n", "\rx")); // \n\r is seen as 2 EOL sequences.

        assertEquals("a\r\nx", writeWindowsTexts("a\r\n", "x"));
        assertEquals("a\r\n\r\nx", writeWindowsTexts("a\n\r", "x")); // \n\r is seen as 2 EOL sequences.

        // Write single characters.
        assertEquals("ax", writeWindowsTexts("a", "x"));
        assertEquals("a\r\nx", writeWindowsTexts("a", "\r", "x"));
        assertEquals("a\r\nx", writeWindowsTexts("a", "\n", "x"));
        assertEquals("a\r\nx", writeWindowsTexts("a", "\r", "\n", "x"));
        assertEquals("a\r\n\r\nx", writeWindowsTexts("a", "\n", "\r", "x")); // \n\r is seen as 2 EOL sequences.
    }

    @Test
    @SuppressWarnings("javadoc")
    public void initialEolTextWriteTest() {
        // Single initial EOL sequences.
        assertEquals("\n_x", writeRawTexts("\n_x"));
        assertEquals("\r_x", writeRawTexts("\r_x"));
        assertEquals("\r\n_x", writeRawTexts("\r\n_x"));
        assertEquals("\n\r_x", writeRawTexts("\n\r_x"));

        assertEquals("\n_x", writeLinuxTexts("\n_x"));
        assertEquals("\n_x", writeLinuxTexts("\r_x"));
        assertEquals("\n_x", writeLinuxTexts("\r\n_x"));
        assertEquals("\n\n_x", writeLinuxTexts("\n\r_x")); // \n\r is seen as 2 EOL sequences.

        assertEquals("\r\n_x", writeWindowsTexts("\n_x"));
        assertEquals("\r\n_x", writeWindowsTexts("\r_x"));
        assertEquals("\r\n_x", writeWindowsTexts("\r\n_x"));
        assertEquals("\r\n\r\n_x", writeWindowsTexts("\n\r_x")); // \n\r is seen as 2 EOL sequences.

        // Two EOL sequences.
        assertEquals("\n\n_x", writeRawTexts("\n\n_x"));
        assertEquals("\r\r_x", writeRawTexts("\r\r_x"));
        assertEquals("\r\n\r\n_x", writeRawTexts("\r\n\r\n_x"));
        assertEquals("\n\r\n\r_x", writeRawTexts("\n\r\n\r_x"));

        assertEquals("\n\n_x", writeLinuxTexts("\n\n_x"));
        assertEquals("\n\n_x", writeLinuxTexts("\r\r_x"));
        assertEquals("\n\n_x", writeLinuxTexts("\r\n\r\n_x"));
        assertEquals("\n\n\n_x", writeLinuxTexts("\n\r\n\r_x")); // Interpreted as "\n" "\r\n" "\r".

        assertEquals("\r\n\r\n_x", writeWindowsTexts("\n\n_x"));
        assertEquals("\r\n\r\n_x", writeWindowsTexts("\r\r_x"));
        assertEquals("\r\n\r\n_x", writeWindowsTexts("\r\n\r\n_x"));
        assertEquals("\r\n\r\n\r\n_x", writeWindowsTexts("\n\r\n\r_x"));  // Interpreted as "\n" "\r\n" "\r".
    }

    @Test
    @SuppressWarnings("javadoc")
    public void trailingEolTextWriteTest() {
        // Single initial EOL sequences.
        assertEquals("y_\n", writeRawTexts("y_\n"));
        assertEquals("y_\r", writeRawTexts("y_\r"));
        assertEquals("y_\r\n", writeRawTexts("y_\r\n"));
        assertEquals("y_\n\r", writeRawTexts("y_\n\r"));

        assertEquals("y_\n", writeLinuxTexts("y_\n"));
        assertEquals("y_\n", writeLinuxTexts("y_\r"));
        assertEquals("y_\n", writeLinuxTexts("y_\r\n"));
        assertEquals("y_\n\n", writeLinuxTexts("y_\n\r")); // \n\r is seen as 2 EOL sequences.

        assertEquals("y_\r\n", writeWindowsTexts("y_\n"));
        assertEquals("y_\r\n", writeWindowsTexts("y_\r"));
        assertEquals("y_\r\n", writeWindowsTexts("y_\r\n"));
        assertEquals("y_\r\n\r\n", writeWindowsTexts("y_\n\r")); // \n\r is seen as 2 EOL sequences.

        // Two EOL sequences.
        assertEquals("y_\n\n", writeRawTexts("y_\n\n"));
        assertEquals("y_\r\r", writeRawTexts("y_\r\r"));
        assertEquals("y_\r\n\r\n", writeRawTexts("y_\r\n\r\n"));
        assertEquals("y_\n\r\n\r", writeRawTexts("y_\n\r\n\r"));

        assertEquals("y_\n\n", writeLinuxTexts("y_\n\n"));
        assertEquals("y_\n\n", writeLinuxTexts("y_\r\r"));
        assertEquals("y_\n\n", writeLinuxTexts("y_\r\n\r\n"));
        assertEquals("y_\n\n\n", writeLinuxTexts("y_\n\r\n\r")); // Interpreted as "\n" "\r\n" "\r".

        assertEquals("y_\r\n\r\n", writeWindowsTexts("y_\n\n"));
        assertEquals("y_\r\n\r\n", writeWindowsTexts("y_\r\r"));
        assertEquals("y_\r\n\r\n", writeWindowsTexts("y_\r\n\r\n"));
        assertEquals("y_\r\n\r\n\r\n", writeWindowsTexts("y_\n\r\n\r"));  // Interpreted as "\n" "\r\n" "\r".
    }

    @Test
    @SuppressWarnings("javadoc")
    public void multiLineTextWriteTest() {
        assertEquals("abc_\n_def_\r_ghi_\r\n_x", writeRawTexts("abc_\n_def_\r_ghi_\r\n_x"));
        assertEquals("abc_\n_def_\n_ghi_\n_x", writeLinuxTexts("abc_\n_def_\r_ghi_\r\n_x"));
        assertEquals("abc_\r\n_def_\r\n_ghi_\r\n_x", writeWindowsTexts("abc_\n_def_\r_ghi_\r\n_x"));
    }
}
