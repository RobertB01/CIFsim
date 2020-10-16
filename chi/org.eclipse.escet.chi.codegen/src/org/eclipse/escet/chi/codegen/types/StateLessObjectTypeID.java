//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.common.java.Assert.fail;

import java.util.List;

import org.eclipse.escet.chi.codegen.java.JavaFile;

/**
 * Object type id, except its object values have no internal state that can be changed, so they do not need to be
 * deep-copied.
 */
public abstract class StateLessObjectTypeID extends ObjectTypeID {
    /**
     * Constructor for a {@link StateLessObjectTypeID} class.
     *
     * @param needsCoordinator Whether the type needs a chiCoordinator for construction.
     * @param kind Type kind of the type.
     */
    public StateLessObjectTypeID(boolean needsCoordinator, TypeKind kind) {
        super(needsCoordinator, kind);
    }

    /**
     * Constructor for a {@link StateLessObjectTypeID} class.
     *
     * @param needsCoordinator Whether the type needs a chiCoordinator for construction.
     * @param kind Type kind of the type.
     * @param subTypes Sub types of the data type.
     */
    public StateLessObjectTypeID(boolean needsCoordinator, TypeKind kind, List<TypeID> subTypes) {
        super(needsCoordinator, kind, subTypes);
    }

    @Override
    public boolean hasDeepCopy() {
        return false;
    }

    @Override
    public String getDeepCopyName(String val, JavaFile jf, boolean doDeep) {
        fail("getDeepCopyName() should not be accessed.");
        return null;
    }
}
