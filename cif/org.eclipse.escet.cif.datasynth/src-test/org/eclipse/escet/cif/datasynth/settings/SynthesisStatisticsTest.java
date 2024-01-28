//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Set;

import org.eclipse.escet.cif.bdd.settings.CifBddStatistics;
import org.eclipse.escet.common.java.Sets;
import org.junit.jupiter.api.Test;

/** {@link SynthesisStatistics} test. */
public class SynthesisStatisticsTest {
    /** Test that the {@link SynthesisStatistics} are complete with respect to the {@link CifBddStatistics}. */
    @Test
    public void testCompleteWrtCifBddStatistics() {
        Set<String> synthNames = Arrays.stream(SynthesisStatistics.values()).map(s -> s.name()).collect(Sets.toSet());
        Set<String> cifBddNames = Arrays.stream(CifBddStatistics.values()).map(s -> s.name()).collect(Sets.toSet());
        Set<String> commonNames = Sets.intersection(synthNames, cifBddNames);
        assertEquals(CifBddStatistics.values().length, commonNames.size());
    }
}
