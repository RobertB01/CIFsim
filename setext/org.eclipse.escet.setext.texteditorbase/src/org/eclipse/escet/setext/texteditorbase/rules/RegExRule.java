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

package org.eclipse.escet.setext.texteditorbase.rules;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.texteditorbase.RuleBasedScannerEx;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * Rule for detecting text that matches a regular expression and highlighting that text using the given style token.
 *
 * <p>
 * This rule can only be added to instances of the {@link RuleBasedScannerEx} class.
 * </p>
 *
 * <p>
 * Performance of this rule is not as good as most rules, as regular expression matching is relatively expensive.
 * However, performance is good enough, as long as the regular expression is 'simple' enough. It is preferable to use a
 * different rule, if available. If no such rule is available however, this rule is a good alternative.
 * </p>
 */
public class RegExRule implements IRule {
    /** The regular expression pattern to use. */
    private final Pattern pattern;

    /** The style token to return if the regular expression is matched. */
    private final IToken token;

    /**
     * Constructor for the {@link RegExRule} class.
     *
     * @param pattern The regular expression pattern to use.
     * @param token The style token to return if the regular expression is matched.
     * @throws PatternSyntaxException If the regular expression is invalid.
     */
    public RegExRule(String pattern, IToken token) {
        this.pattern = Pattern.compile(pattern);
        this.token = token;
    }

    @Override
    public IToken evaluate(ICharacterScanner scanner) {
        // We only support RuleBasedScannerEx instances as scanner, as we
        // need the entire document content for the regular expression matcher,
        // while the standard ICharacterScanner interface only supports reading
        // a single character at a time.
        if (!(scanner instanceof RuleBasedScannerEx)) {
            String msg = fmt("Scanner class %s must implement %s in order for %s rules to be added to it.",
                    scanner.getClass().getName(), RuleBasedScannerEx.class.getName(), RegExRule.class.getName());
            throw new RuntimeException(msg);
        }
        RuleBasedScannerEx scanner2 = (RuleBasedScannerEx)scanner;
        CharSequence cs = new DocumentCharSequence(scanner2.getInternalDocument(), scanner2.getInternalOffset(),
                scanner2.getInternalLength());

        // Construct regular expression matcher, and try to match the regular
        // expression against a prefix of the document content starting at the
        // current scanner position.
        Matcher matcher = pattern.matcher(cs);
        boolean match = matcher.lookingAt();

        // If we found a match, we need to progress the scanner to just beyond
        // the match.
        if (match) {
            int length = matcher.end() - matcher.start();
            for (int i = 0; i < length; i++) {
                int c = scanner.read();
                Assert.check(c != ICharacterScanner.EOF);
            }
        }

        // Return the correct style token.
        return match ? token : Token.UNDEFINED;
    }

    /**
     * Wrapper for {@link IDocument} instances, to view (a part of) them as a {@link CharSequence}, required for regular
     * expression matching.
     */
    public static class DocumentCharSequence implements CharSequence {
        /** The document that is wrapped. */
        private final IDocument document;

        /** The 0-based offset into the document at which to start the character sequence view. */
        private final int offset;

        /** The length of the character sequence view. */
        private final int length;

        /**
         * Constructor for the {@link DocumentCharSequence} class.
         *
         * @param document The document that is wrapped.
         * @param offset The 0-based offset into the document at which to start the character sequence view.
         * @param length The length of the character sequence view.
         */
        public DocumentCharSequence(IDocument document, int offset, int length) {
            Assert.check(offset >= 0);
            Assert.check(length >= 0);
            Assert.check(offset + length <= document.getLength());

            this.document = document;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public char charAt(int index) {
            Assert.check(index >= 0);
            Assert.check(index < length);
            try {
                return document.getChar(offset + index);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            // Sub sequence range is [start..end).
            Assert.check(start >= 0);
            Assert.check(start < length);
            Assert.check(start <= end);
            Assert.check(end <= length);
            return new DocumentCharSequence(document, offset + start, end - start);
        }

        @Override
        public String toString() {
            try {
                return document.get(offset, length);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
