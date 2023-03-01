//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.listc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.common.java.Assert;

/** Expression referencing a variable, includes optional projections on the variable. */
public class PlcVarExpression extends PlcExpression {
    /** Referenced variable. */
    public final PlcVariable variable;

    /** Projections to select a part of the variable. */
    public final List<PlcProjection> projections;

    /**
     * Constructor of the {@link PlcVarExpression} class.
     *
     * @param variable Referenced variable.
     * @param projections Projections to select a part of the variable.
     */
    public PlcVarExpression(PlcVariable variable, PlcProjection... projections) {
        this.variable = variable;
        this.projections = Arrays.asList(projections);
    }

    /**
     * Constructor of the {@link PlcVarExpression} class.
     *
     * @param variable Referenced variable.
     * @param projections Projections to select a part of the variable.
     */
    public PlcVarExpression(PlcVariable variable, List<PlcProjection> projections) {
        this.variable = variable;
        this.projections = projections;
    }

    @Override
    public PlcVarExpression copy() {
        List<PlcProjection> clonedProjs = listc(projections.size());
        projections.stream().map(p -> p.copy()).collect(Collectors.toCollection(() -> clonedProjs));

        return new PlcVarExpression(variable, clonedProjs);
    }

    /** Projection in the value of the referenced variable. */
    public abstract static class PlcProjection {
        /**
         * Make an independent copy of this projection.
         *
         * @return The copy of the projection.
         */
        public abstract PlcProjection copy();
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
        public PlcStructProjection copy() {
            return new PlcStructProjection(fieldName);
        }
    }

    /** Projection in an array of the value of the referenced variable. */
    public static class PlcArrayProjection extends PlcProjection {
        /** Element indices in the array to select, must always select one element in the array. */
        public final List<PlcExpression> indexExpressions;

        /**
         * Constructor of the {@link PlcArrayProjection} class.
         *
         * @param indexExpression Element index in the one dimensional array to select.
         */
        public PlcArrayProjection(PlcExpression indexExpression) {
            this(List.of(indexExpression));
        }

        /**
         * Constructor of the {@link PlcArrayProjection} class.
         *
         * @param indexExpressions Element indices in the array to select, must always select one element in the array.
         */
        public PlcArrayProjection(List<PlcExpression> indexExpressions) {
            Assert.check(!indexExpressions.isEmpty());
            this.indexExpressions = indexExpressions;
        }

        @Override
        public PlcArrayProjection copy() {
            List<PlcExpression> clonedindexExprs = listc(indexExpressions.size());
            indexExpressions.stream().map(ie -> ie.copy()).collect(Collectors.toCollection(() -> clonedindexExprs));

            return new PlcArrayProjection(clonedindexExprs);
        }
    }
}
