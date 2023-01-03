//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.common;

import static org.eclipse.escet.common.java.Lists.list;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.common.java.MultiClassLoader;
import org.eclipse.escet.common.java.UncachedUrlClassLoader;
import org.eclipse.escet.common.java.UncachedUrlClassLoader.OpenUrlException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * Plug-in class loader obtainer. Supports running inside the Eclipse/OSGi platform, with or without Workbench/UI, as
 * well as outside it (command line tools). It supports both actual bundles and workspace JDT/PDE projects, when running
 * inside the Eclipse/OSGi framework with the Workbench/UI running as well.
 */
public abstract class ClassLoaderObtainer {
    /**
     * Obtains and returns a class loader for a plug-in.
     *
     * <p>
     * If running outside the Eclipse/OSGi platform, the {@link ClassLoader#getSystemClassLoader system class loader} is
     * returned instead. It is also possible to force using the system class loader, by providing {@code null} as
     * plug-in name.
     * </p>
     *
     * <p>
     * If running inside the Eclipse/OSGi framework, OSGi bundles are supported. If the Workbench/UI is also available,
     * workspace JDT/PDE projects are supported as well. If a workspace project exists, it takes precedence over any
     * OSGi bundles with the same name.
     * </p>
     *
     * @param name The name of the plug-in (OSGi bundle) for which to obtain the class loader. Ignored if the
     *     Eclipse/OSGi framework is not running. May be {@code null} to always ignore.
     * @return The class loader.
     */
    public ClassLoader getClassLoader(String name) {
        // Get class loader.
        if (!Platform.isRunning()) {
            // Eclipse/OSGi isn't running, so no need to use plug-in names.
            // Always use the system class loader. All Java code and resources
            // that need to be loaded must be on the class path.
            return ClassLoader.getSystemClassLoader();
        } else if (name == null) {
            // Use system class loader. Used primarily to load standard Java
            // classes, such as 'java.lang.String'.
            return ClassLoader.getSystemClassLoader();
        } else {
            // Resolve the plug-in, using PDE/JDT, if Workbench/UI running.
            if (PlatformUI.isWorkbenchRunning()) {
                if (Path.ROOT.isValidSegment(name)) {
                    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                    IProject proj = root.getProject(name);
                    if (proj.exists() && proj.isAccessible()) {
                        return getPdeClassLoader(proj);
                    }
                }
            }

            // Resolve the plug-in, using OSGi.
            return getOsgiClassLoader(name);
        }
    }

    /**
     * Obtains and returns a class loader for a plug-in (Eclipse project), using PDE/JDT.
     *
     * <p>
     * Requires running inside the Eclipse/OSGi framework.
     * </p>
     *
     * @param proj The Eclipse project for which to obtain the class loader.
     * @return The class loader.
     */
    private ClassLoader getPdeClassLoader(IProject proj) {
        // JDT class loader does only direct dependencies. It doesn't do
        // dependencies of the dependencies. That is what PDE handles.

        // Get the PDE plug-in model for the Eclipse project.
        IPluginModelBase plugin = PluginRegistry.findModel(proj);
        if (plugin == null) {
            errNotPluginProj(proj.getName());
            return null; // Never reached.
        }

        // Get the bundle description.
        BundleDescription descr = plugin.getBundleDescription();
        if (descr == null) {
            errPluginClassicFormat(proj.getName());
            return null; // Never reached.
        }

        // Get class loader for dependencies (required bundles).
        List<ClassLoader> reqsLoaders = list();
        for (BundleSpecification reqBundle: descr.getRequiredBundles()) {
            // Get plug-in name of the dependency (required bundle).
            String reqName = reqBundle.getName();

            // Get and add class loader for the plug-in.
            ClassLoader reqLoader = getClassLoader(reqName);
            reqsLoaders.add(reqLoader);
        }

        // Get dependency class loader. If no dependencies, use system class
        // loader, otherwise use all the class loaders of all the dependencies.
        ClassLoader reqsLoader = reqsLoaders.isEmpty() ? ClassLoader.getSystemClassLoader()
                : new MultiClassLoader(reqsLoaders);

        // Use JDT loader for the plug-in itself, with fall back to dependency
        // loaders.
        return getJdtClassLoader(proj, reqsLoader);
    }

    /**
     * A problem occurred while getting a class loader for a plug-in: plug-in (Eclipse project) with the given name is
     * not a plug-in project, or contains a manifest file that is malformed or missing vital information.
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * @param name The name of the plug-in (Eclipse project). Is not {@code null}.
     */
    protected abstract void errNotPluginProj(String name);

    /**
     * A problem occurred while getting a class loader for a plug-in: plug-in (Eclipse project) with the given name is
     * in a classic format (does not use the new OSGi bundle layout).
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * @param name The name of the plug-in (Eclipse project). Is not {@code null}.
     */
    protected abstract void errPluginClassicFormat(String name);

    /**
     * Obtains and returns a class loader for a plug-in (Eclipse project), using JDT.
     *
     * <p>
     * Requires running inside the Eclipse/OSGi framework.
     * </p>
     *
     * @param proj The Eclipse project for which to obtain the class loader.
     * @param parent The parent class loader.
     * @return The class loader.
     */
    private ClassLoader getJdtClassLoader(IProject proj, ClassLoader parent) {
        // JDT class loader does only direct dependencies. It doesn't do
        // dependencies of the dependencies. That is what PDE handles.

        // Get Java project. Succeeds even if project has no Java nature.
        IJavaProject jproj = JavaCore.create(proj);

        // Get runtime class path of the Java project.
        String[] cp;
        try {
            cp = JavaRuntime.computeDefaultRuntimeClassPath(jproj);
        } catch (CoreException ex) {
            errComputeClassPath(proj.getName(), ex);
            return null; // Never reached.
        }

        // Construct URL class loader with runtime class path.
        List<URL> urlList = list();
        for (int i = 0; i < cp.length; i++) {
            IPath path = new Path(cp[i]);
            URL url;
            try {
                url = path.toFile().toURI().toURL();
            } catch (MalformedURLException ex) {
                errMalformedUrl(proj.getName(), ex);
                return null; // Never reached.
            }
            urlList.add(url);
        }

        // Construct URL class loader with runtime class path. We use an
        // uncached URL class loader to avoid caching workspace code. This way,
        // we can always load the latest code.
        URL[] urls = urlList.toArray(new URL[urlList.size()]);
        try {
            return new UncachedUrlClassLoader(urls, parent);
        } catch (OpenUrlException ex) {
            errOpenUrl(proj.getName(), ex);
            return null; // Never reached.
        }
    }

    /**
     * A problem occurred while getting a class loader for a plug-in: could not compute the class path for a plug-in
     * (Eclipse project) with the given name. The plug-in may not have a Java nature.
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * @param name The name of the plug-in (Eclipse project). Is not {@code null}.
     * @param ex The exception that indicates what is wrong with the URL.
     */
    protected abstract void errComputeClassPath(String name, CoreException ex);

    /**
     * A problem occurred while getting a class loader for a plug-in: URL that is in the class path of the plug-in
     * (Eclipse project) with the given name is malformed.
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * @param name The name of the plug-in (Eclipse project). Is not {@code null}.
     * @param ex The exception that indicates what is wrong with the URL.
     */
    protected abstract void errMalformedUrl(String name, MalformedURLException ex);

    /**
     * A problem occurred while getting a class loader for a plug-in: could not open an URL that is in the class path of
     * the plug-in (Eclipse project) with the given name.
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * @param name The name of the plug-in (Eclipse project). Is not {@code null}.
     * @param ex The exception that indicates what URL failed to open ({@link OpenUrlException#url}), and why
     *     ({@link Exception#getCause}).
     */
    protected abstract void errOpenUrl(String name, OpenUrlException ex);

    /**
     * Obtains and returns a class loader for a plug-in (OSGi bundle), using OSGi.
     *
     * <p>
     * Requires running inside the Eclipse/OSGi framework.
     * </p>
     *
     * @param name The name of the plug-in (OSGi bundle) for which to obtain the class loader. Must not be {@code null}.
     * @return The class loader.
     */
    private ClassLoader getOsgiClassLoader(String name) {
        // Get OSGi bundle.
        Bundle bundle = Platform.getBundle(name);
        if (bundle == null) {
            errNotFound(name);
            return null; // Never reached.
        }

        // Make sure plug-in (OSGi bundle) is in proper state.
        int state = bundle.getState();
        boolean stateOk = state == Bundle.RESOLVED || state == Bundle.STARTING || state == Bundle.ACTIVE;
        if (!stateOk) {
            errWrongState(name, bundle, state);
            return null; // Never reached.
        }

        // Get class loader from bundle.
        BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
        if (bundleWiring == null) {
            errNoWiring(name, bundle);
            return null; // Never reached.
        }

        ClassLoader classLoader = bundleWiring.getClassLoader();
        if (classLoader == null) {
            errNoClassLoader(name, bundle);
            return null; // Never reached.
        }

        // Return the resolved class loader.
        return classLoader;
    }

    /**
     * A problem occurred while getting a class loader for a plug-in: a plug-in (OSGi bundle or Eclipse project) with
     * the given name could not be found.
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * @param name The name of the plug-in (OSGi bundle or Eclipse project). Is not {@code null}.
     */
    protected abstract void errNotFound(String name);

    /**
     * A problem occurred while getting a class loader for a plug-in: plug-in (OSGi bundle) with the given name is in a
     * wrong state (it should be in state RESOLVED, STARTING, or ACTIVE).
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * @param name The name of the plug-in (OSGi bundle). Is not {@code null}.
     * @param bundle The plug-in (OSGi bundle).
     * @param state The state of the plug-in (OSGi bundle). See constants in the {@link Bundle} interface.
     */
    protected abstract void errWrongState(String name, Bundle bundle, int state);

    /**
     * A problem occurred while getting a class loader for a plug-in: a class loader could not be obtained from the
     * plug-in (OSGi bundle) with the given name, as obtaining the bundle wiring from that plug-in failed.
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * <p>
     * By default, this method invokes {@link #errNoClassLoader}.
     * </p>
     *
     * @param name The name of the plug-in (OSGi bundle). Is not {@code null}.
     * @param bundle The plug-in (OSGi bundle).
     */
    protected void errNoWiring(String name, Bundle bundle) {
        errNoClassLoader(name, bundle);
    }

    /**
     * A problem occurred while getting a class loader for a plug-in: a class loader could not be obtained from the
     * plug-in (OSGi bundle) with the given name, as obtaining the class loader from the bundle wiring of that plug-in
     * failed.
     *
     * <p>
     * The problem should be reported and an exception should be thrown.
     * </p>
     *
     * <p>
     * By default, this method is also invoked by the {@link #errNoWiring} method.
     * </p>
     *
     * @param name The name of the plug-in (OSGi bundle). Is not {@code null}.
     * @param bundle The plug-in (OSGi bundle).
     */
    protected abstract void errNoClassLoader(String name, Bundle bundle);
}
