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
}
