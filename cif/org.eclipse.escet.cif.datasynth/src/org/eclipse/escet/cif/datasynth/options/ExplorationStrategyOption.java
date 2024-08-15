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

package org.eclipse.escet.cif.datasynth.options;

import org.eclipse.escet.cif.bdd.settings.CifBddSettingsDefaults;
import org.eclipse.escet.cif.bdd.settings.ExplorationStrategy;
import org.eclipse.escet.common.app.framework.options.EnumOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Option to specify the exploration strategy for symbolic reachability computations. */
public class ExplorationStrategyOption extends EnumOption<ExplorationStrategy> {
    /** Constructor for the {@link ExplorationStrategyOption} class. */
    public ExplorationStrategyOption() {
        super(
                // name
                "Exploration strategy",

                // description
                "Specify the exploration strategy to be used for symbolic reachability computations. "
                        + "Specify \"chaining-fixed\" to use the chaining strategy with a fixed edge ordering. "
                        + "Specify \"chaining-workset\" to use the chaining strategy where the edge workset algorithm "
                        + "is used to dynamically choose the best edge to apply during reachability computations. "
                        + "Specify \"saturation\" (default) to use the saturation strategy.",

                // cmdShort
                null,

                // cmdLong
                "exploration-strategy",

                // cmdValue
                "STRATEGY",

                // defaultValue
                CifBddSettingsDefaults.EXPLORATION_STRATEGY_DEFAULT,

                // showInDialog
                true,

                // optDialogDescr
                "Specify the exploration strategy to be used for symbolic reachability computations.");
    }

    @Override
    protected String getDialogText(ExplorationStrategy strategy) {
        switch (strategy) {
            case CHAINING_FIXED:
                return "The chaining strategy with a fixed edge ordering";
            case CHAINING_WORKSET:
                return "The chaining strategy with a dynamic edge ordering determined by the edge workset algorithm";
            case SATURATION:
                return "The saturation strategy";
        }
        throw new RuntimeException("Unknown strategy: " + strategy);
    }

    /**
     * Returns the value of the {@link ExplorationStrategyOption} option.
     *
     * @return The value of the {@link ExplorationStrategyOption} option.
     */
    public static ExplorationStrategy getStrategy() {
        return Options.get(ExplorationStrategyOption.class);
    }
}
