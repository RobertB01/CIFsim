//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.analysis;

import java.util.List;

/** Storage of state information, also used in the synthesis dump file IO. */
public class StateInfo {
    /** Source location combination represented by {@link #targetState}. */
    public final int[] srcLocs;

    /** Number of the target state. */
    public final int targetState;

    /** Whether the target state is marked. */
    public final boolean marked;

    /**
     * Created outgoing edges from this location. By default {@code null} to reduce the number of uselessly created
     * objects.
     *
     * <p>
     * Note that created edges may have been deleted later in the synthesis process.
     * </p>
     */
    public List<EdgeInfo> outEdges = null;

    /**
     * Constructor of the {@link StateInfo} class.
     *
     * @param srcLocs Source location combination represented by {@link #targetState}.
     * @param targetState Number of the target state.
     * @param marked Whether the target state is marked.
     */
    public StateInfo(int[] srcLocs, int targetState, boolean marked) {
        this.srcLocs = srcLocs;
        this.targetState = targetState;
        this.marked = marked;
    }

    /**
     * Return whether the target state is the initial location.
     *
     * @return Whether the target state is the initial location.
     */
    public boolean isInitial() {
        for (int loc: srcLocs) {
            if (loc != 0) {
                return false;
            }
        }
        return true;
    }
}
