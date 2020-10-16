//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

/** Parsing table accept action. */
public class ParserAcceptAction extends ParserAction {
    @Override
    public int getType() {
        return 2;
    }

    @Override
    public int hashCode() {
        return ParserAcceptAction.class.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ParserAcceptAction)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "accept";
    }
}
