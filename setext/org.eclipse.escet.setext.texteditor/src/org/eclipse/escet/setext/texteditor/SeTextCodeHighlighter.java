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

import org.eclipse.escet.setext.texteditorbase.highlight.CodeHighlighter;
import org.eclipse.escet.setext.texteditorbase.scanners.GenericPartitionScanner;
import org.eclipse.jface.text.presentation.IPresentationReconciler;

/** SeText code highlighter. */
public class SeTextCodeHighlighter extends CodeHighlighter {
    @Override
    protected IPresentationReconciler obtainPresentationReconciler() {
        SeTextSourceViewerConfig config = new SeTextSourceViewerConfig();
        config.setColorManager(colorManager);
        return config.getPresentationReconciler(null);
    }

    @Override
    protected GenericPartitionScanner obtainPartitionScanner() {
        return new SeTextPartitionScanner();
    }
}
