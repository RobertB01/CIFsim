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

import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;
import org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class DeclarationImpl extends PositionObjectImpl implements Declaration
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DeclarationImpl()
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
        return TooldefPackage.Literals.DECLARATION;
    }

} //DeclarationImpl
