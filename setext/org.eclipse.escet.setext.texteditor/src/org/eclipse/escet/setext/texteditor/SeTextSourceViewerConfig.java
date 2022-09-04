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

package org.eclipse.escet.setext.texteditor;

import static org.eclipse.escet.setext.texteditor.SeTextTextEditorStyleNames.COMMENT_ML;
import static org.eclipse.escet.setext.texteditor.SeTextTextEditorStyleNames.COMMENT_SL;
import static org.eclipse.escet.setext.texteditor.SeTextTextEditorStyleNames.STRING;

import org.eclipse.escet.setext.texteditorbase.GenericSourceViewerConfiguration;
import org.eclipse.escet.setext.texteditorbase.scanners.SingleStyleScanner;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;

/** SeText text editor source viewer configuration. */
public class SeTextSourceViewerConfig extends GenericSourceViewerConfiguration {
    /** The theme to use. */
    private final TextEditorTheme<SeTextTextEditorStyleNames> theme;

    /**
     * Constructor for the {@link SeTextSourceViewerConfig} class.
     *
     * @param theme The theme to use.
     */
    public SeTextSourceViewerConfig(TextEditorTheme<SeTextTextEditorStyleNames> theme) {
        this.theme = theme;
    }

    @Override
    protected void addDamagersRepairers(PresentationReconciler reconciler) {
        // DEFAULT.
        ITokenScanner scanner = new SeTextTextEditorScanner(theme, colorManager);
        addDamagerRepairer(reconciler, scanner, DEFAULT_CONTENT_TYPE);

        // COMMENT_ML.
        ITokenScanner commentMlScanner = new SingleStyleScanner(theme.getStyle(COMMENT_ML).createToken(colorManager));
        addDamagerRepairer(reconciler, commentMlScanner, "__setext_comment_ml");

        // COMMENT_SL.
        ITokenScanner commentSlScanner = new SingleStyleScanner(theme.getStyle(COMMENT_SL).createToken(colorManager));
        addDamagerRepairer(reconciler, commentSlScanner, "__setext_comment_sl");

        // STRING.
        ITokenScanner stringScanner = new SingleStyleScanner(theme.getStyle(STRING).createToken(colorManager));
        addDamagerRepairer(reconciler, stringScanner, "__setext_string");
    }
}
