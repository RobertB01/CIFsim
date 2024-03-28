//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.escet.common.emf.prettyprint.IdProvider;
import org.eclipse.escet.common.emf.prettyprint.PrettyEFeat;
import org.eclipse.escet.common.emf.prettyprint.PrettyEObject;
import org.eclipse.escet.common.emf.prettyprint.PrettyFeatEAttribute;
import org.eclipse.escet.common.emf.prettyprint.PrettyFeatEReference;

/**
 * Convert an EMF containment tree to human-readable text.
 *
 * <p>
 * The printer outputs each EObject in the tree separately, using increasing Id numbers in the printed order to identify
 * each object. This makes finding a referenced object easier to find again in the output.
 * </p>
 */
public class EMFPrettyPrinter {
    /** Constructor of the {@link EMFPrettyPrinter} class. */
    private EMFPrettyPrinter() {
        // Static class.
    }

    /** Maximum line length before wrapping to the next line. */
    private static final int MAX_LINE_LENGTH = 100;

    /** Indenting for printing value(s). */
    private static final String INDENT_TEXT = "       ";

    /**
     * Pretty-print an EMF contained tree.
     *
     * @param rootObject Root of the tree to pretty-print.
     * @return The created text.
     */
    public static String printPrettyTree(EObject rootObject) {
        List<PrettyEObject> prettyEObjects = sequenceEObjects(rootObject);
        return printPrettyEObjects(prettyEObjects);
    }

    /**
     * Convert the EObjects in the tree to a sequence of pretty objects.
     *
     * <p>
     * Also create Id providers for decorating references with the Id of the referenced EObject.
     * </p>
     *
     * @param rootEObject Root object of the EMF tree to print.
     * @return The sequence of pretty objects to convert to text.
     */
    private static List<PrettyEObject> sequenceEObjects(EObject rootEObject) {
        Map<EObject, IdProvider> foundEObjects = map(); // Found EObjects with their Id provider.
        List<PrettyEObject> prettyEObjects = list(); // Converted EObjects in print order.

        int assignedCount = 0; // Next number to assign to an EObject.

        // Unfold the tree one EObject at a time. with temporarily storage of new EObjects until we run out.
        Deque<EObject> notDone = new ArrayDeque<>();
        notDone.add(rootEObject);
        while (!notDone.isEmpty()) {
            EObject eobj = notDone.poll();

            // Find or create an Id provider for the EObject.
            IdProvider idProvider = getIdProvider(eobj, foundEObjects);

            // Assign an id.
            int objId = assignedCount + 1;
            assignedCount++;
            idProvider.setIdNumber(objId);

            // Process the features.
            EClass eClass = eobj.eClass();
            List<EStructuralFeature> structuralFeatures = getAllStructuralFeatures(eClass);
            List<PrettyEFeat> prettyFeats = listc(structuralFeatures.size());
            for (EStructuralFeature feat: structuralFeatures) {
                Object obj = eobj.eGet(feat);

                // Reference to another EObject.
                //
                // An EOjbect reference needs an Id number to link itself to the referenced EObject. Id numbers are
                // created when the EObject itself is converted to a pretty EObject. References to an EObject however
                // may be needed both before and after assigning an Id. For this reason, Ids are not exchanged directly.
                // Instead this task is delegated to the Id provider of the referenced EObject.
                if (feat instanceof EReference eRef) {
                    @SuppressWarnings("unchecked")
                    int numValues = (obj == null) ? 0 : (feat.isMany() ? ((List<EObject>)obj).size() : 1);
                    int[] idNumbers = new int[numValues];
                    Arrays.fill(idNumbers, -1);
                    PrettyFeatEReference prettyFeat = new PrettyFeatEReference(eRef, obj, idNumbers);

                    if (obj != null) {
                        if (feat.isMany()) {
                            int index = 0;
                            @SuppressWarnings("unchecked")
                            List<EObject> eListObjs = (List<EObject>)obj;
                            for (EObject eListobj: eListObjs) {
                                // Schedule object for pretty printing if it is contained here.
                                if (eRef.isContainment()) {
                                    notDone.add(eListobj);
                                }
                                // Add registration to assign a reference number to the object.
                                IdProvider featIdProvider = getIdProvider(eListobj, foundEObjects);
                                featIdProvider.registerPrettyFeature(prettyFeat, index);
                                index++;
                            }
                        } else {
                            // Schedule object for pretty printing if it is contained here.
                            if (eRef.isContainment()) {
                                notDone.add((EObject)obj);
                            }
                            // Add registration to assign a reference number to the object.
                            IdProvider featIdProvider = getIdProvider((EObject)obj, foundEObjects);
                            featIdProvider.registerPrettyFeature(prettyFeat, 0);
                        }
                    }
                    prettyFeats.add(prettyFeat);
                } else {
                    // Attribute of this EObject.
                    EAttribute eAttr = (EAttribute)feat;
                    prettyFeats.add(new PrettyFeatEAttribute(eAttr, obj));
                }
            }

            // Create a description.
            String className = eClass.getName(); // getInstanceClassName();
            PrettyEObject prettyObject = new PrettyEObject(eobj, objId, className, prettyFeats);
            prettyEObjects.add(prettyObject);
        }
        return prettyEObjects;
    }

    /**
     * Obtain or create an Id provider for the given object.
     *
     * @param eObj Object to use for finding its Id provider.
     * @param foundEObjects Already found EObjects.
     * @return The found or created Id provider of the object.
     */
    private static IdProvider getIdProvider(EObject eObj, Map<EObject, IdProvider> foundEObjects) {
        IdProvider idProvider = foundEObjects.get(eObj);
        if (idProvider == null) {
            idProvider = new IdProvider();
            foundEObjects.put(eObj, idProvider);
        }
        return idProvider;
    }

    /**
     * Convert the sequence pretty EObjects to text.
     *
     * @param prettyEObjects EObjects to convert.
     * @return The created text.
     */
    private static String printPrettyEObjects(List<PrettyEObject> prettyEObjects) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (PrettyEObject prettyEObject: prettyEObjects) {
            // Add empty line between two printed pretties.
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("\n");
            }

            // Generate the text of the EObject and its features.
            sb.append(fmt("[%d] %s:\n", prettyEObject.eObjId, prettyEObject.className));
            for (PrettyEFeat prettyFeat: prettyEObject.prettyFeats) {
                // Construct the initial text of the feature.
                String featKind;
                if (prettyFeat instanceof PrettyFeatEAttribute attr) {
                    featKind = "Attribute";
                } else if (prettyFeat instanceof PrettyFeatEReference ref) {
                    featKind = "Reference";
                } else {
                    throw new AssertionError("Unexpected pretty feature found: \"" + prettyFeat + "\".");
                }
                String typeText = prettyFeat.isMany() ? "List<" + prettyFeat.getEType() + ">" : prettyFeat.getEType();
                String featName = prettyFeat.getFeatureName();
                String initialText = fmt("  - %s %s %s = ", featKind, typeText, featName);
                sb.append(initialText);
                int colNum = initialText.length();

                // And add the values to the text.
                List<String> texts = prettyFeat.getValues();
                if (texts.isEmpty()) {
                    // Empty list, since a null values is returned as "null".
                    sb.append("[ ]");
                } else if (prettyFeat.isMany()) {
                    colNum = addText("[", sb, colNum, false);
                    boolean nonFirst = false;
                    for (String text: texts) {
                        colNum = addText(text, sb, colNum, nonFirst);
                        nonFirst = true;
                    }
                    addText("]", sb, colNum, false);
                } else {
                    addText(texts.get(0), sb, colNum, false);
                }
                sb.append("\n"); // End the line of the feature.
            }
        }
        return sb.toString();
    }

    /**
     * Add text to a line while avoiding too long lines.
     *
     * @param text Text to add.
     * @param sb Storage of already created lines.
     * @param colNum Column index of the current line.
     * @param addComma Whether to add a comma prefix before the text.
     * @return Update column index of the current line.
     */
    private static int addText(String text, StringBuilder sb, int colNum, boolean addComma) {
        if (addComma) {
            sb.append(",");
            colNum++;
        }
        int textLength = text.length() + (addComma ? 1 : 0); // Text needs an additional " " after a comma only.
        if (colNum + textLength < MAX_LINE_LENGTH) {
            // Fits at the line, append it.
            if (addComma) {
                sb.append(" ");
            }
            sb.append(text);
            return colNum + textLength;
        } else {
            // Too long, move to the next line.
            sb.append("\n" + INDENT_TEXT + text);
            return INDENT_TEXT.length() + text.length();
        }
    }

    /**
     * Get a sorted list of all structural features of a class.
     *
     * @param eClass Class to inspect.
     * @return All structural features of the class, in alphabetical order by name.
     */
    private static List<EStructuralFeature> getAllStructuralFeatures(EClass eClass) {
        List<EStructuralFeature> feats = copy(eClass.getEAllStructuralFeatures());
        Collections.sort(feats, (a, b) -> a.getName().compareTo(b.getName()));
        return feats;
    }
}
