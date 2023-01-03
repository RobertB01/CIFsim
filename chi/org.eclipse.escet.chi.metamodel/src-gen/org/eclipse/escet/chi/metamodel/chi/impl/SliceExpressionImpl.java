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
package org.eclipse.escet.chi.metamodel.chi.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.escet.chi.metamodel.chi.ChiPackage;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.SliceExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slice Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.SliceExpressionImpl#getStart <em>Start</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.SliceExpressionImpl#getEnd <em>End</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.SliceExpressionImpl#getStep <em>Step</em>}</li>
 *   <li>{@link org.eclipse.escet.chi.metamodel.chi.impl.SliceExpressionImpl#getSource <em>Source</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SliceExpressionImpl extends ExpressionImpl implements SliceExpression
{
    /**
     * The cached value of the '{@link #getStart() <em>Start</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStart()
     * @generated
     * @ordered
     */
    protected Expression start;

    /**
     * The cached value of the '{@link #getEnd() <em>End</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEnd()
     * @generated
     * @ordered
     */
    protected Expression end;

    /**
     * The cached value of the '{@link #getStep() <em>Step</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStep()
     * @generated
     * @ordered
     */
    protected Expression step;

    /**
     * The cached value of the '{@link #getSource() <em>Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected Expression source;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SliceExpressionImpl()
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
        return ChiPackage.Literals.SLICE_EXPRESSION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getStart()
    {
        return start;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetStart(Expression newStart, NotificationChain msgs)
    {
        Expression oldStart = start;
        start = newStart;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.SLICE_EXPRESSION__START, oldStart, newStart);
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
    public void setStart(Expression newStart)
    {
        if (newStart != start)
        {
            NotificationChain msgs = null;
            if (start != null)
                msgs = ((InternalEObject)start).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.SLICE_EXPRESSION__START, null, msgs);
            if (newStart != null)
                msgs = ((InternalEObject)newStart).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.SLICE_EXPRESSION__START, null, msgs);
            msgs = basicSetStart(newStart, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.SLICE_EXPRESSION__START, newStart, newStart));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getEnd()
    {
        return end;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEnd(Expression newEnd, NotificationChain msgs)
    {
        Expression oldEnd = end;
        end = newEnd;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.SLICE_EXPRESSION__END, oldEnd, newEnd);
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
    public void setEnd(Expression newEnd)
    {
        if (newEnd != end)
        {
            NotificationChain msgs = null;
            if (end != null)
                msgs = ((InternalEObject)end).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.SLICE_EXPRESSION__END, null, msgs);
            if (newEnd != null)
                msgs = ((InternalEObject)newEnd).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.SLICE_EXPRESSION__END, null, msgs);
            msgs = basicSetEnd(newEnd, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.SLICE_EXPRESSION__END, newEnd, newEnd));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getStep()
    {
        return step;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetStep(Expression newStep, NotificationChain msgs)
    {
        Expression oldStep = step;
        step = newStep;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.SLICE_EXPRESSION__STEP, oldStep, newStep);
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
    public void setStep(Expression newStep)
    {
        if (newStep != step)
        {
            NotificationChain msgs = null;
            if (step != null)
                msgs = ((InternalEObject)step).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.SLICE_EXPRESSION__STEP, null, msgs);
            if (newStep != null)
                msgs = ((InternalEObject)newStep).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.SLICE_EXPRESSION__STEP, null, msgs);
            msgs = basicSetStep(newStep, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.SLICE_EXPRESSION__STEP, newStep, newStep));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getSource()
    {
        return source;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSource(Expression newSource, NotificationChain msgs)
    {
        Expression oldSource = source;
        source = newSource;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChiPackage.SLICE_EXPRESSION__SOURCE, oldSource, newSource);
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
    public void setSource(Expression newSource)
    {
        if (newSource != source)
        {
            NotificationChain msgs = null;
            if (source != null)
                msgs = ((InternalEObject)source).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChiPackage.SLICE_EXPRESSION__SOURCE, null, msgs);
            if (newSource != null)
                msgs = ((InternalEObject)newSource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChiPackage.SLICE_EXPRESSION__SOURCE, null, msgs);
            msgs = basicSetSource(newSource, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ChiPackage.SLICE_EXPRESSION__SOURCE, newSource, newSource));
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
            case ChiPackage.SLICE_EXPRESSION__START:
                return basicSetStart(null, msgs);
            case ChiPackage.SLICE_EXPRESSION__END:
                return basicSetEnd(null, msgs);
            case ChiPackage.SLICE_EXPRESSION__STEP:
                return basicSetStep(null, msgs);
            case ChiPackage.SLICE_EXPRESSION__SOURCE:
                return basicSetSource(null, msgs);
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
            case ChiPackage.SLICE_EXPRESSION__START:
                return getStart();
            case ChiPackage.SLICE_EXPRESSION__END:
                return getEnd();
            case ChiPackage.SLICE_EXPRESSION__STEP:
                return getStep();
            case ChiPackage.SLICE_EXPRESSION__SOURCE:
                return getSource();
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
            case ChiPackage.SLICE_EXPRESSION__START:
                setStart((Expression)newValue);
                return;
            case ChiPackage.SLICE_EXPRESSION__END:
                setEnd((Expression)newValue);
                return;
            case ChiPackage.SLICE_EXPRESSION__STEP:
                setStep((Expression)newValue);
                return;
            case ChiPackage.SLICE_EXPRESSION__SOURCE:
                setSource((Expression)newValue);
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
            case ChiPackage.SLICE_EXPRESSION__START:
                setStart((Expression)null);
                return;
            case ChiPackage.SLICE_EXPRESSION__END:
                setEnd((Expression)null);
                return;
            case ChiPackage.SLICE_EXPRESSION__STEP:
                setStep((Expression)null);
                return;
            case ChiPackage.SLICE_EXPRESSION__SOURCE:
                setSource((Expression)null);
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
            case ChiPackage.SLICE_EXPRESSION__START:
                return start != null;
            case ChiPackage.SLICE_EXPRESSION__END:
                return end != null;
            case ChiPackage.SLICE_EXPRESSION__STEP:
                return step != null;
            case ChiPackage.SLICE_EXPRESSION__SOURCE:
                return source != null;
        }
        return super.eIsSet(featureID);
    }

} //SliceExpressionImpl
