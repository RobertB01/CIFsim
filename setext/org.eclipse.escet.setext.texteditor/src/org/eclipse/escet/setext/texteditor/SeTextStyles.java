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

import org.eclipse.escet.setext.texteditorbase.Style;
import org.eclipse.swt.SWT;

/** Styles for the SeText text editor. */
@SuppressWarnings("javadoc")
public interface SeTextStyles {
    Style DEFAULT = new Style(64, 32, 32);

    Style IDENTIFIER = new Style(0, 0, 0);

    Style DESCRIPTION = new Style(0, 97, 192);

    Style COMMENT_SL = new Style(128, 128, 128, SWT.ITALIC);

    Style COMMENT_ML = new Style(128, 128, 128, SWT.ITALIC);

    Style STRING = new Style(192, 0, 0);

    Style KEYWORD = new Style(0, 0, 255);
}
