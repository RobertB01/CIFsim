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
package org.eclipse.escet.cif.metamodel.cif.functions.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;

import org.eclipse.escet.cif.metamodel.cif.functions.*;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.functions.FunctionsPackage
 * @generated
 */
public class FunctionsSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static FunctionsPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FunctionsSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = FunctionsPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage)
    {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject)
    {
        switch (classifierID)
        {
            case FunctionsPackage.FUNCTION:
            {
                Function function = (Function)theEObject;
                T result = caseFunction(function);
                if (result == null) result = caseDeclaration(function);
                if (result == null) result = casePositionObject(function);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.FUNCTION_PARAMETER:
            {
                FunctionParameter functionParameter = (FunctionParameter)theEObject;
                T result = caseFunctionParameter(functionParameter);
                if (result == null) result = casePositionObject(functionParameter);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.INTERNAL_FUNCTION:
            {
                InternalFunction internalFunction = (InternalFunction)theEObject;
                T result = caseInternalFunction(internalFunction);
                if (result == null) result = caseFunction(internalFunction);
                if (result == null) result = caseDeclaration(internalFunction);
                if (result == null) result = casePositionObject(internalFunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.EXTERNAL_FUNCTION:
            {
                ExternalFunction externalFunction = (ExternalFunction)theEObject;
                T result = caseExternalFunction(externalFunction);
                if (result == null) result = caseFunction(externalFunction);
                if (result == null) result = caseDeclaration(externalFunction);
                if (result == null) result = casePositionObject(externalFunction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.FUNCTION_STATEMENT:
            {
                FunctionStatement functionStatement = (FunctionStatement)theEObject;
                T result = caseFunctionStatement(functionStatement);
                if (result == null) result = casePositionObject(functionStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.BREAK_FUNC_STATEMENT:
            {
                BreakFuncStatement breakFuncStatement = (BreakFuncStatement)theEObject;
                T result = caseBreakFuncStatement(breakFuncStatement);
                if (result == null) result = caseFunctionStatement(breakFuncStatement);
                if (result == null) result = casePositionObject(breakFuncStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.CONTINUE_FUNC_STATEMENT:
            {
                ContinueFuncStatement continueFuncStatement = (ContinueFuncStatement)theEObject;
                T result = caseContinueFuncStatement(continueFuncStatement);
                if (result == null) result = caseFunctionStatement(continueFuncStatement);
                if (result == null) result = casePositionObject(continueFuncStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.ASSIGNMENT_FUNC_STATEMENT:
            {
                AssignmentFuncStatement assignmentFuncStatement = (AssignmentFuncStatement)theEObject;
                T result = caseAssignmentFuncStatement(assignmentFuncStatement);
                if (result == null) result = caseFunctionStatement(assignmentFuncStatement);
                if (result == null) result = casePositionObject(assignmentFuncStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.WHILE_FUNC_STATEMENT:
            {
                WhileFuncStatement whileFuncStatement = (WhileFuncStatement)theEObject;
                T result = caseWhileFuncStatement(whileFuncStatement);
                if (result == null) result = caseFunctionStatement(whileFuncStatement);
                if (result == null) result = casePositionObject(whileFuncStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.IF_FUNC_STATEMENT:
            {
                IfFuncStatement ifFuncStatement = (IfFuncStatement)theEObject;
                T result = caseIfFuncStatement(ifFuncStatement);
                if (result == null) result = caseFunctionStatement(ifFuncStatement);
                if (result == null) result = casePositionObject(ifFuncStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.ELIF_FUNC_STATEMENT:
            {
                ElifFuncStatement elifFuncStatement = (ElifFuncStatement)theEObject;
                T result = caseElifFuncStatement(elifFuncStatement);
                if (result == null) result = casePositionObject(elifFuncStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case FunctionsPackage.RETURN_FUNC_STATEMENT:
            {
                ReturnFuncStatement returnFuncStatement = (ReturnFuncStatement)theEObject;
                T result = caseReturnFuncStatement(returnFuncStatement);
                if (result == null) result = caseFunctionStatement(returnFuncStatement);
                if (result == null) result = casePositionObject(returnFuncStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunction(Function object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionParameter(FunctionParameter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Internal Function</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Internal Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInternalFunction(InternalFunction object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>External Function</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>External Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExternalFunction(ExternalFunction object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionStatement(FunctionStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Break Func Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Break Func Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBreakFuncStatement(BreakFuncStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Continue Func Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Continue Func Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContinueFuncStatement(ContinueFuncStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assignment Func Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assignment Func Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssignmentFuncStatement(AssignmentFuncStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>While Func Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>While Func Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWhileFuncStatement(WhileFuncStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Func Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Func Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfFuncStatement(IfFuncStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Elif Func Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Elif Func Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElifFuncStatement(ElifFuncStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Return Func Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Return Func Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReturnFuncStatement(ReturnFuncStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePositionObject(PositionObject object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Declaration</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Declaration</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDeclaration(Declaration object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object)
    {
        return null;
    }

} //FunctionsSwitch
