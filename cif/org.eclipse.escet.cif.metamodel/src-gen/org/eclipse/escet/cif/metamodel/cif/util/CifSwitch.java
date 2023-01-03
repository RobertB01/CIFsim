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
package org.eclipse.escet.cif.metamodel.cif.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.cif.metamodel.cif.*;

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
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage
 * @generated
 */
public class CifSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static CifPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CifSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = CifPackage.eINSTANCE;
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
            case CifPackage.COMPONENT:
            {
                Component component = (Component)theEObject;
                T result = caseComponent(component);
                if (result == null) result = casePositionObject(component);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.GROUP:
            {
                Group group = (Group)theEObject;
                T result = caseGroup(group);
                if (result == null) result = caseComplexComponent(group);
                if (result == null) result = caseComponent(group);
                if (result == null) result = casePositionObject(group);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.COMPONENT_DEF:
            {
                ComponentDef componentDef = (ComponentDef)theEObject;
                T result = caseComponentDef(componentDef);
                if (result == null) result = casePositionObject(componentDef);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.COMPONENT_INST:
            {
                ComponentInst componentInst = (ComponentInst)theEObject;
                T result = caseComponentInst(componentInst);
                if (result == null) result = caseComponent(componentInst);
                if (result == null) result = casePositionObject(componentInst);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.COMPLEX_COMPONENT:
            {
                ComplexComponent complexComponent = (ComplexComponent)theEObject;
                T result = caseComplexComponent(complexComponent);
                if (result == null) result = caseComponent(complexComponent);
                if (result == null) result = casePositionObject(complexComponent);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.SPECIFICATION:
            {
                Specification specification = (Specification)theEObject;
                T result = caseSpecification(specification);
                if (result == null) result = caseGroup(specification);
                if (result == null) result = caseComplexComponent(specification);
                if (result == null) result = caseComponent(specification);
                if (result == null) result = casePositionObject(specification);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.PARAMETER:
            {
                Parameter parameter = (Parameter)theEObject;
                T result = caseParameter(parameter);
                if (result == null) result = casePositionObject(parameter);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.EVENT_PARAMETER:
            {
                EventParameter eventParameter = (EventParameter)theEObject;
                T result = caseEventParameter(eventParameter);
                if (result == null) result = caseParameter(eventParameter);
                if (result == null) result = casePositionObject(eventParameter);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.LOCATION_PARAMETER:
            {
                LocationParameter locationParameter = (LocationParameter)theEObject;
                T result = caseLocationParameter(locationParameter);
                if (result == null) result = caseParameter(locationParameter);
                if (result == null) result = casePositionObject(locationParameter);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.ALG_PARAMETER:
            {
                AlgParameter algParameter = (AlgParameter)theEObject;
                T result = caseAlgParameter(algParameter);
                if (result == null) result = caseParameter(algParameter);
                if (result == null) result = casePositionObject(algParameter);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.COMPONENT_PARAMETER:
            {
                ComponentParameter componentParameter = (ComponentParameter)theEObject;
                T result = caseComponentParameter(componentParameter);
                if (result == null) result = caseParameter(componentParameter);
                if (result == null) result = casePositionObject(componentParameter);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.EQUATION:
            {
                Equation equation = (Equation)theEObject;
                T result = caseEquation(equation);
                if (result == null) result = casePositionObject(equation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.IO_DECL:
            {
                IoDecl ioDecl = (IoDecl)theEObject;
                T result = caseIoDecl(ioDecl);
                if (result == null) result = casePositionObject(ioDecl);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CifPackage.INVARIANT:
            {
                Invariant invariant = (Invariant)theEObject;
                T result = caseInvariant(invariant);
                if (result == null) result = casePositionObject(invariant);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponent(Component object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Group</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Group</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGroup(Group object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Def</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Def</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentDef(ComponentDef object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Inst</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Inst</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentInst(ComponentInst object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Complex Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Complex Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComplexComponent(ComplexComponent object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Specification</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Specification</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecification(Specification object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParameter(Parameter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Event Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Event Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEventParameter(EventParameter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Location Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Location Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLocationParameter(LocationParameter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Alg Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Alg Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAlgParameter(AlgParameter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Parameter</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Parameter</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentParameter(ComponentParameter object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Equation</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Equation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEquation(Equation object)
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
     * Returns the result of interpreting the object as an instance of '<em>Invariant</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Invariant</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInvariant(Invariant object)
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

} //CifSwitch
