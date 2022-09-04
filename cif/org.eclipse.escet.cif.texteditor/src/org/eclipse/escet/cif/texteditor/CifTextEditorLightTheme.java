//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.setext.texteditorbase.Style;
import org.eclipse.escet.setext.texteditorbase.themes.TextEditorTheme;
import org.eclipse.swt.SWT;

/** CIF text editor light theme. */
public class CifTextEditorLightTheme implements TextEditorTheme<CifTextEditorStyleNames> {
    @Override
    public Style getStyle(CifTextEditorStyleNames namedStyle) {
        switch (namedStyle) {
            case DEFAULT:
                return new Style(0, 0, 0);
            case IDENTIFIER:
                return new Style(0, 0, 0);
            case COMMENT_SL:
                return new Style(128, 128, 128, SWT.ITALIC);
            case COMMENT_ML:
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
            case C_EVENT:
                return new Style(0, 97, 0);
            case U_EVENT:
                return new Style(128, 0, 0);
            case E_EVENT:
                return new Style(192, 97, 0);
        }
        throw new AssertionError();
    }
}
