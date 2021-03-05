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

package org.eclipse.escet.setext.runtime;

/** Base interface for all parser call back hooks interfaces. */
public interface ParserHooksBase {
    /**
     * Provides the call backs hooks class with the parser that owns it.
     *
     * @param parser The parser that owns the call back hooks class.
     */
    public void setParser(Parser<?> parser);
}
