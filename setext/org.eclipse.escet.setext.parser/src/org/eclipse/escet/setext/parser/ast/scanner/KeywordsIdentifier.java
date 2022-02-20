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

package org.eclipse.escet.setext.parser.ast.scanner;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.Identifier;
import org.eclipse.escet.setext.parser.ast.SeTextObject;
import org.eclipse.escet.setext.parser.ast.TerminalDescription;

/**
 * A keyword identifier with optional post-processing function, as part of a {@link KeywordsTerminal}. This class is no
 * longer used after type checking.
 */
public class KeywordsIdentifier extends SeTextObject {
    /** The keyword. */
    public final Identifier keyword;

    /**
     * The name of the Java method to invoke for post-processing on tokens for this terminal, or {@code null} if not
     * available or not applicable.
     */
    public final Identifier func;

    /** The description of the terminal in end user readable text, or {@code null} if not available. */
    public final TerminalDescription description;

    /**
     * Constructor for the {@link KeywordsIdentifier} class.
     *
     * @param keyword The keyword.
     * @param func The name of the Java method to invoke for post-processing on tokens for this terminal, or
     *     {@code null} if not available or not applicable.
     * @param description The description of the terminal in end user readable text, or {@code null} if not available.
     * @param position Position information.
     */
    public KeywordsIdentifier(Identifier keyword, Identifier func, TerminalDescription description, Position position) {
        super(position);
        this.keyword = keyword;
        this.func = func;
        this.description = description;
    }
}
