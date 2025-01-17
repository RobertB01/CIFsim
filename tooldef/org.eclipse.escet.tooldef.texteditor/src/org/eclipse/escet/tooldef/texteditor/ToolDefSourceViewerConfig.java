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

import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.COMMENT_ML;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.COMMENT_SL;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.STRING;
import static org.eclipse.escet.tooldef.texteditor.ToolDefTextEditorStylable.STRING_ESCAPE;

import org.eclipse.escet.setext.texteditorbase.GenericSourceViewerConfiguration;
import org.eclipse.escet.setext.texteditorbase.scanners.FormatStringScanner;
import org.eclipse.escet.setext.texteditorbase.scanners.SingleStyleScanner;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;

/** ToolDef text editor source viewer configuration. */
public class ToolDefSourceViewerConfig extends GenericSourceViewerConfiguration {
    /** The theme to use. */
    private final TextEditorTheme<ToolDefTextEditorStylable> theme;

    /**
     * Constructor for the {@link ToolDefSourceViewerConfig} class.
     *
     * @param theme The theme to use.
     */
    public ToolDefSourceViewerConfig(TextEditorTheme<ToolDefTextEditorStylable> theme) {
        this.theme = theme;
    }

    @Override
    protected void addDamagersRepairers(PresentationReconciler reconciler) {
        // DEFAULT.
        ITokenScanner scanner = new ToolDefTextEditorScanner(theme, colorManager);
        addDamagerRepairer(reconciler, scanner, DEFAULT_CONTENT_TYPE);

        // COMMENT_ML.
        ITokenScanner commentMlScanner = new SingleStyleScanner(theme.getStyle(COMMENT_ML).createToken(colorManager));
        addDamagerRepairer(reconciler, commentMlScanner, "__tooldef_comment_ml");

        // COMMENT_SL.
        ITokenScanner commentSlScanner = new SingleStyleScanner(theme.getStyle(COMMENT_SL).createToken(colorManager));
        addDamagerRepairer(reconciler, commentSlScanner, "__tooldef_comment_sl");

        // STRING (string literals, paths, format patterns, etc).
        ITokenScanner stringScanner = new FormatStringScanner(theme.getStyle(STRING).createToken(colorManager),
                theme.getStyle(STRING_ESCAPE).createToken(colorManager),
                theme.getStyle(STRING_ESCAPE).createToken(colorManager));
        addDamagerRepairer(reconciler, stringScanner, "__tooldef_string");
    }
}
