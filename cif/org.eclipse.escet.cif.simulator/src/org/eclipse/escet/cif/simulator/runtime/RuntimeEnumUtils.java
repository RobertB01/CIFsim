//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.cif.simulator.runtime;

import java.lang.reflect.Method;

/** Runtime enumeration utility methods. */
public class RuntimeEnumUtils {
    /** Constructor for the {@link RuntimeEnumUtils} class. */
    private RuntimeEnumUtils() {
        // Static class.
    }

    /**
     * Returns the absolute name of the CIF enumeration declaration for which the given enumeration Java class is the
     * runtime implementation.
     *
     * @param enumClass The enumeration Java class.
     * @return The absolute name of the CIF enumeration declaration, with keyword escaping.
     */
    public static String getCifName(Class<? extends Enum<?>> enumClass) {
        // Get 'getCifName' method of runtime enumeration Java class.
        Method method;
        try {
            method = enumClass.getMethod("getCifName");
        } catch (NoSuchMethodException | SecurityException e) {
            String msg = "Failed to get 'getCifName' method: " + enumClass;
            throw new RuntimeException(msg, e);
        }

        // Invoke 'getCifName' method.
        String name;
        try {
            name = (String)method.invoke(null);
        } catch (Throwable e) {
            String msg = "Failed to invoke 'getCifName' method: " + enumClass;
            throw new RuntimeException(msg, e);
        }

        // Return the name of the CIF enumeration.
        return name;
    }
}
