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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.escet.common.java.Lists;

/** Attribute feature data to pretty print. */
public class PrettyFeatEAttribute extends PrettyEFeat {
    /** Attribute description. */
    public final EAttribute attribute;

    /**
     * Constructor of the {@link PrettyFeatEAttribute} class.
     *
     * @param attribute Attribute description.
     * @param data Value or values of the feature. May be {@code null}, and may not adhere to feature constraints.
     */
    public PrettyFeatEAttribute(EAttribute attribute, Object data) {
        super(attribute, data);
        this.attribute = attribute;
    }

    @Override
    public void resolveId(int idNumber, int index) {
        // Nothing to do.
    }

    @Override
    public List<String> getValues() {
        if (data == null) {
            return List.of("null");
        }
        if (isMany()) {
            @SuppressWarnings("unchecked")
            List<Object> values = (List<Object>)data;
            return values.stream().map(v -> v.toString()).collect(Lists.toList());
        } else {
            return List.of(data.toString());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isEmpty() {
        return data == null || (isMany() && ((List<Object>)data).isEmpty());
    }
}
