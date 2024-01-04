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

package org.eclipse.escet.common.emf.ecore.codegen.java;

/** All the different types of model copiers. */
public enum ModelCopierTypes {
    /**
     * A model copier that maps both containments and references. By default, it constructs copies of all elements. This
     * is the generally considered the 'normal' copier.
     */
    COPY_FULL,

    /** A model copier that maps containments, but not references. By default, it constructs copies of all elements. */
    COPY_CONT,

    /** A model copier that maps both containments and references. By default, no copies are constructed. */
    INPLACE;
}
