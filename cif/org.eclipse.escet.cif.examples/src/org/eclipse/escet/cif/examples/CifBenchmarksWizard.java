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

package org.eclipse.escet.cif.examples;

import org.eclipse.escet.common.eclipse.ui.CopyFilesNewProjectWizard;
import org.osgi.framework.FrameworkUtil;

/** Wizard to create a CIF benchmarks project. */
public class CifBenchmarksWizard extends CopyFilesNewProjectWizard {
    @Override
    protected String getInitialProjectName() {
        String qualifier = FrameworkUtil.getBundle(getClass()).getVersion().toString();
        return "CIFBenchmarks-" + qualifier;
    }

    @Override
    protected String getSourceFolderPath() {
        return "benchmarks";
    }
}
