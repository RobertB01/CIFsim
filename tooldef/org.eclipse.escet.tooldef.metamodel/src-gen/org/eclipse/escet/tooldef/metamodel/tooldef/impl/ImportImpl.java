/**
 * Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.tooldef.metamodel.tooldef.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.escet.tooldef.metamodel.tooldef.Import;
import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Import</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class ImportImpl extends DeclarationImpl implements Import
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ImportImpl()
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
        return TooldefPackage.Literals.IMPORT;
    }

} //ImportImpl
