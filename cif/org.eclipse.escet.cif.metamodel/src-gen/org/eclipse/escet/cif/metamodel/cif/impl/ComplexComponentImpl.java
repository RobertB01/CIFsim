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
package org.eclipse.escet.cif.metamodel.cif.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.escet.cif.metamodel.cif.CifPackage;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;

import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Complex Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl#getDeclarations <em>Declarations</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl#getMarkeds <em>Markeds</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl#getInvariants <em>Invariants</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl#getInitials <em>Initials</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl#getEquations <em>Equations</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.impl.ComplexComponentImpl#getIoDecls <em>Io Decls</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class ComplexComponentImpl extends ComponentImpl implements ComplexComponent
{
    /**
     * The cached value of the '{@link #getDeclarations() <em>Declarations</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDeclarations()
     * @generated
     * @ordered
     */
    protected EList<Declaration> declarations;

    /**
     * The cached value of the '{@link #getMarkeds() <em>Markeds</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMarkeds()
     * @generated
     * @ordered
     */
    protected EList<Expression> markeds;

    /**
     * The cached value of the '{@link #getInvariants() <em>Invariants</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInvariants()
     * @generated
     * @ordered
     */
    protected EList<Invariant> invariants;

    /**
     * The cached value of the '{@link #getInitials() <em>Initials</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInitials()
     * @generated
     * @ordered
     */
    protected EList<Expression> initials;

    /**
     * The cached value of the '{@link #getEquations() <em>Equations</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEquations()
     * @generated
     * @ordered
     */
    protected EList<Equation> equations;

    /**
     * The cached value of the '{@link #getIoDecls() <em>Io Decls</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIoDecls()
     * @generated
     * @ordered
     */
    protected EList<IoDecl> ioDecls;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ComplexComponentImpl()
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
        return CifPackage.Literals.COMPLEX_COMPONENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Declaration> getDeclarations()
    {
        if (declarations == null)
        {
            declarations = new EObjectContainmentEList<Declaration>(Declaration.class, this, CifPackage.COMPLEX_COMPONENT__DECLARATIONS);
        }
        return declarations;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getMarkeds()
    {
        if (markeds == null)
        {
            markeds = new EObjectContainmentEList<Expression>(Expression.class, this, CifPackage.COMPLEX_COMPONENT__MARKEDS);
        }
        return markeds;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Invariant> getInvariants()
    {
        if (invariants == null)
        {
            invariants = new EObjectContainmentEList<Invariant>(Invariant.class, this, CifPackage.COMPLEX_COMPONENT__INVARIANTS);
        }
        return invariants;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Expression> getInitials()
    {
        if (initials == null)
        {
            initials = new EObjectContainmentEList<Expression>(Expression.class, this, CifPackage.COMPLEX_COMPONENT__INITIALS);
        }
        return initials;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Equation> getEquations()
    {
        if (equations == null)
        {
            equations = new EObjectContainmentEList<Equation>(Equation.class, this, CifPackage.COMPLEX_COMPONENT__EQUATIONS);
        }
        return equations;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<IoDecl> getIoDecls()
    {
        if (ioDecls == null)
        {
            ioDecls = new EObjectContainmentEList<IoDecl>(IoDecl.class, this, CifPackage.COMPLEX_COMPONENT__IO_DECLS);
        }
        return ioDecls;
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
            case CifPackage.COMPLEX_COMPONENT__DECLARATIONS:
                return ((InternalEList<?>)getDeclarations()).basicRemove(otherEnd, msgs);
            case CifPackage.COMPLEX_COMPONENT__MARKEDS:
                return ((InternalEList<?>)getMarkeds()).basicRemove(otherEnd, msgs);
            case CifPackage.COMPLEX_COMPONENT__INVARIANTS:
                return ((InternalEList<?>)getInvariants()).basicRemove(otherEnd, msgs);
            case CifPackage.COMPLEX_COMPONENT__INITIALS:
                return ((InternalEList<?>)getInitials()).basicRemove(otherEnd, msgs);
            case CifPackage.COMPLEX_COMPONENT__EQUATIONS:
                return ((InternalEList<?>)getEquations()).basicRemove(otherEnd, msgs);
            case CifPackage.COMPLEX_COMPONENT__IO_DECLS:
                return ((InternalEList<?>)getIoDecls()).basicRemove(otherEnd, msgs);
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
            case CifPackage.COMPLEX_COMPONENT__DECLARATIONS:
                return getDeclarations();
            case CifPackage.COMPLEX_COMPONENT__MARKEDS:
                return getMarkeds();
            case CifPackage.COMPLEX_COMPONENT__INVARIANTS:
                return getInvariants();
            case CifPackage.COMPLEX_COMPONENT__INITIALS:
                return getInitials();
            case CifPackage.COMPLEX_COMPONENT__EQUATIONS:
                return getEquations();
            case CifPackage.COMPLEX_COMPONENT__IO_DECLS:
                return getIoDecls();
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
            case CifPackage.COMPLEX_COMPONENT__DECLARATIONS:
                getDeclarations().clear();
                getDeclarations().addAll((Collection<? extends Declaration>)newValue);
                return;
            case CifPackage.COMPLEX_COMPONENT__MARKEDS:
                getMarkeds().clear();
                getMarkeds().addAll((Collection<? extends Expression>)newValue);
                return;
            case CifPackage.COMPLEX_COMPONENT__INVARIANTS:
                getInvariants().clear();
                getInvariants().addAll((Collection<? extends Invariant>)newValue);
                return;
            case CifPackage.COMPLEX_COMPONENT__INITIALS:
                getInitials().clear();
                getInitials().addAll((Collection<? extends Expression>)newValue);
                return;
            case CifPackage.COMPLEX_COMPONENT__EQUATIONS:
                getEquations().clear();
                getEquations().addAll((Collection<? extends Equation>)newValue);
                return;
            case CifPackage.COMPLEX_COMPONENT__IO_DECLS:
                getIoDecls().clear();
                getIoDecls().addAll((Collection<? extends IoDecl>)newValue);
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
            case CifPackage.COMPLEX_COMPONENT__DECLARATIONS:
                getDeclarations().clear();
                return;
            case CifPackage.COMPLEX_COMPONENT__MARKEDS:
                getMarkeds().clear();
                return;
            case CifPackage.COMPLEX_COMPONENT__INVARIANTS:
                getInvariants().clear();
                return;
            case CifPackage.COMPLEX_COMPONENT__INITIALS:
                getInitials().clear();
                return;
            case CifPackage.COMPLEX_COMPONENT__EQUATIONS:
                getEquations().clear();
                return;
            case CifPackage.COMPLEX_COMPONENT__IO_DECLS:
                getIoDecls().clear();
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
            case CifPackage.COMPLEX_COMPONENT__DECLARATIONS:
                return declarations != null && !declarations.isEmpty();
            case CifPackage.COMPLEX_COMPONENT__MARKEDS:
                return markeds != null && !markeds.isEmpty();
            case CifPackage.COMPLEX_COMPONENT__INVARIANTS:
                return invariants != null && !invariants.isEmpty();
            case CifPackage.COMPLEX_COMPONENT__INITIALS:
                return initials != null && !initials.isEmpty();
            case CifPackage.COMPLEX_COMPONENT__EQUATIONS:
                return equations != null && !equations.isEmpty();
            case CifPackage.COMPLEX_COMPONENT__IO_DECLS:
                return ioDecls != null && !ioDecls.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ComplexComponentImpl
