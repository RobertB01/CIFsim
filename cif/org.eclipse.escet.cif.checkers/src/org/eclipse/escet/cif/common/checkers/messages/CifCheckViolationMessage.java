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

import org.eclipse.escet.cif.common.checkers.CifCheckViolation;

/** A CIF check violation message. */
public abstract class CifCheckViolationMessage {
    /**
     * Returns the violation message text.
     *
     * @param violation The CIF check violation.
     * @return The violation message text.
     */
    public abstract String getMessageText(CifCheckViolation violation);

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();
}
