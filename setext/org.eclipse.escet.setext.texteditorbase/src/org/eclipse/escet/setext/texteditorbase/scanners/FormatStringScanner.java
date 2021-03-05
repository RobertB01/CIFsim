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

package org.eclipse.escet.setext.texteditorbase.scanners;

import org.eclipse.escet.setext.texteditorbase.RuleBasedScannerEx;
import org.eclipse.escet.setext.texteditorbase.detectors.GenericWhitespaceDetector;
import org.eclipse.escet.setext.texteditorbase.rules.LiteralsRule;
import org.eclipse.escet.setext.texteditorbase.rules.RegExRule;
import org.eclipse.escet.setext.texteditorbase.rules.StringLiteralRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WhitespaceRule;

/**
 * Scanner for format pattern string literals. Detects the following escape sequences: {@code \\}, {@code \"},
 * {@code \n}, {@code \t}, and {@code %%}. Furthermore, format specifiers are detected using a regular expression:
 * {@code %([0-9]+[$])?[-+ 0,]*[0-9]*(\.[0-9]+)?[bBdxXeEfgGsS]}. The string itself, the escape sequences, and the format
 * specifiers, can all be styled with different styles.
 *
 * <p>
 * To style string literals without escape sequences, use the {@link StringLiteralRule} instead.
 * </p>
 */
public class FormatStringScanner extends RuleBasedScannerEx {
    /**
     * Constructor for the {@link FormatStringScanner} class.
     *
     * @param stringStyle The style token for the entire string.
     * @param escapeStyle The style token for the escape sequences.
     * @param specifierStyle The style token for the specifier sequences.
     */
    public FormatStringScanner(IToken stringStyle, IToken escapeStyle, IToken specifierStyle) {
        String[] escapeSequences = {"\\n", "\\t", "\\\"", "\\\\"};
        String specRegEx = "%([0-9]+[$])?[-+ 0,]*[0-9]*(\\.[0-9]+)?[bBdxXeEfgGsS]";

        String[] escapedPercentageLiteral = {"%%"};
        IRule[] rules = { //
                new LiteralsRule(escapeSequences, escapeStyle), //
                new LiteralsRule(escapedPercentageLiteral, escapeStyle), //
                new RegExRule(specRegEx, specifierStyle), //
                new WhitespaceRule(new GenericWhitespaceDetector()), //
        };
        setRules(rules);

        setDefaultReturnToken(stringStyle);
    }
}
