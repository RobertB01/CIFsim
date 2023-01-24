//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.checkers.CifCheckViolation;
import org.eclipse.escet.common.java.Strings;

/** A literal message. */
public class LiteralMessage extends CifCheckViolationMessage {
    /** The message text. */
    private final String message;

    /**
     * Constructor for the {@link LiteralMessage} class.
     *
     * @param messagePattern The message text format pattern.
     * @param args The message text format arguments.
     * @see Strings#fmt
     */
    public LiteralMessage(String messagePattern, Object... args) {
        this.message = fmt(messagePattern, args);
    }

    @Override
    public String getMessageText(CifCheckViolation violation) {
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
