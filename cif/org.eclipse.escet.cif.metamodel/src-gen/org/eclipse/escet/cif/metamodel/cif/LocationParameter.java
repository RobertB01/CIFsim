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
package org.eclipse.escet.cif.metamodel.cif;

import org.eclipse.escet.cif.metamodel.cif.automata.Location;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Location Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.escet.cif.metamodel.cif.LocationParameter#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getLocationParameter()
 * @model
 * @generated
 */
public interface LocationParameter extends Parameter
{
    /**
     * Returns the value of the '<em><b>Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Location</em>' containment reference.
     * @see #setLocation(Location)
     * @see org.eclipse.escet.cif.metamodel.cif.CifPackage#getLocationParameter_Location()
     * @model containment="true" required="true"
     * @generated
     */
    Location getLocation();

    /**
     * Sets the value of the '{@link org.eclipse.escet.cif.metamodel.cif.LocationParameter#getLocation <em>Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location</em>' containment reference.
     * @see #getLocation()
     * @generated
     */
    void setLocation(Location value);

} // LocationParameter
