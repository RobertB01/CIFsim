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

package org.eclipse.escet.chi.texteditor;

import org.eclipse.escet.setext.texteditorbase.Style;
import org.eclipse.swt.SWT;

/** Styles for the Chi text editor. */
public interface ChiStyles {
    /** Default style. */
    Style DEFAULT = new Style(64, 32, 32);

    /** Identifiers style. */
    Style IDENTIFIER = new Style(0, 0, 0);

    /** Single line comment style. */
    Style COMMENT_SL = new Style(128, 128, 128, SWT.ITALIC);

    /** String literal style. */
    Style STRING = new Style(192, 0, 0);

    /** String literal escape style. */
    Style STRING_ESCAPE = new Style(255, 128, 0);

    /** Keyword style. */
    Style KEYWORD = new Style(0, 0, 255);

    /** Standard library function keyword style. */
    Style STDLIBFUNC = new Style(128, 0, 255);

    /** Operator style. */
    Style OPERATOR = new Style(0, 97, 192);

    /** Number literal style. */
    Style NUMBER = new Style(0, 97, 0);
}
