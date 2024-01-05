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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>If Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.IfExpressionImpl#getGuards <em>Guards</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.IfExpressionImpl#getThen <em>Then</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.IfExpressionImpl#getElse <em>Else</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.IfExpressionImpl#getElifs <em>Elifs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IfExpressionImpl extends ExpressionImpl implements IfExpression
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
     * The cached value of the '{@link #getThen() <em>Then</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getThen()
     * @generated
     * @ordered
     */
    protected Expression then;

    /**
     * The cached value of the '{@link #getElse() <em>Else</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElse()
     * @generated
     * @ordered
     */
    protected Expression else_;

    /**
     * The cached value of the '{@link #getElifs() <em>Elifs</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElifs()
     * @generated
     * @ordered
     */
    protected EList<ElifExpression> elifs;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IfExpressionImpl()
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
        return ExpressionsPackage.Literals.IF_EXPRESSION;
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
            guards = new EObjectContainmentEList<Expression>(Expression.class, this, ExpressionsPackage.IF_EXPRESSION__GUARDS);
        }
        return guards;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getThen()
    {
        return then;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetThen(Expression newThen, NotificationChain msgs)
    {
        Expression oldThen = then;
        then = newThen;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.IF_EXPRESSION__THEN, oldThen, newThen);
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
    public void setThen(Expression newThen)
    {
        if (newThen != then)
        {
            NotificationChain msgs = null;
            if (then != null)
                msgs = ((InternalEObject)then).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.IF_EXPRESSION__THEN, null, msgs);
            if (newThen != null)
                msgs = ((InternalEObject)newThen).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.IF_EXPRESSION__THEN, null, msgs);
            msgs = basicSetThen(newThen, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.IF_EXPRESSION__THEN, newThen, newThen));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getElse()
    {
        return else_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetElse(Expression newElse, NotificationChain msgs)
    {
        Expression oldElse = else_;
        else_ = newElse;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.IF_EXPRESSION__ELSE, oldElse, newElse);
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
    public void setElse(Expression newElse)
    {
        if (newElse != else_)
        {
            NotificationChain msgs = null;
            if (else_ != null)
                msgs = ((InternalEObject)else_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.IF_EXPRESSION__ELSE, null, msgs);
            if (newElse != null)
                msgs = ((InternalEObject)newElse).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.IF_EXPRESSION__ELSE, null, msgs);
            msgs = basicSetElse(newElse, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.IF_EXPRESSION__ELSE, newElse, newElse));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<ElifExpression> getElifs()
    {
        if (elifs == null)
        {
            elifs = new EObjectContainmentEList<ElifExpression>(ElifExpression.class, this, ExpressionsPackage.IF_EXPRESSION__ELIFS);
        }
        return elifs;
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
            case ExpressionsPackage.IF_EXPRESSION__GUARDS:
                return ((InternalEList<?>)getGuards()).basicRemove(otherEnd, msgs);
            case ExpressionsPackage.IF_EXPRESSION__THEN:
                return basicSetThen(null, msgs);
            case ExpressionsPackage.IF_EXPRESSION__ELSE:
                return basicSetElse(null, msgs);
            case ExpressionsPackage.IF_EXPRESSION__ELIFS:
                return ((InternalEList<?>)getElifs()).basicRemove(otherEnd, msgs);
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
            case ExpressionsPackage.IF_EXPRESSION__GUARDS:
                return getGuards();
            case ExpressionsPackage.IF_EXPRESSION__THEN:
                return getThen();
            case ExpressionsPackage.IF_EXPRESSION__ELSE:
                return getElse();
            case ExpressionsPackage.IF_EXPRESSION__ELIFS:
                return getElifs();
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
            case ExpressionsPackage.IF_EXPRESSION__GUARDS:
                getGuards().clear();
                getGuards().addAll((Collection<? extends Expression>)newValue);
                return;
            case ExpressionsPackage.IF_EXPRESSION__THEN:
                setThen((Expression)newValue);
                return;
            case ExpressionsPackage.IF_EXPRESSION__ELSE:
                setElse((Expression)newValue);
                return;
            case ExpressionsPackage.IF_EXPRESSION__ELIFS:
                getElifs().clear();
                getElifs().addAll((Collection<? extends ElifExpression>)newValue);
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
            case ExpressionsPackage.IF_EXPRESSION__GUARDS:
                getGuards().clear();
                return;
            case ExpressionsPackage.IF_EXPRESSION__THEN:
                setThen((Expression)null);
                return;
            case ExpressionsPackage.IF_EXPRESSION__ELSE:
                setElse((Expression)null);
                return;
            case ExpressionsPackage.IF_EXPRESSION__ELIFS:
                getElifs().clear();
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
            case ExpressionsPackage.IF_EXPRESSION__GUARDS:
                return guards != null && !guards.isEmpty();
            case ExpressionsPackage.IF_EXPRESSION__THEN:
                return then != null;
            case ExpressionsPackage.IF_EXPRESSION__ELSE:
                return else_ != null;
            case ExpressionsPackage.IF_EXPRESSION__ELIFS:
                return elifs != null && !elifs.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //IfExpressionImpl
