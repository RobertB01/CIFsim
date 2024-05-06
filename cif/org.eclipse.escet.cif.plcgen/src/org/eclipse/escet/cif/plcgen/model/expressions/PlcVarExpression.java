//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.expressions;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructField;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;

/** Expression referencing a variable, includes optional projections on the variable. */
public class PlcVarExpression extends PlcExpression {
    /** Referenced variable. */
    public final PlcBasicVariable variable;

    /** Projections to select a part of the variable. */
    public final List<PlcProjection> projections;

    /**
     * Constructor of the {@link PlcVarExpression} class.
     *
     * @param variable Referenced variable.
     * @param projections Projections to select a part of the variable.
     */
    public PlcVarExpression(PlcBasicVariable variable, PlcProjection... projections) {
        this(variable, Arrays.asList(projections));
    }

    /**
     * Constructor of the {@link PlcVarExpression} class.
     *
     * @param variable Referenced variable.
     * @param projections Projections to select a part of the variable.
     */
    public PlcVarExpression(PlcBasicVariable variable, List<PlcProjection> projections) {
        super(deriveType(variable.type, projections));
        this.variable = variable;
        this.projections = Collections.unmodifiableList(projections);
    }

    /**
     * Compute the type after applying all projections.
     *
     * @param resultType Type after applying 0 projections.
     * @param projections Projections to apply.
     * @return The type after applying all projections.
     */
    private static PlcType deriveType(PlcType resultType, List<PlcProjection> projections) {
        for (PlcProjection proj: projections) {
            if (resultType == null) { // TODO Remove this after expressions always have a type.
                return resultType;
            }
            resultType = proj.getProjectedType(resultType);
        }
        return resultType;
    }

    /** Projection in the value of the referenced variable. */
    public abstract static class PlcProjection {
        /**
         * Compute the type after applying the projection.
         *
         * @param unprojectedType Type before applying projection.
         * @return The type after applying the pojection.
         */
        protected abstract PlcType getProjectedType(PlcType unprojectedType);
    }

    /** Projection in a structure of the value of the referenced variable. */
    public static class PlcStructProjection extends PlcProjection {
        /** Name of the field in the structure to select. */
        public final String fieldName;

        /**
         * Constructor of the {@link PlcStructProjection} class.
         *
         * @param fieldName Name of the field in the structure to select.
         */
        public PlcStructProjection(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public PlcType getProjectedType(PlcType unprojectedType) {
            if (unprojectedType instanceof PlcStructType structType) {
                for (PlcStructField field: structType.fields) {
                    if (field.fieldName.equals(this.fieldName)) {
                        return field.type;
                    }
                }
                throw new AssertionError(fmt("Struct type \"%s\" has no field \"%s\".", structType.typeName, fieldName));
            }
            throw new AssertionError("Cannot compute projection on non-struct type \"" + unprojectedType + "\".");
        }
    }

    /** Projection in an array of the value of the referenced variable. */
    public static class PlcArrayProjection extends PlcProjection {
        /** Element indices in the array to select, must always select one element in the array. */
        public final PlcExpression indexExpression;

        /**
         * Constructor of the {@link PlcArrayProjection} class.
         *
         * @param indexExpression Element index in the one dimensional array to select.
         */
        public PlcArrayProjection(PlcExpression indexExpression) {
            this.indexExpression = indexExpression;
        }

        @Override
        public PlcType getProjectedType(PlcType unprojectedType) {
            if (unprojectedType instanceof PlcArrayType arayType) {
                return arayType.elemType;
            }
            throw new AssertionError("Cannot compute projection on non-array type \"" + unprojectedType + "\".");
        }
    }
}
