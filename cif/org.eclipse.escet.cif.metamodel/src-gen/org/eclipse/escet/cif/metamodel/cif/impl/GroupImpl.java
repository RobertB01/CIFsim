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

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Group;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.GroupImpl#getDefinitions <em>Definitions</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.GroupImpl#getComponents <em>Components</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GroupImpl extends ComplexComponentImpl implements Group
{
    /**
     * The cached value of the '{@link #getDefinitions() <em>Definitions</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefinitions()
     * @generated
     * @ordered
     */
    protected EList<ComponentDef> definitions;

    /**
     * The cached value of the '{@link #getComponents() <em>Components</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponents()
     * @generated
     * @ordered
     */
    protected EList<Component> components;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GroupImpl()
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
        return CifPackage.Literals.GROUP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<ComponentDef> getDefinitions()
    {
        if (definitions == null)
        {
            definitions = new EObjectContainmentEList<ComponentDef>(ComponentDef.class, this, CifPackage.GROUP__DEFINITIONS);
        }
        return definitions;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Component> getComponents()
    {
        if (components == null)
        {
            components = new EObjectContainmentEList<Component>(Component.class, this, CifPackage.GROUP__COMPONENTS);
        }
        return components;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
    {
        switch (featureID)
        {
            case CifPackage.GROUP__DEFINITIONS:
                return ((InternalEList<?>)getDefinitions()).basicRemove(otherEnd, msgs);
            case CifPackage.GROUP__COMPONENTS:
                return ((InternalEList<?>)getComponents()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType)
    {
        switch (featureID)
        {
            case CifPackage.GROUP__DEFINITIONS:
                return getDefinitions();
            case CifPackage.GROUP__COMPONENTS:
                return getComponents();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case CifPackage.GROUP__DEFINITIONS:
                getDefinitions().clear();
                getDefinitions().addAll((Collection<? extends ComponentDef>)newValue);
                return;
            case CifPackage.GROUP__COMPONENTS:
                getComponents().clear();
                getComponents().addAll((Collection<? extends Component>)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID)
    {
        switch (featureID)
        {
            case CifPackage.GROUP__DEFINITIONS:
                getDefinitions().clear();
                return;
            case CifPackage.GROUP__COMPONENTS:
                getComponents().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID)
    {
        switch (featureID)
        {
            case CifPackage.GROUP__DEFINITIONS:
                return definitions != null && !definitions.isEmpty();
            case CifPackage.GROUP__COMPONENTS:
                return components != null && !components.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //GroupImpl
