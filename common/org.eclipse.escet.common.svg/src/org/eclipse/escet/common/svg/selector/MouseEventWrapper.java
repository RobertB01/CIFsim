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

package org.eclipse.escet.common.svg.selector;

import org.eclipse.swt.events.MouseEvent;

/** Wrapper that contains a {@link MouseEvent} and its {@link MouseEventType}. */
public class MouseEventWrapper {
    /** The mouse event. */
    public final MouseEvent event;

    /** The type of the mouse event. */
    public final MouseEventType type;

    /**
     * Constructor for the {@link MouseEventWrapper} class.
     *
     * @param event The mouse event.
     * @param type The type of the mouse event.
     */
    public MouseEventWrapper(MouseEvent event, MouseEventType type) {
        this.event = event;
        this.type = type;
    }
}
