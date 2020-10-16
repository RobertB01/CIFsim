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
package org.eclipse.escet.tooldef.metamodel.tooldef.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.*;

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
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.TooldefPackage
 * @generated
 */
public class TooldefSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static TooldefPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TooldefSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = TooldefPackage.eINSTANCE;
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
            case TooldefPackage.SCRIPT:
            {
                Script script = (Script)theEObject;
                T result = caseScript(script);
                if (result == null) result = caseDeclaration(script);
                if (result == null) result = casePositionObject(script);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.DECLARATION:
            {
                Declaration declaration = (Declaration)theEObject;
                T result = caseDeclaration(declaration);
                if (result == null) result = casePositionObject(declaration);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.IMPORT:
            {
                Import import_ = (Import)theEObject;
                T result = caseImport(import_);
                if (result == null) result = caseDeclaration(import_);
                if (result == null) result = casePositionObject(import_);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.TYPE_DECL:
            {
                TypeDecl typeDecl = (TypeDecl)theEObject;
                T result = caseTypeDecl(typeDecl);
                if (result == null) result = caseDeclaration(typeDecl);
                if (result == null) result = casePositionObject(typeDecl);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.TOOL:
            {
                Tool tool = (Tool)theEObject;
                T result = caseTool(tool);
                if (result == null) result = caseDeclaration(tool);
                if (result == null) result = casePositionObject(tool);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.TYPE_PARAM:
            {
                TypeParam typeParam = (TypeParam)theEObject;
                T result = caseTypeParam(typeParam);
                if (result == null) result = casePositionObject(typeParam);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.TOOL_PARAMETER:
            {
                ToolParameter toolParameter = (ToolParameter)theEObject;
                T result = caseToolParameter(toolParameter);
                if (result == null) result = casePositionObject(toolParameter);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.TOOL_DEF_IMPORT:
            {
                ToolDefImport toolDefImport = (ToolDefImport)theEObject;
                T result = caseToolDefImport(toolDefImport);
                if (result == null) result = caseImport(toolDefImport);
                if (result == null) result = caseDeclaration(toolDefImport);
                if (result == null) result = casePositionObject(toolDefImport);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.JAVA_IMPORT:
            {
                JavaImport javaImport = (JavaImport)theEObject;
                T result = caseJavaImport(javaImport);
                if (result == null) result = caseImport(javaImport);
                if (result == null) result = caseDeclaration(javaImport);
                if (result == null) result = casePositionObject(javaImport);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.TOOL_DEF_TOOL:
            {
                ToolDefTool toolDefTool = (ToolDefTool)theEObject;
                T result = caseToolDefTool(toolDefTool);
                if (result == null) result = caseTool(toolDefTool);
                if (result == null) result = caseDeclaration(toolDefTool);
                if (result == null) result = casePositionObject(toolDefTool);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TooldefPackage.JAVA_TOOL:
            {
                JavaTool javaTool = (JavaTool)theEObject;
                T result = caseJavaTool(javaTool);
                if (result == null) result = caseTool(javaTool);
                if (result == null) result = caseDeclaration(javaTool);
                if (result == null) result = casePositionObject(javaTool);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Script</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Script</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseScript(Script object)
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
     * Returns the result of interpreting the object as an instance of '<em>Import</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImport(Import object)
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
     * Returns the result of interpreting the object as an instance of '<em>Tool</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTool(Tool object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type Param</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type Param</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTypeParam(TypeParam object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolParameter(ToolParameter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Def Import</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Def Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolDefImport(ToolDefImport object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Java Import</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Java Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJavaImport(JavaImport object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Def Tool</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Def Tool</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolDefTool(ToolDefTool object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Java Tool</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Java Tool</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJavaTool(JavaTool object)
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

} //TooldefSwitch
