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

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.escet.common.java.Lists;

/** Reference feature data to pretty print. */
public class PrettyFeatEReference extends PrettyEFeat {
    /** Feature description. */
    public final EReference eReference;

    /** Id numbers of the referenced objects. A negative value means the number is not known yet. */
    public final int[] idNumbers;

    /**
     * Constructor of the {@link PrettyFeatEReference} class.
     *
     * @param eReference Feature description.
     * @param data Value or values of the feature. May be {@code null}, and may not adhere to feature constraints.
     * @param idNumbers Id numbers of the referenced objects. A negative value means the number is not known yet.
     */
    public PrettyFeatEReference(EReference eReference, Object data, int[] idNumbers) {
        super(eReference, data);
        this.eReference = eReference;
        this.idNumbers = idNumbers;
    }

    @Override
    public void resolveId(int idNumber, int index) {
        idNumbers[index] = idNumber;
    }

    @Override
    public List<String> getValues() {
        if (data == null) {
            return List.of("null");
        }
        return Arrays.stream(idNumbers).mapToObj(id -> (id < 0) ? "?" : "<<" + id + ">>").collect(Lists.toList());
    }

    @Override
    public boolean isEmpty() {
        return data == null || idNumbers.length == 0;
    }
}
