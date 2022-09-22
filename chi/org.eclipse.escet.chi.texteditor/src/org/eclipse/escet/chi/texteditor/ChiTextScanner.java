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

import static org.eclipse.escet.chi.texteditor.ChiTextEditorStylable.DEFAULT;
import static org.eclipse.escet.chi.texteditor.ChiTextEditorStylable.IDENTIFIER;
import static org.eclipse.escet.chi.texteditor.ChiTextEditorStylable.KEYWORD;
import static org.eclipse.escet.chi.texteditor.ChiTextEditorStylable.NUMBER;
import static org.eclipse.escet.chi.texteditor.ChiTextEditorStylable.OPERATOR;
import static org.eclipse.escet.chi.texteditor.ChiTextEditorStylable.STDLIBFUNC;

import org.eclipse.escet.chi.parser.ChiScanner;
import org.eclipse.escet.setext.texteditorbase.ColorManager;
import org.eclipse.escet.setext.texteditorbase.detectors.GenericWhitespaceDetector;
import org.eclipse.escet.setext.texteditorbase.rules.IdentifiersRule;
import org.eclipse.escet.setext.texteditorbase.rules.IntNumberRule;
import org.eclipse.escet.setext.texteditorbase.rules.KeywordsRule;
import org.eclipse.escet.setext.texteditorbase.rules.RealNumberRule;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;

/** Chi text editor default partition scanner. */
public class ChiTextScanner extends RuleBasedScanner {
    /**
     * Constructor for the {@link ChiScanner} class.
     *
     * @param theme The theme to use.
     * @param manager The color manager to use to create the color tokens.
     */
    public ChiTextScanner(TextEditorTheme<ChiTextEditorStylable> theme, ColorManager manager) {
        String[] keywords = ChiScanner.getKeywords("Keywords");
        String[] types = ChiScanner.getKeywords("Types");
        String[] constants = ChiScanner.getKeywords("Constants");

        String[] allKeywords = new String[keywords.length + types.length + constants.length];
        int destPos = 0;
        System.arraycopy(keywords, 0, allKeywords, destPos, keywords.length);
        destPos += keywords.length;
        System.arraycopy(types, 0, allKeywords, destPos, types.length);
        destPos += types.length;
        System.arraycopy(constants, 0, allKeywords, destPos, constants.length);

        String[] stdlibfuncs = ChiScanner.getKeywords("Functions");
        String[] operators = ChiScanner.getKeywords("Operators");

        // Construct and set predicate rules. Make sure we also have a default
        // token.
        IRule[] rules = new IRule[] { //
                new KeywordsRule(allKeywords, theme.getStyle(KEYWORD).createToken(manager)),
                new KeywordsRule(stdlibfuncs, theme.getStyle(STDLIBFUNC).createToken(manager)),
                new KeywordsRule(operators, theme.getStyle(OPERATOR).createToken(manager)),
                new IdentifiersRule(theme.getStyle(IDENTIFIER).createToken(manager)),
                new RealNumberRule(theme.getStyle(NUMBER).createToken(manager)),
                new IntNumberRule(theme.getStyle(NUMBER).createToken(manager)),
                new WhitespaceRule(new GenericWhitespaceDetector()),
                //
        };
        setRules(rules);

        setDefaultReturnToken(theme.getStyle(DEFAULT).createToken(manager));
    }
}
