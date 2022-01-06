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

package org.eclipse.escet.common.app.framework.output;

/** Output modes for the output components. Indicates what kind of information should be outputted. */
public enum OutputMode {
    /** Output only errors. */
    ERROR,

    /** Output errors as well as warnings. */
    WARNING,

    /**
     * Output errors, warnings, and all 'normal' output. Applications may themselves choose what 'normal' output means.
     */
    NORMAL,

    /** Output everything, including errors, warnings, 'normal' output, and debugging information. */
    DEBUG;
}
