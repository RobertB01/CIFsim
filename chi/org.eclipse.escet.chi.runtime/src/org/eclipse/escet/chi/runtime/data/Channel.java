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

package org.eclipse.escet.chi.runtime.data;

import org.eclipse.escet.chi.runtime.SelectChoice;
import org.eclipse.escet.common.java.removablelist.RemovableElement;
import org.eclipse.escet.common.java.removablelist.RemovableList;

/** Representation of a channel at runtime. */
public class Channel extends RemovableElement<Channel> {
    /** Senders on the channel. Note that their guards may be {@code false}. */
    public RemovableList<SelectChoice> senders = new RemovableList<>();

    /** Receivers on the channel. Note that their guards may be {@code false}. */
    public RemovableList<SelectChoice> receivers = new RemovableList<>();
}
