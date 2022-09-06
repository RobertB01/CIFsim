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
     * @param message The literal message to use if the violation is reported on an ancestor of the object.
     */
    public IfReportOnAncestorMessage(String message) {
        this(new LiteralMessage(message));
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
    public String getMessage(CifCheckViolation violation) {
        return violation.isReportOnSelf() ? "" : message.getMessage(violation);
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
