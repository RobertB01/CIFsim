//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

    /** Variable kind index for reading a variable ('old' value). */
    public final int readIndex;

    /** Variable kind index for writing a variable ('new' value). */
    public final int writeIndex;

    /** Expression converter for converting predicates. Uses 'readIndex' to access variables. */
    private ConvertExpression predicateConvertor = null;

    /**
     * Constructor of the {@link MvSpecBuilder} class.
     *
     * @param cifVarInfoBuilder Storage of the relation between nodes in the tree and CIF variables.
     * @param readIndex Variable sub-index for the current version of a variable.
     * @param writeIndex Variable sub-index for the next version of a variable.
     */
    public MvSpecBuilder(CifVarInfoBuilder cifVarInfoBuilder, int readIndex, int writeIndex) {
        this.cifVarInfoBuilder = cifVarInfoBuilder;
        this.tree = new Tree();
        this.readIndex = readIndex;
        this.writeIndex = writeIndex;
    }

    /**
     * Get the expression converter for predicate conversion.
     *
     * @return Converter for converting predicates.
     */
    public ConvertExpression getPredicateConvertor() {
        if (predicateConvertor == null) {
            predicateConvertor = new ConvertExpression(cifVarInfoBuilder, tree, readIndex, writeIndex);
        }
        return predicateConvertor;
    }
}
