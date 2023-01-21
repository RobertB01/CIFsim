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

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.checkers.CifCheckViolation;

/**
 * Message describing the {@link CifCheckViolation#getReportObject report object}, the CIF object on which the violation
 * is reported.
 *
 * <p>
 * If the message is reported on the specification, {@code "the top level scope of the specification"} is used as
 * description. Otherwise, {@link CifTextUtils#getTypeDescriptionForNamedObject} is called on the report object to
 * obtain the description.
 * </p>
 */
public class ReportObjectTypeDescrMessage extends CifCheckViolationMessage {
    @Override
    public String getMessageText(CifCheckViolation violation) {
        return violation.isReportOnSpecification() ? "the top level scope of the specification"
                : CifTextUtils.getTypeDescriptionForNamedObject(violation.getReportObject());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        return other instanceof SequenceMessage;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
