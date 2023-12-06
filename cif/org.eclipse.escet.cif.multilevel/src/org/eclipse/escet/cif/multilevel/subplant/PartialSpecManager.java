//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.subplant;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newGroup;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.util.EcoreUtil.ExternalCrossReferencer;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;

/**
 * Class that stores the constructed partial specification. It also handles the details of adding new elements, as well
 * as keeping an administration of copied and missing elements.
 */
public class PartialSpecManager {
    /** Root of the partial specification being constructed. */
    private final Specification partialSpec;

    /**
     * Original objects with their partial copied objects.
     *
     * <p>
     * The {@link #deepcloneAndAdd} functions put all copied objects in this map, that is, not just the top-level
     * object.
     * </p>
     * <p>
     * In general, most original and copied partial object have the same class, but this is not always the case.
     * Automata may get changed to Group, while locations and discrete variables may get changed to input variables.
     * </p>
     */
    private Map<EObject, EObject> copiedObjects = map();

    /**
     * Queue with unprocessed collections of dangling references in the partial specification.
     *
     * <p>
     * The result of a scan is a map of dangling objects with their relations to the scanned tree. Keys of the map are
     * the dangling objects. The associated values are 'settings', the EMF connections from non-containing parents of
     * the sub-tree to the dangling object in the key.
     * </p>
     * <p>
     * No attempt is done to merge entries from different scans. This means that in processing the dangling references
     * it may happen that an object reported as dangling at the time of the scan may already have been resolved for a
     * different copied sub-tree. The {@link #copiedObjects} map will have the replacement object in that case.
     * </p>
     */
    private final Deque<Map<EObject, Collection<Setting>>> danglingQueue = new ArrayDeque<>();

    /**
     * Constructor of the {@link PartialSpecManager} class.
     *
     * @param origSpec Original specification to partially copy.
     */
    public PartialSpecManager(Specification origSpec) {
        this.partialSpec = newSpecification();
        addCopiedObject(origSpec, partialSpec);
    }

    /**
     * Retrieve the partial object that represents the given original object if it exists. Returns {@code null} if no
     * such object exists.
     *
     * @param origObject Original object to use for finding its partial equivalent part.
     * @return The partial object that represents the original object, or {@code  null} if no such object exists.
     */
    public EObject getCopiedPartialObject(EObject origObject) {
        return copiedObjects.get(origObject);
    }

    /**
     * Get a collection of dangling objects to resolve.
     *
     * @return A collection of dangling objects. The returned information may be outdated because Returned dangling
     *     object has been resolved for a different copied sub-tree in the mean time. Returns {@code null} when the
     *     queue is empty.
     */
    public Map<EObject, Collection<Setting>> getSomeDanglingObjects() {
        return danglingQueue.poll();
    }

    /**
     * Create a skeleton copy of the provided original automaton by copying its name, alphabet, monitors, initial and
     * marked expressions, and its locations. The partial automaton is also hooked up in the partial specification.
     *
     * @param origAut Automaton to copy.
     */
    public void copyAutomatonSkeleton(Automaton origAut) {
        // Construct the partial automaton.
        Alphabet alphabet = deepcloneAndAdd(origAut.getAlphabet());
        List<Expression> initials = deepcloneAndAdd(origAut.getInitials());
        List<Expression> markeds = deepcloneAndAdd(origAut.getMarkeds());
        Monitors monitors = deepcloneAndAdd(origAut.getMonitors());
        Automaton partialAut = newAutomaton(alphabet, null, null, initials, null, null, origAut.getKind(), null,
                markeds, monitors, origAut.getName(), null);
        addCopiedObject(origAut, partialAut);
        attachAddedToComponent(origAut, partialAut);

        // Create partial locations.
        for (Location origLoc: origAut.getLocations()) {
            List<Edge> edges = deepcloneAndAdd(origLoc.getEdges());
            initials = deepcloneAndAdd(origLoc.getInitials());
            markeds = deepcloneAndAdd(origLoc.getMarkeds());
            Location partialLoc = newLocation(null, edges, null, initials, null, markeds, origLoc.getName(), null,
                    origLoc.isUrgent());
            addCopiedObject(origLoc, partialLoc);
            attachAddedToComponent(origLoc, partialLoc);
        }
    }

    /**
     * Contain the given partial object by attaching it to its parent (partial) complex component. The latter is derived
     * from the complex parent component of the original object. In addition, the given partial object is scanned for
     * dangling objects, which are stored and resolved in a next processing step.
     *
     * <p>
     * The given object pair must already have been added to {@link #copiedObjects}.
     * </p>
     *
     * @param origObj Original object.
     * @param partialObj Created partial object to add to the parent partial complex component. The partial object may
     *     have a different type than the original object.
     */
    public void attachAddedToComponent(EObject origObj, EObject partialObj) {
        // Original object should exist in the copy map, and point at the partial object as its relation.
        Assert.check(copiedObjects.containsKey(origObj));
        Assert.check(copiedObjects.get(origObj) == partialObj);

        // Scan the partial object for dangling references, and store any such references for future resolving.
        scanPartialObject(partialObj);

        // Add the new partial object in the partial containing parent.
        ComplexComponent origParent = (ComplexComponent)origObj.eContainer();
        Assert.notNull(origParent, "Element " + origObj + " has no parent.");
        ComplexComponent partialParent = ensureComponent(origParent);

        if (partialObj instanceof Group group) {
            insertEObject(((Group)origParent).getComponents(), ((Group)partialParent).getComponents(), group);
        } else if (partialObj instanceof Automaton aut) {
            insertEObject(((Group)origParent).getComponents(), ((Group)partialParent).getComponents(), aut);
        } else if (partialObj instanceof Declaration decl) {
            insertEObject(origParent.getDeclarations(), partialParent.getDeclarations(), decl);
        } else if (partialObj instanceof Invariant inv) {
            insertEObject(origParent.getInvariants(), partialParent.getInvariants(), inv);
        } else if (partialObj instanceof Location loc) {
            // Only works for explicitly added automata, since parent automata that had no partial copy always become a
            // Group.
            insertEObject(((Automaton)origParent).getLocations(), ((Automaton)partialParent).getLocations(), loc);
        } else {
            throw new AssertionError("Unexpected partial object \"" + partialObj + "\" found for group attachment.");
        }
    }

    /**
     * Ensure the given original complex component exists in the partial specification and is connected to the partial
     * specification object. Already copied complex components are kept as-is, newly created complex component are
     * always created as {@link Group}.
     *
     * @param origComponent The original complex component to ensure is or becomes available..
     * @return The partial complex component associated with the given original complex component.
     */
    public ComplexComponent ensureComponent(ComplexComponent origComponent) {
        // If a copied object exists for the origComponent, ensuring existence is trivial. This includes the partial
        // specification object, which means the recursion here will terminate.
        ComplexComponent availablePartialComponent = (ComplexComponent)copiedObjects.get(origComponent);
        if (availablePartialComponent != null) {
            return availablePartialComponent;
        }

        // 'origComponent' has no associated partial component and is thus not a Specification since that has been added
        // to the 'copiedIbjects' before. It thus must have a currently unavailable partial parent component. The
        // original parent must therefore be a Group object. Casting the result of the recursive parent query is thus
        // safe.
        Group origParent = (Group)origComponent.eContainer();
        Group partialParent = (Group)ensureComponent(origParent);

        // Found or created a parent, now create and attach the partial partner of 'origComponent' into the partial
        // specification.
        Group partialComponent = newGroup();
        partialComponent.setName(origComponent.getName());
        addCopiedObject(origComponent, partialComponent);
        insertEObject(origParent.getComponents(), partialParent.getComponents(), partialComponent);

        return partialComponent;
    }

    /**
     * Perform {@code partialList.add(partialObject} such that the order of the elements between the original and
     * partial lists match through the {@link #copiedObjects} relations.
     *
     * <p>
     * That is, before and after the call, for all {@code P < Q} there are {@code A} and {@code B} such that:
     * <ol>
     * <li>{@code copiedObjects.get(origList.get(A)) == partialList.get(P)},</li>
     * <li>{@code copiedObjects.get(origList.get(B)) == partialList.get(Q)},</li>
     * <li>{@code A < B}.</li>
     * </ol>
     * </p>
     *
     * @param <T> Type of the lists.
     * @param <U> Type of the new partial object
     * @param origList List of references in the original specification.
     * @param partialList List of references in the partial specification.
     * @param newPartialObject New partial object, to be added in the partial list at the correct index.
     */
    private <T extends EObject, U extends T> void insertEObject(EList<T> origList, EList<T> partialList,
            U newPartialObject)
    {
        Assert.notNull(newPartialObject);

        int partialIndex = 0;
        if (!partialList.isEmpty()) {
            for (EObject origObj: origList) {
                EObject expectedPartialObject = copiedObjects.get(origObj);
                if (expectedPartialObject == null) {
                    // Original object has not been copied, continue with the next entry.
                    continue;
                }

                // If the expected partial object is already in the partial list, skip to the next entry.
                if (partialList.get(partialIndex) == expectedPartialObject) {
                    partialIndex++;

                    // Abort comparing if all partial list elements have been found.
                    if (partialIndex >= partialList.size()) {
                        break;
                    }
                    continue;
                }

                // It must be the partial object that has not been added yet.
                Assert.areEqual(expectedPartialObject, newPartialObject);
                partialList.add(partialIndex, newPartialObject);
                return;
            }
        }
        if (newPartialObject != null) {
            partialList.add(newPartialObject);
        }
    }

    /**
     * Scan the given partial object for dangling references. If found, add them to the queue for future processing.
     *
     * @param partialObject New object to scan for dangling objects.
     */
    private void scanPartialObject(EObject partialObject) {
        Map<EObject, Collection<Setting>> danglingElements = ExternalCrossReferencer.find(partialObject);
        if (!danglingElements.isEmpty()) {
            danglingQueue.add(danglingElements);
        }
    }

    /**
     * Store that the given original object has been copied to the given partial object.
     *
     * @param originalObj The original object.
     * @param partialObj The (copied) partial object.
     */
    public void addCopiedObject(EObject originalObj, EObject partialObj) {
        EObject prevObj = copiedObjects.put(originalObj, partialObj);

        // Don't allow deleting a previously created value.
        Assert.check(prevObj == null, "Duplicate copy of " + originalObj);
    }

    /**
     * If the original object is not null, perform a deepclone on the original object and return its copy. It also
     * updates the collection of copied objects.
     *
     * @param <T> Type of the original and the copied object.
     * @param origObject Object to copy.
     * @return The created copied object.
     */
    public <T extends EObject> T deepcloneAndAdd(T origObject) {
        if (origObject == null) {
            return null;
        }

        // Make the deep copy.
        Copier copier = new Copier(false);
        @SuppressWarnings("unchecked")
        T partialObject = (T)copier.copy(origObject);
        copier.copyReferences();

        // Rescue all copied object relations for future reference.
        for (Entry<EObject, EObject> entry: copier.entrySet()) {
            addCopiedObject(entry.getKey(), entry.getValue());
        }
        return partialObject;
    }

    /**
     * Perform a deepclone on the collection of original objects and return a list with the copied objects.
     *
     * @param <T> Type of the original and the copied objects.
     * @param origObjects Collection of objects to copy.
     * @return The created list of copied objects.
     */
    public <T extends EObject> List<T> deepcloneAndAdd(Collection<T> origObjects) {
        if (origObjects.isEmpty()) {
            return list();
        }

        // Make the deep copy.
        Copier copier = new Copier(false);
        List<T> partialObject = (List<T>)copier.copyAll(origObjects);
        copier.copyReferences();

        // Rescue the copied instances for future reference.
        for (Entry<EObject, EObject> entry: copier.entrySet()) {
            addCopiedObject(entry.getKey(), entry.getValue());
        }
        return partialObject;
    }

    /**
     * Get the constructed partial specification.
     *
     * @return The constructed partial specification
     */
    public Specification getPartialSpec() {
        return partialSpec;
    }
}
