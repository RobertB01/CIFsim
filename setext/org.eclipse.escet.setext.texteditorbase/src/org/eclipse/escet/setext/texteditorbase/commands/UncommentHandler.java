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

package org.eclipse.escet.setext.texteditorbase.commands;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.jface.text.DocumentRewriteSessionType.SEQUENTIAL;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.texteditorbase.GenericTextEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

/** Comment text command handler. */
public class UncommentHandler extends CommentHandlerBase {
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // Get generic text editor.
        GenericTextEditor<?, ?, ?> editor = getTextEditor(event);
        if (editor == null) {
            return null;
        }

        // Get single line comment characters.
        String chars = getCommentChars(event, editor);
        if (chars == null) {
            return null;
        }

        // Get document and text selection.
        IDocument doc = getDocument(event, editor);
        ITextSelection textSelection = getSelection(editor);

        // Get start and end line numbers.
        int startLine = getStartLine(event, textSelection);
        int endLine = getEndLine(event, textSelection);
        if (startLine < 0 || endLine < 0) {
            return null;
        }

        // Start document rewrite session, to ensure the entire operation can
        // be undone with a single 'undo'.
        IDocumentExtension4 docExt = (IDocumentExtension4)doc;
        DocumentRewriteSession session = docExt.startRewriteSession(SEQUENTIAL);

        // Uncomment (partially) selected lines.
        try {
            Assert.check(startLine <= endLine);
            for (int nr = startLine; nr <= endLine; nr++) {
                try {
                    IRegion region = doc.getLineInformation(nr);
                    String line = doc.get(region.getOffset(), region.getLength());

                    int whitespacePrefixLen = getWhitespacePrefixLength(line);
                    String line2 = line.substring(whitespacePrefixLen);
                    if (!line2.startsWith(chars)) {
                        // Skip lines that don't start with the single line
                        // comment characters, after potential whitespace.
                        continue;
                    }

                    boolean spaceAfter = line2.startsWith(chars + " ");
                    int count = chars.length() + (spaceAfter ? 1 : 0);

                    doc.replace(region.getOffset() + whitespacePrefixLen, count, "");
                } catch (BadLocationException e) {
                    IWorkbenchPart part = HandlerUtil.getActivePart(event);
                    Shell shell = part.getSite().getShell();
                    String title = getCommandName(event) + " Command Error";
                    String msg = fmt("Failed to uncomment line %d.", nr);
                    MessageDialog.openError(shell, title, msg);
                    return null;
                }
            }
        } finally {
            // Make sure we always stop the rewrite session.
            docExt.stopRewriteSession(session);
        }

        // Done.
        return null;
    }

    /**
     * Returns the length of the prefix of the given line that contains only whitespace.
     *
     * @param line The line.
     * @return The length of the prefix of the given line that contains only whitespace.
     */
    private int getWhitespacePrefixLength(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                // No more whitespace.
                return i;
            }
        }

        // All whitespace.
        return line.length();
    }
}
