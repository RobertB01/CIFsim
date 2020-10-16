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

package org.eclipse.escet.chi.texteditor;

import org.eclipse.escet.setext.texteditorbase.GenericSourceViewerConfiguration;
import org.eclipse.escet.setext.texteditorbase.scanners.FormatStringScanner;
import org.eclipse.escet.setext.texteditorbase.scanners.SingleStyleScanner;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;

/** Chi text editor source viewer configuration. */
public class ChiSourceViewerConfig extends GenericSourceViewerConfiguration {
    @Override
    protected void addDamagersRepairers(PresentationReconciler reconciler) {
        // DEFAULT.
        ITokenScanner scanner = new ChiTextScanner(colorManager);
        addDamagerRepairer(reconciler, scanner, DEFAULT_CONTENT_TYPE);

        // COMMENT_SL.
        ITokenScanner commentSlScanner = new SingleStyleScanner(ChiStyles.COMMENT_SL.createToken(colorManager));
        addDamagerRepairer(reconciler, commentSlScanner, "__chi_comment_sl");

        // STRING (string literals, paths, format patterns, etc).
        ITokenScanner stringScanner = new FormatStringScanner(ChiStyles.STRING.createToken(colorManager),
                ChiStyles.STRING_ESCAPE.createToken(colorManager), ChiStyles.STRING_ESCAPE.createToken(colorManager));
        addDamagerRepairer(reconciler, stringScanner, "__chi_string");
    }
}
