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

package org.eclipse.escet.common.svg.selector;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;

/** The type of a {@link MouseEvent}. */
public enum MouseEventType {
    /** {@link MouseListener#mouseDown} mouse event type. */
    DOWN,

    /** {@link MouseListener#mouseUp} mouse event type. */
    UP,

    /** {@link MouseMoveListener#mouseMove} mouse event type. */
    MOVE,

    /** {@link MouseListener#mouseDoubleClick} mouse event type. */
    DOUBLE_CLICK;
}
