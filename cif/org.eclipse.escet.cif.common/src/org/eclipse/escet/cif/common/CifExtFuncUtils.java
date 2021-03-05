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

package org.eclipse.escet.cif.common;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.java.Assert;

/** CIF external user-defined functions utility methods. */
public class CifExtFuncUtils {
    /** Constructor for the {@link CifExtFuncUtils} class. */
    private CifExtFuncUtils() {
        // Static class.
    }

    /**
     * Returns the name of the external language, given an external implementation reference.
     *
     * @param extRef The external implementation reference.
     * @return The name of the external language, or {@code ""} if it could not be determined.
     */
    public static String getLangName(String extRef) {
        int idx = extRef.indexOf(':');
        String langName = (idx == -1) ? "" : extRef.substring(0, idx);
        return langName;
    }

    /**
     * Splits an external implementation reference for a Java function into its parts.
     *
     * @param extRef The external implementation reference for a Java function.
     * @return The parts of the external implementation reference:
     *     <ul>
     *     <li>{@code "java"}</li>
     *     <li>The absolute name of the class.</li>
     *     <li>The name of the method.</li>
     *     <li>The class path, or {@code null} if not specified.</li>
     *     </ul>
     * @see #mergeExtJavaRef
     */
    public static String[] splitExtJavaRef(String extRef) {
        // Strip of 'java:' prefix.
        Assert.check(extRef.startsWith("java:"));
        extRef = extRef.substring("java:".length());

        // Separate class/method name and class path.
        int idx = extRef.indexOf('|');
        String methodName = (idx == -1) ? extRef : extRef.substring(0, idx);
        String classPath = (idx == -1) ? null : extRef.substring(idx + 1);

        // Separate class name and method name.
        idx = methodName.lastIndexOf('.');
        String className = (idx == -1) ? "" : methodName.substring(0, idx);
        methodName = (idx == -1) ? methodName : methodName.substring(idx + 1);

        // Return the parts.
        return new String[] {"java", className, methodName, classPath};
    }

    /**
     * Merges an external implementation reference for a Java function from its parts.
     *
     * @param parts The parts.
     * @return The merged external implementation reference.
     * @see #splitExtJavaRef
     */
    public static String mergeExtJavaRef(String[] parts) {
        Assert.check(parts.length == 4);

        String rslt = parts[0] + ":" + parts[1] + "." + parts[2];
        if (parts[3] != null) {
            rslt += "|" + parts[3];
        }
        return rslt;
    }

    /**
     * Splits a Java class path, of an external implementation reference for a Java function, into its entries.
     *
     * @param classPath The Java class path. Must not be {@code null}.
     * @return The entries of the Java class path.
     */
    public static String[] splitJavaClassPathEntries(String classPath) {
        return StringUtils.split(classPath, ";");
    }

    /**
     * Resolves relative paths in an external implementation reference, and replaces them by paths relative to the given
     * directory.
     *
     * @param extRef The external implementation reference.
     * @param funcSpecDir The absolute local file system path of the directory that contains the CIF specification that
     *     contains the external implementation reference.
     * @param relDir The absolute local file system path to the directory to which to make the paths relative.
     * @return The updated external implementation reference.
     */
    public static String updateExtRefRelPaths(String extRef, String funcSpecDir, String relDir) {
        String langName = getLangName(extRef);
        if (langName.equals("java")) {
            // Update relative class path entries.
            String[] parts = splitExtJavaRef(extRef);
            String classPath = parts[3];
            if (classPath == null) {
                return extRef;
            }
            String[] entries = splitJavaClassPathEntries(classPath);
            for (int i = 0; i < entries.length; i++) {
                String entry = entries[i];
                entry = PlatformUriUtils.resolve(entry, funcSpecDir);
                entry = PlatformUriUtils.getRelativePath(entry, relDir);
                entries[i] = entry;
            }
            parts[3] = StringUtils.join(entries, ";");
            return mergeExtJavaRef(parts);
        } else {
            throw new IllegalArgumentException("Uknown language: " + langName);
        }
    }
}
