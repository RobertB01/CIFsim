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

package org.eclipse.escet.chi.codegen.statements.seq.assignment;

/** Assignment node that delegates the real assignment to a child. */
public abstract class ChainedAsgNode implements AssignmentNode {
    /** Child assignment. */
    protected AssignmentNode child;

    /**
     * Constructor of the {@link ChainedAsgNode} class.
     *
     * @param child Child assignment.
     */
    public ChainedAsgNode(AssignmentNode child) {
        this.child = child;
    }
}
