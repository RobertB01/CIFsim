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

package org.eclipse.escet.setext.texteditorbase;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;

/** {@link GenericTextEditor} folding support. */
public class GenericTextEditorFolding {
    /** The name of the package in which this class is defined. */
    private static final String PACKAGE_NAME = GenericTextEditorFolding.class.getPackage().getName();

    /** Name to use for persisted collapsed fold ranges. */
    private static final QualifiedName PERSIST_NAME = new QualifiedName(PACKAGE_NAME, "collapsedFoldRanges");

    /** The text editor to which folding support is to be provided. */
    private final GenericTextEditor<?, ?, ?> textEditor;

    /** Projection annotation model, for folding support. */
    private ProjectionAnnotationModel projAnnoModel;

    /** The currently active projection annotations for fold ranges. */
    private List<ProjectionAnnotation> curProjAnnos = new LinkedList<>();

    /**
     * The loaded projection annotations, as 'addition' mapping. Is {@code null} if nothing has been loaded, and after
     * the loaded annotations have been added to the annotation model.
     */
    private Map<Annotation, Position> loadedAnnos = null;

    /**
     * Constructor for the {@link GenericTextEditorFolding} class.
     *
     * @param textEditor The text editor to which folding support is to be provided.
     */
    public GenericTextEditorFolding(GenericTextEditor<?, ?, ?> textEditor) {
        this.textEditor = textEditor;
    }

    /**
     * Implementation of {@link TextEditor#createPartControl} for folding support.
     *
     * @param sourceViewer The source viewer for the text editor. Must be a {@link ProjectionViewer}.
     * @param annotationAccess The annotation access for the text editor.
     * @param sharedColors The shared text colors for the text editor.
     */
    public void createPartControl(ISourceViewer sourceViewer, IAnnotationAccess annotationAccess,
            ISharedTextColors sharedColors)
    {
        // Created projection support.
        ProjectionViewer viewer = (ProjectionViewer)sourceViewer;
        ProjectionSupport projectionSupport = new ProjectionSupport(viewer, annotationAccess, sharedColors);

        // Make sure folded regions get summarized errors and warnings.
        String errAnnoType = "org.eclipse.ui.workbench.texteditor.error";
        String warnAnnoType = "org.eclipse.ui.workbench.texteditor.warning";
        projectionSupport.addSummarizableAnnotationType(errAnnoType);
        projectionSupport.addSummarizableAnnotationType(warnAnnoType);

        // Enable projection support, and store annotation model to allow
        // updating the fold regions later on.
        projectionSupport.install();
        viewer.doOperation(ProjectionViewer.TOGGLE);
        projAnnoModel = viewer.getProjectionAnnotationModel();

        // Add loaded fold regions and collapse them, if any.
        if (loadedAnnos != null) {
            // Store ranges to add, also as the current ranges
            projAnnoModel.replaceAnnotations(new Annotation[0], loadedAnnos);

            // Collapse the ranges.
            for (ProjectionAnnotation anno: curProjAnnos) {
                projAnnoModel.collapse(anno);
            }

            // No more loaded annotations. Allows garbage collection.
            loadedAnnos = null;
        }
    }

    /**
     * Update the fold ranges of the file.
     *
     * @param newRanges The new fold ranges.
     * @param text The document text, to which the fold ranges apply.
     */
    public void updateFolds(List<Position> newRanges, String text) {
        // Move 'old' annotations ('curProjAnnos') that match the new
        // ranges to the 'new' annotations ('newProjAnnos'). Add 'new'
        // annotations for the ones which did not yet exist(to 'newProjAnnos'
        // and 'annosAdded'). Left over 'old' annotations ('curProjAnnos') are
        // removed.
        List<ProjectionAnnotation> newProjAnnos = new LinkedList<>();
        Map<Annotation, Position> annosAdded = map();

        OUTER:
        for (Position newRange: newRanges) {
            // Look for existing annotation with the exact same range. We walk
            // over all 'old' annotations ('curProjAnnos'). However, since we
            // keep them in the same order as the 'new' ranges, we most often
            // hit a match at the first annotation. For the ones that need to
            // be added, we look through all 'old' annotations. The 'old'
            // annotations that need to be removed, need to be skipped for each
            // 'new' range. The result is very close to linear performance.
            Iterator<ProjectionAnnotation> annoIter = curProjAnnos.iterator();
            while (annoIter.hasNext()) {
                // Get next 'old' annotation.
                ProjectionAnnotation oldAnno = annoIter.next();

                // Get up-to-date position of the 'old' annotation.
                Position annoRange = projAnnoModel.getPosition(oldAnno);

                // Check for match with new 'range'.
                if (annoRange != null && annoRange.offset == newRange.offset && annoRange.length == newRange.length) {
                    // Found matching existing annotation. Move from the 'old'
                    // annotations to the 'new' annotations.
                    newProjAnnos.add(oldAnno);
                    annoIter.remove();
                    continue OUTER;
                }
            }

            // No match found. Add new annotation.
            ProjectionAnnotation newAnno = new ProjectionAnnotation();
            annosAdded.put(newAnno, newRange);
            newProjAnnos.add(newAnno);
        }

        // Let projection annotation model know about the changes.
        final boolean DEBUG = false;
        if (DEBUG) {
            System.out.format("%s: Folding ranges del/add/kept: %d/%d/%d\n", getClass().getSimpleName(),
                    curProjAnnos.size(), annosAdded.size(), newProjAnnos.size() - annosAdded.size());
        }

        if (!curProjAnnos.isEmpty() || !annosAdded.isEmpty()) {
            Annotation[] annosDeleted = new Annotation[curProjAnnos.size()];
            annosDeleted = curProjAnnos.toArray(annosDeleted);
            projAnnoModel.replaceAnnotations(annosDeleted, annosAdded);
        }

        // The 'new' annotations become the 'current' annotations, which are
        // the 'old' annotations for the next time.
        curProjAnnos = newProjAnnos;
    }

    /**
     * Fix the fold ranges by removing single line ranges, and making sure the remaining fold ranges span entire lines.
     *
     * @param foldRanges The fold ranges to fix. Modified in-place.
     * @param text The document text, to which the fold ranges apply.
     */
    public void fixFolds(List<Position> foldRanges, String text) {
        // Process all fold ranges.
        Iterator<Position> iter = foldRanges.iterator();
        while (iter.hasNext()) {
            // Get fold range.
            Position foldRange = iter.next();

            // Extend start of fold range to full line. If fold range is not
            // preceded by a new line, keep extending it, making sure it does
            // not extend beyond the start of the document. We don't include
            // any preceding '\n' or '\r\n'.
            while (foldRange.offset > 0 && text.charAt(foldRange.offset - 1) != '\n') {
                foldRange.offset--;
                foldRange.length++;
            }

            // Extend end of fold range to full line. If fold range does not
            // end with a new line, keep extending it, making sure it does not
            // extend beyond the end of the document. We include any following
            // text until the next '\n' or '\r\n', as long as the fold range
            // does not end with '\n'.
            int startOffset = foldRange.offset;
            int endOffset = startOffset + foldRange.length; // Exclusive.
            while (endOffset < text.length() && text.charAt(endOffset - 1) != '\n') {
                endOffset++;
            }
            foldRange.length = endOffset - startOffset;

            // Exclude single line fold ranges. They include at most one new
            // line character. We only count '\n', as that one is present on
            // all platforms.
            int cnt = 0;
            for (int i = startOffset; i < endOffset; i++) {
                if (text.charAt(i) == '\n') {
                    cnt++;
                    if (cnt > 1) {
                        break;
                    }
                }
            }
            if (cnt < 2) {
                iter.remove();
            }
        }
    }

    /**
     * Saves the folding state to persisted data for the file. Only the ranges of collapsed regions are saved. The
     * expanded ones are recreated upon first validation. If no regions are collapsed, the persisted data for the file
     * is removed. We use a best effort approach to save the data. That is, if the data can not be saved, the method
     * simply does nothing.
     *
     * @param input The file editor input for the file for which to save the folding state.
     */
    public void save(IFileEditorInput input) {
        // No point in saving if the file doesn't exist.
        if (input == null) {
            return;
        }
        IFile file = input.getFile();
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }

        // Get ranges of collapsed fold regions.
        List<Position> collapsedRanges = list();
        Iterator<ProjectionAnnotation> annoIter = curProjAnnos.iterator();
        while (annoIter.hasNext()) {
            // Skip deleted and expanded regions.
            ProjectionAnnotation anno = annoIter.next();
            if (anno.isMarkedDeleted()) {
                continue;
            }
            if (!anno.isCollapsed()) {
                continue;
            }

            // Add range.
            Position range = projAnnoModel.getPosition(anno);
            if (range != null) {
                collapsedRanges.add(range);
            }
        }

        // Serialize the ranges. Is 'null' if no ranges.
        String data = serialize(collapsedRanges);

        // Persist the serialized data. Is removed if no ranges.
        try {
            file.setPersistentProperty(PERSIST_NAME, data);
        } catch (CoreException ex) {
            // Best effort. Ignore failures.
            return;
        }
    }

    /**
     * Loads the folding state from persisted data for the file. Only ranges of collapsed regions are saved, and thus
     * loaded. The expanded ones are recreated upon first validation. We use a best effort approach to load the data.
     * That is, if there is no persisted data for the file, or the data can not be loaded, the method simply does
     * nothing.
     */
    public void load() {
        // Unpersist the serialized data.
        FileEditorInput input = (FileEditorInput)textEditor.getEditorInput();
        if (input == null) {
            return;
        }
        IFile file = input.getFile();
        if (file == null) {
            return;
        }

        String data = null;
        try {
            data = file.getPersistentProperty(PERSIST_NAME);
        } catch (CoreException ex) {
            // Best effort. Ignore failures.
            return;
        }
        if (data == null) {
            return;
        }

        // Deserialize the ranges.
        List<Position> collapsedRanges = deserialize(data);
        if (collapsedRanges == null) {
            return;
        }
        if (collapsedRanges.isEmpty()) {
            return;
        }

        // Store ranges to add, also as the current projection annotations.
        // Don't actually add them yet, as the projection annotation model is
        // not yet available.
        curProjAnnos = new LinkedList<>();
        loadedAnnos = mapc(collapsedRanges.size());
        for (Position range: collapsedRanges) {
            ProjectionAnnotation anno = new ProjectionAnnotation();
            loadedAnnos.put(anno, range);
            curProjAnnos.add(anno);
        }
    }

    /**
     * Serializes the given fold ranges.
     *
     * @param foldRanges The fold ranges to serialize.
     * @return The serialized fold ranges, or {@code null} if no fold ranges provided.
     */
    private String serialize(List<Position> foldRanges) {
        // Handle no fold ranges special case.
        if (foldRanges.isEmpty()) {
            return null;
        }

        // The data that we persist must not exceed 2048 characters. If it
        // would be larger, the operation would fail, and we would ignore that
        // failure. Instead, we ensure we never have too much data, by only
        // serializing at most 512 fold ranges. 512 ranges of 2 integers is 512
        // times 8 bytes, is 4096 bytes. With 2 bytes per character, that is
        // 2048 characters.
        if (foldRanges.size() > 512) {
            foldRanges = foldRanges.subList(0, 512);
        }

        // Normal case. Each range is two integers, thus 8 bytes. Each two
        // bytes become a character.
        ByteBuffer bb = ByteBuffer.allocate(foldRanges.size() * 8);
        for (Position range: foldRanges) {
            bb.putInt(range.offset);
            bb.putInt(range.length);
        }
        Assert.check(!bb.hasRemaining());
        bb.rewind();
        CharBuffer cb = bb.asCharBuffer();

        // Note that Eclipse uses a DataOutputStream to write the string, which
        // uses 'modified UTF', which allows for any 'char' value to be round
        // tripped, even if it is not a valid Unicode character.
        String rslt = cb.toString();
        Assert.check(rslt.length() <= 2048);
        rslt.length();
        return rslt;
    }

    /**
     * Deserializes the given fold ranges.
     *
     * @param data The serialized fold ranges.
     * @return The deserialized fold ranges, or {@code null} in case of a deserialization failure.
     */
    private List<Position> deserialize(String data) {
        // Each character becomes two bytes, and we need 8 bytes per fold
        // range. We thus need the number of characters to be a multiple of 4.
        if (data.length() % 4 != 0) {
            return null;
        }

        // Fill byte buffer.
        ByteBuffer bb = ByteBuffer.allocate(data.length() * 2);
        CharBuffer cb = bb.asCharBuffer();
        cb.append(data);

        // Get and return fold ranges.
        int count = data.length() / 4;
        List<Position> rslt = listc(count);
        for (int i = 0; i < count; i++) {
            int offset = bb.getInt();
            int length = bb.getInt();
            if (offset < 0) {
                return null;
            }
            if (length < 0) {
                return null;
            }
            rslt.add(new Position(offset, length));
        }
        Assert.check(rslt.size() == count);
        return rslt;
    }
}
