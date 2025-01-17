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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.cif.bdd.settings.BddHyperEdgeAlgo;
import org.eclipse.escet.cif.bdd.settings.CifBddSettingsDefaults;
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
                "The algorithm to use to create hyper-edges for BDD variable ordering. "
                        + "Specify \"legacy\" to use the legacy hyper-edge creation algorithm, "
                        + "\"linearized\" to use the linearized hyper-edge creation algorithm, or "
                        + "\"default\" to use the linearized hyper-edge creation algorithm for the FORCE and sliding "
                        + "window algorithms, and the legacy hyper-edge creation algorithm for all other variable "
                        + "orderers. By default, \"default\" is used.",

                // cmdShort
                null,

                // cmdLong
                "hyper-edge-algo",

                // cmdValue
                "ALGO",

                // defaultValue
                CifBddSettingsDefaults.HYPER_EDGE_ALGO_DEFAULT,

                // showInDialog
                true,

                // optDialogDescr
                "The algorithm to use to create hyper-edges for BDD variable ordering.");
    }

    @Override
    protected String getDialogText(BddHyperEdgeAlgo algo) {
        switch (algo) {
            case LEGACY:
                return "Legacy hyper-edge creation algorithm";
            case LINEARIZED:
                return "Linearized hyper-edge creation algorithm";
            case DEFAULT:
                return "Default (linearized for FORCE and sliding window, legacy otherwise)";
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
}
