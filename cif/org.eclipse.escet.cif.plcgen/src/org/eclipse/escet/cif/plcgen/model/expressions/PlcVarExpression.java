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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
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
        this(variable, Arrays.asList(projections));
    }

    /**
     * Constructor of the {@link PlcVarExpression} class.
     *
     * @param variable Referenced variable.
     * @param projections Projections to select a part of the variable.
     */
    public PlcVarExpression(PlcVariable variable, List<PlcProjection> projections) {
        this.variable = variable;
        this.projections = Collections.unmodifiableList(projections);
    }

    /** Projection in the value of the referenced variable. */
    public abstract static class PlcProjection {
        // Nothing to do.
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
            this.indexExpressions = Collections.unmodifiableList(indexExpressions);
        }
    }
}
