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

package org.eclipse.escet.cif.cif2cif;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.java.CifWalker;

/** In-place transformation that removes annotations. */
public class RemoveAnnotations extends CifWalker implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        walkSpecification(spec);
    }

    @Override
    protected void preprocessAnnotatedObject(AnnotatedObject annotatedObj) {
        annotatedObj.getAnnotations().clear();
    }
}
