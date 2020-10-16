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

package org.eclipse.escet.cif.texteditor;

import org.eclipse.escet.setext.texteditorbase.GenericSourceViewerConfiguration;
import org.eclipse.escet.setext.texteditorbase.scanners.FormatStringScanner;
import org.eclipse.escet.setext.texteditorbase.scanners.SingleStyleScanner;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;

/** CIF text editor source viewer configuration. */
public class CifSourceViewerConfig extends GenericSourceViewerConfiguration {
    @Override
    protected void addDamagersRepairers(PresentationReconciler reconciler) {
        // DEFAULT.
        ITokenScanner scanner = new CifTextEditorScanner(colorManager);
        addDamagerRepairer(reconciler, scanner, DEFAULT_CONTENT_TYPE);

        // COMMENT_ML.
        ITokenScanner commentMlScanner = new SingleStyleScanner(CifStyles.COMMENT_ML.createToken(colorManager));
        addDamagerRepairer(reconciler, commentMlScanner, "__cif_comment_ml");

        // COMMENT_SL.
        ITokenScanner commentSlScanner = new SingleStyleScanner(CifStyles.COMMENT_SL.createToken(colorManager));
        addDamagerRepairer(reconciler, commentSlScanner, "__cif_comment_sl");

        // STRING (string literals, paths, format patterns, etc).
        ITokenScanner stringScanner = new FormatStringScanner(CifStyles.STRING.createToken(colorManager),
                CifStyles.STRING_ESCAPE.createToken(colorManager), CifStyles.STRING_ESCAPE.createToken(colorManager));
        addDamagerRepairer(reconciler, stringScanner, "__cif_string");
    }
}
