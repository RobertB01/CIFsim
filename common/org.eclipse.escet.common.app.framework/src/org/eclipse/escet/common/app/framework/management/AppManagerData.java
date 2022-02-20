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

package org.eclipse.escet.common.app.framework.management;

import org.eclipse.escet.common.app.framework.Application;

/** Data about a single application, used by the {@link AppManager}. */
public class AppManagerData {
    /** The application. */
    public final Application<?> application;

    /** The parent application, or {@code null} if not available. */
    public final Application<?> parent;

    /** The status of the application. */
    public AppStatus status;

    /**
     * Constructor for the {@link AppManagerData} class.
     *
     * @param application The application.
     * @param parent The parent application, or {@code null} if not available.
     */
    public AppManagerData(Application<?> application, Application<?> parent) {
        this.application = application;
        this.parent = parent;
        this.status = AppStatus.STARTING;
    }

    /**
     * Creates a copy of this data. As the data is modified in-place by the application manager, status listeners get a
     * copy of the data.
     *
     * @return A copy of this data.
     */
    public AppManagerData copy() {
        AppManagerData dataCopy = new AppManagerData(application, parent);
        dataCopy.status = this.status;
        return dataCopy;
    }
}
