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

import org.eclipse.escet.setext.texteditorbase.GenericSourceViewerConfiguration;
import org.eclipse.escet.setext.texteditorbase.scanners.SingleStyleScanner;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;

/** SeText text editor source viewer configuration. */
public class SeTextSourceViewerConfig extends GenericSourceViewerConfiguration {
    @Override
    protected void addDamagersRepairers(PresentationReconciler reconciler) {
        // DEFAULT.
        ITokenScanner scanner = new SeTextTextEditorScanner(colorManager);
        addDamagerRepairer(reconciler, scanner, DEFAULT_CONTENT_TYPE);

        // COMMENT_ML.
        ITokenScanner commentMlScanner = new SingleStyleScanner(SeTextStyles.COMMENT_ML.createToken(colorManager));
        addDamagerRepairer(reconciler, commentMlScanner, "__setext_comment_ml");

        // COMMENT_SL.
        ITokenScanner commentSlScanner = new SingleStyleScanner(SeTextStyles.COMMENT_SL.createToken(colorManager));
        addDamagerRepairer(reconciler, commentSlScanner, "__setext_comment_sl");

        // STRING.
        ITokenScanner stringScanner = new SingleStyleScanner(SeTextStyles.STRING.createToken(colorManager));
        addDamagerRepairer(reconciler, stringScanner, "__setext_string");
    }
}
