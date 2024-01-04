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

import org.eclipse.escet.setext.texteditorbase.detectors.IntNumberDetector;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Specialized {@link WordRule} that can detect integer numbers and highlight them using the given style token.
 *
 * @see IntNumberDetector
 */
public class IntNumberRule extends WordRule {
    /**
     * Constructor for the {@link IntNumberRule} class.
     *
     * @param token The token to use to style the integer numbers.
     */
    public IntNumberRule(IToken token) {
        super(new IntNumberDetector(), token);
    }
}
