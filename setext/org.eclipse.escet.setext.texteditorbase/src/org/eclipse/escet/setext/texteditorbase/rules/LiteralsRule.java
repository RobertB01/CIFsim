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

package org.eclipse.escet.setext.texteditorbase.rules;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Literals rule. Can detect the given literals.
 *
 * <p>
 * Note that unlike the {@link WordRule}, which is greedy, this rule is lazy. That is, as soon as the shortest matching
 * literal is detected, this rule stops. As a consequence of this, if one literal is a prefix of another, the longer one
 * is never detected. The lazy aspect also means that this rule never reads more characters (Unicode code points) than
 * necessary to figure out whether any of the literals matches.
 * </p>
 */
public class LiteralsRule implements IRule {
    /**
     * The literals to detect. The 2-dimensional jagged array consists of literals, where each literal consists of code
     * points. Note that the literals are sorted on their lengths, with the shortest first.
     */
    private final int[][] literals;

    /** The style token to return when a literal is detected. */
    private final IToken token;

    /** The buffer with code points read from the scanner. */
    private final int[] buffer;

    /** The current number of characters in the buffer. */
    private int bufCount;

    /**
     * Constructor for the {@link LiteralsRule} class.
     *
     * @param literals The literals to detect, in any order.
     * @param token The style token to return when a literal is detected.
     */
    public LiteralsRule(String[] literals, IToken token) {
        // Convert the literal strings to a jagged 2-dimensional code point
        // array.
        int[][] lits = new int[literals.length][];
        int maxLength = 0;
        for (int i = 0; i < lits.length; i++) {
            String literal = literals[i];
            Assert.check(literal.length() > 0);
            lits[i] = Strings.getCodePoints(literal);
            if (literal.length() > maxLength) {
                maxLength = literal.length();
            }
        }

        // Sort the literals on their lengths, to make sure we can detect the
        // shortest ones first.
        Arrays.sort(lits, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return ((Integer)o1.length).compareTo(o2.length);
            }
        });

        // Store literals and style token. Also construct/initialize buffer.
        this.literals = lits;
        this.token = token;
        this.buffer = new int[maxLength];
        this.bufCount = 0;
    }

    @Override
    public IToken evaluate(ICharacterScanner scanner) {
        // Reset buffer.
        bufCount = 0;

        // Process each of the literals in their sorted order.
        for (int[] literal: literals) {
            if (matchesLiteral(scanner, literal)) {
                return token;
            }
        }

        // No match found.
        unreadBuffer(scanner);
        return Token.UNDEFINED;
    }

    /**
     * Returns a value indicating whether the literal is matched from the scanner's current position.
     *
     * @param scanner The scanner to use to read data.
     * @param literal The literal to detect.
     * @return {@code true} if the literal was detected, {@code false} otherwise.
     */
    private boolean matchesLiteral(ICharacterScanner scanner, int[] literal) {
        for (int i = 0; i < literal.length; i++) {
            int codePoint = getCodePoint(scanner, i);
            if (codePoint == ICharacterScanner.EOF) {
                return false;
            }
            if (codePoint != literal[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the scanned code point at index {@code idx}. Uses the buffer if possible, or reads one more code point
     * otherwise.
     *
     * @param scanner The scanner to use to read code points.
     * @param idx The 0-based index from the start position of the scanner.
     * @return The code point at index {@code idx}, or {@link ICharacterScanner#EOF}.
     * @throws ArrayIndexOutOfBoundsException If an index is provided that is beyond the length of the longest literal.
     * @throws IndexOutOfBoundsException If an index is requested beyond the current buffer, and beyond the first
     *     character after the current buffer. That is, if the requested index is larger than the current amount of code
     *     points in the buffer.
     */
    private int getCodePoint(ICharacterScanner scanner, int idx) {
        if (idx < bufCount) {
            return buffer[idx];
        }
        if (idx == bufCount) {
            int codePoint = scanner.read();
            buffer[idx] = codePoint;
            bufCount++;
            return codePoint;
        }
        throw new IndexOutOfBoundsException("idx > bufCount");
    }

    /**
     * Returns the code points in the buffer to the scanner.
     *
     * @param scanner The scanner to return the code points to.
     */
    private void unreadBuffer(ICharacterScanner scanner) {
        for (int i = 0; i < bufCount; i++) {
            scanner.unread();
        }
    }
}
