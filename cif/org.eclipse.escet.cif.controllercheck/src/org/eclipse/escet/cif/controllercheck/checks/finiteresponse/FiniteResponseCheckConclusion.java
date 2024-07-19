//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.checks.finiteresponse;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;

import java.util.List;

import org.eclipse.escet.cif.controllercheck.checks.CheckConclusion;
import org.eclipse.escet.cif.controllercheck.options.PrintControlLoopsOutputOption;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

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
    public boolean hasDetails() {
        return !propertyHolds();
    }

    @Override
    public void printResult(DebugNormalOutput out, WarnOutput warn) {
        if (propertyHolds()) {
            out.line("[OK] The specification has finite response.");
        } else {
            out.line("[ERROR] The specification may NOT have finite response:");
            out.line();

            out.inc();
            out.line("At least one controllable-event loop was found.");
            if (PrintControlLoopsOutputOption.isPrintControlLoopsEnabled()) {
                out.line("The following events might still occur in a controllable-event loop:");
                out.inc();
                for (Event event: unprovenEvents) {
                    out.line("- %s", getAbsName(event));
                }
                out.dec();
            }
            out.dec();
        }
    }
}
