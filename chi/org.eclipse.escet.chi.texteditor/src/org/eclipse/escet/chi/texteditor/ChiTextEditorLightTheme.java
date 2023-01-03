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

package org.eclipse.escet.chi.texteditor;

import org.eclipse.escet.setext.texteditorbase.Style;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.swt.SWT;

/** Chi text editor light theme. */
public class ChiTextEditorLightTheme implements TextEditorTheme<ChiTextEditorStylable> {
    @Override
    public Style getStyle(ChiTextEditorStylable stylable) {
        switch (stylable) {
            case DEFAULT:
                return new Style(0, 0, 0);
            case IDENTIFIER:
                return new Style(0, 0, 0);
            case COMMENT_SL:
                return new Style(128, 128, 128, SWT.ITALIC);
            case STRING:
                return new Style(192, 0, 0);
            case STRING_ESCAPE:
                return new Style(255, 128, 0);
            case KEYWORD:
                return new Style(0, 0, 255);
            case STDLIBFUNC:
                return new Style(128, 0, 255);
            case OPERATOR:
                return new Style(0, 97, 192);
            case NUMBER:
                return new Style(0, 97, 0);
        }
        throw new AssertionError();
    }
}
