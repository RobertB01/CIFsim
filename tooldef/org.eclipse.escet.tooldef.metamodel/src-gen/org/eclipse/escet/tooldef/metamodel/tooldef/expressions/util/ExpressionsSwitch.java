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
package org.eclipse.escet.tooldef.metamodel.tooldef.expressions.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.*;

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
 * @see org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ExpressionsPackage
 * @generated
 */
public class ExpressionsSwitch<T> extends Switch<T>
{
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ExpressionsPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExpressionsSwitch()
    {
        if (modelPackage == null)
        {
            modelPackage = ExpressionsPackage.eINSTANCE;
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
            case ExpressionsPackage.EXPRESSION:
            {
                Expression expression = (Expression)theEObject;
                T result = caseExpression(expression);
                if (result == null) result = casePositionObject(expression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.TOOL_INVOKE_EXPRESSION:
            {
                ToolInvokeExpression toolInvokeExpression = (ToolInvokeExpression)theEObject;
                T result = caseToolInvokeExpression(toolInvokeExpression);
                if (result == null) result = caseExpression(toolInvokeExpression);
                if (result == null) result = casePositionObject(toolInvokeExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.TOOL_REF:
            {
                ToolRef toolRef = (ToolRef)theEObject;
                T result = caseToolRef(toolRef);
                if (result == null) result = casePositionObject(toolRef);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.BOOL_EXPRESSION:
            {
                BoolExpression boolExpression = (BoolExpression)theEObject;
                T result = caseBoolExpression(boolExpression);
                if (result == null) result = caseExpression(boolExpression);
                if (result == null) result = casePositionObject(boolExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.NUMBER_EXPRESSION:
            {
                NumberExpression numberExpression = (NumberExpression)theEObject;
                T result = caseNumberExpression(numberExpression);
                if (result == null) result = caseExpression(numberExpression);
                if (result == null) result = casePositionObject(numberExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.NULL_EXPRESSION:
            {
                NullExpression nullExpression = (NullExpression)theEObject;
                T result = caseNullExpression(nullExpression);
                if (result == null) result = caseExpression(nullExpression);
                if (result == null) result = casePositionObject(nullExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.DOUBLE_EXPRESSION:
            {
                DoubleExpression doubleExpression = (DoubleExpression)theEObject;
                T result = caseDoubleExpression(doubleExpression);
                if (result == null) result = caseExpression(doubleExpression);
                if (result == null) result = casePositionObject(doubleExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.CAST_EXPRESSION:
            {
                CastExpression castExpression = (CastExpression)theEObject;
                T result = caseCastExpression(castExpression);
                if (result == null) result = caseExpression(castExpression);
                if (result == null) result = casePositionObject(castExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.LIST_EXPRESSION:
            {
                ListExpression listExpression = (ListExpression)theEObject;
                T result = caseListExpression(listExpression);
                if (result == null) result = caseExpression(listExpression);
                if (result == null) result = casePositionObject(listExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.SET_EXPRESSION:
            {
                SetExpression setExpression = (SetExpression)theEObject;
                T result = caseSetExpression(setExpression);
                if (result == null) result = caseExpression(setExpression);
                if (result == null) result = casePositionObject(setExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.MAP_EXPRESSION:
            {
                MapExpression mapExpression = (MapExpression)theEObject;
                T result = caseMapExpression(mapExpression);
                if (result == null) result = caseExpression(mapExpression);
                if (result == null) result = casePositionObject(mapExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.MAP_ENTRY:
            {
                MapEntry mapEntry = (MapEntry)theEObject;
                T result = caseMapEntry(mapEntry);
                if (result == null) result = casePositionObject(mapEntry);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.EMPTY_SET_MAP_EXPRESSION:
            {
                EmptySetMapExpression emptySetMapExpression = (EmptySetMapExpression)theEObject;
                T result = caseEmptySetMapExpression(emptySetMapExpression);
                if (result == null) result = caseExpression(emptySetMapExpression);
                if (result == null) result = casePositionObject(emptySetMapExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.UNRESOLVED_REF_EXPRESSION:
            {
                UnresolvedRefExpression unresolvedRefExpression = (UnresolvedRefExpression)theEObject;
                T result = caseUnresolvedRefExpression(unresolvedRefExpression);
                if (result == null) result = caseExpression(unresolvedRefExpression);
                if (result == null) result = casePositionObject(unresolvedRefExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.TOOL_ARGUMENT:
            {
                ToolArgument toolArgument = (ToolArgument)theEObject;
                T result = caseToolArgument(toolArgument);
                if (result == null) result = casePositionObject(toolArgument);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.VARIABLE_EXPRESSION:
            {
                VariableExpression variableExpression = (VariableExpression)theEObject;
                T result = caseVariableExpression(variableExpression);
                if (result == null) result = caseExpression(variableExpression);
                if (result == null) result = casePositionObject(variableExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.STRING_EXPRESSION:
            {
                StringExpression stringExpression = (StringExpression)theEObject;
                T result = caseStringExpression(stringExpression);
                if (result == null) result = caseExpression(stringExpression);
                if (result == null) result = casePositionObject(stringExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.PROJECTION_EXPRESSION:
            {
                ProjectionExpression projectionExpression = (ProjectionExpression)theEObject;
                T result = caseProjectionExpression(projectionExpression);
                if (result == null) result = caseExpression(projectionExpression);
                if (result == null) result = casePositionObject(projectionExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.SLICE_EXPRESSION:
            {
                SliceExpression sliceExpression = (SliceExpression)theEObject;
                T result = caseSliceExpression(sliceExpression);
                if (result == null) result = caseExpression(sliceExpression);
                if (result == null) result = casePositionObject(sliceExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.TUPLE_EXPRESSION:
            {
                TupleExpression tupleExpression = (TupleExpression)theEObject;
                T result = caseTupleExpression(tupleExpression);
                if (result == null) result = caseExpression(tupleExpression);
                if (result == null) result = casePositionObject(tupleExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.TOOL_PARAM_EXPRESSION:
            {
                ToolParamExpression toolParamExpression = (ToolParamExpression)theEObject;
                T result = caseToolParamExpression(toolParamExpression);
                if (result == null) result = caseExpression(toolParamExpression);
                if (result == null) result = casePositionObject(toolParamExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExpression(Expression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Invoke Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Invoke Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolInvokeExpression(ToolInvokeExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Ref</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Ref</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolRef(ToolRef object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Bool Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Bool Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBoolExpression(BoolExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Number Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Number Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNumberExpression(NumberExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Null Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Null Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNullExpression(NullExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Double Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Double Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDoubleExpression(DoubleExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cast Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cast Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCastExpression(CastExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>List Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>List Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseListExpression(ListExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Set Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Set Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSetExpression(SetExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Map Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Map Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMapExpression(MapExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Map Entry</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Map Entry</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMapEntry(MapEntry object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Empty Set Map Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Empty Set Map Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEmptySetMapExpression(EmptySetMapExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unresolved Ref Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unresolved Ref Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnresolvedRefExpression(UnresolvedRefExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Argument</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Argument</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolArgument(ToolArgument object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Variable Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVariableExpression(VariableExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>String Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>String Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStringExpression(StringExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Projection Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Projection Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseProjectionExpression(ProjectionExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Slice Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Slice Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSliceExpression(SliceExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tuple Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tuple Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTupleExpression(TupleExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tool Param Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tool Param Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseToolParamExpression(ToolParamExpression object)
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

} //ExpressionsSwitch
