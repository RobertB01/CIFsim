//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescriptionMessage;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** CIF check that does not allow channels. */
public class EventNoChannelsCheck extends CifCheck {
    @Override
    protected void preprocessEvent(Event event, CifCheckViolations violations) {
        if (event.getType() != null) {
            violations.add(event, new ReportObjectTypeDescriptionMessage(),
                    new LiteralMessage("is a channel (has a data type)"));
        }
    }
}
