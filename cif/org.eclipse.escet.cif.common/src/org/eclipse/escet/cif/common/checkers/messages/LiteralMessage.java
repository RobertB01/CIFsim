//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.messages;

import org.eclipse.escet.cif.common.checkers.CifCheckViolation;

/** A literal message. */
public class LiteralMessage extends CifCheckViolationMessage {
    /** The message. */
    private final String message;

    /**
     * Constructor for the {@link LiteralMessage} class.
     *
     * @param message The message.
     */
    public LiteralMessage(String message) {
        this.message = message;
    }

    @Override
    public String getMessage(CifCheckViolation violation) {
        return message;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LiteralMessage)) {
            return false;
        }
        LiteralMessage that = (LiteralMessage)other;
        return this.message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }
}
