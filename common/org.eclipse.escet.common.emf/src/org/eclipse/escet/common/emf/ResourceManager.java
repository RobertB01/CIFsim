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

package org.eclipse.escet.common.emf;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.escet.common.app.framework.Paths;

/** Class that supports loading and saving of EMF resources. */
public class ResourceManager {
    /** Constructor for the {@link ResourceManager} class. */
    private ResourceManager() {
        // Static class.
    }

    /**
     * Loads a root object from an EMF resource. The resource must be loaded without errors, have a single root object,
     * of the correct type.
     *
     * @param <T> The type of the root object.
     * @param resourcePath The path to the resource to load; either a local file system path (absolute or relative), or
     *     a platform URI.
     * @param objClass The class of the root object.
     * @return The root object from the EMF resource.
     * @throws EMFResourceException If loading fails, the resource does not have exactly one root object, or the root
     *     object has the wrong type.
     */
    public static <T> T loadObject(String resourcePath, Class<T> objClass) throws EMFResourceException {
        URI resourceUri = Paths.createEmfURI(resourcePath);
        return loadObject(resourceUri, objClass);
    }

    /**
     * Loads a root object from an EMF resource. The resource must be loaded without errors, have a single root object,
     * of the correct type.
     *
     * @param <T> The type of the root object.
     * @param resourceUri The URI to the resource that contains the object. Relative local file system paths are
     *     resolved against the current working directory of the application.
     * @param objClass The class of the root object.
     * @return The root object from the EMF resource.
     * @throws EMFResourceException If loading fails, the resource does not have exactly one root object, or the root
     *     object has the wrong type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadObject(URI resourceUri, Class<T> objClass) throws EMFResourceException {
        Resource res = loadResource(resourceUri, true);
        EObject root = res.getContents().get(0);
        if (!objClass.isInstance(root)) {
            throw new EMFResourceException(fmt(
                    "Could not load resource \"%s\", since it contains a root object of type "
                            + "\"%s\", while a root object of type \"%s\" was expected.",
                    resourceUri, root.getClass().getSimpleName(), objClass.getSimpleName()));
        }
        return (T)root;
    }

    /**
     * Loads an EMF resource. Does not require a single root object.
     *
     * <p>
     * In most cases, it is better to use the {@link #loadObject} method, as it includes additional checking.
     * </p>
     *
     * @param resourcePath The path to the resource to load; either a local file system path (absolute or relative), or
     *     a platform URI.
     * @return The loaded EMF resource.
     * @throws EMFResourceException If loading fails.
     */
    public static Resource loadResource(String resourcePath) throws EMFResourceException {
        return loadResource(resourcePath, false);
    }

    /**
     * Loads an EMF resource. Does not require a single root object.
     *
     * <p>
     * In most cases, it is better to use the {@link #loadObject} method, as it includes additional checking.
     * </p>
     *
     * @param resourceUri The URI to the resource to load. Relative local file system paths are resolved against the
     *     current working directory of the application.
     * @return The loaded EMF resource.
     * @throws EMFResourceException If loading fails.
     */
    public static Resource loadResource(URI resourceUri) throws EMFResourceException {
        return loadResource(resourceUri, false);
    }

    /**
     * Loads an EMF resource.
     *
     * <p>
     * In most cases, it is better to use the {@link #loadObject} method, as it includes additional checking.
     * </p>
     *
     * @param resourcePath The path to the resource to load; either a local file system path (absolute or relative), or
     *     a platform URI. Relative local file system paths are resolved against the current working directory of the
     *     application.
     * @param requireSingleRoot Whether a resources with a single root object is required, for loading to succeed.
     * @return The loaded EMF resource.
     * @throws EMFResourceException If loading fails.
     */
    public static Resource loadResource(String resourcePath, boolean requireSingleRoot) throws EMFResourceException {
        URI resourceUri = Paths.createEmfURI(resourcePath);
        return loadResource(resourceUri, requireSingleRoot);
    }

    /**
     * Loads an EMF resource.
     *
     * <p>
     * In most cases, it is better to use the {@link #loadObject} method, as it includes additional checking.
     * </p>
     *
     * @param resourceUri The URI to the resource to load. Relative local file system paths are resolved against the
     *     current working directory of the application.
     * @param requireSingleRoot Whether a resources with a single root object is required, for loading to succeed.
     * @return The loaded EMF resource.
     * @throws EMFResourceException If loading fails.
     */
    public static Resource loadResource(URI resourceUri, boolean requireSingleRoot) throws EMFResourceException {
        dbg("Loading resource from: " + resourceUri);

        // Create resource set.
        ResourceSet resourceSet = new ResourceSetImpl();

        // Make sure the resource exists, before loading it.
        if (!URIConverter.INSTANCE.exists(resourceUri, null)) {
            String msg = fmt("Could not find \"%s\".", resourceUri);
            throw new EMFResourceException(msg);
        }

        // Load the resource.
        Resource resource;
        try {
            resource = resourceSet.getResource(resourceUri, true);
        } catch (WrappedException e) {
            String msg = fmt("Failed to load resource \"%s\".", resourceUri);
            throw new EMFResourceException(msg, e);
        } catch (RuntimeException e) {
            String msg = fmt("Cannot load resource \"%s\". Make sure the file has the proper file extension.",
                    resourceUri);
            throw new EMFResourceException(msg, e);
        }

        // Resolve all references.
        EcoreUtil.resolveAll(resource);

        // Validate resource.
        Diagnostician diagnostician = new Diagnostician();
        List<Diagnostic> diagnostics = list();
        for (EObject obj: resource.getContents()) {
            Diagnostic diagnostic = diagnostician.validate(obj);
            diagnostics.add(diagnostic);
        }

        // Any warnings/errors?
        try {
            EMFHelper.checkResourceWarningsErrors(resource, diagnostics, false);
        } catch (EMFResourceException e) {
            throw new EMFResourceException(fmt("The resource loaded from \"%s\" has errors.", resourceUri), e);
        }

        // One root element?
        EList<EObject> contents = resource.getContents();
        if (contents.size() != 1) {
            if (requireSingleRoot) {
                throw new EMFResourceException(
                        fmt("Failed to load resource \"%s\", as it contains %d root objects, while "
                                + "a single root object is expected.", resourceUri, contents.size()));
            }
            warn("Loaded resource \"%s\" does not contain exactly one root element.", resourceUri);
        }

        // Resource loaded. Return it.
        return resource;
    }

    /**
     * Saves an EMF resource.
     *
     * @param resource The resource to save.
     * @param resourcePath The path to save the resource to; relative local file system paths are resolved against the
     *     current working directory of the application.
     * @throws EMFResourceException If saving fails.
     */
    public static void saveResource(Resource resource, String resourcePath) throws EMFResourceException {
        URI resourceUri = Paths.createEmfURI(resourcePath);
        saveResource(resource, resourceUri);
    }

    /**
     * Saves an EMF resource.
     *
     * @param resource The resource to save.
     * @param resourceUri The URI to save the resource to; relative local file system paths are resolved against the
     *     current working directory of the application.
     * @throws EMFResourceException If saving fails.
     */
    public static void saveResource(Resource resource, URI resourceUri) throws EMFResourceException {
        dbg("Saving resource to: " + resourceUri);

        // Copy the resource. We copy the resource to a new resource with the
        // given resource path. This ensures that we automatically get the
        // resource factory associated with the file extension of the resource.
        // This ensures that references are properly serialized (EMF path
        // fragments vs XMI IDs). We attempt to preserve the xmi:id values.
        Resource res = EMFHelper.copyResource(resource, resourceUri, true);

        // Warning if number of root elements unequal to one.
        EList<EObject> contents = resource.getContents();
        if (contents.size() != 1) {
            warn("Saving resource with %d root elements.", contents.size());
        }

        // Save new resource.
        try {
            res.save(null);
        } catch (IOException e) {
            throw new EMFResourceException("Error saving resource to: " + resourceUri, e);
        }
    }

    /**
     * Saves an {@link EObject} as EMF resource.
     *
     * @param obj The {@link EObject} to save.
     * @param resourcePath The path to save the resource to; either a local file system path (absolute or relative), or
     *     a URI
     * @throws EMFResourceException if saving fails.
     */
    public static void saveResource(EObject obj, String resourcePath) throws EMFResourceException {
        URI resourceUri = Paths.createEmfURI(resourcePath);
        saveResource(obj, resourceUri);
    }

    /**
     * Saves an {@link EObject} as EMF resource.
     *
     * @param obj The {@link EObject} to save.
     * @param resourceUri The URI to save the resource to; relative local file system paths are resolved against the
     *     current working directory of the application.
     * @throws EMFResourceException if saving fails.
     */
    public static void saveResource(EObject obj, URI resourceUri) throws EMFResourceException {
        dbg("Saving resource to: " + resourceUri);

        // Create resource.
        ResourceSet resourceSet = new ResourceSetImpl();
        Resource res = resourceSet.createResource(resourceUri);
        res.getContents().add(obj);

        // Save the resource.
        try {
            res.save(null);
        } catch (IOException e) {
            throw new EMFResourceException("Error saving resource to: " + resourceUri, e);
        }
    }
}
