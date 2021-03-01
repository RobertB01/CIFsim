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
package org.eclipse.escet.cif.metamodel.cif.cifsvg.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.cif.metamodel.cif.IoDecl;

import org.eclipse.escet.cif.metamodel.cif.cifsvg.*;

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
 * @see org.eclipse.escet.cif.metamodel.cif.cifsvg.CifsvgPackage
 * @generated
 */
public class CifsvgSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static CifsvgPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CifsvgSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = CifsvgPackage.eINSTANCE;
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
            case CifsvgPackage.SVG_FILE:
            {
                SvgFile svgFile = (SvgFile)theEObject;
                T result = caseSvgFile(svgFile);
                if (result == null) result = caseIoDecl(svgFile);
                if (result == null) result = casePositionObject(svgFile);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifsvgPackage.SVG_OUT:
            {
                SvgOut svgOut = (SvgOut)theEObject;
                T result = caseSvgOut(svgOut);
                if (result == null) result = caseIoDecl(svgOut);
                if (result == null) result = casePositionObject(svgOut);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifsvgPackage.SVG_IN:
            {
                SvgIn svgIn = (SvgIn)theEObject;
                T result = caseSvgIn(svgIn);
                if (result == null) result = caseIoDecl(svgIn);
                if (result == null) result = casePositionObject(svgIn);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifsvgPackage.SVG_IN_EVENT_SINGLE:
            {
                SvgInEventSingle svgInEventSingle = (SvgInEventSingle)theEObject;
                T result = caseSvgInEventSingle(svgInEventSingle);
                if (result == null) result = caseSvgInEvent(svgInEventSingle);
                if (result == null) result = casePositionObject(svgInEventSingle);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifsvgPackage.SVG_IN_EVENT_IF:
            {
                SvgInEventIf svgInEventIf = (SvgInEventIf)theEObject;
                T result = caseSvgInEventIf(svgInEventIf);
                if (result == null) result = caseSvgInEvent(svgInEventIf);
                if (result == null) result = casePositionObject(svgInEventIf);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifsvgPackage.SVG_IN_EVENT_IF_ENTRY:
            {
                SvgInEventIfEntry svgInEventIfEntry = (SvgInEventIfEntry)theEObject;
                T result = caseSvgInEventIfEntry(svgInEventIfEntry);
                if (result == null) result = casePositionObject(svgInEventIfEntry);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifsvgPackage.SVG_COPY:
            {
                SvgCopy svgCopy = (SvgCopy)theEObject;
                T result = caseSvgCopy(svgCopy);
                if (result == null) result = caseIoDecl(svgCopy);
                if (result == null) result = casePositionObject(svgCopy);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifsvgPackage.SVG_MOVE:
            {
                SvgMove svgMove = (SvgMove)theEObject;
                T result = caseSvgMove(svgMove);
                if (result == null) result = caseIoDecl(svgMove);
                if (result == null) result = casePositionObject(svgMove);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifsvgPackage.SVG_IN_EVENT:
            {
                SvgInEvent svgInEvent = (SvgInEvent)theEObject;
                T result = caseSvgInEvent(svgInEvent);
                if (result == null) result = casePositionObject(svgInEvent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg File</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg File</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgFile(SvgFile object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg Out</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg Out</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgOut(SvgOut object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg In</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg In</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgIn(SvgIn object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg In Event Single</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg In Event Single</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgInEventSingle(SvgInEventSingle object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg In Event If</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg In Event If</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgInEventIf(SvgInEventIf object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg In Event If Entry</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg In Event If Entry</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgInEventIfEntry(SvgInEventIfEntry object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg Copy</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg Copy</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgCopy(SvgCopy object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg Move</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg Move</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgMove(SvgMove object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Svg In Event</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Svg In Event</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSvgInEvent(SvgInEvent object)
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
     * Returns the result of interpreting the object as an instance of '<em>Io Decl</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Io Decl</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIoDecl(IoDecl object)
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

} //CifsvgSwitch
