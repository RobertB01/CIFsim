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

package org.eclipse.escet.cif.controllercheck.multivaluetrees;

import org.eclipse.escet.common.multivaluetrees.Tree;

/**
 * Helper class for constructing a specification in multi-value diagrams.
 *
 * <p>
 * As only one automaton is supported in the specification currently, no need to have an automaton concept in the
 * builder or the specification.
 * </p>
 */
public class MvSpecBuilder {
    /** Storage of the relation between nodes in the tree and CIF variables. */
    public final CifVarInfoBuilder cifVarInfoBuilder;

    /** Storage and manipulation of multi-value trees. */
    public final Tree tree;

    /** Variable use-kind for reading a variable ('old' value). */
    public final int readUseKind;

    /** Variable use-kind for writing a variable ('new' value). */
    public final int writeUseKind;

    /** Expression converter. Uses 'readIndex' to access variables. */
    private ConvertExpression expressionConvertor = null;

    /**
     * Constructor of the {@link MvSpecBuilder} class.
     *
     * @param cifVarInfoBuilder Storage of the relation between nodes in the tree and CIF variables.
     * @param readUseKind Variable use-kind for the current value of a variable.
     * @param writeUseKind Variable use kind for the next value of a variable.
     */
    public MvSpecBuilder(CifVarInfoBuilder cifVarInfoBuilder, int readUseKind, int writeUseKind) {
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
    public ConvertExpression getExpressionConvertor() {
        if (expressionConvertor == null) {
            expressionConvertor = new ConvertExpression(cifVarInfoBuilder, tree, readUseKind, writeUseKind);
        }
        return expressionConvertor;
    }
}
