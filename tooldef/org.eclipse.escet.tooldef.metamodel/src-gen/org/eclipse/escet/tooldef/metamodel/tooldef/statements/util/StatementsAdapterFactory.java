/**
 * Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage
 * @generated
 */
public class StatementsAdapterFactory extends AdapterFactoryImpl
{
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static StatementsPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StatementsAdapterFactory()
    {
        if (modelPackage == null)
        {
            modelPackage = StatementsPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object)
    {
        if (object == modelPackage)
        {
            return true;
        }
        if (object instanceof EObject)
        {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected StatementsSwitch<Adapter> modelSwitch =
        new StatementsSwitch<Adapter>()
        {
            @Override
            public Adapter caseStatement(Statement object)
            {
                return createStatementAdapter();
            }
            @Override
            public Adapter caseVariable(Variable object)
            {
                return createVariableAdapter();
            }
            @Override
            public Adapter caseWhileStatement(WhileStatement object)
            {
                return createWhileStatementAdapter();
            }
            @Override
            public Adapter caseBreakStatement(BreakStatement object)
            {
                return createBreakStatementAdapter();
            }
            @Override
            public Adapter caseContinueStatement(ContinueStatement object)
            {
                return createContinueStatementAdapter();
            }
            @Override
            public Adapter caseReturnStatement(ReturnStatement object)
            {
                return createReturnStatementAdapter();
            }
            @Override
            public Adapter caseExitStatement(ExitStatement object)
            {
                return createExitStatementAdapter();
            }
            @Override
            public Adapter caseForStatement(ForStatement object)
            {
                return createForStatementAdapter();
            }
            @Override
            public Adapter caseIfStatement(IfStatement object)
            {
                return createIfStatementAdapter();
            }
            @Override
            public Adapter caseAssignmentStatement(AssignmentStatement object)
            {
                return createAssignmentStatementAdapter();
            }
            @Override
            public Adapter caseToolInvokeStatement(ToolInvokeStatement object)
            {
                return createToolInvokeStatementAdapter();
            }
            @Override
            public Adapter caseElifStatement(ElifStatement object)
            {
                return createElifStatementAdapter();
            }
            @Override
            public Adapter caseAddressableDecl(AddressableDecl object)
            {
                return createAddressableDeclAdapter();
            }
            @Override
            public Adapter caseTupleAddressableDecl(TupleAddressableDecl object)
            {
                return createTupleAddressableDeclAdapter();
            }
            @Override
            public Adapter caseVariableAddressableDecl(VariableAddressableDecl object)
            {
                return createVariableAddressableDeclAdapter();
            }
            @Override
            public Adapter casePositionObject(PositionObject object)
            {
                return createPositionObjectAdapter();
            }
            @Override
            public Adapter caseDeclaration(Declaration object)
            {
                return createDeclarationAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object)
            {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target)
    {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement <em>Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement
     * @generated
     */
    public Adapter createStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable <em>Variable</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable
     * @generated
     */
    public Adapter createVariableAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement <em>While Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement
     * @generated
     */
    public Adapter createWhileStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement <em>Break Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement
     * @generated
     */
    public Adapter createBreakStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement <em>Continue Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement
     * @generated
     */
    public Adapter createContinueStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement <em>Return Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement
     * @generated
     */
    public Adapter createReturnStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement <em>Exit Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement
     * @generated
     */
    public Adapter createExitStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement <em>For Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement
     * @generated
     */
    public Adapter createForStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement <em>If Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement
     * @generated
     */
    public Adapter createIfStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement <em>Assignment Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement
     * @generated
     */
    public Adapter createAssignmentStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement <em>Tool Invoke Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement
     * @generated
     */
    public Adapter createToolInvokeStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement <em>Elif Statement</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement
     * @generated
     */
    public Adapter createElifStatementAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl <em>Addressable Decl</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl
     * @generated
     */
    public Adapter createAddressableDeclAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl <em>Tuple Addressable Decl</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl
     * @generated
     */
    public Adapter createTupleAddressableDeclAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl <em>Variable Addressable Decl</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl
     * @generated
     */
    public Adapter createVariableAddressableDeclAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.common.position.metamodel.position.PositionObject <em>Object</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.common.position.metamodel.position.PositionObject
     * @generated
     */
    public Adapter createPositionObjectAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.escet.tooldef.metamodel.tooldef.Declaration <em>Declaration</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.escet.tooldef.metamodel.tooldef.Declaration
     * @generated
     */
    public Adapter createDeclarationAdapter()
    {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter()
    {
        return null;
    }

} //StatementsAdapterFactory
