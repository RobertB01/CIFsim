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

package org.eclipse.escet.cif.codegen.updates.tree;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;

/** Tuple projection at a left hand side. */
public class LhsTupleProjection extends LhsProjection {
    /** Type of the surrounding tuple. */
    public final TupleType tupleType;

    /** Number of the indexed field in the tuple. */
    public final int fieldNumber;

    /**
     * Constructor of the {@link LhsTupleProjection} class.
     *
     * @param tupleType Type of the surrounding container.
     * @param index Index expression into the tuple.
     */
    public LhsTupleProjection(TupleType tupleType, Expression index) {
        this.tupleType = tupleType;
        fieldNumber = getTupleFieldIndex(tupleType.getFields(), index);
    }

    @Override
    public CifType getContainerType() {
        return tupleType;
    }

    @Override
    public CifType getPartType() {
        return tupleType.getFields().get(fieldNumber).getType();
    }

    /**
     * Get the name of the selected field in the tuple projection.
     *
     * @return The name of the selected field.
     */
    public String getSelectedFieldName() {
        return tupleType.getFields().get(fieldNumber).getName();
    }

    /**
     * Retrieve the field index number from a tuple projection.
     *
     * @param tupleFields Fields of the tuple type.
     * @param indexExpr Index expression into the tuple.
     * @return Index of the field in the tuple type.
     */
    private static int getTupleFieldIndex(List<Field> tupleFields, Expression indexExpr) {
        if (indexExpr instanceof FieldExpression) {
            FieldExpression fieldExpr = (FieldExpression)indexExpr;
            Field field = fieldExpr.getField();
            return tupleFields.indexOf(field);
        } else {
            // Get field index for tuple index projection. Index is valid: type
            // checker already checked it.
            try {
                return (Integer)CifEvalUtils.eval(indexExpr, false);
            } catch (CifEvalException e) {
                // Should never fail: type checker already evaluated this.
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String toString() {
        return fmt("TupleProject(field#%d)", fieldNumber);
    }
}
