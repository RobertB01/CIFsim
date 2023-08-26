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
package org.eclipse.escet.cif.metamodel.cif.declarations.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;

import org.eclipse.escet.cif.metamodel.cif.declarations.*;

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
 * @see org.eclipse.escet.cif.metamodel.cif.declarations.DeclarationsPackage
 * @generated
 */
public class DeclarationsSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static DeclarationsPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DeclarationsSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = DeclarationsPackage.eINSTANCE;
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
            case DeclarationsPackage.DECLARATION:
            {
                Declaration declaration = (Declaration)theEObject;
                T result = caseDeclaration(declaration);
                if (result == null) result = caseAnnotatedObject(declaration);
                if (result == null) result = casePositionObject(declaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.ALG_VARIABLE:
            {
                AlgVariable algVariable = (AlgVariable)theEObject;
                T result = caseAlgVariable(algVariable);
                if (result == null) result = caseDeclaration(algVariable);
                if (result == null) result = caseAnnotatedObject(algVariable);
                if (result == null) result = casePositionObject(algVariable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.EVENT:
            {
                Event event = (Event)theEObject;
                T result = caseEvent(event);
                if (result == null) result = caseDeclaration(event);
                if (result == null) result = caseAnnotatedObject(event);
                if (result == null) result = casePositionObject(event);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.ENUM_DECL:
            {
                EnumDecl enumDecl = (EnumDecl)theEObject;
                T result = caseEnumDecl(enumDecl);
                if (result == null) result = caseDeclaration(enumDecl);
                if (result == null) result = caseAnnotatedObject(enumDecl);
                if (result == null) result = casePositionObject(enumDecl);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.TYPE_DECL:
            {
                TypeDecl typeDecl = (TypeDecl)theEObject;
                T result = caseTypeDecl(typeDecl);
                if (result == null) result = caseDeclaration(typeDecl);
                if (result == null) result = caseAnnotatedObject(typeDecl);
                if (result == null) result = casePositionObject(typeDecl);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.ENUM_LITERAL:
            {
                EnumLiteral enumLiteral = (EnumLiteral)theEObject;
                T result = caseEnumLiteral(enumLiteral);
                if (result == null) result = casePositionObject(enumLiteral);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.DISC_VARIABLE:
            {
                DiscVariable discVariable = (DiscVariable)theEObject;
                T result = caseDiscVariable(discVariable);
                if (result == null) result = caseDeclaration(discVariable);
                if (result == null) result = caseAnnotatedObject(discVariable);
                if (result == null) result = casePositionObject(discVariable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.VARIABLE_VALUE:
            {
                VariableValue variableValue = (VariableValue)theEObject;
                T result = caseVariableValue(variableValue);
                if (result == null) result = casePositionObject(variableValue);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.CONSTANT:
            {
                Constant constant = (Constant)theEObject;
                T result = caseConstant(constant);
                if (result == null) result = caseDeclaration(constant);
                if (result == null) result = caseAnnotatedObject(constant);
                if (result == null) result = casePositionObject(constant);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.CONT_VARIABLE:
            {
                ContVariable contVariable = (ContVariable)theEObject;
                T result = caseContVariable(contVariable);
                if (result == null) result = caseDeclaration(contVariable);
                if (result == null) result = caseAnnotatedObject(contVariable);
                if (result == null) result = casePositionObject(contVariable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case DeclarationsPackage.INPUT_VARIABLE:
            {
                InputVariable inputVariable = (InputVariable)theEObject;
                T result = caseInputVariable(inputVariable);
                if (result == null) result = caseDeclaration(inputVariable);
                if (result == null) result = caseAnnotatedObject(inputVariable);
                if (result == null) result = casePositionObject(inputVariable);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
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
     * Returns the result of interpreting the object as an instance of '<em>Alg Variable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Alg Variable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAlgVariable(AlgVariable object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Event</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Event</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEvent(Event object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enum Decl</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enum Decl</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumDecl(EnumDecl object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type Decl</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type Decl</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTypeDecl(TypeDecl object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enum Literal</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enum Literal</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumLiteral(EnumLiteral object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Disc Variable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Disc Variable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiscVariable(DiscVariable object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Variable Value</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Variable Value</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVariableValue(VariableValue object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constant</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constant</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstant(Constant object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cont Variable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cont Variable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContVariable(ContVariable object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Input Variable</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Input Variable</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInputVariable(InputVariable object)
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
     * Returns the result of interpreting the object as an instance of '<em>Annotated Object</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Annotated Object</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnnotatedObject(AnnotatedObject object)
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

} //DeclarationsSwitch
