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

package org.eclipse.escet.chi.codegen;

import java.util.List;

import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Position code storage and generation in the output. */
public class OutputPosition {
    /** (Optional) position information. */
    public final PositionObject position;

    /**
     * Constructor.
     *
     * @param posobj Chi statement being translated (used for deriving position information).
     */
    public OutputPosition(PositionObject posobj) {
        position = posobj;
    }

    /**
     * Generate a statement to set the current position in the running process. If no position information exists,
     * nothing happens.
     *
     * @param box Output stream to write into.
     */
    public void setCurrentPositionStatement(VBox box) {
        String txt = genCurrentPositionStatement();
        if (txt == null) {
            return;
        }
        box.add(txt);
    }

    /**
     * Generate a statement to set the current position in the running process. If no position information exists,
     * nothing happens.
     *
     * @param box Output stream to write into.
     */
    public void setCurrentPositionStatement(CodeBox box) {
        String txt = genCurrentPositionStatement();
        if (txt == null) {
            return;
        }
        box.add(txt);
    }

    /**
     * Generate a statement to set the current position in the running process. If no position information exists,
     * nothing happens.
     *
     * @param lines Output stream to write into.
     */
    public void setCurrentPositionStatement(List<String> lines) {
        String txt = genCurrentPositionStatement();
        if (txt == null) {
            return;
        }
        lines.add(txt);
    }

    /**
     * Construct a Java statement that sets the current source position, if position information is available.
     *
     * @return The generated statement, or {@code null}.
     */
    public String genCurrentPositionStatement() {
        return genCurrentPositionStatement(position);
    }

    /**
     * Construct a Java statement that sets the current source position, if position information is available.
     *
     * @param posobj Chi object (used for deriving position information).
     * @return The generated statement, or {@code null}.
     */
    public static String genCurrentPositionStatement(PositionObject posobj) {
        if (posobj == null) {
            return null;
        }
        return "position.statementPos = " + String.valueOf(posobj.getPosition().getStartLine()) + ";";
    }
}
