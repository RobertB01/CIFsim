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
package org.eclipse.escet.cif.metamodel.cif.expressions.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;
import org.eclipse.escet.cif.metamodel.cif.expressions.FunctionCallExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function Call Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionCallExpressionImpl#getParams <em>Params</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.FunctionCallExpressionImpl#getFunction <em>Function</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FunctionCallExpressionImpl extends ExpressionImpl implements FunctionCallExpression
{
    /**
     * The cached value of the '{@link #getParams() <em>Params</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParams()
     * @generated
     * @ordered
     */
    protected EList<Expression> params;

    /**
     * The cached value of the '{@link #getFunction() <em>Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFunction()
     * @generated
     * @ordered
     */
    protected Expression function;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FunctionCallExpressionImpl()
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
        return ExpressionsPackage.Literals.FUNCTION_CALL_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getParams()
    {
        if (params == null)
        {
            params = new EObjectContainmentEList<Expression>(Expression.class, this, ExpressionsPackage.FUNCTION_CALL_EXPRESSION__PARAMS);
        }
        return params;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getFunction()
    {
        return function;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFunction(Expression newFunction, NotificationChain msgs)
    {
        Expression oldFunction = function;
        function = newFunction;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION, oldFunction, newFunction);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setFunction(Expression newFunction)
    {
        if (newFunction != function)
        {
            NotificationChain msgs = null;
            if (function != null)
                msgs = ((InternalEObject)function).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION, null, msgs);
            if (newFunction != null)
                msgs = ((InternalEObject)newFunction).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION, null, msgs);
            msgs = basicSetFunction(newFunction, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION, newFunction, newFunction));
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
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__PARAMS:
                return ((InternalEList<?>)getParams()).basicRemove(otherEnd, msgs);
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION:
                return basicSetFunction(null, msgs);
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
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__PARAMS:
                return getParams();
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION:
                return getFunction();
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
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__PARAMS:
                getParams().clear();
                getParams().addAll((Collection<? extends Expression>)newValue);
                return;
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION:
                setFunction((Expression)newValue);
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
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__PARAMS:
                getParams().clear();
                return;
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION:
                setFunction((Expression)null);
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
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__PARAMS:
                return params != null && !params.isEmpty();
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION__FUNCTION:
                return function != null;
        }
        return super.eIsSet(featureID);
    }

} //FunctionCallExpressionImpl
