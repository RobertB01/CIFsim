/**
 * Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
 * 
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 * 
 * This program and the accompanying materials are made available under the terms
 * of the MIT License which is available at https://opensource.org/licenses/MIT
 * 
 * SPDX-License-Identifier: MIT
 * 
 * Disable Eclipse Java formatter for generated code file:
 * @formatter:off
 */
package org.eclipse.escet.cif.metamodel.cif.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;

import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Io Decl</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class IoDeclImpl extends PositionObjectImpl implements IoDecl
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IoDeclImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass()
    {
        return CifPackage.Literals.IO_DECL;
    }

} //IoDeclImpl
