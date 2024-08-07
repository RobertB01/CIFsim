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

package org.eclipse.escet.cif.controllercheck.checks.confluence;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.controllercheck.checks.CheckConclusion;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** Conclusion of the confluence check. */
public class ConfluenceCheckConclusion implements CheckConclusion {
    /** Pairs of events that could not be proven to be confluent. */
    private final List<Pair<String, String>> cannotProvePairs;

    /**
     * Constructor of the {@link ConfluenceCheckConclusion} class.
     *
     * @param cannotProvePairs Pairs of events where confluence could not be proven.
     */
    public ConfluenceCheckConclusion(List<Pair<String, String>> cannotProvePairs) {
        this.cannotProvePairs = cannotProvePairs;
    }

    @Override
    public boolean propertyHolds() {
        return cannotProvePairs.isEmpty();
    }

    @Override
    public boolean hasDetails() {
        return !propertyHolds();
    }

    @Override
    public void printResult(DebugNormalOutput out, WarnOutput warn) {
        if (propertyHolds()) {
            out.line("[OK] The specification has confluence.");
        } else {
            out.line("[ERROR] The specification may NOT have confluence:");
            out.line();

            out.inc();
            String pairText = (cannotProvePairs.size() == 1) ? "pair" : "pairs";
            out.line("Confluence of the following event %s could not be decided:", pairText);
            out.inc();
            out.line(cannotProvePairs.stream().map(Pair::toString).collect(Collectors.joining(", ")));
            out.dec();
            out.dec();
        }
    }
}
