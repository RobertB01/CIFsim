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

package org.eclipse.escet.cif.texteditor;

import org.eclipse.escet.setext.texteditorbase.Style;
import org.eclipse.swt.SWT;

/** Styles for the CIF text editor. */
@SuppressWarnings("javadoc")
public interface CifStyles {
    Style DEFAULT = new Style(64, 32, 32);

    Style IDENTIFIER = new Style(0, 0, 0);

    Style COMMENT_SL = new Style(128, 128, 128, SWT.ITALIC);

    Style COMMENT_ML = new Style(128, 128, 128, SWT.ITALIC);

    Style STRING = new Style(192, 0, 0);

    Style STRING_ESCAPE = new Style(255, 128, 0);

    Style KEYWORD = new Style(0, 0, 255);

    Style STDLIBFUNC = new Style(128, 0, 255);

    Style OPERATOR = new Style(0, 97, 192);

    Style NUMBER = new Style(0, 97, 0);

    Style C_EVENT = new Style(0, 97, 0);

    Style U_EVENT = new Style(128, 0, 0);

    Style E_EVENT = new Style(192, 97, 0);
}
