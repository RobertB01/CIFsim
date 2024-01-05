//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.texteditorbase.scanners;

import org.eclipse.escet.setext.texteditorbase.detectors.GenericWhitespaceDetector;
import org.eclipse.escet.setext.texteditorbase.rules.LiteralsRule;
import org.eclipse.escet.setext.texteditorbase.rules.StringLiteralRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;

/**
 * Scanner for string literals. Detects the following escape sequences: {@code \\}, {@code \"}, {@code \n}, and
 * {@code \t}. Both the string itself and the escape sequences can be styled.
 *
 * <p>
 * To style string literals without escape sequences, use the {@link StringLiteralRule} instead.
 * </p>
 */
public class StringLiteralScanner extends RuleBasedScanner {
    /**
     * Constructor for the {@link StringLiteralScanner} class.
     *
     * @param stringStyle The style token for the entire string.
     * @param escapeStyle The style token for the escape sequences.
     */
    public StringLiteralScanner(IToken stringStyle, IToken escapeStyle) {
        IRule[] rules = new IRule[2];
        rules[0] = new LiteralsRule(new String[] {"\\n", "\\t", "\\\"", "\\\\"}, escapeStyle);
        rules[1] = new WhitespaceRule(new GenericWhitespaceDetector());
        setRules(rules);

        setDefaultReturnToken(stringStyle);
    }
}
