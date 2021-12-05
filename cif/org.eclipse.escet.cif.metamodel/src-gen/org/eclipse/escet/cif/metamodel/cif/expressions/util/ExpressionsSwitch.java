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
package org.eclipse.escet.cif.metamodel.cif.expressions.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.escet.cif.metamodel.cif.expressions.*;

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
 * @see org.eclipse.escet.cif.metamodel.cif.expressions.ExpressionsPackage
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
            case ExpressionsPackage.BINARY_EXPRESSION:
            {
                BinaryExpression binaryExpression = (BinaryExpression)theEObject;
                T result = caseBinaryExpression(binaryExpression);
                if (result == null) result = caseExpression(binaryExpression);
                if (result == null) result = casePositionObject(binaryExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.UNARY_EXPRESSION:
            {
                UnaryExpression unaryExpression = (UnaryExpression)theEObject;
                T result = caseUnaryExpression(unaryExpression);
                if (result == null) result = caseExpression(unaryExpression);
                if (result == null) result = casePositionObject(unaryExpression);
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
            case ExpressionsPackage.INT_EXPRESSION:
            {
                IntExpression intExpression = (IntExpression)theEObject;
                T result = caseIntExpression(intExpression);
                if (result == null) result = caseExpression(intExpression);
                if (result == null) result = casePositionObject(intExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.FUNCTION_CALL_EXPRESSION:
            {
                FunctionCallExpression functionCallExpression = (FunctionCallExpression)theEObject;
                T result = caseFunctionCallExpression(functionCallExpression);
                if (result == null) result = caseExpression(functionCallExpression);
                if (result == null) result = casePositionObject(functionCallExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.IF_EXPRESSION:
            {
                IfExpression ifExpression = (IfExpression)theEObject;
                T result = caseIfExpression(ifExpression);
                if (result == null) result = caseExpression(ifExpression);
                if (result == null) result = casePositionObject(ifExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.DISC_VARIABLE_EXPRESSION:
            {
                DiscVariableExpression discVariableExpression = (DiscVariableExpression)theEObject;
                T result = caseDiscVariableExpression(discVariableExpression);
                if (result == null) result = caseExpression(discVariableExpression);
                if (result == null) result = casePositionObject(discVariableExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.ALG_VARIABLE_EXPRESSION:
            {
                AlgVariableExpression algVariableExpression = (AlgVariableExpression)theEObject;
                T result = caseAlgVariableExpression(algVariableExpression);
                if (result == null) result = caseExpression(algVariableExpression);
                if (result == null) result = casePositionObject(algVariableExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.EVENT_EXPRESSION:
            {
                EventExpression eventExpression = (EventExpression)theEObject;
                T result = caseEventExpression(eventExpression);
                if (result == null) result = caseExpression(eventExpression);
                if (result == null) result = casePositionObject(eventExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.ENUM_LITERAL_EXPRESSION:
            {
                EnumLiteralExpression enumLiteralExpression = (EnumLiteralExpression)theEObject;
                T result = caseEnumLiteralExpression(enumLiteralExpression);
                if (result == null) result = caseExpression(enumLiteralExpression);
                if (result == null) result = casePositionObject(enumLiteralExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.LOCATION_EXPRESSION:
            {
                LocationExpression locationExpression = (LocationExpression)theEObject;
                T result = caseLocationExpression(locationExpression);
                if (result == null) result = caseExpression(locationExpression);
                if (result == null) result = casePositionObject(locationExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.ELIF_EXPRESSION:
            {
                ElifExpression elifExpression = (ElifExpression)theEObject;
                T result = caseElifExpression(elifExpression);
                if (result == null) result = casePositionObject(elifExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.COMP_PARAM_WRAP_EXPRESSION:
            {
                CompParamWrapExpression compParamWrapExpression = (CompParamWrapExpression)theEObject;
                T result = caseCompParamWrapExpression(compParamWrapExpression);
                if (result == null) result = caseExpression(compParamWrapExpression);
                if (result == null) result = casePositionObject(compParamWrapExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.COMP_INST_WRAP_EXPRESSION:
            {
                CompInstWrapExpression compInstWrapExpression = (CompInstWrapExpression)theEObject;
                T result = caseCompInstWrapExpression(compInstWrapExpression);
                if (result == null) result = caseExpression(compInstWrapExpression);
                if (result == null) result = casePositionObject(compInstWrapExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.COMPONENT_EXPRESSION:
            {
                ComponentExpression componentExpression = (ComponentExpression)theEObject;
                T result = caseComponentExpression(componentExpression);
                if (result == null) result = caseExpression(componentExpression);
                if (result == null) result = casePositionObject(componentExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.COMP_PARAM_EXPRESSION:
            {
                CompParamExpression compParamExpression = (CompParamExpression)theEObject;
                T result = caseCompParamExpression(compParamExpression);
                if (result == null) result = caseExpression(compParamExpression);
                if (result == null) result = casePositionObject(compParamExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.CONSTANT_EXPRESSION:
            {
                ConstantExpression constantExpression = (ConstantExpression)theEObject;
                T result = caseConstantExpression(constantExpression);
                if (result == null) result = caseExpression(constantExpression);
                if (result == null) result = casePositionObject(constantExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.TAU_EXPRESSION:
            {
                TauExpression tauExpression = (TauExpression)theEObject;
                T result = caseTauExpression(tauExpression);
                if (result == null) result = caseExpression(tauExpression);
                if (result == null) result = casePositionObject(tauExpression);
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
            case ExpressionsPackage.BASE_FUNCTION_EXPRESSION:
            {
                BaseFunctionExpression baseFunctionExpression = (BaseFunctionExpression)theEObject;
                T result = caseBaseFunctionExpression(baseFunctionExpression);
                if (result == null) result = caseExpression(baseFunctionExpression);
                if (result == null) result = casePositionObject(baseFunctionExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.STD_LIB_FUNCTION_EXPRESSION:
            {
                StdLibFunctionExpression stdLibFunctionExpression = (StdLibFunctionExpression)theEObject;
                T result = caseStdLibFunctionExpression(stdLibFunctionExpression);
                if (result == null) result = caseBaseFunctionExpression(stdLibFunctionExpression);
                if (result == null) result = caseExpression(stdLibFunctionExpression);
                if (result == null) result = casePositionObject(stdLibFunctionExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.REAL_EXPRESSION:
            {
                RealExpression realExpression = (RealExpression)theEObject;
                T result = caseRealExpression(realExpression);
                if (result == null) result = caseExpression(realExpression);
                if (result == null) result = casePositionObject(realExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.TIME_EXPRESSION:
            {
                TimeExpression timeExpression = (TimeExpression)theEObject;
                T result = caseTimeExpression(timeExpression);
                if (result == null) result = caseExpression(timeExpression);
                if (result == null) result = casePositionObject(timeExpression);
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
            case ExpressionsPackage.DICT_EXPRESSION:
            {
                DictExpression dictExpression = (DictExpression)theEObject;
                T result = caseDictExpression(dictExpression);
                if (result == null) result = caseExpression(dictExpression);
                if (result == null) result = casePositionObject(dictExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.DICT_PAIR:
            {
                DictPair dictPair = (DictPair)theEObject;
                T result = caseDictPair(dictPair);
                if (result == null) result = casePositionObject(dictPair);
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
            case ExpressionsPackage.CAST_EXPRESSION:
            {
                CastExpression castExpression = (CastExpression)theEObject;
                T result = caseCastExpression(castExpression);
                if (result == null) result = caseExpression(castExpression);
                if (result == null) result = casePositionObject(castExpression);
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
            case ExpressionsPackage.FIELD_EXPRESSION:
            {
                FieldExpression fieldExpression = (FieldExpression)theEObject;
                T result = caseFieldExpression(fieldExpression);
                if (result == null) result = caseExpression(fieldExpression);
                if (result == null) result = casePositionObject(fieldExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.FUNCTION_EXPRESSION:
            {
                FunctionExpression functionExpression = (FunctionExpression)theEObject;
                T result = caseFunctionExpression(functionExpression);
                if (result == null) result = caseBaseFunctionExpression(functionExpression);
                if (result == null) result = caseExpression(functionExpression);
                if (result == null) result = casePositionObject(functionExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.CONT_VARIABLE_EXPRESSION:
            {
                ContVariableExpression contVariableExpression = (ContVariableExpression)theEObject;
                T result = caseContVariableExpression(contVariableExpression);
                if (result == null) result = caseExpression(contVariableExpression);
                if (result == null) result = casePositionObject(contVariableExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.INPUT_VARIABLE_EXPRESSION:
            {
                InputVariableExpression inputVariableExpression = (InputVariableExpression)theEObject;
                T result = caseInputVariableExpression(inputVariableExpression);
                if (result == null) result = caseExpression(inputVariableExpression);
                if (result == null) result = casePositionObject(inputVariableExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.RECEIVED_EXPRESSION:
            {
                ReceivedExpression receivedExpression = (ReceivedExpression)theEObject;
                T result = caseReceivedExpression(receivedExpression);
                if (result == null) result = caseExpression(receivedExpression);
                if (result == null) result = casePositionObject(receivedExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.SELF_EXPRESSION:
            {
                SelfExpression selfExpression = (SelfExpression)theEObject;
                T result = caseSelfExpression(selfExpression);
                if (result == null) result = caseExpression(selfExpression);
                if (result == null) result = casePositionObject(selfExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.SWITCH_EXPRESSION:
            {
                SwitchExpression switchExpression = (SwitchExpression)theEObject;
                T result = caseSwitchExpression(switchExpression);
                if (result == null) result = caseExpression(switchExpression);
                if (result == null) result = casePositionObject(switchExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ExpressionsPackage.SWITCH_CASE:
            {
                SwitchCase switchCase = (SwitchCase)theEObject;
                T result = caseSwitchCase(switchCase);
                if (result == null) result = casePositionObject(switchCase);
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
     * Returns the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Binary Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBinaryExpression(BinaryExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unary Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unary Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnaryExpression(UnaryExpression object)
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
     * Returns the result of interpreting the object as an instance of '<em>Int Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Int Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIntExpression(IntExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function Call Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function Call Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionCallExpression(FunctionCallExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfExpression(IfExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Disc Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Disc Variable Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDiscVariableExpression(DiscVariableExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Alg Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Alg Variable Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAlgVariableExpression(AlgVariableExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Event Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Event Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEventExpression(EventExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enum Literal Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enum Literal Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumLiteralExpression(EnumLiteralExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Location Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Location Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLocationExpression(LocationExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Elif Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Elif Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElifExpression(ElifExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Comp Param Wrap Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Comp Param Wrap Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompParamWrapExpression(CompParamWrapExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Comp Inst Wrap Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Comp Inst Wrap Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompInstWrapExpression(CompInstWrapExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentExpression(ComponentExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Comp Param Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Comp Param Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCompParamExpression(CompParamExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constant Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constant Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstantExpression(ConstantExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tau Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tau Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTauExpression(TauExpression object)
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
     * Returns the result of interpreting the object as an instance of '<em>Base Function Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Base Function Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBaseFunctionExpression(BaseFunctionExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Std Lib Function Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Std Lib Function Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStdLibFunctionExpression(StdLibFunctionExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Real Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Real Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRealExpression(RealExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Time Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Time Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTimeExpression(TimeExpression object)
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
     * Returns the result of interpreting the object as an instance of '<em>Dict Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dict Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDictExpression(DictExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dict Pair</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dict Pair</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDictPair(DictPair object)
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
     * Returns the result of interpreting the object as an instance of '<em>Field Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Field Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFieldExpression(FieldExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunctionExpression(FunctionExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Cont Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Cont Variable Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContVariableExpression(ContVariableExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Input Variable Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Input Variable Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInputVariableExpression(InputVariableExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Received Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Received Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReceivedExpression(ReceivedExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Self Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Self Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSelfExpression(SelfExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Switch Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Switch Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSwitchExpression(SwitchExpression object)
    {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Switch Case</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Switch Case</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSwitchCase(SwitchCase object)
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
