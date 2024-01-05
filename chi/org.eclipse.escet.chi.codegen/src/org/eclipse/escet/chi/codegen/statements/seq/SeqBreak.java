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

import org.eclipse.escet.chi.metamodel.chi.BreakStatement;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.java.Assert;

/** Break statement in sequential language. */
public class SeqBreak extends Seq {
    /** Destination to jump to (if not {@code null}). */
    Integer destination;

    /**
     * Constructor of the {@link SeqBreak} class.
     *
     * @param stat Break statement represented by the object.
     */
    public SeqBreak(BreakStatement stat) {
        super(stat);
        destination = null;
    }

    /**
     * Does the break have a jump destination?
     *
     * @return The statement will jump to a destination instead of performing a Java 'break'.
     */
    public boolean hasDestination() {
        return destination != null;
    }

    /**
     * Set the jump destination of the continue statement.
     *
     * @param dest Destination to jump to.
     */
    public void setDestination(Integer dest) {
        // Only allow setting the destination one time.
        Assert.check(destination == null);
        destination = dest;
    }

    @Override
    public Box boxify() {
        if (destination == null) {
            return new TextBox("if (ALWAYS) break;");
        }
        return SeqReturn.generateGoto(destination);
    }
}
