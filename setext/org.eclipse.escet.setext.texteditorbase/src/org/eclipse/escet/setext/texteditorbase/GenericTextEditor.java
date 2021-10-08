//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticProblem;
import org.eclipse.escet.common.typechecker.TypeChecker;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.Parser;
import org.eclipse.escet.setext.runtime.SyntaxWarning;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;
import org.eclipse.escet.setext.texteditorbase.scanners.GenericPartitionScanner;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.MarkerUtilities;

/**
 * Generic text editor for Eclipse.
 *
 * @param <T1> The type of the abstract syntax tree that results from parsing. If no parser is available, use a dummy
 *     type, such as {@link Object}.
 * @param <T2> The type of the decorated abstract syntax tree that results from type checking. If no type checker is
 *     available, use a dummy type, such as {@link Object}.
 */
public class GenericTextEditor<T1, T2> extends TextEditor implements IDocumentListener, IPartListener {
    /** Whether to print debugging information related to validation timing. */
    private static final boolean DEBUG_TIMING = false;

    /** Whether to print debugging information for the scanner. */
    private static final boolean DEBUG_SCANNER = false;

    /** Whether to print debugging information for the parser. */
    private static final boolean DEBUG_PARSER = false;

    /** The id of the 'presentation' action set. */
    private static final String PRESENTATION_ACTION_SET_ID = "org.eclipse.ui.edit.text.actionSet.presentation";

    /**
     * The single line comment characters for the language supported by this text editor, or {@code null} if not
     * applicable.
     */
    public final String singleLineCommentChars;

    /**
     * Whether to perform continuous validation. If {@code false}, validation is only performed on load and save. If
     * {@code true}, validation is additionally performed on every document change, after a short delay.
     */
    public static final boolean CONTINUOUS_VALIDATION = true;

    /** Color manager used to share color resources. */
    private final ColorManager colorManager;

    /** The partition scanner to use for the text editor. */
    private final GenericPartitionScanner scanner;

    /** The SeText generated parser class to use for parsing, or {@code null} if no parser is available. */
    private final Class<? extends Parser<T1>> parserClass;

    /** The type checker class to use for type checking, or {@code null} if no type checker is available. */
    private final Class<? extends TypeChecker<T1, T2>> typeCheckerClass;

    /** The syntax problem marked identifier, or {@code null} if {@link #parserClass} is {@code null}. */
    private final String syntaxProblemMarkerId;

    /** The semantic problem marked identifier, or {@code null} if {@link #typeCheckerClass} is {@code null}. */
    private final String semanticProblemMarkerId;

    /** Folding support. */
    private final GenericTextEditorFolding folding = new GenericTextEditorFolding(this);

    /** The input of the editor, or {@code null} if unknown. */
    private IFileEditorInput input;

    /** Locking object used ensure mutually exclusive validation. */
    protected final Object validationLock = new Object();

    /** Counter used to ensure that obsolete validations can be discarded. */
    protected final AtomicInteger validationCount = new AtomicInteger(0);

    /**
     * Constructor for the {@link GenericTextEditor} class.
     *
     * @param scanner The partition scanner to use for the text editor.
     * @param viewerConfig The text editor source viewer configuration to use for the text editor.
     * @param parserClass The SeText generated parser class to use for parsing, or {@code null} if no parser is
     *     available.
     * @param typeCheckerClass The type checker class to use for type checking, or {@code null} if no type checker is
     *     available.
     * @param syntaxProblemMarkerId The syntax problem marked identifier, or {@code null} if {@code parserClass} is
     *     {@code null}.
     * @param semanticProblemMarkerId The semantic problem marked identifier, or {@code null} if
     *     {@code typeCheckerClass} is {@code null}.
     * @param singleLineCommentChars The single line comment characters for the language supported by this text editor,
     *     or {@code null} if not applicable.
     */
    public GenericTextEditor(GenericPartitionScanner scanner, GenericSourceViewerConfiguration viewerConfig,
            Class<? extends Parser<T1>> parserClass, Class<? extends TypeChecker<T1, T2>> typeCheckerClass,
            String syntaxProblemMarkerId, String semanticProblemMarkerId, String singleLineCommentChars)
    {
        this.scanner = scanner;
        this.parserClass = parserClass;
        this.typeCheckerClass = typeCheckerClass;
        this.syntaxProblemMarkerId = syntaxProblemMarkerId;
        this.semanticProblemMarkerId = semanticProblemMarkerId;
        this.singleLineCommentChars = singleLineCommentChars;
        colorManager = new ColorManager();
        viewerConfig.setColorManager(colorManager);
        viewerConfig.setPartitionScanner(scanner);
        viewerConfig.setPreferenceStore(getPreferenceStore());
        setSourceViewerConfiguration(viewerConfig);
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        // Perform normal initialization.
        super.init(site, input);

        // Show the 'presentation' action set with the 'Toggle Block Selection
        // Mode' and 'Show Whitespace Characters' actions.
        IWorkbenchPage page = site.getPage();
        page.showActionSet(PRESENTATION_ACTION_SET_ID);

        // Register part listener.
        getSite().getWorkbenchWindow().getPartService().addPartListener(this);
    }

    @Override
    protected void doSetInput(IEditorInput newInput) throws CoreException {
        // Handle moved or renamed file.
        if (input != null) {
            // We already have an input file, so the file was renamed or moved.
            Assert.check(newInput instanceof IFileEditorInput);
            IFileEditorInput newFileInput = ((IFileEditorInput)newInput);

            // Get the default editor for the new file name.
            String newFileName = newFileInput.getName();
            IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
            IEditorDescriptor editor = registry.getDefaultEditor(newFileName);

            // Check new editor against current one.
            String curEditorId = getEditorSite().getId();
            String newEditorId = editor.getId();
            boolean sameEditor = curEditorId.equals(newEditorId);

            // If the new editor is different from the current editor, clear
            // all problems for the file (the new file, as the old one no
            // longer exists). This is to ensure that the problems don't stick
            // around for the new editor, which only removes its own problem
            // types.
            if (!sameEditor) {
                IFile newFile = newFileInput.getFile();
                try {
                    newFile.deleteMarkers(null, true, IResource.DEPTH_ZERO);
                } catch (CoreException ex) {
                    String msg = "Failed to delete problem markers.";
                    throw new RuntimeException(msg, ex);
                }
            }

            // Note that moving or renaming is impossible if any open files
            // still have changes. Furthermore, renaming uses a modal dialog,
            // so no new changes can be introduced while renaming. As such, it
            // doesn't matter whether or not we save the changes of the editor
            // when we close it, as there are no changes. However, since we
            // have to make a choice, we save them, just to be safe.
            close(true);

            // Save the folding state for the new file.
            folding.save(newFileInput);

            // Open the default editor for the new file.
            getSite().getPage().openEditor(newInput, editor.getId());

            // Don't continue, as this editor is now closed.
            return;
        }

        // Defer to default implementation, before performing additional tasks.
        super.doSetInput(newInput);

        // Add partitioner that uses our partition scanner.
        IDocument document = getDocumentProvider().getDocument(newInput);
        IDocumentPartitioner partitioner = new FastPartitioner(scanner, scanner.getTypes());
        partitioner.connect(document);
        document.setDocumentPartitioner(partitioner);

        // Check input.
        Assert.notNull(newInput);
        if (!(newInput instanceof IFileEditorInput)) {
            // We don't support external files, from outside of the workspace.
            //
            // Two ways to open external files:
            // 1) Drag/drop such a file from a file manager outside of Eclipse,
            // onto the Eclipse window.
            // 2) Via the 'File' / 'Open File...' menu.
            //
            // How other text editors handle this, for files not in the
            // workspace:
            // 1) Java: it opens correctly, and has syntax highlighting, but no
            // error checking, refactoring, etc. Can be edited/saved.
            // This is confusing, since users may assume there are no problems
            // with the file.
            // 2) EMFText generated editors: it opens in the default text
            // editor, so no syntax highlighting, error checking, etc. Can be
            // edited/saved. This is confusing, since users may not understand
            // why there is no highlighting, etc.
            //
            // Implementation aspects:
            // 1) GenericTextEditor.doSetInput has a parameter of type
            // org.eclipse.ui.IEditorInput. Normally, we get
            // org.eclipse.ui.IFileEditorInput, and this works as expected.
            // For external files, we get
            // org.eclipse.ui.ide.FileStoreEditorInput instead.
            // 2) Getting document returns null for FileStoreEditorInput.
            // Thus, no way to set a listener for document text changes.
            // Thus, no way to get the text of the document (except maybe
            // from file itself, but that does not have the local/unsaved
            // changes from the editor).
            // 3) No way to get an IFile object for a FileStoreEditorInput,
            // to which to add resource markers (for errors and warnings).
            // There are no resource objects for external files, except maybe
            // via linked resources or virtual folders. Resource markers can
            // only be added to resources (workspace root, projects, folders,
            // files) in the workspace.
            // 4) We may not have a way to get absolute local file system
            // paths for external files, needed for the type checker, for
            // import cycle detection: But I haven't checked this, as the
            // code never got that far, so it may very well be possible.

            // Get location. Special case for FileStoreEditorInput, to get
            // full URI instead of just the file name.
            String loc;
            if (newInput instanceof FileStoreEditorInput) {
                loc = ((FileStoreEditorInput)newInput).getURI().toString();
            } else {
                loc = newInput.getName();
            }

            // We report the input being unsupported, using the name of the
            // editor class instead of the name of the plug-in that the editor
            // class is a part of.
            String title = fmt("Could not open an editor for \"%s\": it is not a file, the file is not on a local file "
                    + "system, or the file is not part of any workspace project.", loc);
            String pluginName = GenericTextEditor.this.getClass().getName();
            Status status = new Status(IStatus.ERROR, pluginName, title);
            throw new CoreException(status);
        }

        // Store input.
        input = (IFileEditorInput)newInput;

        // Register for document changes.
        getDocumentProvider().getDocument(newInput).addDocumentListener(this);

        // Load folding state.
        folding.load();

        // There is no need to perform validation on the new input. We had no
        // no input before, since the editor is being opened. It will
        // subsequently also be activated, which already triggers validation.
        // In other words, all cases are already covered, and there is nothing
        // to do here.
    }

    @Override
    public void createPartControl(Composite parent) {
        // Make sure other parts are created as well.
        super.createPartControl(parent);

        // Additional part control creation for folding support.
        folding.createPartControl(getSourceViewer(), getAnnotationAccess(), getSharedColors());
    }

    @Override
    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        // Create source viewer with folding support.
        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(),
                styles);

        // Ensure decoration support has been created and configured.
        getSourceViewerDecorationSupport(viewer);
        return viewer;
    }

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        stripTrailingWhitespace();
        addNewLineAtEof();
        super.doSave(progressMonitor);
    }

    @Override
    public void doSaveAs() {
        stripTrailingWhitespace();
        addNewLineAtEof();
        super.doSaveAs();
    }

    @Override
    public void documentAboutToBeChanged(DocumentEvent event) {
        // Nothing to do here.
    }

    @Override
    public void documentChanged(DocumentEvent event) {
        if (!CONTINUOUS_VALIDATION) {
            return;
        }

        // Start a thread that will validate the document after a delay.
        Thread t = new DelayedValidate(validationCount.incrementAndGet());
        t.setName(fmt("%s-DelayedValidateThread-%d", getClass().getSimpleName(), t.getId()));
        t.start();
    }

    @Override
    protected void handleEditorInputChanged() {
        super.handleEditorInputChanged();
        validate(true);
    }

    /**
     * Returns a text with issue reporting instructions, for the UI report to users in cas of a validation crash.
     *
     * <p>
     * The default implementation is specific for Eclipse ESCET. Derived classes may override this method to provide
     * instructions matching their own text editor.
     * </p>
     *
     * @return The text with issue reporting instructions.
     */
    public String getValidationCrashIssueReportingInstructions() {
        return "Validation crashed. Please report this to the Eclipse ESCET development team at "
                + "https://gitlab.eclipse.org/eclipse/escet/escet/-/issues. For more information, see "
                + "https://eclipse.org/escet/escet/issue-reporting.html. We appreciate you "
                + "taking the effort to report issues to us!";
    }

    /**
     * Performs validation by parsing and type checking the text document. Updates the problem markers based on the
     * parse and type check results.
     *
     * @param acquireLock Whether to acquire the lock. If called from outside this method, always pass {@code true}.
     */
    protected void validate(boolean acquireLock) {
        // Ensure mutual exclusion of validation, for thread safety.
        if (acquireLock) {
            synchronized (validationLock) {
                validate(false);
                return;
            }
        }

        // Perform validation, reporting validation errors to the end user via the Eclipse UI.
        try {
            validateInternal();
        } catch (Throwable ex) {
            String title = getValidationCrashIssueReportingInstructions();
            Status status = new Status(IStatus.ERROR, getClass(), IStatus.OK, title, ex);
            int style = StatusManager.LOG | StatusManager.SHOW;
            StatusManager.getManager().handle(status, style);
        }
    }

    /**
     * Performs validation by parsing and type checking the text document. Updates the problem markers based on the
     * parse and type check results.
     *
     * <p>
     * This method should only be invoked directly by the {@link #validate} method. To perform validation, use the
     * {@link #validate} method instead of this method.
     * </p>
     */
    private void validateInternal() {
        // Safety checks.
        Assert.notNull(input);

        // Time this method for debugging.
        long startTime = System.nanoTime();

        // Get document and input file.
        IDocumentProvider docProvider = getDocumentProvider();
        if (docProvider == null) {
            return; // Editor closed.
        }

        final IDocument document = docProvider.getDocument(input);
        if (document == null) {
            return; // Editor closed.
        }

        IFile file = input.getFile();
        if (!file.exists()) {
            return; // File no longer exists.
        }

        // Get document text. We execute the 'get' on the UI thread, to prevent
        // modification while getting the text.
        final Display display = getSite().getShell().getDisplay();
        if (display.isDisposed()) {
            return;
        }

        final String[] text = new String[1];
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                if (display.isDisposed()) {
                    return;
                }
                text[0] = document.get();
            }
        });

        // Clear all current problem markers.
        try {
            file.deleteMarkers(syntaxProblemMarkerId, true, IResource.DEPTH_ZERO);
        } catch (CoreException ex) {
            // Ignore failures: the file probably no longer exists.
        }

        try {
            file.deleteMarkers(semanticProblemMarkerId, true, IResource.DEPTH_ZERO);
        } catch (CoreException ex) {
            // Ignore failures: the file probably no longer exists.
        }

        // Get absolute local file system path to source file, with platform
        // specific path separators.
        IPath fileLocation = file.getLocation();
        if (fileLocation == null) {
            return; // File no longer exists.
        }
        String absPath = fileLocation.toOSString();

        // Parse the text. We don't provide source information, as we don't
        // want that to end up in the exception message. Instead, the source
        // information is added separately to the problem markers.
        T1 parseRslt = null;
        final Parser<T1> parser;
        if (parserClass != null) {
            // Create fresh parser, to avoid interference between subsequent
            // parsing operations.
            try {
                parser = parserClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to create parser.", e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Failed to create parser.", e);
            } catch (SecurityException e) {
                throw new RuntimeException("Failed to create parser.", e);
            }

            // Enable collection of fold ranges.
            parser.foldRanges = list();

            // Parse the document text. If we ask for debugging, register
            // this thread with the application framework. This is not
            // thread-safe, and should be used for debugging only!
            DebugMode debugMode = DebugMode.getDebugMode(DEBUG_SCANNER, DEBUG_PARSER);
            if (debugMode != DebugMode.NONE) {
                AppEnv.registerSimple();
                Options.set(OutputModeOption.class, OutputMode.NORMAL);
            }

            try {
                parseRslt = parser.parseString(text[0], absPath, debugMode);
            } catch (SyntaxException ex) {
                // Add marker for syntax error.
                addMarker(document, file, ex.getPosition(), ex.getMessage(), syntaxProblemMarkerId,
                        IMarker.SEVERITY_ERROR);
            } finally {
                if (debugMode != DebugMode.NONE) {
                    AppEnv.unregisterApplication();
                }
            }

            // Add markers for syntax warnings.
            for (SyntaxWarning warning: parser.getWarnings()) {
                addMarker(document, file, warning.position, warning.message, syntaxProblemMarkerId,
                        IMarker.SEVERITY_WARNING);
            }

            // Update fold ranges.
            if (parseRslt != null) {
                // Fix the fold ranges.
                folding.fixFolds(parser.foldRanges, text[0]);

                // Update the fold ranges, on the UI thread.
                display.syncExec(new Runnable() {
                    @Override
                    public void run() {
                        if (display.isDisposed()) {
                            return;
                        }
                        folding.updateFolds(parser.foldRanges, text[0]);
                    }
                });
            }
        }

        // Perform type checking on the parse result.
        TypeChecker<T1, T2> tchecker = null;
        try {
            if (parseRslt != null && typeCheckerClass != null) {
                tchecker = typeCheckerClass.getDeclaredConstructor().newInstance();
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate type checker: " + typeCheckerClass, e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to instantiate type checker: " + typeCheckerClass, e);
        } catch (SecurityException e) {
            throw new RuntimeException("Failed to instantiate type checker: " + typeCheckerClass, e);
        }

        if (tchecker != null) {
            // Set source file path.
            tchecker.setSourceFilePath(absPath);

            // Perform type checking.
            tchecker.typeCheck(parseRslt);

            // Add a marker for each problem.
            for (SemanticProblem problem: tchecker.getProblems()) {
                int severity;
                switch (problem.severity) {
                    case WARNING:
                        severity = IMarker.SEVERITY_WARNING;
                        break;
                    case ERROR:
                        severity = IMarker.SEVERITY_ERROR;
                        break;
                    default:
                        String msg = "Unknown severity: " + problem.severity;
                        throw new RuntimeException(msg);
                }
                addMarker(document, file, problem.position, problem.message, semanticProblemMarkerId, severity);
            }
        }

        // Print method execution time for debugging.
        if (DEBUG_TIMING) {
            double msecs = (System.nanoTime() - startTime) / 1e6;
            System.out.format("Validation completed in %s milliseconds.\n", msecs);
        }
    }

    /**
     * Adds a problem marker for a syntax or semantic problem.
     *
     * @param document The document that contains the problem.
     * @param file The file that contains the problem.
     * @param position The position information to use for the creation of the marker.
     * @param msg The problem message.
     * @param markerId The marker identifier to use to create the marker.
     * @param severity The Eclipse text editor marker severity. See also {@link IMarker#SEVERITY}.
     */
    private void addMarker(IDocument document, IFile file, Position position, String msg, String markerId,
            int severity)
    {
        // Get start and end offsets.
        int start = position.getStartOffset();
        int end = position.getEndOffset() + 1;
        if (end >= document.getLength()) {
            // Correct for premature EOF, which is just one character after
            // the end of the document.
            Assert.check(start >= 0);
            end = document.getLength();
            if (start >= end) {
                start = end - 1;
            }
            if (start < 0) {
                start = 0;
                end = 1;
            }
        }
        Assert.check(start < end);

        // Construct marker attributes.
        Map<String, Object> map = map();
        map.put(IMarker.LINE_NUMBER, position.getStartLine());
        map.put(IMarker.MESSAGE, msg);
        map.put(IMarker.LOCATION, fmt("line %d, column %d", position.getStartLine(), position.getStartColumn()));
        map.put(IMarker.SEVERITY, severity);
        map.put(IMarker.CHAR_START, start);
        map.put(IMarker.CHAR_END, end);

        // Create marker.
        try {
            MarkerUtilities.createMarker(file, map, markerId);
        } catch (CoreException ex) {
            // Ignore failures: the file probably no longer exists.
        }
    }

    /** Strip trailing whitespace from the document. */
    protected void stripTrailingWhitespace() {
        // Safety checks.
        Assert.notNull(input);

        // Get document.
        IDocument document = getDocumentProvider().getDocument(input);
        FindReplaceDocumentAdapter adapter = new FindReplaceDocumentAdapter(document);

        // Perform a search and replace for trailing whitespace.
        int offset = 0;
        String searchPattern = "[ \t]+$";
        while (offset < document.getLength()) {
            // Search. If not found, we are done.
            IRegion region;
            try {
                region = adapter.find(offset, searchPattern, true, false, false, true);
            } catch (BadLocationException ex) {
                String msg = "Strip trailing whitespace find failure.";
                throw new RuntimeException(msg, ex);
            }
            if (region == null) {
                break;
            }

            // Found, so replace.
            try {
                adapter.replace("", false);
            } catch (BadLocationException ex) {
                String msg = "Strip trailing whitespace replace failure.";
                throw new RuntimeException(msg, ex);
            }

            // Keep searching from where we found the last match.
            offset = region.getOffset();
        }
    }

    /** Add new line characters at end of document, if not yet present. If the document is empty, nothing is added. */
    protected void addNewLineAtEof() {
        // Safety checks.
        Assert.notNull(input);

        // Get document.
        IDocument document = getDocumentProvider().getDocument(input);

        // Skip empty documents.
        int size = document.getLength();
        if (size == 0) {
            return;
        }

        // Get last line (always exists, never ends with new line characters).
        String lastLine = null;
        int lineCount = document.getNumberOfLines();
        try {
            int lastOffset = document.getLineOffset(lineCount - 1);
            int lastLength = document.getLineLength(lineCount - 1);
            lastLine = document.get(lastOffset, lastLength);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }

        // If last line is empty, we are done.
        if (lastLine.isEmpty()) {
            return;
        }

        // Get used new line characters from first line, if any. Otherwise, use
        // platform new line characters.
        String nlChars;
        try {
            nlChars = document.getLineDelimiter(0);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        if (nlChars == null) {
            nlChars = Strings.NL;
        }

        // Replace last line.
        try {
            document.replace(size - lastLine.length(), lastLine.length(), lastLine + nlChars);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    /** Delayed validation thread. */
    private class DelayedValidate extends Thread {
        /** The unique count for this validation. */
        private final int cnt;

        /**
         * Constructor for the {@link DelayedValidate} class.
         *
         * @param cnt The unique count for this validation.
         */
        public DelayedValidate(int cnt) {
            this.cnt = cnt;
        }

        @Override
        public void run() {
            // Delay the validation, for performance reasons.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException("Unexpected thread interrupt.", e);
            }

            // Wait to acquire the lock.
            synchronized (validationLock) {
                // We have to lock. Cancel the request if it is outdated.
                if (validationCount.get() != cnt) {
                    return;
                }

                // We have the lock, to validate without requesting it again.
                validate(false);
            }
        }
    }

    @Override
    public void partActivated(IWorkbenchPart part) {
        // Ignore if not for this part.
        if (part != this) {
            return;
        }

        // If this part is activated, perform validation. This is used to
        // ensure that files that depend on other files (for instance by using
        // imports), properly reflect the state of their dependencies.
        validate(true);
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
        // Nothing to do here.
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
        // Ignore if not for this part.
        if (part != this) {
            return;
        }

        // Unregister part listener if this editor is closed, to allow garbage
        // collection of this editor.
        IPartService ps = getSite().getWorkbenchWindow().getPartService();
        ps.removePartListener(this);

        // Save folding state. Fails if the file no longer exists, for instance
        // because it has been renamed. However, failures are ignored, so it's
        // not a problem.
        folding.save(input);
    }

    @Override
    public void partDeactivated(IWorkbenchPart part) {
        // Nothing to do here.
    }

    @Override
    public void partOpened(IWorkbenchPart part) {
        // Nothing to do here.
    }
}
