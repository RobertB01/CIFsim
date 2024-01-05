//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.escet.common.java.exceptions.ApplicationException;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * An Eclipse {@link IApplication} that supports running any application framework {@link Application application}.
 *
 * <p>
 * The following command line arguments are expected for the {@link IApplication}:
 * <ul>
 * <li>The name of the plug-in (OSGi bundle) that provides the application.</li>
 * <li>The full/absolute name of the Java class that implements the application. Must extend {@link Application} and
 * have a parameterless constructor.</li>
 * <li>The remaining command line arguments are the command line arguments for the application itself.</li>
 * </ul>
 * </p>
 */
public class AppEclipseApplication implements IApplication {
    @Override
    public Object start(IApplicationContext context) throws Exception {
        // Get command line arguments.
        Map<?, ?> args = context.getArguments();
        String[] appArgs = (String[])args.get("application.args");

        // Check application arguments.
        if (appArgs == null || appArgs.length < 2) {
            throw new ApplicationException(
                    "Invalid command line arguments. Expected: <plugin_name> <class_name> [application_arguments]");
        }

        // Split application arguments.
        String bundleName = appArgs[0];
        String className = appArgs[1];
        String[] cmdLineArgs = Arrays.copyOfRange(appArgs, 2, appArgs.length);

        // Get OSGi bundle.
        Bundle bundle = Platform.getBundle(bundleName);
        if (bundle == null) {
            String msg = fmt("OSGi bundle \"%s\" not found.", bundleName);
            throw new ApplicationException(msg);
        }

        // Make sure plug-in (OSGi bundle) is in proper state.
        int state = bundle.getState();
        boolean stateOk = state == Bundle.RESOLVED || state == Bundle.STARTING || state == Bundle.ACTIVE;
        if (!stateOk) {
            String msg = fmt("OSGi bundle \"%s\" is not in state RESOLVED, STARTING or ACTIVE, but in state %s.",
                    bundleName, PlatformUtils.getStateName(bundle));
            throw new ApplicationException(msg);
        }

        // Get bundle wiring.
        BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
        if (bundleWiring == null) {
            String msg = fmt("Failed to obtain bundle wiring from OSGi bundle \"%s\".", bundleName);
            throw new ApplicationException(msg);
        }

        // Get class loader.
        ClassLoader classLoader = bundleWiring.getClassLoader();
        if (classLoader == null) {
            String msg = fmt("Failed to class loader from OSGi bundle \"%s\".", bundleName);
            throw new ApplicationException(msg);
        }

        // Load the application class.
        Class<?> cls;
        try {
            cls = classLoader.loadClass(className);
        } catch (ClassNotFoundException ex) {
            String msg = fmt("Failed to load/find class \"%s\" for OSGi bundle \"%s\".", className, bundleName);
            throw new ApplicationException(msg, ex);
        }

        // Make sure it is an application framework application class.
        Class<Application<?>> appClass;
        try {
            @SuppressWarnings("unchecked")
            Class<Application<?>> tmpClass = (Class<Application<?>>)cls;
            appClass = tmpClass;
        } catch (ClassCastException ex) {
            String msg = fmt("Class \"%s\" is not an application framework application class.", className);
            throw new ApplicationException(msg, ex);
        }

        // Find constructor.
        final Constructor<Application<?>> appConstructor;
        try {
            appConstructor = appClass.getConstructor();
        } catch (SecurityException ex) {
            String msg = fmt("Failed to obtain constructor for class \"%s\" due to security settings.",
                    appClass.getName());
            throw new RuntimeException(msg, ex);
        } catch (NoSuchMethodException ex) {
            String msg = fmt("Application class \"%s\" is missing a parameterless constructor.", appClass.getName());
            throw new RuntimeException(msg, ex);
        }

        // Construct application.
        Application<?> app = appConstructor.newInstance();

        // Suppress non-zero exit code being reported by the Eclipse launcher.
        System.setProperty(IApplicationContext.EXIT_DATA_PROPERTY, "");

        // Run application.
        return app.run(cmdLineArgs, false);
    }

    @Override
    public void stop() {
        // Not applicable.
    }
}
