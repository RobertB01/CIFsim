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
 * Message that is conditional on the violation being reported on the object itself, rather than on one of its
 * ancestors. Is an empty message if the violation is reported on one of the ancestors of the object.
 */
public class IfReportOnSelfMessage extends CifCheckViolationMessage {
    /** The message to use if the violation is reported on the object itself. */
    private final CifCheckViolationMessage message;

    /**
     * Constructor for the {@link IfReportOnSelfMessage} class.
     *
     * @param messagePattern The message format pattern, to use if the violation is reported on the object itself.
     * @param args The message format arguments.
     * @see Strings#fmt
     */
    public IfReportOnSelfMessage(String messagePattern, Object... args) {
        this(new LiteralMessage(messagePattern, args));
    }

    /**
     * Constructor for the {@link IfReportOnSelfMessage} class.
     *
     * @param message The message to use if the violation is reported on the object itself.
     */
    public IfReportOnSelfMessage(CifCheckViolationMessage message) {
        this.message = message;
    }

    @Override
    public String getMessage(CifCheckViolation violation) {
        return violation.isReportOnSelf() ? message.getMessage(violation) : "";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof IfReportOnSelfMessage)) {
            return false;
        }
        IfReportOnSelfMessage that = (IfReportOnSelfMessage)other;
        return this.message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }
}
