//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.Declaration;
import org.eclipse.escet.chi.metamodel.chi.Specification;
import org.eclipse.escet.chi.parser.ChiParser;
import org.eclipse.escet.chi.typecheck.ChiTypeChecker;
import org.eclipse.escet.setext.texteditorbase.GenericTextEditor;

/** Chi text editor for Eclipse. */
public class ChiTextEditor extends GenericTextEditor<List<Declaration>, Specification, ChiTextEditorStylable> {
    /** Constructor for the {@link ChiTextEditor} class. */
    public ChiTextEditor() {
        super(new ChiPartitionScanner(), theme -> new ChiSourceViewerConfig(theme), new ChiTextEditorDarkTheme(),
                new ChiTextEditorLightTheme(), ChiParser.class, ChiTypeChecker.class,
                "org.eclipse.escet.chi.texteditor.ChiSyntaxError", "org.eclipse.escet.chi.texteditor.ChiSemanticError",
                "#");
    }
}
