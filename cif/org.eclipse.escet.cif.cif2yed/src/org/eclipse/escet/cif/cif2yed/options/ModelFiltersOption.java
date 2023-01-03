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

package org.eclipse.escet.cif.cif2yed.options;

import java.util.EnumSet;

import org.eclipse.escet.common.app.framework.options.EnumSetOption;
import org.eclipse.escet.common.app.framework.options.Options;

/** Model filters option. */
public class ModelFiltersOption extends EnumSetOption<ModelFilter> {
    /** Constructor for the {@link ModelFiltersOption} class. */
    public ModelFiltersOption() {
        super(
                // name
                "Model filters",

                // description
                "The kinds of features to include in the diagram. Only applies to diagrams of the model. "
                        + "Specify comma separated feature names. Specify \"io\" to include the I/O declarations, "
                        + "\"decls\" (default) to include the declarations of the components, "
                        + "\"loc-decls\" (default) to include the declarations of the locations, "
                        + "\"guards\" (default) to include the guards of the edges, and/or "
                        + "\"updates\" (default) to include the updates of the edges. "
                        + "Prefix a name with \"+\" to add it on top of the defaults, "
                        + "or with \"-\" to remove it from the defaults.",

                // cmdShort
                'f',

                // cmdLong
                "model-filters",

                // cmdValue
                "FILTERS",

                // defaultValue
                getDefaults(),

                // showInDialog
                true,

                // optDialogDescr
                "The kinds of features to include in the diagram. Only applies to diagrams of the model.",

                // enumClass
                ModelFilter.class);
    }

    /**
     * Returns the default model filters, the model features to include in diagrams of the model.
     *
     * @return The default model filters.
     */
    private static EnumSet<ModelFilter> getDefaults() {
        return EnumSet.of(ModelFilter.DECLS, ModelFilter.LOC_DECLS, ModelFilter.GUARDS, ModelFilter.UPDATES);
    }

    @Override
    protected String getDialogText(ModelFilter filter) {
        switch (filter) {
            case IO:
                return "The I/O declarations";
            case DECLS:
                return "The declarations of the components";
            case LOC_DECLS:
                return "The declarations of the locations";
            case GUARDS:
                return "The guards of the edges";
            case UPDATES:
                return "The updates of the edges";
        }
        throw new RuntimeException("Unknown model filter: " + filter);
    }

    /**
     * Returns the model filters, the model features to include in diagrams of the model.
     *
     * @return The model filters.
     */
    public static EnumSet<ModelFilter> getFilters() {
        return Options.get(ModelFiltersOption.class);
    }
}
