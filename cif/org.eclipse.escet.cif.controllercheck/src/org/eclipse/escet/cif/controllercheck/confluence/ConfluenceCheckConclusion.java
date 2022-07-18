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
    private final List<Pair<String, String>> failedPairs;

    /**
     * Constructor of the {@link ConfluenceCheckConclusion} class.
     *
     * @param failedPairs Pairs of events that failed all checks.
     */
    public ConfluenceCheckConclusion(List<Pair<String, String>> failedPairs) {
        this.failedPairs = failedPairs;
    }

    @Override
    public boolean propertyHolds() {
        return failedPairs.isEmpty();
    }

    @Override
    public void printDetails() {
        if (failedPairs.isEmpty()) {
            out("The specification has confluence.");
        } else {
            out("ERROR, the specification may NOT have confluence.");
            out();

            String pairText = (failedPairs.size() == 1) ? "pair" : "pairs";
            out("The following event %s failed all tests:", pairText);
            iout();
            out(failedPairs.stream().map(p -> "(" + p.left + ", " + p.right + ")").collect(Collectors.joining(", ")));
            dout();
        }
    }
}
