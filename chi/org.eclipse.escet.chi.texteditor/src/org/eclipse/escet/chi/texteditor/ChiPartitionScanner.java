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

package org.eclipse.escet.chi.texteditor;

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.eclipse.escet.setext.texteditorbase.scanners.GenericPartitionScanner;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

/** Chi text editor partition scanner. */
public class ChiPartitionScanner extends GenericPartitionScanner {
    @Override
    public String[] getTypes() {
        return new String[] {"__chi_comment_sl", "__chi_string"};
    }

    @Override
    protected void fillRules(IToken[] tokens, IPredicateRule[] rules) {
        rules[0] = new EndOfLineRule("#", tokens[0]); // COMMENT_SL
        rules[1] = new MultiLineRule("\"", "\"", tokens[1], '\\'); // STRING
    }

    @Override
    public Set<String> getSpellingTypes() {
        return set("__chi_comment_sl");
    }
}
