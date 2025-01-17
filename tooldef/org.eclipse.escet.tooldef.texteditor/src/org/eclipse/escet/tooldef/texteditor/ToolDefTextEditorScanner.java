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

package org.eclipse.escet.tooldef.texteditor;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.BUILTIN;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.DEFAULT;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.IDENTIFIER;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.KEYWORD;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.NUMBER;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.OPERATOR;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.escet.setext.texteditorbase.ColorManager;
import org.eclipse.escet.setext.texteditorbase.RuleBasedScannerEx;
import org.eclipse.escet.setext.texteditorbase.detectors.GenericWhitespaceDetector;
import org.eclipse.escet.setext.texteditorbase.rules.IntNumberRule;
import org.eclipse.escet.setext.texteditorbase.rules.KeywordsRule;
import org.eclipse.escet.setext.texteditorbase.rules.RegExRule;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.escet.tooldef.parser.ToolDefScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.WhitespaceRule;

/** ToolDef text editor default partition scanner. */
public class ToolDefTextEditorScanner extends RuleBasedScannerEx {
    /**
     * Constructor for the {@link ToolDefTextEditorScanner} class.
     *
     * @param theme The theme to use.
     * @param manager The color manager to use to create the color tokens.
     */
    public ToolDefTextEditorScanner(TextEditorTheme<ToolDefTextEditorStylable> theme, ColorManager manager) {
        // Keywords copied from ToolDef scanner.
        String[] keywords = ToolDefScanner.getKeywords("Keyword");
        String[] operators = ToolDefScanner.getKeywords("Operator");
        String[][] builtinss = { //
                ToolDefScanner.getKeywords("BuiltInIoTool"), //
                ToolDefScanner.getKeywords("BuiltInGenericTool"), //
                ToolDefScanner.getKeywords("BuiltInPathTool"), //
                ToolDefScanner.getKeywords("BuiltInFileTool"), //
                ToolDefScanner.getKeywords("BuiltInDataTool") //
        };
        String[] builtins = {};
        for (String[] elems: builtinss) {
            builtins = ArrayUtils.addAll(builtins, elems);
        }

        // Regular expression patterns for identifiers and names.
        String idPat = "[$]?[A-Za-z_][A-Za-z0-9_]*";
        String namePat = fmt("%s([.]%s)*", idPat, idPat);

        // Regular expression pattern for reals. Note that for the second line,
        // if we switch the alternatives, we get into trouble, as anything
        // that matches "\\.[0-9]+" will go into that alternative (which is
        // then first), and not into the other alternative, even though it
        // could have a longer match.
        String realPat = "(0|[1-9][0-9]*)((\\.[0-9]+)?[eE][\\-\\+]?[0-9]+|\\.[0-9]+)";

        // Construct and set predicate rules. Make sure we also have a default
        // token.
        IRule[] rules = new IRule[] { //
                new KeywordsRule(keywords, theme.getStyle(KEYWORD).createToken(manager)),
                new KeywordsRule(builtins, theme.getStyle(BUILTIN).createToken(manager)),
                new KeywordsRule(operators, theme.getStyle(OPERATOR).createToken(manager)),
                new RegExRule(namePat, theme.getStyle(IDENTIFIER).createToken(manager)),
                new RegExRule(realPat, theme.getStyle(NUMBER).createToken(manager)),
                new IntNumberRule(theme.getStyle(NUMBER).createToken(manager)),
                new WhitespaceRule(new GenericWhitespaceDetector())
                //
        };
        setRules(rules);

        setDefaultReturnToken(theme.getStyle(DEFAULT).createToken(manager));
    }
}
