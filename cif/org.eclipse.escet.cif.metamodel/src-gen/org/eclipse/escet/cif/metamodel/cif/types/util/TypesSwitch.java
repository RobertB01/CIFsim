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
package org.eclipse.escet.cif.metamodel.cif.types.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.cif.metamodel.cif.types.*;

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
 * @see org.eclipse.escet.cif.metamodel.cif.types.TypesPackage
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
            case TypesPackage.CIF_TYPE:
            {
                CifType cifType = (CifType)theEObject;
                T result = caseCifType(cifType);
                if (result == null) result = casePositionObject(cifType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.BOOL_TYPE:
            {
                BoolType boolType = (BoolType)theEObject;
                T result = caseBoolType(boolType);
                if (result == null) result = caseCifType(boolType);
                if (result == null) result = casePositionObject(boolType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.INT_TYPE:
            {
                IntType intType = (IntType)theEObject;
                T result = caseIntType(intType);
                if (result == null) result = caseCifType(intType);
                if (result == null) result = casePositionObject(intType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.TYPE_REF:
            {
                TypeRef typeRef = (TypeRef)theEObject;
                T result = caseTypeRef(typeRef);
                if (result == null) result = caseCifType(typeRef);
                if (result == null) result = casePositionObject(typeRef);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.ENUM_TYPE:
            {
                EnumType enumType = (EnumType)theEObject;
                T result = caseEnumType(enumType);
                if (result == null) result = caseCifType(enumType);
                if (result == null) result = casePositionObject(enumType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.COMP_PARAM_WRAP_TYPE:
            {
                CompParamWrapType compParamWrapType = (CompParamWrapType)theEObject;
                T result = caseCompParamWrapType(compParamWrapType);
                if (result == null) result = caseCifType(compParamWrapType);
                if (result == null) result = casePositionObject(compParamWrapType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.COMP_INST_WRAP_TYPE:
            {
                CompInstWrapType compInstWrapType = (CompInstWrapType)theEObject;
                T result = caseCompInstWrapType(compInstWrapType);
                if (result == null) result = caseCifType(compInstWrapType);
                if (result == null) result = casePositionObject(compInstWrapType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.COMPONENT_TYPE:
            {
                ComponentType componentType = (ComponentType)theEObject;
                T result = caseComponentType(componentType);
                if (result == null) result = caseCifType(componentType);
                if (result == null) result = casePositionObject(componentType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.COMPONENT_DEF_TYPE:
            {
                ComponentDefType componentDefType = (ComponentDefType)theEObject;
                T result = caseComponentDefType(componentDefType);
                if (result == null) result = caseCifType(componentDefType);
                if (result == null) result = casePositionObject(componentDefType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.REAL_TYPE:
            {
                RealType realType = (RealType)theEObject;
                T result = caseRealType(realType);
                if (result == null) result = caseCifType(realType);
                if (result == null) result = casePositionObject(realType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.STRING_TYPE:
            {
                StringType stringType = (StringType)theEObject;
                T result = caseStringType(stringType);
                if (result == null) result = caseCifType(stringType);
                if (result == null) result = casePositionObject(stringType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.LIST_TYPE:
            {
                ListType listType = (ListType)theEObject;
                T result = caseListType(listType);
                if (result == null) result = caseCifType(listType);
                if (result == null) result = casePositionObject(listType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.SET_TYPE:
            {
                SetType setType = (SetType)theEObject;
                T result = caseSetType(setType);
                if (result == null) result = caseCifType(setType);
                if (result == null) result = casePositionObject(setType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.DICT_TYPE:
            {
                DictType dictType = (DictType)theEObject;
                T result = caseDictType(dictType);
                if (result == null) result = caseCifType(dictType);
                if (result == null) result = casePositionObject(dictType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.TUPLE_TYPE:
            {
                TupleType tupleType = (TupleType)theEObject;
                T result = caseTupleType(tupleType);
                if (result == null) result = caseCifType(tupleType);
                if (result == null) result = casePositionObject(tupleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.FIELD:
            {
                Field field = (Field)theEObject;
                T result = caseField(field);
                if (result == null) result = casePositionObject(field);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.FUNC_TYPE:
            {
                FuncType funcType = (FuncType)theEObject;
                T result = caseFuncType(funcType);
                if (result == null) result = caseCifType(funcType);
                if (result == null) result = casePositionObject(funcType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.DIST_TYPE:
            {
                DistType distType = (DistType)theEObject;
                T result = caseDistType(distType);
                if (result == null) result = caseCifType(distType);
                if (result == null) result = casePositionObject(distType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case TypesPackage.VOID_TYPE:
            {
                VoidType voidType = (VoidType)theEObject;
                T result = caseVoidType(voidType);
                if (result == null) result = caseCifType(voidType);
                if (result == null) result = casePositionObject(voidType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cif Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cif Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCifType(CifType object)
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
     * Returns the result of interpreting the object as an instance of '<em>Enum Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enum Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumType(EnumType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Comp Param Wrap Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Comp Param Wrap Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompParamWrapType(CompParamWrapType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Comp Inst Wrap Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Comp Inst Wrap Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompInstWrapType(CompInstWrapType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentType(ComponentType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Def Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Def Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentDefType(ComponentDefType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Real Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Real Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRealType(RealType object)
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
     * Returns the result of interpreting the object as an instance of '<em>Dict Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dict Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDictType(DictType object)
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
     * Returns the result of interpreting the object as an instance of '<em>Field</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Field</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseField(Field object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Func Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Func Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFuncType(FuncType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dist Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dist Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDistType(DistType object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Void Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Void Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVoidType(VoidType object)
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
