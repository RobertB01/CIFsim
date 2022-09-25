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

import org.eclipse.escet.setext.parser.SeTextParser;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.texteditorbase.GenericTextEditor;
import org.eclipse.escet.setext.typechecker.SeTextTypeChecker;

/** SeText text editor for Eclipse. */
public class SeTextTextEditor extends GenericTextEditor<Specification, Specification, SeTextTextEditorStylable> {
    /** Constructor for the {@link SeTextTextEditor} class. */
    public SeTextTextEditor() {
        super(new SeTextPartitionScanner(), theme -> new SeTextSourceViewerConfig(theme),
                new SeTextTextEditorDarkTheme(), new SeTextTextEditorLightTheme(), SeTextParser.class,
                SeTextTypeChecker.class, "org.eclipse.escet.setext.texteditor.SeTextSyntaxProblem",
                "org.eclipse.escet.setext.texteditor.SeTextSemanticProblem", "//");
    }
}
