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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.cif.datasynth.options.BddHyperEdgeAlgoOption.BddHyperEdgeAlgo;
import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** BDD hyper-edge creation algorithm option. */
public class BddHyperEdgeAlgoOption extends EnumOption<BddHyperEdgeAlgo> {
    /** Constructor for the {@link BddHyperEdgeAlgoOption} class. */
    public BddHyperEdgeAlgoOption() {
        super(
                // name
                "BDD hyper-edge creation algorithm",

                // description
                "The algorithm to use to create hyper-edges for BDD variable ordering algorithms. "
                        + "Specify \"legacy\" (default) to use the legacy hyper-edge creation algorithm, "
                        + "or \"linearized\" to use the linearized hyper-edge creation algorithm.",

                // cmdShort
                null,

                // cmdLong
                "hyper-edge-algo",

                // cmdValue
                "ALGO",

                // defaultValue
                BddHyperEdgeAlgo.LEGACY,

                // showInDialog
                true,

                // optDialogDescr
                "The algorithm to use to create hyper-edges for BDD variable ordering algorithms.");
    }

    @Override
    protected String getDialogText(BddHyperEdgeAlgo algo) {
        switch (algo) {
            case LEGACY:
                return "Legacy hyper-edge creation algorithm";
            case LINEARIZED:
                return "Linearized hyper-edge creation algorithm";
        }
        throw new RuntimeException("Unknown algorithm: " + algo);
    }

    /**
     * Returns the BDD hyper-edge creation algorithm.
     *
     * @return The BDD hyper-edge creation algorithm.
     */
    public static BddHyperEdgeAlgo getAlgo() {
        return Options.get(BddHyperEdgeAlgoOption.class);
    }

    /** BDD hyper-edge creation algorithm. */
    public static enum BddHyperEdgeAlgo {
        /** Use legacy hyper-edge creator. */
        LEGACY,

        /** Use linearized hyper-edge creator. */
        LINEARIZED;
    }
}
