//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.asciidoc.source.checker;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * AsciiDoc source file checker Maven mojo.
 *
 * @see AsciiDocSourceChecker
 */
@Mojo(name = "source-check")
public class AsciiDocSourceCheckerMojo extends AbstractMojo {
    /** The path to the root directory that contains the AsciiDoc source files. */
    @Parameter(required = true)
    private File sourceRootPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        int problemCount;
        try {
            problemCount = AsciiDocSourceChecker.checkSources(sourceRootPath.toPath(), log::info);
        } catch (Throwable e) {
            log.error(e);
            throw new MojoExecutionException("Error while executing Maven plugin.", e);
        }

        if (problemCount > 0) {
            throw new MojoFailureException("One or more AsciiDoc source files have problems.");
        }
    }
}
