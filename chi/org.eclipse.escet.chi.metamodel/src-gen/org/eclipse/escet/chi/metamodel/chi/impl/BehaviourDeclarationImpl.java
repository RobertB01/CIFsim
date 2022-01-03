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
package org.eclipse.escet.chi.metamodel.chi.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Behaviour Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.BehaviourDeclarationImpl#getVariables <em>Variables</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.BehaviourDeclarationImpl#getStatements <em>Statements</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class BehaviourDeclarationImpl extends DeclarationImpl implements BehaviourDeclaration
{
    /**
     * The cached value of the '{@link #getVariables() <em>Variables</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVariables()
     * @generated
     * @ordered
     */
    protected EList<VariableDeclaration> variables;

    /**
     * The cached value of the '{@link #getStatements() <em>Statements</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStatements()
     * @generated
     * @ordered
     */
    protected EList<Statement> statements;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BehaviourDeclarationImpl()
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
        return ChiPackage.Literals.BEHAVIOUR_DECLARATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<VariableDeclaration> getVariables()
    {
        if (variables == null)
        {
            variables = new EObjectContainmentEList<VariableDeclaration>(VariableDeclaration.class, this, ChiPackage.BEHAVIOUR_DECLARATION__VARIABLES);
        }
        return variables;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Statement> getStatements()
    {
        if (statements == null)
        {
            statements = new EObjectContainmentEList<Statement>(Statement.class, this, ChiPackage.BEHAVIOUR_DECLARATION__STATEMENTS);
        }
        return statements;
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
            case ChiPackage.BEHAVIOUR_DECLARATION__VARIABLES:
                return ((InternalEList<?>)getVariables()).basicRemove(otherEnd, msgs);
            case ChiPackage.BEHAVIOUR_DECLARATION__STATEMENTS:
                return ((InternalEList<?>)getStatements()).basicRemove(otherEnd, msgs);
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
            case ChiPackage.BEHAVIOUR_DECLARATION__VARIABLES:
                return getVariables();
            case ChiPackage.BEHAVIOUR_DECLARATION__STATEMENTS:
                return getStatements();
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
            case ChiPackage.BEHAVIOUR_DECLARATION__VARIABLES:
                getVariables().clear();
                getVariables().addAll((Collection<? extends VariableDeclaration>)newValue);
                return;
            case ChiPackage.BEHAVIOUR_DECLARATION__STATEMENTS:
                getStatements().clear();
                getStatements().addAll((Collection<? extends Statement>)newValue);
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
            case ChiPackage.BEHAVIOUR_DECLARATION__VARIABLES:
                getVariables().clear();
                return;
            case ChiPackage.BEHAVIOUR_DECLARATION__STATEMENTS:
                getStatements().clear();
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
            case ChiPackage.BEHAVIOUR_DECLARATION__VARIABLES:
                return variables != null && !variables.isEmpty();
            case ChiPackage.BEHAVIOUR_DECLARATION__STATEMENTS:
                return statements != null && !statements.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //BehaviourDeclarationImpl
