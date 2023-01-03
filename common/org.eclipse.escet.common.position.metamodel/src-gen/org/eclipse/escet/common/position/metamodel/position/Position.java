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
package org.eclipse.escet.common.position.metamodel.position;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Position</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.Position#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartLine <em>Start Line</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndOffset <em>End Offset</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartColumn <em>Start Column</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndLine <em>End Line</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndColumn <em>End Column</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartOffset <em>Start Offset</em>}</li>
 *   <li>{@link org.eclipse.escet.common.position.metamodel.position.Position#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition()
 * @model
 * @generated
 */
public interface Position extends EObject
{
    /**
     * Returns the value of the '<em><b>Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' attribute.
     * @see #setSource(String)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition_Source()
     * @model
     * @generated
     */
    String getSource();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.Position#getSource <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' attribute.
     * @see #getSource()
     * @generated
     */
    void setSource(String value);

    /**
     * Returns the value of the '<em><b>Start Line</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start Line</em>' attribute.
     * @see #setStartLine(int)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition_StartLine()
     * @model required="true"
     * @generated
     */
    int getStartLine();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartLine <em>Start Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Line</em>' attribute.
     * @see #getStartLine()
     * @generated
     */
    void setStartLine(int value);

    /**
     * Returns the value of the '<em><b>End Offset</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>End Offset</em>' attribute.
     * @see #setEndOffset(int)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition_EndOffset()
     * @model required="true"
     * @generated
     */
    int getEndOffset();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndOffset <em>End Offset</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End Offset</em>' attribute.
     * @see #getEndOffset()
     * @generated
     */
    void setEndOffset(int value);

    /**
     * Returns the value of the '<em><b>Start Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start Column</em>' attribute.
     * @see #setStartColumn(int)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition_StartColumn()
     * @model required="true"
     * @generated
     */
    int getStartColumn();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartColumn <em>Start Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Column</em>' attribute.
     * @see #getStartColumn()
     * @generated
     */
    void setStartColumn(int value);

    /**
     * Returns the value of the '<em><b>End Line</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>End Line</em>' attribute.
     * @see #setEndLine(int)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition_EndLine()
     * @model required="true"
     * @generated
     */
    int getEndLine();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndLine <em>End Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End Line</em>' attribute.
     * @see #getEndLine()
     * @generated
     */
    void setEndLine(int value);

    /**
     * Returns the value of the '<em><b>End Column</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>End Column</em>' attribute.
     * @see #setEndColumn(int)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition_EndColumn()
     * @model required="true"
     * @generated
     */
    int getEndColumn();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.Position#getEndColumn <em>End Column</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End Column</em>' attribute.
     * @see #getEndColumn()
     * @generated
     */
    void setEndColumn(int value);

    /**
     * Returns the value of the '<em><b>Start Offset</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start Offset</em>' attribute.
     * @see #setStartOffset(int)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition_StartOffset()
     * @model required="true"
     * @generated
     */
    int getStartOffset();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.Position#getStartOffset <em>Start Offset</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Offset</em>' attribute.
     * @see #getStartOffset()
     * @generated
     */
    void setStartOffset(int value);

    /**
     * Returns the value of the '<em><b>Location</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Location</em>' attribute.
     * @see #setLocation(String)
     * @see org.eclipse.escet.common.position.metamodel.position.PositionPackage#getPosition_Location()
     * @model required="true"
     * @generated
     */
    String getLocation();

    /**
     * Sets the value of the '{@link org.eclipse.escet.common.position.metamodel.position.Position#getLocation <em>Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location</em>' attribute.
     * @see #getLocation()
     * @generated
     */
    void setLocation(String value);

} // Position
