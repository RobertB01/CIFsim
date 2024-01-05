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

/** Position information during execution. */
public class PositionData {
    /** Name of the definition being executed. */
    public final String defName;

    /** Kind of definition being executed (model, process, or function). */
    public final DefinitionKind defKind;

    /** Line number of the statement being executed, negative if not set. */
    public int statementPos;

    /**
     * Constructor of the {@link PositionData} class.
     *
     * @param defKind Kind of definition being executed.
     * @param defName Name of the definition being executed.
     */
    public PositionData(DefinitionKind defKind, String defName) {
        this.defKind = defKind;
        this.defName = defName;
        statementPos = -1;
    }

    /**
     * Return a human-readable description of the position denoted by the object.
     *
     * @return A human-readable description of the position.
     */
    public String getPosition() {
        String text = this.defKind.name + " \"" + this.defName + "\"";
        if (this.statementPos > 0) {
            text += " near line " + String.valueOf(this.statementPos);
        }
        return text;
    }
}
