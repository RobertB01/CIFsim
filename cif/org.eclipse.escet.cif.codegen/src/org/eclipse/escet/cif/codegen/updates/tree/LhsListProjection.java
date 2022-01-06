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

package org.eclipse.escet.cif.codegen.updates.tree;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;

/** Left hand side projection in a list. */
public class LhsListProjection extends LhsProjection {
    /** Type of the surrounding list container. */
    public final ListType listType;

    /** Index expression into the list. */
    public final Expression index;

    /**
     * Constructor of the {@link LhsListProjection} class.
     *
     * @param listType Type of the surrounding list container.
     * @param index Index expression into the list.
     */
    public LhsListProjection(ListType listType, Expression index) {
        this.listType = listType;
        this.index = index;
    }

    @Override
    public CifType getContainerType() {
        return listType;
    }

    @Override
    public CifType getPartType() {
        return listType.getElementType();
    }

    @Override
    public String toString() {
        return "ListProject(" + exprToStr(index) + ")";
    }
}
