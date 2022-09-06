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
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.checkers.CifCheckViolation;
import org.eclipse.escet.common.java.Assert;

/** A message that is a concatenation of a non-empty sequence of messages. */
public class SequenceMessage extends CifCheckViolationMessage {
    /** The non-empty sequence of messages. */
    private final List<CifCheckViolationMessage> messages;

    /**
     * Constructor for the {@link SequenceMessage} class.
     *
     * @param messages The non-empty sequence of messages.
     */
    public SequenceMessage(List<CifCheckViolationMessage> messages) {
        Assert.check(!messages.isEmpty());
        this.messages = messages;
    }

    @Override
    public String getMessage(CifCheckViolation violation) {
        return messages.stream().map(m -> m.getMessage(violation)).collect(Collectors.joining());
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
