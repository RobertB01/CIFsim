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

package org.eclipse.escet.common.app.framework.console;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/** Console terminate action that can be used to terminate the running {@link Application}. */
public class ConsoleTerminateAction extends Action {
    /** The console related to this action. */
    private final Console console;

    /**
     * Constructor for the {@link ConsoleTerminateAction} class.
     *
     * @param console The console related to this action.
     */
    public ConsoleTerminateAction(Console console) {
        super("Terminate");
        this.console = console;
        setToolTipText("Terminate");

        // Add icon image, with lazy loading.
        Bundle bundle = FrameworkUtil.getBundle(getClass());
        setImageDescriptor(ImageDescriptor.createFromURLSupplier(false, () -> {
            URL imageUrl = FileLocator.find(bundle, new Path("icons/terminate_button.png"));
            Assert.notNull(imageUrl);
            return imageUrl;
        }));
    }

    @Override
    public void run() {
        Application<?> application = console.getApplication();
        if (application != null) {
            application.terminate();
        }
    }
}
