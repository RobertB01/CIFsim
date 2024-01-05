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

package org.eclipse.escet.common.app.framework.options;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.AppEnvData;
import org.eclipse.escet.common.app.framework.SWTUtils;
import org.eclipse.escet.common.app.framework.io.MemAppStream;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.ApplicationException;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/** Option dialog that can be used to graphically query the user for values for all of the options of an application. */
public class OptionDialog implements Runnable {
    /** The options wrapper category, with all categories and all options that apply to the current dialog. */
    private final OptionCategory options;

    /** The SWT display to use for the dialog. */
    private final Display display;

    /** The SWT shell to use for the dialog. */
    protected final Shell shell;

    /** The option pages, one for each option category. */
    private SashForm pages;

    /** Mapping from option category tree items to option pages. */
    private Map<TreeItem, Composite> categoryPageMap = map();

    /** Mapping from options to their option groups. */
    @SuppressWarnings("rawtypes")
    private Map<Option, OptionGroup> optionGroupMap = map();

    /**
     * The command line arguments that represent the values that the user selected in the option dialog. The value of
     * this field is {@code null} as long as the dialog is still open. It remains {@code null} if the dialog was
     * canceled by the user.
     */
    protected String[] cmdLine = null;

    /**
     * Constructor for the {@link OptionDialog} class.
     *
     * @param display The SWT display to use for the dialog.
     * @param options The options wrapper category, with all categories and all options that apply to the current
     *     dialog.
     */
    protected OptionDialog(Display display, OptionCategory options) {
        this.options = options;
        this.display = display;
        this.shell = new Shell(display);

        show();
    }

    /**
     * This method should not be invoked by the end user. It should only be invoked by the {@link #showDialog} method.
     */
    @Override
    public void run() {
        OptionDialog dlg = new OptionDialog(display, options);
        cmdLine = dlg.cmdLine;
    }

    /**
     * Creates and shows the option dialog, and returns after the dialog is closed.
     *
     * @param options The options wrapper category, with all categories and all options that apply to the current
     *     dialog.
     *
     * @return The command line arguments that correspond to the option values that the user selected in the option
     *     dialog, or {@code null} if the dialog was canceled.
     */
    public static String[] showDialog(final OptionCategory options) {
        // Get the display.
        final Display display = Display.getDefault();

        // Show option dialog in the display, on the UI thread, and wait for
        // it to finish.
        OptionDialogRunner runner = new OptionDialogRunner(display, options, AppEnv.getData());
        display.syncExec(runner);
        if (runner.error != null) {
            throw runner.error;
        }

        // Return the command line arguments, if any.
        return runner.cmdLine;
    }

    /**
     * {@link Runnable} that can create and show an option dialog, and store the resulting command line arguments for
     * retrieval.
     */
    private static class OptionDialogRunner implements Runnable {
        /** The SWT display to use for the dialog. */
        private final Display display;

        /** The options wrapper category, with all categories and all options that apply to the current dialog. */
        private final OptionCategory options;

        /** The application environment data to use for the new thread. */
        private final AppEnvData data;

        /**
         * The resulting command line arguments, or {@code null} if the dialog is still open, was canceled, or an
         * {@link #error} occurred.
         */
        public String[] cmdLine;

        /** The error that occurred, or {@code null} if not yet available or if no error occurred. */
        public ApplicationException error;

        /**
         * Constructor for the {@link OptionDialogRunner} class.
         *
         * @param display The SWT display to use for the dialog.
         * @param options The options wrapper category, with all categories and all options that apply to the current
         *     dialog.
         * @param data The application environment data to use for the new thread.
         */
        public OptionDialogRunner(Display display, OptionCategory options, AppEnvData data) {
            this.display = display;
            this.options = options;
            this.data = data;
        }

        @Override
        public void run() {
            // Make sure UI is still alive.
            Assert.check(!display.isDisposed());

            // Register (or re-register) the GUI thread with the application
            // framework environment manager.
            AppEnv.registerThread(data, true);

            // Show the dialog.
            OptionDialog dlg = null;
            try {
                dlg = new OptionDialog(display, options);
            } catch (ApplicationException ex) {
                // Error occurred. Relay to caller. Most likely an option
                // processing issue.
                this.error = ex;
                return;
            }

            // Save the command line, for later retrieval.
            cmdLine = dlg.cmdLine;
        }
    }

    /** Shows the already created option dialog, and returns after the dialog is closed. */
    private void show() {
        // Configure shell, and show it centered on the primary monitor.
        shell.setText(options.getName());
        SWTUtils.resize(shell, 0.75);
        shell.setMinimumSize(640, 480);
        addComponents();
        SWTUtils.center(shell);
        shell.open();

        // Event loop.
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        // Process any lingering display events. This ensures that the shell
        // is correctly closed.
        while (display.readAndDispatch()) {
            // Nothing to do here...
        }
    }

    /** Adds the components to the option dialog. */
    private void addComponents() {
        // Set up the shell.
        FormLayout shellLayout = new FormLayout();
        shellLayout.marginHeight = 8;
        shellLayout.marginWidth = 8;
        shellLayout.spacing = 8;
        shell.setLayout(shellLayout);

        // Shell contains a splitter and buttons.
        SashForm split = new SashForm(shell, SWT.HORIZONTAL);
        Composite buttons = new Composite(shell, SWT.NONE);

        FormData splitData = new FormData();
        splitData.top = new FormAttachment(0, 0);
        splitData.left = new FormAttachment(0, 0);
        splitData.right = new FormAttachment(100, 0);
        splitData.bottom = new FormAttachment(buttons, 0);
        split.setLayoutData(splitData);

        FormData buttonsData = new FormData();
        buttonsData.left = new FormAttachment(0, 0);
        buttonsData.right = new FormAttachment(100, 0);
        buttonsData.bottom = new FormAttachment(100, 0);
        buttons.setLayoutData(buttonsData);

        // Set up the buttons.
        FormLayout buttonsLayout = new FormLayout();
        buttonsLayout.marginHeight = 0;
        buttonsLayout.marginWidth = 8;
        buttonsLayout.spacing = 8;
        buttons.setLayout(buttonsLayout);

        Button okButton = new Button(buttons, SWT.PUSH);
        Button cancelButton = new Button(buttons, SWT.PUSH);
        okButton.setText("OK");
        cancelButton.setText("Cancel");

        FormData okData = new FormData();
        okData.right = new FormAttachment(cancelButton, 0);
        okButton.setLayoutData(okData);

        FormData cancelData = new FormData();
        cancelData.right = new FormAttachment(100, 0);
        cancelButton.setLayoutData(cancelData);

        // Click event handlers for the OK and Cancel buttons.
        okButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                saveCmdLine();
                shell.close();
            }
        });
        cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });

        // Set up <enter> for OK, and <esc> for Cancel.
        shell.setDefaultButton(okButton);
        shell.addListener(SWT.Traverse, new Listener() {
            @Override
            public void handleEvent(Event event) {
                switch (event.detail) {
                    case SWT.TRAVERSE_ESCAPE: {
                        shell.close();
                        event.detail = SWT.TRAVERSE_NONE;
                        event.doit = false;
                        break;
                    }
                }
            }
        });

        // Set up the splitter. It contains a tree with option categories on
        // the left, and the option pages for the categories on the right.
        Tree tree = new Tree(split, SWT.SINGLE | SWT.BORDER);
        pages = new SashForm(split, SWT.HORIZONTAL);
        split.setWeights(new int[] {1, 3});

        // For each category, add an item to the tree, and add an option page.
        addCategories(tree, null, pages, options);

        // Add the help category.
        addHelpCategory(tree, pages);

        // Add selection listener to the tree, to select the corresponding
        // option page.
        tree.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Assert.check(event.item instanceof TreeItem);
                TreeItem item = (TreeItem)event.item;
                selectCategory(item);
            }
        });

        // Initialize, by selecting the first item in the tree.
        selectCategory(tree.getItem(0));
    }

    /**
     * Selects the option category corresponding to the given tree item, and displays the option page for that option
     * category.
     *
     * @param item The tree item for which to select the corresponding option category.
     */
    protected void selectCategory(TreeItem item) {
        // Get the option page that corresponds to the tree item, and make it
        // visible.
        Composite page = categoryPageMap.get(item);
        Assert.notNull(page);
        pages.setMaximizedControl(page);
    }

    /**
     * Adds the option categories to the option dialog.
     *
     * @param tree The tree to add the root tree items to.
     * @param item The tree item to add the tree items to, or {@code null} to indicate that they should be added to the
     *     tree itself.
     * @param pages The parent control to add option pages to.
     * @param cat The option category for which to add all sub-categories.
     */
    @SuppressWarnings("rawtypes")
    private void addCategories(Tree tree, TreeItem item, SashForm pages, OptionCategory cat) {
        // Process each of the sub categories.
        for (OptionCategory subcat: cat.getSubCategories()) {
            // Add a new item to the categories tree view.
            TreeItem newItem;
            if (item == null) {
                newItem = new TreeItem(tree, SWT.NONE);
            } else {
                newItem = new TreeItem(item, SWT.NONE);
                item.setExpanded(true);
            }
            newItem.setText(subcat.getName());

            // Add a new option page for the sub category.
            final Composite page = new Composite(pages, SWT.BORDER);
            page.setLayout(new FormLayout());

            // Add a scrolled composite, to accommodate the variable height of
            // option pages.
            final ScrolledComposite scroll = new ScrolledComposite(page, SWT.V_SCROLL);
            scroll.setExpandHorizontal(true);
            scroll.setExpandVertical(true);
            FormData scrollData = new FormData();
            scrollData.left = new FormAttachment(0, 0);
            scrollData.top = new FormAttachment(0, 0);
            scrollData.right = new FormAttachment(100, 0);
            scrollData.bottom = new FormAttachment(100, 0);
            scroll.setLayoutData(scrollData);

            // The contents of the page is a vertical stack of option groups,
            // for each of the options.
            final Composite contents = new Composite(scroll, SWT.NONE);
            scroll.setContent(contents);

            FormLayout contentsLayout = new FormLayout();
            contentsLayout.marginWidth = 8;
            contentsLayout.marginHeight = 8;
            contentsLayout.spacing = 8;
            contents.setLayout(contentsLayout);

            // Add controls for each of the options.
            OptionGroup previous = null;
            int optCount = 0;
            for (Option opt: subcat.getOptions()) {
                // Skip options that should not be shown in a dialog.
                if (!opt.showInDialog) {
                    if (opt == Options.getInstance(OptionDialogOption.class)) {
                        // Special case for option dialog option, to make sure
                        // we don't show the dialog again, after this one
                        // closes...
                        Options.set(OptionDialogOption.class, false);
                    }
                    continue;
                }

                // Let the option create its own child control.
                OptionGroup optGrp = opt.createOptionGroup(contents);

                // Set the form data.
                FormData grpData = new FormData();
                grpData.left = new FormAttachment(0, 0);
                grpData.right = new FormAttachment(100, 0);
                grpData.top = (previous == null) ? new FormAttachment(0, 0) : new FormAttachment(previous, 0);
                optGrp.setLayoutData(grpData);

                // Update previous for next loop.
                previous = optGrp;

                // Add the option and group pair to the option/group mapping.
                optionGroupMap.put(opt, optGrp);

                // We have added one more option.
                optCount++;
            }

            // If there are no options (or only options that should not be
            // shown, then add a label.
            if (optCount == 0) {
                Label noOpts = new Label(contents, SWT.NULL);
                noOpts.setText("This option category has no options.");
            }

            // Add a resize listener to the page, to automatically resize the
            // children of the contents to the width of the page.
            page.addListener(SWT.Resize, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    Rectangle bounds = page.getBounds();
                    for (Control c: contents.getChildren()) {
                        Object layoutData = c.getLayoutData();
                        Assert.check(layoutData instanceof FormData);
                        FormData grpData = (FormData)c.getLayoutData();
                        grpData.width = bounds.width - 32;
                    }

                    // Reset the minimum width and height so that the children
                    // can be seen.
                    scroll.setMinSize(contents.computeSize(SWT.DEFAULT, SWT.DEFAULT));
                }
            });

            // Reset the minimum width and height so that the children can be
            // seen.
            scroll.setMinSize(contents.computeSize(SWT.DEFAULT, SWT.DEFAULT));

            // Add tree item and option page pair.
            categoryPageMap.put(newItem, page);

            // Recursively add the sub-sub categories.
            addCategories(tree, newItem, pages, subcat);
        }
    }

    /**
     * Adds the 'Help' option category to the option dialog.
     *
     * @param tree The tree to add the root tree items to.
     * @param pages The parent control to add option pages to.
     */
    private void addHelpCategory(Tree tree, SashForm pages) {
        // Add 'Help' item to the categories tree view.
        TreeItem helpItem = new TreeItem(tree, SWT.NONE);
        helpItem.setText("Help");

        // Add sub items.
        addHelpAboutCategory(helpItem, pages);
        addHelpCmdLineCategory(helpItem, pages);
        addHelpLicenseCategory(helpItem, pages);

        // Expand the 'Help' category.
        helpItem.setExpanded(true);
    }

    /**
     * Adds the 'About' option category to the 'Help' category of the option dialog.
     *
     * @param parentItem The help item to add the category tree item to.
     * @param pages The parent control to add option pages to.
     */
    private void addHelpAboutCategory(TreeItem parentItem, SashForm pages) {
        // Add sub-item to the 'Help' category in the tree view.
        TreeItem item = new TreeItem(parentItem, SWT.NONE);
        item.setText("About");

        // Add a page for the category.
        final Composite page = new Composite(pages, SWT.BORDER);
        page.setLayout(new FormLayout());

        // The contents of the page is just a text box.
        Text txt = new Text(page, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
        txt.setBackground(page.getBackground());
        txt.setFont(JFaceResources.getTextFont());

        // Fill the page with the text box.
        FormData txtData = new FormData();
        txtData.left = new FormAttachment(0, 2);
        txtData.top = new FormAttachment(0, 2);
        txtData.right = new FormAttachment(100, 0);
        txtData.bottom = new FormAttachment(100, 0);
        txt.setLayoutData(txtData);

        // Set the text.
        MemAppStream helpStream = new MemAppStream();
        AppEnv.getApplication().printHelpHeader(helpStream);
        if (AppEnv.getApplication().getHelpMessageNotes().length > 0) {
            helpStream.println();
            AppEnv.getApplication().printHelpNotes(helpStream);
        }
        helpStream.println();
        AppEnv.getApplication().printHelpCopyright(helpStream);
        txt.setText(helpStream.toString());

        // Add tree item and option page pair.
        categoryPageMap.put(parentItem, page);
        categoryPageMap.put(item, page);
    }

    /**
     * Adds the 'Command line options' option category to the 'Help' category of the option dialog.
     *
     * @param parentItem The help item to add the category tree item to.
     * @param pages The parent control to add option pages to.
     */
    private void addHelpCmdLineCategory(TreeItem parentItem, SashForm pages) {
        // Add sub-item to the 'Help' category in the tree view.
        TreeItem item = new TreeItem(parentItem, SWT.NONE);
        item.setText("Command line options");

        // Add a page for the category.
        final Composite page = new Composite(pages, SWT.BORDER);
        page.setLayout(new FormLayout());

        // The contents of the page is just a text box.
        Text txt = new Text(page, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
        txt.setBackground(page.getBackground());
        txt.setFont(JFaceResources.getTextFont());

        // Fill the page with the text box.
        FormData txtData = new FormData();
        txtData.left = new FormAttachment(0, 2);
        txtData.top = new FormAttachment(0, 2);
        txtData.right = new FormAttachment(100, 0);
        txtData.bottom = new FormAttachment(100, 0);
        txt.setLayoutData(txtData);

        // Set the text.
        MemAppStream helpStream = new MemAppStream();
        AppEnv.getApplication().printHelpHeader(helpStream);
        helpStream.println();
        AppEnv.getApplication().printHelpOptions(helpStream);
        helpStream.println();
        AppEnv.getApplication().printHelpExitCodes(helpStream);
        helpStream.println();
        AppEnv.getApplication().printHelpCopyright(helpStream);
        txt.setText(helpStream.toString());

        // Add tree item and option page pair.
        categoryPageMap.put(item, page);
    }

    /**
     * Adds the 'License information' option category to the 'Help' category of the option dialog.
     *
     * @param parentItem The help item to add the category tree item to.
     * @param pages The parent control to add option pages to.
     */
    private void addHelpLicenseCategory(TreeItem parentItem, SashForm pages) {
        // Add sub-item to the 'Help' category in the tree view.
        TreeItem item = new TreeItem(parentItem, SWT.NONE);
        item.setText("License information");

        // Add a page for the category.
        final Composite page = new Composite(pages, SWT.BORDER);
        page.setLayout(new FormLayout());

        // The contents of the page is just a text box.
        Text txt = new Text(page, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
        txt.setBackground(page.getBackground());
        txt.setFont(JFaceResources.getTextFont());

        // Fill the page with the text box.
        FormData txtData = new FormData();
        txtData.left = new FormAttachment(0, 2);
        txtData.top = new FormAttachment(0, 2);
        txtData.right = new FormAttachment(100, 0);
        txtData.bottom = new FormAttachment(100, 0);
        txt.setLayoutData(txtData);

        // Set the text.
        MemAppStream helpStream = new MemAppStream();
        AppEnv.getApplication().printHelpLicense(helpStream);
        txt.setText(helpStream.toString());

        // Add tree item and option page pair.
        categoryPageMap.put(item, page);
    }

    /** Saves the command line arguments for the selected values of all the options of the option dialog. */
    @SuppressWarnings("rawtypes")
    protected void saveCmdLine() {
        List<String> parts = list();
        List<Option> opts = sortedgeneric(optionGroupMap.keySet(), Option.SORTER);
        for (Option opt: opts) {
            OptionGroup grp = optionGroupMap.get(opt);
            for (String part: grp.getCmdLine()) {
                parts.add(part);
            }
        }
        cmdLine = parts.toArray(new String[0]);
    }
}
