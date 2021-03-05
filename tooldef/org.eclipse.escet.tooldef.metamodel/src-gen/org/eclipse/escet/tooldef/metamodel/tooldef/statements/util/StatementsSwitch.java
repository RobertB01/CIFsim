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
package org.eclipse.escet.tooldef.metamodel.tooldef.statements.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.Declaration;

import org.eclipse.escet.tooldef.metamodel.tooldef.statements.*;

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
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.statements.StatementsPackage
 * @generated
 */
public class StatementsSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static StatementsPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StatementsSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = StatementsPackage.eINSTANCE;
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
            case StatementsPackage.STATEMENT:
            {
                Statement statement = (Statement)theEObject;
                T result = caseStatement(statement);
                if (result == null) result = caseDeclaration(statement);
                if (result == null) result = casePositionObject(statement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.VARIABLE:
            {
                Variable variable = (Variable)theEObject;
                T result = caseVariable(variable);
                if (result == null) result = caseStatement(variable);
                if (result == null) result = caseDeclaration(variable);
                if (result == null) result = casePositionObject(variable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.WHILE_STATEMENT:
            {
                WhileStatement whileStatement = (WhileStatement)theEObject;
                T result = caseWhileStatement(whileStatement);
                if (result == null) result = caseStatement(whileStatement);
                if (result == null) result = caseDeclaration(whileStatement);
                if (result == null) result = casePositionObject(whileStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.BREAK_STATEMENT:
            {
                BreakStatement breakStatement = (BreakStatement)theEObject;
                T result = caseBreakStatement(breakStatement);
                if (result == null) result = caseStatement(breakStatement);
                if (result == null) result = caseDeclaration(breakStatement);
                if (result == null) result = casePositionObject(breakStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.CONTINUE_STATEMENT:
            {
                ContinueStatement continueStatement = (ContinueStatement)theEObject;
                T result = caseContinueStatement(continueStatement);
                if (result == null) result = caseStatement(continueStatement);
                if (result == null) result = caseDeclaration(continueStatement);
                if (result == null) result = casePositionObject(continueStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.RETURN_STATEMENT:
            {
                ReturnStatement returnStatement = (ReturnStatement)theEObject;
                T result = caseReturnStatement(returnStatement);
                if (result == null) result = caseStatement(returnStatement);
                if (result == null) result = caseDeclaration(returnStatement);
                if (result == null) result = casePositionObject(returnStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.EXIT_STATEMENT:
            {
                ExitStatement exitStatement = (ExitStatement)theEObject;
                T result = caseExitStatement(exitStatement);
                if (result == null) result = caseStatement(exitStatement);
                if (result == null) result = caseDeclaration(exitStatement);
                if (result == null) result = casePositionObject(exitStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.FOR_STATEMENT:
            {
                ForStatement forStatement = (ForStatement)theEObject;
                T result = caseForStatement(forStatement);
                if (result == null) result = caseStatement(forStatement);
                if (result == null) result = caseDeclaration(forStatement);
                if (result == null) result = casePositionObject(forStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.IF_STATEMENT:
            {
                IfStatement ifStatement = (IfStatement)theEObject;
                T result = caseIfStatement(ifStatement);
                if (result == null) result = caseStatement(ifStatement);
                if (result == null) result = caseDeclaration(ifStatement);
                if (result == null) result = casePositionObject(ifStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.ASSIGNMENT_STATEMENT:
            {
                AssignmentStatement assignmentStatement = (AssignmentStatement)theEObject;
                T result = caseAssignmentStatement(assignmentStatement);
                if (result == null) result = caseStatement(assignmentStatement);
                if (result == null) result = caseDeclaration(assignmentStatement);
                if (result == null) result = casePositionObject(assignmentStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.TOOL_INVOKE_STATEMENT:
            {
                ToolInvokeStatement toolInvokeStatement = (ToolInvokeStatement)theEObject;
                T result = caseToolInvokeStatement(toolInvokeStatement);
                if (result == null) result = caseStatement(toolInvokeStatement);
                if (result == null) result = caseDeclaration(toolInvokeStatement);
                if (result == null) result = casePositionObject(toolInvokeStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.ELIF_STATEMENT:
            {
                ElifStatement elifStatement = (ElifStatement)theEObject;
                T result = caseElifStatement(elifStatement);
                if (result == null) result = casePositionObject(elifStatement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.ADDRESSABLE_DECL:
            {
                AddressableDecl addressableDecl = (AddressableDecl)theEObject;
                T result = caseAddressableDecl(addressableDecl);
                if (result == null) result = casePositionObject(addressableDecl);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.TUPLE_ADDRESSABLE_DECL:
            {
                TupleAddressableDecl tupleAddressableDecl = (TupleAddressableDecl)theEObject;
                T result = caseTupleAddressableDecl(tupleAddressableDecl);
                if (result == null) result = caseAddressableDecl(tupleAddressableDecl);
                if (result == null) result = casePositionObject(tupleAddressableDecl);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case StatementsPackage.VARIABLE_ADDRESSABLE_DECL:
            {
                VariableAddressableDecl variableAddressableDecl = (VariableAddressableDecl)theEObject;
                T result = caseVariableAddressableDecl(variableAddressableDecl);
                if (result == null) result = caseAddressableDecl(variableAddressableDecl);
                if (result == null) result = casePositionObject(variableAddressableDecl);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStatement(Statement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Variable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Variable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVariable(Variable object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>While Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>While Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWhileStatement(WhileStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Break Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Break Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBreakStatement(BreakStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Continue Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Continue Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContinueStatement(ContinueStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Return Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Return Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReturnStatement(ReturnStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Exit Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Exit Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExitStatement(ExitStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>For Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>For Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForStatement(ForStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfStatement(IfStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assignment Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assignment Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssignmentStatement(AssignmentStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Invoke Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Invoke Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolInvokeStatement(ToolInvokeStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Elif Statement</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Elif Statement</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElifStatement(ElifStatement object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Addressable Decl</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Addressable Decl</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAddressableDecl(AddressableDecl object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tuple Addressable Decl</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tuple Addressable Decl</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTupleAddressableDecl(TupleAddressableDecl object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Variable Addressable Decl</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Variable Addressable Decl</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVariableAddressableDecl(VariableAddressableDecl object)
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

} //StatementsSwitch
