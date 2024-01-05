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

package org.eclipse.escet.chi.codegen.statements.seq;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.MultiLineTextBox;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** 'Native' code statement in sequential language. */
public class SeqCode extends Seq {
    /** Lines of code. */
    public List<String> lines;

    /**
     * Sequence of (java code lines).
     *
     * @param lines Code lines in the native implementation language. Copied verbatim into the output.
     * @param chiObj Originating statement in the Chi source code.
     */
    public SeqCode(List<String> lines, PositionObject chiObj) {
        super(chiObj);
        this.lines = (lines == null) ? list() : lines;
    }

    @Override
    public Box boxify() {
        return new MultiLineTextBox(lines);
    }
}
