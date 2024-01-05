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
package org.eclipse.escet.cif.metamodel.cif.functions.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.escet.cif.metamodel.cif.functions.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FunctionsFactoryImpl extends EFactoryImpl implements FunctionsFactory
{
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static FunctionsFactory init()
    {
        try
        {
            FunctionsFactory theFunctionsFactory = (FunctionsFactory)EPackage.Registry.INSTANCE.getEFactory(FunctionsPackage.eNS_URI);
            if (theFunctionsFactory != null)
            {
                return theFunctionsFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new FunctionsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FunctionsFactoryImpl()
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
            case FunctionsPackage.FUNCTION_PARAMETER: return createFunctionParameter();
            case FunctionsPackage.INTERNAL_FUNCTION: return createInternalFunction();
            case FunctionsPackage.EXTERNAL_FUNCTION: return createExternalFunction();
            case FunctionsPackage.BREAK_FUNC_STATEMENT: return createBreakFuncStatement();
            case FunctionsPackage.CONTINUE_FUNC_STATEMENT: return createContinueFuncStatement();
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT: return createAssignmentFuncStatement();
            case FunctionsPackage.WHILE_FUNC_STATEMENT: return createWhileFuncStatement();
            case FunctionsPackage.IF_FUNC_STATEMENT: return createIfFuncStatement();
            case FunctionsPackage.ELIF_FUNC_STATEMENT: return createElifFuncStatement();
            case FunctionsPackage.RETURN_FUNC_STATEMENT: return createReturnFuncStatement();
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
    public FunctionParameter createFunctionParameter()
    {
        FunctionParameterImpl functionParameter = new FunctionParameterImpl();
        return functionParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public InternalFunction createInternalFunction()
    {
        InternalFunctionImpl internalFunction = new InternalFunctionImpl();
        return internalFunction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ExternalFunction createExternalFunction()
    {
        ExternalFunctionImpl externalFunction = new ExternalFunctionImpl();
        return externalFunction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public BreakFuncStatement createBreakFuncStatement()
    {
        BreakFuncStatementImpl breakFuncStatement = new BreakFuncStatementImpl();
        return breakFuncStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ContinueFuncStatement createContinueFuncStatement()
    {
        ContinueFuncStatementImpl continueFuncStatement = new ContinueFuncStatementImpl();
        return continueFuncStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public AssignmentFuncStatement createAssignmentFuncStatement()
    {
        AssignmentFuncStatementImpl assignmentFuncStatement = new AssignmentFuncStatementImpl();
        return assignmentFuncStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public WhileFuncStatement createWhileFuncStatement()
    {
        WhileFuncStatementImpl whileFuncStatement = new WhileFuncStatementImpl();
        return whileFuncStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public IfFuncStatement createIfFuncStatement()
    {
        IfFuncStatementImpl ifFuncStatement = new IfFuncStatementImpl();
        return ifFuncStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ElifFuncStatement createElifFuncStatement()
    {
        ElifFuncStatementImpl elifFuncStatement = new ElifFuncStatementImpl();
        return elifFuncStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ReturnFuncStatement createReturnFuncStatement()
    {
        ReturnFuncStatementImpl returnFuncStatement = new ReturnFuncStatementImpl();
        return returnFuncStatement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public FunctionsPackage getFunctionsPackage()
    {
        return (FunctionsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static FunctionsPackage getPackage()
    {
        return FunctionsPackage.eINSTANCE;
    }

} //FunctionsFactoryImpl
