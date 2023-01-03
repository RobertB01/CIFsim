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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;

/** In-place transformation that replaces tuple field projections by tuple index projections. */
public class ElimTupleFieldProjs extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    /**
     * Performs the in-place transformation.
     *
     * @param expr The CIF expression for which to perform the transformation. The expression is modified in-place. The
     *     expression itself may also be replaced as a whole.
     */
    public void transform(Expression expr) {
        walkExpression(expr);
    }

    @Override
    protected void walkFieldExpression(FieldExpression expr) {
        // Get index of field in tuple type.
        Field field = expr.getField();
        TupleType ttype = (TupleType)field.eContainer();
        int idx = ttype.getFields().indexOf(field);
        Assert.check(idx >= 0);

        // Create index expression.
        IntType itype = newIntType();
        itype.setLower(idx);
        itype.setUpper(idx);

        IntExpression iexpr = newIntExpression();
        iexpr.setValue(idx);
        iexpr.setType(itype);

        // Replace tuple field projection by tuple index projection.
        EMFHelper.updateParentContainment(expr, iexpr);
    }
}
