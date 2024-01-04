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

package org.eclipse.escet.common.emf;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.escet.common.emf.ecore.xmi.RealXMIResourceFactory;

/**
 * Language registry. Can be used to register languages with the EMF registry. Registering languages allows them to be
 * used in ToolDef executions. Note that this is only required for stand-alone execution (that is, execution outside of
 * Eclipse). For execution within Eclipse, registration should be automatic.
 */
public final class LanguageRegistry {
    /** Constructor for the {@link LanguageRegistry} class. */
    private LanguageRegistry() {
        // Private constructor, to make the class static.
    }

    /** Registers the Ecore language with the EMF registry. */
    public static void registerEcore() {
        registerFactory("ecore", new EcoreResourceFactoryImpl());
    }

    /**
     * Registers a language's resource factory with the EMF registry.
     *
     * @param fileExt The file extension of the language that should be registered, not including a dot.
     * @param fact The resource factory to register for the file extension.
     */
    public static void registerFactory(String fileExt, Factory fact) {
        Object old = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(fileExt, fact);
        if (old != null) {
            warn("Reregistered factory for file extension \"%s\".", fileExt);
        }
    }

    /**
     * Registers an EMF based package (language) that uses XMI serialization (using the {@link RealXMIResourceFactory}
     * class) with the EMF registry. Also registers the given file extension.
     *
     * @param fileExt The file extension of instances of the metamodel of the language that should be registered, not
     *     including a dot.
     * @param pkg The package to register.
     */
    public static void registerPackageXmi(String fileExt, EPackage pkg) {
        registerFactory(fileExt, new RealXMIResourceFactory());
        registerPackage(pkg);
    }

    /**
     * Registers an EMF based package (language) that uses default XML serialization (using the
     * {@link XMIResourceFactoryImpl} class) with the EMF registry. Default XMI serialization uses {@code xsi:type} and
     * EMF path fragments. Also registers the given file extension.
     *
     * @param fileExt The file extension of instances of the metamodel of the language that should be registered, not
     *     including a dot.
     * @param pkg The package to register.
     */
    public static void registerPackageXml(String fileExt, EPackage pkg) {
        registerFactory(fileExt, new XMIResourceFactoryImpl());
        registerPackage(pkg);
    }

    /**
     * Registers an EMF based package (language) with the EMF registry, regardless of the class used for serialization.
     * Does not register any file extensions.
     *
     * @param pkg The package to register.
     */
    public static void registerPackage(EPackage pkg) {
        Object old = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(pkg.getNsURI(), pkg);
        if (old != null) {
            warn("Reregistered package for namespace URI \"%s\".", pkg.getNsURI());
        }
    }
}
