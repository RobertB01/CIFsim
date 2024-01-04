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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>For Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ForStatementImpl#getStatements <em>Statements</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ForStatementImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl.ForStatementImpl#getAddressables <em>Addressables</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ForStatementImpl extends StatementImpl implements ForStatement
{
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
     * The cached value of the '{@link #getSource() <em>Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSource()
     * @generated
     * @ordered
     */
    protected Expression source;

    /**
     * The cached value of the '{@link #getAddressables() <em>Addressables</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAddressables()
     * @generated
     * @ordered
     */
    protected EList<AddressableDecl> addressables;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ForStatementImpl()
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
        return StatementsPackage.Literals.FOR_STATEMENT;
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
            statements = new EObjectContainmentEList<Statement>(Statement.class, this, StatementsPackage.FOR_STATEMENT__STATEMENTS);
        }
        return statements;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StatementsPackage.FOR_STATEMENT__SOURCE, oldSource, newSource);
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
                msgs = ((InternalEObject)source).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StatementsPackage.FOR_STATEMENT__SOURCE, null, msgs);
            if (newSource != null)
                msgs = ((InternalEObject)newSource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StatementsPackage.FOR_STATEMENT__SOURCE, null, msgs);
            msgs = basicSetSource(newSource, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, StatementsPackage.FOR_STATEMENT__SOURCE, newSource, newSource));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<AddressableDecl> getAddressables()
    {
        if (addressables == null)
        {
            addressables = new EObjectContainmentEList<AddressableDecl>(AddressableDecl.class, this, StatementsPackage.FOR_STATEMENT__ADDRESSABLES);
        }
        return addressables;
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
            case StatementsPackage.FOR_STATEMENT__STATEMENTS:
                return ((InternalEList<?>)getStatements()).basicRemove(otherEnd, msgs);
            case StatementsPackage.FOR_STATEMENT__SOURCE:
                return basicSetSource(null, msgs);
            case StatementsPackage.FOR_STATEMENT__ADDRESSABLES:
                return ((InternalEList<?>)getAddressables()).basicRemove(otherEnd, msgs);
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
            case StatementsPackage.FOR_STATEMENT__STATEMENTS:
                return getStatements();
            case StatementsPackage.FOR_STATEMENT__SOURCE:
                return getSource();
            case StatementsPackage.FOR_STATEMENT__ADDRESSABLES:
                return getAddressables();
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
            case StatementsPackage.FOR_STATEMENT__STATEMENTS:
                getStatements().clear();
                getStatements().addAll((Collection<? extends Statement>)newValue);
                return;
            case StatementsPackage.FOR_STATEMENT__SOURCE:
                setSource((Expression)newValue);
                return;
            case StatementsPackage.FOR_STATEMENT__ADDRESSABLES:
                getAddressables().clear();
                getAddressables().addAll((Collection<? extends AddressableDecl>)newValue);
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
            case StatementsPackage.FOR_STATEMENT__STATEMENTS:
                getStatements().clear();
                return;
            case StatementsPackage.FOR_STATEMENT__SOURCE:
                setSource((Expression)null);
                return;
            case StatementsPackage.FOR_STATEMENT__ADDRESSABLES:
                getAddressables().clear();
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
            case StatementsPackage.FOR_STATEMENT__STATEMENTS:
                return statements != null && !statements.isEmpty();
            case StatementsPackage.FOR_STATEMENT__SOURCE:
                return source != null;
            case StatementsPackage.FOR_STATEMENT__ADDRESSABLES:
                return addressables != null && !addressables.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ForStatementImpl
