//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import java.util.Comparator;

/** Comparator for comparing two arrays of locations. */
public class LocationArrayComparator implements Comparator<int[]> {
    /** Singleton instance of the class. */
    public static final LocationArrayComparator INSTANCE = new LocationArrayComparator();

    /** Constructor of the {@link LocationArrayComparator} class. */
    private LocationArrayComparator() {
        // Nothing to do.
    }

    @Override
    public int compare(int[] srcLocs1, int[] srcLocs2) {
        for (int i = 0; i < srcLocs1.length; i++) {
            if (srcLocs1[i] != srcLocs2[i]) {
                if (srcLocs1[i] < srcLocs2[i]) {
                    return -1;
                }
                return 1;
            }
        }
        return 0;
    }
}
