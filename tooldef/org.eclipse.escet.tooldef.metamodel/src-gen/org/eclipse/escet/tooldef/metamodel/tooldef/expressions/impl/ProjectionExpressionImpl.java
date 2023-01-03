/**
 * Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Projection Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ProjectionExpressionImpl#getChild <em>Child</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.expressions.impl.ProjectionExpressionImpl#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProjectionExpressionImpl extends ExpressionImpl implements ProjectionExpression
{
    /**
     * The cached value of the '{@link #getChild() <em>Child</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChild()
     * @generated
     * @ordered
     */
    protected Expression child;

    /**
     * The cached value of the '{@link #getIndex() <em>Index</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIndex()
     * @generated
     * @ordered
     */
    protected Expression index;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ProjectionExpressionImpl()
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
        return ExpressionsPackage.Literals.PROJECTION_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getChild()
    {
        return child;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetChild(Expression newChild, NotificationChain msgs)
    {
        Expression oldChild = child;
        child = newChild;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.PROJECTION_EXPRESSION__CHILD, oldChild, newChild);
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
    public void setChild(Expression newChild)
    {
        if (newChild != child)
        {
            NotificationChain msgs = null;
            if (child != null)
                msgs = ((InternalEObject)child).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.PROJECTION_EXPRESSION__CHILD, null, msgs);
            if (newChild != null)
                msgs = ((InternalEObject)newChild).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.PROJECTION_EXPRESSION__CHILD, null, msgs);
            msgs = basicSetChild(newChild, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.PROJECTION_EXPRESSION__CHILD, newChild, newChild));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getIndex()
    {
        return index;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIndex(Expression newIndex, NotificationChain msgs)
    {
        Expression oldIndex = index;
        index = newIndex;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.PROJECTION_EXPRESSION__INDEX, oldIndex, newIndex);
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
    public void setIndex(Expression newIndex)
    {
        if (newIndex != index)
        {
            NotificationChain msgs = null;
            if (index != null)
                msgs = ((InternalEObject)index).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.PROJECTION_EXPRESSION__INDEX, null, msgs);
            if (newIndex != null)
                msgs = ((InternalEObject)newIndex).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.PROJECTION_EXPRESSION__INDEX, null, msgs);
            msgs = basicSetIndex(newIndex, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.PROJECTION_EXPRESSION__INDEX, newIndex, newIndex));
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
            case ExpressionsPackage.PROJECTION_EXPRESSION__CHILD:
                return basicSetChild(null, msgs);
            case ExpressionsPackage.PROJECTION_EXPRESSION__INDEX:
                return basicSetIndex(null, msgs);
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
            case ExpressionsPackage.PROJECTION_EXPRESSION__CHILD:
                return getChild();
            case ExpressionsPackage.PROJECTION_EXPRESSION__INDEX:
                return getIndex();
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
            case ExpressionsPackage.PROJECTION_EXPRESSION__CHILD:
                setChild((Expression)newValue);
                return;
            case ExpressionsPackage.PROJECTION_EXPRESSION__INDEX:
                setIndex((Expression)newValue);
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
            case ExpressionsPackage.PROJECTION_EXPRESSION__CHILD:
                setChild((Expression)null);
                return;
            case ExpressionsPackage.PROJECTION_EXPRESSION__INDEX:
                setIndex((Expression)null);
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
            case ExpressionsPackage.PROJECTION_EXPRESSION__CHILD:
                return child != null;
            case ExpressionsPackage.PROJECTION_EXPRESSION__INDEX:
                return index != null;
        }
        return super.eIsSet(featureID);
    }

} //ProjectionExpressionImpl
