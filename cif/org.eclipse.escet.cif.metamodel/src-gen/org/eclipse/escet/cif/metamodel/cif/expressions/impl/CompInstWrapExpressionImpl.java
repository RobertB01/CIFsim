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

import org.eclipse.escet.cif.metamodel.cif.ComponentInst;

import org.eclipse.escet.cif.metamodel.cif.expressions.CompInstWrapExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Comp Inst Wrap Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompInstWrapExpressionImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.expressions.impl.CompInstWrapExpressionImpl#getInstantiation <em>Instantiation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompInstWrapExpressionImpl extends ExpressionImpl implements CompInstWrapExpression
{
    /**
     * The cached value of the '{@link #getReference() <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReference()
     * @generated
     * @ordered
     */
    protected Expression reference;

    /**
     * The cached value of the '{@link #getInstantiation() <em>Instantiation</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInstantiation()
     * @generated
     * @ordered
     */
    protected ComponentInst instantiation;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CompInstWrapExpressionImpl()
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
        return ExpressionsPackage.Literals.COMP_INST_WRAP_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getReference()
    {
        return reference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReference(Expression newReference, NotificationChain msgs)
    {
        Expression oldReference = reference;
        reference = newReference;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE, oldReference, newReference);
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
    public void setReference(Expression newReference)
    {
        if (newReference != reference)
        {
            NotificationChain msgs = null;
            if (reference != null)
                msgs = ((InternalEObject)reference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE, null, msgs);
            if (newReference != null)
                msgs = ((InternalEObject)newReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE, null, msgs);
            msgs = basicSetReference(newReference, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE, newReference, newReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ComponentInst getInstantiation()
    {
        if (instantiation != null && instantiation.eIsProxy())
        {
            InternalEObject oldInstantiation = (InternalEObject)instantiation;
            instantiation = (ComponentInst)eResolveProxy(oldInstantiation);
            if (instantiation != oldInstantiation)
            {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__INSTANTIATION, oldInstantiation, instantiation));
            }
        }
        return instantiation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentInst basicGetInstantiation()
    {
        return instantiation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setInstantiation(ComponentInst newInstantiation)
    {
        ComponentInst oldInstantiation = instantiation;
        instantiation = newInstantiation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__INSTANTIATION, oldInstantiation, instantiation));
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
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE:
                return basicSetReference(null, msgs);
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
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE:
                return getReference();
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__INSTANTIATION:
                if (resolve) return getInstantiation();
                return basicGetInstantiation();
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
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE:
                setReference((Expression)newValue);
                return;
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__INSTANTIATION:
                setInstantiation((ComponentInst)newValue);
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
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE:
                setReference((Expression)null);
                return;
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__INSTANTIATION:
                setInstantiation((ComponentInst)null);
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
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__REFERENCE:
                return reference != null;
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION__INSTANTIATION:
                return instantiation != null;
        }
        return super.eIsSet(featureID);
    }

} //CompInstWrapExpressionImpl
