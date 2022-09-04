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

/** CIF text editor style names. */
public enum CifTextEditorStyleNames {
    /** Default style. */
    DEFAULT,

    /** Identifier style. */
    IDENTIFIER,

    /** Single-line comment style. */
    COMMENT_SL,

    /** Multi-line comment style. */
    COMMENT_ML,

    /** String literal style. */
    STRING,

    /** String literal escape sequence style. */
    STRING_ESCAPE,

    /** Keyword style. */
    KEYWORD,

    /** Standard library function style. */
    STDLIBFUNC,

    /** Operator style. */
    OPERATOR,

    /** Number literal style. */
    NUMBER,

    /** Controllable event name style. */
    C_EVENT,

    /** Uncontrollable event name style. */
    U_EVENT,

    /** Event name style. */
    E_EVENT,
}
