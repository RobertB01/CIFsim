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

package org.eclipse.escet.setext.texteditor;

import org.eclipse.escet.setext.texteditorbase.Style;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.swt.SWT;

/** SeText text editor dark theme. */
public class SeTextTextEditorDarkTheme implements TextEditorTheme<SeTextTextEditorStylable> {
    @Override
    public Style getStyle(SeTextTextEditorStylable stylable) {
        switch (stylable) {
            case DEFAULT:
                return new Style(240, 240, 240);
            case IDENTIFIER:
                return new Style(240, 240, 240);
            case DESCRIPTION:
                return new Style(64, 210, 210);
            case COMMENT_SL:
                return new Style(150, 150, 150, SWT.ITALIC);
            case COMMENT_ML:
                return new Style(150, 150, 150, SWT.ITALIC);
            case STRING:
                return new Style(235, 64, 64);
            case KEYWORD:
                return new Style(0, 164, 255);
        }
        throw new AssertionError();
    }
}
