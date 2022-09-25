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

package org.eclipse.escet.cif.texteditor;

/** CIF text editor stylable. */
public enum CifTextEditorStylable {
    /** Default. */
    DEFAULT,

    /** Identifier. */
    IDENTIFIER,

    /** Single-line comment. */
    COMMENT_SL,

    /** Multi-line comment. */
    COMMENT_ML,

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

    /** Controllable event name. */
    C_EVENT,

    /** Uncontrollable event name. */
    U_EVENT,

    /** Event name. */
    E_EVENT,
}
