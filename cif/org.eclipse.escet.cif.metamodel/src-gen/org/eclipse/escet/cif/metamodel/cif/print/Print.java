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
package org.eclipse.escet.cif.metamodel.cif.print;

import org.eclipse.emf.common.util.EList;

import org.eclipse.escet.cif.metamodel.cif.IoDecl;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Print</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getTxtPre <em>Txt Pre</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getTxtPost <em>Txt Post</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getWhenPre <em>When Pre</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getWhenPost <em>When Post</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getFile <em>File</em>}</li>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getFors <em>Fors</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrint()
 * @model
 * @generated
 */
public interface Print extends IoDecl
{
    /**
     * Returns the value of the '<em><b>Txt Pre</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Txt Pre</em>' containment reference.
     * @see #setTxtPre(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrint_TxtPre()
     * @model containment="true"
     * @generated
     */
    Expression getTxtPre();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getTxtPre <em>Txt Pre</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Txt Pre</em>' containment reference.
     * @see #getTxtPre()
     * @generated
     */
    void setTxtPre(Expression value);

    /**
     * Returns the value of the '<em><b>Txt Post</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Txt Post</em>' containment reference.
     * @see #setTxtPost(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrint_TxtPost()
     * @model containment="true"
     * @generated
     */
    Expression getTxtPost();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getTxtPost <em>Txt Post</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Txt Post</em>' containment reference.
     * @see #getTxtPost()
     * @generated
     */
    void setTxtPost(Expression value);

    /**
     * Returns the value of the '<em><b>When Pre</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>When Pre</em>' containment reference.
     * @see #setWhenPre(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrint_WhenPre()
     * @model containment="true"
     * @generated
     */
    Expression getWhenPre();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getWhenPre <em>When Pre</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>When Pre</em>' containment reference.
     * @see #getWhenPre()
     * @generated
     */
    void setWhenPre(Expression value);

    /**
     * Returns the value of the '<em><b>When Post</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>When Post</em>' containment reference.
     * @see #setWhenPost(Expression)
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrint_WhenPost()
     * @model containment="true"
     * @generated
     */
    Expression getWhenPost();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getWhenPost <em>When Post</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>When Post</em>' containment reference.
     * @see #getWhenPost()
     * @generated
     */
    void setWhenPost(Expression value);

    /**
     * Returns the value of the '<em><b>File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>File</em>' containment reference.
     * @see #setFile(PrintFile)
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrint_File()
     * @model containment="true"
     * @generated
     */
    PrintFile getFile();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.print.Print#getFile <em>File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File</em>' containment reference.
     * @see #getFile()
     * @generated
     */
    void setFile(PrintFile value);

    /**
     * Returns the value of the '<em><b>Fors</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.escet.cif.metamodel.cif.print.PrintFor}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Fors</em>' containment reference list.
     * @see org.eclipse.escet.cif.metamodel.cif.print.PrintPackage#getPrint_Fors()
     * @model containment="true"
     * @generated
     */
    EList<PrintFor> getFors();

} // Print
