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

package org.eclipse.escet.setext.texteditorbase.commands;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.setext.texteditorbase.GenericTextEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.handlers.HandlerUtil;

/** Comment/uncomment text command handler common functionality. */
public abstract class CommentHandlerBase extends AbstractHandler {
    /**
     * Returns the name of the command, to use in error messages.
     *
     * @param event An event containing all the information about the current state of the application.
     * @return The name of the command, to use in error messages.
     */
    protected String getCommandName(ExecutionEvent event) {
        Command command = event.getCommand();
        try {
            return command.getName();
        } catch (NotDefinedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the currently active {@link GenericTextEditor}. If the currently active text editor is not a
     * {@link GenericTextEditor}, {@code null} is returned, and the user is informed of the problem.
     *
     * @param event An event containing all the information about the current state of the application.
     * @return The generic text editor, or {@code null}.
     */
    protected GenericTextEditor<?, ?, ?> getTextEditor(ExecutionEvent event) {
        // Get generic text editor.
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        Assert.notNull(part);
        Assert.check(part.getAdapter(TextEditor.class) != null);

        GenericTextEditor<?, ?, ?> editor = null;
        if (part instanceof GenericTextEditor) {
            editor = (GenericTextEditor<?, ?, ?>)part;
        } else {
            Object obj = part.getAdapter(GenericTextEditor.class);
            editor = (GenericTextEditor<?, ?, ?>)obj;
        }

        if (editor == null) {
            String name = getEditorName(event);
            String msg = fmt("The active %stext editor is not supported by the \"%s\" command.",
                    (name == null) ? "" : fmt("\"%s\" ", name), getCommandName(event));
            String title = getCommandName(event) + " Command Error";
            MessageDialog.openError(part.getSite().getShell(), title, msg);
            return null;
        }
        return editor;
    }

    /**
     * Returns the single line comment characters for the given {@link GenericTextEditor}. If the text editor does not
     * support single line comments, {@code null} is returned, and the user is informed of the problem.
     *
     * @param event An event containing all the information about the current state of the application.
     * @param editor The text editor for which to return the single line comment characters.
     * @return The single line comment characters, or {@code null}.
     * @see GenericTextEditor#singleLineCommentChars
     */
    protected String getCommentChars(ExecutionEvent event, GenericTextEditor<?, ?, ?> editor) {
        String chars = editor.singleLineCommentChars;
        if (chars == null) {
            IWorkbenchPart part = HandlerUtil.getActivePart(event);
            Assert.notNull(part);
            String name = getEditorName(event);
            String msg = fmt("The active %stext editor does not support single line comments.",
                    (name == null) ? "" : fmt("\"%s\" ", name));
            String title = getCommandName(event) + " Command Error";
            MessageDialog.openError(part.getSite().getShell(), title, msg);
            return null;
        }
        return chars;
    }

    /**
     * Returns the document of the editor.
     *
     * @param event An event containing all the information about the current state of the application.
     * @param editor The text editor for which to return the document.
     * @return The document of the editor.
     */
    protected IDocument getDocument(ExecutionEvent event, GenericTextEditor<?, ?, ?> editor) {
        IEditorInput input = HandlerUtil.getActiveEditorInput(event);
        Assert.notNull(input);
        IDocument doc = editor.getDocumentProvider().getDocument(input);
        Assert.notNull(doc);
        return doc;
    }

    /**
     * Returns the currently active text selection of the editor.
     *
     * @param editor The text editor for which to return the currently active text selection.
     * @return The currently active text selection of the editor.
     */
    protected ITextSelection getSelection(GenericTextEditor<?, ?, ?> editor) {
        // Don't obtain it using HandlerUtil, as it doesn't give the correct
        // selection.
        ISelection selection = editor.getSelectionProvider().getSelection();
        Assert.check(selection instanceof ITextSelection);
        return (ITextSelection)selection;
    }

    /**
     * Returns the line number of the start line of the selection. If the start line can't be obtained, {@code -1} is
     * returned, and the user is informed of the problem.
     *
     * @param event An event containing all the information about the current state of the application.
     * @param selection The selection for which to return the start line.
     * @return The line number of the start line, or {@code -1}.
     */
    protected int getStartLine(ExecutionEvent event, ITextSelection selection) {
        int startLine = selection.getStartLine();
        if (startLine < 0) {
            IWorkbenchPart part = HandlerUtil.getActivePart(event);
            String title = getCommandName(event) + " Command Error";
            MessageDialog.openError(part.getSite().getShell(), title, "Failed to obtain selection start line.");
        }
        return startLine;
    }

    /**
     * Returns the line number of the end line of the selection. If the end line can't be obtained, {@code -1} is
     * returned, and the user is informed of the problem.
     *
     * @param event An event containing all the information about the current state of the application.
     * @param selection The selection for which to return the end line.
     * @return The line number of the end line, or {@code -1}.
     */
    protected int getEndLine(ExecutionEvent event, ITextSelection selection) {
        int endLine = selection.getEndLine();
        if (endLine < 0) {
            IWorkbenchPart part = HandlerUtil.getActivePart(event);
            String title = getCommandName(event) + " Command Error";
            MessageDialog.openError(part.getSite().getShell(), title, "Failed to obtain selection end line.");
        }
        return endLine;
    }

    /**
     * Returns the name of the active site workbench part, which is the text editor that is currently active.
     *
     * @param event An event containing all the information about the current state of the application.
     * @return The name of text editor, or {@code null} if not available.
     */
    private static String getEditorName(ExecutionEvent event) {
        IWorkbenchSite site = HandlerUtil.getActiveSite(event);
        if (site != null && site instanceof IWorkbenchPartSite) {
            return ((IWorkbenchPartSite)site).getRegisteredName();
        }
        return null;
    }
}
