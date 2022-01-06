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

package org.eclipse.escet.common.eclipse.ui;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * Base implementation of {@link SelectionListener} that redirects {@link #widgetDefaultSelected} to
 * {@link #widgetSelected}. Derived classes need only implement {@link #widgetSelected}.
 */
public abstract class SelectionListenerBase implements SelectionListener {
    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        widgetSelected(e);
    }
}
