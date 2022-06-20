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

package org.eclipse.escet.cif.datasynth.varorder;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererHelper;
import org.eclipse.escet.common.java.PermuteUtils;

/** Sliding window algorithm variable ordering heuristic. */
public class SlidingWindowVarOrderer implements VarOrderer {
    /** The maximum length of the window. */
    private final int maxLen;

    /**
     * Constructor for the {@link SlidingWindowVarOrderer} class.
     *
     * @param maxLen The maximum length of the window.
     */
    public SlidingWindowVarOrderer(int maxLen) {
        this.maxLen = maxLen;
    }

    @Override
    public List<SynthesisVariable> order(VarOrdererHelper helper, List<SynthesisVariable> inputOrder,
            boolean dbgEnabled, int dbgLevel)
    {
        // Get variable count.
        int varCnt = inputOrder.size();

        // Debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying sliding window algorithm.");
        }

        // Determine window length.
        int length = Math.min(maxLen, varCnt);
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Window length: %,d", length);
        }

        // Initialize current indices and total span.
        int[] curIndices = helper.getNewIndicesForVarOrder(inputOrder);
        long curSpan = helper.computeTotalSpanForNewIndices(curIndices);
        if (dbgEnabled) {
            helper.dbgTotalSpan(dbgLevel, curSpan, "before");
        }

        // Process all windows.
        int[] window = new int[length];
        int permCnt = PermuteUtils.factorial(window.length);
        int[][] windowPerms = new int[permCnt][window.length];
        for (int offset = 0; offset <= (varCnt - length); offset++) {
            // Fill window and all permutations.
            System.arraycopy(curIndices, offset, window, 0, length);
            PermuteUtils.permute(window, windowPerms);

            // Compute total span for each order.
            int bestIdx = -1;
            int[] windowIndices = curIndices.clone();
            for (int i = 0; i < windowPerms.length; i++) {
                int[] windowPerm = windowPerms[i];
                System.arraycopy(windowPerm, 0, windowIndices, offset, length);
                long windowSpan = helper.computeTotalSpanForNewIndices(windowIndices);
                if (windowSpan < curSpan) { // Check for better order (with lower total span).
                    curSpan = windowSpan;
                    bestIdx = i;
                }
            }

            // Update order if improved by this window (has lower total span).
            if (bestIdx >= 0) {
                System.arraycopy(windowPerms[bestIdx], 0, curIndices, offset, length);

                if (dbgEnabled) {
                    helper.dbgTotalSpan(dbgLevel, curSpan, fmt("window %d..%d", offset, offset + length - 1));
                }
            }
        }

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbgTotalSpan(dbgLevel, curSpan, "after");
        }

        // Return the resulting order.
        return helper.reorderForNewIndices(curIndices);
    }
}
