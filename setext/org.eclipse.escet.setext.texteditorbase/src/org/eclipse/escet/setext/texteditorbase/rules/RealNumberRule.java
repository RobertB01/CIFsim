//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.setext.texteditorbase.detectors.RealNumberDetector;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Specialized {@link WordRule} that can detect real numbers and highlight them using the given style token.
 *
 * <p>
 * Note that this actually detects too much. For instance, {@code 0-e}, which is an expression with a binary subtraction
 * operator, highlights as a real number. Ideally, we would need a real regular expression {@link IRule rule}.
 * </p>
 *
 * @see RealNumberDetector
 */
public class RealNumberRule extends WordRule {
    /**
     * Constructor for the {@link RealNumberRule} class.
     *
     * @param token The token to use to style the real numbers.
     */
    public RealNumberRule(IToken token) {
        super(new RealNumberDetector(), token);
    }
}
