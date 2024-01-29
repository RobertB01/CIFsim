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

package org.eclipse.escet.setext.texteditor;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.setext.texteditor.SeTextTextEditorStylable.DEFAULT;
import static org.eclipse.escet.setext.texteditor.SeTextTextEditorStylable.DESCRIPTION;
import static org.eclipse.escet.setext.texteditor.SeTextTextEditorStylable.IDENTIFIER;
import static org.eclipse.escet.setext.texteditor.SeTextTextEditorStylable.KEYWORD;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.escet.setext.parser.SeTextScanner;
import org.eclipse.escet.setext.texteditorbase.ColorManager;
import org.eclipse.escet.setext.texteditorbase.RuleBasedScannerEx;
import org.eclipse.escet.setext.texteditorbase.detectors.GenericWhitespaceDetector;
import org.eclipse.escet.setext.texteditorbase.rules.RegExRule;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.WhitespaceRule;

/** SeText text editor default partition scanner. */
public class SeTextTextEditorScanner extends RuleBasedScannerEx {
    /**
     * Constructor for the {@link SeTextTextEditorScanner} class.
     *
     * @param theme The theme to use.
     * @param manager The color manager to use to create the color tokens.
     */
    public SeTextTextEditorScanner(TextEditorTheme<SeTextTextEditorStylable> theme, ColorManager manager) {
        // Get keywords.
        String[] keywords = SeTextScanner.getKeywords("Keywords");

        // Regular expression patterns for keywords, identifiers/names, and descriptions.
        String keywordsPat = Arrays.stream(keywords)
                .map(k -> "(?<![A-Za-z0-9_.\\$])" + Pattern.quote(k) + "(?![A-Za-z0-9_.])")
                .collect(Collectors.joining("|"));
        String idPat = "[$]?[A-Za-z_][A-Za-z0-9_]*";
        String idOrNamePat = fmt("%s([.]%s)*", idPat, idPat);
        String descrPat = "\\[[^\\]]+\\]";

        // Construct and set predicate rules. Make sure we also have a default token.
        IRule[] rules = new IRule[] { //
                new RegExRule(keywordsPat, theme.getStyle(KEYWORD).createToken(manager)),
                new RegExRule(idOrNamePat, theme.getStyle(IDENTIFIER).createToken(manager)),
                new RegExRule(descrPat, theme.getStyle(DESCRIPTION).createToken(manager)),
                new WhitespaceRule(new GenericWhitespaceDetector()),
                //
        };
        setRules(rules);

        setDefaultReturnToken(theme.getStyle(DEFAULT).createToken(manager));
    }
}
