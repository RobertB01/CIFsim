//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.setext.texteditorbase.Style;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.swt.SWT;

/** ToolDef text editor dark theme. */
public class ToolDefTextEditorDarkTheme implements TextEditorTheme<ToolDefTextEditorStylable> {
    @Override
    public Style getStyle(ToolDefTextEditorStylable stylable) {
        switch (stylable) {
            case DEFAULT:
                return new Style(240, 240, 240);
            case IDENTIFIER:
                return new Style(240, 240, 240);
            case COMMENT_SL:
                return new Style(150, 150, 150, SWT.ITALIC);
            case COMMENT_ML:
                return new Style(150, 150, 150, SWT.ITALIC);
            case STRING:
                return new Style(235, 64, 64);
            case STRING_ESCAPE:
                return new Style(235, 150, 40);
            case KEYWORD:
                return new Style(0, 164, 255);
            case BUILTIN:
                return new Style(210, 110, 255);
            case OPERATOR:
                return new Style(64, 210, 210);
            case NUMBER:
                return new Style(24, 225, 24);
        }
        throw new AssertionError();
    }
}
