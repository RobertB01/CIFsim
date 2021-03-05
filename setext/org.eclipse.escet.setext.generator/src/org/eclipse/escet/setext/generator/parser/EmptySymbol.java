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

package org.eclipse.escet.setext.generator.parser;

import org.eclipse.escet.setext.parser.ast.Symbol;

/** Symbol representing &#x03F5; (empty). */
public class EmptySymbol extends Symbol {
    /** Singleton instance of the {@link EmptySymbol} class. */
    public static final EmptySymbol EMPTY_SYMBOL = new EmptySymbol();

    /** Constructor for the {@link EmptySymbol} class. */
    private EmptySymbol() {
        super("\u03F5", null);
    }
}
