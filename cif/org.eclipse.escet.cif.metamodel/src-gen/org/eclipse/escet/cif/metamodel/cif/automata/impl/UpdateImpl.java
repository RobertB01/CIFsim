/**
 * Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif.automata.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.escet.cif.metamodel.cif.automata.AutomataPackage;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;

import org.eclipse.escet.common.position.metamodel.position.impl.PositionObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Update</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class UpdateImpl extends PositionObjectImpl implements Update
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UpdateImpl()
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
        return AutomataPackage.Literals.UPDATE;
    }

} //UpdateImpl
