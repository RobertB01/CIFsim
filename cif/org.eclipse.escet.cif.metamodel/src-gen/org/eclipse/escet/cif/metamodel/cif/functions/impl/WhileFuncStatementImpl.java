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
package org.eclipse.escet.cif.metamodel.cif.functions.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>While Func Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.WhileFuncStatementImpl#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.functions.impl.WhileFuncStatementImpl#getStatements <em>Statements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WhileFuncStatementImpl extends FunctionStatementImpl implements WhileFuncStatement
{
    /**
     * The cached value of the '{@link #getGuards() <em>Guards</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGuards()
     * @generated
     * @ordered
     */
    protected EList<Expression> guards;

    /**
     * The cached value of the '{@link #getStatements() <em>Statements</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStatements()
     * @generated
     * @ordered
     */
    protected EList<FunctionStatement> statements;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected WhileFuncStatementImpl()
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
        return FunctionsPackage.Literals.WHILE_FUNC_STATEMENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getGuards()
    {
        if (guards == null)
        {
            guards = new EObjectContainmentEList<Expression>(Expression.class, this, FunctionsPackage.WHILE_FUNC_STATEMENT__GUARDS);
        }
        return guards;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<FunctionStatement> getStatements()
    {
        if (statements == null)
        {
            statements = new EObjectContainmentEList<FunctionStatement>(FunctionStatement.class, this, FunctionsPackage.WHILE_FUNC_STATEMENT__STATEMENTS);
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
            case FunctionsPackage.WHILE_FUNC_STATEMENT__GUARDS:
                return ((InternalEList<?>)getGuards()).basicRemove(otherEnd, msgs);
            case FunctionsPackage.WHILE_FUNC_STATEMENT__STATEMENTS:
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
            case FunctionsPackage.WHILE_FUNC_STATEMENT__GUARDS:
                return getGuards();
            case FunctionsPackage.WHILE_FUNC_STATEMENT__STATEMENTS:
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
            case FunctionsPackage.WHILE_FUNC_STATEMENT__GUARDS:
                getGuards().clear();
                getGuards().addAll((Collection<? extends Expression>)newValue);
                return;
            case FunctionsPackage.WHILE_FUNC_STATEMENT__STATEMENTS:
                getStatements().clear();
                getStatements().addAll((Collection<? extends FunctionStatement>)newValue);
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
            case FunctionsPackage.WHILE_FUNC_STATEMENT__GUARDS:
                getGuards().clear();
                return;
            case FunctionsPackage.WHILE_FUNC_STATEMENT__STATEMENTS:
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
            case FunctionsPackage.WHILE_FUNC_STATEMENT__GUARDS:
                return guards != null && !guards.isEmpty();
            case FunctionsPackage.WHILE_FUNC_STATEMENT__STATEMENTS:
                return statements != null && !statements.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //WhileFuncStatementImpl
