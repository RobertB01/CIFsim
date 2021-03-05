//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.typechecker;

/** The kind of a ToolDef object in a {@link CheckerContext}. */
public enum SymbolKind {
    /** Script, imported script, or scoped statement. */
    SCOPE,

    /** ToolDef tool or Java method. */
    TOOL,

    /** Type declaration or type parameter. */
    TYPE,

    /** Variable or ToolDef tool parameter. */
    VALUE;

    @Override
    public String toString() {
        switch (this) {
            case SCOPE:
                return "scope";
            case TOOL:
                return "tool";
            case TYPE:
                return "type";
            case VALUE:
                return "value";
        }
        throw new RuntimeException("Unexpected kind: " + this);
    }
}
