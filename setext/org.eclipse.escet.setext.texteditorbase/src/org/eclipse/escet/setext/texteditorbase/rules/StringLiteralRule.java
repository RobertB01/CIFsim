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

package org.eclipse.escet.setext.texteditorbase.rules;

import org.eclipse.escet.setext.texteditorbase.scanners.StringLiteralScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.SingleLineRule;

/**
 * Specialized {@link SingleLineRule} that can detect single line string literals and highlight them using the given
 * style token.
 *
 * <p>
 * Note that this rule does not recognize any escape sequences. To detect them, use the {@link StringLiteralScanner}
 * instead.
 * </p>
 */
public class StringLiteralRule extends SingleLineRule {
    /**
     * Constructor for the {@link StringLiteralRule} class.
     *
     * @param token The token to use to style the string literals.
     */
    public StringLiteralRule(IToken token) {
        super("\"", "\"", token, (char)0, false);
    }
}
