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

package org.eclipse.escet.cif.examples;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/** CIF examples plug-in activator. */
public class CifExamplesPlugin extends Plugin {
    /** The singleton instance of the {@link CifExamplesPlugin}, or {@code null} if the plug-in is not running. */
    public static CifExamplesPlugin instance;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        instance = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        instance = null;
        super.stop(context);
    }
}
