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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StatementsFactoryImpl extends EFactoryImpl implements StatementsFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static StatementsFactory init()
    {
        try
        {
            StatementsFactory theStatementsFactory = (StatementsFactory)EPackage.Registry.INSTANCE.getEFactory(StatementsPackage.eNS_URI);
            if (theStatementsFactory != null)
            {
                return theStatementsFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new StatementsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StatementsFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case StatementsPackage.VARIABLE: return createVariable();
            case StatementsPackage.WHILE_STATEMENT: return createWhileStatement();
            case StatementsPackage.BREAK_STATEMENT: return createBreakStatement();
            case StatementsPackage.CONTINUE_STATEMENT: return createContinueStatement();
            case StatementsPackage.RETURN_STATEMENT: return createReturnStatement();
            case StatementsPackage.EXIT_STATEMENT: return createExitStatement();
            case StatementsPackage.FOR_STATEMENT: return createForStatement();
            case StatementsPackage.IF_STATEMENT: return createIfStatement();
            case StatementsPackage.ASSIGNMENT_STATEMENT: return createAssignmentStatement();
            case StatementsPackage.TOOL_INVOKE_STATEMENT: return createToolInvokeStatement();
            case StatementsPackage.ELIF_STATEMENT: return createElifStatement();
            case StatementsPackage.TUPLE_ADDRESSABLE_DECL: return createTupleAddressableDecl();
            case StatementsPackage.VARIABLE_ADDRESSABLE_DECL: return createVariableAddressableDecl();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Variable createVariable()
    {
        VariableImpl variable = new VariableImpl();
        return variable;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public WhileStatement createWhileStatement()
    {
        WhileStatementImpl whileStatement = new WhileStatementImpl();
        return whileStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public BreakStatement createBreakStatement()
    {
        BreakStatementImpl breakStatement = new BreakStatementImpl();
        return breakStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ContinueStatement createContinueStatement()
    {
        ContinueStatementImpl continueStatement = new ContinueStatementImpl();
        return continueStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ReturnStatement createReturnStatement()
    {
        ReturnStatementImpl returnStatement = new ReturnStatementImpl();
        return returnStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ExitStatement createExitStatement()
    {
        ExitStatementImpl exitStatement = new ExitStatementImpl();
        return exitStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ForStatement createForStatement()
    {
        ForStatementImpl forStatement = new ForStatementImpl();
        return forStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IfStatement createIfStatement()
    {
        IfStatementImpl ifStatement = new IfStatementImpl();
        return ifStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AssignmentStatement createAssignmentStatement()
    {
        AssignmentStatementImpl assignmentStatement = new AssignmentStatementImpl();
        return assignmentStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ToolInvokeStatement createToolInvokeStatement()
    {
        ToolInvokeStatementImpl toolInvokeStatement = new ToolInvokeStatementImpl();
        return toolInvokeStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ElifStatement createElifStatement()
    {
        ElifStatementImpl elifStatement = new ElifStatementImpl();
        return elifStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public TupleAddressableDecl createTupleAddressableDecl()
    {
        TupleAddressableDeclImpl tupleAddressableDecl = new TupleAddressableDeclImpl();
        return tupleAddressableDecl;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public VariableAddressableDecl createVariableAddressableDecl()
    {
        VariableAddressableDeclImpl variableAddressableDecl = new VariableAddressableDeclImpl();
        return variableAddressableDecl;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public StatementsPackage getStatementsPackage()
    {
        return (StatementsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static StatementsPackage getPackage()
    {
        return StatementsPackage.eINSTANCE;
    }

} //StatementsFactoryImpl
