//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;

import org.eclipse.escet.common.java.PermuteUtils;

/** Implementation of the sliding window algorithm for automatic variable ordering. */
public class SlidingWindowVarOrderer extends AutoVarOrderer {
    /** The maximum length of the window. */
    public final int maxLen;

    /**
     * Constructor for the {@link SlidingWindowVarOrderer} class.
     *
     * @param maxLen The maximum length of the window.
     */
    public SlidingWindowVarOrderer(int maxLen) {
        this.maxLen = maxLen;
    }

    @Override
    protected void initializeAlgo() {
        // Nothing to do.
    }

    @Override
    protected void cleanupAlgo() {
        // Nothing to do.
    }

    @Override
    protected void computeOrder() {
        // Debug output: starting.
        if (dbgEnabled) {
            dbg();
            dbg("  Applying sliding window algorithm:");
        }

        // Determine window length.
        int length = Math.min(maxLen, varCnt);
        if (dbgEnabled) {
            dbg("    Window length: %,d", length);
        }

        // Initialize total span.
        long bestSpan = computeTotalSpan(bestIndices);
        if (dbgEnabled) {
            dbg();
            dbg("    Total span: %,20d (total) %,20.2f (avg/edge) [before]", bestSpan, (double)bestSpan / edges.length);
        }

        // Compute for all windows.
        int[] window = new int[length];
        int permCnt = PermuteUtils.factorial(window.length);
        int[][] windowPerms = new int[permCnt][window.length];
        for (int offset = 0; offset <= (varCnt - length); offset++) {
            // Fill window and all permutations.
            System.arraycopy(bestIndices, offset, window, 0, length);
            PermuteUtils.permute(window, windowPerms);

            // Compute total span for each order.
            int bestIdx = -1;
            int[] order = bestIndices.clone();
            for (int i = 0; i < windowPerms.length; i++) {
                int[] windowPerm = windowPerms[i];
                System.arraycopy(windowPerm, 0, order, offset, length);
                long span = computeTotalSpan(order);
                if (span < bestSpan) {
                    bestSpan = span;
                    bestIdx = i;
                }
            }

            // Update best order if improved by this window.
            if (bestIdx >= 0) {
                System.arraycopy(windowPerms[bestIdx], 0, bestIndices, offset, length);

                if (dbgEnabled) {
                    dbg("    Total span: %,20d (total) %,20.2f (avg/edge) [window %d..%d]", bestSpan,
                            (double)bestSpan / edges.length, offset, offset + length - 1);
                }
            }
        }

        // Debug output for result.
        if (dbgEnabled) {
            dbg("    Total span: %,20d (total) %,20.2f (avg/edge) [after]", bestSpan, (double)bestSpan / edges.length);
        }
    }
}
