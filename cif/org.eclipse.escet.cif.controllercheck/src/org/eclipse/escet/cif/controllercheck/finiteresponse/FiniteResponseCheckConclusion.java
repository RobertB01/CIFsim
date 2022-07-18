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

package org.eclipse.escet.cif.controllercheck.finiteresponse;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.iout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;

import java.util.List;

import org.eclipse.escet.cif.controllercheck.CheckConclusion;
import org.eclipse.escet.cif.controllercheck.options.PrintControlLoopsOutputOption;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

/** Conclusion of the finite response check. */
public class FiniteResponseCheckConclusion implements CheckConclusion {
    /** Events that may be in a controllable-event loop. */
    private final List<Event> unprovenEvents;

    /**
     * Constructor of the {@link FiniteResponseCheckConclusion} class.
     *
     * @param orderedEvents Events that may be in a controllable-event loop.
     */
    public FiniteResponseCheckConclusion(List<Event> orderedEvents) {
        this.unprovenEvents = orderedEvents;
    }

    @Override
    public boolean propertyHolds() {
        return unprovenEvents.isEmpty();
    }

    @Override
    public void printDetails() {
        if (!unprovenEvents.isEmpty()) {
            out("ERROR, the specification may NOT have finite response.");
            out();
            out("At least one controllable-event loop was found.");
            if (PrintControlLoopsOutputOption.isPrintControlLoopsEnabled()) {
                out("The following events might still occur in a controllable-event loop:");
                iout();
                for (Event event: unprovenEvents) {
                    out("- %s", getAbsName(event));
                }
                dout();
            }
        } else {
            out("The specification has finite response.");
        }
    }
}
