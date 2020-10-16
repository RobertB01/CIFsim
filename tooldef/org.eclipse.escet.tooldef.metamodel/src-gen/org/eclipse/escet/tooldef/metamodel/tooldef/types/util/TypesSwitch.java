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
package org.eclipse.escet.tooldef.metamodel.tooldef.types.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.*;

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
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.types.TypesPackage
 * @generated
 */
public class TypesSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static TypesPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypesSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = TypesPackage.eINSTANCE;
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
            case TypesPackage.TOOL_DEF_TYPE:
            {
                ToolDefType toolDefType = (ToolDefType)theEObject;
                T result = caseToolDefType(toolDefType);
                if (result == null) result = casePositionObject(toolDefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.BOOL_TYPE:
            {
                BoolType boolType = (BoolType)theEObject;
                T result = caseBoolType(boolType);
                if (result == null) result = caseToolDefType(boolType);
                if (result == null) result = casePositionObject(boolType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.INT_TYPE:
            {
                IntType intType = (IntType)theEObject;
                T result = caseIntType(intType);
                if (result == null) result = caseToolDefType(intType);
                if (result == null) result = casePositionObject(intType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.LONG_TYPE:
            {
                LongType longType = (LongType)theEObject;
                T result = caseLongType(longType);
                if (result == null) result = caseToolDefType(longType);
                if (result == null) result = casePositionObject(longType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.DOUBLE_TYPE:
            {
                DoubleType doubleType = (DoubleType)theEObject;
                T result = caseDoubleType(doubleType);
                if (result == null) result = caseToolDefType(doubleType);
                if (result == null) result = casePositionObject(doubleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.STRING_TYPE:
            {
                StringType stringType = (StringType)theEObject;
                T result = caseStringType(stringType);
                if (result == null) result = caseToolDefType(stringType);
                if (result == null) result = casePositionObject(stringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.OBJECT_TYPE:
            {
                ObjectType objectType = (ObjectType)theEObject;
                T result = caseObjectType(objectType);
                if (result == null) result = caseToolDefType(objectType);
                if (result == null) result = casePositionObject(objectType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.LIST_TYPE:
            {
                ListType listType = (ListType)theEObject;
                T result = caseListType(listType);
                if (result == null) result = caseToolDefType(listType);
                if (result == null) result = casePositionObject(listType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.SET_TYPE:
            {
                SetType setType = (SetType)theEObject;
                T result = caseSetType(setType);
                if (result == null) result = caseToolDefType(setType);
                if (result == null) result = casePositionObject(setType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.MAP_TYPE:
            {
                MapType mapType = (MapType)theEObject;
                T result = caseMapType(mapType);
                if (result == null) result = caseToolDefType(mapType);
                if (result == null) result = casePositionObject(mapType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.TUPLE_TYPE:
            {
                TupleType tupleType = (TupleType)theEObject;
                T result = caseTupleType(tupleType);
                if (result == null) result = caseToolDefType(tupleType);
                if (result == null) result = casePositionObject(tupleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.TYPE_REF:
            {
                TypeRef typeRef = (TypeRef)theEObject;
                T result = caseTypeRef(typeRef);
                if (result == null) result = caseToolDefType(typeRef);
                if (result == null) result = casePositionObject(typeRef);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.TYPE_PARAM_REF:
            {
                TypeParamRef typeParamRef = (TypeParamRef)theEObject;
                T result = caseTypeParamRef(typeParamRef);
                if (result == null) result = caseToolDefType(typeParamRef);
                if (result == null) result = casePositionObject(typeParamRef);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.UNRESOLVED_TYPE:
            {
                UnresolvedType unresolvedType = (UnresolvedType)theEObject;
                T result = caseUnresolvedType(unresolvedType);
                if (result == null) result = caseToolDefType(unresolvedType);
                if (result == null) result = casePositionObject(unresolvedType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Def Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Def Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolDefType(ToolDefType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bool Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bool Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBoolType(BoolType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Int Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Int Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIntType(IntType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Long Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Long Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLongType(LongType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Double Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Double Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDoubleType(DoubleType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>String Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>String Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStringType(StringType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Object Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Object Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObjectType(ObjectType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>List Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>List Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseListType(ListType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Set Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Set Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSetType(SetType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Map Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Map Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMapType(MapType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tuple Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tuple Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTupleType(TupleType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type Ref</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type Ref</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTypeRef(TypeRef object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type Param Ref</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type Param Ref</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTypeParamRef(TypeParamRef object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unresolved Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unresolved Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnresolvedType(UnresolvedType object)
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

} //TypesSwitch
