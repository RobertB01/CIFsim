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

package org.eclipse.escet.common.eclipse.ui;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.osgi.framework.Bundle;

/** Wizard to create a new project, with the contents of certain directories of a plug-in copied to it. */
public abstract class CopyFilesNewProjectWizard extends Wizard implements INewWizard {
    /** New project creation page, or {@code null} if not yet created. */
    private WizardNewProjectCreationPage page;

    /**
     * Returns the initial suggestion for the project name.
     *
     * @return The initial suggestion for the project name.
     */
    protected abstract String getInitialProjectName();

    /**
     * Returns a mapping of plug-in relative paths to copy and the project relative path to which to copy them. Use '/'
     * as path separators. E.g. 'examples/example1' mapping to 'example1'.
     *
     * @return The mapping.
     */
    protected abstract Map<String, String> getPathsToCopy();

    /**
     * Returns the plug-in, in which to locate the plug-in relative paths.
     *
     * @return The plug-in, in which to locate the plug-in relative paths.
     */
    protected abstract Plugin getPlugin();

    /**
     * Returns the configuration element for this wizard, from the plug-in extension.
     *
     * @return The configuration element for this wizard.
     */
    private IConfigurationElement getWizardConfig() {
        // Get configuration elements for wizards.
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        String extensionPointId = "org.eclipse.ui.newWizards";
        IConfigurationElement[] elems;
        elems = registry.getConfigurationElementsFor(extensionPointId);

        // Find the configuration element for this wizard.
        String wizardClsName = getClass().getName();
        for (IConfigurationElement elem: elems) {
            // Look for 'wizard' elements.
            if (!elem.getName().equals("wizard")) {
                continue;
            }

            // Make sure it has a 'class' matching this wizard class.
            String elemClsName = elem.getAttribute("class");
            if (elemClsName == null) {
                continue;
            }
            if (!elemClsName.equals(wizardClsName)) {
                continue;
            }

            // Configuration element found.
            return elem;
        }

        // No matching configuration element found.
        String msg = fmt("Configuration element for wizard \"%s\" not found.", wizardClsName);
        throw new RuntimeException(msg);
    }

    /**
     * Obtains the title of the wizard page, from the plug-in's extension configuration. The 'name' of the 'wizard'
     * element of the 'org.eclipse.ui.newWizards' extension is used for this.
     *
     * @return The title of the wizard page.
     */
    private String getTitle() {
        String clsName = getClass().getName();

        // Get wizard configuration element.
        IConfigurationElement elem = getWizardConfig();

        // Get name.
        String name = elem.getAttribute("name");
        if (name == null) {
            String msg = fmt("Wizard \"%s\" has no name.", clsName);
            throw new RuntimeException(msg);
        }
        return name;
    }

    /**
     * Obtains the description of the wizard page, from the plug-in's extension configuration. The text of the
     * 'description' element 'wizard' element of the 'org.eclipse.ui.newWizards' extension is used for this.
     *
     * @return The description of the wizard page.
     */
    private String getDescription() {
        String clsName = getClass().getName();

        // Get wizard configuration element.
        IConfigurationElement elem = getWizardConfig();

        // Get the 'description' child.
        IConfigurationElement[] children = elem.getChildren("description");
        if (children.length == 0) {
            String msg = fmt("Wizard \"%s\" has no description.", clsName);
            throw new RuntimeException(msg);
        }
        if (children.length > 1) {
            String msg = fmt("Wizard \"%s\" has multiple descriptions.", clsName);
            throw new RuntimeException(msg);
        }

        // Get the actual description.
        String descr = children[0].getValue();
        if (descr == null) {
            String msg = fmt("Wizard \"%s\" has empty description.", clsName);
            throw new RuntimeException(msg);
        }
        return descr;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        // Create and add wizard page, to select project name and location.
        String pageName = getClass().getSimpleName();
        page = new MyNewProjectCreationPage(pageName);
        page.setTitle(getTitle());
        page.setDescription(getDescription());
        page.setInitialProjectName(getInitialProjectName());
        addPage(page);
    }

    /**
     * Modified {@link WizardNewProjectCreationPage} that performs an additional {@link #validatePage} once the page is
     * made visible, to ensure that if the initial suggested project name already exists, the error message is properly
     * shown. Workaround for Eclipse bug 430516: <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=430516"
     * >https://bugs.eclipse.org/bugs/show_bug.cgi?id=430516</a>.
     */
    private static class MyNewProjectCreationPage extends WizardNewProjectCreationPage {
        /**
         * Constructor for the {@link MyNewProjectCreationPage} class.
         *
         * @param pageName The name of this page.
         */
        public MyNewProjectCreationPage(String pageName) {
            super(pageName);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if (visible) {
                validatePage();
            }
        }
    }

    @Override
    public boolean performFinish() {
        try {
            // Run wizard.
            IRunnableWithProgress operation = new CreateProjectOperation();
            getContainer().run(false, true, operation);
        } catch (InterruptedException ex) {
            // Cancelled.
        } catch (Exception ex) {
            // Log crash.
            String name = getClass().getName();
            Status status = new Status(IStatus.ERROR, name, IStatus.OK, name + " failed.", ex);
            getPlugin().getLog().log(status);
        }

        return true;
    }

    /** Workspace modification operation for {@link CopyFilesNewProjectWizard}. */
    private final class CreateProjectOperation extends WorkspaceModifyOperation {
        @Override
        public void execute(IProgressMonitor monitor) throws InterruptedException {
            try {
                // Start task.
                SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
                subMonitor.subTask("Creating new project...");

                // Make sure the project does not yet exist in the workspace
                // (regardless of whether it is opened or closed). Wizard
                // should already have checked it though.
                subMonitor.split(10);
                String projectName = page.getProjectName();
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IProject project = workspace.getRoot().getProject(projectName);
                Assert.check(!project.exists());

                // Check whether the directory already exists on disk.
                subMonitor.split(10);
                IPath projectParent = page.getLocationPath();
                Path projectPath = Paths.get(projectParent.toOSString()).resolve(projectName);
                if (Files.exists(projectPath)) {
                    Shell shell = page.getShell();
                    String title = "Directory already exists";
                    String msg = fmt(
                            "Directory \"%s\" already exists. Are you sure you want to create a new "
                                    + "project in that directory? Any existing files will be overwritten.",
                            projectPath);
                    if (!MessageDialog.openQuestion(shell, title, msg)) {
                        return;
                    }
                }

                // Create directory on disk, including any parents.
                subMonitor.split(10);
                Files.createDirectories(projectPath);

                // Copy files.
                copyFiles(projectPath, subMonitor.split(50));

                // Create project in Eclipse workspace.
                if (projectParent.equals(workspace.getRoot().getLocation())) {
                    project.create(subMonitor.split(10));
                } else {
                    IProjectDescription desc = workspace.newProjectDescription(project.getName());
                    desc.setLocation(new org.eclipse.core.runtime.Path(projectPath.toString()));
                    project.create(desc, subMonitor.split(10));
                }

                // Open project.
                project.open(subMonitor.split(10));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CoreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Copy the files from the plug-in to the location of the new project.
     *
     * @param projectPath The local file system path for the root of the new project.
     * @param monitor The progress monitor.
     * @throws IOException In case of an I/O error.
     */
    protected void copyFiles(Path projectPath, IProgressMonitor monitor) throws IOException {
        Bundle bundle = getPlugin().getBundle();
        String projectPathSeparator = projectPath.getFileSystem().getSeparator();

        Map<String, String> entriesToCopy = getPathsToCopy();
        SubMonitor subMonitor = SubMonitor.convert(monitor, entriesToCopy.entrySet().size());
        for (Entry<String, String> entryToCopy: entriesToCopy.entrySet()) {
            subMonitor.split(1);
            String rootPathToCopy = entryToCopy.getKey();
            String targetRootPath = entryToCopy.getValue();
            Assert.check(!rootPathToCopy.startsWith("/"));
            Assert.check(!targetRootPath.startsWith("/"));
            Assert.check(!rootPathToCopy.contains("\\"));
            Assert.check(!targetRootPath.contains("\\"));
            String localTargetRootPath = targetRootPath.replace("/", projectPathSeparator);

            Enumeration<URL> urlsToCopy = bundle.findEntries(rootPathToCopy, "*", true);
            while (urlsToCopy.hasMoreElements()) {
                // Skip directories.
                URL urlToCopy = urlsToCopy.nextElement();
                if (urlToCopy.getPath().endsWith("/")) {
                    continue;
                }

                // Get local file system relative path.
                String pathToCopy = urlToCopy.getPath();
                Assert.check(pathToCopy.startsWith("/" + rootPathToCopy));
                String relPath = pathToCopy.substring(1 + rootPathToCopy.length());
                while (relPath.startsWith("/")) {
                    relPath = relPath.substring(1);
                }
                String localRelPath = relPath.replace("/", projectPathSeparator);

                // Determine target path.

                Path targetPath = projectPath.resolve(localTargetRootPath).resolve(localRelPath);

                // Create ancestry directories on disk.
                Path parentPath = targetPath.getParent();
                Files.createDirectories(parentPath);

                // Copy file.
                try (InputStream istream = new BufferedInputStream(urlToCopy.openStream());
                     OutputStream ostream = new BufferedOutputStream(new FileOutputStream(targetPath.toFile())))
                {
                    IOUtils.copy(istream, ostream);
                }
            }
        }
    }
}
