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

package org.eclipse.escet.common.emf.prettyprint;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

/** Base class of a pretty feature. */
public abstract class PrettyEFeat {
    /** Feature Feature description. */
    private final EStructuralFeature eFeature;

    /**
     * Value or values of the feature. May be {@code null}, and may not adhere to constraints of the feature (for
     * example be {@code null} when the feature is required).
     */
    public final Object data;

    /**
     * Constructor of the {@link PrettyEFeat} class.
     *
     * @param eFeature Feature description.
     * @param data Value or values of the feature. May be {@code null}, and may not adhere to feature constraints.
     */
    public PrettyEFeat(EStructuralFeature eFeature, Object data) {
        this.eFeature = eFeature;
        this.data = data;
    }

    /**
     * Whether the value should be considered multi-valued.
     *
     * @return Whether the value should be considered multi-valued.
     */
    public boolean isMany() {
        return eFeature.isMany();
    }

    /**
     * Retrieve the name of the type of the data.
     *
     * @return Type of the data of the feature.
     */
    public String getEType() {
        return eFeature.getEType().getName();
    }

    /**
     * Get the name of the feature.
     *
     * @return Name of the feature.
     */
    public String getFeatureName() {
        return eFeature.getName();
    }

    /**
     * Callback that a requested number has been assigned.
     *
     * @param idNumber Id number that was assigned.
     * @param index Index of the feature value that needs the id number.
     */
    public abstract void resolveId(int idNumber, int index);

    /**
     * Provide a textual description of the value.
     *
     * @return {@code List.of("null")} if there is no value, else a text for each object in the value.
     */
    public abstract List<String> getValues();
}
