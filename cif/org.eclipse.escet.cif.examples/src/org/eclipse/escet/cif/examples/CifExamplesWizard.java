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

import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.escet.common.eclipse.ui.CopyFilesNewProjectWizard;
import org.osgi.framework.FrameworkUtil;

/** Wizard to create a CIF examples project. */
public class CifExamplesWizard extends CopyFilesNewProjectWizard {
    @Override
    protected String getInitialProjectName() {
        String qualifier = FrameworkUtil.getBundle(getClass()).getVersion().toString();
        return "CIFExamples-" + qualifier;
    }

    @Override
    protected Map<String, String> getPathsToCopy() {
        Map<String, String> entries = map();
        entries.put("examples/.settings", ".settings");
        entries.put("examples/hybrid", "hybrid");
        entries.put("examples/synthesis", "synthesis");
        entries.put("examples/timed", "timed");
        return entries;
    }
}
