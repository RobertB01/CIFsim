//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

/** Chi text editor stylable. */
public enum ChiTextEditorStylable {
    /** Default. */
    DEFAULT,

    /** Identifier. */
    IDENTIFIER,

    /** Single-line comment. */
    COMMENT_SL,

    /** String literal. */
    STRING,

    /** String literal escape sequence. */
    STRING_ESCAPE,

    /** Keyword. */
    KEYWORD,

    /** Standard library function. */
    STDLIBFUNC,

    /** Operator. */
    OPERATOR,

    /** Number literal. */
    NUMBER,
}
