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

package org.eclipse.escet.cif.datasynth.varorder;

import static org.eclipse.escet.common.java.Lists.list;

/**
 * DSM-based Cuthill-McKee/Sloan variable ordering Heuristic (DCSH).
 *
 * <p>
 * This algorithm is based on: Sam Lousberg, Sander Thuijsman and Michel Reniers, "DSM-based variable ordering heuristic
 * for reduced computational effort of symbolic supervisor synthesis", IFAC-PapersOnLine, volume 53, issue 4, pages
 * 429-436, 2020, doi:<a href="https://doi.org/10.1016/j.ifacol.2021.04.058">10.1016/j.ifacol.2021.04.058</a>.
 * </p>
 */
public class DcshVarOrderer extends ChoiceVarOrderer {
    /** Constructor for the {@link DcshVarOrderer} class. */
    public DcshVarOrderer() {
        super("DCSH", list( //
                new WeightedCuthillMcKeeVarOrderer(), // First algorithm.
                new SloanVarOrderer(), // Second algorithm.
                new ReverseVarOrderer(new WeightedCuthillMcKeeVarOrderer()), // Reverse first algorithm.
                new ReverseVarOrderer(new SloanVarOrderer()) // Reverse second algorithm.
        ));
    }
}
