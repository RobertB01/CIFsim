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

package org.eclipse.escet.setext.generator.parser;

/** Parsing table shift action. */
public class ParserShiftAction extends ParserAction {
    /** The target state of the shift action. */
    public final LALR1AutomatonState target;

    /**
     * Constructor for the {@link ParserShiftAction} class.
     *
     * @param target The target state of the shift action.
     */
    public ParserShiftAction(LALR1AutomatonState target) {
        this.target = target;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int hashCode() {
        return ParserShiftAction.class.hashCode() ^ target.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ParserShiftAction)) {
            return false;
        }
        ParserShiftAction other = (ParserShiftAction)obj;
        return target.id == other.target.id;
    }

    @Override
    public String toString() {
        return "shift to state " + target.id;
    }
}
