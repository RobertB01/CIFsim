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
package org.eclipse.escet.cif.metamodel.cif.expressions.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binary Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BinaryExpressionImpl#getOperator <em>Operator</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BinaryExpressionImpl#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.BinaryExpressionImpl#getRight <em>Right</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BinaryExpressionImpl extends ExpressionImpl implements BinaryExpression
{
    /**
     * The default value of the '{@link #getOperator() <em>Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperator()
     * @generated
     * @ordered
     */
    protected static final BinaryOperator OPERATOR_EDEFAULT = BinaryOperator.DISJUNCTION;

    /**
     * The cached value of the '{@link #getOperator() <em>Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperator()
     * @generated
     * @ordered
     */
    protected BinaryOperator operator = OPERATOR_EDEFAULT;

    /**
     * The cached value of the '{@link #getLeft() <em>Left</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLeft()
     * @generated
     * @ordered
     */
    protected Expression left;

    /**
     * The cached value of the '{@link #getRight() <em>Right</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRight()
     * @generated
     * @ordered
     */
    protected Expression right;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BinaryExpressionImpl()
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
        return ExpressionsPackage.Literals.BINARY_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public BinaryOperator getOperator()
    {
        return operator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setOperator(BinaryOperator newOperator)
    {
        BinaryOperator oldOperator = operator;
        operator = newOperator == null ? OPERATOR_EDEFAULT : newOperator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.BINARY_EXPRESSION__OPERATOR, oldOperator, operator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getLeft()
    {
        return left;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLeft(Expression newLeft, NotificationChain msgs)
    {
        Expression oldLeft = left;
        left = newLeft;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.BINARY_EXPRESSION__LEFT, oldLeft, newLeft);
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
    public void setLeft(Expression newLeft)
    {
        if (newLeft != left)
        {
            NotificationChain msgs = null;
            if (left != null)
                msgs = ((InternalEObject)left).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.BINARY_EXPRESSION__LEFT, null, msgs);
            if (newLeft != null)
                msgs = ((InternalEObject)newLeft).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.BINARY_EXPRESSION__LEFT, null, msgs);
            msgs = basicSetLeft(newLeft, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.BINARY_EXPRESSION__LEFT, newLeft, newLeft));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getRight()
    {
        return right;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRight(Expression newRight, NotificationChain msgs)
    {
        Expression oldRight = right;
        right = newRight;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.BINARY_EXPRESSION__RIGHT, oldRight, newRight);
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
    public void setRight(Expression newRight)
    {
        if (newRight != right)
        {
            NotificationChain msgs = null;
            if (right != null)
                msgs = ((InternalEObject)right).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.BINARY_EXPRESSION__RIGHT, null, msgs);
            if (newRight != null)
                msgs = ((InternalEObject)newRight).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.BINARY_EXPRESSION__RIGHT, null, msgs);
            msgs = basicSetRight(newRight, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.BINARY_EXPRESSION__RIGHT, newRight, newRight));
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
            case ExpressionsPackage.BINARY_EXPRESSION__LEFT:
                return basicSetLeft(null, msgs);
            case ExpressionsPackage.BINARY_EXPRESSION__RIGHT:
                return basicSetRight(null, msgs);
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
            case ExpressionsPackage.BINARY_EXPRESSION__OPERATOR:
                return getOperator();
            case ExpressionsPackage.BINARY_EXPRESSION__LEFT:
                return getLeft();
            case ExpressionsPackage.BINARY_EXPRESSION__RIGHT:
                return getRight();
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
            case ExpressionsPackage.BINARY_EXPRESSION__OPERATOR:
                setOperator((BinaryOperator)newValue);
                return;
            case ExpressionsPackage.BINARY_EXPRESSION__LEFT:
                setLeft((Expression)newValue);
                return;
            case ExpressionsPackage.BINARY_EXPRESSION__RIGHT:
                setRight((Expression)newValue);
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
            case ExpressionsPackage.BINARY_EXPRESSION__OPERATOR:
                setOperator(OPERATOR_EDEFAULT);
                return;
            case ExpressionsPackage.BINARY_EXPRESSION__LEFT:
                setLeft((Expression)null);
                return;
            case ExpressionsPackage.BINARY_EXPRESSION__RIGHT:
                setRight((Expression)null);
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
            case ExpressionsPackage.BINARY_EXPRESSION__OPERATOR:
                return operator != OPERATOR_EDEFAULT;
            case ExpressionsPackage.BINARY_EXPRESSION__LEFT:
                return left != null;
            case ExpressionsPackage.BINARY_EXPRESSION__RIGHT:
                return right != null;
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
        result.append(" (operator: ");
        result.append(operator);
        result.append(')');
        return result.toString();
    }

} //BinaryExpressionImpl
