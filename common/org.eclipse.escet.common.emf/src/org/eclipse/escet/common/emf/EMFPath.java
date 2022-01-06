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

package org.eclipse.escet.common.emf;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** Class for printing the containment path of an EMF object in a model. */
public class EMFPath {
    /** Data structure describing a level in the containment hierarchy of a EMF object. */
    private class Level {
        /**
         * The object of the level. For all but the leaf, this is an {@link EObject}. For the leaf, it may be either an
         * {@link EObject}, or a primitive type, in case of an attribute leaf.
         */
        public Object object;

        /** The structural feature to the next level, or {@code null} for leaves. */
        public EStructuralFeature feature;

        /**
         * Zero-based index of the object at the next level, in the {@link #feature feature} to the object at the next
         * level, or a negative value if not applicable.
         */
        public Integer index;

        /**
         * Constructor for the {@link Level} class.
         *
         * @param object The object of the level.
         * @param feature The structural feature to the next level, or {@code null} for leaves.
         * @param index Zero-based index of the object at the next level, in the {@link #feature feature} to the object
         *     at the next level, or a negative value if not applicable.
         */
        public Level(Object object, EStructuralFeature feature, Integer index) {
            this.object = object;
            this.feature = feature;
            this.index = index;

            // Attribute values don't have a next level (so no feature or
            // index).
            Assert.check(object instanceof EObject || feature == null);
            Assert.check(object instanceof EObject || index < 0);
        }

        @Override
        public String toString() {
            // Special case for simple values (for attributes).
            if (!(object instanceof EObject)) {
                if (object instanceof String) {
                    return Strings.stringToJava((String)object);
                }
                return object.toString();
            }

            // We are dealing with an EObject.
            EObject eObj = (EObject)object;

            // Try to find a readable name, via reflection.
            String name = null;
            for (EStructuralFeature ef: eObj.eClass().getEAllStructuralFeatures()) {
                // If it has a 'name', or 'id' attribute, with [0..1] or [1]
                // multiplicity, is of a string compatible type, and has a
                // value, then we can use that as the name.
                if (ef instanceof EAttribute && (ef.getName().equals("name") || ef.getName().equals("id"))
                        && !ef.isMany() && ef.getEType().getInstanceClassName().equals("java.lang.String"))
                {
                    String nameValue = (String)eObj.eGet(ef);
                    if (nameValue != null) {
                        name = "(" + ef.getName() + "=" + nameValue + ")";
                        break;
                    }
                }
            }

            // Return the level information as readable text.
            return fmt("%s%s%s%s", eObj.eClass().getName(), name == null ? "" : name,
                    feature == null ? "" : "." + feature.getName(), index >= 0 ? "[" + index + "]" : "");
        }
    }

    /** List of levels from the model root to the object that this path was created for. */
    private final LinkedList<Level> levels = new LinkedList<>();

    /**
     * Constructor for the {@link EMFPath} class. Constructs a path to the given object, from the root.
     *
     * @param eObject If no {@code eFeature} is provided, then this is the destination object. That is, the object that
     *     is the leaf of the EMF path to construct. Otherwise, if an {@code eFeature} is provided, then this is the
     *     object that contains the attribute that is the leaf value.
     * @param eFeature The structural feature that contains the leaf value, or {@code null} if not applicable or not
     *     available. This should only be provided if the leaf is an attribute, or a non-containment reference.
     */
    public EMFPath(EObject eObject, EStructuralFeature eFeature) {
        this(eObject, eFeature, null);
    }

    /**
     * Constructor for the {@link EMFPath} class. Constructs a path to the given object, from the given root.
     *
     * @param eObject If no {@code eFeature} is provided, then this is the destination object. That is, the object that
     *     is the leaf of the EMF path to construct. Otherwise, if an {@code eFeature} is provided, then this is the
     *     object that contains the attribute that is the leaf value.
     * @param eFeature The structural feature that contains the leaf value, or {@code null} if not applicable or not
     *     available. This should only be provided if the leaf is an attribute, or a non-containment reference.
     * @param root The root object, from which to construct the path. May be {@code null}, to indicate the root of the
     *     entire {@link EObject} tree. If provided, must be {@code eObject} or one of its ancestors.
     */
    public EMFPath(EObject eObject, EStructuralFeature eFeature, EObject root) {
        // Construct leaf level(s).
        if (eFeature == null) {
            // Just add the leaf object as leaf level.
            levels.addFirst(new Level(eObject, null, -1));
        } else {
            // Add the leaf feature value (if available), and the object
            // containing it.
            Object eAttrValue = eObject.eGet(eFeature);
            if (eAttrValue != null) {
                levels.addFirst(new Level(eAttrValue, null, -1));
            }
            levels.addFirst(new Level(eObject, eFeature, -1));
        }

        // Add parents, as long as they are available.
        EObject eObj = eObject;
        while (eObj != root) {
            // Get parent and containment feature. If there is no parent, we
            // have found the whole path.
            EObject eParent = eObj.eContainer();
            if (eParent == null) {
                break;
            }
            EReference eRef = eObj.eContainmentFeature();

            // Get the containment index, if applicable.
            int index = -1;
            if (eRef.isMany()) {
                @SuppressWarnings("unchecked")
                List<EObject> eList = (List<EObject>)eParent.eGet(eRef);
                index = eList.indexOf(eObj);
            }

            // Add new level, and set up for the next level.
            levels.addFirst(new Level(eParent, eRef, index));
            eObj = eParent;
        }
        Assert.check(root == null || eObj == root);
    }

    /**
     * Resolves the object referred to by this path, against the given root.
     *
     * @param root The root from which to start resolving.
     * @return The resolved object.
     * @throws IllegalArgumentException If a feature does not exist for a certain {@link EObject}; if a feature is to be
     *     resolved on a {@code null} object; or if an index is invalid for a certain feature.
     */
    public Object resolveAgainst(EObject root) {
        Object cur = root;
        for (Level level: levels) {
            if (level.feature == null) {
                return cur;
            }

            EObject curObj = (EObject)cur;
            if (curObj == null) {
                String msg = fmt("Feature %s on null object.", level.feature);
                throw new IllegalArgumentException(msg);
            }

            EStructuralFeature feature = curObj.eClass().getEStructuralFeature(level.feature.getFeatureID());
            if (feature != level.feature) {
                String msg = fmt("Feature %s does not exist for %s.", level.feature, curObj);
                throw new IllegalArgumentException(msg);
            }

            cur = curObj.eGet(level.feature);
            if (level.feature.isMany()) {
                @SuppressWarnings("unchecked")
                List<Object> values = (List<Object>)cur;
                try {
                    cur = values.get(level.index);
                } catch (IndexOutOfBoundsException ex) {
                    String msg = fmt("Index out of bounds for feature %s on object %s.", level.feature, curObj);
                    throw new IllegalArgumentException(msg, ex);
                }
            }
        }
        return cur;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (Level level: levels) {
            if (text.length() > 0) {
                text.append(" / ");
            }
            text.append(level.toString());
        }
        return text.toString();
    }
}
