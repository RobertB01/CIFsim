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

package org.eclipse.escet.setext.texteditorbase;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.jface.text.source.projection.AnnotationBag;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.ui.texteditor.spelling.SpellingAnnotation;

/**
 * Text/annotation hover that displays the annotations for the hover position/line. The annotations are filtered to only
 * include:
 * <ul>
 * <li>{@link MarkerAnnotation} for syntax problems and semantic problems.</li>
 * <li>{@link SpellingAnnotation} for spelling problems.</li>
 * <li>{@link AnnotationBag} for summarized annotations, for folding support.</li>
 * </ul>
 */
public class GenericTextHover implements IAnnotationHover, ITextHover, ITextHoverExtension2 {
    /** The source viewer configuration to use to obtain the annotations for the hover text. */
    private final ISourceViewer sourceViewer;

    /**
     * Constructor for the {@link GenericTextHover} class.
     *
     * @param sourceViewer The source viewer configuration to use to obtain the annotations for the hover text.
     */
    public GenericTextHover(ISourceViewer sourceViewer) {
        this.sourceViewer = sourceViewer;
    }

    @Override
    public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
        return new Region(offset, 0);
    }

    @Override
    public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
        if (lineNumber < 0) {
            return null;
        }
        return getHoverInfo(lineNumber, -1);
    }

    @Override
    @SuppressWarnings("deprecation")
    public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
        return getHoverInfo2(textViewer, hoverRegion);
    }

    @Override
    public String getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
        int offset = hoverRegion.getOffset();
        Assert.check(offset >= 0);
        return getHoverInfo(-1, offset);
    }

    /**
     * Returns the hover info (hover message) for the given line/offset.
     *
     * <p>
     * If a line number is given it is used for position-based filtering of the annotations. If no line number is given,
     * and an offset is given, the offset is used for position-based filtering. If neither is given, no position-based
     * filtering is applied.
     * </p>
     *
     * @param lineNumber The 0-based index of the line number for which to return the hover info, or {@code -1} if not
     *     available.
     * @param offset The 0-based index of the character in the input for which to return the hover info, or {@code -1}
     *     if not available.
     * @return The hover info.
     */
    private String getHoverInfo(int lineNumber, int offset) {
        // Get the annotations, if any.
        List<Annotation> annos = getAnnotations(lineNumber, offset);
        if (annos == null) {
            return null;
        }

        // Get messages from the annotations, and store them in a TreeSet for
        // automatic sorting and duplicate removal.
        TreeSet<String> messages = new TreeSet<>();
        Iterator<?> iterator = annos.iterator();
        while (iterator.hasNext()) {
            Annotation annotation = (Annotation)iterator.next();
            String message = annotation.getText();
            if (message != null) {
                messages.add(message);
            }
        }

        // Format the message.
        if (messages.size() == 0) {
            return null;
        }
        if (messages.size() == 1) {
            return messages.iterator().next();
        }
        String header = (lineNumber == -1) ? "Multiple problems at this position:" : "Multiple problems at this line:";
        return formatMessages(header, messages);
    }

    /**
     * Returns the annotations for the given line/offset.
     *
     * <p>
     * If a line number is given it is used for position-based filtering of the annotations. If no line number is given,
     * and an offset is given, the offset is used for position-based filtering. If neither is given, no position-based
     * filtering is applied.
     * </p>
     *
     * @param lineNumber The 0-based index of the line number for which to return the hover info, or {@code -1} if not
     *     available.
     * @param offset The 0-based index of the character in the input for which to return the hover info, or {@code -1}
     *     if not available.
     * @return The annotations, or {@code null} if not available.
     */
    private List<Annotation> getAnnotations(int lineNumber, int offset) {
        // Get annotation model.
        IAnnotationModel annoModel = (sourceViewer instanceof ISourceViewerExtension2)
                ? ((ISourceViewerExtension2)sourceViewer).getVisualAnnotationModel()
                : sourceViewer.getAnnotationModel();
        if (annoModel == null) {
            return null;
        }

        // Get lock object for annotation model.
        Object lockObject = (annoModel instanceof ISynchronizable) ? ((ISynchronizable)annoModel).getLockObject()
                : annoModel;

        // Get annotations.
        List<Annotation> annos = list();
        synchronized (lockObject) {
            Iterator<Annotation> iterator = annoModel.getAnnotationIterator();
            getAnnotations(iterator, annoModel, annos, lineNumber, offset);
        }

        // Return filtered annotations.
        return annos;
    }

    /**
     * Collects the annotations for the given hover offset position.
     *
     * <p>
     * If a line number is given it is used for position-based filtering of the annotations. If no line number is given,
     * and an offset is given, the offset is used for position-based filtering. If neither is given, no position-based
     * filtering is applied.
     * </p>
     *
     * @param annoIter The iterator to use to retrieve all annotations that are candidates for collection.
     * @param annoModel The annotation model to use to retrieve the positions of the annotations.
     * @param annos The annotations collected so far. Is modified in-place.
     * @param lineNumber The 0-based index of the line number for which to return the hover info, or {@code -1} if not
     *     available.
     * @param offset The 0-based index of the character in the input for which to return the hover info, or {@code -1}
     *     if not available.
     */
    private void getAnnotations(Iterator<Annotation> annoIter, IAnnotationModel annoModel, List<Annotation> annos,
            int lineNumber, int offset)
    {
        while (annoIter.hasNext()) {
            Annotation annotation = annoIter.next();

            // Filter out annotations, based on their type.
            if (excludeAnnoByType(annotation)) {
                continue;
            }

            // Get annotation position.
            Position position = annoModel.getPosition(annotation);
            if (position == null) {
                continue;
            }

            // Filter based on position.
            if (lineNumber != -1) {
                // Make sure annotation overlaps with the given line.
                IDocument doc = sourceViewer.getDocument();
                int line;
                try {
                    line = doc.getLineOfOffset(position.getOffset());
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                if (line != lineNumber) {
                    continue;
                }
            } else if (offset != -1) {
                // Make sure annotation overlaps with the given offset.
                int start = position.getOffset();
                int end = start + position.getLength();
                if (offset < start || end <= offset) {
                    continue;
                }
            }

            // Handle summarized annotations, for folding support.
            if (annotation instanceof AnnotationBag) {
                getAnnotations(((AnnotationBag)annotation).iterator(), annoModel, annos, -1, -1);
                continue;
            }

            // Add annotation.
            annos.add(annotation);
        }
    }

    /**
     * Should the given annotation be excluded due to its type?
     *
     * @param annotation The annotation.
     * @return {@code true} if the annotation is to be excluded, {@code false} otherwise.
     */
    protected boolean excludeAnnoByType(Annotation annotation) {
        if (annotation instanceof MarkerAnnotation) {
            return false;
        }
        if (annotation instanceof SpellingAnnotation) {
            return false;
        }
        if (annotation instanceof AnnotationBag) {
            return false;
        }
        return true;
    }

    /**
     * Formats multiple messages together into a single message.
     *
     * @param header The header text to use.
     * @param messages The messages to format.
     * @return The formatted message.
     */
    protected String formatMessages(String header, TreeSet<String> messages) {
        // Add header.
        StringBuilder rslt = new StringBuilder();
        rslt.append(header);

        // Add messages
        for (String message: messages) {
            rslt.append("\n");
            rslt.append(" - " + message);
        }

        // Return full message.
        return rslt.toString();
    }
}
