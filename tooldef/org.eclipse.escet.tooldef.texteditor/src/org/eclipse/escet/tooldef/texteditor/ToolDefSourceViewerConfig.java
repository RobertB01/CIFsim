//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.setext.texteditorbase.GenericSourceViewerConfiguration;
import org.eclipse.escet.setext.texteditorbase.scanners.FormatStringScanner;
import org.eclipse.escet.setext.texteditorbase.scanners.SingleStyleScanner;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.ITokenScanner;

/** ToolDef text editor source viewer configuration. */
public class ToolDefSourceViewerConfig extends GenericSourceViewerConfiguration {
    @Override
    protected void addDamagersRepairers(PresentationReconciler reconciler) {
        // DEFAULT.
        ITokenScanner scanner = new ToolDefTextEditorScanner(colorManager);
        addDamagerRepairer(reconciler, scanner, DEFAULT_CONTENT_TYPE);

        // COMMENT_ML.
        ITokenScanner commentMlScanner = new SingleStyleScanner(ToolDefStyles.COMMENT_ML.createToken(colorManager));
        addDamagerRepairer(reconciler, commentMlScanner, "__tooldef_comment_ml");

        // COMMENT_SL.
        ITokenScanner commentSlScanner = new SingleStyleScanner(ToolDefStyles.COMMENT_SL.createToken(colorManager));
        addDamagerRepairer(reconciler, commentSlScanner, "__tooldef_comment_sl");

        // STRING (string literals, paths, format patterns, etc).
        ITokenScanner stringScanner = new FormatStringScanner(ToolDefStyles.STRING.createToken(colorManager),
                ToolDefStyles.STRING_ESCAPE.createToken(colorManager),
                ToolDefStyles.STRING_ESCAPE.createToken(colorManager));
        addDamagerRepairer(reconciler, stringScanner, "__tooldef_string");
    }
}
