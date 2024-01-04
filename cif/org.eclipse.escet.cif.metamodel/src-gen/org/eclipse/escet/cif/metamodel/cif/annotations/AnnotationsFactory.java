/**
 * Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.escet.cif.metamodel.cif.annotations;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.escet.cif.metamodel.cif.annotations.AnnotationsPackage
 * @generated
 */
public interface AnnotationsFactory extends EFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    AnnotationsFactory eINSTANCE = org.eclipse.escet.cif.metamodel.cif.annotations.impl.AnnotationsFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Annotation</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Annotation</em>'.
     * @generated
     */
    Annotation createAnnotation();

    /**
     * Returns a new object of class '<em>Annotation Argument</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Annotation Argument</em>'.
     * @generated
     */
    AnnotationArgument createAnnotationArgument();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    AnnotationsPackage getAnnotationsPackage();

} //AnnotationsFactory
