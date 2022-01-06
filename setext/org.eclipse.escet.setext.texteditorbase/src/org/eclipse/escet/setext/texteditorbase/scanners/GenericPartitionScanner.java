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

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/** Generic text editor partition scanner. */
public abstract class GenericPartitionScanner extends RuleBasedPartitionScanner {
    /** Constructor for the {@link GenericPartitionScanner} class. */
    public GenericPartitionScanner() {
        // Construct a predicate rule for each token.
        IToken[] tokens = createTokens();
        IPredicateRule[] rules = new IPredicateRule[tokens.length];

        // Fill the predicate rules.
        fillRules(tokens, rules);

        // Set predicate rules.
        setPredicateRules(rules);

        // Make sure the spell check partition types are a subset of the
        // document partition content types.
        Set<String> types = set();
        for (String type: getTypes()) {
            types.add(type);
        }
        Assert.check(types.containsAll(getSpellingTypes()));
    }

    /**
     * Returns the names of the content types (document partition types) that can be identified by this scanner.
     *
     * <p>
     * By convention, the content types names have the following format: {@code __<lang>_<type>}, where {@code <lang>}
     * is the name of the language, and {@code <type>} is the actual content type within the language. For instance, for
     * the XML language, for the COMMENT type, the content type name would be {@code __xml_comment}.
     * </p>
     *
     * @return The names of the content types (document partition types) that can be identified by this scanner.
     */
    public abstract String[] getTypes();

    /**
     * Returns the subset, of the names of the content type (document partition types) that can be identified by this
     * scanner, for which spell checking should be performed.
     *
     * @return The content types for which to perform spell checking.
     */
    public abstract Set<String> getSpellingTypes();

    /**
     * Creates and returns the tokens for all content types (document partition types) that this scanner can identify.
     *
     * @return The tokens for all content types (document partition types) that this scanner can identify.
     */
    private IToken[] createTokens() {
        IToken[] tokens;
        String[] types = getTypes();
        tokens = new Token[types.length];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = new Token(types[i]);
        }
        return tokens;
    }

    /**
     * Fills the predicate rules for the given tokens.
     *
     * @param tokens The tokens for all content types (document partition types) that this scanner can identify.
     * @param rules The predicate rules. This array is of equal length to the {@code tokens} array, and it should be
     *     modified in-place.
     * @see #getTypes
     */
    protected abstract void fillRules(IToken[] tokens, IPredicateRule[] rules);
}
