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

import java.util.List;

import org.eclipse.escet.cif.common.checkers.CifCheckViolation;
import org.eclipse.escet.common.java.Assert;

/** A message that is a concatenation of a non-empty sequence of messages. */
public class SequenceMessage extends CifCheckViolationMessage {
    /** The non-empty sequence of messages. */
    private final List<CifCheckViolationMessage> messages;

    /**
     * Constructor for the {@link SequenceMessage} class.
     *
     * @param messages The non-empty sequence of messages. The messages are trimmed and concatenated, with a space being
     *     added in between each two messages if needed.
     */
    public SequenceMessage(List<CifCheckViolationMessage> messages) {
        Assert.check(!messages.isEmpty());
        this.messages = messages;
    }

    @Override
    public String getMessage(CifCheckViolation violation) {
        StringBuilder text = new StringBuilder();
        for (CifCheckViolationMessage message: messages) {
            String messageText = message.getMessage(violation);
            messageText = messageText.trim();
            if (!messageText.isEmpty()) { // Only add if not empty.
                if (text.length() > 0) { // Add separator if there is any text already present.
                    text.append(" ");
                }
                text.append(messageText);
            }
        }
        return text.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SequenceMessage)) {
            return false;
        }
        SequenceMessage that = (SequenceMessage)other;
        return this.messages.equals(that.messages);
    }

    @Override
    public int hashCode() {
        return messages.hashCode();
    }
}
