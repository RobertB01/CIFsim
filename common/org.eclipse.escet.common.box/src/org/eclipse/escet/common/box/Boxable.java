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

package org.eclipse.escet.common.box;

/** Interface that all classes that can be converted to a {@link Box} representation should implement. */
public interface Boxable {
    /**
     * Returns a {@link Box} representation of this object.
     *
     * @return A {@link Box} representation of this object.
     */
    public Box toBox();
}
