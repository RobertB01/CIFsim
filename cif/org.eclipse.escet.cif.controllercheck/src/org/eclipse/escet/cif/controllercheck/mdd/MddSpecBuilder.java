//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import org.eclipse.escet.common.multivaluetrees.Tree;

/**
 * Helper class for representing a CIF specification using MDDs.
 *
 * <p>
 * As only one automaton is supported in the specification currently, no need to have an automaton concept in the
 * builder or the specification.
 * </p>
 */
public class MddSpecBuilder {
    /** Storage of the relation between nodes in the MDD tree and CIF variables. */
    public final MddCifVarInfoBuilder cifVarInfoBuilder;

    /** Storage and manipulation of MDDs. */
    public final Tree tree;

    /** Variable use-kind for reading a variable ('old' value). */
    public final int readUseKind;

    /** Variable use-kind for writing a variable ('new' value). */
    public final int writeUseKind;

    /** Expression converter. Uses 'readIndex' to access variables. */
    private MddConvertExpression expressionConvertor = null;

    /**
     * Constructor of the {@link MddSpecBuilder} class.
     *
     * @param cifVarInfoBuilder Storage of the relation between nodes in the MDD tree and CIF variables.
     * @param readUseKind Variable use-kind for the current value of a variable.
     * @param writeUseKind Variable use kind for the next value of a variable.
     */
    public MddSpecBuilder(MddCifVarInfoBuilder cifVarInfoBuilder, int readUseKind, int writeUseKind) {
        this.cifVarInfoBuilder = cifVarInfoBuilder;
        this.tree = new Tree();
        this.readUseKind = readUseKind;
        this.writeUseKind = writeUseKind;
    }

    /**
     * Get the expression converter.
     *
     * @return Converter for converting expressions.
     */
    public MddConvertExpression getExpressionConvertor() {
        if (expressionConvertor == null) {
            expressionConvertor = new MddConvertExpression(cifVarInfoBuilder, tree, readUseKind, writeUseKind);
        }
        return expressionConvertor;
    }
}
