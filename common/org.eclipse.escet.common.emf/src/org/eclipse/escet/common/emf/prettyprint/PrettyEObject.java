//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.emf.prettyprint;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/** An {@link EObject} that should be pretty-printed. */
public class PrettyEObject {
    /** The object being pretty-printed. */
    public final EObject eObj;

    /** Id of the {@link #eObj} object. */
    public final int eObjId;

    /** Name of the class of the {@link #eObj} object. */
    public final String className;

    /** Objects in {@link #eObj} with their Id, for as far as they are defined. */
    public final List<PrettyEFeat> prettyFeats;

    /**
     * Constructor of the {@link PrettyEObject} class.
     *
     * @param eObj The object being pretty-printed.
     * @param eObjId Id of the {@link #eObj} object.
     * @param className Name of the class of the {@link #eObj} object.
     * @param prettyFeats Objects in {@link #eObj} with their Id, for as far as they are defined.
     */
    public PrettyEObject(EObject eObj, int eObjId, String className, List<PrettyEFeat> prettyFeats) {
        this.eObj = eObj;
        this.eObjId = eObjId;
        this.className = className;
        this.prettyFeats = prettyFeats;
    }
}
