//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.subplant;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.java.Assert;

/**
 * Class that stores the constructed partial specification. It also handles the details of adding new elements, as well
 * as keeping an administration of copied and missing elements.
 */
public class PartialSpecManager {
    /** Root of the partial specification being constructed. */
    private final Specification partialSpec;

    /**
     * Original objects with their partial copied objects.
     */
    private Map<EObject, EObject> copiedObjects = map();

    /**
     * Constructor of the {@link PartialSpecManager} class.
     *
     * @param origSpec Original specification to partially copy.
     */
    public PartialSpecManager(Specification origSpec) {
        this.partialSpec = newSpecification();
        addCopiedObject(origSpec, partialSpec);
    }

    /**
     * State that an original object has been copied to a partial object.
     *
     * @param originalObj The original object.
     * @param partialObj The (copied) partial object.
     */
    public void addCopiedObject(EObject originalObj, EObject partialObj) {
        EObject prevObj = copiedObjects.put(originalObj, partialObj);

        // Don't allow deleting a previously created value.
        Assert.check(prevObj == null, "Duplicate copy of " + originalObj);
    }

    /**
     * Get the constructed partial specification.
     *
     * @return The constructed partial specification
     */
    public Specification getPartialSpec() {
        return partialSpec;
    }
}
