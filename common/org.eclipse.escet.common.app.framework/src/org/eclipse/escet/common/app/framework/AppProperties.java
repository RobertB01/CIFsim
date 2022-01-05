//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Properties;

/** Application properties. Application framework version of the Java {@link Properties} class. */
public class AppProperties extends LinkedHashMap<String, String> {
    /**
     * Constructor for the {@link AppProperties} class. Uses the result from a call to {@link System#getProperties} to
     * fill the new instance of the {@link AppProperties} class.
     */
    public AppProperties() {
        // Obtain the current system properties.
        Properties sysProps = System.getProperties();

        // Copy them into this instance. We don't want to share options between
        // the running Eclipse environment and this application, and also not
        // between multiple applications. As such, copying is required.
        for (Entry<Object, Object> entry: sysProps.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            put((key instanceof String) ? (String)key : null, (value instanceof String) ? (String)value : null);
        }
    }

    /**
     * Returns a copy of the application properties.
     *
     * @return A copy of the application properties.
     */
    public AppProperties copy() {
        return (AppProperties)clone();
    }

    /**
     * Returns the value of the property with the given name, or {@code null} if the property does not exist.
     *
     * @param propertyName The name of the property to return the value for.
     * @return The value of the property with the given name, or {@code null}.
     */
    public String get(String propertyName) {
        return super.get(propertyName);
    }

    /**
     * Returns the value of the property with the given name, or the given default value if the property does not exist.
     *
     * @param propertyName The name of the property to return the value for.
     * @param defaultValue The default value to return if the property does not exist.
     * @return The value of the property, or the default value if the property does not exist.
     */
    public String get(String propertyName, String defaultValue) {
        String rslt = get(propertyName);
        return (rslt == null) ? defaultValue : rslt;
    }

    /**
     * Sets the property with the specified name to the given value.
     *
     * @param propertyName The name of the property to set.
     * @param value The new value of the property.
     * @return The previous value of the specified property, or {@code null} if it did not have one.
     *
     * @see #put
     */
    public String set(String propertyName, String value) {
        return put(propertyName, value);
    }
}
