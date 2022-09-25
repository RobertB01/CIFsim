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
import org.eclipse.escet.common.java.Strings;

/**
 * Message that is conditional on the violation being reported on an ancestor of the object, rather than on the object
 * itself. Is an empty message if the violation is reported on the object itself.
 */
public class IfReportOnAncestorMessage extends CifCheckViolationMessage {
    /** The message to use if the violation is reported on an ancestor of the object. */
    private final CifCheckViolationMessage message;

    /**
     * Constructor for the {@link IfReportOnAncestorMessage} class.
     *
     * @param messagePattern The message text format pattern, to use if the violation is reported on an ancestor of the
     *     object.
     * @param args The message text format arguments.
     * @see Strings#fmt
     */
    public IfReportOnAncestorMessage(String messagePattern, Object... args) {
        this(new LiteralMessage(messagePattern, args));
    }

    /**
     * Constructor for the {@link IfReportOnAncestorMessage} class.
     *
     * @param message The message to use if the violation is reported on an ancestor of the object.
     */
    public IfReportOnAncestorMessage(CifCheckViolationMessage message) {
        this.message = message;
    }

    @Override
    public String getMessageText(CifCheckViolation violation) {
        return violation.isReportOnSelf() ? "" : message.getMessageText(violation);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof IfReportOnAncestorMessage)) {
            return false;
        }
        IfReportOnAncestorMessage that = (IfReportOnAncestorMessage)other;
        return this.message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }
}
