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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.escet.common.emf.ecore.xmi.RealXMIResource;
import org.eclipse.escet.common.emf.ecore.xmi.RealXMIResourceFactory;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Exceptions;
import org.eclipse.escet.common.java.Strings;

/** Generic EMF helper functionality. */
public final class EMFHelper {
    /** Constructor for the {@link EMFHelper} class. */
    private EMFHelper() {
        // Private constructor to turn this class into a static class.
    }

    /**
     * Copies an EMF resource.
     *
     * <p>
     * Optionally, xmi:id values may be preserved. Note that preservation of xmi:id values, applies only if both the
     * original resource and the newly created resource, are instances of {@link RealXMIResourceFactory}. This is to
     * ensure that the xmi:id values that we preserve, are actually XMI ids, and not for instance EMF path fragments.
     * </p>
     *
     * @param orig The original EMF resource to copy.
     * @param newUri The {@link URI} of the new resource.
     * @param preserveXmiIds Whether xmi:id values should be preserved.
     * @return The copied EMF resource.
     */
    public static Resource copyResource(Resource orig, URI newUri, boolean preserveXmiIds) {
        // Create resource set.
        ResourceSet resourceSet = new ResourceSetImpl();

        // Create empty resource.
        Resource rslt = resourceSet.createResource(newUri);

        // Copy the contents to the new resource.
        List<EObject> rootObjs = orig.getContents();
        Collection<EObject> newRootObjs = EcoreUtil.copyAll(rootObjs);
        rslt.getContents().addAll(newRootObjs);

        // In order to preserve the xmi:id values, we synchronize them.
        if (preserveXmiIds && orig.getClass() == RealXMIResource.class && rslt.getClass() == RealXMIResource.class) {
            syncXmiIds(rslt.getContents(), orig.getContents());
        }

        // Return the copy.
        return rslt;
    }

    /**
     * Synchronizes the xmi:id values of the target to the xmi:id values of the source, recursively. This assumes that
     * they have a compatible structure.
     *
     * @param target The target objects for which to set the xmi:id values.
     * @param source The source objects from which to get the xmi:id values.
     */
    public static void syncXmiIds(List<EObject> target, List<EObject> source) {
        // Make sure the object collections are compatible and can be
        // synchronized.
        Assert.check(target.size() == source.size());

        // Synchronize the collections.
        for (int i = 0; i < target.size(); i++) {
            EObject targetElem = target.get(i);
            EObject sourceElem = source.get(i);
            syncXmiIds(targetElem, sourceElem);
        }
    }

    /**
     * Synchronizes the xmi:id values of the target to the xmi:id values of the source, recursively. This assumes that
     * they have a compatible structure.
     *
     * @param target The target object for which to set the xmi:id values.
     * @param source The source object from which to get the xmi:id values.
     */
    public static void syncXmiIds(EObject target, EObject source) {
        // Make sure the objects are of the same type. If this doesn't hold,
        // then the target is not a proper copy of the source.
        Assert.check(target.eClass() == source.eClass());

        // Synchronize xmi:id values of these objects.
        // Generate a unique xmi:id value.
        Resource sourceRes = source.eResource();
        Resource targetRes = target.eResource();

        // If one of them doesn't have an XML file format (like EMFText
        // resources), then skip xmi:id value synchronization.
        if (sourceRes instanceof XMLResource && targetRes instanceof XMLResource) {
            XMLResource sourceXRes = (XMLResource)sourceRes;
            XMLResource targetXRes = (XMLResource)targetRes;

            String sourceXmiId = sourceXRes.getID(source);
            targetXRes.setID(target, sourceXmiId);
        }

        // Apply recursively.
        syncXmiIds(target.eContents(), source.eContents());
    }

    /**
     * Normalizes the xmi:id values of the objects in the XML resource, recursively.
     *
     * @param resource The XML resource.
     */
    public static void normalizeXmiIds(XMLResource resource) {
        int id = 0;
        for (TreeIterator<EObject> iter = resource.getAllContents(); iter.hasNext();) {
            id++;
            EObject obj = iter.next();
            resource.setID(obj, Integer.toString(id));
        }
    }

    /**
     * Checks a loaded EMF resource for warnings and errors.
     *
     * @param resource The EMF resource to check for warnings and errors.
     * @param strict {@code true} to fail on warnings, {@code false} to ignore them.
     * @throws EMFResourceException If the resource has errors (or warnings, if strict mode enabled) associated with it.
     */
    public static void checkResourceWarningsErrors(Resource resource, boolean strict) throws EMFResourceException {
        // Anything to report?
        if (resource.getErrors().isEmpty() && (!strict || resource.getWarnings().isEmpty())) {
            return;
        }

        // Get problem messages.
        List<String> messages = list();
        for (Diagnostic diagnostic: resource.getWarnings()) {
            StringBuilder diagMsg = new StringBuilder();
            diagMsg.append("WARNING: ");
            diagMsg.append(diagnostic.getMessage());
            if (diagnostic.getLocation() != null) {
                diagMsg.append(" (location: " + diagnostic.getLocation());
                if (diagnostic.getLine() > 0) {
                    diagMsg.append(", line: " + diagnostic.getLine());
                    if (diagnostic.getColumn() > 0) {
                        diagMsg.append(", column: " + diagnostic.getColumn());
                    }
                }
                diagMsg.append(")");
            }
            messages.add(diagMsg.toString());
        }
        for (Diagnostic diagnostic: resource.getErrors()) {
            StringBuilder diagMsg = new StringBuilder();
            diagMsg.append("ERROR: ");
            diagMsg.append(diagnostic.getMessage());
            if (diagnostic.getLocation() != null) {
                diagMsg.append(" (location: " + diagnostic.getLocation());
                if (diagnostic.getLine() > 0) {
                    diagMsg.append(", line: " + diagnostic.getLine());
                    if (diagnostic.getColumn() > 0) {
                        diagMsg.append(", column: " + diagnostic.getColumn());
                    }
                }
                diagMsg.append(")");
            }
            messages.add(diagMsg.toString());
        }
        String message = String.join(Strings.NL, messages);
        Throwable cause = new EMFResourceException(message);

        // Report problems.
        String msg = fmt("Could not load resource \"%s\": resource has %,d errors, %,d warnings.", resource.getURI(),
                resource.getErrors().size(), resource.getWarnings().size());
        throw new EMFResourceException(msg, cause);
    }

    /**
     * Checks a loaded EMF resource for warnings and errors.
     *
     * @param resource The EMF resource to check for warnings and errors.
     * @param diagnostics The results of validation by a {@link Diagnostician}.
     * @param strict {@code true} to fail on warnings, {@code false} to ignore them.
     * @throws EMFResourceException If the resource has errors (or warnings, if strict mode enabled) associated with it.
     */
    public static void checkResourceWarningsErrors(Resource resource,
            Collection<org.eclipse.emf.common.util.Diagnostic> diagnostics, boolean strict) throws EMFResourceException
    {
        // Anything to report?
        boolean anyError = false;
        boolean anyWarning = false;
        for (org.eclipse.emf.common.util.Diagnostic diagnostic: diagnostics) {
            if (diagnostic.getSeverity() == org.eclipse.emf.common.util.Diagnostic.ERROR) {
                anyError = true;
            }
            if (diagnostic.getSeverity() == org.eclipse.emf.common.util.Diagnostic.WARNING) {
                anyWarning = true;
            }
        }
        if (!anyError && (!strict || !anyWarning)) {
            return;
        }

        // Get problem messages.
        List<String> messages = list();
        int errorCount = 0;
        int warningCount = 0;
        Deque<org.eclipse.emf.common.util.Diagnostic> todos = new LinkedList<>();
        todos.addAll(diagnostics);
        while (!todos.isEmpty()) {
            org.eclipse.emf.common.util.Diagnostic diagnostic;
            diagnostic = todos.pollFirst();

            StringBuilder diagMsg = new StringBuilder();
            if (diagnostic.getSeverity() == org.eclipse.emf.common.util.Diagnostic.ERROR) {
                diagMsg.append("ERROR: ");
                errorCount++;
            } else if (diagnostic.getSeverity() == org.eclipse.emf.common.util.Diagnostic.WARNING) {
                diagMsg.append("ERROR: ");
                warningCount++;
            } else {
                continue;
            }
            diagMsg.append(diagnostic.getMessage());
            if (diagnostic.getException() != null) {
                diagMsg.append(" (exception: ");
                diagMsg.append(Exceptions.exToLine(diagnostic.getException()));
            }
            if (diagnostic.getSource() != null) {
                if (diagnostic.getException() == null) {
                    diagMsg.append(" (source: ");
                } else {
                    diagMsg.append(", source: ");
                }
                diagMsg.append(diagnostic.getSource());
            }
            if (diagnostic.getException() != null || diagnostic.getSource() != null) {
                diagMsg.append(")");
            }
            messages.add(diagMsg.toString());

            todos.addAll(diagnostic.getChildren());
        }
        String message = String.join(Strings.NL, messages);
        Throwable cause = new EMFResourceException(message);

        // Report problems.
        String msg = fmt("Could not load resource \"%s\": resource has %,d errors, %,d warnings.", resource.getURI(),
                errorCount, warningCount);
        throw new EMFResourceException(msg, cause);
    }

    /**
     * Creates a deep-clone (recursive clone) of an {@link EObject}. Only clones over the containment hierarchy, and
     * leaves references alone.
     *
     * <p>
     * This method is a simple wrapper around the {@link EcoreUtil#copy} method.
     * </p>
     *
     * @param <T> The type of object to deep-clone.
     * @param obj The {@link EObject} instance to deep-clone.
     * @return The deep-clone of the {@link EObject}.
     * @see EcoreUtil#copy
     */
    public static <T extends EObject> T deepclone(T obj) {
        return EcoreUtil.copy(obj);
    }

    /**
     * Creates deep-clones (recursive clones) of an {@link EObject} collection. Only clones over the containment
     * hierarchy, and leaves references alone.
     *
     * <p>
     * This method is a simple wrapper around the {@link EcoreUtil#copyAll} method.
     * </p>
     *
     * @param <T> The type of objects to deep-clone.
     * @param objs The {@link EObject} collection to deep-clone.
     * @return The deep-clones of the {@link EObject} collection.
     * @see EcoreUtil#copyAll
     */
    public static <T extends EObject> Collection<T> deepclone(Collection<T> objs) {
        return EcoreUtil.copyAll(objs);
    }

    /**
     * Creates deep-clones (recursive clones) of an {@link EObject} list. Only clones over the containment hierarchy,
     * and leaves references alone.
     *
     * <p>
     * This method is a simple wrapper around the {@link EcoreUtil#copyAll} method, adapted for {@link List} instances.
     * </p>
     *
     * @param <T> The type of objects to deep-clone.
     * @param objs The {@link EObject} list to deep-clone.
     * @return The deep-clones of the {@link EObject} list.
     * @see EcoreUtil#copyAll
     */
    public static <T extends EObject> List<T> deepclone(List<T> objs) {
        return (List<T>)EcoreUtil.copyAll(objs);
    }

    /**
     * Prints the XML representation of an {@link EObject} to the given stream.
     *
     * @param obj The object to print.
     * @param fileExt The file extension to use to retrieve the proper resource factory, and thus achieve the proper
     *     serialization. Should not include the dot. For instance: {@code "ecore"}.
     * @param stream The stream to which to write the XML representation.
     * @throws IllegalArgumentException If {@code obj} has a parent/container.
     */
    public static void printEObjectXML(EObject obj, String fileExt, OutputStream stream) {
        // Construct resource with the proper extension, to make sure we
        // use the proper serialization.
        ResourceSet s = new ResourceSetImpl();
        Resource r = s.createResource(URI.createURI("memory:///dummy." + fileExt));

        // Make sure the object has no parent, as otherwise adding it to
        // the resource changes its parent. This check makes sure we don't have
        // to clone the object before adding it to the resource.
        if (obj.eContainer() != null) {
            throw new IllegalArgumentException("obj.eContainer() != null");
        }

        // Add object to the resource.
        r.getContents().add(obj);

        // Serialize the object to a byte array stream.
        try {
            r.save(stream, null);
            stream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Given a {@code child}, update the parent containment for this child, and replace it by the given
     * {@code newChild}.
     *
     * @param child The child to replace in its parent containment.
     * @param newChild The child to replace it with.
     * @see EcoreUtil#replace(EObject, EObject)
     */
    public static void updateParentContainment(EObject child, EObject newChild) {
        // If new child same as original, no need to replace anything.
        if (child == newChild) {
            return;
        }

        // Get parent.
        EObject parent = child.eContainer();
        Assert.notNull(parent);

        // Get feature.
        EReference feature = child.eContainmentFeature();

        // Update feature value.
        if (feature.isMany()) {
            @SuppressWarnings("unchecked")
            List<EObject> children = (List<EObject>)parent.eGet(feature);
            int idx = children.indexOf(child);
            Assert.check(idx != -1);
            children.set(idx, newChild);
        } else {
            parent.eSet(feature, newChild);
        }
    }

    /**
     * Removes a {@code child} object from its parent containment, thereby making the {@code child} an orphan.
     *
     * <p>
     * Note that performance wise, it is better to manually remove the child from its containment feature directly,
     * instead of using this method. That is, use {@code obj.setSomeSingleValuedFeature(null)} or
     * {@code obj.getSomeMultiValuedFeature().remove(child)} whenever possible. However, sometimes the parent object
     * and/or the containment feature are not known, and the reflective approach used by this method is the only way to
     * get the desired result.
     * </p>
     *
     * @param child The child to remove.
     * @see EcoreUtil#remove(EObject)
     */
    public static void removeFromParentContainment(EObject child) {
        // Get parent.
        EObject parent = child.eContainer();
        Assert.notNull(parent);

        // Get feature.
        EReference feature = child.eContainmentFeature();

        // Remove (from) feature value.
        if (feature.isMany()) {
            @SuppressWarnings("unchecked")
            List<EObject> children = (List<EObject>)parent.eGet(feature);
            int idx = children.indexOf(child);
            Assert.check(idx != -1);
            children.remove(idx);
        } else {
            parent.eSet(feature, null);
        }
    }
}
