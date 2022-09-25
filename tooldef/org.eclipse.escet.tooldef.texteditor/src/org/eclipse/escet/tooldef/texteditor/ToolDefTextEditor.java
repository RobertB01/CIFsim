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

package org.eclipse.escet.tooldef.texteditor;

import org.eclipse.escet.setext.texteditorbase.GenericTextEditor;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.parser.ToolDefParser;
import org.eclipse.escet.tooldef.typechecker.ToolDefTypeChecker;

/** ToolDef text editor for Eclipse. */
public class ToolDefTextEditor extends GenericTextEditor<Script, Script, ToolDefTextEditorStylable> {
    /** Constructor for the {@link ToolDefTextEditor} class. */
    public ToolDefTextEditor() {
        super(new ToolDefPartitionScanner(), theme -> new ToolDefSourceViewerConfig(theme),
                new ToolDefTextEditorDarkTheme(), new ToolDefTextEditorLightTheme(), ToolDefParser.class,
                ToolDefTypeChecker.class, "org.eclipse.escet.tooldef.texteditor.ToolDefSyntaxProblem",
                "org.eclipse.escet.tooldef.texteditor.ToolDefSemanticProblem", "//");
    }
}
