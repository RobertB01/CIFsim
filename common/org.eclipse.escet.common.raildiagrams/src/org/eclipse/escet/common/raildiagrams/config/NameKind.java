//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.config;

/** Available kinds of nodes. */
public enum NameKind {
    /** Name represents a fixed terminal, like 'colon'. */
    TERMINAL("terminal"),

    /** Name represents a variable terminal, like 'number', or 'identifier'. */
    META_TERMINAL("meta-terminal"),

    /** Name represents another railroad diagram. */
    NON_TERMINAL("nonterminal"),

    /** Name represents a branch label. */
    LABEL("branch-label"),

    /** Name is a header of a diagram. */
    HEADER("diagram-header");

    /** Prefix to use for the name kind in the configuration file. */
    public final String configPrefix;

    /**
     * Constructor of the {@link NameKind} enumeration.
     *
     * @param configPrefix Prefix to use for the name kind in the configuration file.
     */
    private NameKind(String configPrefix) {
        this.configPrefix = configPrefix;
    }
}
