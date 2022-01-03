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

package org.eclipse.escet.setext.texteditorbase.scanners;

import org.eclipse.escet.setext.texteditorbase.detectors.GenericWhitespaceDetector;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;

/** Scanner that highlights everything using a single style token. */
public class SingleStyleScanner extends RuleBasedScanner {
    /**
     * Constructor for the {@link SingleStyleScanner} class.
     *
     * @param styleToken The style token.
     */
    public SingleStyleScanner(IToken styleToken) {
        // Always detect whitespace as last rule.
        IRule[] rules = new IRule[1];
        rules[0] = new WhitespaceRule(new GenericWhitespaceDetector());
        setRules(rules);

        // Set default token to given style.
        setDefaultReturnToken(styleToken);
    }
}
