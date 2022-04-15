//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.asciidoc.html.multipage;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * AsciiDoc multi-page HTML splitter Maven mojo.
 *
 * @see AsciiDocMultiPageHtmlSplitter
 */
@Mojo(name = "multi-page-html-split")
public class AsciiDocMultiPageHtmlSplitterMojo extends AbstractMojo {
    /** The path to the root directory that contains the AsciiDoc source files. */
    @Parameter(required = true)
    private File sourceRootPath;

    /** The base name (file name excluding file extension) of the root AsciiDoc file. */
    @Parameter(required = true)
    private String rootBaseName;

    /** The path to the single AsciiDoc-generated HTML file. */
    @Parameter(required = true)
    private File singleHtmlPagePath;

    /**
     * The path to the directory in which to write output. Is removed if it already exists. Is created if it does not
     * yet exist.
     */
    @Parameter(required = true)
    private File outputRootPath;

    /** The HTML type. */
    @Parameter(required = true)
    private HtmlType htmlType;

    /**
     * The name of the parent website to link to, if {@code htmlType} is {@link HtmlType#WEBSITE}, {@code null}
     * otherwise.
     */
    @Parameter
    private String parentWebsiteName;

    /**
     * The relative path of the parent website to link to, if {@code htmlType} is {@link HtmlType#WEBSITE}, {@code null}
     * otherwise.
     */
    @Parameter
    private String parentWebsiteLink;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        try {
            AsciiDocMultiPageHtmlSplitter.splitHtml(sourceRootPath.toPath(), singleHtmlPagePath.toPath(),
                    outputRootPath.toPath(), htmlType, parentWebsiteName, parentWebsiteLink, rootBaseName, log::info);
        } catch (Throwable e) {
            log.error(e);
            throw new MojoExecutionException("Error while executing Maven plugin.", e);
        }
    }
}
