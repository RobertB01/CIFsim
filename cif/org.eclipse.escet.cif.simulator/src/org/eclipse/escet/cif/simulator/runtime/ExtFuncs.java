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

package org.eclipse.escet.cif.simulator.runtime;

import static org.eclipse.escet.cif.common.CifExtFuncUtils.splitJavaClassPathEntries;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.escet.cif.simulator.CifSimulatorContext;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;

/** Runtime support for external user-defined functions. */
public class ExtFuncs {
    /** The time in milliseconds between polls for simulation termination. */
    private static final int TERMINATE_POLL_TIME = 100;

    /** Constructor for the {@link ExtFuncs} class. */
    private ExtFuncs() {
        // Static class.
    }

    /**
     * Loads a static Java method for an external user-defined Java function.
     *
     * @param cifFuncName The absolute name of the CIF external user-defined function.
     * @param className The name of the class that contains the function, including its package (if any).
     * @param methodName The name of the method.
     * @param classPath The class path, as absolute or relative local file system paths to directories or JAR files.
     *     Paths may use either {@code \} or {@code /} as path separators. Class path entries are to be separated by
     *     {@code ;} characters. If {@code null}, the system class path is used, otherwise the system class path is
     *     extended with the given class path entries.
     * @param workingDir The absolute local file system path against which to resolve relative class path entries.
     * @param paramTypes The types of the parameters of the Java method.
     * @param expReturnType The expected return type of the Java method. The actual return type must be assignable to
     *     this type.
     * @return The resolved static Java method.
     * @throws CifSimulatorException If loading fails.
     */
    public static Method loadJavaMethod(String cifFuncName, String className, String methodName, String classPath,
            String workingDir, Class<?>[] paramTypes, Class<?> expReturnType)
    {
        try {
            // Get class loader.
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();

            if (classPath != null) {
                // Process class path.
                String[] classPathParts = splitJavaClassPathEntries(classPath);
                URL[] classPathUrls = new URL[classPathParts.length];
                for (int i = 0; i < classPathParts.length; i++) {
                    // Resolve to absolute path, and convert to URL.
                    String classPathPart = classPathParts[i];
                    classPathPart = Paths.resolve(classPathPart, workingDir);
                    File classPartFile = new File(classPathPart);
                    Assert.check(classPartFile.isAbsolute());
                    try {
                        classPathUrls[i] = classPartFile.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }

                    // Make sure the path (directory or JAR file) exists.
                    if (!classPartFile.exists()) {
                        String msg = fmt("Invalid class path entry \"%s\": the path of the entry does not exist.",
                                classPathPart);
                        throw new CifSimulatorException(msg);
                    }
                }

                // Construct extended class loader.
                classLoader = new URLClassLoader(classPathUrls, classLoader);
            }

            // Load class.
            Class<?> extClass;
            try {
                extClass = classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                String msg = fmt("Class \"%s\" could not be found.", className);
                throw new CifSimulatorException(msg, e);
            }

            // Get method.
            Method extMethod;
            try {
                extMethod = extClass.getDeclaredMethod(methodName, paramTypes);
            } catch (NoSuchMethodException e) {
                String msg = fmt("Class \"%s\" has no method \"%s\", or the arguments of the method are incompatible.",
                        className, methodName);
                throw new CifSimulatorException(msg, e);
            }

            // Make sure method is static.
            if (!Modifier.isStatic(extMethod.getModifiers())) {
                String msg = fmt("Method \"%s\" of class \"%s\" is not static.", methodName, className);
                throw new CifSimulatorException(msg);
            }

            // Check return type.
            Class<?> actualReturnType = extMethod.getReturnType();
            if (actualReturnType == boolean.class) {
                actualReturnType = Boolean.class;
            } else if (actualReturnType == int.class) {
                actualReturnType = Integer.class;
            } else if (actualReturnType == double.class) {
                actualReturnType = Double.class;
            }

            if (!expReturnType.isAssignableFrom(actualReturnType)) {
                String msg = fmt("Method \"%s\" has a \"%s\" return type, while a \"%s\" type is expected.",
                        extMethod.toGenericString(), actualReturnType.getName(), expReturnType.getName());
                throw new CifSimulatorException(msg);
            }

            // Set accessible, to allow using for instance private methods.
            extMethod.setAccessible(true);

            // Return the loaded method.
            return extMethod;
        } catch (CifSimulatorException e) {
            String msg = fmt("Failed to load external user-defined function \"%s\".", cifFuncName);
            throw new CifSimulatorException(msg, e);
        }
    }

    /**
     * Invokes a static Java method for an external user-defined Java function, asynchronously on the new thread. If
     * {@link CifSimulatorContext#extFuncAsync} is disabled, the function is executed synchronously on the same thread
     * as the calling code.
     *
     * @param ctxt The simulator runtime context.
     * @param method The static method.
     * @param args The method arguments.
     * @return The method result.
     * @throws CifSimulatorException If execution of the method fails.
     */
    public static Object invokeJavaMethodAsync(final CifSimulatorContext ctxt, final Method method,
            final Object... args)
    {
        // Test mode.
        if (!ctxt.extFuncAsync) {
            return invokeJavaMethodSync(method, args);
        }

        // Thread data.
        final Object[] rslt = new Object[1];
        final Throwable[] ex = new Throwable[1];
        final AtomicBoolean done = new AtomicBoolean(false);

        // Construct method invocation thread.
        final int INVOKE = 0;
        final int OBSERVER = 1;
        final Thread[] threads = new Thread[2];
        threads[INVOKE] = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rslt[0] = invokeJavaMethodInternal(method, args);
                } catch (ThreadDeath e) {
                    // Ignore the thread being killed by the observer.
                } catch (InvocationTargetException e) {
                    // Invocation/execution failure.
                    ex[0] = e;
                } catch (ExceptionInInitializerError e) {
                    // Invocation/execution failure.
                    ex[0] = e;
                } finally {
                    // Invocation done, either successful or with error.
                    done.set(true);
                    threads[OBSERVER].interrupt();
                }
            }
        });

        // Construct termination observer thread.
        threads[OBSERVER] = new Thread(new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                while (true) {
                    // Check for finished execution.
                    if (done.get()) {
                        return;
                    }

                    // Check for termination.
                    if (ctxt.appEnvData.isTerminationRequested()) {
                        // Kill thread, and stop observing.
                        threads[INVOKE].stop();
                        return;
                    }

                    // Wait a bit before polling again.
                    try {
                        Thread.sleep(TERMINATE_POLL_TIME);
                    } catch (InterruptedException e) {
                        // Ignore the thread being interrupted, signaling that
                        // invocation is done.
                    }
                }
            }
        });

        // Start threads.
        threads[INVOKE].setName("CifExtFuncInvoke: " + method.toString());
        threads[INVOKE].start();

        threads[OBSERVER].setName("CifExtFuncObserver: " + method.toString());
        threads[OBSERVER].start();

        // Wait for threads to complete.
        try {
            threads[INVOKE].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            threads[OBSERVER].join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Check for termination.
        ctxt.checkTermination();

        // Check result.
        if (ex[0] == null) {
            // Invocation successful.
            return rslt[0];
        }

        // Report error.
        reportJavaMethodInvokeError(ex[0]);
        return null; // Never reached.
    }

    /**
     * Invokes a static Java method for an external user-defined Java function, synchronously on the same thread as the
     * calling code. Exceptions are not caught and should be handled by the caller.
     *
     * @param method The static method.
     * @param args The method arguments.
     * @return The method result.
     * @throws CifSimulatorException If execution of the method fails.
     */
    public static Object invokeJavaMethodSync(Method method, Object... args) {
        try {
            return invokeJavaMethodInternal(method, args);
        } catch (InvocationTargetException e) {
            // Invocation/execution failure.
            reportJavaMethodInvokeError(e);
            return null; // Never reached.
        } catch (ExceptionInInitializerError e) {
            // Invocation/execution failure.
            reportJavaMethodInvokeError(e);
            return null; // Never reached.
        }
    }

    /**
     * Invokes a static Java method for an external user-defined Java function, synchronously on the same thread as the
     * calling code. Exceptions are not caught and should be handled by the caller.
     *
     * @param method The static method.
     * @param args The method arguments.
     * @return The method result.
     * @throws InvocationTargetException If execution of the method fails.
     * @throws ExceptionInInitializerError If execution fails due to a failure in static initialization.
     */
    protected static Object invokeJavaMethodInternal(Method method, Object... args) throws InvocationTargetException {
        // Invoke via reflection.
        try {
            return method.invoke(null, args);
        } catch (IllegalArgumentException e) {
            // Unexpected, as we checked for compatible arguments, etc.
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            // Unexpected, as we set the method to be accessible.
            throw new RuntimeException(e);
        }
    }

    /**
     * Reports an error that occurred during invocation of an external user-defined Java function to the simulator.
     *
     * @param ex The exception that occurred. Must not be {@code null}.
     */
    protected static void reportJavaMethodInvokeError(Throwable ex) {
        Assert.notNull(ex);

        // Problem during invocation. Print stack trace, to allow debugging of
        // external user-defined Java functions.
        AppStream errStream = AppEnv.getStreams().err;
        errStream.println("ERROR: Java method execution failed:");
        errStream.printStackTrace(ex);
        errStream.println();
        errStream.flush();

        // Simulation fails.
        String msg = "Java method execution failed.";
        throw new CifSimulatorException(msg, ex);
    }

    /**
     * Checks a return value of a Java function for {@code null} values. Note that only the top level object is checked.
     * That is, elements of lists etc are not recursively checked.
     *
     * @param value The value to check.
     * @throws CifSimulatorException If the value is {@code null}.
     */
    public static void checkJavaNullReturn(Object value) {
        if (value == null) {
            String msg = "The return value of the external Java function contains a \"null\" value.";
            throw new CifSimulatorException(msg);
        }
    }

    /**
     * Checks a return value of a Java function for illegal double values.
     *
     * @param value The value to check.
     * @throws CifSimulatorException If the value is {@code NaN}, {@code -inf}, or {@code +inf}.
     */
    public static void checkJavaDoubleReturn(double value) {
        if (Double.isNaN(value)) {
            String msg = "The return value of the external Java function contains a NaN value.";
            throw new CifSimulatorException(msg);
        } else if (value == Double.NEGATIVE_INFINITY) {
            String msg = "The return value of the external Java function contains a -inf value.";
            throw new CifSimulatorException(msg);
        } else if (value == Double.POSITIVE_INFINITY) {
            String msg = "The return value of the external Java function contains a +inf value.";
            throw new CifSimulatorException(msg);
        }
    }

    /**
     * Throws a {@link CifSimulatorException} indicating that (a part of) the return value of a Java function has a
     * wrong type.
     *
     * @param value The (part of the) value to that has the wrong type.
     * @param expected The expected type.
     * @throws CifSimulatorException Always thrown.
     */
    public static void checkJavaRetTypeFailed(Object value, Class<?> expected) {
        String msg = fmt("The return value of the external Java function contains a value of type \"%s\", "
                + "while a value of type \"%s\" was expected.", value.getClass().getName(), expected.getName());
        throw new CifSimulatorException(msg);
    }

    /**
     * Throws a {@link CifSimulatorException} indicating that (a part of) the return value of a Java function has an out
     * of range integer value.
     *
     * @param value The (part of the) value to that is out of range.
     * @param lower The lower bound of the integer type range.
     * @param upper The upper bound of the integer type range.
     * @throws CifSimulatorException Always thrown.
     */
    public static void checkJavaIntRangeFailed(int value, int lower, int upper) {
        String msg = fmt("The return value of the external Java function contains integer value %d, while a value "
                + "of type \"int[%d .. %d]\" was expected.", value, lower, upper);
        throw new CifSimulatorException(msg);
    }

    /**
     * Throws a {@link CifSimulatorException} indicating that (a part of) the return value of a Java function has a list
     * that has too few or too many elements.
     *
     * @param value The (part of the) value to that is the problematic list.
     * @param lower The lower bound of the integer type range.
     * @param upper The upper bound of the integer type range.
     * @throws CifSimulatorException Always thrown.
     */
    public static void checkJavaListRangeFailed(List<?> value, int lower, int upper) {
        String typeRange;
        if (lower == upper) {
            typeRange = str(lower);
        } else {
            typeRange = fmt("%d..%d", lower, upper);
        }
        String msg = fmt("The return value of the external Java function contains a list with %d element(s), "
                + "while a list of type \"list[%s]\" was expected.", value.size(), typeRange);
        throw new CifSimulatorException(msg);
    }
}
