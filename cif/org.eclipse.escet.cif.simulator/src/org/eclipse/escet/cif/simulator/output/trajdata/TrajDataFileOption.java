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

package org.eclipse.escet.cif.simulator.output.trajdata;

import org.eclipse.escet.cif.simulator.options.CifSpecOption;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.StringOption;
import org.eclipse.escet.common.java.PathPair;

/** Trajectory data file path option. */
public class TrajDataFileOption extends StringOption {
    /** Constructor for the {@link TrajDataFileOption} class. */
    public TrajDataFileOption() {
        super("Trajectory data file",
                "Specify the absolute or relative local file system path of the trajectory data file, or \"\" to "
                        + "default to the path of file containing the specification being simulated, post-fixed with "
                        + "\".trajdata\". [DEFAULT=\"\"]",
                null, "trajdata-file", "TDFILE", null, true, true,
                "Specify the absolute or relative local file system path of the trajectory data file. Leave empty "
                        + "to default to the path file containing the specification being simulated, post-fixed with "
                        + "\".trajdata\".",
                "File path:");
    }

    /**
     * Returns the paths of the trajectory data file.
     *
     * @return The paths of the trajectory data file.
     */
    public static PathPair getPaths() {
        String path = Options.get(TrajDataFileOption.class);
        if (path == null) {
            path = CifSpecOption.getCifSpecPath() + ".trajdata";
        }
        String absPath = Paths.resolve(path);
        return new PathPair(path, absPath);
    }
}
