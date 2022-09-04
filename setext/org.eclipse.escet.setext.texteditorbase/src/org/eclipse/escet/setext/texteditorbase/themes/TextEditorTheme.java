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

package org.eclipse.escet.setext.texteditorbase.themes;

import org.eclipse.escet.setext.texteditorbase.Style;

/**
 * A text editor theme.
 *
 * @param <T> The enum with the named styles of a text editor.
 */
public interface TextEditorTheme<T> {
    /**
     * Returns the themed style for a given named style.
     *
     * @param namedStyle The named style.
     * @return The themed style.
     */
    public Style getStyle(T namedStyle);
}
