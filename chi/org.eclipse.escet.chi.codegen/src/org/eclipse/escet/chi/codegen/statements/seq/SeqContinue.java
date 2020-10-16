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

package org.eclipse.escet.chi.codegen.statements.seq;

import org.eclipse.escet.chi.metamodel.chi.ContinueStatement;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.TextBox;
import org.eclipse.escet.common.java.Assert;

/** Continue statement in sequential language. */
public class SeqContinue extends Seq {
    /** Destination to jump to (if not {@code null}). */
    private Integer destination;

    /**
     * Constructor of the {@link SeqContinue} class.
     *
     * @param stat Continue statement represented by the object.
     */
    public SeqContinue(ContinueStatement stat) {
        super(stat);
        destination = null;
    }

    /**
     * Does the continue have a jump destination?
     *
     * @return The statement will jump to a destination instead of performing a Java 'continue'.
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
            return new TextBox("if (ALWAYS) continue;");
        }
        return SeqReturn.generateGoto(destination);
    }
}
