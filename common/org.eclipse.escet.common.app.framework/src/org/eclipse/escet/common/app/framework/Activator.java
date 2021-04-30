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

package org.eclipse.escet.common.app.framework;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/** Activator for the application framework. */
public class Activator extends Plugin {
    /** The shared instance. */
    private static Activator plugin;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     *
     * @return The shared instance.
     */
    public static Activator getDefault() {
        Assert.notNull(plugin);
        return plugin;
    }

    /**
     * Returns the bundle context.
     *
     * @return The bundle context.
     */
    public static BundleContext getContext() {
        return getDefault().getBundle().getBundleContext();
    }

    /**
     * Returns an image descriptor for the icon referenced by the given path.
     *
     * @param path The path (within the bundle) of the icon.
     * @return An image descriptor for the icon.
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        Bundle bundle = getDefault().getBundle();
        ImageDescriptor descriptor = ImageDescriptor.createFromURLSupplier(false, () -> {
            URL iconUrl = FileLocator.find(bundle, new Path(path));
            Assert.notNull(iconUrl);
            return iconUrl;
        });
        return descriptor;
    }
}
