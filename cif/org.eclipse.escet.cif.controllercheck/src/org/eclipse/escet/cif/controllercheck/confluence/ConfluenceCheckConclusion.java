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

package org.eclipse.escet.cif.controllercheck.confluence;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.iout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.controllercheck.CheckConclusion;
import org.eclipse.escet.common.java.Pair;

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
    public void printDetails() {
        if (cannotProvePairs.isEmpty()) {
            out("[OK] The specification has confluence.");
        } else {
            out("[ERROR] The specification may NOT have confluence.");
            out();

            iout();
            String pairText = (cannotProvePairs.size() == 1) ? "pair" : "pairs";
            out("Confluence of the following event %s could not be decided:", pairText);
            iout();
            out(cannotProvePairs.stream().map(Pair::toString).collect(Collectors.joining(", ")));
            dout();
            dout();
        }
    }
}
