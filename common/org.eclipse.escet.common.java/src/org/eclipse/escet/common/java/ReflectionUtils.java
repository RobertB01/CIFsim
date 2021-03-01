//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** Reflection related helper methods. */
public final class ReflectionUtils {
    /** Constructor for the {@link ReflectionUtils} class. */
    private ReflectionUtils() {
        // Static class.
    }

    /**
     * Returns the value of a field of an object, using reflection, regardless of any access modifiers.
     *
     * @param <T> The type of the value of the field.
     * @param obj The object for which to retrieve the value of the field.
     * @param fieldName The name of the field for which to retrieve the value.
     * @return The retrieved value of the field.
     *
     * @throws IllegalArgumentException If a field with the given name could not be found, if the field could not be
     *     resolved due to security restrictions, or if the field is unaccessible.
     * @throws ClassCastException If the provided type &lt;T&gt; is incompatible with the actual type of the value of
     *     the field.
     */
    public static <T> T getFieldValue(Object obj, String fieldName) {
        return getFieldValue(obj, obj.getClass(), fieldName);
    }

    /**
     * Returns the value of a field of an object, using reflection, regardless of any access modifiers.
     *
     * @param <T> The type of the value of the field.
     * @param obj The object for which to retrieve the value of the field. If the value of a static field is to be
     *     retrieved, {@code null} may be provided.
     * @param cls The (super) type of 'obj', for which to resolve the field. If the field is not declared in this type
     *     (class), the super type of the type (if any) is recursively checked.
     * @param fieldName The name of the field for which to retrieve the value.
     * @return The retrieved value of the field.
     *
     * @throws IllegalArgumentException If a field with the given name could not be found, if the field could not be
     *     resolved due to security restrictions, or if the field is unaccessible.
     * @throws ClassCastException If the provided type &lt;T&gt; is incompatible with the actual type of the value of
     *     the field.
     */
    public static <T> T getFieldValue(Object obj, Class<?> cls, String fieldName) {
        // Resolve the field.
        Field field;
        try {
            field = cls.getDeclaredField(fieldName);
        } catch (SecurityException e) {
            String msg = fmt("Failed to retrieve field \"%s\" from object: %s", fieldName, obj);
            throw new IllegalArgumentException(msg, e);
        } catch (NoSuchFieldException e) {
            Class<?> superCls = cls.getSuperclass();
            if (superCls == null) {
                String msg = fmt("Failed to retrieve field \"%s\" from object: %s", fieldName, obj);
                throw new IllegalArgumentException(msg, e);
            }
            return getFieldValue(obj, superCls, fieldName);
        }

        // Make sure we can access the field.
        field.setAccessible(true);

        // Get the value of the field.
        Object value;
        try {
            value = field.get(obj);
        } catch (IllegalArgumentException e) {
            String msg = fmt("Failed to retrieve value of field \"%s\" from object: %s", fieldName, obj);
            throw new IllegalArgumentException(msg, e);
        } catch (IllegalAccessException e) {
            String msg = fmt("Failed to retrieve value of field \"%s\" from object: %s", fieldName, obj);
            throw new IllegalArgumentException(msg, e);
        }

        // Return a correctly typed value of the field.
        if (value == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T rslt = (T)value;
        return rslt;
    }

    /**
     * Execute the {@code public static void main(String[] args)} method of the given class.
     *
     * @param cls The class from which to run the 'main' method.
     * @param args Arguments of the 'main' method.
     * @throws RuntimeException If the main method is not found, could not be obtained, has the wrong modifiers, could
     *     not be invoked, etc.
     */
    public static void runMain(Class<?> cls, String[] args) {
        // Obtain 'main' method.
        Method m;
        try {
            m = cls.getMethod("main", new Class[] {args.getClass()});
        } catch (SecurityException e) {
            String msg = "Failed to obtain 'main' method for class: " + cls;
            throw new RuntimeException(msg, e);
        } catch (NoSuchMethodException e) {
            String msg = "Could not find 'main' method for class: " + cls;
            throw new RuntimeException(msg, e);
        }

        // Make sure we have access to the method.
        m.setAccessible(true);

        // Make sure the method is 'public static void'.
        int mods = m.getModifiers();
        if (m.getReturnType() != void.class || !Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
            String msg = "The 'main' method is not 'public static void' for class: " + cls;
            throw new RuntimeException(msg);
        }

        // Invoke the main method.
        try {
            m.invoke(null, new Object[] {args});
        } catch (IllegalAccessException e) {
            // This should not happen, as we have disabled access checks.
            String msg = "Failed to invoke 'main' method for class: " + cls;
            throw new RuntimeException(msg, e);
        } catch (IllegalArgumentException e) {
            String msg = "Failed to invoke 'main' method for class: " + cls;
            throw new RuntimeException(msg, e);
        } catch (InvocationTargetException e) {
            String msg = "Failed to invoke 'main' method for class: " + cls;
            throw new RuntimeException(msg, e);
        }
    }
}
