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

package org.eclipse.escet.setext.texteditorbase.highlight;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IDocumentPartitioningListener;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;

/** Read only implementation of {@link IDocument} with a {@link String} as storage. */
public class ReadOnlyDocument implements IDocument {
    /** The text of the document. */
    public final String text;

    /** The length of the text of the document. */
    public final int length;

    /**
     * Constructor for the {@link ReadOnlyDocument} class.
     *
     * @param text The text of the document.
     */
    public ReadOnlyDocument(String text) {
        this.text = text;
        this.length = text.length();
    }

    @Override
    public char getChar(int offset) throws BadLocationException {
        if (offset < 0) {
            throw new BadLocationException();
        }
        if (offset >= length) {
            throw new BadLocationException();
        }
        return text.charAt(offset);
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public String get() {
        return text;
    }

    @Override
    public String get(int offset, int length) throws BadLocationException {
        if (offset < 0) {
            throw new BadLocationException();
        }
        if (length < 0) {
            throw new BadLocationException();
        }
        if (offset + length > this.length) {
            throw new BadLocationException();
        }
        return text.substring(offset, offset + length);
    }

    @Override
    public void set(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replace(int offset, int length, String text) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addDocumentListener(IDocumentListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeDocumentListener(IDocumentListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPrenotifiedDocumentListener(IDocumentListener documentAdapter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePrenotifiedDocumentListener(IDocumentListener documentAdapter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPositionCategory(String category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePositionCategory(String category) throws BadPositionCategoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getPositionCategories() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsPositionCategory(String category) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPosition(Position position) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePosition(Position position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPosition(String category, Position position)
            throws BadLocationException, BadPositionCategoryException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePosition(String category, Position position) throws BadPositionCategoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Position[] getPositions(String category) throws BadPositionCategoryException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsPosition(String category, int offset, int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int computeIndexInCategory(String category, int offset)
            throws BadLocationException, BadPositionCategoryException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addPositionUpdater(IPositionUpdater updater) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePositionUpdater(IPositionUpdater updater) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertPositionUpdater(IPositionUpdater updater, int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IPositionUpdater[] getPositionUpdaters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getLegalContentTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentType(int offset) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ITypedRegion getPartition(int offset) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ITypedRegion[] computePartitioning(int offset, int length) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addDocumentPartitioningListener(IDocumentPartitioningListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeDocumentPartitioningListener(IDocumentPartitioningListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDocumentPartitioner(IDocumentPartitioner partitioner) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IDocumentPartitioner getDocumentPartitioner() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLineLength(int line) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLineOfOffset(int offset) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLineOffset(int line) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public IRegion getLineInformation(int line) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public IRegion getLineInformationOfOffset(int offset) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfLines() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfLines(int offset, int length) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int computeNumberOfLines(String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getLegalLineDelimiters() {
        // Returns unique copy each time, to ensure that if caller modifies the
        // array subsequent calls to this method still return a proper result.
        return new String[] {"\r", "\n", "\r\n"};
    }

    @Override
    public String getLineDelimiter(int line) throws BadLocationException {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public int search(int startOffset, String findString, boolean forwardSearch, boolean caseSensitive,
            boolean wholeWord) throws BadLocationException
    {
        throw new UnsupportedOperationException();
    }
}
