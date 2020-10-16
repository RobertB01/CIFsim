//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.typechecker;

import java.net.MalformedURLException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.escet.common.app.framework.PlatformUtils;
import org.eclipse.escet.common.java.UncachedUrlClassLoader.OpenUrlException;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.tooldef.common.ClassLoaderObtainer;
import org.osgi.framework.Bundle;

/** Class loader obtainer for ToolDef imports. */
public class ImportClassLoaderObtainer extends ClassLoaderObtainer {
    /** The position information to use to report problems. */
    private final Position pos;

    /** The type checker context. */
    private final CheckerContext ctxt;

    /** The text describing what is being imported. Must be either {@code "Java code"} or {@code "ToolDef script"}. */
    private final String useText;

    /**
     * Constructor for the {@link ImportClassLoaderObtainer} class.
     *
     * @param pos The position information to use to report problems.
     * @param ctxt The type checker context.
     * @param useText The text describing what is being imported. Must be either {@code "Java code"} or
     *     {@code "ToolDef script"}.
     */
    public ImportClassLoaderObtainer(Position pos, CheckerContext ctxt, String useText) {
        this.pos = pos;
        this.ctxt = ctxt;
        this.useText = useText;
    }

    @Override
    protected void errNotPluginProj(String name) {
        ctxt.addProblem(Message.IMPORT_PLUGIN_NOT_PLUGIN, pos, useText, name);
        throw new SemanticException();
    }

    @Override
    protected void errPluginClassicFormat(String name) {
        ctxt.addProblem(Message.IMPORT_PLUGIN_CLASSIC_FORMAT, pos, useText, name);
        throw new SemanticException();
    }

    @Override
    protected void errComputeClassPath(String name, CoreException ex) {
        String exMsg = ex.getMessage();
        if (exMsg == null) {
            exMsg = "no additional details available.";
        }
        ctxt.addProblem(Message.IMPORT_PLUGIN_NO_JAVA_NATURE, pos, useText, name, exMsg);
        throw new SemanticException();
    }

    @Override
    protected void errMalforumedUrl(String name, MalformedURLException ex) {
        String exMsg = ex.getMessage();
        if (exMsg == null) {
            exMsg = "no additional details available.";
        }
        ctxt.addProblem(Message.IMPORT_PLUGIN_MALFORMED_URL, pos, useText, name, exMsg);
        throw new SemanticException();
    }

    @Override
    protected void errOpenUrl(String name, OpenUrlException ex) {
        String exMsg = ex.getMessage();
        if (exMsg == null) {
            exMsg = "no additional details available.";
        }
        ctxt.addProblem(Message.IMPORT_PLUGIN_OPEN_URL, pos, useText, name, ex.url.toString(), exMsg);
        throw new SemanticException();
    }

    @Override
    protected void errNotFound(String name) {
        ctxt.addProblem(Message.IMPORT_PLUGIN_NOT_FOUND, pos, name);
        throw new SemanticException();
    }

    @Override
    protected void errWrongState(String name, Bundle bundle, int state) {
        ctxt.addProblem(Message.IMPORT_PLUGIN_STATE, pos, useText, name, PlatformUtils.getStateName(bundle));
        throw new SemanticException();
    }

    @Override
    protected void errNoClassLoader(String name, Bundle bundle) {
        ctxt.addProblem(Message.IMPORT_PLUGIN_ADAPT, pos, useText, name);
        throw new SemanticException();
    }
}
