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

import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.jface.text.IDocumentExtension3.DEFAULT_PARTITIONING;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.spelling.ISpellingProblemCollector;
import org.eclipse.ui.texteditor.spelling.SpellingAnnotation;
import org.eclipse.ui.texteditor.spelling.SpellingProblem;
import org.eclipse.ui.texteditor.spelling.SpellingReconcileStrategy;
import org.eclipse.ui.texteditor.spelling.SpellingService;

/** Reconcile strategy used for spell checking. */
public class GenericSpellingReconcileStrategy extends SpellingReconcileStrategy {
    /** Spelling problem collector. */
    private class SpellingProblemCollector implements ISpellingProblemCollector {
        /** The annotation model. */
        private final IAnnotationModel annotationModel;

        /** The annotations to add. */
        private List<Pair<SpellingAnnotation, Position>> addAnnos;

        /** The lock object for modifying the annotations. */
        private final Object lockObject;

        /**
         * Initializes this collector with the given annotation model.
         *
         * @param annotationModel The annotation model.
         */
        public SpellingProblemCollector(IAnnotationModel annotationModel) {
            Assert.notNull(annotationModel);
            this.annotationModel = annotationModel;
            lockObject = (annotationModel instanceof ISynchronizable)
                    ? ((ISynchronizable)annotationModel).getLockObject() : annotationModel;
        }

        @Override
        public void accept(SpellingProblem problem) {
            Position position = new Position(problem.getOffset(), problem.getLength());
            addAnnos.add(pair(new SpellingAnnotation(problem), position));
        }

        @Override
        public void beginCollecting() {
            addAnnos = new LinkedList<>();
        }

        @Override
        public void endCollecting() {
            synchronized (lockObject) {
                for (Pair<SpellingAnnotation, Position> entry: addAnnos) {
                    SpellingAnnotation annotation = entry.left;
                    Position position = entry.right;
                    if (keepSpellingAnno(annotation, position)) {
                        annotationModel.addAnnotation(annotation, position);
                    }
                }
            }

            addAnnos = null;
        }
    }

    /** Whether to output debug information. */
    private static final boolean DEBUG = false;

    /** The partition content types for which to perform spell checking. */
    private final Set<String> spellingContentTypes;

    /**
     * Constructor for the {@link GenericSpellingReconcileStrategy} class.
     *
     * @param viewer The source viewer.
     * @param spellingService The spelling service.
     * @param spellingContentTypes The partition content types for which to perform spell checking.
     */
    public GenericSpellingReconcileStrategy(ISourceViewer viewer, SpellingService spellingService,
            Set<String> spellingContentTypes)
    {
        super(viewer, spellingService);
        this.spellingContentTypes = spellingContentTypes;
    }

    @Override
    public void reconcile(IRegion region) {
        if (DEBUG) {
            System.out.format("%s.reconcile(region=%s)\n", getClass().getSimpleName(), region);
        }
        removeAnnos(region);
        super.reconcile(region);
    }

    /**
     * Removes all the spelling annotations overlapping the given region.
     *
     * @param region The region for which to remove all spelling annotations.
     */
    private void removeAnnos(IRegion region) {
        // Get annotation model.
        IAnnotationModel model = getAnnotationModel();
        if (model == null) {
            return;
        }

        // Process all annotations.
        Iterator<?> iter = model.getAnnotationIterator();
        while (iter.hasNext()) {
            // Skip non-spelling annotations.
            Annotation annotation = (Annotation)iter.next();
            if (!(annotation instanceof SpellingAnnotation)) {
                continue;
            }

            // Check overlap.
            Position position = model.getPosition(annotation);
            boolean overlap = position.overlapsWith(region.getOffset(), region.getLength());
            if (!overlap) {
                continue;
            }

            // Remove spelling annotation.
            if (DEBUG) {
                String msg = "%s.removeAnnos(region=%s): remove %s = %s\n";
                System.out.format(msg, getClass().getSimpleName(), region, position, getAnnotationText(position));
            }
            model.removeAnnotation(annotation);
        }
    }

    /**
     * Should the given spelling annotation be kept? That is, is the given spelling annotation for the given position
     * contained in a partition with a content type for which spell checking is enabled?
     *
     * @param annotation The spelling annotation.
     * @param position The position of the spelling annotation.
     * @return {@code true} if the spelling annotation should be kept, {@code false} otherwise.
     * @see #spellingContentTypes
     */
    private boolean keepSpellingAnno(SpellingAnnotation annotation, Position position) {
        // Get content type of the partition that contains the annotation.
        IDocumentExtension3 document = (IDocumentExtension3)getDocument();
        IDocumentPartitioner partitioner = document.getDocumentPartitioner(DEFAULT_PARTITIONING);
        String contentType = partitioner.getContentType(position.getOffset());
        Assert.notNull(contentType);

        // Check content type against spell checking enabled content types.
        boolean rslt = spellingContentTypes.contains(contentType);
        if (DEBUG) {
            String msg = "%s.keepSpellingAnno(position=%s, txt=%s): %s\n";
            System.out.format(msg, getClass().getSimpleName(), position, getAnnotationText(position), rslt);
        }
        return rslt;
    }

    @Override
    protected ISpellingProblemCollector createSpellingProblemCollector() {
        IAnnotationModel model = getAnnotationModel();
        if (model == null) {
            return null;
        }
        return new SpellingProblemCollector(model);
    }

    /**
     * Returns the text of the region covered by the given position of a spelling annotation.
     *
     * @param position The position covering the region of a spelling annotation.
     * @return The text of the region, enclosed in double quotes, or {@code null} if the text could not be obtained.
     */
    private String getAnnotationText(Position position) {
        try {
            IDocument doc = getDocument();
            String txt = doc.get(position.getOffset(), position.getLength());
            return "\"" + txt + "\"";
        } catch (BadLocationException e) {
            return null;
        }
    }
}
