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

import org.eclipse.escet.setext.texteditorbase.detectors.IdentifierDetector;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WordRule;

/**
 * Specialized {@link WordRule} that can detect keywords and highlight them using the given style token.
 *
 * <p>
 * In order to correctly highlight identifiers starting with a keyword, it is essential to use the same word detection
 * rules as for identifiers. This class uses the {@link IdentifierDetector}, which is also used by the
 * {@link IdentifiersRule}.
 * </p>
 */
public class KeywordsRule extends WordRule {
    /**
     * Constructor for the {@link KeywordsRule} class.
     *
     * @param keywords The keywords to highlight.
     * @param token The token to use to style the keywords.
     */
    public KeywordsRule(String[] keywords, IToken token) {
        super(new IdentifierDetector());
        for (String keyword: keywords) {
            addWord(keyword, token);
        }
    }
}
