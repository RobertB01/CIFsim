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

import static org.eclipse.escet.chi.texteditor.ChiTextEditorStyleNames.COMMENT_SL;
import static org.eclipse.escet.chi.texteditor.ChiTextEditorStyleNames.STRING;
import static org.eclipse.escet.chi.texteditor.ChiTextEditorStyleNames.STRING_ESCAPE;

import org.eclipse.escet.setext.texteditorbase.GenericSourceViewerConfiguration;
import org.eclipse.escet.setext.texteditorbase.scanners.FormatStringScanner;
import org.eclipse.escet.setext.texteditorbase.scanners.SingleStyleScanner;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;

/** Chi text editor source viewer configuration. */
public class ChiSourceViewerConfig extends GenericSourceViewerConfiguration {
    /** The theme to use. */
    private final TextEditorTheme<ChiTextEditorStyleNames> theme;

    /**
     * Constructor for the {@link ChiSourceViewerConfig} class.
     *
     * @param theme The theme to use.
     */
    public ChiSourceViewerConfig(TextEditorTheme<ChiTextEditorStyleNames> theme) {
        this.theme = theme;
    }

    @Override
    protected void addDamagersRepairers(PresentationReconciler reconciler) {
        // DEFAULT.
        ITokenScanner scanner = new ChiTextScanner(theme, colorManager);
        addDamagerRepairer(reconciler, scanner, DEFAULT_CONTENT_TYPE);

        // COMMENT_SL.
        ITokenScanner commentSlScanner = new SingleStyleScanner(theme.getStyle(COMMENT_SL).createToken(colorManager));
        addDamagerRepairer(reconciler, commentSlScanner, "__chi_comment_sl");

        // STRING (string literals, paths, format patterns, etc).
        ITokenScanner stringScanner = new FormatStringScanner(theme.getStyle(STRING).createToken(colorManager),
                theme.getStyle(STRING_ESCAPE).createToken(colorManager),
                theme.getStyle(STRING_ESCAPE).createToken(colorManager));
        addDamagerRepairer(reconciler, stringScanner, "__chi_string");
    }
}
