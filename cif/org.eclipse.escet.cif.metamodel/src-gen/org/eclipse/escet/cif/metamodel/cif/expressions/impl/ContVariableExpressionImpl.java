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
package org.eclipse.escet.cif.metamodel.cif.expressions.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;

import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cont Variable Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ContVariableExpressionImpl#getVariable <em>Variable</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.ContVariableExpressionImpl#isDerivative <em>Derivative</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContVariableExpressionImpl extends ExpressionImpl implements ContVariableExpression
{
    /**
     * The cached value of the '{@link #getVariable() <em>Variable</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVariable()
     * @generated
     * @ordered
     */
    protected ContVariable variable;

    /**
     * The default value of the '{@link #isDerivative() <em>Derivative</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDerivative()
     * @generated
     * @ordered
     */
    protected static final boolean DERIVATIVE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isDerivative() <em>Derivative</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDerivative()
     * @generated
     * @ordered
     */
    protected boolean derivative = DERIVATIVE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ContVariableExpressionImpl()
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
        return ExpressionsPackage.Literals.CONT_VARIABLE_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ContVariable getVariable()
    {
        if (variable != null && variable.eIsProxy())
        {
            InternalEObject oldVariable = (InternalEObject)variable;
            variable = (ContVariable)eResolveProxy(oldVariable);
            if (variable != oldVariable)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.CONT_VARIABLE_EXPRESSION__VARIABLE, oldVariable, variable));
            }
        }
        return variable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContVariable basicGetVariable()
    {
        return variable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setVariable(ContVariable newVariable)
    {
        ContVariable oldVariable = variable;
        variable = newVariable;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.CONT_VARIABLE_EXPRESSION__VARIABLE, oldVariable, variable));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isDerivative()
    {
        return derivative;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDerivative(boolean newDerivative)
    {
        boolean oldDerivative = derivative;
        derivative = newDerivative;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.CONT_VARIABLE_EXPRESSION__DERIVATIVE, oldDerivative, derivative));
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
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION__VARIABLE:
                if (resolve) return getVariable();
                return basicGetVariable();
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION__DERIVATIVE:
                return isDerivative();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION__VARIABLE:
                setVariable((ContVariable)newValue);
                return;
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION__DERIVATIVE:
                setDerivative((Boolean)newValue);
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
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION__VARIABLE:
                setVariable((ContVariable)null);
                return;
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION__DERIVATIVE:
                setDerivative(DERIVATIVE_EDEFAULT);
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
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION__VARIABLE:
                return variable != null;
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION__DERIVATIVE:
                return derivative != DERIVATIVE_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (derivative: ");
        result.append(derivative);
        result.append(')');
        return result.toString();
    }

} //ContVariableExpressionImpl
