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

package org.eclipse.escet.common.app.framework.appsview.ui;

import static org.eclipse.escet.common.app.framework.appsview.ui.AppsViewConstants.AUTO_EXPAND_PREF_ID;
import static org.eclipse.escet.common.app.framework.appsview.ui.AppsViewConstants.AUTO_REMOVE_PREF_ID;
import static org.eclipse.escet.common.app.framework.appsview.ui.AppsViewConstants.AUTO_TERMINATE_PREF_ID;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.AppStatusLegendCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.AutoExpandCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.AutoRemoveCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.AutoTerminateCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.CommandUtils;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.RemoveAllCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.RemoveCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.TerminateAllCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.TerminateCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.ToolItemCheckCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.commands.ToolItemCommand;
import org.eclipse.escet.common.app.framework.appsview.ui.icons.AppStatusIcons;
import org.eclipse.escet.common.app.framework.management.AppManager;
import org.eclipse.escet.common.app.framework.management.AppManagerData;
import org.eclipse.escet.common.app.framework.management.AppStatus;
import org.eclipse.escet.common.app.framework.management.AppStatusListener;
import org.eclipse.escet.common.eclipse.ui.SelectionListenerBase;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.Bundle;

/** Applications view. */
public class AppsView implements AppStatusListener {
    /** The part of the view. Is {@code null} if not yet or no longer available. */
    private MPart part = null;

    /** Application icons. */
    private final AppStatusIcons icons = new AppStatusIcons();

    /** The tree of the view. Is {@code null} once the view is disposed. */
    private Tree tree;

    /** Mapping from applications to tree items. Is {@code null} once the view is disposed. */
    private Map<Application<?>, TreeItem> treeItems = map();

    /** The tree items to remove when the corresponding application is done with its execution. */
    private Set<TreeItem> itemsToRemove = set();

    /** The toolbar items for this view. */
    private MDirectToolItem[] toolbarItems;

    /** The popup menu items for this view. */
    private MDirectMenuItem[] popupItems;

    /** The contribution URIs of the {@link #toolbarItems} and {@link #popupItems} items that are always enabled. */
    private Set<String> alwaysEnabledContributions = set();

    /** Whether to automatically terminate all running applications, when starting a new application. */
    public AtomicBoolean autoTerminate;

    /** Whether to automatically remove all terminated applications, when starting a new application. */
    public AtomicBoolean autoRemove;

    /** Whether to automatically expand the parent application tree item, when starting a new application. */
    public AtomicBoolean autoExpand;

    /**
     * Constructor for the {@link AppsView} class. Injected as an Eclipse e4 view.
     *
     * @param parent The parent of the view in the GUI.
     * @param part The part that contains the view.
     */
    @Inject
    public AppsView(Composite parent, MPart part) {
        // Store part.
        this.part = part;

        // Put a tree in the view.
        parent.setLayout(new FillLayout());
        tree = new Tree(parent, SWT.MULTI);

        // When the view is disposed, dispose all resources.
        parent.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                AppManager.removeStatusListener(AppsView.this);
                icons.dispose();
                treeItems.clear();
                treeItems = null;
                tree = null;
                toolbarItems = new MDirectToolItem[0];
                popupItems = new MDirectMenuItem[0];
                alwaysEnabledContributions.clear();
                AppsView.this.part = null;
            }
        });

        // Load preferences.
        loadPreferences();

        // Setup toolbar. Uses the preferences to set 'checked' state of
        // toolbar items.
        setupToolbar();

        // Add selection listener to update enabledness of toolbar items.
        tree.addSelectionListener(new SelectionListenerBase() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateEnabled();
            }
        });

        // Update enabledness of toolbar items as initialization of
        // enabledness. Do this as asynchronous callback, to ensure it is
        // queued on the GUI thread, and executed once the view is created,
        // initialized, and is added to the GUI.
        Display.getDefault().asyncExec(() -> { updateEnabled(); });

        // Register status listener with application manager.
        AppManager.addStatusListener(this, true);
    }

    /** Loads the preferences. */
    private void loadPreferences() {
        // Get preferences.
        IPreferenceStore store = PlatformUI.getPreferenceStore();
        String autoTerminatePref = store.getString(AUTO_TERMINATE_PREF_ID);
        String autoRemovePref = store.getString(AUTO_REMOVE_PREF_ID);
        String autoExpandPref = store.getString(AUTO_EXPAND_PREF_ID);

        // Set defaults.
        if (autoTerminatePref.isEmpty()) {
            autoTerminatePref = "true";
        }
        if (autoRemovePref.isEmpty()) {
            autoRemovePref = "true";
        }
        if (autoExpandPref.isEmpty()) {
            autoExpandPref = "true";
        }

        // Store preferences locally.
        autoTerminate = new AtomicBoolean(autoTerminatePref.equals("true"));
        autoRemove = new AtomicBoolean(autoRemovePref.equals("true"));
        autoExpand = new AtomicBoolean(autoExpandPref.equals("true"));
    }

    /** Setup the toolbar of the view. */
    private void setupToolbar() {
        // Add or replace toolbar.
        MToolBar toolbar = MMenuFactory.INSTANCE.createToolBar();
        part.setToolbar(toolbar);

        // Get commands.
        ToolItemCommand[] toolbarCommands = { //
                new TerminateAllCommand(), //
                new AutoTerminateCommand(), //
                new AutoRemoveCommand(), //
                new AutoExpandCommand(), //
        };

        ToolItemCommand[] popupCommands = { //
                new TerminateCommand(), //
                new RemoveCommand(), //
                new RemoveAllCommand(), //
                new AppStatusLegendCommand(), //
        };

        // Add tools to the toolbar.
        toolbarItems = new MDirectToolItem[toolbarCommands.length];
        for (int i = 0; i < toolbarCommands.length; i++) {
            // Create item.
            toolbarItems[i] = MMenuFactory.INSTANCE.createDirectToolItem();
            toolbarItems[i].setEnabled(false);
            toolbarItems[i].setIconURI(toolbarCommands[i].getIconUri());
            toolbarItems[i].setLabel(toolbarCommands[i].getItemLabel());
            toolbarItems[i].setTooltip(toolbarCommands[i].getTooltip());
            toolbarItems[i].setContributionURI(toolbarCommands[i].getContributionUri());
            toolbarItems[i].setType(toolbarCommands[i].getItemType());

            // Set selected status, for checked items.
            if (toolbarItems[i].getType() == ItemType.CHECK) {
                // Set selected status.
                ToolItemCheckCommand checkCommand = (ToolItemCheckCommand)toolbarCommands[i];
                toolbarItems[i].setSelected(checkCommand.isSelected(this));
            }

            // Add item.
            toolbar.getChildren().add(toolbarItems[i]);
        }

        // Add tools to the popup menu.
        popupItems = new MDirectMenuItem[popupCommands.length];
        if (popupCommands.length > 0) {
            // Add popup menu.
            MPopupMenu popupMenu = MMenuFactory.INSTANCE.createPopupMenu();
            popupMenu.getTags().add("ViewMenu");
            part.getMenus().add(popupMenu);

            // Add tools to the popup menu.
            for (int i = 0; i < popupCommands.length; i++) {
                // Create item.
                popupItems[i] = MMenuFactory.INSTANCE.createDirectMenuItem();
                popupItems[i].setEnabled(false);
                popupItems[i].setIconURI(popupCommands[i].getIconUri());
                popupItems[i].setLabel(popupCommands[i].getItemLabel());
                popupItems[i].setTooltip(popupCommands[i].getTooltip());
                popupItems[i].setContributionURI(popupCommands[i].getContributionUri());
                popupItems[i].setType(popupCommands[i].getItemType());

                // Set selected status, for checked items.
                if (popupItems[i].getType() == ItemType.CHECK) {
                    // Set selected status.
                    ToolItemCheckCommand checkCommand = (ToolItemCheckCommand)popupCommands[i];
                    popupItems[i].setSelected(checkCommand.isSelected(this));
                }

                // Add item.
                popupMenu.getChildren().add(popupItems[i]);
            }
        }
    }

    /**
     * Returns the {@link #icons} of the view.
     *
     * @return The {@link #icons} of the view. Is never {@code null}.
     */
    public AppStatusIcons getIcons() {
        return icons;
    }

    /**
     * Returns the {@link #tree} of the view.
     *
     * @return The {@link #tree} of the view. May be {@code null} if no longer available.
     */
    public Tree getTree() {
        return tree;
    }

    /**
     * Returns the {@link #treeItems} of the view.
     *
     * @return The {@link #treeItems} of the view. May be {@code null} if no longer available.
     */
    public Map<Application<?>, TreeItem> getTreeItems() {
        return treeItems;
    }

    @Override
    public void appStatusChanged(AppManagerData data, boolean newApp) {
        // Get display.
        Display display = Display.getDefault();
        if (display.isDisposed()) {
            return;
        }

        // Update on UI thread. Is executed asynchronously, to prevent blocking
        // the application while the view is updated. Although this may
        // theoretically introduce the risk of flooding the UI with a lot of
        // asynchronous calls to execute, the effect seems to be limited in
        // practice, even when executing a simulator repeatedly from a script.
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                // Make sure UI is still available.
                if (display.isDisposed()) {
                    return;
                }
                if (part == null) {
                    return;
                }

                // Add application and/or update status.
                appStatusChangedInternal(data, newApp);
            }
        });
    }

    /**
     * Adds a new application or updates the status of an existing one.
     *
     * @param data The data about the application, and its status.
     * @param newApp Whether the status listener should consider this notification to be about a new application, about
     *     which it has not been previously notified.
     */
    private void appStatusChangedInternal(AppManagerData data, boolean newApp) {
        // Skip if disposed.
        if (treeItems == null) {
            return;
        }

        // See if application already in tree.
        TreeItem item = treeItems.get(data.application);
        if (item == null) {
            // Only add if new application. Otherwise, ignore. That is, don't
            // add orphaned children as root items.
            if (!newApp) {
                return;
            }

            // Determine parent.
            TreeItem parentItem = null;
            if (data.parent != null) {
                parentItem = treeItems.get(data.parent);
            }

            // Add and store new tree item.
            if (parentItem != null) {
                // Add tree item for new child application.
                if (parentItem.isDisposed()) {
                    return;
                }
                item = new TreeItem(parentItem, SWT.NONE);

                // Expand parent if preference set.
                if (autoExpand.get()) {
                    parentItem.setExpanded(true);
                }
            } else {
                // New root application.

                // Get some preferences.
                boolean doTerminate = autoTerminate.get();
                boolean doRemove = autoRemove.get();

                // Terminate all (other) applications if preference set.
                if (doTerminate && !tree.isDisposed()) {
                    TreeItem[] roots = tree.getItems();
                    Set<TreeItem> items = setc(roots.length);
                    for (TreeItem root: roots) {
                        items.add(root);
                    }
                    for (Entry<Application<?>, TreeItem> entry: treeItems.entrySet()) {
                        if (items.contains(entry.getValue())) {
                            AppManager.terminate(entry.getKey());
                        }
                    }
                }

                // Remove terminated applications if preference set.
                if (doRemove && !tree.isDisposed()) {
                    TreeItem[] roots = tree.getItems();
                    for (TreeItem root: roots) {
                        Image icon = root.getImage();
                        AppStatus status = icons.getStatus(icon);
                        if (status.isDone()) {
                            // Remove now.
                            root.dispose();
                        } else if (doTerminate) {
                            // Remove later.
                            itemsToRemove.add(root);
                        }
                    }
                }

                // Add tree item for new root application.
                if (tree.isDisposed()) {
                    return;
                }
                item = new TreeItem(tree, SWT.NONE);
            }
            treeItems.put(data.application, item);

            // Configure new tree item.
            item.setText(data.application.getAppName());
        }

        // Update item.
        if (!item.isDisposed()) {
            if (data.status.isDone() && itemsToRemove.contains(item)) {
                // Scheduled to be removed, when it is done, which it is.
                removeRootItem(item);
            } else {
                // Update icon for (new) status.
                Image icon = icons.getIcon(data.status);
                if (icon != null && !icon.isDisposed()) {
                    item.setImage(icon);
                }
            }
        }

        // Update command enabledness, based on new application/status.
        if (part != null) {
            updateEnabled();
        }
    }

    /** Method invoked when the view gets focus. */
    @Focus
    public void setFocus() {
        if (tree == null || tree.isDisposed()) {
            return;
        }
        tree.setFocus();
    }

    /** Updates the enabledness of the tool items. */
    private void updateEnabled() {
        // If not yet or no longer available in GUI, skip it.
        if (part == null || part.getObject() == null) {
            return;
        }
        if (tree == null || tree.isDisposed()) {
            return;
        }

        // Update for each toolbar item.
        for (int i = 0; i < toolbarItems.length; i++) {
            String contributionUri = toolbarItems[i].getContributionURI();
            boolean enabled = isEnabled(contributionUri);
            if (toolbarItems[i].isEnabled() != enabled) {
                toolbarItems[i].setEnabled(enabled);
            }
        }

        // Update for each popup menu item.
        for (int i = 0; i < popupItems.length; i++) {
            String contributionUri = popupItems[i].getContributionURI();
            boolean enabled = isEnabled(contributionUri);
            if (popupItems[i].isEnabled() != enabled) {
                popupItems[i].setEnabled(enabled);
            }
        }
    }

    /**
     * Is the command for the given contribution URI enabled?
     *
     * @param contributionUri The contribution URI indicating the class that implements the command.
     * @return {@code true} if the command is enabled, {@code false} if not enabled or if it could not be determined
     *     whether the command is enabled.
     */
    private boolean isEnabled(String contributionUri) {
        // Check for always enabled. This is an optimization, to prevent having
        // to use expensive reflection operations.
        if (alwaysEnabledContributions.contains(contributionUri)) {
            return true;
        }

        // Construct URI object, checking for validity.
        URI uri = null;
        try {
            uri = new URI(contributionUri);
        } catch (URISyntaxException ex) {
            logError("Invalid contribution URI.", ex);
            return false;
        }

        // Get OSGi bundle for the plug-in name.
        Bundle bundle;
        try {
            bundle = Platform.getBundle(uri.getHost());
        } catch (IllegalArgumentException ex) {
            String msg = fmt("Can't obtain OSGi bundle \"%s\".", uri.getHost());
            logError(msg, ex);
            return false;
        }

        if (bundle == null) {
            String msg = fmt("OSGi bundle \"%s\" not found.", uri.getHost());
            logError(msg, null);
            return false;
        }

        // Get class name implementing the command.
        String clsName = uri.getPath();
        while (clsName.startsWith("/")) {
            clsName = clsName.substring(1);
        }

        if (clsName.isEmpty()) {
            String msg = fmt("Class name missing in URI \"%s\".", uri);
            logError(msg, null);
            return false;
        }

        // Load class implementing the command.
        Class<?> cls;
        try {
            cls = bundle.loadClass(clsName);
        } catch (ClassNotFoundException ex) {
            String msg = fmt("Class \"%s\" not found.", clsName);
            logError(msg, ex);
            return false;
        }

        // Try to find 'isEnabled' method. We look it up by name, not via the
        // annotation, as Eclipse does.
        Method method;
        try {
            method = cls.getMethod("isEnabled", MPart.class);
        } catch (NoSuchMethodException ex) {
            // No method: always enabled.
            alwaysEnabledContributions.add(contributionUri);
            return true;
        } catch (SecurityException ex) {
            String msg = fmt("Can't obtain \"isEnabled\" method for class \"%s\".", clsName);
            logError(msg, ex);
            return false;
        }

        // Create new instance of class implementing the command.
        Object obj;
        try {
            obj = cls.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException ex) {
            String msg = fmt("Can't instantiate class \"%s\".", clsName);
            logError(msg, ex);
            return false;
        }

        // Invoke 'isEnabled' method.
        Object rslt;
        try {
            rslt = method.invoke(obj, part);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            String msg = fmt("Failed to invoke \"isEnabled\" method of class \"%s\".", clsName);
            logError(msg, ex);
            return false;
        }

        // Return enabledness.
        if (!(rslt instanceof Boolean)) {
            String msg = fmt("Unexpected result for \"isEnabled\" method of class \"%s\".", clsName);
            logError(msg, null);
            return false;
        }

        return (boolean)rslt;
    }

    /**
     * Removes a root item and all its descendants. May only be invoked for root items for which the root and all its
     * descendants are done executing code.
     *
     * @param root The root item to remove.
     */
    public void removeRootItem(TreeItem root) {
        // Remove item from tree.
        Set<TreeItem> removed = set();
        Assert.check(root.getParentItem() == null);
        CommandUtils.collectItems(root, removed);
        root.dispose();

        // Remove items from mapping.
        Iterator<Entry<Application<?>, TreeItem>> iter = treeItems.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Application<?>, TreeItem> entry = iter.next();
            TreeItem item = entry.getValue();
            if (removed.contains(item)) {
                // Remove from tree items mapping.
                iter.remove();

                // Remove from applications marked for removal. If item was
                // not marked for removal, the set is not changed.
                itemsToRemove.remove(item);
            }
        }
    }

    /**
     * Removes root items and all their descendants. May only be invoked for root items for which the root and all their
     * descendants are done executing code.
     *
     * @param roots The root items to remove.
     */
    public void removeRootItems(Set<TreeItem> roots) {
        // Remove items from tree.
        Set<TreeItem> removed = set();
        for (TreeItem root: roots) {
            Assert.check(root.getParentItem() == null);
            CommandUtils.collectItems(root, removed);
            root.dispose();
        }

        // Remove items from mapping.
        Iterator<Entry<Application<?>, TreeItem>> iter = treeItems.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Application<?>, TreeItem> entry = iter.next();
            TreeItem item = entry.getValue();
            if (removed.contains(item)) {
                // Remove from tree items mapping.
                iter.remove();

                // Remove from applications marked for removal. If item was
                // not marked for removal, the set is not changed.
                itemsToRemove.remove(item);
            }
        }
    }

    /**
     * Log an error to the Eclipse error log.
     *
     * @param msg The error message.
     * @param ex The exception that occurred, or {@code null} if not applicable.
     */
    private void logError(String msg, Exception ex) {
        msg += " Please report this to the Eclipse ESCET development team.";
        String pluginName = getClass().getName();
        Status status = new Status(IStatus.ERROR, pluginName, IStatus.OK, msg, ex);
        StatusManager.getManager().handle(status, StatusManager.LOG);
    }
}
