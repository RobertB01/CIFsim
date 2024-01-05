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

package org.eclipse.escet.cif.codegen.assignments;

import org.eclipse.escet.cif.codegen.typeinfos.ContainerTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;

/** Projection operation on a variable or projected variable. */
public class AddressableProjection {
    /** Type information of the container performing the projection operation. */
    public final ContainerTypeInfo parentTI;

    /**
     * Index expression of the projection, may be {@code null}. In the latter case, {@link #indexNumber} contains the
     * index position.
     */
    public final Expression indexExpression;

    /** Index number of the projection. */
    public final int indexNumber;

    /**
     * Constructor of the {@link AddressableProjection} class.
     *
     * @param parentTI Type information of the container performing the projection operation.
     * @param indexExpression Index expression of the projection, should not be {@code null}.
     */
    public AddressableProjection(ContainerTypeInfo parentTI, Expression indexExpression) {
        Assert.notNull(indexExpression);
        this.parentTI = parentTI;
        this.indexExpression = indexExpression;
        this.indexNumber = -1;
    }

    /**
     * Constructor of the {@link AddressableProjection} class.
     *
     * @param parentTI Type information of the container performing the projection operation.
     * @param indexNumber Statically determined index value.
     */
    public AddressableProjection(ContainerTypeInfo parentTI, int indexNumber) {
        this.parentTI = parentTI;
        this.indexExpression = null;

        int normalizedIndex = parentTI.normalizeIndex(indexNumber);
        this.indexNumber = (normalizedIndex >= 0) ? normalizedIndex : indexNumber;
    }
}
