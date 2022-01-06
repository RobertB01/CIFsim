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

package org.eclipse.escet.cif.cif2mcrl2;

/** Scanner of the instance tree text. */
public class InstanceTreeScanner {
    /** Index in {@link #text} pointing to the first unscanned character. */
    private int index = 0;

    /** Text to scan. */
    private final String text;

    /**
     * Next token to return.
     *
     * <p>
     * <ul>
     * <li>{@code null} means EOF,</li>
     * <li>{@code "("} means parenthesis-open,</li>
     * <li>{@code ")"} means parenthesis-close,</li>
     * <li>otherwise, it is an identifier.</li>
     * </ul>
     * </p>
     */
    private String token;

    /**
     * Constructor of the {@link InstanceTreeScanner} class.
     *
     * @param text Text to scan.
     */
    public InstanceTreeScanner(String text) {
        this.text = text;
        scanNext(); // Initialize #token.
    }

    /** Get the next token. */
    public void scanNext() {
        // Skip any white space.
        while (index < text.length()) {
            char k = text.charAt(index);
            if (k == ' ' || k == ',' || k == '\t' || k == '\n' || k == '\r') {
                index++;
            } else {
                break;
            }
        }

        if (index == text.length()) { // EOF detected.
            token = null;
            return;
        }

        char k = text.charAt(index);
        if (k == '(') { // Parenthesis-open detected.
            token = "(";
            index++;
            return;
        }

        if (k == ')') { // Parenthesis-close detected.
            token = ")";
            index++;
            return;
        }

        // It's a name.
        int j = index++;
        while (index < text.length()) {
            k = text.charAt(index);
            if (k == ' ' || k == ',' || k == '\t' || k == '\n' || k == '\r' || k == '(' || k == ')') {
                break;
            }
            index++;
        }
        token = text.substring(j, index);
    }

    /**
     * Peek whether the next token is an EOF.
     *
     * @return Whether the next token is an EOF.
     */
    public boolean peekEOF() {
        return token == null;
    }

    /**
     * Peek whether the next token is a '(' token.
     *
     * @return Whether the next token is an open parenthesis token.
     */
    public boolean peekParOpen() {
        return !peekEOF() && token.equals("(");
    }

    /**
     * Peek whether the next token is a ')' token.
     *
     * @return Whether the next token is a close parenthesis token.
     */
    public boolean peekParClose() {
        return !peekEOF() && token.equals(")");
    }

    /**
     * Peek the next name if available.
     *
     * @return The name token that is next in the stream, or {@code null} if another token comes first.
     */
    public String peekName() {
        if (peekEOF() || peekParOpen() || peekParClose()) {
            return null;
        }
        return token;
    }
}
