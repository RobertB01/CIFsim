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

package org.eclipse.escet.common.app.framework.management;

/** Application status listener. */
public interface AppStatusListener {
    /**
     * Notifies the status listeners of a status change. Is also invoked for new applications (with status
     * {@link AppStatus#STARTING}).
     *
     * @param data The data about the application, and its status. This is a copy of the data maintained by the
     *     application manager, and won't be changed by the application manager on future status changes.
     * @param newApp Whether the status listener should consider this notification to be about a new application, about
     *     which it has not been previously notified.
     */
    public void appStatusChanged(AppManagerData data, boolean newApp);
}
