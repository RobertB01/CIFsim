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

package org.eclipse.escet.setext.texteditorbase.rules;

import org.eclipse.escet.setext.texteditorbase.detectors.IdentifierDetector;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Specialized {@link WordRule} that can detect identifiers and highlight them using the given style token.
 *
 * @see IdentifierDetector
 */
public class IdentifiersRule extends WordRule {
    /**
     * Constructor for the {@link IdentifiersRule} class.
     *
     * @param token The token to use to style the identifiers.
     */
    public IdentifiersRule(IToken token) {
        super(new IdentifierDetector(), token);
    }
}
